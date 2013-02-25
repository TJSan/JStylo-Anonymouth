package edu.drexel.psal.anonymouth.projectDev;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import edu.drexel.psal.anonymouth.utils.Pair;
import edu.drexel.psal.jstylo.generics.Logger;

/**
 * Extracts targets 
 * @author Andrew W.E. McDonald
 *
 */
public class TargetExtractor {
	
	private static ArrayList<Integer> previousInitialization;
	private int numMeans;
	private int numAuthors;
	private int additionalPartitions = 0;
	//private double[] thisFeature;
	private double min;
	private double max;
	private double spread;
	private int numPartitions; // same as number of clusters (1 partition == 1 cluster)
	private boolean isFinished;
	private double avgAbsDev;
	private double authorAvg;
	private double authorStdDev;
	private double authorMin;
	private double authorMax;
	private Cluster targetCluster;
	private boolean targetSet=false;
	private double targetValue;
	private double presentValue;
	private double targetCent;
	private double targetDev;
	ArrayList<Cluster> thisFeaturesClusters; 
	private ArrayList<String> trainTitlesList;
	private Pair[] thePairs;
	private boolean maxCentroidsFound = false;
	private String featName;
	
	/**
	 * Constructor
	 * @param numAuthors the number of authors (not including the user). This defines the starting number of centroids
	 * @param attrib the Attribute to extract a target for
	 */
	public TargetExtractor(int numAuthors, Attribute attrib){//, boolean usePreviousInitialization){
		this.featName = attrib.getConcatGenNameAndStrInBraces();
		Logger.logln("In TargetExtractor extracting targets for "+featName);
		this.trainTitlesList = DocumentMagician.getTrainTitlesList();
		this.numAuthors = numAuthors;
		this.numMeans = numAuthors;
		double[] thisFeature = attrib.getTrainVals();
		int lenThisFeature = thisFeature.length;
		int i=0;
		this.thePairs = new Pair[lenThisFeature];
		for(i=0;i<lenThisFeature;i++){
			thePairs[i] = new Pair(trainTitlesList.get(i),thisFeature[i]);
		}
		this.min = attrib.getTrainMin();
		this.max = attrib.getTrainMax();
		this.spread = max-min;
		isFinished = false;
		this.authorAvg = attrib.getAuthorAvg();
		this.authorStdDev = attrib.getAuthorStdDev();
		this.presentValue = attrib.getToModifyValue();
		//System.out.println("The Min is: "+min+" and the Max is: "+max+" the spread is: "+spread);
		
	}
	
	/**
	 * Empty constructor for testing purposes
	 */
	public TargetExtractor(){
		thisFeaturesClusters = new ArrayList<Cluster>();
	}
	
