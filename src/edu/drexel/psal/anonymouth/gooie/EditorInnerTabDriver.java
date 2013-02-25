package edu.drexel.psal.anonymouth.gooie;

import edu.drexel.psal.anonymouth.utils.ConsolidationStation;
import edu.drexel.psal.jstylo.generics.CumulativeFeatureDriver;
import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.jstylo.generics.ProblemSet;
import edu.drexel.psal.jstylo.generics.WekaInstancesBuilder;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import weka.classifiers.Classifier;
import edu.drexel.psal.jstylo.analyzers.WekaAnalyzer;


/**
 * Creates new inner panel for editor tab, broken up into three parts: get information from last clicked pane, create pane/set values, set main objects
 * @author Andrew W.E. McDonald
 *
 */
public class EditorInnerTabDriver {
	
			protected JPanel editBoxPanel;
			protected JScrollPane editBox;
			protected JTextPane editorBox;
			protected JScrollPane resultsTablePane;
			protected JTable resultsTable;
			protected JLabel classificationLabel;
			protected JLabel resultsTableLabel;
			private boolean tabMade = false;
	
	
			public EditorInnerTabDriver(){
				
			}

			public EditorInnerTabDriver makeTab(){
				Logger.logln("Making new EditorInnerDriver tab");
				if(tabMade == false){
				editBoxPanel = new JPanel();
				GroupLayout editBoxPanelLayout = new GroupLayout((JComponent)editBoxPanel);
				editBoxPanel.setLayout(editBoxPanelLayout);
				editBoxPanel.setPreferredSize(new java.awt.Dimension(785, 563));
				{
					editBox = new JScrollPane();
					{
						editorBox = new JTextPane();
						editBox.setViewportView(editorBox);
						editorBox.setText("Your document will be here.");
						editorBox.setEditable(true);
					}
				}
				{
					resultsTablePane = new JScrollPane();
					{
						TableModel resultsTableModel = 
								new DefaultTableModel(
										new String[][] { { "One", "Two" }, { "Three", "Four" } },
										new String[] { "Column 1", "Column 2" });
						resultsTable = new JTable();
						resultsTablePane.setViewportView(resultsTable);
						resultsTable.setModel(resultsTableModel);
					}
				}
				{
					classificationLabel = new JLabel();
					classificationLabel.setText("Your document has been classified as...");
				}
				{
					resultsTableLabel = new JLabel();
					resultsTableLabel.setText("Results of This Document's Classification (% probability of authorship per author)");
				}
			editBoxPanelLayout.setHorizontalGroup(editBoxPanelLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(editBoxPanelLayout.createParallelGroup()
					    .addComponent(editBox, GroupLayout.Alignment.LEADING, 0, 768, Short.MAX_VALUE)
					    .addComponent(resultsTablePane, GroupLayout.Alignment.LEADING, 0, 768, Short.MAX_VALUE)
					    .addGroup(editBoxPanelLayout.createSequentialGroup()
					        .addGap(127)
					        .addGroup(editBoxPanelLayout.createParallelGroup()
					            .addGroup(GroupLayout.Alignment.LEADING, editBoxPanelLayout.createSequentialGroup()
					                .addComponent(resultsTableLabel, GroupLayout.PREFERRED_SIZE, 518, GroupLayout.PREFERRED_SIZE)
					                .addGap(0, 123, Short.MAX_VALUE))
					            .addGroup(GroupLayout.Alignment.LEADING, editBoxPanelLayout.createSequentialGroup()
					                .addGap(64)
					                .addComponent(classificationLabel, 0, 577, Short.MAX_VALUE)))))
					.addContainerGap());
			editBoxPanelLayout.setVerticalGroup(editBoxPanelLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(editBox, GroupLayout.PREFERRED_SIZE, 414, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(resultsTableLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
					.addComponent(resultsTablePane, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(classificationLabel, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(16, Short.MAX_VALUE));
				}
				tabMade = true;
				return this;
			}
			

}

