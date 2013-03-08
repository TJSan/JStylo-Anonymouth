/*
 * JGAAP -- a graphical program for stylometric authorship attribution
 * Copyright (C) 2009,2011 by Patrick Juola
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 **/
package com.jgaap.generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.jgaap.languages.English;

/**
 * Code for storing and processing individual documents of any type.
 */
public class Document extends Parameterizable {

	private String author;
	private String filepath;
	private String title;
	private char[] text;
	private int size;
	private DocType docType;
	private Language language;
	private List<Canonicizer> canonicizers;
	private Map<EventDriver, EventSet> eventSets;
	private Map<AnalysisDriver, Map<EventDriver, List<Pair<String, Double>>>> results;
	private boolean failed = false;
	
	public Document() {
		filepath = "";
		title = "";
		size = 0;
		canonicizers = new ArrayList<Canonicizer>();
		eventSets = new HashMap<EventDriver, EventSet>();
		results = new HashMap<AnalysisDriver, Map<EventDriver, List<Pair<String, Double>>>>();
		docType = DocType.GENERIC;
		this.language = new English();
	}

	public Document(String filepath, String author) throws Exception {
		this(filepath, author, getTitleFromPath(filepath));
	}

	/**
	 * Copy constructor. Can be used to break object references and protect a
	 * Document instance from being modified by other classes.
	 * 
	 * @param document
	 *            The document to be copied
	 */
	public Document(Document document) {
		if (document.text == null){
			try {
				document.load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.author = document.author;
		this.canonicizers = new ArrayList<Canonicizer>(document.canonicizers);
		this.docType = document.docType;
		this.eventSets = new HashMap<EventDriver, EventSet>(document.eventSets);
		this.results = new HashMap<AnalysisDriver, Map<EventDriver, List<Pair<String, Double>>>>(
				document.results);
		this.filepath = document.filepath;
		this.text = Arrays.copyOf(document.text, document.text.length);
		this.size = document.size;
		this.title = document.title;
		this.language = document.getLanguage();
	}

	/**
	 * Constructor that takes three arguments: file path, file author, file
	 * title
	 * 
	 * @param filepath
	 *            The path to the file
	 * @param author
	 *            The author of the document
	 * @param title
	 *            The title of the document
	 * @throws Exception
	 */
	public Document(String filepath, String author, String title){
		this.author = author;
		if (author != null && author.equals("")) // unknown authors are null
			this.author = null;
		this.filepath = filepath;
		this.title = title;
		if (title == null || title.equals(""))
			this.title = getTitleFromPath(filepath);
		this.docType = DocumentHelper.getDocType(filepath);
		this.language = new English();
		this.eventSets = new HashMap<EventDriver, EventSet>();
		this.canonicizers = new ArrayList<Canonicizer>();
		this.results = new HashMap<AnalysisDriver, Map<EventDriver, List<Pair<String, Double>>>>();
	}
	
	public void load() throws Exception{
		this.text = DocumentHelper.loadDocument(filepath, language.getCharset());
		this.size = this.text.length;
		if (this.size == 0) {
			throw new Exception("Empty Document Error");
		}
	}

	/**
	 * Takes a file path and returns only the file name.
	 * 
	 * @param filePath
	 *            the full path to the file
	 * @return A document title derived from the file path.
	 */
	private static String getTitleFromPath(String filePath) {
		String[] split = filePath.split("[\\\\[\\/]]");
		return split[split.length - 1];
	}

	public void readStringText(String text) {
		this.text = text.toCharArray();
		size = this.text.length;
	}
	
	public void setText(char[] text){
		this.text = text;
		size = text.length;
	}

	public void print() {
		System.out.println(stringify());
	}

	/** Retrieves the author of the current document **/
	public String getAuthor() {
		return author;
	}

	/** Returns the docType of the current document **/
	public DocType getDocType() {
		return docType;
	}

	/** Returns the full filepath of the current document **/
	public String getFilePath() {
		return filepath;
	}

	/**
	 * Returns text with preprocessing done. Preprocessing can include stripping
	 * whitespace or normalizing the case
	 **/
	public char[] getProcessedText() {
			return Arrays.copyOf(text, text.length);
	}

	/**
	 * Returns the size of the document. Size is determined by the number of
	 * characters plus whitespace
	 **/
	public int getSize() {
		return size;
	}

	/** Returns the title of the current document **/
	public String getTitle() {
		return title;
	}

	/** Sets the author of the current document **/
	public void setAuthor(String author) {
		this.author = author;
	}

	/** Sets the docType of the current document **/
	public void setDocType(DocType docType) {
		this.docType = docType;
	}

	/** Sets the title of the current document **/
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Clear the list of canonicizers associated with this Document.
	 */
	public void clearCanonicizers() {
		canonicizers.clear();
	}

	/**
	 * Add a Canonicizer to the internal list maintained by this Document.
	 * 
	 * @param canonicizer
	 *            A new canonicizer to add to the list
	 */
	public void addCanonicizer(Canonicizer canonicizer) {
		canonicizers.add(canonicizer);
	}

	/**
	 * Remove a Canonicizer from the internal list maintained by this Document.
	 * 
	 * @param canonicizer
	 *            A canonicizer to remove from the list
	 * @return Returns true if a matching Canonicizer was found and removed
	 */
	public boolean removeCanonicizer(Canonicizer canonicizer) {
		return canonicizers.remove(canonicizer);
	}

	public boolean removeCanonicizer(String action) {
		for (Canonicizer canonicizer : canonicizers) {
			if (canonicizer.displayName().equalsIgnoreCase(action)) {
				return canonicizers.remove(canonicizer);
			}
		}
		return false;
	}

	/**
	 * Get all the canonicizers associated with this Document.
	 * 
	 * return A vector of canonicizers associated with this document
	 */
	public List<Canonicizer> getCanonicizers() {
		return new ArrayList<Canonicizer>(canonicizers);
	}

	/**
	 * Take the list of canonicizers associated with this document and apply
	 * them to the document one by one, in the same order they were added.
	 */
	public void processCanonicizers() throws LanguageParsingException, CanonicizationException {
		if (language.isParseable()){
			text = language.parseLanguage(stringify());
		}
		for (Canonicizer canonicizer : canonicizers) {
			text = canonicizer.process(text);
		}
	}

	public void addEventSet(EventDriver eventDriver, EventSet eventSet) {
		eventSet.setAuthor(author);
		eventSet.setDocumentName(getFilePath());
		eventSets.put(eventDriver, eventSet);
	}

	public Map<EventDriver, EventSet> getEventSets() {
		return eventSets;
	}

	public EventSet getEventSet(EventDriver eventDriver) {
		return eventSets.get(eventDriver);
	}

	public void clearEventSets() {
		eventSets.clear();
	}

	public void addResult(AnalysisDriver analysisDriver,
			EventDriver eventDriver, List<Pair<String, Double>> list) {
		Map<EventDriver, List<Pair<String, Double>>> entry;
		if (results.containsKey(analysisDriver)) {
			entry = results.get(analysisDriver);
		} else {
			entry = new HashMap<EventDriver, List<Pair<String, Double>>>();
			results.put(analysisDriver, entry);
		}
		entry.put(eventDriver, list);
	}

	public String getResult() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(getTitle() + " ");
		buffer.append(getFilePath() + "\n");
		buffer.append("Canonicizers: ");
		if (canonicizers.isEmpty()) {
			buffer.append("none");
		} else {
			for (Canonicizer canonicizer : canonicizers) {
				buffer.append(canonicizer.displayName() + " ");
			}
		}
		buffer.append("\n");
		for (Entry<AnalysisDriver, Map<EventDriver, List<Pair<String, Double>>>> analysisEntry : results.entrySet()) {
			String analysis = analysisEntry.getKey().displayName();
			for (Entry<EventDriver, List<Pair<String, Double>>> eventEntry : analysisEntry.getValue().entrySet()) {
				EventDriver eventDriver = eventEntry.getKey(); 
				buffer.append("Analyzed by " + analysis + " using "
						+ eventDriver.displayName() + " as events\n");
				int count = 0; // Keeps a relative count (adjusted for ties)
				int fullCount = 0; // Keeps the absolute count (does not count ties)
				Double lastResult = Double.NaN;
				for (Pair<String, Double> result : eventEntry.getValue()) {
					fullCount++;

					// Account for ties
					if(!result.getSecond().equals(lastResult)) {
						count = fullCount;
					} 
					
					lastResult = result.getSecond();
					
					buffer.append(count + ". " + result.getFirst() + " "
							+ result.getSecond() + "\n");
				}
				buffer.append("\n\n");
			}
		}
		return buffer.toString();
	}

	public Map<AnalysisDriver, Map<EventDriver, List<Pair<String, Double>>>> getResults() {
		return results;
	}

	public void clearResults() {
		results.clear();
	}

	/**
	 * Indicates whether this document has a known author or not.
	 * 
	 * @return boolean value indicating whether the author of this document is
	 *         known
	 */
	public boolean isAuthorKnown() {
		return (author != null);
	}

	/**
	 * Convert processed document into one really long string.
	 **/
	public String stringify() {
		return new String(text);
	}

	@Override
	public String toString() {
		String t;
		t = "Title:  " + title + "\n";
		t += "Path:   " + filepath + "\n";
		t += "Author: " + author + "\n";
		t += "Canons: " + getCanonicizers() + "\n";
		return t;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
	
	public void failed(){
		failed = true;
	}
	
	public boolean hasFailed(){
		return failed;
	}
}