	/**
	 * Implementation of k-means++ initialization (seeding) algorithm for k-means
	 * @param thisFeature
	 */
	public void kPlusPlusPrep(){//double[] thisFeature){ // parameter just for testing
		previousInitialization = new ArrayList<Integer>();
		thisFeaturesClusters.clear();
		int numFeatures = thePairs.length;
		MersenneTwisterFast mtfGen = new MersenneTwisterFast();
		int firstPick = mtfGen.nextInt(numFeatures);
		thisFeaturesClusters.add(0,new Cluster(thePairs[firstPick].value));
		previousInitialization.add(firstPick);
		int numClusters = thisFeaturesClusters.size();
		int i =0;
		int j = 0;
		int k = 0;
		double smallestDSquared = Integer.MAX_VALUE; // large initial value
		double tempSmallestDSquared;
		double currentValue;
		double currentCentroid;
		double[] dSquaredRay = new double[numFeatures];
		double dSquaredRaySum = 0;
		double[] probabilities = new double[numFeatures];
		double randomChoice;
		boolean notFound = true;
		boolean tooManyTries = false;
		Set<Double> skipSet = new HashSet<Double>();
		for(i=0;i<numMeans-1;i++){
			for(j=0;j<numFeatures;j++){
				currentValue = thePairs[j].value;
				smallestDSquared = Integer.MAX_VALUE;
				for(k=0;k<numClusters;k++){
					currentCentroid = thisFeaturesClusters.get(k).getCentroid();
					tempSmallestDSquared = (currentValue-currentCentroid)*(currentValue-currentCentroid);
					if(tempSmallestDSquared < smallestDSquared)
						smallestDSquared = tempSmallestDSquared;
				}
				dSquaredRay[j]=smallestDSquared;
			}
			dSquaredRaySum = 0;
			for(k=0;k<numFeatures;k++)
				dSquaredRaySum=dSquaredRaySum+dSquaredRay[k];
			if(dSquaredRaySum== 0){ 
				maxCentroidsFound = true; 
				numMeans = i+1; // add one because it starts counting from '0', and even if every document has the same value for this feature, there 
				// will still be one centroid (if ALL values are the same, this will break out of the loop at i==0)
				break;
			}
			for(k=0;k<numFeatures;k++)
				probabilities[k]=dSquaredRay[k]/dSquaredRaySum;
			
			notFound = true;
			
			
			ArrayList<Double> badRandomsTesting = new ArrayList<Double>();
			double thisProb = 0;
			while(notFound == true){
				randomChoice = mtfGen.nextDouble(true,true);
				thisProb = 0;
				for(k=0;k<numFeatures;k++){
					thisProb = thisProb +probabilities[k];
					if((randomChoice <= thisProb) && (!skipSet.contains(thePairs[k].value))){
						thisFeaturesClusters.add(new Cluster(thePairs[k].value));
						skipSet.add(thePairs[k].value);
						previousInitialization.add(k);
						notFound = false;
						break;
					}
				}
				if(notFound == true){	
					if(skipSet.size() > 10000){
						Logger.logln("kPlusPlusPrep reached 10k tries.");
						tooManyTries = true;
						break;
					}
					Set<Double> cSkipSet = new HashSet<Double>(skipSet);
					int preSize = cSkipSet.size();
					for(k=0;k<numFeatures;k++){
						cSkipSet.add(thePairs[k].value);
					}
					int postSize = cSkipSet.size();
					if(preSize == postSize){
						maxCentroidsFound = true;
						numMeans = thisFeaturesClusters.size();
					}
					badRandomsTesting.add(randomChoice);
					//System.out.println("Size of 'thisFeaturesClusters' => "+thisFeaturesClusters.size());
					//System.out.println("size of 'skipList' => "+skipSet.size());
					//System.out.println("numFeatures' => "+numFeatures);
					//System.out.print("The probablities are => ");
					//for(int l=0; l<numFeatures;l++)
					//	System.out.print(probabilities[l]+", ");
					//System.out.println();
					//System.out.println("Random numbers chosen: "+badRandomsTesting.toString());
				}
				if(maxCentroidsFound==true || tooManyTries == true)
					break;
			}
			if(maxCentroidsFound==true)
				break;
			if(tooManyTries == true){
				Logger.logln("Calling kPlusPlusPrep again from within itself.");
				kPlusPlusPrep();
				break;
			}
			
		}
		//System.out.println("Number of centroids after kmeans++ aglorithm: "+thisFeaturesClusters.size());
		
	}
	
