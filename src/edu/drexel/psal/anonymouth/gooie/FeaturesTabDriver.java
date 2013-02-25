package edu.drexel.psal.anonymouth.gooie;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.drexel.psal.JSANConstants;
import edu.drexel.psal.jstylo.eventDrivers.*;
import edu.drexel.psal.jstylo.generics.*;
import edu.drexel.psal.jstylo.generics.Logger.LogOut;
import edu.drexel.psal.jstylo.GUI.DocsTabDriver.ExtFilter;
import edu.drexel.psal.jstylo.canonicizers.*;

import com.jgaap.canonicizers.*;
import com.jgaap.generics.*;

public class FeaturesTabDriver {
	
	/* ======================
	 * Features tab listeners
	 * ======================
	 */
	
	/**
	 * Initialize all documents tab listeners.
	 */
	protected static void initListeners(final GUIMain main) 
	{
		initMainListeners(main);
		initAdvListeners(main);
	}
	
	/**
	 * Initialize all documents tab listeners.
	 */
	protected static void initMainListeners(final GUIMain main) 
	{
		// feature set buttons
		// ===================
		
		// feature set combo box
		main.featuresSetJComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Logger.logln("Preset feature set selected in the features tab.");
				
				int answer = JOptionPane.YES_OPTION;
				/*
				if (!isCFDEmpty(main.cfd)) {
					answer = JOptionPane.showConfirmDialog(main,
							"Are you sure you want to override current feature set?",
							"Load Preset Feature Set",
							JOptionPane.YES_NO_OPTION);
				}
				*/

				if (answer == JOptionPane.YES_OPTION) {
					int selected = main.featuresSetJComboBox.getSelectedIndex() - 1;
					if (selected == -1) {
						main.cfd = new CumulativeFeatureDriver();
					} else {
						main.cfd = main.presetCFDs.get(selected);
						Logger.logln("loaded preset feature set: "+main.cfd.getName());
					}
					main.featuresSetJComboBox.setSelectedIndex(selected+1);
					main.PPSP.featuresSetJComboBox.setSelectedIndex(selected+1);
					// update tab view
					GUIUpdateInterface.updateFeatureSetView(main);
					GUIUpdateInterface.updateFeatPrepColor(main);
				} else {
					Logger.logln("Loading preset feature set canceled.");
				}
			}
		});
	}
	
	/**
	 * Initialize all documents tab listeners.
	 */
	protected static void initAdvListeners(final GUIMain main) 
	{
		// feature set combo box
		main.PPSP.featuresSetJComboBox.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				Logger.logln("Preset feature set selected in the features tab.");
				
				int answer = JOptionPane.YES_OPTION;
				/*
				if (!isCFDEmpty(main.cfd)) {
					answer = JOptionPane.showConfirmDialog(main,
							"Are you sure you want to override current feature set?",
							"Load Preset Feature Set",
							JOptionPane.YES_NO_OPTION);
				}
				*/

				if (answer == JOptionPane.YES_OPTION) {
					int selected = main.PPSP.featuresSetJComboBox.getSelectedIndex() - 1;
					if (selected == -1) {
						main.cfd = new CumulativeFeatureDriver();
					} else {
						main.cfd = main.presetCFDs.get(selected);
						Logger.logln("loaded preset feature set: "+main.cfd.getName());
					}
					main.featuresSetJComboBox.setSelectedIndex(selected+1);
					main.PPSP.featuresSetJComboBox.setSelectedIndex(selected+1);
					// update tab view
					GUIUpdateInterface.updateFeatureSetView(main);
					GUIUpdateInterface.updateFeatPrepColor(main);
				} else {
					Logger.logln("Loading preset feature set canceled.");
				}
			}
		});

		// new feature set button
		main.PPSP.featuresNewSetJButton.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'New Feature Set' button clicked in the features tab.");

				// check if the current CFD is not empty
				int answer;
				if (!isCFDEmpty(main.cfd)) {
					answer = JOptionPane.showConfirmDialog(main,
							"Are you sure you want to override current feature set?",
							"New Feature Set",
							JOptionPane.YES_NO_OPTION);
				} else {
					answer = JOptionPane.YES_OPTION;
				}

				if (answer == JOptionPane.YES_OPTION) {
					main.cfd = new CumulativeFeatureDriver();
					main.PPSP.featuresSetJComboBox.setSelectedIndex(0);
					GUIUpdateInterface.updateFeatureSetView(main);
				}
			}
		});
		
		// add feature set
		main.PPSP.featuresAddSetJButton.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Logger.logln("'Add Feature Set' button clicked in the features tab.");
				
				// check name
				if (main.cfd.getName() == null || main.cfd.getName().matches("\\s*")) {
					JOptionPane.showMessageDialog(main,
							"Feature set must have at least a name to be added.",
							"Add Feature Set",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// check that doesn't exist
				for (int i=0; i<main.PPSP.featuresSetJComboBoxModel.getSize(); i++) {
					if (main.cfd.getName().equals(main.PPSP.featuresSetJComboBoxModel.getElementAt(i))) {
						JOptionPane.showMessageDialog(main,
								"Feature set with the given name already exists.",
								"Add Feature Set",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				}
				
				// add
				main.presetCFDs.add(main.cfd);
				main.PPSP.featuresSetJComboBoxModel.addElement(main.cfd.getName());
				main.PPSP.featuresSetJComboBox.setSelectedIndex(main.PPSP.featuresSetJComboBoxModel.getSize()-1);
			}
		});

		// load from file button
		main.PPSP.featuresLoadSetFromFileJButton.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Logger.logln("'Import from XML' button clicked in the features tab.");
				
				// check if the current CFD is not empty
				int answer = JOptionPane.YES_OPTION;
				/*
				if (!isCFDEmpty(main.cfd)) {
					answer = JOptionPane.showConfirmDialog(main,
							"Are you sure you want to override current feature set?",
							"Import Feature Set",
							JOptionPane.YES_NO_OPTION);
				}
				*/
				
				if (answer == JOptionPane.YES_OPTION) {
					JFileChooser load = new JFileChooser(new File("."));
					load.addChoosableFileFilter(new ExtFilter("XML files (*.xml)", "xml"));
					answer = load.showOpenDialog(main);
					
					if (answer == JFileChooser.APPROVE_OPTION) {
						String path = load.getSelectedFile().getAbsolutePath();
						Logger.logln("Trying to load cumulative feature driver from "+path);
						try {
							// read CFD and update
							CumulativeFeatureDriver cfd = new CumulativeFeatureDriver(path);
							for (int i=0; i<main.PPSP.featuresSetJComboBoxModel.getSize(); i++) {
								if (cfd.getName().equals(main.PPSP.featuresSetJComboBoxModel.getElementAt(i))) {
									main.PPSP.featuresSetJComboBoxModel.removeElementAt(i);
									//JOptionPane.showMessageDialog(main,
										//	"Feature set already exists.",
										//	"Import Feature Set",
										//	JOptionPane.INFORMATION_MESSAGE);
									//return;
								}
							}
									
							main.cfd = cfd;
							main.presetCFDs.add(cfd);
							main.PPSP.featuresSetJComboBoxModel.addElement(cfd.getName());
							main.PPSP.featuresSetJComboBox.setSelectedIndex(main.PPSP.featuresSetJComboBoxModel.getSize()-1);
							GUIUpdateInterface.updateFeatureSetView(main);
							
						} catch (Exception exc) {
							Logger.logln("Failed loading "+path, LogOut.STDERR);
							Logger.logln(exc.toString(),LogOut.STDERR);
							JOptionPane.showMessageDialog(null,
									"Failed loading feature set from:\n"+path,
									"Load Feature Set Failure",
									JOptionPane.ERROR_MESSAGE);
						}
			            
			        } else {
			            Logger.logln("Load cumulative feature driver canceled");
			        }
				}
			}
		});

		// save feature set button
		main.PPSP.featuresSaveSetJButton.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Logger.logln("'Save Feature Set...' button clicked in the features tab.");

				JFileChooser save = new JFileChooser(new File("."));
				save.addChoosableFileFilter(new ExtFilter("XML files (*.xml)", "xml"));
				int answer = save.showSaveDialog(main);

				if (answer == JFileChooser.APPROVE_OPTION) {
					File f = save.getSelectedFile();
					String path = f.getAbsolutePath();
					if (!path.toLowerCase().endsWith(".xml"))
						path += ".xml";
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(path));
						bw.write(main.cfd.toXMLString());
						bw.flush();
						bw.close();
						Logger.log("Saved cumulative feature driver to "+path+":\n"+main.cfd.toXMLString());
					} catch (IOException exc) {
						Logger.logln("Failed opening "+path+" for writing",LogOut.STDERR);
						Logger.logln(exc.toString(),LogOut.STDERR);
						JOptionPane.showMessageDialog(null,
								"Failed saving feature set set into:\n"+path,
								"Save Feature Set Failure",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					Logger.logln("Save cumulative feature driver canceled");
				}
			}
		});
		
		
		// feature set properties
		// ======================
		
		// feature set name text field
