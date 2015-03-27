package uppg2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.TreeMap;
import java.util.TreeSet;

//Author(s): Linda Karlsson, Linn Leiulfsrud
//Email: kalinda@student.chalmers.se & linnl@student.chalmers.se
//Date: 26/3 2015

public class WordLists {
	private Reader in = null;
	private TreeMap<String, Integer> wordMap;
	private TreeMap<String, Integer> wordMap2;
	private TreeMap<Integer, TreeSet<String>> freqMap;
	private TreeMap<Integer, TreeSet<String>> freqMap2;
	private TreeSet<String> reverseSet;
	private TreeMap<String, String> reverseMap;
	private StringComparator stringComp;
	private StringComparatorDescending stringCompDesc;
	private StringComparatorDescending stringCompDesc2;
	private String fileName;

	public WordLists(String inputFileName) {
		
	    try {
			in = new BufferedReader(new FileReader(inputFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	   
	    fileName = inputFileName;
	    /*try {
			in.mark(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		wordMap = new TreeMap<String, Integer>();
		wordMap2 = new TreeMap<String, Integer>();
		freqMap = new TreeMap<Integer, TreeSet<String>>(stringCompDesc);
		freqMap2 = new TreeMap<Integer, TreeSet<String>>(stringCompDesc2);
		
		reverseSet = new TreeSet<String>();
		reverseMap = new TreeMap<String, String>(stringComp);
	}
	
	private boolean isPunctuationChar(char c) {
	    final String punctChars = ",.:;?!";
	    return punctChars.indexOf(c) != -1;
	}
	
	private String getWord() throws IOException {
		int state = 0;
		int i;
		String word = "";
		while ( true ) {
			i = in.read();
			char c = (char)i;
			switch ( state ) {
			case 0:
				if ( i == -1 )
					return null;
				if ( Character.isLetter( c ) ) {
					word += Character.toLowerCase( c );
					state = 1;
				}
				break;
			case 1:
				if ( i == -1 || isPunctuationChar( c ) || Character.isWhitespace( c ) )
					return word;
				else if ( Character.isLetter( c ) ) 
					word += Character.toLowerCase( c );
				else {
					word = "";
					state = 0;
				}
			}
		}
	}
	
	private String reverse(String s) {
	    String reversed = "";
	    char[] wordArray = s.toCharArray();
	    char[] reversedArray = new char[wordArray.length];
	    int x;
	    for(x = 0; x < wordArray.length ; x++){
	    	reversedArray[wordArray.length - x - 1] = wordArray[x];
	    }
	    
	    //String hej2 = new String(wordArray);
	    reversed = new String(reversedArray);
	    //System.out.println("" + hej2);
	    //System.out.println("" + hej3);
		return reversed;
	}
	
	private void computeWordFrequencies() {
		
		String word = "";
		try{
			while( (word = getWord()) != null){
				if (wordMap.containsKey(word) == true ){
					int value;
					value = wordMap.get(word) + 1 ;
					wordMap.put(word, value);
				}
				else{	//Första gången ett ord dyker upp
					wordMap.put(word, 1);
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		//System.out.println("Java: " + wordMap.get("java") );
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("alfaSorted.txt"));
			for(String s : wordMap.keySet()){
				out.write(s + "      ");
				out.write(wordMap.get(s).toString());
				out.write("\n");
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//HÄR SKRIVER VI
	private void computeFrequencyMap() {
		

		try {
			in = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		
		
		String word = "";
		try{
			while( (word = getWord()) != null){

				//****************
				//wordMap = new TreeMap<String, Integer>();
				//freqMap = new TreeMap<Integer, TreeSet<String>>(stringCompDesc);
				//****************

				if (wordMap2.containsKey(word) == true ){
					int value;
					value = wordMap2.get(word) + 1 ;
					wordMap2.put(word, value);
				}
				else{	//Första gången ett ord dyker upp
					wordMap2.put(word, 1);
				}

				for(String s : wordMap2.keySet()){
					System.out.println("" + s);
				}
				
				for(String s : wordMap2.keySet()){
					TreeSet<String> treeSet = new TreeSet<String>();
					
					if(freqMap2.get(wordMap2.get(word)) != null)
						treeSet = freqMap2.get(wordMap2.get(word));
					
					treeSet.add(word);
					freqMap2.put(wordMap2.get(word), treeSet);
					
					//System.out.println("" + s);
					//System.out.println("" + wordMap2.get(word));
				}
				/*
				for(Integer i : wordMap.values()){
					freqMap2.get(i).add(word);
					freqMap2.put(i, freqMap2.get(i) );
				}*/
				
				/*
				if (freqMap.values().toString().contains(word) == true ){
					//System.out.println("HEJ IF!");
					
					for(Integer i :  freqMap.keySet() ){
						TreeSet<String> treeSet = new TreeSet<String>();
						treeSet = freqMap.get(i);
						
							for(String s : treeSet){
								
								if(s.equals(word)){
									sumInt = Integer.sum(i.intValue(), 1);
									//System.out.println("" + sumInt);
									//Integer newValue = new Integer(sumInt);
									
									Integer newValue = new Integer(i + 1);
									
									TreeSet<String> treeSet2 = new TreeSet<String>();
									if(freqMap.get(newValue) != null)
										treeSet2 = freqMap.get(newValue);
									treeSet2.add(word);
									freqMap.put(newValue, treeSet2);
									
									//System.out.println("Summa: " + newValue);
									
									//freqMap.get(i).remove(word);
									//treeSet.remove(word);
									freqMap.put(i, treeSet);
								}
							}
							freqMap.get(i).remove(word);	
					}
				}
				else{	//Första gången ett ord dyker upp
					//System.out.println("HEJ ELSE!");
					Integer i = new Integer(1);
					TreeSet<String> treeSet = new TreeSet<String>();
					
					if(freqMap.get(i) != null)
						treeSet = freqMap.get(i);
					
					treeSet.add(word);
					freqMap.put(1, treeSet);
					//freqMap.put(1, freqMap.get(i).add(word));
				}*/
				
				
			}
			
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		
		
		//System.out.println("Java: " + freqMap.get(1).toString() );
		try {
			TreeSet<String> emptySet = new TreeSet<String>();
			BufferedWriter out = new BufferedWriter(new FileWriter("frequencySorted.txt"));
			for(Integer number : freqMap2.keySet()){
				if( !(freqMap2.get(number).equals(emptySet)) ){
					out.write(number + ":\n");
					out.write("" + freqMap2.get(number).toString());
					out.write("\n");
				}
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//***********************************************************************************
	private void computeBackwardsOrder() {
		
		try {
			in = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		}
		
		
		String word = "";
		String rword = "";
		try{
			while( (word = getWord()) != null){
				rword = reverse(word);
				reverseMap.put(rword, word);
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("backwardsSorted.txt"));
			for(String s : reverseMap.keySet()){
				out.write(reverseMap.get(s));
				out.write("\n");
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	


	public static void main(String[] args) throws IOException {
		//WordLists wl = new WordLists(args[0]);  // arg[0] contains the input file name
		WordLists wl = new WordLists("provtext.txt");
		wl.computeWordFrequencies();
		wl.computeFrequencyMap();
		wl.computeBackwardsOrder();
		
		System.out.println("Finished!");
	}
	
	public class StringComparator implements Comparator<String> {
		public int compare(String s1,String s2) {
			return s1.compareTo(s2);
		}
	} 
	
	public class StringComparatorDescending implements Comparator<Integer> {
		public int compare(Integer s1,Integer s2) {
			if(s1.compareTo(s2) > 0)
				return -1;
			else if(s1.compareTo(s2) < 0)
				return 1;
			else
				return 0;
		}
	} 
	
}



















