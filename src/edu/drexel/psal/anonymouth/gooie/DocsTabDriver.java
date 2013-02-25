package edu.drexel.psal.anonymouth.gooie;

import java.awt.Point;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.jgaap.generics.Document;

import edu.drexel.psal.jstylo.generics.Logger.LogOut;
import edu.drexel.psal.jstylo.generics.*;

public class DocsTabDriver {
	
	protected static ActionListener clearProblemSetAL;
	protected static ActionListener loadProblemSetAL;
	protected static ActionListener saveProblemSetAL;
	protected static ActionListener addTestDocAL;
	protected static ActionListener removeTestDocAL;
	protected static ActionListener addUserSampleDocAL;
	protected static ActionListener removeUserSampleDocAL;
	protected static ActionListener addTrainDocsAL;
	protected static ActionListener removeTrainDocsAL;
	
	/* =======================
	 * Documents tab listeners
	 * =======================
	 */
	
	
	protected static void setProbSetPathProperty(String path, GUIMain main)
	{
		// saves the path of the file chosen in the properties file
		BufferedWriter writer;
		try {
			main.prop.setProperty("recentProbSet", "" + path);
			writer = new BufferedWriter(new FileWriter(main.propFileName));
			main.prop.store(writer, "User Preferences");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Initialize all documents tab listeners.
	 */
	protected static void initListeners(final GUIMain main) 
	{
		initMainListeners(main);
		initAdvListeners(main);
	}
	
	//=======================================================================================================================
	//=======================================================================================================================
	//+++++++++++++++++++++++++++++++++++++++ Main Listeners ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//=======================================================================================================================
	//=======================================================================================================================
	protected static void initMainListeners(final GUIMain main)
	{
		// new problem set button
		clearProblemSetAL = new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Clear Problem Set' button clicked on the documents tab");
				
				int answer = -1;
				// ask if current problem set is not empty
				if (main.ps != null && (main.ps.hasAuthors() || main.ps.hasTestDocs())) {
					answer = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to clear the current problem set?",
							"Clear Current Problem Set",
							JOptionPane.WARNING_MESSAGE,
							JOptionPane.YES_NO_CANCEL_OPTION);
				}
				if (answer == 0) {					
					main.ps = new ProblemSet();
					main.ps.setTrainCorpusName(main.defaultTrainDocsTreeName);
					GUIUpdateInterface.updateProblemSet(main);// todo This needs to be fixed.. someone screwed it up.. (see function for where it fails -- there's a note)
				}
			}
		};
		main.clearProblemSetJButton.addActionListener(clearProblemSetAL);		
		
		
		// load problem set button
		loadProblemSetAL = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Load Problem Set' button clicked on the documents tab");
				
