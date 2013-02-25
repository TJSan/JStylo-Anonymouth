package edu.drexel.psal.anonymouth.gooie;

import edu.drexel.psal.JSANConstants;
import edu.drexel.psal.jstylo.generics.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent.AboutEvent;
import com.apple.eawt.Application;

/**
 * ThePresident sets up the Application and System fields/preferences prior to calling 'GUIMain'
 * @author Andrew W.E. McDonald
 *
 */
public class ThePresident {

	//protected static ImageIcon buffImg;
	public static ImageIcon LOGO;
	public static String sessionName;
	public static String TEMP_DIR =  "temp/"; // TODO: put in "options"
	public static String GRAMMAR_DIR = "grammar_data/";//TODO: put in "options"
	//public static boolean SHOULD_KEEP_TEMP_CLEAN_DOCS = false; // TODO : put in "options" XXX not used!!
	public static boolean SHOULD_KEEP_AUTO_SAVED_ANONYMIZED_DOCS = true; // TODO: put in "options"
	public static boolean SAVE_TAGGED_DOCUMENTS = true; // TODO: put in "options

	/*
	public void getDockImage(String name){
		try{
			buffImg = new ImageIcon(getClass().getResource(name));
		} catch (Exception e){
			e.printStackTrace();
			//System.exit(5);
		}
	}
	*/

	public void getLogo(String name){
		try{
			LOGO = new ImageIcon(getClass().getResource(name), "Anonymouth's Logo");
		} catch (Exception e){
			e.printStackTrace();
			//System.exit(6);
		}
	}


	public static void main(String[] args){
		String OS = System.getProperty("os.name").toLowerCase();
		if(OS.contains("mac")){
			Logger.logln("We're on a Mac!");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name","Anonymouth");
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			Application app = Application.getApplication();
			ThePresident leader = new ThePresident();
			String logoName = JSANConstants.JSAN_GRAPHICS_PREFIX+"Anonymouth_LOGO.png";
			try{
				leader.getLogo(logoName);
				app.setDockIconImage(LOGO.getImage());
			}catch(Exception e){
				Logger.logln("Error loading logos");
			}
			/*
			JMenuBar menuBar = new JMenuBar();
			int numMenus = 3;
			JMenu[] menu = new JMenu[numMenus];
			menu[0] = new JMenu();
			menu[0].setText("File");
			menu[1] = new JMenu();
			menu[1].setText("Edit");
			menu[2] = new JMenu();
			menu[2].setText("Help");
			int i;
			for(i=0;i<numMenus;i++)
				menuBar.add(menu[i]);
			app.setDefaultMenuBar(menuBar);
			 */
			app.setAboutHandler(new AboutHandler(){
				public void handleAbout(AboutEvent e){
					JOptionPane.showMessageDialog(null, 
							"Anonymouth\nVersion 0.0.3\nAuthor: Andrew W.E. McDonald\nDrexel University, PSAL, Dr. Rachel Greenstadt - P.I.",
							"About Anonymouth",
							JOptionPane.INFORMATION_MESSAGE,
							LOGO);

				}
			});

			app.requestForeground(true);
		}
		sessionName = "anonymous"; 
//		String tempName = null;
//		tempName = JOptionPane.showInputDialog("Please name your session:", sessionName);
//		if(tempName == null)
//			System.exit(665);
//			
//		tempName = tempName.replaceAll("['.?!()<>#\\\\/|\\[\\]{}*\":;`~&^%$@+=,]", "");
//		tempName = tempName.replaceAll(" ", "_");
//		if(tempName != null)
//			sessionName = tempName;
		//System.out.println(tempName+" "+sessionName);
		Logger.setFilePrefix("Anonymouth_"+sessionName);
		Logger.logFile = true;	
		Logger.initLogFile();
		Logger.logln("Gooie starting...");
		GUIMain.startGooie();
	}
}