	/**
	 * 
	 * Initializes the clustering algorithm by evenly spacing 'numMeans' centroids between the features [min,max],
	 * and assigns features to partitions based upon Euclidean distance from centroids (single dimension)
	 */
	public void initialize(){
		Logger.logln("Intitializing Clustering, will call kPlusPlusPrep.");
		kPlusPlusPrep();
		Logger.logln("kPlusPlusPrep seems to have done its job. Moving on.");
		//System.out.println("Initialized with k-means++....");
		int i;
		int j;
		double[] temp = new double[2];// temp[0] <=> parition number && temp[1] <=> difference value
		int partitionToGoTo;
		
		numPartitions = numMeans;
		/*
		double interval = spread/numPartitions; // one mean at each interval, begin with 'numAuthors-2' intervals... will be changed as needed.
		double currentCentroid = min+interval/2;
		// create clusters and initialize centroids
		for(i=0; i<numPartitions;i++){
			//System.out.println("The centroid for partition: "+i+" is: "+currentCentroid);
			thisFeaturesClusters.add(i,new Cluster(currentCentroid));
			currentCentroid += interval;
			
		}*/	
		// create list of all centroids
		double[] allCentroids = getAllCentroids();
	
		// Initialize cluster element sets based on distance from each centroid
		//System.out.println(numPartitions+", "+thePairs.length);
		double[][] differences = new double[numMeans][thePairs.length];
		for(i=0;i<numMeans;i++){
			double  tempCentroid = allCentroids[i];
			for(j=0;j<thePairs.length;j++){
				//TODO: squared??
				differences[i][j] =Math.abs(thePairs[j].value-tempCentroid); 
			}
		}
		for(i=0;i<differences[0].length;i++){//differences array's columns (correspond to 'thisFeature' indices (feature events per document)
			j=0;
			temp[0] = j;
			temp[1] = differences[j][i];
			//System.out.println("differences[0].length == "+differences[0].length+" and differences.length == "+differences.length);
			for(j=1;j<differences.length;j++){// differences array's rows (correspond to 'thisFeaturesClusters' cluster indices)
				if (temp[1]>differences[j][i]){
					temp[0] = j;
					temp[1] =differences[j][i];
				}
			}
			partitionToGoTo = (int)temp[0];
			thisFeaturesClusters.get(partitionToGoTo).addElement(thePairs[i]);
		}
		Logger.logln("Initial positions for elements found. Updating Centroids.");
		updateCentroids();
		
	}
	
	
	/**
	 * Updates the centroids to be the average of the values contained within their respective partitions. 
	 */
	public void updateCentroids(){
		Logger.logln("Begin updating centroids.");
		// update centroids to be the averages of their respective element lists
		int i=0;
		int j = 0;
		for(i=0;i<numMeans;i++){
			double sum= 0;
			double avg = 0;
			int count = 0;
			Pair[] someElements = thisFeaturesClusters.get(i).getElements();
			int someElementsLen = someElements.length;
			//System.out.println("someElements is: "+someElements.toString()+" and the centroid is: "+thisFeaturesClusters.get(i).getCentroid());
			for(j=0; j<someElementsLen;j++){
			//System.out.println("the values in partition "+i+" are:");
				double temp = someElements[j].value;
				//sum+=(double)someIter.next();
				sum+=temp;
				//System.out.print("  "+temp+"  ");
				count += 1;
			}
			//if (count > 2){
				avg = sum/count;
				thisFeaturesClusters.get(i).updateCentroid(avg);
			//}
			//else{
			//	numMeans--;
			//	System.out.println("numMeans: "+numMeans);
			//	if(numMeans < 2){
			//		additionalPartitions++;
			//		numMeans = numAuthors+additionalPartitions;
			//		Scanner in = new Scanner(System.in);
			//		in.next();
			//	}
			//	thisFeaturesClusters.clear();
				
			//	initialize();
			//}
		}
		// Once all centroids have been updated, re-organize
		Logger.logln("Updating centroids complete, will reOrganize");
		reOrganize();
	}
	