				int answer = 0;
				// ask if current problem set is not empty
				if (main.ps != null && (main.ps.hasAuthors() || main.ps.hasTestDocs())) {
					answer = JOptionPane.showConfirmDialog(null,
							"Loading Problem Set will override current. Continue?",
							"Load Problem Set",
							JOptionPane.WARNING_MESSAGE,
							JOptionPane.YES_NO_CANCEL_OPTION);
				}
				if (answer == 0) {
					main.load.addChoosableFileFilter(new ExtFilter("XML files (*.xml)", "xml"));
					if (main.prop.getProperty("recentProbSet") != null)
						main.load.setSelectedFile(new File(main.prop.getProperty("recentProbSet")));
					answer = main.load.showDialog(main, "Load Problem Set");
					
					if (answer == JFileChooser.APPROVE_OPTION) {
						String path = main.load.getSelectedFile().getAbsolutePath();
						
						setProbSetPathProperty(path, main);
						
						Logger.logln("Trying to load problem set from "+path);
						try {
							main.ps = new ProblemSet(path);
							ProblemSet temp = main.ps;
							GUIUpdateInterface.updateProblemSet(main);
						} catch (Exception exc) {
							Logger.logln("Failed loading "+path, LogOut.STDERR);
							Logger.logln(exc.toString(),LogOut.STDERR);
							JOptionPane.showMessageDialog(null,
									"Failed loading problem set from:\n"+path,
									"Load Problem Set Failure",
									JOptionPane.ERROR_MESSAGE);
						}
			            
			        } else {
			            Logger.logln("Load problem set canceled");
			        }
				}
			}
		};
		main.loadProblemSetJButton.addActionListener(loadProblemSetAL);
		
		// save problem set button
		saveProblemSetAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Save Problem Set' button clicked on the documents tab.");
				
				main.save.addChoosableFileFilter(new ExtFilter("XML files (*.xml)", "xml"));
				if (main.prop.getProperty("recentProbSet") != null)
					main.save.setSelectedFile(new File(main.prop.getProperty("recentProbSet")));
				int answer = main.save.showSaveDialog(main);
				
				if (answer == JFileChooser.APPROVE_OPTION) {
					File f = main.save.getSelectedFile();
					String path = f.getAbsolutePath();
					
					setProbSetPathProperty(path, main);
					
					if (!path.toLowerCase().endsWith(".xml"))
						path += ".xml";
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(path));
						bw.write(main.ps.toXMLString());
						bw.flush();
						bw.close();
						Logger.log("Saved problem set to "+path+":\n"+main.ps.toXMLString());
					} catch (IOException exc) {
						Logger.logln("Failed opening "+path+" for writing",LogOut.STDERR);
						Logger.logln(exc.toString(),LogOut.STDERR);
						JOptionPane.showMessageDialog(null,
								"Failed saving problem set into:\n"+path,
								"Save Problem Set Failure",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
		            Logger.logln("Save problem set canceled");
		        }
			}
		};
		
		main.saveProblemSetJButton.addActionListener(saveProblemSetAL);
		
		// test documents
		// ==============
		
		// test documents table
		// -- none --
		
		// add test documents button
		addTestDocAL = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Logger.logln("'Add Document(s)...' button clicked under the 'Test Documents' section on the documents tab.");

				JFileChooser open = new JFileChooser();
				open.setMultiSelectionEnabled(true);
				File dir;
				try {
					dir = new File(new File(".").getCanonicalPath());
					open.setCurrentDirectory(dir);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				open.addChoosableFileFilter(new ExtFilter("Text files (*.txt)", "txt"));
				int answer = open.showOpenDialog(main);

				if (answer == JFileChooser.APPROVE_OPTION) {
					File[] files = open.getSelectedFiles();
					String msg = "Trying to load test documents:\n";
					for (File file: files)
						msg += "\t\t> "+file.getAbsolutePath()+"\n";
					Logger.log(msg);
					
					
					String path;
					ArrayList<String> allTestDocPaths = new ArrayList<String>();
					for (Document doc: main.ps.getTestDocs())
						allTestDocPaths.add(doc.getFilePath());
					for (File file: files) {
						path = file.getAbsolutePath();
						if (allTestDocPaths.contains(path))
							continue;
						main.ps.addTestDoc(new Document(path,ProblemSet.getDummyAuthor(),file.getName()));
					}
					
					GUIUpdateInterface.updateTestDocTable(main);

				} else {
					Logger.logln("Load test documents canceled");
				}
			}
		};
		main.addTestDocJButton.addActionListener(addTestDocAL);
		
		// remove test documents button
		removeTestDocAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Logger.logln("'Remove Document(s)...' button clicked under the 'Test Documents' section on the documents tab.");
				
				if (main.prepMainDocList.isSelectionEmpty()) 
				{
					Logger.logln("Failed removing test documents - no documents are selected",LogOut.STDERR);
					JOptionPane.showMessageDialog(null,
							"You must select test documents to remove.",
							"Remove Test Documents Failure",
							JOptionPane.WARNING_MESSAGE);
				} 
				else 
				{
					int answer = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to remove the selected test documents?",
							"Remove Test Documents Confirmation",
							JOptionPane.YES_NO_OPTION);
					
					if (answer == 0) 
					{
						DefaultListModel dlm = (DefaultListModel)main.prepMainDocList.getModel();
						int[] rows = main.prepMainDocList.getSelectedIndices();
						String msg = "Removed test documents:\n";
						for (int i=rows.length-1; i>=0; i--) 
						{
							msg += "\t\t> "+main.ps.testDocAt(rows[i]).getTitle()+"\n";
							main.ps.removeTestDocAt(rows[i]);
						}
						Logger.log(msg);
						
						GUIUpdateInterface.updateTestDocTable(main);
					} 
					else 
					{
						Logger.logln("Removing test documents canceled");
					}
				}
			}
		};
		main.removeTestDocJButton.addActionListener(removeTestDocAL);
		
		
		/////////////////// userSampleDocuments
		
		addUserSampleDocAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Add Document(s)...' button clicked under the 'User Sample Documents' section on the documents tab.");
				
				JFileChooser open = new JFileChooser();
				File dir;
				try {
					dir = new File(new File(".").getCanonicalPath());
					open.setCurrentDirectory(dir);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				open.setMultiSelectionEnabled(true);
				open.addChoosableFileFilter(new ExtFilter("Text files (*.txt)", "txt"));
				int answer = open.showOpenDialog(main);

				if (answer == JFileChooser.APPROVE_OPTION) {
					File[] files = open.getSelectedFiles();
					String msg = "Trying to load User Sample documents:\n";
					for (File file: files)
						msg += "\t\t> "+file.getAbsolutePath()+"\n";
					Logger.log(msg);
					
					
					String path;
					ArrayList<String> allUserSampleDocPaths = new ArrayList<String>();
					for (Document doc: main.ps.getTestDocs())
						allUserSampleDocPaths.add(doc.getFilePath());
					for (Document doc: main.ps.getAllTrainDocs())
						allUserSampleDocPaths.add(doc.getFilePath());
					for (File file: files) {
						path = file.getAbsolutePath();
						if (allUserSampleDocPaths.contains(path))
							continue;
						main.ps.addTrainDoc(ProblemSet.getDummyAuthor(), new Document(path,ProblemSet.getDummyAuthor(),file.getName()));
					}
					
					GUIUpdateInterface.updateUserSampleDocTable(main);
				} else {
					Logger.logln("Load user sample documents canceled");
				}
			}
		};
		main.adduserSampleDocJButton.addActionListener(addUserSampleDocAL);
			
		// remove userSample documents button
		removeUserSampleDocAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Remove Document(s)...' button clicked under the 'User Sample Documents' section on the documents tab.");
				
				if (main.prepSampleDocsList.isSelectionEmpty()) {
					Logger.logln("Failed removing user sample documents - no documents are selected",LogOut.STDERR);
					JOptionPane.showMessageDialog(null,
							"You must select documents to remove.",
							"Remove Documents Failure",
							JOptionPane.WARNING_MESSAGE);
				} else {
					int answer = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to remove the selected documents?",
							"Remove Documents Confirmation",
							JOptionPane.YES_NO_OPTION);
					
					if (answer == 0) {
						DefaultListModel dlm = (DefaultListModel)main.prepSampleDocsList.getModel();
						int[] rows = main.prepSampleDocsList.getSelectedIndices();
						String msg = "Removed test documents:\n";
						for (int i=rows.length-1; i>=0; i--) {
							msg += "\t\t> "+main.ps.trainDocAt(ProblemSet.getDummyAuthor(),rows[i]).getTitle()+"\n";
							main.ps.removeTrainDocAt(ProblemSet.getDummyAuthor(),rows[i]);
						}
						Logger.log(msg);
						
						GUIUpdateInterface.updateUserSampleDocTable(main);
					} else {
						Logger.logln("Removing user sample documents canceled");
					}
				}
			}
		};
		main.removeuserSampleDocJButton.addActionListener(removeUserSampleDocAL);
		
		// add training documents button
		addTrainDocsAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Add Document(s)...' button clicked under the 'Training Corpus' section on the documents tab.");
				if(main.ps.getTestDocs().size() == 0){
					JOptionPane.showMessageDialog(null,"You must first select your document to anonymize and your sample documents.","Add Your Own First!", JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					boolean mustBeFolders = false;
					if ( main.trainCorpusJTree.getSelectionCount() == 0 ||
							main.trainCorpusJTree.getSelectionPath().getPath().length != 2) {
						JOptionPane.showMessageDialog(null,
								"You have not selected an author to add documents to. Because of this,\n" +
										"you must select one or more folders containing training documents.\n" +
										"The folder name will be taken as the author name.\n" +
										"If you would rather choose an author and select documents for that author,\n" +
										"first add an author, and then select documents (rather than folder(s)).",
										"Add Training Documents Note",
										JOptionPane.INFORMATION_MESSAGE);
						Logger.logln("tried to add training documents without selecting an author", LogOut.STDERR);
						mustBeFolders = true;

					}
					String author = "no author entered";
					try{
						author = main.trainCorpusJTree.getSelectionPath().getPath()[1].toString();
					} catch(NullPointerException npe){
						Logger.logln("no author entered prior to clicking 'Add Document(s)' button. Must select folder with documents - folder name will be set as author name.", LogOut.STDERR);
					}
					JFileChooser open = new JFileChooser();
					open.setMultiSelectionEnabled(true);
					File dir;
					try {
						dir = new File(new File(".").getCanonicalPath());
						open.setCurrentDirectory(dir);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					open.addChoosableFileFilter(new ExtFilter("Text files (*.txt)", "txt"));
					open.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					int answer = open.showOpenDialog(main);

					if (answer == JFileChooser.APPROVE_OPTION) {

						File[] files = open.getSelectedFiles();
						String msg = "Trying to load training documents for author \""+author+"\":\n";
						String seperator = System.getProperty("file.separator");
						for (File file: files)
							msg += "\t\t> "+file.getAbsolutePath()+"\n";
						Logger.log(msg);

						String path = "";
						String skipList = "";
						ArrayList<String> allTrainDocPaths = new ArrayList<String>();
						ArrayList<String> allTestDocPaths = new ArrayList<String>();
						try{
							for (Document doc: main.ps.getTrainDocs(author))
							{
								allTrainDocPaths.add(doc.getFilePath());
								Logger.logln("Added to Train Docs: " + doc.getFilePath());
							}
						} catch(NullPointerException npe){
							Logger.logln("file '"+author+"' was not found. If name in single quotes is 'no author entered', this is not a problem.", LogOut.STDERR);
						}

						for (Document doc: main.ps.getTestDocs())
							allTestDocPaths.add(doc.getFilePath());
						for (Document doc: main.ps.getTrainDocs(ProblemSet.getDummyAuthor()))
							allTestDocPaths.add(doc.getFilePath());
						for (File file: files) {
							if(file.isDirectory()){
								String[] theDocsInTheDir = file.list();
								author = file.getName();
								String pathFirstHalf = file.getAbsolutePath();
								for (String otherFile: theDocsInTheDir){
									File newFile = new File(otherFile);
									//author = newFile.getName();
									path = pathFirstHalf+File.separator+otherFile;
									System.out.println(path);
									if (allTrainDocPaths.contains(path)) {
										skipList += "\n"+path+" - already contained for author "+author;
										continue;
									}
									if (allTestDocPaths.contains(path)) {
										skipList += "\n"+path+" - already contained as a test document";
										continue;
									}
									if(path.contains(".svn") || path.contains("imitation") || path.contains("verification") || path.contains("obfuscation") || path.contains("demographics"))
										continue;
									main.ps.addTrainDocs(author, new ArrayList<Document>());
									main.ps.addTrainDoc(author, new Document(path,author,newFile.getName()));
								}
							}
							else if (mustBeFolders == true){
								JOptionPane.showMessageDialog(null,
										"You did not select an author to add documents to,\n" +
												"and did not select a folder full of documents\n" +
												"Please either choose an author and then select documents,\n" +
												"or select a folder containing training documents for a single author.\n",
												"Add Training Documents Error",
												JOptionPane.ERROR_MESSAGE);
								Logger.logln("tried to add training documents without selecting an author", LogOut.STDERR);
							}
							else{
								path = file.getAbsolutePath();
								if (allTrainDocPaths.contains(path)) {
									skipList += "\n"+path+" - already contained for author "+author;
									continue;
								}
								if (allTestDocPaths.contains(path)) {
									skipList += "\n"+path+" - already contained as a test document";
									continue;
								}
								main.ps.addTrainDoc(author, new Document(path,ProblemSet.getDummyAuthor(),file.getName()));
							}
						}

						if (!skipList.equals("")) {
							JOptionPane.showMessageDialog(null,
									"Skipped the following documents:"+skipList,
									"Add Training Documents",
									JOptionPane.WARNING_MESSAGE);
							Logger.logln("skipped the following training documents:"+skipList);
						}

						GUIUpdateInterface.updateTrainDocTree(main);
						GUIUpdateInterface.clearDocPreview(main);

					} else {
						Logger.logln("Load training documents canceled");
					}
				}
			}
			
		};
		main.addTrainDocsJButton.addActionListener(addTrainDocsAL);

		
		// remove training documents button
		removeTrainDocsAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Remove Document(s)' button clicked under the 'Training Corpus' section on the documents tab.");
				
				TreePath[] paths = main.trainCorpusJTree.getSelectionPaths();
				List<DefaultMutableTreeNode> selectedDocs = new ArrayList<DefaultMutableTreeNode>();
				if (paths != null)
					for (TreePath path: paths)
						if (path.getPath().length == 3)
							selectedDocs.add((DefaultMutableTreeNode)path.getPath()[2]);

				if (selectedDocs.isEmpty()) {
					Logger.logln("Failed removing training documents - no documents are selected",LogOut.STDERR);
					JOptionPane.showMessageDialog(null,
							"You must select training documents to remove.",
							"Remove Training Documents Failure",
							JOptionPane.WARNING_MESSAGE);
				} else {
					int answer = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to remove the selected training documents?",
							"Remove Training Documents Confirmation",
							JOptionPane.YES_NO_OPTION);

					if (answer == 0) {
						String msg = "Removed training documents:\n";
						String author;
						for (DefaultMutableTreeNode doc: selectedDocs) {
							author = doc.getParent().toString();
							main.ps.removeTrainDocAt(author, doc.toString());
							msg += "\t\t> "+doc.toString()+"\n";
						}
						Logger.log(msg);
						GUIUpdateInterface.updateTrainDocTree(main);
						//GUIUpdateInterface.clearDocPreview(main);
					} else {
						Logger.logln("Removing training documents canceled");
					}
				}
			}
		};
		main.removeTrainDocsJButton.addActionListener(removeTrainDocsAL);
	}
	
	//=======================================================================================================================
	//=======================================================================================================================
	//+++++++++++++++++++++++++++++++++++++++ Advanced Listeners ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//=======================================================================================================================
	//=======================================================================================================================
	
	protected static void initAdvListeners(final GUIMain main)
	{
		// problem set
		// ===========
		
		// new problem set button
		clearProblemSetAL = new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Clear Problem Set' button clicked on the documents tab");
				
				int answer = 0;
				// ask if current problem set is not empty
				if (main.ps != null && (main.ps.hasAuthors() || main.ps.hasTestDocs())) {
					answer = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to clear the current problem set?",
							"Clear Current Problem Set",
							JOptionPane.WARNING_MESSAGE,
							JOptionPane.YES_NO_CANCEL_OPTION);
				}
				if (answer == 0) {					
					main.ps = new ProblemSet();
					main.ps.setTrainCorpusName(main.defaultTrainDocsTreeName);
					GUIUpdateInterface.updateProblemSet(main);// todo This needs to be fixed.. someone screwed it up.. (see function for where it fails -- there's a note)
				}
			}
		};
		main.PPSP.clearProblemSetJButton.addActionListener(clearProblemSetAL);
		
		// load problem set button
		loadProblemSetAL = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Load Problem Set' button clicked on the documents tab");
				
				int answer = 0;
				// ask if current problem set is not empty
				if (main.ps != null && (main.ps.hasAuthors() || main.ps.hasTestDocs())) {
					answer = JOptionPane.showConfirmDialog(null,
							"Loading Problem Set will override current. Continue?",
							"Load Problem Set",
							JOptionPane.WARNING_MESSAGE,
							JOptionPane.YES_NO_CANCEL_OPTION);
				}
				if (answer == 0) {
					main.load.addChoosableFileFilter(new ExtFilter("XML files (*.xml)", "xml"));
					if (main.prop.getProperty("recentProbSet") != null)
						main.load.setSelectedFile(new File(main.prop.getProperty("recentProbSet")));
					answer = main.load.showDialog(main, "Load Problem Set");
					
					if (answer == JFileChooser.APPROVE_OPTION) {
						String path = main.load.getSelectedFile().getAbsolutePath();
						
						setProbSetPathProperty(path, main);
						
						Logger.logln("Trying to load problem set from "+path);
						try {
							main.ps = new ProblemSet(path);
							ProblemSet temp = main.ps;
							GUIUpdateInterface.updateProblemSet(main);
						} catch (Exception exc) {
							Logger.logln("Failed loading "+path, LogOut.STDERR);
							Logger.logln(exc.toString(),LogOut.STDERR);
							JOptionPane.showMessageDialog(null,
									"Failed loading problem set from:\n"+path,
									"Load Problem Set Failure",
									JOptionPane.ERROR_MESSAGE);
						}
			            
			        } else {
			            Logger.logln("Load problem set canceled");
			        }
				}
			}
		};
		main.PPSP.loadProblemSetJButton.addActionListener(loadProblemSetAL);
		
		// save problem set button
		saveProblemSetAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Save Problem Set' button clicked on the documents tab.");
				
				main.save.addChoosableFileFilter(new ExtFilter("XML files (*.xml)", "xml"));
				if (main.prop.getProperty("recentProbSet") != null)
					main.save.setSelectedFile(new File(main.prop.getProperty("recentProbSet")));
				int answer = main.save.showSaveDialog(main);
				
				if (answer == JFileChooser.APPROVE_OPTION) {
					File f = main.save.getSelectedFile();
					String path = f.getAbsolutePath();
					
					setProbSetPathProperty(path, main);
					
					if (!path.toLowerCase().endsWith(".xml"))
						path += ".xml";
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(path));
						bw.write(main.ps.toXMLString());
						bw.flush();
						bw.close();
						Logger.log("Saved problem set to "+path+":\n"+main.ps.toXMLString());
					} catch (IOException exc) {
						Logger.logln("Failed opening "+path+" for writing",LogOut.STDERR);
						Logger.logln(exc.toString(),LogOut.STDERR);
						JOptionPane.showMessageDialog(null,
								"Failed saving problem set into:\n"+path,
								"Save Problem Set Failure",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
		            Logger.logln("Save problem set canceled");
		        }
			}
		};
		
		main.PPSP.saveProblemSetJButton.addActionListener(saveProblemSetAL);
		
		// test documents
		// ==============
		
		// test documents table
		// -- none --
		
		// add test documents button
		addTestDocAL = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Logger.logln("'Add Document(s)...' button clicked under the 'Test Documents' section on the documents tab.");

				JFileChooser open = new JFileChooser();
				open.setMultiSelectionEnabled(true);
				File dir;
				try {
					dir = new File(new File(".").getCanonicalPath());
					open.setCurrentDirectory(dir);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				open.addChoosableFileFilter(new ExtFilter("Text files (*.txt)", "txt"));
				int answer = open.showOpenDialog(main);

				if (answer == JFileChooser.APPROVE_OPTION) {
					File[] files = open.getSelectedFiles();
					String msg = "Trying to load test documents:\n";
					for (File file: files)
						msg += "\t\t> "+file.getAbsolutePath()+"\n";
					Logger.log(msg);
					
					
					String path;
					ArrayList<String> allTestDocPaths = new ArrayList<String>();
					for (Document doc: main.ps.getTestDocs())
						allTestDocPaths.add(doc.getFilePath());
					for (File file: files) {
						path = file.getAbsolutePath();
						if (allTestDocPaths.contains(path))
							continue;
						main.ps.addTestDoc(new Document(path,ProblemSet.getDummyAuthor(),file.getName()));
					}
					
					GUIUpdateInterface.updateTestDocTable(main);

				} else {
					Logger.logln("Load test documents canceled");
				}
			}
		};
		main.PPSP.addTestDocJButton.addActionListener(addTestDocAL);
		
		// remove test documents button
		removeTestDocAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Logger.logln("'Remove Document(s)...' button clicked under the 'Test Documents' section on the documents tab.");
				
				if (main.PPSP.prepMainDocList.isSelectionEmpty()) 
				{
					Logger.logln("Failed removing test documents - no documents are selected",LogOut.STDERR);
					JOptionPane.showMessageDialog(null,
							"You must select test documents to remove.",
							"Remove Test Documents Failure",
							JOptionPane.WARNING_MESSAGE);
				} 
				else 
				{
					int answer = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to remove the selected test documents?",
							"Remove Test Documents Confirmation",
							JOptionPane.YES_NO_OPTION);
					
					if (answer == 0) 
					{
						DefaultListModel dlm = (DefaultListModel)main.PPSP.prepMainDocList.getModel();
						int[] rows = main.PPSP.prepMainDocList.getSelectedIndices();
						String msg = "Removed test documents:\n";
						for (int i=rows.length-1; i>=0; i--) 
						{
							msg += "\t\t> "+main.ps.testDocAt(rows[i]).getTitle()+"\n";
							main.ps.removeTestDocAt(rows[i]);
						}
						Logger.log(msg);
						
						GUIUpdateInterface.updateTestDocTable(main);
					} 
					else 
					{
						Logger.logln("Removing test documents canceled");
					}
				}
			}
		};
		main.PPSP.removeTestDocJButton.addActionListener(removeTestDocAL);
		
		// preview test document button
