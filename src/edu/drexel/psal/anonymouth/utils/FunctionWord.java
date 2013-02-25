package edu.drexel.psal.anonymouth.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.anonymouth.utils.Trie;

/**
 * 
 * @author Joe Muoio
 * Keeps track of the list of function words and implements the Trie to search them.
 * 
 */

public class FunctionWord implements Runnable {
	private final int fWordArrSize=486;//make this larger if more words are added
	protected String[] functionWordArray=new String[fWordArrSize];
	private ArrayList<String> functionWordList;
	private static String filePath="src/edu/drexel/psal/resources/koppel_function_words.txt";
	private Trie node;

	public FunctionWord() {
		
	}
	
	public void run(){
		functionWordList=readFunctionWords();
		for(int i=0;i<fWordArrSize;i++){
			functionWordArray[i]=functionWordList.get(i);
		}
		node = new Trie();
		node.addWords(functionWordArray);
		Logger.logln("FINISHED INITIALIZING FUNCTION WORDS",Logger.LogOut.STDERR);
		System.out.println("TEST CASE: "+this.searchListFor("The"));
			
		//System.exit(0);
	}
	public boolean searchListFor(String str){
		return node.find(str);
	}
	
	public String getWordAt(int index){
		return functionWordArray[index];
	}
	
	
	private static ArrayList<String> readFunctionWords(){
		ArrayList<String> functionWords=new ArrayList<String>();
		
		 try {
			BufferedReader readIn  = new BufferedReader(new FileReader(filePath));
			String newLine;
			try {
				while((newLine=readIn.readLine())!=null){
					if(newLine.length()>1){
						functionWords.add(newLine);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Logger.logln("Error opening reader: "+e.getMessage());
		}
		
		return functionWords;
	}
	
	public static void main(String[] args){//times the execution of the search on the list of function words.
		
		FunctionWord fWord=new FunctionWord();
		fWord.run();
		String findStr;
		Random randomGen = new Random(); 
		int num;
		long startTime;
		long endTime;	
		startTime = System.currentTimeMillis();
		for(int i=0;i<fWord.functionWordList.size();i++){
			num=Math.abs(randomGen.nextInt()%fWord.functionWordList.size());
			findStr=fWord.functionWordList.get(num);			
			System.out.println(fWord.searchListFor(findStr));
			fWord.searchListFor(findStr);
		}
		endTime = System.currentTimeMillis();
		System.out.println("Trie Test Time: " + (endTime-startTime));//strList.size());		
		
		findStr="ain't";
		System.out.println(fWord.searchListFor(findStr));
		
	}
}
