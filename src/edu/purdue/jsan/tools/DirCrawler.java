package edu.purdue.jsan.tools;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DirCrawler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] folderNames={"Salta","Lauren","JiHeyon","Amy"};

		File directory = new File("jsan_resources/corpora/amt");
		boolean isDirectory = directory.isDirectory();
		if (isDirectory) {
			String names[] = directory.list();
			for (String file : names) {
				if (file.length() < 14)
					System.out.printf("%s\n", file);
			}
			XMLGenerator xmlG = new XMLGenerator();
			int total=0;
			for(int count=0;count<12;count++){
			for (int i = 5; i < 10; i += 5) {
				List<String> sample = pickNRandom(Arrays.asList(names), i);
				String[] chosen = new String[i];
				for (int j = 0; j < chosen.length && sample.size() > 0; j++) {
					chosen[j] = sample.remove(0);
				}
				xmlG.generateXML(chosen, folderNames[(total/3)], i);
				total++;

			}
			}
			System.out.println("the name you have entered is a directory  : "
					+ directory);
			System.out.println("the path is " + directory.getAbsolutePath());

		} else {
			// It returns false if directory is a file.
			System.out.println("the name you have entered is a file  : "
					+ directory);
			// It returns the absolute path of a file.
			System.out.println("the path is " + directory.getAbsolutePath());
		}
		// TODO Auto-generated method stub

	}

	public static List<String> pickNRandom(List<String> lst, int n) {
		List<String> copy = new LinkedList<String>(lst);
		Collections.shuffle(copy);
		return copy.subList(0, n);
	}
}