//				main.testDocPreviewJButton.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						Logger.logln("'Preview Document' button clicked under the 'Test Documents' section on the documents tab.");
//						
//						int row = main.testDocsJTable.getSelectedRow();
//						if (row == -1) {
//							JOptionPane.showMessageDialog(null,
//									"You must select a test document in order to show its preview.",
//									"Show Test Document Preview Error",
//									JOptionPane.ERROR_MESSAGE);
//							Logger.logln("No test document is selected for preview",LogOut.STDERR);
//						} else {
//							Document doc = main.ps.testDocAt(row);
//							try {
//								doc.load();
//								main.docPreviewNameJLabel.setText("- "+doc.getTitle());
//								main.docPreviewJTextPane.setText(doc.stringify());
//							} catch (Exception exc) {
//								JOptionPane.showMessageDialog(null,
//										"Failed opening test document for preview:\n"+doc.getFilePath(),
//										"Show Test Document Preview Error",
//										JOptionPane.ERROR_MESSAGE);
//								Logger.logln("Failed opening test document for preview",LogOut.STDERR);
//								Logger.logln(exc.toString(),LogOut.STDERR);
//								GUIUpdateInterface.clearDocPreview(main);
//							}
//						}
//					}
//				});
		
		
		/////////////////// userSampleDocuments
		
		addUserSampleDocAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Add Document(s)...' button clicked under the 'User Sample Documents' section on the documents tab.");
				
				JFileChooser open = new JFileChooser();
				File dir;
				try {
					dir = new File(new File(".").getCanonicalPath());
					open.setCurrentDirectory(dir);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				open.setMultiSelectionEnabled(true);
				open.addChoosableFileFilter(new ExtFilter("Text files (*.txt)", "txt"));
				int answer = open.showOpenDialog(main);

				if (answer == JFileChooser.APPROVE_OPTION) {
					File[] files = open.getSelectedFiles();
					String msg = "Trying to load User Sample documents:\n";
					for (File file: files)
						msg += "\t\t> "+file.getAbsolutePath()+"\n";
					Logger.log(msg);
					
					
					String path;
					ArrayList<String> allUserSampleDocPaths = new ArrayList<String>();
					for (Document doc: main.ps.getTestDocs())
						allUserSampleDocPaths.add(doc.getFilePath());
					for (Document doc: main.ps.getAllTrainDocs())
						allUserSampleDocPaths.add(doc.getFilePath());
					for (File file: files) {
						path = file.getAbsolutePath();
						if (allUserSampleDocPaths.contains(path))
							continue;
						main.ps.addTrainDoc(ProblemSet.getDummyAuthor(), new Document(path,ProblemSet.getDummyAuthor(),file.getName()));
					}
					
					GUIUpdateInterface.updateUserSampleDocTable(main);
				} else {
					Logger.logln("Load user sample documents canceled");
				}
			}
		};
		main.PPSP.adduserSampleDocJButton.addActionListener(addUserSampleDocAL);
			
		// remove userSample documents button
		removeUserSampleDocAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Remove Document(s)...' button clicked under the 'User Sample Documents' section on the documents tab.");
				
				if (main.PPSP.prepSampleDocsList.isSelectionEmpty()) {
					Logger.logln("Failed removing user sample documents - no documents are selected",LogOut.STDERR);
					JOptionPane.showMessageDialog(null,
							"You must select documents to remove.",
							"Remove Documents Failure",
							JOptionPane.WARNING_MESSAGE);
				} else {
					int answer = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to remove the selected documents?",
							"Remove Documents Confirmation",
							JOptionPane.YES_NO_OPTION);
					
					if (answer == 0) {
						DefaultListModel dlm = (DefaultListModel)main.PPSP.prepSampleDocsList.getModel();
						int[] rows = main.PPSP.prepSampleDocsList.getSelectedIndices();
						String msg = "Removed test documents:\n";
						for (int i=rows.length-1; i>=0; i--) {
							msg += "\t\t> "+main.ps.trainDocAt(ProblemSet.getDummyAuthor(),rows[i]).getTitle()+"\n";
							main.ps.removeTrainDocAt(ProblemSet.getDummyAuthor(),rows[i]);
						}
						Logger.log(msg);
						
						GUIUpdateInterface.updateUserSampleDocTable(main);
					} else {
						Logger.logln("Removing user sample documents canceled");
					}
				}
			}
		};
		main.PPSP.removeuserSampleDocJButton.addActionListener(removeUserSampleDocAL);
			
			// preview userSample document button
