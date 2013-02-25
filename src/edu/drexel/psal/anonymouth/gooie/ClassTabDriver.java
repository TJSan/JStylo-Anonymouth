package edu.drexel.psal.anonymouth.gooie;

import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.jstylo.generics.Logger.LogOut;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.jgaap.generics.Document;

import weka.classifiers.*;
import weka.classifiers.bayes.*;
import weka.classifiers.functions.*;
import weka.classifiers.lazy.*;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.*;

public class ClassTabDriver {

	/* =========================
	 * Classifiers tab listeners
	 * =========================
	 */
	
	protected static Classifier tmpClassifier;
	
	/**
	 * Initialize all classifiers tab listeners.
	 */
	protected static void initListeners(final GUIMain main) 
	{
		initMainListeners(main);
		initAdvListeners(main);
	}
	
	/**
	 * Initialize all classifiers tab listeners.
	 */
	protected static void initMainListeners(final GUIMain main) 
	{
		
		// available classifiers tree
		// ==========================
		
		main.classJTree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				// if unselected
				if (main.classJTree.getSelectionCount() == 0) {
					Logger.logln("Classifier tree unselected in the classifiers tab.");
					//resetAvClassSelection(main);
					tmpClassifier = null;
					return;
				}
				
				// unselect selected list
				main.classJList.clearSelection();
				
				Object[] path = main.classJTree.getSelectionPath().getPath();
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path[path.length-1];
				
				// if selected a classifier
				if (selectedNode.isLeaf()) {
					Logger.logln("Classifier selected in the available classifiers tree in the classifiers tab: "+selectedNode.toString());
					
					// get classifier
					String className = getClassNameFromPath(path);
					tmpClassifier = null;
					try {
						tmpClassifier = Classifier.forName(className, null);						
					} catch (Exception e) {
						Logger.logln("Could not create classifier out of class: "+className);
						JOptionPane.showMessageDialog(main,
								"Could not generate classifier for selected class:\n"+className,
								"Classifier Selection Error",
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
						return;
					}
					// Add an -M option for SMO classifier
					String dashM = "";
					if(className.toLowerCase().contains("smo"))
						dashM = " -M";
					
					
					// show options and description
					
					//main.classAvClassArgsJTextField.setText(getOptionsStr(tmpClassifier.getOptions())+dashM);
					//main.classDescJTextPane.setText(getDesc(tmpClassifier));
				}
				// otherwise
				else {
					//resetAvClassSelection(main);
					tmpClassifier = null;
				}
			}
		});
		
		// add button
		// ==========
		
		main.classAddJButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Logger.logln("'Add' button clicked in the analysis tab.");

				// check if classifier is selected
				if (tmpClassifier == null) {
					JOptionPane.showMessageDialog(main,
							"You must select a classifier to be added.",
							"Add Classifier Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if( main.classifiers.size() >0){
					JOptionPane.showMessageDialog(main,
							"It is only possible to select one classifier at a time.",
							"Add Classifier Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else {
					// check classifier options
					try {
						//tmpClassifier.setOptions(main.classAvClassArgsJTextField.getText().split(" "));
						tmpClassifier.setOptions(getOptionsStr(tmpClassifier.getOptions()).split(" "));
					} catch (Exception e) {
						Logger.logln("Invalid options given for classifier.",LogOut.STDERR);
						JOptionPane.showMessageDialog(main,
								"The classifier arguments entered are invalid.\n"+
										"Restoring original options.",
										"Classifier Options Error",
										JOptionPane.ERROR_MESSAGE);
						//main.classAvClassArgsJTextField.setText(getOptionsStr(tmpClassifier.getOptions()));
						return;
					}
					
					// add classifier
					main.classifiers.add(tmpClassifier);
					GUIUpdateInterface.updateClassList(main);
					GUIUpdateInterface.updateClassPrepColor(main);
					//resetAvClassSelection(main);
					tmpClassifier = null;
					main.classJTree.clearSelection();
				}
			}
		});
		
		// selected classifiers list
		// =========================
		
		main.classJList.addListSelectionListener(new ListSelectionListener() {
			int lastSelected = -2;
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				int selected = main.classJList.getSelectedIndex();
				if (selected == lastSelected)
					return;
				lastSelected = selected;
				
				// if unselected
				if (selected == -1) {
					Logger.logln("Classifier list unselected in the classifiers tab.");
					//resetSelClassSelection(main);
					tmpClassifier = null;
					return;
				}

				// unselect available classifiers tree
				main.classJTree.clearSelection();

				String className = main.classJList.getSelectedValue().toString();
				Logger.logln("Classifier selected in the selected classifiers list in the classifiers tab: "+className);

				// show options and description
				//main.classSelClassArgsJTextField.setText(getOptionsStr(main.classifiers.get(selected).getOptions()));
				//main.classDescJTextPane.setText(getDesc(main.classifiers.get(selected)));
			}
		});
		
		// remove button
		// =============
		
		main.classRemoveJButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Logger.log("'Remove' button clicked in the classifiers tab.");
				int selected = main.classJList.getSelectedIndex();
				
				// check if selected
				if (selected == -1) {
					JOptionPane.showMessageDialog(main,
							"You must select a classifier to be removed.",
							"Remove Classifier Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// remove classifier
				main.classifiers.remove(selected);
				GUIUpdateInterface.updateClassList(main);
				GUIUpdateInterface.updateClassPrepColor(main);
			}
		});
		
		// about button
		// ============