	public double[] getAllCentroids(){
		int i;
		int numClusters = thisFeaturesClusters.size();
		double[] allCentroids = new double[numClusters];
		for(i=0;i<numClusters;i++)
			allCentroids[i] = thisFeaturesClusters.get(i).getCentroid();
		return allCentroids;
	}
	
	
	/**
	 * Moves the features to their new nearest centroids
	 */
	public void reOrganize(){
		Logger.logln("Starting reOrganize");
		// need to go through all elements, extract data, and check distance agains new centroids
		// create list of all centroids
		int i;
		int j;
		int k;
		int m;
		double[] temp = new double[2];// index '0' holds the centroid number that corresponds to the difference value in index '1'
		int bestCentroid;
		boolean movedElement = false;
		//System.out.println("all centroids: "+getAllCentroids().toString());
		//TODO: maybe there is a better way to go about this than casting from Object to Double
		double[] allCentroids = getAllCentroids();
		double[] diffs = new double[allCentroids.length];
		Pair[] elementHolder;
		for(i=0;i<numMeans;i++){// for each cluster
			elementHolder = thisFeaturesClusters.get(i).getElements(); //get the element list, and change it to a Double[] (from ArrayList<Double>)
			for(j=0;j<elementHolder.length;j++){
				for(k=0;k<numMeans;k++){
					//TODO: squared??
					diffs[k] = Math.abs(elementHolder[j].value-(Double)allCentroids[k]);
				}
				temp[0]=0;
				temp[1]=diffs[0];
				for(m=1;m<diffs.length;m++){
					if(temp[1]>diffs[m]){
						temp[0]=m;
						temp[1]=diffs[m];
					}
				}
				bestCentroid = (int)temp[0];
				if(!  (bestCentroid  ==   i)   ){// if a more fitting centroid was found for the element in question...
					thisFeaturesClusters.get(i).removeElement((Pair)elementHolder[j]);
					thisFeaturesClusters.get(bestCentroid).addElement((Pair)elementHolder[j]);
					movedElement = true;
				}
				
			}
		}
		//Scanner in = new Scanner(System.in);
		boolean noProblems = true;
		if(movedElement == false ){
			Logger.logln("Elements stopped moving - algorithm converged.");
			int numClusters = thisFeaturesClusters.size();
			if(numClusters < 2 && maxCentroidsFound == false){
				additionalPartitions++;
				numMeans = numAuthors+additionalPartitions;
				//Iterator<Cluster> clusterIter = thisFeaturesClusters.iterator();
				//while(clusterIter.hasNext())
					//System.out.println(clusterIter.next().getElements().toString());
				Logger.logln("Less than two Clusters. Will restart with '"+numMeans+"' means. Enter a character.");
				noProblems = false;
				
			}
			else{
				//System.out.println("Size of 'thisFeaturesClusters' (numClusters): "+numClusters);
				for(i=0;i<numClusters;i++){
					//System.out.println("moved element == false, num clusters > 2, index (i) == '"+i+"'");
					if(thisFeaturesClusters.get(i).getElements().length < 3 && maxCentroidsFound == false){
						numMeans--;
						
						//Iterator<Cluster> clusterIter = thisFeaturesClusters.iterator();
						//while(clusterIter.hasNext())
						//	System.out.println(clusterIter.next().getElements().toString());
						Logger.logln("Cluster '"+i+"' has less than 3 elements. Will restart with '"+numMeans+"' means. Enter a character.");
						noProblems = false;
						break;
					}
				}
			}
			if(noProblems == true){
				Logger.logln("All is well, clustering complete.");
				isFinished=true;
			}
			else{
				//in.next();
				noProblems = true;
				thisFeaturesClusters.clear();
				initialize();
			}
		}
		else{
			Logger.logln("Updating Centroids... something moved");
			updateCentroids();
		}
		
	}