//					main.userSampleDocPreviewJButton.addActionListener(new ActionListener() {
//						
//						@Override
//						public void actionPerformed(ActionEvent e) {
//							Logger.logln("'Preview Document' button clicked under the 'User Sample Documents' section on the documents tab.");
//							
//							int row = main.userSampleDocsJTable.getSelectedRow();
//							if (row == -1) {
//								JOptionPane.showMessageDialog(null,
//										"You must select a document in order to show its preview.",
//										"Show Document Preview Error",
//										JOptionPane.ERROR_MESSAGE);
//								Logger.logln("No user sample document is selected for preview",LogOut.STDERR);
//							} else {
//								Document doc = main.ps.trainDocAt(ProblemSet.getDummyAuthor(),row);
//								try {
//									doc.load();
//									main.docPreviewNameJLabel.setText("- "+doc.getTitle());
//									main.docPreviewJTextPane.setText(doc.stringify());
//								} catch (Exception exc) {
//									JOptionPane.showMessageDialog(null,
//											"Failed opening document for preview:\n"+doc.getFilePath(),
//											"Show Document Preview Error",
//											JOptionPane.ERROR_MESSAGE);
//									Logger.logln("Failed opening user sample document for preview",LogOut.STDERR);
//									Logger.logln(exc.toString(),LogOut.STDERR);
//									GUIUpdateInterface.clearDocPreview(main);
//								}
//							}
//						}
//					});

		// training documents
		// ==================
		
		// training documents tree
		// -- none --
		
		// add author button