/*
		main.classAboutJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				GUIUpdateInterface.showAbout(main);
			}
		});
*/		
		// back button
		// ===========
		
//		main.classBackJButton.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Logger.logln("'Back' button clicked in the classifiers tab");
//				main.mainJTabbedPane.setSelectedIndex(1);
//			}
//		});
//		
//		// next button
//		// ===========
//		
//		main.classNextJButton.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				Logger.logln("'Next' button clicked in the classifiers tab");
//				
//				if (main.classifiers.isEmpty()) 
//				{
//					JOptionPane.showMessageDialog(main,
//							"You must add at least one classifier.",
//							"Error",
//							JOptionPane.ERROR_MESSAGE);
//					return;
//				} 
//				else 
//				{
//					if (EditorTabDriver.isFirstRun == false)
//					{
//						int answer = JOptionPane.showConfirmDialog(main, "If you choose to continue, you will lose all unsaved data/work. Continue?","Reset?",JOptionPane.YES_NO_OPTION);
//						if(answer ==0)
//						{
//							EditorTabDriver.shouldReset = true;
//							EditorTabDriver.isFirstRun = true;
//							EditorTabDriver.resetAll(main);
//						}
//					}
//					EditorTabDriver.eits.processButton.setEnabled(true);
//					main.mainJTabbedPane.setEnabledAt(3, true);
//					main.mainJTabbedPane.setSelectedIndex(3);
//					Document toModifyPreview = main.ps.testDocAt(0);
//					try {
//						toModifyPreview.load();
//						EditorTabDriver.eitsList.get(0).editorBox.setText(toModifyPreview.stringify());
//						JOptionPane.showMessageDialog(main, "Click 'Process' to perform initial classification of your document.\n" +
//								"Once the results appear in the table below it,\n" +
//								"click on suggestions in the suggestion list to see what to change.\n\n" +
//								"Note: Depending on the classifier chosen, the number/size of documents,\n" +
//								"and the features selected, classificatoin may take a long time. However,\n" +
//								"you do not need to re-process before/after each suggestion.",
//								"Getting Started",
//								JOptionPane.INFORMATION_MESSAGE,
//								GUIMain.icon); 
//					} catch (Exception e1) {
//						JOptionPane.showMessageDialog(null,
//								"Document to modify could not load.",
//								"Error",
//								JOptionPane.ERROR_MESSAGE,
//								GUIMain.iconNO);
//						//e1.printStackTrace();
//					} finally{
//						EditorTabDriver.shouldReset = false;
//					}
//				}
//			}
//		});
	}
	
	protected static void initAdvListeners(final GUIMain main) 
	{
		
		// available classifiers tree
		// ==========================
		
		main.PPSP.classJTree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				// if unselected
				if (main.PPSP.classJTree.getSelectionCount() == 0) {
					Logger.logln("Classifier tree unselected in the classifiers tab.");
					//resetAvClassSelection(main);
					tmpClassifier = null;
					return;
				}
				
				// unselect selected list
				main.PPSP.classJList.clearSelection();
				
				Object[] path = main.PPSP.classJTree.getSelectionPath().getPath();
				DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path[path.length-1];
				
				// if selected a classifier
				if (selectedNode.isLeaf()) {
					Logger.logln("Classifier selected in the available classifiers tree in the classifiers tab: "+selectedNode.toString());
					
					// get classifier
					String className = getClassNameFromPath(path);
					tmpClassifier = null;
					try {
						tmpClassifier = Classifier.forName(className, null);						
					} catch (Exception e) {
						Logger.logln("Could not create classifier out of class: "+className);
						JOptionPane.showMessageDialog(main,
								"Could not generate classifier for selected class:\n"+className,
								"Classifier Selection Error",
								JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
						return;
					}
					// Add an -M option for SMO classifier
					String dashM = "";
					if(className.toLowerCase().contains("smo"))
						dashM = " -M";
					
					
					// show options and description
					
					main.PPSP.classAvClassArgsJTextField.setText(getOptionsStr(tmpClassifier.getOptions())+dashM);
					main.PPSP.classDescJTextPane.setText(getDesc(tmpClassifier));
				}
				// otherwise
				else {
					//resetAvClassSelection(main);
					tmpClassifier = null;
				}
			}
		});
		
		// add button
		// ==========
		
		main.PPSP.classAddJButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Logger.logln("'Add' button clicked in the analysis tab.");

				// check if classifier is selected
				if (tmpClassifier == null) {
					JOptionPane.showMessageDialog(main,
							"You must select a classifier to be added.",
							"Add Classifier Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if( main.classifiers.size() >0){
					JOptionPane.showMessageDialog(main,
							"It is only possible to select one classifier at a time.",
							"Add Classifier Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else {
					// check classifier options
					try {
						//tmpClassifier.setOptions(main.classAvClassArgsJTextField.getText().split(" "));
						tmpClassifier.setOptions(getOptionsStr(tmpClassifier.getOptions()).split(" "));
					} catch (Exception e) {
						Logger.logln("Invalid options given for classifier.",LogOut.STDERR);
						JOptionPane.showMessageDialog(main,
								"The classifier arguments entered are invalid.\n"+
										"Restoring original options.",
										"Classifier Options Error",
										JOptionPane.ERROR_MESSAGE);
						//main.classAvClassArgsJTextField.setText(getOptionsStr(tmpClassifier.getOptions()));
						return;
					}
					
					// add classifier
					main.classifiers.add(tmpClassifier);
					GUIUpdateInterface.updateClassList(main);
					GUIUpdateInterface.updateClassPrepColor(main);
					//resetAvClassSelection(main);
					tmpClassifier = null;
					main.PPSP.classJTree.clearSelection();
				}
			}
		});
		
		// selected classifiers list
		// =========================
		
		main.PPSP.classJList.addListSelectionListener(new ListSelectionListener() {
			int lastSelected = -2;
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				int selected = main.PPSP.classJList.getSelectedIndex();
				if (selected == lastSelected)
					return;
				lastSelected = selected;
				
				// if unselected
				if (selected == -1) {
					Logger.logln("Classifier list unselected in the classifiers tab.");
					//resetSelClassSelection(main);
					tmpClassifier = null;
					return;
				}

				// unselect available classifiers tree
				main.PPSP.classJTree.clearSelection();

				String className = main.PPSP.classJList.getSelectedValue().toString();
				Logger.logln("Classifier selected in the selected classifiers list in the classifiers tab: "+className);

				// show options and description
				main.PPSP.classSelClassArgsJTextField.setText(getOptionsStr(main.classifiers.get(selected).getOptions()));
				main.PPSP.classDescJTextPane.setText(getDesc(main.classifiers.get(selected)));
			}
		});
		
		// remove button
		// =============
		
		main.PPSP.classRemoveJButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Logger.log("'Remove' button clicked in the classifiers tab.");
				int selected = main.PPSP.classJList.getSelectedIndex();
				
				// check if selected
				if (selected == -1) {
					JOptionPane.showMessageDialog(main,
							"You must select a classifier to be removed.",
							"Remove Classifier Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// remove classifier
				main.classifiers.remove(selected);
				GUIUpdateInterface.updateClassList(main);
				GUIUpdateInterface.updateClassPrepColor(main);
			}
		});
	}
	
	/**
	 * Clears the GUI when no available classifier is selected.
	 */
