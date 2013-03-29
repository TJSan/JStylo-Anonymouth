package edu.purdue.jsan.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.jgaap.generics.Document;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import edu.drexel.psal.JSANConstants;
import edu.drexel.psal.jstylo.GUI.ClassTabDriver;
import edu.drexel.psal.jstylo.analyzers.WekaAnalyzer;
import edu.drexel.psal.jstylo.generics.Analyzer;
import edu.drexel.psal.jstylo.generics.AnalyzerTypeEnum;
import edu.drexel.psal.jstylo.generics.CumulativeFeatureDriver;
import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.jstylo.generics.ProblemSet;
import edu.drexel.psal.jstylo.generics.WekaInstancesBuilder;
import edu.drexel.psal.jstylo.generics.Logger.LogOut;

public class WriterprintsAuto {

	/**
	 * @param args
	 */

	protected ProblemSet ps;
	protected CumulativeFeatureDriver cfd;
	protected List<CumulativeFeatureDriver> presetCFDs;
	protected WekaInstancesBuilder wib;
	protected Analyzer wad;
	protected AnalyzerTypeEnum at = AnalyzerTypeEnum.WEKA_ANALYZER; // default
	protected List<Classifier> classifiers;
	protected Thread analysisThread;
	protected List<String> results;

	protected String defaultTrainDocsTreeName = "Authors";
	protected String defaultLoadSaveDir = ".";
	protected DefaultTableModel testDocsTableModel;
	protected DefaultComboBoxModel featuresCullJListModel;
	protected DefaultComboBoxModel featuresCanonJListModel;
	protected DefaultComboBoxModel featuresJListModel;
	protected DefaultComboBoxModel featuresSetJComboBoxModel;
	protected JTree trainCorpusJTree;
	protected File directory;
	protected Classifier tmpClassifier;
	protected Evaluation resArr[][];

	public WriterprintsAuto(String nameOfFolder) {
		super();
		initData(nameOfFolder);
		testOfCorpora();
		testingFeateres();
		// readData();
	}