//				main.addAuthorJButton.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent e) 
//					{
//						Logger.logln("'Add Author...' button clicked under the 'Training Corpus' section on the documents tab.");
//
//						String answer = JOptionPane.showInputDialog(null,
//								"Enter new author name:",
//								"",
//								JOptionPane.OK_CANCEL_OPTION);
//						if (answer == null) 
//						{
//							Logger.logln("Aborted adding new author");
//						}
//						else if (answer.isEmpty()) 
//						{
//							JOptionPane.showMessageDialog(null,
//									"New author name must be a non-empty string.",
//									"Add New Author Error",
//									JOptionPane.ERROR_MESSAGE);
//							Logger.logln("tried to add new author with an empty string", LogOut.STDERR);
//						} 
//						else 
//						{
//							if (main.ps.getAuthorMap().keySet().contains(answer)) 
//							{
//								JOptionPane.showMessageDialog(null,
//										"Author \""+answer+"\" already exists.",
//										"Add New Author Error",
//										JOptionPane.ERROR_MESSAGE);
//								Logger.logln("tried to add author that already exists: "+answer, LogOut.STDERR);
//							} 
//							else 
//							{
//								main.ps.addTrainDocs(answer, new ArrayList<Document>());
//								GUIUpdateInterface.updateTrainDocTree(main);
//								Logger.logln("Added new author: "+answer);
//							}
//						}
//					}
//				});
		
		// add training documents button
		addTrainDocsAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Add Document(s)...' button clicked under the 'Training Corpus' section on the documents tab.");
				if(main.ps.getTestDocs().size() == 0){
					JOptionPane.showMessageDialog(null,"You must first select your document to anonymize and your sample documents.","Add Your Own First!", JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					boolean mustBeFolders = false;
					if ( main.trainCorpusJTree.getSelectionCount() == 0 ||
							main.trainCorpusJTree.getSelectionPath().getPath().length != 2) {
						JOptionPane.showMessageDialog(null,
								"You have not selected an author to add documents to. Because of this,\n" +
										"you must select one or more folders containing training documents.\n" +
										"The folder name will be taken as the author name.\n" +
										"If you would rather choose an author and select documents for that author,\n" +
										"first add an author, and then select documents (rather than folder(s)).",
										"Add Training Documents Note",
										JOptionPane.INFORMATION_MESSAGE);
						Logger.logln("tried to add training documents without selecting an author", LogOut.STDERR);
						mustBeFolders = true;

					}
					String author = "no author entered";
					try{
						author = main.trainCorpusJTree.getSelectionPath().getPath()[1].toString();
					} catch(NullPointerException npe){
						Logger.logln("no author entered prior to clicking 'Add Document(s)' button. Must select folder with documents - folder name will be set as author name.", LogOut.STDERR);
					}
					JFileChooser open = new JFileChooser();
					open.setMultiSelectionEnabled(true);
					File dir;
					try {
						dir = new File(new File(".").getCanonicalPath());
						open.setCurrentDirectory(dir);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					open.addChoosableFileFilter(new ExtFilter("Text files (*.txt)", "txt"));
					open.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					int answer = open.showOpenDialog(main);

					if (answer == JFileChooser.APPROVE_OPTION) {

						File[] files = open.getSelectedFiles();
						String msg = "Trying to load training documents for author \""+author+"\":\n";
						String seperator = System.getProperty("file.separator");
						for (File file: files)
							msg += "\t\t> "+file.getAbsolutePath()+"\n";
						Logger.log(msg);

						String path = "";
						String skipList = "";
						ArrayList<String> allTrainDocPaths = new ArrayList<String>();
						ArrayList<String> allTestDocPaths = new ArrayList<String>();
						try{
							for (Document doc: main.ps.getTrainDocs(author))
							{
								allTrainDocPaths.add(doc.getFilePath());
								Logger.logln("Added to Train Docs: " + doc.getFilePath());
							}
						} catch(NullPointerException npe){
							Logger.logln("file '"+author+"' was not found. If name in single quotes is 'no author entered', this is not a problem.", LogOut.STDERR);
						}

						for (Document doc: main.ps.getTestDocs())
							allTestDocPaths.add(doc.getFilePath());
						for (Document doc: main.ps.getTrainDocs(ProblemSet.getDummyAuthor()))
							allTestDocPaths.add(doc.getFilePath());
						for (File file: files) {
							if(file.isDirectory()){
								String[] theDocsInTheDir = file.list();
								author = file.getName();
								String pathFirstHalf = file.getAbsolutePath();
								for (String otherFile: theDocsInTheDir){
									File newFile = new File(otherFile);
									//author = newFile.getName();
									path = pathFirstHalf+File.separator+otherFile;
									System.out.println(path);
									if (allTrainDocPaths.contains(path)) {
										skipList += "\n"+path+" - already contained for author "+author;
										continue;
									}
									if (allTestDocPaths.contains(path)) {
										skipList += "\n"+path+" - already contained as a test document";
										continue;
									}
									if(path.contains(".svn") || path.contains("imitation") || path.contains("verification") || path.contains("obfuscation") || path.contains("demographics"))
										continue;
									main.ps.addTrainDocs(author, new ArrayList<Document>());
									main.ps.addTrainDoc(author, new Document(path,author,newFile.getName()));
								}
							}
							else if (mustBeFolders == true){
								JOptionPane.showMessageDialog(null,
										"You did not select an author to add documents to,\n" +
												"and did not select a folder full of documents\n" +
												"Please either choose an author and then select documents,\n" +
												"or select a folder containing training documents for a single author.\n",
												"Add Training Documents Error",
												JOptionPane.ERROR_MESSAGE);
								Logger.logln("tried to add training documents without selecting an author", LogOut.STDERR);
							}
							else{
								path = file.getAbsolutePath();
								if (allTrainDocPaths.contains(path)) {
									skipList += "\n"+path+" - already contained for author "+author;
									continue;
								}
								if (allTestDocPaths.contains(path)) {
									skipList += "\n"+path+" - already contained as a test document";
									continue;
								}
								main.ps.addTrainDoc(author, new Document(path,ProblemSet.getDummyAuthor(),file.getName()));
							}
						}

						if (!skipList.equals("")) {
							JOptionPane.showMessageDialog(null,
									"Skipped the following documents:"+skipList,
									"Add Training Documents",
									JOptionPane.WARNING_MESSAGE);
							Logger.logln("skipped the following training documents:"+skipList);
						}

						GUIUpdateInterface.updateTrainDocTree(main);
						GUIUpdateInterface.clearDocPreview(main);

					} else {
						Logger.logln("Load training documents canceled");
					}
				}
			}
			
		};
		main.PPSP.addTrainDocsJButton.addActionListener(addTrainDocsAL);
		
		// edit corpus name button