//	protected static void resetAvClassSelection(GUIMain main) {
//		// clear everything
//		tmpClassifier = null;
//		main.classAvClassArgsJTextField.setText("");
//		main.classDescJTextPane.setText("");
//	}
	
	/**
	 * Clears the GUI when no selected classifier is selected.
	 */
//	protected static void resetSelClassSelection(GUIMain main) {
//		// clear everything
//		main.classSelClassArgsJTextField.setText("");
//		main.classDescJTextPane.setText("");
//	}
	
	/**
	 * Creates a classifier options string.
	 */
	public static String getOptionsStr(String[] options) {
		String optionStr = "";
		for (String option: options)
			optionStr += option+" ";
		return optionStr;
	}
	
	
	/**
	 * Constructs the class name out of a tree path.
	 */
	protected static String getClassNameFromPath(Object[] path) {
		String res = "";
		for (Object o: path) {
			res += o.toString()+".";
		}
		res = res.substring(0,res.length()-1);
		return res;
	}
	
	/* ======================
	 * initialization methods
	 * ======================
	 */

	// build classifiers tree from list of class names
	protected static String[] classNames = new String[] {
		// bayes
		//"weka.classifiers.bayes.BayesNet",
		"weka.classifiers.bayes.NaiveBayes",
		"weka.classifiers.bayes.NaiveBayesMultinomial",
		//"weka.classifiers.bayes.NaiveBayesMultinomialUpdateable",
		//"weka.classifiers.bayes.NaiveBayesUpdateable",

		// functions
		"weka.classifiers.functions.Logistic",
		"weka.classifiers.functions.MultilayerPerceptron",
		"weka.classifiers.functions.SMO",

		// lazy
		"weka.classifiers.lazy.IBk",

		// meta


		// misc


		// rules
		"weka.classifiers.rules.ZeroR",

		// trees
		"weka.classifiers.trees.J48",
	};
	
	/**
	 * Initialize available classifiers tree
	 */
	protected static void initMainWekaClassifiersTree(GUIMain main) {
		// create root and set to tree
		DefaultMutableTreeNode wekaNode = new DefaultMutableTreeNode("weka");
		DefaultMutableTreeNode classifiersNode = new DefaultMutableTreeNode("classifiers");
		wekaNode.add(classifiersNode);
		DefaultTreeModel model = new DefaultTreeModel(wekaNode);
		main.classJTree.setModel(model);
		
		// add all classes
		DefaultMutableTreeNode currNode, child;
		for (String className: classNames) {
			String[] nameArr = className.split("\\.");
			currNode = classifiersNode;
			for (int i=2; i<nameArr.length; i++) {
				// look for node
				Enumeration<DefaultMutableTreeNode> children = currNode.children();
				while (children.hasMoreElements()) {
					child = children.nextElement();
					if (child.getUserObject().toString().equals(nameArr[i])) {
						currNode = child;
						break;
					}
				}
				
				// if not found, create a new one
				if (!currNode.getUserObject().toString().equals(nameArr[i])) {
					child = new DefaultMutableTreeNode(nameArr[i]);
					currNode.add(child);
					currNode = child;
				}
			}
		}
		
		// expand tree
		int row = 0;
		while (row < main.classJTree.getRowCount())
			main.classJTree.expandRow(row++);
	}
	
	/**
	 * Initialize available classifiers tree
	 */
	protected static void initAdvWekaClassifiersTree(PreProcessSettingsFrame PPSP) {
		// create root and set to tree
		DefaultMutableTreeNode wekaNode = new DefaultMutableTreeNode("weka");
		DefaultMutableTreeNode classifiersNode = new DefaultMutableTreeNode("classifiers");
		wekaNode.add(classifiersNode);
		DefaultTreeModel model = new DefaultTreeModel(wekaNode);
		PPSP.classJTree.setModel(model);
		
		// add all classes
		DefaultMutableTreeNode currNode, child;
		for (String className: classNames) {
			String[] nameArr = className.split("\\.");
			currNode = classifiersNode;
			for (int i=2; i<nameArr.length; i++) {
				// look for node
				Enumeration<DefaultMutableTreeNode> children = currNode.children();
				while (children.hasMoreElements()) {
					child = children.nextElement();
					if (child.getUserObject().toString().equals(nameArr[i])) {
						currNode = child;
						break;
					}
				}
				
				// if not found, create a new one
				if (!currNode.getUserObject().toString().equals(nameArr[i])) {
					child = new DefaultMutableTreeNode(nameArr[i]);
					currNode.add(child);
					currNode = child;
				}
			}
		}
		
		// expand tree
		int row = 0;
		while (row < PPSP.classJTree.getRowCount())
			PPSP.classJTree.expandRow(row++);
	}
	
	/**
	 * Initialize map of classifier class-name to its description.
	 */
	protected static String getDesc(Classifier c) {
		// bayes
		if (c instanceof NaiveBayes) {
			return ((NaiveBayes) c).globalInfo();
		} else if (c instanceof NaiveBayesMultinomial) {
			return ((NaiveBayesMultinomial) c).globalInfo();
		}
		
		// functions
		else if (c instanceof Logistic) {
			return ((Logistic) c).globalInfo();
		}
		else if (c instanceof MultilayerPerceptron) {
			return ((MultilayerPerceptron) c).globalInfo();
		}
		else if (c instanceof SMO) {
			return ((SMO) c).globalInfo();
		}
		
		// lazy
		else if (c instanceof IBk) {
			return ((IBk) c).globalInfo();
		}
		
		// meta

		// misc

		// rules
		else if (c instanceof ZeroR) {
			return ((ZeroR) c).globalInfo();
		}

		// trees
		else if (c instanceof J48) {
			return ((J48) c).globalInfo();
		}
		
		else {
			return "No description available.";
		}
	}
}































