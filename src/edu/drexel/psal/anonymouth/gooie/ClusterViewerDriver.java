package edu.drexel.psal.anonymouth.gooie;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.drexel.psal.anonymouth.projectDev.ClusterAnalyzer;
import edu.drexel.psal.anonymouth.projectDev.ClusterGroup;
import edu.drexel.psal.anonymouth.projectDev.DataAnalyzer;
import edu.drexel.psal.jstylo.generics.Logger;

public class ClusterViewerDriver {
	
	private GUIMain main = GUIMain.inst;
	
	private static int lenJPanels;
	public static boolean clusterGroupReady = false;
	private static ClusterGroup[] clusterGroupRay;
	private static int lenCGR;
	private static int[][] intRepresentation;
	private static String[] stringRepresentation;
	protected static JPanel[] namePanels;
	
	public static int[][] getIntRep()
	{
		return intRepresentation;
	}
	
	public static String[] getStringRep()
	{
		return stringRepresentation;
	}
	
	public static boolean setClusterGroup(){
		Logger.logln("Cluster group array retrieved from ClusterAnalyzer and brought to ClusterViewerDriver");
		if(clusterGroupReady){
			clusterGroupRay = ClusterAnalyzer.getClusterGroupArray();
			lenCGR = clusterGroupRay.length;
			return true;
		}
		else
			return false;
	}
	
	public static void initializeClusterViewer(GUIMain main, boolean showMessage){
		int i = 0;
		Logger.logln("Initializing ClusterViewer");
		int numPanels = ClusterViewer.allPanels.length;
		for(i=5; i< numPanels;i++){
			if(i== 19 || i == 20)
				continue;
			main.holderPanel.add(namePanels[i]);
			ClusterViewer.allPanels[i].setPreferredSize(new Dimension(800,50));
			main.holderPanel.add(ClusterViewer.allPanels[i]);
		}
		
		boolean cgIsSet = setClusterGroup();
		
		
		i=0;
		intRepresentation = new int[lenCGR][clusterGroupRay[0].getGroupKey().length()];
		stringRepresentation = new String[1+lenCGR];
		stringRepresentation[0] = "Select Targets";
		for(i=0; i< lenCGR; i++){
			intRepresentation[i] = clusterGroupRay[i].getGroupKey().toIntArray();
			stringRepresentation[i+1] = clusterGroupRay[i].getGroupKey().toString();
		}
		
		//ComboBoxModel clusterGroupChoices = new DefaultComboBoxModel(stringRepresentation);
		//main.clusterConfigurationBox.setModel(clusterGroupChoices);
		//main.mainJTabbedPane.setSelectedIndex(4);
		int[] theOne = intRepresentation[0];
		ClusterViewer.selectedClustersByFeature = theOne;
		lenJPanels = ClusterViewer.allPanels.length;
		for(i=0;i<lenJPanels;i++)
		{
			ClusterViewer.allPanels[i].revalidate();
			ClusterViewer.allPanels[i].repaint();
		}
			
		/*if(showMessage == true)
			JOptionPane.showMessageDialog(main, "The red dot is where each of your features are now.\nThe center of the " +
					"green oval is where they will be after you are done editing.\nAccept these targets if they all look reasonably " +
					"far away from the purple ovals. If not, get new green ovals.","Target Selection",JOptionPane.INFORMATION_MESSAGE,GUIMain.icon);
		*/
	}
		
	
	public static void initListeners(final GUIMain main){
		
		
		/*
		main.clusterConfigurationBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int selectedIndex = main.clusterConfigurationBox.getSelectedIndex();
				Logger.logln("Cluster Group number '"+(selectedIndex-1)+"' selected for VIEWING");
				int i = 0;
				if(selectedIndex != 0){
					int[] theOne = intRepresentation[selectedIndex-1];
					ClusterViewer.selectedClustersByFeature = theOne;
					lenJPanels = ClusterViewer.allPanels.length;
				}
				for(i=0;i<lenJPanels;i++)
					ClusterViewer.allPanels[i].repaint();
				
			}
			
		});	
		*/
//		main.selectClusterConfiguration.addActionListener(new ActionListener(){
//			
//			public void actionPerformed(ActionEvent e){
//				int selectedIndex = 1; //main.clusterConfigurationBox.getSelectedIndex();
//				if(selectedIndex == 0){
//					JOptionPane.showMessageDialog(main,"You must select a cluster group configuration before continuing","Select Targets!", JOptionPane.OK_OPTION);
//				}
//				else{
//					int answer = JOptionPane.showConfirmDialog(main, "Are you sure you would like to generate suggestions to move your\n" +
//							"document's features in the direction of the selectd clusters?","Confirm Choice",JOptionPane.YES_NO_OPTION);
//					if(answer ==0){
//						int trueIndex = selectedIndex -1;
//						Logger.logln("Cluster Group number '"+trueIndex+"' selected: "+stringRepresentation[selectedIndex]);
//						Logger.logln("Cluster Group chosen by Anonymouth: "+stringRepresentation[1]);
//						DataAnalyzer.selectedTargets = intRepresentation[trueIndex];
//						Logger.logln("INTREP: "+intRepresentation[trueIndex]);//added this.
//						EditorTabDriver.wizard.setSelectedTargets();
//						EditorTabDriver.signalTargetsSelected(main, true);
//						//main.mainJTabbedPane.getComponentAt(3).setEnabled(true);
//						//main.mainJTabbedPane.setSelectedIndex(3);
//					}
//				}
//				
//			}
//			
//		});
		
//		main.refreshButton.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				for(int i=0;i<lenJPanels;i++)
//					ClusterViewer.allPanels[i].repaint();
//				
//			}
//			
//		});
		
//		main.reClusterAllButton.addActionListener(new ActionListener(){
//			
//			public void actionPerformed(ActionEvent e){
//				int sureness = JOptionPane.showConfirmDialog(main, "Are you sure you want to re-cluster all features?");
//				if (sureness == 0){
//					Logger.logln("Re-cluster requested...");
//					int i =0;
//					JPanel[] firstThreePanels = new JPanel[3];
//					for(i=0;i<3;i++)
//						firstThreePanels[i] = (JPanel) main.holderPanel.getComponent(i);
//					main.holderPanel.removeAll();
//					for(i=0;i<3;i++)
//						main.holderPanel.add(firstThreePanels[i]);
//					int maxClusters =EditorTabDriver.wizard.runAllTopFeatures();
//					EditorTabDriver.wizard.runClusterAnalysis(maxClusters);
//					initializeClusterViewer(main,false);
//					int[] theOne = intRepresentation[0];
//					ClusterViewer.selectedClustersByFeature = theOne;
//					lenJPanels = ClusterViewer.allPanels.length;
//					for(i=0;i<lenJPanels;i++)
//						ClusterViewer.allPanels[i].repaint();
//					Logger.logln("Re-cluster complete.");
//				}
//			}
//			
//		});
		
	}

}
