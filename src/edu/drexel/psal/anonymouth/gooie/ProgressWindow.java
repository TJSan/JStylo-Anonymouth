package edu.drexel.psal.anonymouth.gooie;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.Date;
import com.memetix.mst.language.Language;
import java.util.ArrayList;

import javax.swing.*;
//import edu.drexel.psal.anonymouth.gooie.EditorTabDriver;

import edu.drexel.psal.anonymouth.gooie.Translation;
import edu.drexel.psal.anonymouth.utils.TaggedSentence;

public class ProgressWindow extends JDialog
{
	private JProgressBar progressBar;
	private JLabel progressLabel;
	private GUIMain main;
	
	public ProgressWindow(String title, GUIMain main)
	{
		super(main, title, Dialog.ModalityType.MODELESS); // MODELESS lets it stay on top, but not block any processes
		init(main);
	}
	
	private void init(GUIMain main)
	{
		this.main = main;
		
		GridBagLayout layout = new GridBagLayout();
		this.setLayout(layout);
		GridBagConstraints constraints = new GridBagConstraints();
		
		progressLabel = new JLabel();
		progressLabel.setHorizontalAlignment(SwingConstants.CENTER);
		progressLabel.setPreferredSize(new java.awt.Dimension(300, 50));
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		this.add(progressLabel, constraints);
		
		progressBar = new JProgressBar();
		progressBar.setPreferredSize(new java.awt.Dimension(300, 25));
		progressBar.setIndeterminate(true);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		this.add(progressBar, constraints);
		
		this.pack(); // makes it fit around the components
		this.setLocationRelativeTo(null); // makes it form in the center of the screen
		this.setVisible(true);
	}

	public void setText(String text)
	{
		progressLabel.setText(text);
	}
	
	public JProgressBar getProgressBar()
	{
		return progressBar;
	}
	
	public void closeWindow() 
	{
		//main.setEnabled(true);
        WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(wev);
	}
}