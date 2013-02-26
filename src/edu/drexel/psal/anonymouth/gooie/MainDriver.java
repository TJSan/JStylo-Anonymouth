package edu.drexel.psal.anonymouth.gooie;

import java.awt.Color;
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

public class MainDriver 
{
	
	public static void findCluster(GUIMain main, String name)
	{
		for (int i = 0; i < main.holderPanel.getComponentCount(); i = i + 2)
		{
			JPanel panel = (JPanel)main.holderPanel.getComponent(i);
			JLabel label = (JLabel)panel.getComponent(0);
			String labelName = label.getText();
			if (name.equals(labelName))
			{
				main.clusterScrollPane.getVerticalScrollBar().setValue((i/2)*74); // 70 + 4 extra pixels for the borders
				break;
			}
		}
	}
	
	/**
	 * Initialize all main listeners.
	 */
	protected static void initListeners(final GUIMain main) 
	{
		// feature set buttons
		// ===================
		
		// feature set combo box
		main.prepAdvButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				main.PPSP.openWindow();
			}
		});
		
		main.featuresBox.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				int index = main.featuresBox.getSelectedIndex();
				main.subFeaturesBoxModel = new DefaultComboBoxModel(main.subfeatures.get(index).toArray());
				main.subFeaturesBox.setModel(main.subFeaturesBoxModel);
				if (main.subfeatures.get(index).isEmpty())
				{
					main.subFeaturesBox.setEnabled(false);
					findCluster(main, (String)main.featuresBox.getSelectedItem());
				}
				else
					main.subFeaturesBox.setEnabled(true);
			}
		});
		
		main.subFeaturesBox.addItemListener(new ItemListener()
		{
			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				findCluster(main, (String)main.featuresBox.getSelectedItem() + "--" + (String)main.subFeaturesBox.getSelectedItem());
			}
		});
		
//		main.prepDocLabel.addMouseListener(new MouseListener()
//		{
//			@Override
//			public void mouseClicked(MouseEvent e) 
//			{
//				
//			}
//			
//			@Override
//			public void mouseEntered(MouseEvent e) 
//			{
//				main.prepDocLabel.setBorder(BorderFactory.createLoweredBevelBorder());
//				main.prepDocLabel.setBackground(Color.YELLOW);
//			}
//			
//			@Override
//			public void mouseExited(MouseEvent e) 
//			{
//				main.prepDocLabel.setBorder(BorderFactory.createRaisedBevelBorder());
//				if (main.documentsAreReady())
//					main.prepDocLabel.setBackground(main.ready);
//				else
//					main.prepDocLabel.setBackground(main.notReady);
//			}
//			
//			@Override
//			public void mouseReleased(MouseEvent e) 
//			{
//			}
//			
//			@Override
//			public void mousePressed(MouseEvent e) 
//			{
//				if (main.docPPIsShowing)
//					main.docPPIsShowing = false;
//				else
//					main.docPPIsShowing = true;
//				
//				main.saveProblemSetJButton.setVisible(main.docPPIsShowing);
//				main.loadProblemSetJButton.setVisible(main.docPPIsShowing);
//				main.mainLabel.setVisible(main.docPPIsShowing);
//				main.sampleLabel.setVisible(main.docPPIsShowing);
//				main.prepMainDocScrollPane.setVisible(main.docPPIsShowing);
//				main.prepSampleDocsScrollPane.setVisible(main.docPPIsShowing);
//				main.addTestDocJButton.setVisible(main.docPPIsShowing);
//				main.removeTestDocJButton.setVisible(main.docPPIsShowing);
//				main.adduserSampleDocJButton.setVisible(main.docPPIsShowing);
//				main.removeuserSampleDocJButton.setVisible(main.docPPIsShowing);
//				main.trainLabel.setVisible(main.docPPIsShowing);
//				main.trainCorpusJTreeScrollPane.setVisible(main.docPPIsShowing);
//				main.addTrainDocsJButton.setVisible(main.docPPIsShowing);
//				main.removeTrainDocsJButton.setVisible(main.docPPIsShowing);
//			}
//		});
	}
}