	/**
	 * @deprecated
	 * The best cluster to move the feature to is randomly chosen within a range based upon how populated other clusters are (the more populated the better), and the distance a cluster is from the user's sample average and std. devation,
	 * basically. If a highly populated cluster (at lease the average number of elements per cluster, minus the average absolute deviation) that falls outside of the user's sample features norm is not found, 
	 * the user's 'present value' (document to modify's value) is left untouched. The target value is then set to minus 1 (-1), and is later set to the present value of the feature.
	 */
	public void findOptimalCluster(){
		// 1) the target location of the feature MUST lie outside of the range of (authorAverage +/- one standard deviation - 'mu' will be the multiplier.)
		// 2) ... 
		// 3) The target cluster must have at least (averageNumElementsPerCluster - averageAbsoluteDeviation) elements in it (in order to not move a feature to an 'outlier' location)
		// 4) Once a suitable cluster has been found, randomly pick a location for the target value of the feature within the range ( centroid +/- average absolute deviation) to further minimize the possibility that many
		// features will end up targeted to the exact location of the centroid, which would suggest JAM's involvement (even though this is highly unlikely to happen). 
		// 5) In the case that one or more of these conditions cannot be met, the feature will be left as it is - as there would (probably) be little benefit from (and possibly some harm done by) 
		// changing the feature's value.
		int i=0;
		double sum = 0;
		double avgElementsPerCluster;
		double clusterAvgAbsDev;
		int minElements;
		double muCheck;
		double	tempClustCent;
		double tempClustDev;
		double clustMin;
		double clustMax;
		double[] potentialTarget = new double[3]; // potentialTarget[0] <=> cluster number , potentialTarget[1] <=> mu[cluster][0] (min), potentialTarget[2] <=> mu[cluster][1] (max)
		int numClusters = thisFeaturesClusters.size();
		double[][] mu = new double[numClusters][2]; // mu[clusterNumber][0] <=> coefficient of StdDev for minimums, mu[clusterNumber][1] <=> coefficient of StdDev for maximums ... both values must be greater than 2
		int[] sizes = new int[numClusters];
		
		// collect sizes of all clusters
		for(i=0;i<sizes.length;i++){
			sizes[i] = thisFeaturesClusters.get(i).getElements().length;
			sum += sizes[i]; 
		}
	
		// find average cluster size
		avgElementsPerCluster = sum/(sizes.length);
		sum=0;
		
		// find cluster size average absolute deviation
		for(i=0;i<sizes.length;i++)
			sum+=Math.abs(sizes[i]-avgElementsPerCluster);
		clusterAvgAbsDev = sum/(sizes.length);
		
		// find minimum number of elements allowed in the target cluster
		minElements =(int) Math.ceil(avgElementsPerCluster-clusterAvgAbsDev);
		
		// calculate author min and max
		authorMin = authorAvg-authorStdDev;
		authorMax = authorAvg+authorStdDev;
	
		
		// iterate through all clusters, if the cluster has less than the minimum number of elements, disregard it. Otherwise, save it, and calculate distance from 
		for(i=0; i<numClusters;i++){

			Cluster tempCluster = thisFeaturesClusters.get(i); 
			
			if (tempCluster.getElements().length < minElements){
				mu[i][0]=0;
				mu[i][1]=0;
				continue; // disregard cluster - we dont think we like it.
			}
			tempClustCent = tempCluster.getCentroid();
			tempClustDev =tempCluster.avgAbsDev();
			clustMin = tempClustCent - tempClustDev;
			clustMax = tempClustCent + tempClustDev;
			
		// for min: the min of the new cluster must either be mu*authorStdDev greater than author's average, or mu*authorStdDev less than authors min
																	// AND
		// for max: the max of the new cluster must either be mu*authorStdDev greater than author's max, or mu*authorStdDev less than author's average
			// picture (example of one possible senario:
			//  authMin->\/ authAvg \/<-authMax 
			// 0---------<----|---->-<------|------>------100
			//                        targetMin->^   targetAvg     ^<-targetMax
			
			
			// {authorMin,authorMax} + (mu*authorStdDev) = {clustMin,clustMax}  => mu = ({clustMin,clustMax}-{authMin,authMax})/authorStdDev => abs(mu) must be greater than 2.00
			// negative mu indicates cluster contains values less than authors values, and vice-versa
			mu[i][0] = (clustMin-authorMin)/authorStdDev;
			mu[i][1] = (clustMax-authorMax)/authorStdDev;
				
		}
		
		// THIS SPECIFIC STEP MAY ONLY BE FOR TESTING: set the target cluster to the cluster who's absolute mu[cluster][0] and mu[cluster][1] values are both greater than 2 
		// TODO(START):The selection process SHOULD be randomized to decrease the possiblity that the the author's actual features/style could be guessed to be within a range.
		boolean invalidInitializer= false;
		if(Math.abs(mu[0][0]) > 2 && Math.abs(mu[0][1]) > 2){
				potentialTarget[0]=0;
				potentialTarget[1]=mu[0][0];
				potentialTarget[2]=mu[0][1];
		}
		else{
				potentialTarget[0]=0;
				potentialTarget[1]=0;
				potentialTarget[2]=0;
				invalidInitializer = true;
		}
		for(i=1;i<numClusters;i++){
			double muZero = mu[i][0];
			double muOne = mu[i][1];
			if(Math.abs(muZero) > 2 && Math.abs(muOne) > 2){
				if((muZero*muOne)<(potentialTarget[1]*potentialTarget[2]) || invalidInitializer){
					invalidInitializer = false;
					potentialTarget[0]=i;
					potentialTarget[1]=muZero;
					potentialTarget[2]=muOne;
				}
			}
		}
		// TODO(END)
		
		MersenneTwisterFast mtfGen = new MersenneTwisterFast();
		if (potentialTarget[1] != 0 && potentialTarget[2] != 0){
			targetCluster = thisFeaturesClusters.get((int)potentialTarget[0]);
			System.out.println("Target cluster is: "+targetCluster.getElements());
			targetSet = true;
			targetCent = targetCluster.getCentroid();
			targetDev = targetCluster.avgAbsDev();
			System.out.println("Min : Max =>"+(targetCent-targetDev)+" : "+(targetCent+targetDev));
			targetValue = (targetCent-targetDev)+(mtfGen.nextDouble()*(2*targetDev)); // generate a random number between the minimum and maximum values of the target cluster as defined by 
			// (targetCent-targetDev) <=> min, and (targetCent+targetDev) <=> (max)
			//System.out.println("Target value: "+targetValue);
		}	
		else if (invalidInitializer == true){ // if invalidInitializer == true, and hasn't been set to false, that means that there is only one cluster, and it's values are (probably) zero. 
			targetCluster = thisFeaturesClusters.get((int)potentialTarget[0]);
			System.out.println("Target cluster is: "+Arrays.deepToString(targetCluster.getElements()));
			targetCent = targetCluster.getCentroid();
			targetDev = targetCluster.avgAbsDev();
			System.out.println("Min:Max ->"+(targetCent-targetDev)+" : "+(targetCent+targetDev));
			targetValue = (targetCent-targetDev)+(mtfGen.nextDouble()*(2*targetDev)); // generate a random number between the minimum and maximum values of the target cluster as defined by 
			targetSet = true;
			// (targetCent-targetDev) <=> min, and (targetCent+targetDev) <=> (max)
			System.out.println("Target value: "+targetValue);
		}
		else
			targetValue = -1;
			
	}
	
	
	/**
	 * @deprecated use @orderClustersByDistFromAuthor instead
	 * returns the target value
	 * @return
	 */
	public Double getTargetValue(){
		findOptimalCluster();
		return targetValue;
	}
	
