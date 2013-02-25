package edu.drexel.psal.anonymouth.gooie;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import edu.drexel.psal.JSANConstants;
import edu.drexel.psal.jstylo.generics.*;
import edu.drexel.psal.jstylo.generics.Logger.LogOut;
import edu.drexel.psal.anonymouth.gooie.Translation;
import edu.drexel.psal.anonymouth.gooie.DocsTabDriver.ExtFilter;
import edu.drexel.psal.anonymouth.utils.ConsolidationStation;

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.*;
import javax.swing.tree.*;

import net.miginfocom.swing.MigLayout;

import com.jgaap.generics.Document;

import weka.classifiers.*;

import edu.drexel.psal.jstylo.analyzers.WekaAnalyzer;

public class PreProcessSettingsFrame extends JDialog
{
	
	
//	// main instance
//		public static GUIMain inst;
//
//		// ------------------------
//
//		// data
//		protected ProblemSet ps;
//		protected CumulativeFeatureDriver cfd;
//		protected List<CumulativeFeatureDriver> presetCFDs;
//		protected WekaInstancesBuilder wib;
//		protected WekaAnalyzer wad;
//		protected List<Classifier> classifiers;
//		protected Thread analysisThread;
//		protected List<String> results;
//
//		protected String defaultTrainDocsTreeName = "Authors"; 
//	protected Font defaultLabelFont = new Font("Verdana",0,16);
//		protected static int cellPadding = 5;
//		
//		// possible color scheme
//		protected final Color lightblue = new Color(126, 181, 214);
//		protected final Color medblue = new Color(42, 117, 169);
//		protected final Color darkblue = new Color(39, 66, 87);
//		protected final Color lightbrown = new Color(223, 193, 132);
//		protected final Color medbrown = new Color(143, 96, 72);
//		protected final Color darkbrown = new Color(100, 68, 54);
//		
//		protected final Color ready = new Color(0,255,128);
//		protected final Color notReady = new Color(255,102,102);
//
//		// tabs
//		protected JTabbedPane mainJTabbedPane;
//		protected JPanel docsTab;
//		protected JPanel featuresTab;
//		protected JPanel classTab;
//		protected JPanel editorTab;
//		
//		// documents tab
//		protected String propFileName = "jsan_resources/anonymouth_prop.prop";
//		File propFile = new File(propFileName);
//		Properties prop = new Properties();
//		protected JFileChooser load = new JFileChooser();
//		protected JFileChooser save = new JFileChooser();
//		
//		protected JLabel testDocsJLabel;
//		protected JButton trainDocPreviewJButton;
//		protected JButton testDocPreviewJButton;
//		protected JButton trainNameJButton;
//		protected JLabel docPreviewJLabel;
//		protected JButton newProblemSetJButton;
//		protected JTextPane docPreviewJTextPane;
//		protected JScrollPane docPreviewJScrollPane;

//		protected JButton docTabNextJButton;
//		protected JButton removeAuthorJButton;

//		
//		protected JTable testDocsJTable;
//		protected DefaultTableModel testDocsTableModel;
//		
//		protected JLabel docPreviewNameJLabel;
//		protected JLabel corpusJLabel;

//		protected JButton clearDocPreviewJButton;
//		protected JButton docsAboutJButton;
//		protected JTable userSampleDocsJTable;
//		protected DefaultTableModel userSampleDocsTableModel;
//		protected JLabel userSampleDocsJLabel;
//		protected JPanel buttons;

//		protected JButton userSampleDocPreviewJButton;
//
		// features tab
		protected static int cellPadding = 5;
	
		protected JLabel featuresToolsJLabel;
		protected JButton featuresNextJButton;
		protected JButton featuresBackJButton;
		protected JLabel featuresFeatureConfigJLabel;
		
		protected JLabel featuresFeatureExtractorContentJLabel;
//		protected JScrollPane featuresFeatureExtractorJScrollPane;
//		protected JScrollPane featuresFeatureExtractorConfigJScrollPane;
//		protected JScrollPane featuresCullConfigJScrollPane;
//		protected JScrollPane featuresCanonConfigJScrollPane;
//		protected JList featuresCullJList;
//		protected DefaultComboBoxModel featuresCullJListModel;
//		protected JScrollPane featuresCullListJScrollPane;
//		protected JScrollPane featuresCanonListJScrollPane;
//		protected JList featuresCanonJList;
//		protected DefaultComboBoxModel featuresCanonJListModel;
//		protected JScrollPane featuresFeatureDescJScrollPane;
		
		protected JLabel featuresFeatureExtractorJLabel;
		protected JLabel featuresFactorJLabel;
		protected JLabel featuresNormJLabel;
		protected JLabel featuresFeatureDescJLabel;
		
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
		protected JLabel featuresListLabel;
		protected JLabel featuresInfoLabel;
//
		// Calssifiers tab
		protected JTextField classAvClassArgsJTextField;
		protected JLabel classAvClassArgsJLabel;
		protected JComboBox classClassJComboBox;
		protected JLabel classAvClassJLabel;
		protected JButton classAddJButton;
		