//				main.trainNameJButton.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						Logger.logln("'Edit Name...' button clicked under the 'Training Corpus' section on the documents tab.");
//						
//						String answer = JOptionPane.showInputDialog(null,
//								"Edit corpus name:",
//								main.ps.getTrainCorpusName());
//						if (answer == null) {
//							Logger.logln("Aborted editing corpus name");
//						} else if (answer.isEmpty()) {
//							JOptionPane.showMessageDialog(null,
//									"Training corpus name must be a non-empty string.",
//									"Edit Training Corpus Name Error",
//									JOptionPane.ERROR_MESSAGE);
//							Logger.logln("tried to change training corpus name to an empty string", LogOut.STDERR);
//						} else {
//							main.ps.setTrainCorpusName(answer);
//							GUIUpdateInterface.updateTrainDocTree(main);
//						}
//					}
//				});

		
		// remove author button
//				main.removeAuthorJButton.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						Logger.logln("'Remove Author(s)' button clicked under the 'Training Corpus' section on the documents tab.");
//						
//						TreePath[] paths = main.trainCorpusJTree.getSelectionPaths();
//						List<DefaultMutableTreeNode> selectedAuthors = new ArrayList<DefaultMutableTreeNode>();
//						if (paths != null)
//							for (TreePath path: paths)
//								if (path.getPath().length == 2)
//									selectedAuthors.add((DefaultMutableTreeNode)path.getPath()[1]);
//
//						if (selectedAuthors.isEmpty()) {
//							Logger.logln("Failed removing authors - no authors are selected",LogOut.STDERR);
//							JOptionPane.showMessageDialog(null,
//									"You must select authors to remove.",
//									"Remove Authors Failure",
//									JOptionPane.WARNING_MESSAGE);
//						} else {
//							int answer = JOptionPane.showConfirmDialog(null,
//									"Are you sure you want to remove the selected authors?",
//									"Remove Authors Confirmation",
//									JOptionPane.YES_NO_OPTION);
//
//							if (answer == 0) {
//								String msg = "Removed authors:\n";
//								for (DefaultMutableTreeNode author: selectedAuthors) {
//									main.ps.removeAuthor(author.toString());
//									msg += "\t\t> "+author.toString()+"\n";
//								}
//								Logger.log(msg);
//								GUIUpdateInterface.updateTrainDocTree(main);
//								GUIUpdateInterface.clearDocPreview(main);
//							} else {
//								Logger.logln("Removing authors canceled");
//							}
//						}
//					}
//				});

		
		// remove training documents button
		removeTrainDocsAL = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Logger.logln("'Remove Document(s)' button clicked under the 'Training Corpus' section on the documents tab.");
				
				TreePath[] paths = main.PPSP.trainCorpusJTree.getSelectionPaths();
				List<DefaultMutableTreeNode> selectedDocs = new ArrayList<DefaultMutableTreeNode>();
				if (paths != null)
					for (TreePath path: paths)
						if (path.getPath().length == 3)
							selectedDocs.add((DefaultMutableTreeNode)path.getPath()[2]);

				if (selectedDocs.isEmpty()) {
					Logger.logln("Failed removing training documents - no documents are selected",LogOut.STDERR);
					JOptionPane.showMessageDialog(null,
							"You must select training documents to remove.",
							"Remove Training Documents Failure",
							JOptionPane.WARNING_MESSAGE);
				} else {
					int answer = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to remove the selected training documents?",
							"Remove Training Documents Confirmation",
							JOptionPane.YES_NO_OPTION);

					if (answer == 0) {
						String msg = "Removed training documents:\n";
						String author;
						for (DefaultMutableTreeNode doc: selectedDocs) {
							author = doc.getParent().toString();
							main.ps.removeTrainDocAt(author, doc.toString());
							msg += "\t\t> "+doc.toString()+"\n";
						}
						Logger.log(msg);
						GUIUpdateInterface.updateTrainDocTree(main);
						//GUIUpdateInterface.clearDocPreview(main);
					} else {
						Logger.logln("Removing training documents canceled");
					}
				}
			}
		};
		main.PPSP.removeTrainDocsJButton.addActionListener(removeTrainDocsAL);

				
				// preview training document button
