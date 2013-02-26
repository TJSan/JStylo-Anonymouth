package edu.drexel.psal.anonymouth.gooie;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.*;

import edu.drexel.psal.anonymouth.projectDev.Attribute;
import edu.drexel.psal.anonymouth.projectDev.Cluster;
import edu.drexel.psal.anonymouth.utils.Pair;

public class ClusterViewer extends JPanel {
	
	//double[] features = {3.3, 3.5, 2.1, 7.8, 9.5, 5.5, 6.1,14.9, 18.0,19.6};
	Cluster[] clusters;
	int numClusters;
	protected static JPanel[] allPanels;
	protected static int numFeatures;
	protected static int[] selectedClustersByFeature;
	
	double xoffset = 20;
	double yoffset;
	double dim = 6;
	double minimum;
	double maximum;
	double authorMin;
	double authorMax;
	double presentValue;
	double scale;
	static int count = 0;
	private int featureNumber;
	private Color transPurple =new Color(0.75f,0.1f,0.9f,0.55f);
	private Color highlightColor = new Color(0f,1.0f,0,0.6f);	
	private Color transRed = new Color(1.0f,0f,0f,.9f);
	private Color transBlue = new Color(0f,0f,1.0f,.9f);
	
	
	public ClusterViewer(Cluster[] clusters,int featureNumber, double minimum, double maximum, double authorMin, double authorMax, double presentValue){
		this.clusters = clusters;
		this.numClusters = clusters.length;
		this.minimum = minimum;
		this.maximum = maximum;
		this.authorMin = authorMin;
		this.authorMax = authorMax;
		this.presentValue = presentValue;
		this.featureNumber = featureNumber;
		this.setBorder(BorderFactory.createLoweredBevelBorder());
	}
	
	public double transform(double value, boolean noOffset){
		double temp = value - minimum;
		if(noOffset == false)
			temp = temp*scale + xoffset;
		else
			temp = temp*scale;
		return temp;
	}
	
	public String roundToString(int precision, double value){
		// precision is some multiple of 10
		return  Double.toString(Math.floor(value*precision+.5)/precision);
	}
		
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		setPreferredSize(new Dimension(800,50));
		Graphics2D g2 = (Graphics2D)g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int width = getWidth();
		int height = getHeight();
		yoffset = height/2;
		double tooDamnBigOffset = 1;
		g2.draw(new Line2D.Double(xoffset,yoffset,width-xoffset,yoffset));
		if(maximum <1 )
			maximum = 1.0; // prevent divide by zero, or creating too large a scale by dividing by a decimal.... FIXME - there is probably a better way...
		else if(maximum >width)
			tooDamnBigOffset = maximum/width;
		if(minimum <0)
			minimum = 0;
		scale = (double) (((double)width/tooDamnBigOffset)-2*xoffset)/(maximum-minimum);
		int i=0;
		double transAuthorMin = transform(authorMin,false);
		double transAuthorMax = transform(authorMax,false);
		double transPresentValue = transform(presentValue,false);
		g2.drawString(roundToString(100,minimum),(int)xoffset/2,(int)(.75*yoffset));
		g2.drawString(roundToString(100,maximum),(int)(width-3*xoffset),(int)(.75*yoffset));
		double[][] thisClustersMinsAndMaxes = new double[numClusters][2]; // [min,max] (index == cluster number) => used to update cluster colors
		for(i=0;i<numClusters;i++){
			Cluster current = clusters[i];
			double maxValue = transform(current.getMaxValue(),false);
			double minValue = transform(current.getMinValue(),false);
			double centroid = transform(current.getCentroid(),false);
			
			/*
			g2.setPaint(transBlue);
			int j=0;
			Pair[] pRay = current.getElements();
			int lenPRay = pRay.length;
			for(j=0; j < lenPRay; j++){
				double position = transform(pRay[j].value,false);
				g2.fill(new Ellipse2D.Double(position-Math.sqrt(dim), yoffset-Math.sqrt(dim),dim,dim));
			}
			
			g2.setColor(Color.black);
			dim = 4;
			g2.fill(new Ellipse2D.Double(centroid-Math.sqrt(dim), yoffset-Math.sqrt(dim),dim,dim));
			*/
			
			int selectedCluster = selectedClustersByFeature[featureNumber];
			if(i == selectedCluster-1){ // this needs to be offset by 1, because a '1' was added to the cluster numbers to avoid
				// a cluster being number '0'. 
				g2.setColor(highlightColor);
				g2.fill(new Ellipse2D.Double(minValue,yoffset*.68,maxValue - minValue,yoffset*.75));

			}
			/*
			else{
				g2.setColor(Color.green);
				g2.draw(new Ellipse2D.Double(minValue,yoffset*.75,maxValue - minValue,yoffset*.5));
			}
			*/
			thisClustersMinsAndMaxes[i][0] = minValue;
			thisClustersMinsAndMaxes[i][1] = maxValue;
			
		}
		g2.setColor(transRed);
		g2.fill(new Ellipse2D.Double(transAuthorMin,yoffset*.78,transAuthorMax-transAuthorMin,yoffset*.5));
		
