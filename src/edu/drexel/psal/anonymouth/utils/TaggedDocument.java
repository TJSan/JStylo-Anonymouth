package edu.drexel.psal.anonymouth.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jgaap.JGAAPConstants;

import edu.drexel.psal.anonymouth.gooie.ErrorHandler;
import edu.drexel.psal.anonymouth.projectDev.DataAnalyzer;
import edu.drexel.psal.jstylo.generics.Logger;
import edu.drexel.psal.jstylo.generics.Logger.LogOut;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.process.Tokenizer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.TreebankLanguagePack;

enum TENSE {PAST,PRESENT,FUTURE};

enum POV {FIRST_PERSON,SECOND_PERSON,THIRD_PERSON};

enum CONJ {SIMPLE,PROGRESSIVE,PERFECT,PERFECT_PROGRESSIVE};

/**
 * 
 * @author Andrew W.E. McDonald
 * @author Joe Muoio
 * 
 */
public class TaggedDocument {
	
	
	protected TaggedSentence currentLiveTaggedSentences;
	protected ArrayList<TaggedSentence> taggedSentences;
	//protected ArrayList<String> untaggedSentences;
	private static final Pattern EOS_chars = Pattern.compile("(([?!]+)|([.]){1})\\s*");
	
	protected String documentTitle = "None";
	protected String documentAuthor = "None";
	protected ArrayList<ArrayList<TENSE>> tenses;
	protected ArrayList<ArrayList<POV>> pointsOfView;
	protected ArrayList<ArrayList<CONJ>> conjugations;
	protected List<List<? extends HasWord>> sentencesPreTagging;
	protected Iterator<List<? extends HasWord>> preTagIterator;
	protected TreebankLanguagePack tlp = new PennTreebankLanguagePack(); 
	protected List<? extends HasWord> sentenceTokenized;
	protected Tokenizer<? extends HasWord> toke;
	protected final int PROBABLE_NUM_SENTENCES = 50;
	protected SentenceTools jigsaw;
	protected Iterator<String> strIter;
	private static int sentNumber = -1;
	private String ID; 
	private int totalSentences=0;

	/**
	 * Constructor for TaggedDocument
	 */
	public TaggedDocument(){
		jigsaw = new SentenceTools();
		taggedSentences = new ArrayList<TaggedSentence>(PROBABLE_NUM_SENTENCES);
		//currentLiveTaggedSentences = new ArrayList<TaggedSentence>(5); // Most people probably won't try to edit more than 5 sentences at a time.... if they do... they'll just have to wait for the array to grow.
	}
	
	/**
	 * Constructor for TaggedDocument, accepts an untagged string, and calls addSentences method.
	 * @param untaggedDocument
	 */
	public TaggedDocument(String untaggedDocument){
		jigsaw = new SentenceTools();
		taggedSentences = new ArrayList<TaggedSentence>(PROBABLE_NUM_SENTENCES);
		//currentLiveTaggedSentences = new ArrayList<TaggedSentence>(5); 
		makeAndTagSentences(untaggedDocument, true);
		//consolidateFeatures(taggedSentences);
		//setHashMaps();
		//setWordsToAddRemove();
	}
	 
	/**
	 * 
	 * @param untaggedDocument
	 * @param docTitle
	 * @param author
	 */
	public TaggedDocument(String untaggedDocument, String docTitle, String author){
		this.documentTitle = docTitle;
		this.documentAuthor = author;
		this.ID = documentTitle+"_"+documentAuthor;
		//Logger.logln("TaggedDocument ID: "+ID);
	//	currentLiveTaggedSentences = new ArrayList<TaggedSentence>(5); 
		jigsaw = new SentenceTools();
		taggedSentences = new ArrayList<TaggedSentence>(PROBABLE_NUM_SENTENCES);
		makeAndTagSentences(untaggedDocument, true);
		//consolidateFeatures(taggedSentences);
		//setHashMaps();
		//setWordsToAddRemove();
		//Logger.logln("Top 100 wordsToRemove: "+wordsToRemove.toString());
	}
	
	/**
	 * returns the number of Words in the TaggedDocument
	 * @return
	 */
	public int getWordCount(){
		int wordCount = 0;
		for(TaggedSentence ts:taggedSentences){
			wordCount += ts.size();
		}
		return wordCount;
	}
	
	/**
	 * returns all Words in the TaggedDocument
	 * @return
	 */
	public ArrayList<Word> getWords(){
		int numWords = getWordCount();
		ArrayList<Word> theWords = new ArrayList<Word>(numWords);
		for(TaggedSentence ts: taggedSentences){
			theWords.addAll(ts.wordsInSentence);
		}
		return theWords;
	}
	