//				main.trainDocPreviewJButton.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						Logger.logln("'Preview Document' button clicked under the 'Training Corpus' section on the documents tab.");
//						
//						TreePath path = main.trainCorpusJTree.getSelectionPath();
//						if (path == null || path.getPathCount() != 3) {
//							JOptionPane.showMessageDialog(null,
//									"You must select a training document in order to show its preview.",
//									"Show Training Document Preview Error",
//									JOptionPane.ERROR_MESSAGE);
//							Logger.logln("No training document is selected for preview",LogOut.STDERR);
//						} else {
//							String docTitle = path.getPath()[2].toString();
//							Document doc = main.ps.trainDocAt(path.getPath()[1].toString(),docTitle);
//							try {
//								doc.load();
//								main.docPreviewNameJLabel.setText("- "+doc.getTitle());
//								main.docPreviewJTextPane.setText(doc.stringify());
//							} catch (Exception exc) {
//								JOptionPane.showMessageDialog(null,
//										"Failed opening training document for preview:\n"+doc.getFilePath(),
//										"Show Training Document Preview Error",
//										JOptionPane.ERROR_MESSAGE);
//								Logger.logln("Failed opening training document for preview",LogOut.STDERR);
//								Logger.logln(exc.toString(),LogOut.STDERR);
//								GUIUpdateInterface.clearDocPreview(main);
//							}
//						}
//					}
//				});

				
				// document preview
				// ================
				
				// document preview clear button