		g2.setColor(Color.black);
		dim = 7;
		g2.fill(new Ellipse2D.Double(transPresentValue-Math.sqrt(dim), yoffset-Math.sqrt(dim),dim,dim));
		
	}
	
	public static void makeViewer(Attribute[] theOnesYouWantToSee){
		//System.out.println("length of theOnesYouWantToSee: "+theOnesYouWantToSee.length);
		int numFeatures = theOnesYouWantToSee.length;
		double[] minimums = new double[numFeatures]; 
		double[] maximums = new double[numFeatures];
		double[] authorMin = new double[numFeatures];
		double[] authorMax = new double[numFeatures];
		double[] presentValues = new double[numFeatures];
		String[] names = new String[numFeatures];
		
		int i = 0;
		ArrayList<Cluster[]> everySingleCluster = new ArrayList<Cluster[]>(numFeatures);
		double tempMinMax;
		String tempString;
		String dashes;
		selectedClustersByFeature = new int[numFeatures];
		double tempAuthorMinMax;
		for(i=0; i< numFeatures;i++){
			selectedClustersByFeature[i] = -1; // initialize with no clusters selected;
			everySingleCluster.add(i,theOnesYouWantToSee[i].getOrderedClusters());
			authorMin[i] = theOnesYouWantToSee[i].getAuthorAvg() - theOnesYouWantToSee[i].getAuthorConfidence();
			if(authorMin[i] <  0)
				authorMin[i] = 0;
			authorMax[i] = theOnesYouWantToSee[i].getAuthorAvg() + theOnesYouWantToSee[i].getAuthorConfidence();	
			presentValues[i] = theOnesYouWantToSee[i].getToModifyValue();
			tempMinMax = theOnesYouWantToSee[i].getTrainMax();
			tempAuthorMinMax = authorMax[i];
			
			if(tempAuthorMinMax < presentValues[i])
				tempAuthorMinMax = presentValues[i];
			
			if(tempAuthorMinMax > tempMinMax)
				maximums[i] = tempAuthorMinMax;
			else
				maximums[i] = tempMinMax; 
			tempMinMax = theOnesYouWantToSee[i].getTrainMin();
			tempAuthorMinMax = authorMin[i];
			
			if(tempAuthorMinMax > presentValues[i])
				tempAuthorMinMax = presentValues[i];
			
			if(tempAuthorMinMax < tempMinMax)
				minimums[i] = tempAuthorMinMax;
			else
				minimums[i] = tempMinMax;
			//System.out.println(presentValues[i]);
			tempString = theOnesYouWantToSee[i].getStringInBraces();
			if(tempString == "")
				dashes = "";
			else
				dashes = "--";
			names[i] = theOnesYouWantToSee[i].getGenericName()+dashes+tempString;
		}
		
		Iterator<Cluster[]> outerLevel = everySingleCluster.iterator();
		allPanels = new JPanel[everySingleCluster.size()];
		i=0;
		int[] initialLayoverVals = new int[numFeatures];
		ClusterViewerDriver.namePanels = new JPanel[numFeatures];
		String[] usedNames = new String[numFeatures];
		while(outerLevel.hasNext())
		{
			JPanel namePanel = new JPanel();
			namePanel.add(new JLabel(names[i]));
			ClusterViewerDriver.namePanels[i] = namePanel;
			usedNames[i] = names[i];
			
			JPanel oneOfMany = new ClusterViewer(outerLevel.next(),i,minimums[i],maximums[i], authorMin[i],authorMax[i],presentValues[i]);
			
			allPanels[i] = oneOfMany;
			initialLayoverVals[i] = 1;
			i++;
			
		}
		GUIMain.inst.addClusterFeatures(usedNames);
		/*
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ClusterViewerFrame inst = new ClusterViewerFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				}
			});
			*/
	}
}