		protected JTextField classSelClassArgsJTextField;
		protected JLabel classSelClassArgsJLabel;
		protected JScrollPane classSelClassJScrollPane;
		protected DefaultListModel classSelClassJListModel;
		protected JScrollPane classTreeScrollPane;
		protected JScrollPane classDescJScrollPane;
		protected JTextPane classDescJTextPane;
		protected JLabel classDescJLabel;
		protected JButton classBackJButton;
		protected JButton classNextJButton;
		protected JLabel classSelClassJLabel;
		protected JButton classRemoveJButton;
		protected JButton classAboutJButton;
//		
//		// Editor tab
//		
//		
//		protected JScrollPane theEditorScrollPane;
//		protected JTable suggestionTable;
//		protected JTextPane editorBox;
//		protected JTable resultsTable;
//		protected JLabel classificationLabel;
//		//protected JButton addSentence;
//		protected JPanel editorRowTwoButtonBufferPanel;
//		protected JPanel buttonBufferJPanel;
//		protected JPanel editorBottomRowButtonPanel;
//		protected JPanel editorTopRowButtonsPanel;
//		protected JPanel editorButtonJPanel;
//		protected JPanel editorInteractionWestPanel;
//		protected JPanel editorInteractionJPanel;
//		protected JPanel jPanel2;
//		protected JPanel dummyPanelUpdatorLeftSide;
//		protected JPanel elementsToAddBoxLabelJPanel;
//		protected JPanel suggestionBoxLabelJPanel;
//		protected JPanel jPanel1;
//		protected JPanel valueLabelJPanel;
//		protected JPanel valueBoxPanel;
//		protected JPanel updaterJPanel;
		//-------------- HELP TAB PANE STUFF ---------
		protected JTabbedPane editorHelpTabPane;
		
		protected JPanel editorHelpPrepPanel;
		protected JButton prepAdvButton;
			protected JPanel prepDocumentsPanel;
				protected JPanel prepMainDocPanel;
					protected JLabel prepDocLabel;
					protected JButton loadProblemSetJButton;
					protected JButton saveProblemSetJButton;
					protected JList prepMainDocList;
					protected JScrollPane prepMainDocScrollPane;
					protected JPanel mainDocSettingsPanel;
						protected JLabel mainDocSettingsFullPathLabel;
						protected JLabel mainDocSettingsSizeLabel;
						protected JLabel mainDocSettingsLastModifiedLabel;
					protected JButton removeTestDocJButton;
					protected JButton addTestDocJButton;
				protected JPanel prepSampleDocsPanel;
					protected JList prepSampleDocsList;
					protected JScrollPane prepSampleDocsScrollPane;
					protected JPanel sampleDocSettingsPanel;
						protected JLabel sampleDocSettingsFullPathLabel;
						protected JLabel sampleDocSettingsSizeLabel;
						protected JLabel sampleDocSettingsLastModifiedLabel;
					protected JButton adduserSampleDocJButton;
					protected JButton removeuserSampleDocJButton;
				protected JPanel prepTrainDocsPanel;
					protected JTree trainCorpusJTree;
					protected JScrollPane trainCorpusJTreeScrollPane;
					protected JPanel trainDocSettingsPanel;
						protected JLabel trainDocSettingsFullPathLabel;
						protected JLabel trainDocSettingsSizeLabel;
						protected JLabel trainDocSettingsLastModifiedLabel;
					protected JButton removeTrainDocsJButton;
					protected JButton addTrainDocsJButton;
			protected JPanel prepFeaturesPanel;
				protected JLabel prepFeatLabel;
			protected JPanel prepClassifiersPanel;
				protected JLabel prepClassLabel;
				protected JPanel prepAvailableClassPanel;
					protected JTree classJTree;
					protected JScrollPane prepAvailableClassScrollPane;
				protected JPanel prepSelectedClassPanel;
					protected JList classJList;
					protected JScrollPane prepSelectedClassScrollPane;
		
		protected JPanel editorHelpSugPanel;
			protected JPanel elementsPanel;
			protected JPanel elementsToAddPanel;
			protected JLabel elementsToAddLabel;
			protected JTextPane elementsToAddPane;
			protected JScrollPane elementsToAddScrollPane;
			protected JPanel elementsToRemovePanel;
			protected JLabel elementsToRemoveLabel;
			protected JTextPane elementsToRemovePane;
			protected JScrollPane elementsToRemoveScrollPane;
			
		protected JPanel editorHelpTransPanel;
			protected JPanel translationsPanel;
			protected JLabel translationsLabel;
			protected JTable translationsTable;
			protected JScrollPane translationsScrollPane;
			protected JComboBox translationsComboBox;
		
		protected JPanel editorHelpInfoPanel;
			protected JLabel sentenceEditorLabel;
			protected JLabel documentViewerLabel;
			protected JLabel classificationResultsLabel;
			protected JTextPane descriptionPane;
			