//				main.clearDocPreviewJButton.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						Logger.logln("'Clear Preview' button clicked on the documents tab.");
//						
//						GUIUpdateInterface.clearDocPreview(main);
//					}
//				});

				// button toolbar operations
				// =========================

				// about button
				// ============
		/*
				main.docsAboutJButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						GUIUpdateInterface.showAbout(main);
					}
				});
		*/
				// next button
//				main.docTabNextJButton.addActionListener(new ActionListener() {
//					
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						Logger.logln("'Next' button clicked on the documents tab.");
		//
//						if (main.ps == null || !main.ps.hasAuthors() || !main.ps.hasTestDocs()) {
//							JOptionPane.showMessageDialog(null,
//									"You must set training corpus and test documents before continuing.",
//									"Error",
//									JOptionPane.ERROR_MESSAGE);
//						} 
//						
//						else
//							main.mainJTabbedPane.setSelectedIndex(1);
//					}
//				});
	}
	
	
	
	
	/*
	 * =====================
	 * Supporting operations
	 * =====================
	 */

	/**
	 * Extension File Filter
	 */
	public static class ExtFilter extends FileFilter {
		
		private String desc;
		private String[] exts;
		
		// constructors
		
		public ExtFilter(String desc, String[] exts) {
			this.desc = desc;
			this.exts = exts;
		}
		
		public ExtFilter(String desc, String ext) {
			this.desc = desc;
			this.exts = new String[] {ext};
		}
		
		// operations
		
		@Override
		public String getDescription() {
			return desc;
		}

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) return true;
			String path = f.getAbsolutePath().toLowerCase();
			for (String extension: exts) {
				if ((path.endsWith(extension) &&
						(path.charAt(path.length() - extension.length() - 1)) == '.'))
					return true;
			}
			return false;
		}
	}
}