	/**
	 * consolidates features for an ArrayList of TaggedSentences (does both word level and sentence level features)
	 * @param alts
	 */
	public void consolidateFeatures(ArrayList<TaggedSentence> alts){
		
		for(TaggedSentence ts:alts){
			ConsolidationStation.featurePacker(ts);
		}
	}
		
	
	/**
	 * consolidates features for a single TaggedSentence object
	 * @param ts
	 */
	public void consolidateFeatures(TaggedSentence ts){
		ConsolidationStation.featurePacker(ts);
	}
		
	
	/**
	 * Takes a String of sentences (can be an entire document), breaks it up into individual sentences (sentence tokens), breaks those up into tokens, and then tags them (via MaxentTagger).
	 * Each tagged sentence is saved into a TaggedSentence object, along with its untagged counterpart.
	 * @param untagged String containing sentences to tag
	 * @param appendTaggedSentencesToGlobalArrayList if true, appends the TaggedSentence objects to the TaggedDocument's arraylist of TaggedSentences
	 * @return the TaggedSentences
	 */
	public ArrayList<TaggedSentence> makeAndTagSentences(String untagged, boolean appendTaggedSentencesToGlobalArrayList){
		ArrayList<String> untaggedSent = jigsaw.makeSentenceTokens(untagged);
		ArrayList<TaggedSentence> taggedSentences = new ArrayList<TaggedSentence>(untaggedSent.size());
		//sentencesPreTagging = new ArrayList<List<? extends HasWord>>();
		strIter = untaggedSent.iterator();
		String tempSent;
		while(strIter.hasNext()){
			tempSent = strIter.next();
			TaggedSentence taggedSentence = new TaggedSentence(tempSent);
			toke = tlp.getTokenizerFactory().getTokenizer(new StringReader(tempSent));
			sentenceTokenized = toke.tokenize();
			taggedSentence.setTaggedSentence(Tagger.mt.tagSentence(sentenceTokenized));
			consolidateFeatures(taggedSentence);
			taggedSentences.add(taggedSentence); 
			
		}
		if(appendTaggedSentencesToGlobalArrayList == true){
			int i = 0;
			int len = taggedSentences.size();
			for(i=0;i<len;i++){
				totalSentences++;
				this.taggedSentences.add(taggedSentences.get(i)); 
			}
		}
		
		return taggedSentences;
	}
	
	/**
	 * returns the ArrayList of TaggedSentences
	 * @return
	 */
	public ArrayList<TaggedSentence> getTaggedDocument(){
		return taggedSentences;
	}
		

	/**
	 * returns the untagged sentences of the TaggedDocument
	 * @return
	 */
	public ArrayList<String> getUntaggedSentences()
	{
		ArrayList<String> sentences = new ArrayList<String>();
		for (int i=0;i<taggedSentences.size();i++)
			sentences.add(taggedSentences.get(i).getUntagged());
		return sentences;
	}
	
	
	/**
	 * Gets the next sentence and alters the sentNumber to match
	 * @return TaggedSentence - The next sentence
	 */
	public TaggedSentence getNextSentence()
	{
		if(sentNumber < totalSentences-1)
		{
			sentNumber++;
			return taggedSentences.get(sentNumber);
		}
		else
		{
			sentNumber = totalSentences-1;
			return taggedSentences.get(sentNumber);
		}
	}
	
	/**
	 * Gets the current sentence and alters the sentNumber to match
	 * @return TaggedSentence - The current sentence
	 */
	public TaggedSentence getCurrentSentence()
	{
		return taggedSentences.get(sentNumber);
	}
	
	/**
	 * Gets the previous sentence and alters the sentNumber to match
	 * @return TaggedSentence - The previous sentence
	 */
	public TaggedSentence getPrevSentence()
	{
		if(sentNumber > 0)
		{
			sentNumber--;
			return taggedSentences.get(sentNumber);
		}
		else
		{
			Logger.logln("Returned first sentence");
			sentNumber=0;
			return taggedSentences.get(0);
		}
	}
	
