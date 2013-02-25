package edu.drexel.psal.anonymouth.utils;

import java.util.ArrayList;
import java.util.Scanner;

import edu.drexel.psal.anonymouth.gooie.DictionaryBinding;
import edu.drexel.psal.anonymouth.gooie.GUIMain;
import edu.drexel.psal.jstylo.generics.Logger;

public class SynonymReplaceTest {

	
	/*
	 * This function is for the study of replacing words with their highest ranked synonyms.
	 */
	public static void replaceWords(GUIMain main){
		String currentSent = main.sentenceEditPane.getText();
		String newSentence=" ";
		TaggedSentence taggedSent=ConsolidationStation.toModifyTaggedDocs.get(0).taggedSentences.get(ConsolidationStation.toModifyTaggedDocs.get(0).getSentNumber());
		ArrayList<Word> words=taggedSent.getWordsInSentence();
		//Scanner parser=new Scanner(currentSent);
		String currentWord, synonyms[];
		Word currentSynonym,topSynonym;
		//while(parser.hasNext()){//loops through every word in the sentence
		for(Word w:words){
			//currentWord=parser.next();
			currentWord=w.getUntagged();
			synonyms=DictionaryBinding.getSynonyms(currentWord,w.partOfSpeech.get(0));
			topSynonym=new Word(currentWord);
			//Double d=topSynonym.getAnonymityIndex();
			ConsolidationStation.setWordFeatures(topSynonym);
			for(String s:synonyms){//loops through every synonym
				currentSynonym=new Word(s);
				ConsolidationStation.setWordFeatures(currentSynonym);
				if(topSynonym.getAnonymityIndex()<currentSynonym.getAnonymityIndex()){
					topSynonym=currentSynonym;
					Logger.logln("Replaced the TopSynonym!"+topSynonym.getUntagged());
				}
			}
			if(topSynonym.getUntagged()!=""){//Then replace the word with this
				newSentence+=topSynonym.getUntagged()+ " ";
			}
			else{
				newSentence+=currentWord+" ";
			}
		}
		Logger.logln(newSentence,Logger.LogOut.STDERR);
		main.sentenceEditPane.setText(newSentence);
		
	}
}