	/**
	 * @deprecated
	 * returns the centroid of the target cluster
	 * @return
	 */
	public double getTargetCentroid(){
		if(targetSet == true){
			return targetCent;
		}
		else
			return -1;
	}
	
	/**
	 * returns the average absolute deviation of the elements in the target cluster from the centroid
	 * @return
	 */
	public double getTargetAvgAbsDev(){
		if(targetSet==true)
			return targetDev;
		else
			return -1;
	}
	
	
	
	/**
	 * Method that runs the modified k-means clustering algorithm, initialized via the k-means++ algorithm
	 */
	public void aMeansCluster(){ // a-means-cluster vs k-means-cluster
		Logger.logln("Entered aMeansCluster");
		thisFeaturesClusters = new ArrayList<Cluster>(numPartitions);
		//System.out.println("Starting Clustering...");
		initialize();	
		//System.out.println("Clusters Initialized");
		updateCentroids();
		//System.out.println("Algorithm running....");
		double avgAbsDev;
		ArrayList<String> holderForLogger = new ArrayList<String>();
		if(isFinished == true){
			Iterator<Cluster> clusterIter = thisFeaturesClusters.iterator();
			int clusterNumber = 0;
			Logger.logln(featName+" has: "+thisFeaturesClusters.size()+" clusters.");
			while(clusterIter.hasNext()){
				Cluster thisOne = clusterIter.next();
				holderForLogger.clear();
				Logger.logln("Cluster "+clusterNumber+" has its centroid at"+thisOne.getCentroid()+" and has "+thisOne.getElements().length+" elements. They are: ");
				Pair[] somePairs = thisOne.getElements();
				int numSomePairs = somePairs.length;
				int i = 0;
				for(i=0;i<numSomePairs;i++){
					holderForLogger.add(somePairs[i].pairToString()+" , ");
				}
				Logger.logln(holderForLogger.toString());
				clusterNumber+=1;
			}
		}
		Logger.logln("leaving aMeansCluster");
	}	
	