	private void testingFeateres() {
		// writes Evaluation results matrix to new file in excel, iterates through each feature in the xml
		
		boolean isDirectory = directory.isDirectory();
		if (isDirectory) {
			File[] names = directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".xml");
				}
			});
			ExcelWriter test = new ExcelWriter("results2.xls");
			for(int i=0; i<presetCFDs.size();i++){
				resArr = new Evaluation[8][names.length/8];
				cfd = presetCFDs.get(i);
				for (File file : names) { startProg(file.getName()); }
				
				
				try {
					test.write(cfd.getName(), i, resArr);
					System.out.println("Please check the result file under "+cfd.getName());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else {
			// It returns false if directory is a file.
			System.out.println("the name you have entered is a file  : "
					+ directory);
			System.out.println("the path is " + directory.getAbsolutePath());
		}
		
	}

	private void readData() {
		Kryo kryo = new Kryo();
		Input input;
		try {
			input = new Input(new FileInputStream(directory.getAbsolutePath()
					+ "/objects.bin"));
			resArr = kryo.readObject(input, Evaluation[][].class);
			// resArr=(Evaluation[][]) kryo.readClassAndObject(input);
			System.out.println(resArr[1][1].correct());

			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// SomeClass someObject = kryo.readObject(input, SomeClass.class);

	}

	private void testOfCorpora() {
		boolean isDirectory = directory.isDirectory();
		if (isDirectory) {
			File[] names = directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".xml");
				}
			});
			
			resArr = new Evaluation[8][names.length/8];
			for (File file : names) { startProg(file.getName()); }
			
			
			Logger.logln("Writing array object to "
					+ directory.getAbsolutePath() + "/objects.bin");
			Kryo kryo = new Kryo();
			Output output;
			try {
				output = new Output(new FileOutputStream(
						"jsan_resources/our_problem_set/objectsWriterPrints.bin"));
				//kryo.writeObject(output, resArr);
				//resArr[0][0].
				output.close();
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			// It returns false if directory is a file.
			System.out.println("the name you have entered is a file  : "
					+ directory);
			System.out.println("the path is " + directory.getAbsolutePath());
		}
	}

	private void initData(String nameOfFolder) {
		String[] folderNames = { "Salta", "Lauren", "JiHeyon", "Amy","All","Small" };
		int index=0;
		for(int i=0;i<folderNames.length;i++){
			if(folderNames[i].equals(nameOfFolder))
				index=i;
		}
		directory = new File("jsan_resources/our_problem_set/" + folderNames[index]);
		
		ps = new ProblemSet();
		ps.setTrainCorpusName(defaultTrainDocsTreeName);
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				ps.getTrainCorpusName());
		trainCorpusJTree = new JTree(top);
		cfd = new CumulativeFeatureDriver();
		presetCFDs = new ArrayList<CumulativeFeatureDriver>();
		try {
			File file = new File(JSANConstants.JSAN_FEATURESETS_PREFIX);
			File[] featureSetFiles = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".xml");
				}
			});

			String path;
			for (File f : featureSetFiles) {
				path = f.getAbsolutePath();
				presetCFDs.add(new CumulativeFeatureDriver(path));
			}
		} catch (Exception e) {
			Logger.logln("Failed to read feature set files.", LogOut.STDERR);
			e.printStackTrace();
		}
		classifiers = new ArrayList<Classifier>();
		results = new ArrayList<String>();
	}

	private void startProg(String file) {
		// Logger.logln("'Load Problem Set' button clicked on the documents tab");

		String path = directory.getAbsolutePath() + "/" + file;
		// Logger.logln("Trying to load problem set from " + path);
		try {
			ps = new ProblemSet(path);
			defaultLoadSaveDir = (new File(path)).getParent();
		} catch (Exception exc) {
			Logger.logln("Failed loading " + path, LogOut.STDERR);
			Logger.logln(exc.toString(), LogOut.STDERR);
		}

		// Logger.logln("Preset feature set selected in the features tab.");
		//cfd = presetCFDs.get(0);
		// Logger.logln("loaded preset feature set: " + cfd.getName());

		tmpClassifier = null;
		try {
			tmpClassifier = (Classifier) Class.forName(
					"weka.classifiers.functions.SMO").newInstance();
		} catch (Exception e) {
			Logger.logln("Could not create classifier out of class: ");
			e.printStackTrace();
			return;
		}
		// Logger.logln("'Add' button clicked in the analysis tab.");
		if (classifiers.isEmpty())
			classifiers.add(tmpClassifier);

		// Logger.logln("'Run Analysis' button clicked in the analysis tab.");

		// check
		if (ps == null || ps.getAllTrainDocs().size() == 0) {
			Logger.logln("Run Analysis Error: Training corpus not set or empty.");
			return;

		} else if (cfd == null || cfd.numOfFeatureDrivers() == 0) {
			Logger.logln("Feature set not set or has no features.");
			return;

		} else if (classifiers.isEmpty()) {
			Logger.logln("No classifiers added.");
			return;
		}

		// start analysis thread
		// main.at = AnalyzerTypeEnum.WRITEPRINTS_ANALYZER;
		analysisThread = new Thread(new RunWriterPrintsAnalysisThread(this,
				file));
		analysisThread.start();
		try {
			analysisThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * // ... Input input = new Input(new FileInputStream("file.bin"));
		 * SomeClass someObject = kryo.readObject(input, SomeClass.class);
		 * input.close();
		 */

		/*
		 * ObjectOutputStream objectOut; try { objectOut = new
		 * ObjectOutputStream(new BufferedOutputStream( new
		 * FileOutputStream(directory.getAbsolutePath()+"/"+"objects.ser")));
		 * objectOut.writeObject(resArr); objectOut.close(); } catch
		 * (FileNotFoundException e) { e.printStackTrace(); } catch (IOException
		 * e) { e.printStackTrace(); }
		 */

	}

	protected static String getTimestamp() {
		SimpleDateFormat tf = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return tf.format(cal.getTime());
	}

	public static class RunWriterPrintsAnalysisThread implements Runnable {

		protected WriterprintsAuto main;
		protected String content;
		protected String fileName;

		public RunWriterPrintsAnalysisThread(WriterprintsAuto main,
				String fileName) {
			this.main = main;
			this.fileName = fileName;
			main.wib = new WekaInstancesBuilder(true);
		}

		public void run() {
			StringTokenizer token = new StringTokenizer(fileName, "_");
			String a[] = new String[token.countTokens()];
			int count = 0;
			while (token.hasMoreTokens()) {
				a[count++] = token.nextToken();
			}
			int num = Integer.parseInt(a[3].substring(0, a[3].length() - 4));
			int row = (num - 1) % 8;
			int col = (num - 1) / 8;
			// Logger.logln(">>> Run Analysis thread started.");

			// initialize results tab
			content = "";
			boolean classifyTestDocs = false;

			// update header
			// -------------
			content += "============================ JStylo Analysis Output ============================\n"
					+ "Started analysis on "
					+ getTimestamp()
					+ "\n"
					+ (classifyTestDocs ? "Running test documents classification"
							: "Running 10-folds cross validation on training corpus")
					+ "\n" + "\n";

			// training set
			content += "Training corpus:\n";
			TreeMap<String, List<Document>> authors = (TreeMap<String, List<Document>>) main.ps
					.getAuthorMap();
			for (Entry<String, List<Document>> author : authors.entrySet()) {
				content += "> " + author.getKey() + " ("
						+ author.getValue().size() + " documents)\n";
			}

			content += "\n";

			// documents
			List<Document> trainingDocs = main.ps.getAllTrainDocs();
			List<Document> testDocs = main.ps.getTestDocs();
			int numTrainDocs = trainingDocs.size();
			int numTestDocs = testDocs.size();

			// feature set
			content += "Feature set: " + main.cfd.getName() + ":\n";
			for (int i = 0; i < main.cfd.numOfFeatureDrivers(); i++) {
				content += "> " + main.cfd.featureDriverAt(i).getName() + "\n";
			}
			content += "\n";

			// classifiers
			content += "Classifiers used:\n";
			for (Classifier c : main.classifiers) {
				content += "> "
						+ String.format("%-50s", c.getClass().getName()) + "\t"
						+ ClassTabDriver.getOptionsStr(c.getOptions()) + "\n";
			}

			content += "\n"
					+ "================================================================================\n"
					+ "\n";

			// feature extraction
			// ==================
			
			// pre-processing
			if (main.at == AnalyzerTypeEnum.WRITEPRINTS_ANALYZER) {
				Logger.logln("Applying analyzer feature-extraction pre-processing procedures...");
				content += getTimestamp()
						+ "Applying analyzer feature-extraction pre-processing procedures...\n";

				// move all test documents to be training documents
				trainingDocs.addAll(testDocs);
				testDocs = new ArrayList<Document>();

				content += getTimestamp() + "done!\n\n";
			}
			// training set
			// Logger.logln("Extracting features from training corpus...");

			main.wib.setSparse(true);

			content += getTimestamp()
					+ " Extracting features from training corpus ("
					+ (main.wib.isSparse() ? "" : "not ")
					+ "using sparse representation)...\n";

			try {
				main.wib.prepareTrainingSet(trainingDocs, main.cfd);
			} catch (Exception e) {
				Logger.logln(
						"Could not extract features from training corpus!",
						LogOut.STDERR);
				e.printStackTrace();
				Thread.currentThread().stop();
			}

			content += getTimestamp() + " done!\n\n";

			content += "Training corpus features (ARFF):\n"
					+ "================================\n"
					+ main.wib.getTrainingSet().toString() + "\n\n";

			// running InfoGain
			// ================

			content += "Calculating InfoGain on the training set's features\n";
			content += "===================================================\n";
			int igValue = 200;
			try {
				content += main.wib.applyInfoGain(false, igValue);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			content += "done!\n\n";

			// running classification
			// ======================

			// Running cross-validation on training corpus
			// ===========================================

			// Logger.logln("Starting training 10-folds CV phase...");

			content += getTimestamp()
					+ " Starting 10-folds cross-validation on training corpus phase...\n";
			content += "\n================================================================================\n\n";

			Classifier c;
			int numClass = main.classifiers.size();
			for (int i = 0; i < numClass; i++) {
				c = main.classifiers.get(i);
				content += "Running analysis with classifier " + (i + 1)
						+ " out of " + numClass + ":\n" + "> Classifier: "
						+ c.getClass().getName() + "\n" + "> Options:    "
						+ ClassTabDriver.getOptionsStr(c.getOptions()) + "\n\n";

				main.wad = new WekaAnalyzer(c);
				content += getTimestamp() + " Starting cross validation...\n";
				// Logger.log("Starting cross validation...");

				// run
				Object results = (Evaluation) main.wad.runCrossValidation(
						main.wib.getTrainingSet(), 10, 0);
				Logger.logln("size equal=" + main.wib.getAuthors().size());
				Logger.logln("size of classes equal="
						+ main.wib.getTrainingSet().numClasses());

				content += getTimestamp() + " done!\n\n";
				// Logger.logln("Done!");

				// print out results
				switch (main.at) {
				case WEKA_ANALYZER:
					Evaluation eval = (Evaluation) results;
					main.resArr[row][col] = (Evaluation) eval;
					content += eval.toSummaryString(false) + "\n";
					try {
						content += eval.toClassDetailsString() + "\n"
								+ eval.toMatrixString() + "\n";
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case WRITEPRINTS_ANALYZER:
					String strResults = (String) results;
					content += strResults + "\n";
					break;
				}
			}
			// Logger.logln(content);
			// unlock gui and update results
			
			main.results.add(content);

			FileWriter fstream;
			try {
				fstream = new FileWriter(
						"jsan_resources/our_problem_set/writerprints_results/"
								+ fileName.substring(0, fileName.length() - 4)
								+ ".txt");
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(content);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Logger.logln(">>> Run Analysis thread finished." + row + " " + col
					+ " incorrect num:" + main.resArr[row][col].incorrect()
					+ " correct num:" + main.resArr[row][col].correct());
			
		}
	}
}