	/**
	 * adds the next sentence to the current one.
	 * @param The sentenceEditBox text
	 * @return the concatenation of the current sentence and the next sentence.
	 */
	public String addNextSentence(String boxText) {
		if(sentNumber <totalSentences-1 && sentNumber>=0){
			//have to add the next sentence to this one otherwise the appended sentence will not be taken into calculations.
			totalSentences--;
			ArrayList<TaggedSentence> tempTaggedSentences=new ArrayList<TaggedSentence>(2);
			tempTaggedSentences.add(taggedSentences.get(sentNumber));
			tempTaggedSentences.add(taggedSentences.get(sentNumber+1));
			currentLiveTaggedSentences=concatSentences(tempTaggedSentences);
			
			TaggedSentence newSent= new TaggedSentence(boxText);
			int position=0;
			while(position<boxText.length()){
				Matcher sent = EOS_chars.matcher(boxText);
				if(!sent.find(position)){//checks to see if there is a lack of an end of sentence character.
					Logger.logln("User tried submitting an incomplete sentence.");//THIS DOES NOT KEEP TAGS. 
					//--------------------This is because you cannot pass in incomplete sent to parser
					TaggedSentence tagSentNext=removeTaggedSentence(sentNumber+1);
					removeTaggedSentence(sentNumber);
					newSent.untagged=newSent.getUntagged()+tagSentNext.getUntagged();
					addTaggedSentence(newSent,sentNumber);//--------possible improvement needed to parser?-----
					//ErrorHandler.incompleteSentence();
					//for(int i=0;i<currentLiveTaggedSentences.size();i++)
					
					Logger.logln(currentLiveTaggedSentences.untagged);
					updateReferences(currentLiveTaggedSentences,newSent);
					
					return newSent.getUntagged();
				}
				position=sent.end();
			}
			ArrayList<TaggedSentence> taggedSents=makeAndTagSentences(boxText,false);
			TaggedSentence nextSent=taggedSentences.remove(sentNumber+1);
			taggedSents.add(nextSent);
			taggedSentences.remove(sentNumber);
			newSent=concatSentences(taggedSents);
			taggedSentences.add(sentNumber, newSent);
			
			updateReferences(currentLiveTaggedSentences,newSent);
			
			//for(int i=0;i<currentLiveTaggedSentences.size();i++)
			Logger.logln(currentLiveTaggedSentences.untagged);
			return newSent.getUntagged();
		}
		if(sentNumber<0){
			sentNumber=0;
		}
		//currentLiveTaggedSentences=taggedSentences.get(sentNumber);
		//for(int i=0;i<currentLiveTaggedSentences.size();i++)
		Logger.logln(currentLiveTaggedSentences.untagged);
		return taggedSentences.get(sentNumber).getUntagged();
		
	}
	/**
	 * updates the referenced Attributes 'toModifyValue's (present value) with the amount that must be added/subtracted from each respective value 
	 * @param oldSentence The pre-editing version of the sentence(s)
	 * @param newSentence The post-editing version of the sentence(s)
	 */
	private void updateReferences(TaggedSentence oldSentence, TaggedSentence newSentence){
		Logger.logln("Old Sentence: "+oldSentence.toString()+"\nNew Sentence: "+newSentence.toString());
		SparseReferences updatedValues = newSentence.getOldToNewDeltas(oldSentence);
		Logger.logln(updatedValues.toString());
		for(Reference ref:updatedValues.references){
			//Logger.logln("Attribute: "+DataAnalyzer.topAttributes[ref.index].getFullName()+" pre-update value: "+DataAnalyzer.topAttributes[ref.index].getToModifyValue());
			if(DataAnalyzer.topAttributes[ref.index].getFullName().contains("Percentage")){
				//then it is a percentage.
				Logger.logln("Attribute: "+DataAnalyzer.topAttributes[ref.index].getFullName()+"Is a percentage! ERROR!",Logger.LogOut.STDERR);
			}
			else if(DataAnalyzer.topAttributes[ref.index].getFullName().contains("Percentage")){
				//then it is an average
				Logger.logln("Attribute: "+DataAnalyzer.topAttributes[ref.index].getFullName()+"Is an average!ERROR!",Logger.LogOut.STDERR);
			}
			else{
				DataAnalyzer.topAttributes[ref.index].setToModifyValue((DataAnalyzer.topAttributes[ref.index].getToModifyValue() + ref.value));
				//Logger.logln("Updated attribute: "+DataAnalyzer.topAttributes[ref.index].getFullName());
			}
				
			//Logger.logln("Attribute: "+DataAnalyzer.topAttributes[ref.index].getFullName()+" post-update value: "+DataAnalyzer.topAttributes[ref.index].getToModifyValue());
		}
	}
	
	
	