//				main.PPSP.featuresSetNameJTextField.addFocusListener(new FocusListener() {
//					
//					@Override
//					public void focusLost(FocusEvent arg0) {
//						Logger.logln("Feature set name edited in the features tab.");
//						main.cfd.setName(main.PPSP.featuresSetNameJTextField.getText());
//					}
//					
//					@Override
//					public void focusGained(FocusEvent arg0) {}
//				});
		
		// feature set description text pane
		main.PPSP.featuresSetDescJTextPane.addFocusListener(new FocusListener() 
		{
			
			@Override
			public void focusLost(FocusEvent arg0) {
				Logger.logln("Feature set description edited in the features tab.");
				main.cfd.setDescription(main.PPSP.featuresSetDescJTextPane.getText());
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {}
		});
		
		
		// features
		// ========
		
		// feature list
		main.PPSP.featuresJList.addListSelectionListener(new ListSelectionListener() 
		{
			int lastSelected = -2;
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				int selected = main.PPSP.featuresJList.getSelectedIndex();
				// skip if already processed
				if (selected == lastSelected)
					return;
				Logger.logln("Feature selected in the features tab: "+main.PPSP.featuresJList.getSelectedValue());
				GUIUpdateInterface.updateFeatureView(main, selected);
				lastSelected = selected;
			}
		});
		
		// add feature button
