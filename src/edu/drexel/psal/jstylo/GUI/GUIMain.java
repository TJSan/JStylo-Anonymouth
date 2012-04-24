package edu.drexel.psal.jstylo.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.*;

import edu.drexel.psal.JSANConstants;
import edu.drexel.psal.jstylo.analyzers.WekaAnalyzer;
import edu.drexel.psal.jstylo.generics.Analyzer;
import edu.drexel.psal.jstylo.generics.AnalyzerTypeEnum;
import edu.drexel.psal.jstylo.generics.CumulativeFeatureDriver;
import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.jstylo.generics.ProblemSet;
import edu.drexel.psal.jstylo.generics.WekaInstancesBuilder;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;

import weka.classifiers.*;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
/**
 * JStylo main GUI class.
 * 
 * @author Ariel Stolerman
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class GUIMain extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// main instance
	public static GUIMain inst;

	// ------------------------

	// data
	protected ProblemSet ps;
	protected CumulativeFeatureDriver cfd;
	protected List<CumulativeFeatureDriver> presetCFDs;
	protected WekaInstancesBuilder wib;
	protected Analyzer wad;
	protected AnalyzerTypeEnum at;
	protected List<Classifier> classifiers;
	protected Thread analysisThread;
	protected List<String> results;

	protected String defaultTrainDocsTreeName = "Authors"; 
	protected Font defaultLabelFont = new Font("Verdana",0,16);
	protected static int cellPadding = 5;

	// tabs
	protected JTabbedPane mainJTabbedPane;
	protected JPanel docsTab;
	protected JPanel featuresTab;
	protected JPanel classTab;
	protected JPanel analysisTab;
	
	// documents tab
	protected JLabel testDocsJLabel;
	protected JButton trainDocPreviewJButton;
	protected JButton testDocPreviewJButton;
	protected JButton trainNameJButton;
	protected JLabel docPreviewJLabel;
	protected JButton newProblemSetJButton;
	protected JTextPane docPreviewJTextPane;
	protected JScrollPane docPreviewJScrollPane;
	protected JButton loadProblemSetJButton;
	protected JButton saveProblemSetJButton;
	protected JButton docTabNextJButton;
	protected JButton removeAuthorJButton;
	protected JButton removeTrainDocsJButton;
	protected JButton addTrainDocsJButton;
	protected JTree trainCorpusJTree;
	protected JTable testDocsJTable;
	protected DefaultTableModel testDocsTableModel;
	protected JLabel featuresToolsJLabel;
	protected JLabel docPreviewNameJLabel;
	protected JLabel corpusJLabel;
	protected JButton removeTestDocJButton;
	protected JButton addAuthorJButton;
	protected JButton addTestDocJButton;
	protected JButton clearDocPreviewJButton;
	protected JButton docsAboutJButton;

	// features tab
	protected JButton featuresNextJButton;
	protected JButton featuresBackJButton;
	protected JLabel featuresFeatureConfigJLabel;
	protected JLabel featuresFactorContentJLabel;
	protected JLabel featuresFeatureExtractorContentJLabel;
	protected JScrollPane featuresFeatureExtractorJScrollPane;
	protected JLabel featuresNormContentJLabel;
	protected JScrollPane featuresFeatureExtractorConfigJScrollPane;
	protected JScrollPane featuresCullConfigJScrollPane;
	protected JScrollPane featuresCanonConfigJScrollPane;
	protected JList featuresCullJList;
	protected DefaultComboBoxModel featuresCullJListModel;
	protected JScrollPane featuresCullListJScrollPane;
	protected JScrollPane featuresCanonListJScrollPane;
	protected JList featuresCanonJList;
	protected DefaultComboBoxModel featuresCanonJListModel;
	protected JScrollPane featuresFeatureDescJScrollPane;
	protected JTextPane featuresFeatureDescJTextPane;
	protected JLabel featuresFeatureExtractorJLabel;
	protected JLabel featuresFactorJLabel;
	protected JLabel featuresNormJLabel;
	protected JLabel featuresFeatureDescJLabel;
	protected JTextField featuresFeatureNameJTextField;
	protected JLabel featuresFeatureNameJLabel;
	protected JLabel featuresCullJLabel;
	protected JLabel featuresCanonJLabel;
	protected JButton featuresEditJButton;
	protected JButton featuresRemoveJButton;
	protected JButton featuresAddJButton;
	protected JList featuresJList;
	protected DefaultComboBoxModel featuresJListModel;
	protected JLabel featuresFeaturesJLabel;
	protected JTextPane featuresSetDescJTextPane;
	protected JScrollPane featuresSetDescJScrollPane;
	protected JLabel featuresSetDescJLabel;
	protected JTextField featuresSetNameJTextField;
	protected JLabel featuresSetNameJLabel;
	protected JButton featuresNewSetJButton;
	protected JButton featuresSaveSetJButton;
	protected JButton featuresLoadSetFromFileJButton;
	protected JButton featuresAddSetJButton;
	protected JComboBox featuresSetJComboBox;
	protected DefaultComboBoxModel featuresSetJComboBoxModel;
	protected JLabel featuresSetJLabel;
	protected JButton featuresAboutJButton;

	// Calssifiers tab
	protected JTextField classAvClassArgsJTextField;
	protected JLabel classAvClassArgsJLabel;
	protected JComboBox classClassJComboBox;
	protected JLabel classAvClassJLabel;
	protected JButton classAddJButton;
	protected JTree classJTree;
	protected JTextField classSelClassArgsJTextField;
	protected JLabel classSelClassArgsJLabel;
	protected JScrollPane classSelClassJScrollPane;
	protected JList classJList;
	protected DefaultComboBoxModel classSelClassJListModel;
	protected JScrollPane classTreeScrollPane;
	protected JScrollPane classDescJScrollPane;
	protected JTextPane classDescJTextPane;
	protected JLabel classDescJLabel;
	protected JButton classBackJButton;
	protected JButton classNextJButton;
	protected JLabel classSelClassJLabel;
	protected JButton classRemoveJButton;
	protected JButton classAboutJButton;
	
	// Analysis tab
	protected JLabel analysisTypeJLabel;
	protected JButton analysisBackJButton;
	protected JButton analysisExportTestToARFFJButton;
	protected JButton analysisExportTestToCSVJButton;
	protected JButton analysisExportTrainToARFFJButton;
	protected JButton analysisExportTrainToCSVJButton;
	protected JLabel analysisPostAnalysisJLabel;
	protected JTabbedPane analysisResultsJTabbedPane;
	protected JButton analysisRunJButton;
	protected JButton analysisStopJButton;
	protected JProgressBar analysisJProgressBar;
	protected JButton analysisSaveResultsJButton;
	protected JCheckBox analysisOutputAccByClassJCheckBox;
	protected JCheckBox analysisOutputConfusionMatrixJCheckBox;
	protected JCheckBox analysisSparseInstancesJCheckBox;
	protected JCheckBox analysisOutputFeatureVectorJCheckBox;
	protected JCheckBox analysisCalcInfoGainJCheckBox;
	protected JTextField infoGainValueJTextField;
	protected JCheckBox analysisApplyInfoGainJCheckBox;
	protected ButtonGroup analysisTypeButtonGroup;
	protected JLabel analysisConfigJLabel;
	protected JRadioButton analysisTrainCVJRadioButton;
	protected JRadioButton analysisClassTestDocsJRadioButton;
	protected JLabel analysisResultsJLabel;
	protected JButton analysisAboutJButton;
	
	
	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Logger.initLogFile();
				try {
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				} catch (Exception e) {
					System.err.println("Look-and-Feel error!");
				}
				inst = new GUIMain();
				inst.setDefaultCloseOperation(EXIT_ON_CLOSE);
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public GUIMain() {
		super();
		initData();
		initGUI();
	}

	private void initData() {
		ps = new ProblemSet();
		ps.setTrainCorpusName(defaultTrainDocsTreeName);
		cfd = new CumulativeFeatureDriver();
		FeaturesTabDriver.initPresetCFDs(this);
		FeatureWizardDriver.populateAll();
		classifiers = new ArrayList<Classifier>();
		wib = new WekaInstancesBuilder(true);
		results = new ArrayList<String>();
	}

	private void initGUI() {
		try {
			
			setSize(1024, 768);
			setTitle("JStylo");
			setIconImage(new ImageIcon(Thread.currentThread().getClass().getResource(JSANConstants.JSAN_GRAPHICS_PREFIX+"icon32.jpg")).getImage());
			
			{
				mainJTabbedPane = new JTabbedPane();
				getContentPane().add(mainJTabbedPane, BorderLayout.CENTER);
				
				/* =============
				 * Documents tab
				 * =============
				 */
				docsTab = new JPanel(new BorderLayout(cellPadding,cellPadding));
				mainJTabbedPane.addTab("Documents", docsTab);

				// problem set buttons
				// ===================
				{
					JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
					docsTab.add(panel,BorderLayout.NORTH);
					{
						newProblemSetJButton = new JButton();
						panel.add(newProblemSetJButton);
						newProblemSetJButton.setText("New Problem Set");
					}
					{
						saveProblemSetJButton = new JButton();
						panel.add(saveProblemSetJButton);
						saveProblemSetJButton.setText("Save Problem Set...");
					}
					{
						loadProblemSetJButton = new JButton();
						panel.add(loadProblemSetJButton);
						loadProblemSetJButton.setText("Load Problem Set...");
					}
				}
				{
					JPanel centerPanel = new JPanel(new GridLayout(2,1,cellPadding,cellPadding));
					docsTab.add(centerPanel,BorderLayout.CENTER);
					JPanel topPanel = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
					centerPanel.add(topPanel);
					JPanel testDocsPanel = new JPanel(new BorderLayout(cellPadding,cellPadding));
					JPanel trainDocsPanel = new JPanel(new BorderLayout(cellPadding,cellPadding));
					topPanel.add(testDocsPanel);
					topPanel.add(trainDocsPanel);
					JPanel bottomPanel = new JPanel(new BorderLayout(cellPadding,cellPadding));
					centerPanel.add(bottomPanel);

					// test documents
					// ==============
					{
						testDocsJLabel = new JLabel();
						testDocsPanel.add(testDocsJLabel,BorderLayout.NORTH);
						testDocsJLabel.setText("Test Documents");
						testDocsJLabel.setFont(defaultLabelFont);
					}
					{
						testDocsTableModel = new DefaultTableModel();
						testDocsTableModel.addColumn("Title");
						testDocsTableModel.addColumn("Path");
						testDocsJTable = new JTable(testDocsTableModel){
							private static final long serialVersionUID = 1L;

							public boolean isCellEditable(int rowIndex, int colIndex) {
								return false;
							}
						};
						testDocsJTable.getTableHeader().setReorderingAllowed(false);
						testDocsJTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
						JScrollPane scrollPane = new JScrollPane(testDocsJTable);
						testDocsPanel.add(scrollPane,BorderLayout.CENTER);
					}
					{
						JPanel buttons = new JPanel(new GridLayout(2,3,cellPadding,cellPadding));
						testDocsPanel.add(buttons,BorderLayout.SOUTH);
						{
							addTestDocJButton = new JButton();
							buttons.add(addTestDocJButton);
							addTestDocJButton.setText("Add Document(s)...");
						}
						{
							removeTestDocJButton = new JButton();
							buttons.add(removeTestDocJButton);
							removeTestDocJButton.setText("Remove Document(s)");
						}
						{
							testDocPreviewJButton = new JButton();
							buttons.add(testDocPreviewJButton);
							testDocPreviewJButton.setText("Preview Document");
						}
						buttons.add(new JPanel());
						buttons.add(new JPanel());
						buttons.add(new JPanel());
					}

					// training documents
					// ==================
					{
						corpusJLabel = new JLabel();
						trainDocsPanel.add(corpusJLabel,BorderLayout.NORTH);
						corpusJLabel.setText("Training Corpus");
						corpusJLabel.setFont(defaultLabelFont);
					}
					{
						DefaultMutableTreeNode top = new DefaultMutableTreeNode(ps.getTrainCorpusName());
						trainCorpusJTree = new JTree(top);
						JScrollPane scrollPane = new JScrollPane(trainCorpusJTree);
						trainDocsPanel.add(scrollPane,BorderLayout.CENTER);
					}
					{
						JPanel buttons = new JPanel(new GridLayout(2,3,cellPadding,cellPadding));
						trainDocsPanel.add(buttons,BorderLayout.SOUTH);
						{
							addAuthorJButton = new JButton();
							buttons.add(addAuthorJButton);
							addAuthorJButton.setText("Add Author...");
						}
						{
							addTrainDocsJButton = new JButton();
							buttons.add(addTrainDocsJButton);
							addTrainDocsJButton.setText("Add Document(s)...");
						}
						{
							trainNameJButton = new JButton();
							buttons.add(trainNameJButton);
							trainNameJButton.setText("Edit Name...");
						}
						{
							removeAuthorJButton = new JButton();
							buttons.add(removeAuthorJButton);
							removeAuthorJButton.setText("Remove Author(s)");
						}
						{
							removeTrainDocsJButton = new JButton();
							buttons.add(removeTrainDocsJButton);
							removeTrainDocsJButton.setText("Remove Document(s)");
						}
						{
							trainDocPreviewJButton = new JButton();
							buttons.add(trainDocPreviewJButton);
							trainDocPreviewJButton.setText("Preview Document");
						}
					}

					// preview documents
					// =================
					{
						JPanel preview = new JPanel(new FlowLayout(FlowLayout.LEFT));
						bottomPanel.add(preview,BorderLayout.NORTH);
						{
							docPreviewJLabel = new JLabel();
							preview.add(docPreviewJLabel);
							docPreviewJLabel.setText("Document Preview");
							docPreviewJLabel.setFont(defaultLabelFont);
						}
						{
							docPreviewNameJLabel = new JLabel();
							preview.add(docPreviewNameJLabel);
							docPreviewNameJLabel.setFont(defaultLabelFont);
						}
					}
					{
						docPreviewJTextPane = new JTextPane();
						docPreviewJTextPane.setEditable(false);
						docPreviewJTextPane.setPreferredSize(new java.awt.Dimension(413, 261));
						docPreviewJScrollPane = new JScrollPane(docPreviewJTextPane);
						bottomPanel.add(docPreviewJScrollPane,BorderLayout.CENTER);
					}
					{
						JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
						bottomPanel.add(p,BorderLayout.SOUTH);
						clearDocPreviewJButton = new JButton();
						p.add(clearDocPreviewJButton);
						clearDocPreviewJButton.setText("Clear Preview");
					}
				}

				// bottom toolbar buttons
				// ======================
				{
					JPanel bottomToolbar = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
					docsTab.add(bottomToolbar,BorderLayout.SOUTH);
					{
						JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
						bottomToolbar.add(p);
						docsAboutJButton = new JButton("About...");
						p.add(docsAboutJButton);
					}
					{
						JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
						bottomToolbar.add(p);
						docTabNextJButton = new JButton();
						p.add(docTabNextJButton);
						docTabNextJButton.setText("Next");
					}
				}

				/* ============
				 * Features tab
				 * ============
				 */
				featuresTab = new JPanel(new BorderLayout(cellPadding,cellPadding));
				mainJTabbedPane.addTab("Features", featuresTab);
				{
					// top of the features tab
					// =======================
					JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT,cellPadding,cellPadding));
					featuresTab.add(panel,BorderLayout.NORTH);
					{
						featuresSetJLabel = new JLabel();
						featuresSetJLabel.setFont(defaultLabelFont);
						panel.add(featuresSetJLabel);
						featuresSetJLabel.setText("Feature Set");
					}
					{
						String[] presetCFDsNames = new String[presetCFDs.size() + 1];
						presetCFDsNames[0] = "";
						for (int i=0; i<presetCFDs.size(); i++)
							presetCFDsNames[i+1] = presetCFDs.get(i).getName();

						featuresSetJComboBoxModel = new DefaultComboBoxModel(presetCFDsNames);
						featuresSetJComboBox = new JComboBox();
						panel.add(featuresSetJComboBox);
						featuresSetJComboBox.setModel(featuresSetJComboBoxModel);
						featuresSetJComboBox.setPreferredSize(new java.awt.Dimension(200, 20));
					}
					{
						featuresAddSetJButton = new JButton();
						panel.add(featuresAddSetJButton);
						featuresAddSetJButton.setText("Add Feature Set");
					}
					{
						featuresLoadSetFromFileJButton = new JButton();
						panel.add(featuresLoadSetFromFileJButton);
						featuresLoadSetFromFileJButton.setText("Import from XML...");
					}
					{
						featuresSaveSetJButton = new JButton();
						panel.add(featuresSaveSetJButton);
						featuresSaveSetJButton.setText("Export to XML...");
					}
					{
						featuresNewSetJButton = new JButton();
						panel.add(featuresNewSetJButton);
						featuresNewSetJButton.setText("New Feature Set");
					}
				}
				{
					// center of the features tab
					// ==========================

					JPanel main = new JPanel(new BorderLayout(cellPadding,cellPadding));
					featuresTab.add(main,BorderLayout.CENTER);
					{
						// name and description
						// ====================

						JPanel nameDescPanel = new JPanel(new BorderLayout(cellPadding,cellPadding));
						main.add(nameDescPanel,BorderLayout.NORTH);
						{
							JPanel north = new JPanel(new BorderLayout(cellPadding,cellPadding));
							nameDescPanel.add(north,BorderLayout.NORTH);
							{
								featuresSetNameJLabel = new JLabel();
								featuresSetNameJLabel.setVerticalAlignment(JLabel.TOP);
								featuresSetNameJLabel.setFont(defaultLabelFont);
								featuresSetNameJLabel.setPreferredSize(new Dimension(200,20));
								north.add(featuresSetNameJLabel,BorderLayout.WEST);
								featuresSetNameJLabel.setText("Feature Set Name");
							}
							{
								featuresSetNameJTextField = new JTextField();
								north.add(featuresSetNameJTextField,BorderLayout.CENTER);
							}
						}
						{
							JPanel center = new JPanel(new BorderLayout(cellPadding,cellPadding));
							nameDescPanel.add(center,BorderLayout.CENTER);
							{
								featuresSetDescJLabel = new JLabel();
								featuresSetDescJLabel.setVerticalAlignment(JLabel.TOP);
								center.add(featuresSetDescJLabel,BorderLayout.WEST);
								featuresSetDescJLabel.setText("Feature Set Description");
								featuresSetDescJLabel.setFont(defaultLabelFont);
								featuresSetDescJLabel.setPreferredSize(new Dimension(200,20));
							}
							{
								featuresSetDescJScrollPane = new JScrollPane();
								featuresSetDescJScrollPane.setPreferredSize(new Dimension(featuresSetNameJTextField.getWidth(),100));
								center.add(featuresSetDescJScrollPane,BorderLayout.CENTER);
								{
									featuresSetDescJTextPane = new JTextPane();
									featuresSetDescJScrollPane.setViewportView(featuresSetDescJTextPane);
								}
							}
						}
					}

					{
						// all the rest
						// ============
						JPanel remainderPanel = new JPanel(new BorderLayout(cellPadding,cellPadding));
						main.add(remainderPanel,BorderLayout.CENTER);

						{
							// north - header
							// ==============
							{
								featuresFeaturesJLabel = new JLabel();
								remainderPanel.add(featuresFeaturesJLabel,BorderLayout.NORTH);
								featuresFeaturesJLabel.setText("Features");
								featuresFeaturesJLabel.setFont(defaultLabelFont);
							}
						}
						{
							// west - feature list
							//====================

							JPanel west = new JPanel(new BorderLayout(cellPadding,cellPadding));
							remainderPanel.add(west,BorderLayout.WEST);
							{
								JScrollPane featuresListJScrollPane = new JScrollPane();
								west.add(featuresListJScrollPane,BorderLayout.CENTER);
								{
									featuresJListModel = 
											new DefaultComboBoxModel();
									featuresJList = new JList();
									featuresListJScrollPane.setViewportView(featuresJList);
									featuresJList.setModel(featuresJListModel);
									featuresJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
								}
							}
							{
								JPanel buttons = new JPanel(new GridLayout(1,3,cellPadding,cellPadding));
								west.add(buttons,BorderLayout.SOUTH);
								{
									featuresAddJButton = new JButton();
									buttons.add(featuresAddJButton);
									featuresAddJButton.setText("Add...");
								}
								{
									featuresRemoveJButton = new JButton();
									buttons.add(featuresRemoveJButton);
									featuresRemoveJButton.setText("Remove");
								}
								{
									featuresEditJButton = new JButton();
									buttons.add(featuresEditJButton);
									featuresEditJButton.setText("Edit...");
								}
							}
						}
						{
							// center - feature configuration
							// ==============================

							JPanel center = new JPanel();
							center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
							remainderPanel.add(center,BorderLayout.CENTER);
							Dimension labelDim = new Dimension(200,20);

							{
								// feature name
								// ============

								JPanel name = new JPanel(new BorderLayout(cellPadding,cellPadding));
								center.add(name);
								{
									featuresFeatureNameJLabel = new JLabel();
									featuresFeatureNameJLabel.setVerticalAlignment(JLabel.TOP);
									featuresFeatureNameJLabel.setPreferredSize(labelDim);
									name.add(featuresFeatureNameJLabel,BorderLayout.WEST);
									featuresFeatureNameJLabel.setText("Feature Name");
									featuresFeatureNameJLabel.setFont(defaultLabelFont);
								}
								{
									featuresFeatureNameJTextField = new JTextField();
									featuresFeatureNameJTextField.setEditable(false);
									featuresFeatureNameJTextField.setPreferredSize(new Dimension(0,20));
									name.add(featuresFeatureNameJTextField,BorderLayout.CENTER);
								}
							}
							{
								// feature description
								// ===================

								JPanel desc = new JPanel(new BorderLayout(cellPadding,cellPadding));
								center.add(desc);
								{
									featuresFeatureDescJLabel = new JLabel();
									featuresFeatureDescJLabel.setVerticalAlignment(JLabel.TOP);
									featuresFeatureDescJLabel.setPreferredSize(labelDim);
									desc.add(featuresFeatureDescJLabel, BorderLayout.WEST);
									featuresFeatureDescJLabel.setText("Feature Description");
									featuresFeatureDescJLabel.setFont(defaultLabelFont);
								}
								{
									featuresFeatureDescJScrollPane = new JScrollPane();
									desc.add(featuresFeatureDescJScrollPane,BorderLayout.CENTER);
									{
										featuresFeatureDescJTextPane = new JTextPane();
										featuresFeatureDescJTextPane.setEditable(false);
										featuresFeatureDescJScrollPane.setViewportView(featuresFeatureDescJTextPane);
									}
								}
							}
							{
								// configuration headers
								// =====================

								JPanel configHeaders = new JPanel(new BorderLayout(cellPadding,cellPadding));
								center.add(configHeaders);
								{
									JLabel stub = new JLabel();
									stub.setPreferredSize(new Dimension(labelDim));
									configHeaders.add(stub,BorderLayout.WEST);
								}
								{
									JPanel headers = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
									configHeaders.add(headers,BorderLayout.CENTER);
									{
										featuresToolsJLabel = new JLabel();
										headers.add(featuresToolsJLabel);
										featuresToolsJLabel.setText("Tools");
										featuresToolsJLabel.setFont(defaultLabelFont);
									}
									{
										featuresFeatureConfigJLabel = new JLabel();
										headers.add(featuresFeatureConfigJLabel);
										featuresFeatureConfigJLabel.setText("Configuration");
										featuresFeatureConfigJLabel.setFont(defaultLabelFont);
									}
								}
							}
							{
								// feature extractor
								// =================

								JPanel extractor = new JPanel(new BorderLayout(cellPadding,cellPadding));
								center.add(extractor);
								{
									featuresFeatureExtractorJLabel = new JLabel();
									featuresFeatureExtractorJLabel.setVerticalAlignment(JLabel.TOP);
									featuresFeatureExtractorJLabel.setPreferredSize(labelDim);
									extractor.add(featuresFeatureExtractorJLabel,BorderLayout.WEST);
									featuresFeatureExtractorJLabel.setText("Feature Extractor");
									featuresFeatureExtractorJLabel.setFont(defaultLabelFont);
								}
								{
									JPanel config = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
									extractor.add(config,BorderLayout.CENTER);
									{
										featuresFeatureExtractorJScrollPane = new JScrollPane();
										config.add(featuresFeatureExtractorJScrollPane);
										{
											featuresFeatureExtractorContentJLabel = new JLabel();
											featuresFeatureExtractorContentJLabel.setVerticalAlignment(JLabel.TOP);
											featuresFeatureExtractorJScrollPane.setViewportView(featuresFeatureExtractorContentJLabel);
										}
									}
									{
										featuresFeatureExtractorConfigJScrollPane = new JScrollPane();
										config.add(featuresFeatureExtractorConfigJScrollPane);
									}
								}
							}
							{
								// canonicizers
								// ============

								JPanel canons = new JPanel(new BorderLayout(cellPadding,cellPadding));
								center.add(canons);
								{
									featuresCanonJLabel = new JLabel();
									featuresCanonJLabel.setVerticalAlignment(JLabel.TOP);
									featuresCanonJLabel.setPreferredSize(labelDim);
									canons.add(featuresCanonJLabel,BorderLayout.WEST);
									featuresCanonJLabel.setText("Text Pre-Processing");
									featuresCanonJLabel.setFont(defaultLabelFont);
								}
								{
									JPanel config = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
									canons.add(config,BorderLayout.CENTER);
									{
										featuresCanonListJScrollPane = new JScrollPane();
										config.add(featuresCanonListJScrollPane);
										{
											featuresCanonJListModel = 
													new DefaultComboBoxModel();
											featuresCanonJList = new JList();
											featuresCanonListJScrollPane.setViewportView(featuresCanonJList);
											featuresCanonJList.setModel(featuresCanonJListModel);
										}
									}
									{
										featuresCanonConfigJScrollPane = new JScrollPane();
										config.add(featuresCanonConfigJScrollPane);
									}
								}
							}
							{
								// cullers
								// =======

								JPanel cullers = new JPanel(new BorderLayout(cellPadding,cellPadding));
								center.add(cullers);
								{
									featuresCullJLabel = new JLabel();
									featuresCullJLabel.setVerticalAlignment(JLabel.TOP);
									featuresCullJLabel.setPreferredSize(labelDim);
									cullers.add(featuresCullJLabel,BorderLayout.WEST);
									featuresCullJLabel.setText("Feature Post-Processing");
									featuresCullJLabel.setFont(defaultLabelFont);
								}
								{
									JPanel config = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
									cullers.add(config,BorderLayout.CENTER);
									{
										featuresCullListJScrollPane = new JScrollPane();
										config.add(featuresCullListJScrollPane);
										{
											featuresCullJListModel = 
													new DefaultComboBoxModel();
											featuresCullJList = new JList();
											featuresCullListJScrollPane.setViewportView(featuresCullJList);
											featuresCullJList.setModel(featuresCullJListModel);
										}
									}
									{
										featuresCullConfigJScrollPane = new JScrollPane();
										config.add(featuresCullConfigJScrollPane);
									}
								}
							}
							{
								// normalization
								// =============

								JPanel norm = new JPanel(new BorderLayout(cellPadding,cellPadding));
								center.add(norm);
								{
									featuresNormJLabel = new JLabel();
									featuresNormJLabel.setVerticalAlignment(JLabel.TOP);
									featuresNormJLabel.setPreferredSize(labelDim);
									norm.add(featuresNormJLabel,BorderLayout.WEST);
									featuresNormJLabel.setText("Normalization");
									featuresNormJLabel.setFont(defaultLabelFont);
								}
								{
									featuresNormContentJLabel = new JLabel();
									norm.add(featuresNormContentJLabel,BorderLayout.CENTER);
									featuresNormContentJLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
								}
							}
							{
								// feature factor
								// ==============

								JPanel factor = new JPanel(new BorderLayout(cellPadding,cellPadding));
								center.add(factor);
								{
									featuresFactorJLabel = new JLabel();
									featuresFactorJLabel.setVerticalAlignment(JLabel.TOP);
									featuresFactorJLabel.setPreferredSize(labelDim);
									factor.add(featuresFactorJLabel,BorderLayout.WEST);
									featuresFactorJLabel.setText("Factor");
									featuresFactorJLabel.setFont(defaultLabelFont);
								}
								{
									featuresFactorContentJLabel = new JLabel();
									factor.add(featuresFactorContentJLabel,BorderLayout.CENTER);
									featuresFactorContentJLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
								}
							}
						}
					}

				}
				{
					// bottom toolbar buttons
					// ======================
					{
						JPanel bottomToolbar = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
						featuresTab.add(bottomToolbar,BorderLayout.SOUTH);
						{
							JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
							bottomToolbar.add(p);
							featuresAboutJButton = new JButton("About...");
							p.add(featuresAboutJButton);
						}
						{
							JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
							bottomToolbar.add(p);
							{
								featuresBackJButton = new JButton();
								p.add(featuresBackJButton);
								featuresBackJButton.setText("Back");
							}
							{
								featuresNextJButton = new JButton();
								p.add(featuresNextJButton);
								featuresNextJButton.setText("Next");
							}
						}
					}
				}

				/* ===============
				 * Classifiers tab
				 * ===============
				 */
				classTab = new JPanel(new BorderLayout(cellPadding,cellPadding));
				mainJTabbedPane.addTab("Classifiers", classTab);
				{
					// main center
					// ===========
					
					JPanel center = new JPanel(new GridLayout(2,1,cellPadding,cellPadding));
					classTab.add(center,BorderLayout.CENTER);
					
					{
						// available and selected classifiers
						// ==================================
						
						JPanel top = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
						center.add(top);
						
						{
							// available
							// =========
							JPanel av = new JPanel(new BorderLayout(cellPadding,cellPadding));
							top.add(av);
							{
								classAvClassJLabel = new JLabel();
								classAvClassJLabel.setFont(defaultLabelFont);
								av.add(classAvClassJLabel,BorderLayout.NORTH);
								classAvClassJLabel.setText("Available WEKA Classifiers");
							}
							{
								classTreeScrollPane = new JScrollPane();
								av.add(classTreeScrollPane,BorderLayout.CENTER);
								{
									classJTree = new JTree();
									classJTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
									classTreeScrollPane.setViewportView(classJTree);
									ClassTabDriver.initWekaClassifiersTree(this);
								}
							}
							{
								JPanel config = new JPanel(new GridLayout(3,1,cellPadding,cellPadding));
								av.add(config,BorderLayout.SOUTH);
								{
									classAvClassArgsJLabel = new JLabel();
									config.add(classAvClassArgsJLabel);
									classAvClassArgsJLabel.setText("Classifier Arguments");
									classAvClassArgsJLabel.setFont(defaultLabelFont);
								}
								{
									classAvClassArgsJTextField = new JTextField();
									config.add(classAvClassArgsJTextField);
								}
								{
									classAddJButton = new JButton();
									config.add(classAddJButton);
									classAddJButton.setText("Add");
								}
							}
						}
						{
							// selected
							// ========
							JPanel sel = new JPanel(new BorderLayout(cellPadding,cellPadding));
							top.add(sel);
							{
								classSelClassJLabel = new JLabel();
								sel.add(classSelClassJLabel,BorderLayout.NORTH);
								classSelClassJLabel.setText("Selected WEKA Classifiers");
								classSelClassJLabel.setFont(defaultLabelFont);
							}
							{
								classSelClassJScrollPane = new JScrollPane();
								sel.add(classSelClassJScrollPane,BorderLayout.CENTER);
								{
									classSelClassJListModel = 
											new DefaultComboBoxModel();
									classJList = new JList();
									classJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
									classSelClassJScrollPane.setViewportView(classJList);
									classJList.setModel(classSelClassJListModel);
								}
							}
							{
								JPanel config = new JPanel(new GridLayout(3,1,cellPadding,cellPadding));
								sel.add(config,BorderLayout.SOUTH);
								{
									classSelClassArgsJLabel = new JLabel();
									config.add(classSelClassArgsJLabel);
									classSelClassArgsJLabel.setText("Classifier Arguments");
									classSelClassArgsJLabel.setFont(defaultLabelFont);
								}
								{
									classSelClassArgsJTextField = new JTextField();
									config.add(classSelClassArgsJTextField);
								}
								{
									classRemoveJButton = new JButton();
									config.add(classRemoveJButton);
									classRemoveJButton.setText("Remove");
								}
							}
						}
					}
					
					{
						// classifier description
						// ======================
						
						JPanel bottom = new JPanel(new BorderLayout(cellPadding,cellPadding));
						center.add(bottom);
						{
							classDescJLabel = new JLabel();
							bottom.add(classDescJLabel,BorderLayout.NORTH);
							classDescJLabel.setText("Classifier Description");
							classDescJLabel.setFont(defaultLabelFont);
						}
						{
							classDescJScrollPane = new JScrollPane();
							bottom.add(classDescJScrollPane,BorderLayout.CENTER);
							{
								classDescJTextPane = new JTextPane();
								classDescJTextPane.setEditable(false);
								classDescJScrollPane.setViewportView(classDescJTextPane);
							}
						}
					}
				}
				{
					// bottom toolbar buttons
					// ======================
					{
						JPanel bottomToolbar = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
						classTab.add(bottomToolbar,BorderLayout.SOUTH);
						{
							JPanel bottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
							bottomToolbar.add(bottomLeft);
							classAboutJButton = new JButton("About...");
							bottomLeft.add(classAboutJButton);
						}
						{
							JPanel bottomRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
							bottomToolbar.add(bottomRight,BorderLayout.SOUTH);
							{
								classBackJButton = new JButton();
								bottomRight.add(classBackJButton);
								classBackJButton.setText("Back");
							}
							{
								classNextJButton = new JButton();
								bottomRight.add(classNextJButton);
								classNextJButton.setText("Next");
							}
						}
					}
					
					
				}
				
				/* ============
				 * Analysis tab
				 * ============
				 */
				analysisTab = new JPanel(new BorderLayout(cellPadding,cellPadding));
				mainJTabbedPane.addTab("Analysis", analysisTab);
				analysisTab.setLayout(new BorderLayout(cellPadding,cellPadding));
				{
					// header
					// ======
					
					//JPanel header = new JPanel(new GridLayout(5,3,cellPadding,cellPadding));
					//JPanel header = new JPanel(new GridLayout(3,3,cellPadding,cellPadding));
					JPanel header = new JPanel(new GridLayout(1,3,cellPadding,cellPadding));
					analysisTab.add(header,BorderLayout.NORTH);
					
					// configuration
					JPanel analysisConfPanel = new JPanel(new BorderLayout(cellPadding,cellPadding));
					header.add(analysisConfPanel);
					{
						analysisConfigJLabel = new JLabel();
						analysisConfPanel.add(analysisConfigJLabel,BorderLayout.NORTH);
						analysisConfigJLabel.setText("Configuration");
						analysisConfigJLabel.setFont(defaultLabelFont);
						
						// options
						JPanel options = new JPanel(new GridLayout(4,1,cellPadding,cellPadding));
						analysisConfPanel.add(options,BorderLayout.CENTER);
						{
							analysisOutputFeatureVectorJCheckBox = new JCheckBox();
							analysisOutputFeatureVectorJCheckBox.setSelected(true);
							options.add(analysisOutputFeatureVectorJCheckBox);
							analysisOutputFeatureVectorJCheckBox.setText("Output feature vectors (ARFF format)");
						}
						{
							analysisSparseInstancesJCheckBox = new JCheckBox();
							analysisSparseInstancesJCheckBox.setSelected(true);
							options.add(analysisSparseInstancesJCheckBox);
							analysisSparseInstancesJCheckBox.setText("Use sparse representation for feature vectors");
						}
						{
							analysisCalcInfoGainJCheckBox = new JCheckBox();
							analysisCalcInfoGainJCheckBox.setSelected(true);
							options.add(analysisCalcInfoGainJCheckBox);
							analysisCalcInfoGainJCheckBox.setText("Calculate InfoGain on feature set");
						}
						{
							JPanel applyIG = new JPanel(new FlowLayout(FlowLayout.LEFT));
							options.add(applyIG);
							{
								analysisApplyInfoGainJCheckBox = new JCheckBox();
								applyIG.add(analysisApplyInfoGainJCheckBox);
								analysisApplyInfoGainJCheckBox.setText("Apply InfoGain on top N features:");
							}
							{
								infoGainValueJTextField = new JTextField("200");
								infoGainValueJTextField.setColumns(5);
								applyIG.add(infoGainValueJTextField);
								infoGainValueJTextField.setEnabled(false);
							}
						}
					}					
					
					
					// analysis type
					JPanel analysisTypePanel = new JPanel(new BorderLayout(cellPadding,cellPadding));
					header.add(analysisTypePanel);
					{
						analysisTypeJLabel = new JLabel();
						analysisTypeJLabel.setFont(defaultLabelFont);
						analysisTypePanel.add(analysisTypeJLabel,BorderLayout.NORTH);
						analysisTypeJLabel.setText("Analysis Type");
						
						// options
						JPanel options = new JPanel(new GridLayout(2,1,cellPadding,cellPadding));
						analysisTypePanel.add(options,BorderLayout.CENTER);
						analysisTypeButtonGroup = new ButtonGroup();
						{
							analysisClassTestDocsJRadioButton = new JRadioButton();
							analysisClassTestDocsJRadioButton.setSelected(true);
							options.add(analysisClassTestDocsJRadioButton,BorderLayout.CENTER);
							analysisTypeButtonGroup.add(analysisClassTestDocsJRadioButton);
							analysisClassTestDocsJRadioButton.setText("Train on training corpus and classify test documents");
						}
						{
							analysisTrainCVJRadioButton = new JRadioButton();
							options.add(analysisTrainCVJRadioButton,BorderLayout.CENTER);
							analysisTypeButtonGroup.add(analysisTrainCVJRadioButton);
							analysisTrainCVJRadioButton.setText("Run 10-folds cross validation on training corpus");
						}
					}
					
					
					// post-analysis
					JPanel postAnalysisPanel = new JPanel(new BorderLayout(cellPadding,cellPadding));
					header.add(postAnalysisPanel);
					{
						analysisPostAnalysisJLabel = new JLabel();
						postAnalysisPanel.add(analysisPostAnalysisJLabel,BorderLayout.NORTH);
						analysisPostAnalysisJLabel.setText("Post Analysis");
						analysisPostAnalysisJLabel.setFont(defaultLabelFont);
						
						// options
						JPanel options = new JPanel(new GridLayout(2,1,cellPadding,cellPadding));
						postAnalysisPanel.add(options,BorderLayout.CENTER);
						{
							JPanel trainButtons = new JPanel(new GridLayout(2,1,cellPadding,cellPadding));
							options.add(trainButtons);
							{
								trainButtons.add(new JLabel("Training corpus features:"));
								JPanel buttons = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
								trainButtons.add(buttons);
								{
									analysisExportTrainToARFFJButton = new JButton();
									buttons.add(analysisExportTrainToARFFJButton);
									analysisExportTrainToARFFJButton.setText("Save to ARFF...");
								}
								{
									analysisExportTrainToCSVJButton = new JButton();
									buttons.add(analysisExportTrainToCSVJButton);
									analysisExportTrainToCSVJButton.setText("Save to CSV...");
								}
							}
							JPanel testButtons = new JPanel(new GridLayout(2,1,cellPadding,cellPadding));
							options.add(testButtons);
							{
								testButtons.add(new JLabel("Test documents features:"));
								JPanel buttons = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
								testButtons.add(buttons);
								{
									analysisExportTestToARFFJButton = new JButton();
									buttons.add(analysisExportTestToARFFJButton);
									analysisExportTestToARFFJButton.setText("Save to ARFF...");
								}
								{
									analysisExportTestToCSVJButton = new JButton();
									buttons.add(analysisExportTestToCSVJButton);
									analysisExportTestToCSVJButton.setText("Save to CSV...");
								}
							}
						}
					}
					
					/*
					// line 4
					header.add(new JPanel());
					header.add(new JPanel());
					{
						analysisOutputConfusionMatrixJCheckBox = new JCheckBox();
						analysisOutputConfusionMatrixJCheckBox.setSelected(true);
						header.add(analysisOutputConfusionMatrixJCheckBox);
						analysisOutputConfusionMatrixJCheckBox.setText("Output Weka confusion matrix");
					}
					header.add(new JPanel());
					
					// line 5
					header.add(new JPanel());
					{
						analysisOutputAccByClassJCheckBox = new JCheckBox();
						analysisOutputAccByClassJCheckBox.setSelected(true);
						header.add(analysisOutputAccByClassJCheckBox);
						analysisOutputAccByClassJCheckBox.setText("Output Weka detailed accuracy by class");
					}
					header.add(new JPanel());
					*/
				}
				{
					// main
					// ====
					
					JPanel main = new JPanel(new BorderLayout(cellPadding,cellPadding));
					analysisTab.add(main,BorderLayout.CENTER);
					{
						JPanel mainHeader = new JPanel(new BorderLayout(cellPadding,cellPadding));
						main.add(mainHeader,BorderLayout.NORTH);
						
						// buttons
						JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
						mainHeader.add(buttons,BorderLayout.NORTH);
						{
							analysisRunJButton = new JButton();
							analysisRunJButton.setPreferredSize(new Dimension(100,50));
							buttons.add(analysisRunJButton);
							analysisRunJButton.setText("Run Analysis");
						}
						{
							analysisStopJButton = new JButton();
							analysisStopJButton.setPreferredSize(new Dimension(100,50));
							buttons.add(analysisStopJButton);
							analysisStopJButton.setText("Stop");
							analysisStopJButton.setEnabled(false);
						}
						
						// results header
						{
							analysisResultsJLabel = new JLabel();
							analysisResultsJLabel.setText("Results");
							analysisResultsJLabel.setHorizontalAlignment(JLabel.LEFT);
							analysisResultsJLabel.setFont(defaultLabelFont);
							mainHeader.add(analysisResultsJLabel,BorderLayout.CENTER);
						}
					}
					{
						analysisResultsJTabbedPane = new JTabbedPane();
						analysisResultsJTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
						main.add(analysisResultsJTabbedPane,BorderLayout.CENTER);
					}
					{
						JPanel resultsBottom = new JPanel(new GridLayout(1,3,cellPadding,cellPadding));
						main.add(resultsBottom,BorderLayout.SOUTH);
						{
							JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
							resultsBottom.add(p);
							analysisSaveResultsJButton = new JButton("Save Results...");
							p.add(analysisSaveResultsJButton);
						}
						{
							analysisJProgressBar = new JProgressBar();
							resultsBottom.add(analysisJProgressBar,BorderLayout.SOUTH);
						}
						resultsBottom.add(new JPanel());
						
					}
				}
				{
					// bottom toolbar buttons
					// ======================
					{
						JPanel bottomToolbar = new JPanel(new GridLayout(1,2,cellPadding,cellPadding));
						analysisTab.add(bottomToolbar,BorderLayout.SOUTH);
						{
							JPanel bottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
							bottomToolbar.add(bottomLeft);
							analysisAboutJButton = new JButton("About...");
							bottomLeft.add(analysisAboutJButton);
						}
						{
							JPanel bottomRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
							bottomToolbar.add(bottomRight,BorderLayout.SOUTH);
							{
								analysisBackJButton = new JButton();
								bottomRight.add(analysisBackJButton);
								analysisBackJButton.setText("Back");
							}
						}
					}
				}
			}

			// initialize listeners
			DocsTabDriver.initListeners(this);
			FeaturesTabDriver.initListeners(this);
			ClassTabDriver.initListeners(this);
			AnalysisTabDriver.initListeners(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