	/**
	 * accepts a list of TaggedSentences and returns a single TaggedSentence, preserving all original Word objects
	 * @param taggedList takes a list of tagged sentences.
	 * @return returns a single tagged sentences with the properties of all the sentences in the list.
	 */
	private TaggedSentence concatSentences(ArrayList<TaggedSentence> taggedList){
		TaggedSentence toReturn =new TaggedSentence(taggedList.get(0));
		int taggedListSize = taggedList.size();
		int i, j;
		for (i=1;i<taggedListSize;i++){
				toReturn.wordsInSentence.addAll(taggedList.get(i).wordsInSentence);
				toReturn.untagged += taggedList.get(i).untagged;
		}
		return toReturn;
	}
	
	
	public int getSentNumber(){
		return sentNumber;
	}
	
	/**
	 * returns the size of the ArrayList holding the TaggedSentences (i.e. the number of sentences in the document)
	 * @return
	 */
	public int getNumSentences(){
		return taggedSentences.size();
	}
	
		
	public static void setSentenceCounter(int sentNumber){//is this needed?
		TaggedDocument.sentNumber = sentNumber;
	}

	public void addTaggedSentence(TaggedSentence sentToAdd, int placeToAdd){
		taggedSentences.add(placeToAdd,sentToAdd);
		
	}
	
	public TaggedSentence removeTaggedSentence(int indexToRemove){
		return taggedSentences.remove(indexToRemove);
	}

	
	/**
	 * 
	 * @param sentsToAdd a String representing the sentence(s) from the editBox
	 * @return 1 if everything worked as expected. 0 if user deleted a sentence. -1 if user submitted an incomplete sentence
	 */
	public int removeAndReplace(String sentsToAdd){//, int indexToRemove, int placeToAdd){
		if(sentsToAdd.matches("\\s*")){//checks to see if the user deleted the current sentence
			currentLiveTaggedSentences=taggedSentences.get(sentNumber);
			//CALL COMPARE
			removeTaggedSentence(sentNumber);
			Logger.logln("User deleted a sentence.");
			updateReferences(currentLiveTaggedSentences,new TaggedSentence(""));//all features must be deleted
			totalSentences--;
			sentNumber--;
			return 0;
		}
		int position=0;
		while(position<sentsToAdd.length()){
			Matcher sent = EOS_chars.matcher(sentsToAdd);
			if(!sent.find(position)){//checks to see if there is a lack of an end of sentence character.
				Logger.logln("User tried submitting an incomplete sentence.");
				currentLiveTaggedSentences=taggedSentences.get(sentNumber);
				TaggedSentence newSent= new TaggedSentence(sentsToAdd);
				removeTaggedSentence(sentNumber);
				addTaggedSentence(newSent,sentNumber);
				
				updateReferences(currentLiveTaggedSentences,newSent);
				//call compare with newSent to currentLiveTaggedSentences
				ErrorHandler.incompleteSentence();
				return -1;
			}
			position=sent.end();
		}
		ArrayList<TaggedSentence> taggedSentsToAdd = makeAndTagSentences(sentsToAdd,false);
		currentLiveTaggedSentences=taggedSentences.get(sentNumber);
		removeTaggedSentence(sentNumber);
		addTaggedSentence(taggedSentsToAdd.get(0),sentNumber);
		
		//call compare
		int i, len = taggedSentsToAdd.size();
		for(i=1;i<len;i++){
			sentNumber++;
			//removeTaggedSentence(sentNumber);
			addTaggedSentence(taggedSentsToAdd.get(i),sentNumber);
			totalSentences++;
		}
		updateReferences(currentLiveTaggedSentences,concatSentences(taggedSentsToAdd));
		return 1;
		
	}
	
	public ArrayList<TaggedSentence> getTaggedSentences(){
		return taggedSentences;
	}
	
	
	public String getUntaggedDocument(){
		String str = "";
		for (int i=0;i<totalSentences;i++){
			str+=taggedSentences.get(i).getUntagged();
		}
		return str;
	}
	
	
	public String toString(){
		String toReturn = "Document Title: "+documentTitle+" Author: "+documentAuthor+"\n";
		int len = taggedSentences.size();
		int i =0;
		for(i=0;i<len;i++){
			toReturn += taggedSentences.get(i).toString()+"\n";
		}
		return toReturn;
	}
	
	
	public static void main(String[] args){
		String text1 = "people's enjoy coffee, especially in the mornings, because it helps to wake me up. My dog is fairly small, but she seems not to realize it when she is around bigger dogs. This is my third testing sentence. I hope this works well.";
		TaggedDocument testDoc = new TaggedDocument(text1);
		System.out.println(testDoc.toString());			
		//System.out.println(testDoc.getFunctionWords());
		
	}

	public String getCurrentLiveTaggedSentence() {
		// TODO Auto-generated method stub
		return currentLiveTaggedSentences.untagged;
	}
	
}
	