	/**
	 * Orders the clusters with respect to 'preference'. 
	 * 
	 * preference = (number of elements in cluster)*(positive distance between cluster centroid and user's average)
	 * 
	 * @return
	 */
	public Cluster[] getPreferredOrdering(){
		Logger.logln("Getting preferred ordering for clusters");
		int i=0;
		int sizeSum =0;
		double sizeAvg = 0;
		double	tempClustCent;
		double tempClustDev;
		double clustMin;
		double clustMax;
		int numClusters = thisFeaturesClusters.size();
		int[] sizes = new int[numClusters];
		double[] dists = new double[numClusters];
		Double[][] preferences = new Double[numClusters][2]; // highest number => most ideal cluster 
		double distSum = 0 ;
		double distAvg = 0;
		
		// collect sizes of all clusters
		for(i=0;i<numClusters;i++){
			sizes[i] = thisFeaturesClusters.get(i).getElements().length;
			sizeSum += sizes[i]; 
		}
		
		sizeAvg = (double)sizeSum/numClusters;
		
		for(i=0; i<numClusters;i++){

			Cluster tempCluster = thisFeaturesClusters.get(i); 
			
			tempClustCent = tempCluster.getCentroid();
			if(tempClustCent< authorAvg)
				dists[i] = authorAvg - tempClustCent;
			else if (tempClustCent > authorMax)
				dists[i] = tempClustCent - authorAvg;
			else
				dists[i] = 0;
			
			distSum += dists[i];
		}
		
		distAvg = distSum/numClusters;
		
		for(i = 0; i < numClusters; i++){
			preferences[i][0] =(Double)(double) i;
			preferences[i][1] = (dists[i])*(sizes[i]/sizeAvg); //  ( distance)*(cluster size/ average cluster size)
			
		}
		
		Arrays.sort(preferences, new Comparator<Double[]>(){
			public int compare(Double one[], Double two[]){
				return one[1].compareTo(two[1]);
			}
		});	
		
		Cluster[] targets = new Cluster[numClusters]; // can't be more than this. 
		i= 0;
		for(i=0;i<numClusters;i++){
				targets[i]= thisFeaturesClusters.get(preferences[i][0].intValue());
				//System.out.println(targets[i]);
			}	
		Logger.logln("finished ordering clusters");
		return targets;
	}
		
		
	/**
	 * @deprecated 
	 * orders the clusters by the linear distance of the average absolute value of the elements +/- the centroid's value (average), and returns an ArrayList of 'Clusters' such that
	 * the first Cluster in the ArrayList is the farthest away from the auhtor's values, and the last is the closest. The Clusters are filtered prior to sorting, and any clusters that have 
	 * less than (avg - avg. abs. dev) number of elements are not considered.
	 * @return
	 * 	returns ArrayList of Clusters (containing Pair objects) ordered from greatest distance from author's values => least distance, with Clusters determined obscure or useless ignored.
	 */
	public Cluster[] orderClustersByDistFromAuthor(){
		// 1) find author's avg. and std. dev for feature in question
		// 2) find avg. and avg. abs. dev for each cluster
		// 3) order clusters such that the first cluster's average (centroid) +/- avg. abs. dev. is the farthest from the author's values, and the last is the closest - done after
		// filtering out clusters with few values ( less than avg - avg. abs. dev number of elements)
		int i=0;
		double sum = 0;
		double avgElementsPerCluster;
		double clusterAvgAbsDev;
		int minElements;
		double	tempClustCent;
		double tempClustDev;
		double clustMin;
		double clustMax;
		int numClusters = thisFeaturesClusters.size();
		double[][] mu = new double[numClusters][2]; // mu[clusterNumber][0] <=> coefficient of StdDev for minimums, mu[clusterNumber][1] <=> coefficient of StdDev for maximums ... both values must be greater than 2
		int[] sizes = new int[numClusters];
		Double[][] potentialTargets = new Double[numClusters][3]; // potentialTarget[i][0] <=> cluster number , potentialTarget[i][1] <=> mu[cluster][0] (min), potentialTarget[i][2] <=> mu[cluster][1] (max)
		
		// collect sizes of all clusters
		for(i=0;i<numClusters;i++){
			sizes[i] = thisFeaturesClusters.get(i).getElements().length;
			sum += sizes[i]; 
		}
	
		// find average cluster size
		avgElementsPerCluster = sum/(sizes.length);
		sum=0;
		
		// find cluster size average absolute deviation
		for(i=0;i<sizes.length;i++)
			sum+=Math.abs(sizes[i]-avgElementsPerCluster);
		clusterAvgAbsDev = sum/(sizes.length);
		
		// find minimum number of elements allowed in the target cluster
		minElements =(int) Math.ceil(avgElementsPerCluster-clusterAvgAbsDev);
		
		// calculate author min and max
		authorMin = authorAvg-authorStdDev;
		authorMax = authorAvg+authorStdDev;
	
		
		// iterate through all clusters, if the cluster has less than the minimum number of elements, disregard it. Otherwise, save it, and calculate distance from 
		for(i=0; i<numClusters;i++){

			Cluster tempCluster = thisFeaturesClusters.get(i); 
			
			if (tempCluster.getElements().length < minElements){ // maybe this isn't a good idea? It seems like it is, because it deals with potentially outlying clusters. 
				continue; // disregard cluster - we dont think we like it.
			}
			tempClustCent = tempCluster.getCentroid();
			tempClustDev =tempCluster.avgAbsDev();
			clustMin = tempClustCent - tempClustDev;
			clustMax = tempClustCent + tempClustDev;
			
		// for min: the min of the new cluster must either be mu*authorStdDev greater than author's average, or mu*authorStdDev less than authors min
																	// AND
		// for max: the max of the new cluster must either be mu*authorStdDev greater than author's max, or mu*authorStdDev less than author's average
			// picture (example of one possible senario:
			//  authMin->\/ authAvg \/<-authMax 
			// 0---------<----|---->-<------|------>------100
			//                        targetMin->^   targetAvg     ^<-targetMax
			
			
			// {authorMin,authorMax} + (mu*authorStdDev) = {clustMin,clustMax}  => mu = ({clustMin,clustMax}-{authMin,authMax})/authorStdDev => abs(mu) must be greater than 2.00
			// negative mu indicates cluster contains values less than authors values, and vice-versa
			mu[i][0] = (clustMin-authorMin)/authorStdDev;
			mu[i][1] = (clustMax-authorMax)/authorStdDev;
				
		}
		
		if(Math.abs(mu[0][0]) > 2 && Math.abs(mu[0][1]) > 2){
				potentialTargets[0][0]=0.0;
				potentialTargets[0][1]=mu[0][0];
				potentialTargets[0][2]=mu[0][1];
		}
		else{
				potentialTargets[0][0]=0.0;
				potentialTargets[0][1]=0.0;
				potentialTargets[0][2]=0.0;
		}
		for(i=1;i<numClusters;i++){
			double muZero = mu[i][0];
			double muOne = mu[i][1];
			if(Math.abs(muZero) > 2 && Math.abs(muOne) > 2){
					potentialTargets[i][0]=(Double)(double)i;
					potentialTargets[i][1]=muZero;
					potentialTargets[i][2]=muOne;
			}
			else{
					potentialTargets[i][0]=(Double) (double)i;
					potentialTargets[i][1]=0.0;
					potentialTargets[i][2]=0.0;
			}
		}
		
		Arrays.sort(potentialTargets, new Comparator<Double[]>(){
			public int compare(Double[] ptOne, Double[] ptTwo){
				Double dOne = ptOne[1]*ptOne[2];
				Double dTwo = ptTwo[1]*ptTwo[2];
				return -1*dOne.compareTo(dTwo);
			}
		});	
		
		boolean foundEndOrNull = false;
		Cluster[] targets = new Cluster[numClusters]; // can't be more than this. 
		i= 0;
		while(foundEndOrNull == false && i <potentialTargets.length){ //XXX fixed?
			if ((potentialTargets[i][1] == 0.0) || (potentialTargets[i][2] == 0.0)){
					foundEndOrNull = true; // the rest (if any) will be null, because the array was just ordered from greatest -> least
					System.out.println("found zero, i == "+i);
			}
			else{
				targets[i]= thisFeaturesClusters.get(potentialTargets[i][0].intValue());
				System.out.println(targets[i]);
				i++;
			}	
			
		}
		int j=0;
		Cluster[] sizedClusterRay = new Cluster[i];
		for(j = 0;j<i;j++){
			System.out.println(targets[j]);
			sizedClusterRay[j] = targets[j];
		}
		//Scanner in = new Scanner(System.in);
		//in.next();
		return sizedClusterRay;
	}
	
/*
	public static void main(String[] args){
		TargetExtractor te = new TargetExtractor();
		double[] testRay = {1,2,3,5,77,79,101,1300};
		double[] sortedRay = testRay.clone();
		Arrays.sort(sortedRay);
		int i=0;
		for(i=0;i<sortedRay.length;i++)
			System.out.print(sortedRay[i]+", ");
		System.out.println();
		te.numMeans = 3;
		te.kPlusPlusPrep(testRay);
		Iterator<Cluster> clusterIter = te.thisFeaturesClusters.iterator();
		int index = 0;
		while(clusterIter.hasNext()){
			Cluster clust = clusterIter.next();
			index++;
			System.out.println("Centroid Number '"+index+"' is: "+clust.getCentroid());
		}
		
		TargetExtractor.testPair("a", 5.65);
		
	}
	
	public static void testPair(String str, double dub){
		Pair p = new Pair(str,dub);
		System.out.println("name: "+p.doc+" and value: "+p.value);
	}
	*/	
}