			protected JPanel instructionsPanel;
			protected JLabel instructionsLabel;
			protected JTextPane instructionsPane;
			protected JScrollPane instructionsScrollPane;
			protected JPanel synonymsPanel;
			protected JLabel synonymsLabel;
			protected JTextPane synonymsPane;
			protected JScrollPane synonymsScrollPane;
		//--------------------------------------------
//		
//		protected JPanel editorInfoJPanel;
//		protected JScrollPane editorInteractionScrollPane;
//		protected JScrollPane EditorInfoScrollPane;
//		protected JTabbedPane editTP;
//		
//		protected JScrollPane wordsToAddPane;
//		//protected JButton nextSentenceButton;
//		//protected JButton refreshButtonEditor;
//		//protected JButton lastSentenceButton;
//		//protected JButton prevSentenceButton;
//		//protected JButton transButton;
//		protected JTextField searchInputBox;
//		protected JComboBox highlightSelectionBox;
//		protected JLabel highlightLabel;
//		protected JPanel jPanel_IL3;
//		protected JButton clearHighlightingButton;
//		//protected JProgressBar editorProgressBar;
//		//protected JLabel editingProgressBarLabel;
//		protected JLabel featureNameLabel;
//		protected JLabel targetValueLabel;
//		protected JLabel presentValueLabel;
//		protected JTextField targetValueField;
//		protected JTextField presentValueField;
//		protected JLabel suggestionListLabel;
//		//protected JButton saveButton;
//		//protected JButton exitButton;
//		protected JButton verboseButton;
//		//protected JButton dictButton;
//		protected JLabel resultsTableLabel;
//		protected JScrollPane resultsTablePane;
//		protected JScrollPane editBox;
//		protected JPanel editBoxPanel;
//		protected JScrollPane suggestionListPane;
//
//		// Cluster tab
//		protected JScrollPane theScrollPane;
//		protected JPanel examplePlotPanel;
//		protected JPanel topPanel;
//		protected JPanel secondPanel;
//		protected JPanel holderPanel;
//		protected JButton refreshButton;
//		protected JTabbedPane clusterTab;
//		protected JCheckBox shouldCloseBox;
//		protected JButton selectClusterConfiguration;
//		protected JComboBox clusterConfigurationBox;
//		protected JPanel Targets;
//		protected JButton reClusterAllButton;
//		protected JLayeredPane[] clusterViewerLayoverPanes;
//		protected JPanel[] namePanels;
//		
//		// Analysis tab
//		protected JCheckBox analysisOutputAccByClassJCheckBox;
//		protected JCheckBox analysisOutputConfusionMatrixJCheckBox;
//		protected ButtonGroup analysisTypeButtonGroup;
//		
//		protected static ImageIcon iconNO;
//		protected static ImageIcon iconFINISHED;
//		public static ImageIcon icon;
//		
//		protected Translation GUITranslator = new Translation();
	
	protected GUIMain main;
	public boolean panelsAreMade = false;
	
	protected JSplitPane splitPane;
	protected JScrollPane treeScrollPane;
	protected JScrollPane mainScrollPane;
	protected JScrollPane bottomScrollPane;
	
	protected JPanel treePanel;
	protected JTree tree;
	protected DefaultMutableTreeNode top;
	
	protected JPanel mainPanel;
	
	protected JPanel docPanel;
	protected JPanel docMainPanel;
	protected JButton clearProblemSetJButton;
	
	protected JPanel featPanel;
	protected JPanel featMainPanel;
	protected JTextPane featuresFeatureNameJTextPane;
	protected JTextPane featuresFeatureDescJTextPane;
	protected JTextPane featuresNormContentJTextPane;
	protected JTextPane featuresFactorContentJTextPane;
	protected JTable featuresFeatureExtractorContentJTable;
	protected JTable featuresFeatureExtractorConfigJTable;
	protected JTable featuresCanonJTable;
	protected JTable featuresCanonConfigJTable;
	protected JTable featuresCullJTable;
	protected JTable featuresCullConfigJTable;
	protected DefaultTableModel featuresFeatureExtractorContentJTableModel;
	protected DefaultTableModel featuresFeatureExtractorConfigJTableModel;
	protected DefaultTableModel featuresCanonJTableModel;
	protected DefaultTableModel featuresCanonConfigJTableModel;
	protected DefaultTableModel featuresCullJTableModel;
	protected DefaultTableModel featuresCullConfigJTableModel;
	
	
	protected JPanel classPanel;
	protected JPanel classMainPanel;
	
	protected JPanel bottomPanel;
	protected JButton okButton;
	protected JButton cancelButton;
	
	public PreProcessSettingsFrame(GUIMain main)
	{
		super(main, "Pre-Process Settings", Dialog.ModalityType.APPLICATION_MODAL);
		init(main);
		setVisible(false);
	}
	
	private void init(GUIMain main)
	{
		this.main = main;
		this.setIconImage(new ImageIcon(getClass().getResource(JSANConstants.JSAN_GRAPHICS_PREFIX+"Anonymouth_LOGO.png")).getImage());
		initPanels();
		getContentPane().setLayout(new MigLayout(
				"fill, wrap 1, ins 0, gap 0 0",
				"fill, grow",
				"[grow][grow, shrink 0, 40!]"));
		{
			// main panel must be created before the tree panel, but added to content pane after the tree panel
			// when tree is initialized it selects the first leaf and needs the main panel to be created
			mainPanel = new JPanel();
			mainPanel.setLayout(new MigLayout(
					"fill, wrap 1, ins 0, gap 0 0",
					"fill",
					"fill"));
			mainPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY));
			
