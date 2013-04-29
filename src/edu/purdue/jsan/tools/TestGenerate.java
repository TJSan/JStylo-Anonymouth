package edu.purdue.jsan.tools;

import java.io.File;

public class TestGenerate {
	public static void main(String args[]){
		
		int a[]=new int[9];
		
		for(int i=0; i<a.length;i++){
			a[i]=i+1;
		}
		int count=0;
		for(int i=0; i<a.length-3;i++){
			for(int j=i+1; j<a.length-2;j++){
				for(int k=j+1; k<a.length-1;k++){
					for(int l=k+1; l<a.length;l++){
						System.out.println(a[i]+" "+a[j]+" "+a[k]+" "+a[l]);
						count++;
					}
				}		
			}
		}
		
		System.out.println("Total:"+count);
		
		String text="/Users/Salta/Documents/workspace/JStylo-Anonymouth/jsan_resources/corpora/amt/d/d_01_1.txt         ";
		System.out.println(text.length());
		text=text.replace(" ","");
		System.out.println(text.length());
		File f=new File(text);
		System.out.println(f.isFile());
		
	}

}