//				main.PPSP.featuresAddJButton.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent arg0) {
//						Logger.logln("'Add' feature button clicked in the features tab.");
//						FeatureWizard fw = new FeatureWizard(main);
//						fw.setVisible(true);
//					}
//				});
//				
//				// edit feature button
//				main.PPSP.featuresEditJButton.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						Logger.logln("'Edit' feature button clicked in the features tab.");
//						if (main.PPSP.featuresJList.getSelectedIndex() == -1) {
//							JOptionPane.showMessageDialog(main,
//									"You must select a feature to edit.",
//									"Edit Feature Error",
//									JOptionPane.ERROR_MESSAGE);
//							return;
//						}
//						FeatureDriver fd = main.cfd.featureDriverAt(main.PPSP.featuresJList.getSelectedIndex());
//						FeatureWizard fw = new FeatureWizard(main,fd,main.PPSP.featuresJList.getSelectedIndex());
//						fw.setVisible(true);
//					}
//				});
//				
//				// remove feature button
//				main.PPSP.featuresRemoveJButton.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						Logger.logln("'Remove' feature button clicked in the features tab.");
//						int selected = main.PPSP.featuresJList.getSelectedIndex();
//						
//						if (selected == -1) {
//							JOptionPane.showMessageDialog(main,
//									"You must select a feature to be removed.",
//									"Remove Feature Error",
//									JOptionPane.ERROR_MESSAGE);
//							return;
//						}
//						
//						int answer = JOptionPane.showConfirmDialog(main,
//								"Are you sure you want to remove feature '"+main.PPSP.featuresJList.getSelectedValue()+"'",
//								"Remove Feature",
//								JOptionPane.YES_NO_OPTION);
//						if (answer == JOptionPane.YES_OPTION) {
//							FeatureDriver fd = main.cfd.removeFeatureDriverAt(selected);
//							GUIUpdateInterface.updateFeatureSetView(main);
//							Logger.logln("Removed feature "+fd.getName());
//						}
//					}
//				});
				
		// canonicizers list
		main.PPSP.featuresCanonJTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
		{
			int lastSelected = -2;
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selected = main.PPSP.featuresCanonJTable.getSelectedRow();
				
				// already selected
				if (selected == lastSelected)
					return;
				
				// unselected
				else if (selected == -1) {
					Logger.logln("Canonicizer unselected in features tab.");
					main.PPSP.featuresCanonConfigJTableModel.getDataVector().removeAllElements();
				}
				
				//selected
				else {
					Canonicizer c = main.cfd.featureDriverAt(main.PPSP.featuresJList.getSelectedIndex()).canonicizerAt(selected);
					Logger.logln("Canonicizer '"+c.displayName()+"' selected in features tab.");
					GUIUpdateInterface.populateTableWithParams(c, main.PPSP.featuresCanonConfigJTableModel);
				}
				
				lastSelected = selected;
			}
		});
		
		// cullers list
		main.PPSP.featuresCullJTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() 
		{
			int lastSelected = -2;
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selected = main.PPSP.featuresCullJTable.getSelectedRow();
				
				// already selected
				if (selected == lastSelected)
					return;
				
				// unselected
				else if (selected == -1) {
					Logger.logln("Culler unselected in features tab.");
					main.PPSP.featuresCullConfigJTableModel.getDataVector().removeAllElements();
				}
				
				//selected
				else {
					EventCuller ec = main.cfd.featureDriverAt(main.PPSP.featuresJList.getSelectedIndex()).cullerAt(selected);
					Logger.logln("Culler '"+ec.displayName()+"' selected in features tab.");
					GUIUpdateInterface.populateTableWithParams(ec, main.PPSP.featuresCullConfigJTableModel);
				}
				
				lastSelected = selected;
			}
		});
	}
	
	/**
	 * Returns true iff the given cumulative feature driver is effectively empty
	 */
	protected static boolean isCFDEmpty(CumulativeFeatureDriver cfd) {
		if (cfd == null)
			return true;
		else if ((cfd.getName() == null || cfd.getName().matches("\\s*")) &&
			(cfd.getDescription() == null || cfd.getDescription().matches("\\s*")) &&
			cfd.numOfFeatureDrivers() == 0)
			return true;
		else
			return false;
	}
	
	
	/* =================================
	 * Preset cumulative feature drivers
	 * =================================
	 */
	
	protected static void initPresetCFDs(GUIMain main) {
		// initialize list of preset CFDs
		main.presetCFDs = new ArrayList<CumulativeFeatureDriver>();
		
		try {
			File[] featureSetFiles = new File(JSANConstants.JSAN_FEATURESETS_PREFIX).listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".xml");
				}
			});
			
			String path;
			for (File f: featureSetFiles) {
				path = f.getAbsolutePath();
				main.presetCFDs.add(new CumulativeFeatureDriver(path));
			}
		} catch (Exception e) {
			Logger.logln("Failed to read feature set files.",LogOut.STDERR);
			e.printStackTrace();
		}
		
		
		/* =============
		 * 9 feature-set
		 * =============
		 */
		/*
		CumulativeFeatureDriver nineFeatures = new CumulativeFeatureDriver();
		nineFeatures.setName("9 feature-set");
		nineFeatures.setDescription("9 features used by Brennan and Greenstadt.");
		Canonicizer stripEdgesPunct = new StripEdgesPunctuation();
		Canonicizer unifyCase = new UnifyCase();

		// unique words  - number of unique words in the document (remove punctuation and unify case)
		FeatureDriver uniqueWords = new FeatureDriver("Unique Words Count", false, new UniqueWordsCounterEventDriver());
		uniqueWords.setDescription("Number of unique words in the document, after removing punctuation and unifying case.");
		uniqueWords.addCanonicizer(stripEdgesPunct);
		uniqueWords.addCanonicizer(unifyCase);
		nineFeatures.addFeatureDriver(uniqueWords);

		// complexity - ratio of unique words to total number of words
		FeatureDriver complexity = new FeatureDriver("Complexity", false, new UniqueWordsCounterEventDriver());
		complexity.setDescription("Ratio of unique words to total number of words in the document.");
		complexity.addCanonicizer(stripEdgesPunct);
		complexity.addCanonicizer(unifyCase);
		complexity.setNormBaseline(NormBaselineEnum.WORDS_IN_DOC);
		nineFeatures.addFeatureDriver(complexity);

		// sentence count - number of sentences in the document
		FeatureDriver sentenceCount = new FeatureDriver("Sentence Count", false, new SentenceCounterEventDriver());
		sentenceCount.setDescription("Number of sentences in the document.");
		nineFeatures.addFeatureDriver(sentenceCount);

		// average sentence length - in words (total number of words / total number of sentences) 
		FeatureDriver avgSentenceLen = new FeatureDriver("Average Sentence Length", false, new WordCounterEventDriver());
		avgSentenceLen.setDescription("Average sentence length in words (total number of words / total number of sentences).");
		avgSentenceLen.setNormBaseline(NormBaselineEnum.SENTENCES_IN_DOC);
		nineFeatures.addFeatureDriver(avgSentenceLen);

		// average syllables - per word
		FeatureDriver avgSyllables = new FeatureDriver("Average Syllables in Word", false, new SyllableCounterEventDriver());
		avgSyllables.setDescription("Average syllables in word.");
		avgSyllables.setNormBaseline(NormBaselineEnum.WORDS_IN_DOC);
		nineFeatures.addFeatureDriver(avgSyllables);

		// Gunning-Fog readability index
		FeatureDriver gfIndex = new FeatureDriver("Gunning-Fog Readability Index", false, new GunningFogIndexEventDriver());
		gfIndex.setDescription("The Gunning-Fog readability index: 0.4*((total words / total sentences) + 100*(total complex words / total words)) [where complex words are words with 3 or more syllables].");
		nineFeatures.addFeatureDriver(gfIndex);

		// character space - total number of characters, spaces included
		FeatureDriver charSpace = new FeatureDriver("Character Space", false, new CharCounterEventDriver());
		charSpace.setDescription("The total number of characters in the document, spaces included.");
		nineFeatures.addFeatureDriver(charSpace);

		// letter space (no spaces / punctuation)
		FeatureDriver letterSpace = new FeatureDriver("Letter Space", false, new LetterCounterEventDriver());
		letterSpace.setDescription("The total number of letters (excluding spaces and punctuation).");
		nineFeatures.addFeatureDriver(letterSpace);

		// Flesch reading ease score
		FeatureDriver fleschScore = new FeatureDriver("Flesch Reading Ease Score", false, new FleschReadingEaseScoreEventDriver());
		fleschScore.setDescription("The Flesch reading ease score: 206.835 - 1.015*(total words / total sentences) -84.6*(total syllables / total words)");
		nineFeatures.addFeatureDriver(fleschScore);
		
		main.presetCFDs.add(nineFeatures);
		/*
		/* =======================
		 * WritePrints feature set
		 * =======================
		 */
	}
}