			treePanel = new JPanel();
			treePanel.setLayout(new MigLayout(
					"",
					"fill",
					"fill"));
			treePanel.setBackground(Color.WHITE);
			treeScrollPane = new JScrollPane(treePanel);
			{
				top = new DefaultMutableTreeNode("Pre-Process");
				tree = new JTree(top);
				initializeTree(tree, top);
			}
			treePanel.add(tree);
			
			bottomPanel = new JPanel();
			bottomPanel.setLayout(new MigLayout(
					"right",
					"right",
					"bottom"));
			bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));
			{
				okButton = new JButton("Ok");
				okButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						closeWindow();
					}
				});
				bottomPanel.add(okButton);
				
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						closeWindow();
					}
				});
				bottomPanel.add(cancelButton);
			}
			getContentPane().add(treePanel, "split 2, growy, shrinkx 0");
			getContentPane().add(mainPanel, "grow");
			getContentPane().add(bottomPanel, "h 40!, span 2, shrinky 0");
		}
		
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(new Dimension((int)(screensize.width*.9), (int)(screensize.height*.9)));
		this.setLocationRelativeTo(null); // makes it form in the center of the screen
	}
	
	private void initializeTree(JTree tree, DefaultMutableTreeNode top) {
	    DefaultMutableTreeNode section = null;
	    DefaultMutableTreeNode subSection = null;
	    
	    section = new DefaultMutableTreeNode("Documents");
	    top.add(section);
	    
	    section = new DefaultMutableTreeNode("Features");
	    top.add(section);
	    
	    section = new DefaultMutableTreeNode("Classifiers");
	    top.add(section);
	    
	    TreeSelectionListener treeListener = new TreeSelectionListener()
    	{
			@Override
			public void valueChanged(TreeSelectionEvent e) 
			{
				String name = e.getPath().getLastPathComponent().toString();
				if (name.equals("Documents"))
				{
					showPanel(docPanel);
				}
				else if (name.equals("Features"))
				{
					showPanel(featPanel);
				}
				else if (name.equals("Classifiers"))
				{
					showPanel(classPanel);
				}
				else
				{}
			}
		};
		
		tree.addTreeSelectionListener(treeListener);
		tree.expandRow(0);
		tree.setSelectionRow(1);
	}
	
	public void openWindow()
	{
		this.setVisible(true);
		this.setLocationRelativeTo(null); // makes it form in the center of the screen
	}
	
	public void closeWindow() 
	{
		//main.setEnabled(true);
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}
	
	private void showPanel(JPanel panel)
	{
//		if (docPanel == null || featPanel == null || classPanel == null)
//			makePanels();
		mainPanel.removeAll();
		mainPanel.add(panel);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	/**
	 * Initializes all of the panels on the tree. Must be called before the tree is created, because when the tree
	 * is initialized it selects the first leaf in the tree which causes the panel to be shown.
	 */
	private void initPanels()
	{
		//==========================================================================================
		//================================ Documents Panel =========================================
		//==========================================================================================
		docPanel = new JPanel();
		
		MigLayout docLayout = new MigLayout(
				"wrap",
				"grow, fill",
				"[30][grow, fill]");
		docPanel.setLayout(docLayout);
		//prepDocumentsPanel.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK));
		{
			// Documents Label-----------------------------
			prepDocLabel = new JLabel("Documents:");
			prepDocLabel.setFont(new Font("Ariel", Font.BOLD, 15));
			prepDocLabel.setHorizontalAlignment(SwingConstants.CENTER);
			prepDocLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			prepDocLabel.setOpaque(true);
			if (main.documentsAreReady())
				prepDocLabel.setBackground(main.ready);
			else
				prepDocLabel.setBackground(main.notReady);
			
			docMainPanel = new JPanel();
			docMainPanel.setLayout(new MigLayout(
					"wrap 3",
					"grow, fill",
					"[20][20]0[grow][20][grow]"));
			{
				JPanel docMainTopPanel = new JPanel();
				docMainTopPanel.setLayout(new MigLayout());
				{
					// combo box label---------------------------------------------
					JLabel problemSetJLabel = new JLabel("Problem Set:");
					docMainTopPanel.add(problemSetJLabel);
					
					// Save Problem Set button--------------------------------
					saveProblemSetJButton = new JButton("Save");
					docMainTopPanel.add(saveProblemSetJButton);
					
					// load problem set button-------------------------------
					loadProblemSetJButton = new JButton("Load");
					docMainTopPanel.add(loadProblemSetJButton);
					
					clearProblemSetJButton = new JButton("Clear");
					docMainTopPanel.add(clearProblemSetJButton);
				}
				
				// main label-----------------------------------
				JLabel mainLabel = new JLabel("Main:");
				mainLabel.setHorizontalAlignment(SwingConstants.CENTER);
				mainLabel.setOpaque(true);
				mainLabel.setBackground(main.tan);
				mainLabel.setBorder(BorderFactory.createRaisedBevelBorder());
				
				// sample label--------------------------------
				JLabel sampleLabel = new JLabel("Sample:");
				sampleLabel.setHorizontalAlignment(SwingConstants.CENTER);
				sampleLabel.setOpaque(true);
				sampleLabel.setBackground(main.tan);
				sampleLabel.setBorder(BorderFactory.createRaisedBevelBorder());
				
				// train label----------------------------------
				JLabel trainLabel = new JLabel("Other Authors:");
				trainLabel.setHorizontalAlignment(SwingConstants.CENTER);
				trainLabel.setOpaque(true);
				trainLabel.setBackground(main.tan);
				trainLabel.setBorder(BorderFactory.createRaisedBevelBorder());
				
				
				// main documents list---------------------------------------------
				DefaultListModel mainDocListModel = new DefaultListModel();
				prepMainDocList = new JList(mainDocListModel);
				prepMainDocList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				prepMainDocScrollPane = new JScrollPane(prepMainDocList);
				
				// sample documents list-----------------------------------------
				DefaultListModel sampleDocsListModel = new DefaultListModel();
				prepSampleDocsList = new JList(sampleDocsListModel);
				prepSampleDocsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				prepSampleDocsScrollPane = new JScrollPane(prepSampleDocsList);
				
				// train tree---------------------------------------------------
				DefaultMutableTreeNode top = new DefaultMutableTreeNode(main.ps.getTrainCorpusName());
				trainCorpusJTree = new JTree(top);
				trainCorpusJTreeScrollPane = new JScrollPane(trainCorpusJTree);
				
				// main add button----------------------------------------------
				addTestDocJButton = new JButton("Add");
				
				// main delete button-------------------------------------------
				removeTestDocJButton = new JButton("Delete");
				
				// sample add button-------------------------------------------
				adduserSampleDocJButton = new JButton("Add");
				
				// sample delete button------------------------------------------
				removeuserSampleDocJButton = new JButton("Delete");
				
				// train add button-------------------------------------------
				addTrainDocsJButton = new JButton("Add");
				
				// train delete button----------------------------------------
				removeTrainDocsJButton = new JButton("Delete");
				
				// main doc settings panel-------------------------------------------
				mainDocSettingsPanel = new JPanel();
				//mainDocSettingsPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				mainDocSettingsPanel.setLayout(new MigLayout("wrap 1", "grow", "top"));
				{
					mainDocSettingsFullPathLabel = new JLabel("Location:");
					mainDocSettingsPanel.add(mainDocSettingsFullPathLabel);
					
					mainDocSettingsSizeLabel = new JLabel("Size:");
					mainDocSettingsPanel.add(mainDocSettingsSizeLabel);
					
					mainDocSettingsLastModifiedLabel = new JLabel("Last Modified:");
					mainDocSettingsPanel.add(mainDocSettingsLastModifiedLabel);
				}
				
				// sample doc settings panel-------------------------------------------
				sampleDocSettingsPanel = new JPanel();
				//sampleDocSettingsPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				sampleDocSettingsPanel.setLayout(new MigLayout("wrap 1", "grow", "top"));
				{
					sampleDocSettingsFullPathLabel = new JLabel("Location:");
					sampleDocSettingsPanel.add(sampleDocSettingsFullPathLabel);
					
					sampleDocSettingsSizeLabel = new JLabel("Size:");
					sampleDocSettingsPanel.add(sampleDocSettingsSizeLabel);
					
					sampleDocSettingsLastModifiedLabel = new JLabel("Last Modified:");
					sampleDocSettingsPanel.add(sampleDocSettingsLastModifiedLabel);
				}
				
				// train doc settings panel-------------------------------------------
				trainDocSettingsPanel = new JPanel();
				//trainDocSettingsPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				trainDocSettingsPanel.setLayout(new MigLayout("wrap 1", "grow", "top"));
				{
					trainDocSettingsFullPathLabel = new JLabel("Location:");
					trainDocSettingsPanel.add(trainDocSettingsFullPathLabel);
					
					trainDocSettingsSizeLabel = new JLabel("Size:");
					trainDocSettingsPanel.add(trainDocSettingsSizeLabel);
					
					trainDocSettingsLastModifiedLabel = new JLabel("Last Modified:");
					trainDocSettingsPanel.add(trainDocSettingsLastModifiedLabel);
				}
				
				docMainPanel.add(docMainTopPanel, "span");
				docMainPanel.add(mainLabel);
				docMainPanel.add(sampleLabel);
				docMainPanel.add(trainLabel);
				docMainPanel.add(prepMainDocScrollPane, "grow, h 100::, w 100::");
				docMainPanel.add(prepSampleDocsScrollPane, "grow, h 100::, w 100::");
				docMainPanel.add(trainCorpusJTreeScrollPane, "grow, h 100::, w 100::");
				docMainPanel.add(addTestDocJButton, "split 2, w 100::");
				docMainPanel.add(removeTestDocJButton, "w 100::");
				docMainPanel.add(adduserSampleDocJButton, "split 2, w 100::");
				docMainPanel.add(removeuserSampleDocJButton, "w 100::");
				docMainPanel.add(addTrainDocsJButton, "split 2, w 100::");
				docMainPanel.add(removeTrainDocsJButton, "w 100::");
				docMainPanel.add(mainDocSettingsPanel, "grow, w 100::");
				docMainPanel.add(sampleDocSettingsPanel, "grow, w 100::");
				docMainPanel.add(trainDocSettingsPanel, "grow, w 100::");
			}
			docPanel.add(prepDocLabel, "h 30!");
			docPanel.add(docMainPanel);
		}
		
		//==========================================================================================
		//================================ Features Panel =========================================
		//==========================================================================================
		
		featPanel = new JPanel();
		
		MigLayout featLayout = new MigLayout(
				"wrap",
				"[grow, fill]",
				"[30][grow, fill]");
		featPanel.setLayout(featLayout);
		{
			// Features Label---------------------------------------------------
			prepFeatLabel = new JLabel("Features:");
			prepFeatLabel.setFont(new Font("Ariel", Font.BOLD, 15));
			prepFeatLabel.setHorizontalAlignment(SwingConstants.CENTER);
			prepFeatLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			prepFeatLabel.setOpaque(true);
			if (main.featuresAreReady())
				prepFeatLabel.setBackground(main.ready);
			else
				prepFeatLabel.setBackground(main.notReady);
			
			// Main Features area---------------------------------------------
			featMainPanel = new JPanel();
			featMainPanel.setLayout(new MigLayout(
					"fill, wrap 4",
					"[150!]20[left][150:40%:, fill][300:60%:, fill]",
					"[][][20!][40!][20!][20!]20[33%, fill][33%, fill][33%, fill]"));
			{
				JPanel featMainTopPanel = new JPanel();
				featMainTopPanel.setLayout(new MigLayout(
						"wrap 6",
						"[][grow, fill][][][][]"));
				{
					// combo box label---------------------------------------------
					featuresSetJLabel = new JLabel("Feature Set:");
					featMainTopPanel.add(featuresSetJLabel);
					
					// Combo box----------------------------------------------------------
					String[] presetCFDsNames = new String[main.presetCFDs.size() + 1];
					presetCFDsNames[0] = "Select A Feature Set...";
					for (int i=0; i<main.presetCFDs.size(); i++)
						presetCFDsNames[i+1] = main.presetCFDs.get(i).getName();
			
					featuresSetJComboBoxModel = new DefaultComboBoxModel(presetCFDsNames);
					featuresSetJComboBox = new JComboBox();
					featuresSetJComboBox.setModel(featuresSetJComboBoxModel);
					featMainTopPanel.add(featuresSetJComboBox, "grow");
					
					featuresAddSetJButton = new JButton("Add");
					featMainTopPanel.add(featuresAddSetJButton);
					
					featuresLoadSetFromFileJButton = new JButton("Import");
					featMainTopPanel.add(featuresLoadSetFromFileJButton);
					
					featuresSaveSetJButton = new JButton("Export");
					featMainTopPanel.add(featuresSaveSetJButton);
					
					featuresNewSetJButton = new JButton("New");
					featMainTopPanel.add(featuresNewSetJButton);
					
					// description label-----------------------------------------------
					featuresSetDescJLabel = new JLabel("Description:");
					featMainTopPanel.add(featuresSetDescJLabel);
					
					// description pane--------------------------------------------------
					featuresSetDescJTextPane = new JTextPane();
					featuresSetDescJScrollPane = new JScrollPane(featuresSetDescJTextPane);
					featMainTopPanel.add(featuresSetDescJScrollPane, "span, grow");
				}
				
				featuresListLabel = new JLabel("Features:");
				featuresListLabel.setHorizontalAlignment(JLabel.CENTER);
				featuresListLabel.setOpaque(true);
				featuresListLabel.setBackground(main.tan);
				featuresListLabel.setBorder(BorderFactory.createRaisedBevelBorder());
				
				featuresInfoLabel = new JLabel("Feature Information:");
				featuresInfoLabel.setHorizontalAlignment(JLabel.CENTER);
				featuresInfoLabel.setOpaque(true);
				featuresInfoLabel.setBackground(main.tan);
				featuresInfoLabel.setBorder(BorderFactory.createRaisedBevelBorder());
				
				// features list--------------------------------------------------
				featuresJListModel = new DefaultComboBoxModel();
				featuresJList = new JList(featuresJListModel);
				JScrollPane featuresListJScrollPane = new JScrollPane(featuresJList);
				
				// feature name label--------------------------------------------------
				featuresFeatureNameJLabel = new JLabel("Name:");
				
				// feature name field--------------------------------------------------
				featuresFeatureNameJTextPane = new JTextPane();
				featuresFeatureNameJTextPane.setEditable(false);
				
				// feature description label--------------------------------------------------
				featuresFeatureDescJLabel = new JLabel("Description:");
				
				// feature description pane--------------------------------------------------
				featuresFeatureDescJTextPane = new JTextPane();
				//featuresFeatureDescJScrollPane = new JScrollPane(featuresFeatureDescJTextPane);
				featuresFeatureDescJTextPane.setEditable(false);
				
				// feature description pane--------------------------------------------------
				featuresNormJLabel = new JLabel("Normalization:");
				
				// feature description pane--------------------------------------------------
				featuresNormContentJTextPane = new JTextPane();
				featuresNormContentJTextPane.setEditable(false);
				
				// feature description pane--------------------------------------------------
				featuresFactorJLabel = new JLabel("Factor:");
				
				// feature description pane--------------------------------------------------
				featuresFactorContentJTextPane = new JTextPane();
				featuresFactorContentJTextPane.setEditable(false);
				
				// feature description pane--------------------------------------------------
				String[][] toolsTableFiller = new String[1][1];
				toolsTableFiller[0] = new String[] {"N/A"};
	        	String[] toolsTableHeaderFiller = {"Tools:"};
	        	
	        	String[][] configTableFiller = new String[1][1];
				configTableFiller[0] = new String[] {"N/A", "N/A"};
	        	String[] configTableHeaderFiller = {"Tool:", "Parameter:", "Value:"};
				
				// feature description pane--------------------------------------------------
				featuresFeatureExtractorJLabel = new JLabel("Extractor:");
				
				// feature description pane--------------------------------------------------
				featuresFeatureExtractorContentJTableModel = new DefaultTableModel(toolsTableFiller, toolsTableHeaderFiller){
					public boolean isCellEditable(int rowIndex, int mColIndex) {
				        return false;
				    }
				};
				
				featuresFeatureExtractorContentJTable = new JTable(featuresFeatureExtractorContentJTableModel);
				featuresFeatureExtractorContentJTable.setRowSelectionAllowed(false);
				featuresFeatureExtractorContentJTable.setColumnSelectionAllowed(false);
				
				// feature description pane--------------------------------------------------
				featuresFeatureExtractorConfigJTableModel = new DefaultTableModel(configTableFiller, configTableHeaderFiller){
					public boolean isCellEditable(int rowIndex, int mColIndex) {
				        return false;
				    }
				};
				
				featuresFeatureExtractorConfigJTable = new JTable(featuresFeatureExtractorConfigJTableModel);
				featuresFeatureExtractorConfigJTable.setRowSelectionAllowed(false);
				featuresFeatureExtractorConfigJTable.setColumnSelectionAllowed(false);
				
				// feature description pane--------------------------------------------------
				featuresCanonJLabel = new JLabel("Pre-Processing:");
				
				// feature description pane--------------------------------------------------
				featuresCanonJTableModel = new DefaultTableModel(toolsTableFiller, toolsTableHeaderFiller){
					public boolean isCellEditable(int rowIndex, int mColIndex) {
				        return false;
				    }
				};
				
				featuresCanonJTable = new JTable(featuresCanonJTableModel);
				featuresCanonJTable.setRowSelectionAllowed(false);
				featuresCanonJTable.setColumnSelectionAllowed(false);
				
				// feature description pane--------------------------------------------------
				featuresCanonConfigJTableModel = new DefaultTableModel(configTableFiller, configTableHeaderFiller){
					public boolean isCellEditable(int rowIndex, int mColIndex) {
				        return false;
				    }
				};
				
				featuresCanonConfigJTable = new JTable(featuresCanonConfigJTableModel);
				featuresCanonConfigJTable.setRowSelectionAllowed(false);
				featuresCanonConfigJTable.setColumnSelectionAllowed(false);
				
				// feature description pane--------------------------------------------------
				featuresCullJLabel = new JLabel("Post-Processing:");
				
				// feature description pane--------------------------------------------------
				featuresCullJTableModel = new DefaultTableModel(toolsTableFiller, toolsTableHeaderFiller){
					public boolean isCellEditable(int rowIndex, int mColIndex) {
				        return false;
				    }
				};
				
				featuresCullJTable = new JTable(featuresCullJTableModel);
				featuresCullJTable.setRowSelectionAllowed(false);
				featuresCullJTable.setColumnSelectionAllowed(false);
				
				// feature description pane--------------------------------------------------
				featuresCullConfigJTableModel = new DefaultTableModel(configTableFiller, configTableHeaderFiller){
					public boolean isCellEditable(int rowIndex, int mColIndex) {
				        return false;
				    }
				};
				
				featuresCullConfigJTable = new JTable(featuresCullConfigJTableModel);
				featuresCullConfigJTable.setRowSelectionAllowed(false);
				featuresCullConfigJTable.setColumnSelectionAllowed(false);
				
				featMainPanel.add(featMainTopPanel, "spanx, growx, gapbottom 20");
				featMainPanel.add(featuresListLabel, "w 150!");
				featMainPanel.add(featuresInfoLabel, "spanx, growx");
				featMainPanel.add(featuresListJScrollPane, "spany, growy, w 150!");
				featMainPanel.add(featuresFeatureNameJLabel);
				featMainPanel.add(new JScrollPane(featuresFeatureNameJTextPane), "span 2, grow");
				featMainPanel.add(featuresFeatureDescJLabel);
				featMainPanel.add(new JScrollPane(featuresFeatureDescJTextPane), "span 2, grow");
				featMainPanel.add(featuresNormJLabel);
				featMainPanel.add(new JScrollPane(featuresNormContentJTextPane), "span 2, grow");
				featMainPanel.add(featuresFactorJLabel);
				featMainPanel.add(new JScrollPane(featuresFactorContentJTextPane), "span 2, grow");
				featMainPanel.add(featuresFeatureExtractorJLabel);
				featMainPanel.add(new JScrollPane(featuresFeatureExtractorContentJTable));
				featMainPanel.add(new JScrollPane(featuresFeatureExtractorConfigJTable));
				featMainPanel.add(featuresCanonJLabel);
				featMainPanel.add(new JScrollPane(featuresCanonJTable));
				featMainPanel.add(new JScrollPane(featuresCanonConfigJTable));
				featMainPanel.add(featuresCullJLabel);
				featMainPanel.add(new JScrollPane(featuresCullJTable));
				featMainPanel.add(new JScrollPane(featuresCullConfigJTable));
				
				
				JTable[] configTableArray = {featuresFeatureExtractorConfigJTable, featuresCanonConfigJTable, featuresCullConfigJTable};
				
				for (final JTable table: configTableArray)
				{
					table.getModel().addTableModelListener(new TableModelListener() {
			            public void tableChanged(TableModelEvent e) {
			                GUIMain.ColumnsAutoSizer.sizeColumnsToFit(table);
			            }
			        });
				}
				
				
				
//				featuresAddJButton = new JButton("Add");
//				featMainPanel.add(featuresAddJButton);
//				
//				featuresRemoveJButton = new JButton("Remove");
//				featMainPanel.add(featuresAddJButton);
//				
//				featuresEditJButton = new JButton("Edit");
//				featMainPanel.add(featuresAddJButton);
			}
			featPanel.add(prepFeatLabel, "span, h 30!");
			featPanel.add(featMainPanel);
		}

		
		//==========================================================================================
		//================================ Classifiers Panel =========================================
		//==========================================================================================
				
		classPanel = new JPanel();
		
		MigLayout classLayout = new MigLayout(
				"wrap",
				"grow, fill",
				"[30][grow, fill]");
		classPanel.setLayout(classLayout);
		{
			// Features Label---------------------------------------------------
			prepClassLabel = new JLabel("Classifiers:");
			prepClassLabel.setFont(new Font("Ariel", Font.BOLD, 15));
			prepClassLabel.setHorizontalAlignment(SwingConstants.CENTER);
			prepClassLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			prepClassLabel.setOpaque(true);
			if (main.classifiersAreReady())
				prepClassLabel.setBackground(main.ready);
			else
				prepClassLabel.setBackground(main.notReady);
			
			// Main Features area---------------------------------------------
			classMainPanel = new JPanel();
			classMainPanel.setLayout(new MigLayout(
					"fill, wrap 2, ins 0 0",
					"grow, fill",
					"[20][60%, fill][20][20]20[20][40%, fill]"));
			{

				classAvClassJLabel = new JLabel("Available WEKA Classifiers:");
				classAvClassJLabel.setHorizontalAlignment(JLabel.CENTER);
				classAvClassJLabel.setOpaque(true);
				classAvClassJLabel.setBackground(main.tan);
				classAvClassJLabel.setBorder(BorderFactory.createRaisedBevelBorder());
				
				classSelClassJLabel = new JLabel("Selected WEKA Classifiers:");
				classSelClassJLabel.setHorizontalAlignment(JLabel.CENTER);
				classSelClassJLabel.setOpaque(true);
				classSelClassJLabel.setBackground(main.tan);
				classSelClassJLabel.setBorder(BorderFactory.createRaisedBevelBorder());
				
				classJTree = new JTree();
				classJTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
				classTreeScrollPane = new JScrollPane(classJTree);
				ClassTabDriver.initAdvWekaClassifiersTree(this);
				
				classSelClassJListModel = new DefaultListModel();
				classJList = new JList(classSelClassJListModel);
				classJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				classSelClassJScrollPane = new JScrollPane(classJList);
				
				classAvClassArgsJLabel = new JLabel("Arguments:");
				
				classSelClassArgsJLabel = new JLabel("Arguments:");
				
				classAvClassArgsJTextField = new JTextField();
				
				classSelClassArgsJTextField = new JTextField();
				
				classAddJButton = new JButton("Add");
				
				classRemoveJButton = new JButton("Remove");
					
				classDescJLabel = new JLabel("Classifier Description");
				classDescJLabel.setHorizontalAlignment(JLabel.CENTER);
				classDescJLabel.setOpaque(true);
				classDescJLabel.setBackground(main.tan);
				classDescJLabel.setBorder(BorderFactory.createRaisedBevelBorder());
				
				classDescJTextPane = new JTextPane();
				classDescJTextPane.setEditable(false);
				classDescJScrollPane = new JScrollPane(classDescJTextPane);
				
				classMainPanel.add(classAvClassJLabel);
				classMainPanel.add(classSelClassJLabel);
				classMainPanel.add(classTreeScrollPane);
				classMainPanel.add(classSelClassJScrollPane);
				classMainPanel.add(classAvClassArgsJLabel, "split 2, grow 0");
				classMainPanel.add(classAvClassArgsJTextField);
				classMainPanel.add(classSelClassArgsJLabel, "split 2, grow 0");
				classMainPanel.add(classSelClassArgsJTextField);
				classMainPanel.add(classAddJButton);
				classMainPanel.add(classRemoveJButton);
				classMainPanel.add(classDescJLabel, "span");
				classMainPanel.add(classDescJScrollPane, "span");
			}
			classPanel.add(prepClassLabel, "h 30!");
			classPanel.add(classMainPanel);
		}
	}
}