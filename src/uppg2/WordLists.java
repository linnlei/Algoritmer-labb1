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
	private TreeMap<String, String> reverseMap;
	private String fileName;

	public WordLists(String inputFileName) throws FileNotFoundException {
		
		in = new BufferedReader(new FileReader(inputFileName));
	    fileName = inputFileName;
	    
		wordMap = new TreeMap<String, Integer>();
		wordMap2 = new TreeMap<String, Integer>();
		freqMap = new TreeMap<Integer, TreeSet<String>>(new StringComparatorDescending());
		reverseMap = new TreeMap<String, String>(new StringComparator());
		
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
	    
	    reversed = new String(reversedArray);
		return reversed;
	}
	
	
	private void computeWordFrequencies() {
		String word = "";
		try{
			while( (word = getWord()) != null){	
				if (wordMap.containsKey(word) == true ){	//Om ordet redan finns i mapen
					int value;
					value = wordMap.get(word) + 1 ;			//Hämta ordets frekvens, lägg till 1
					wordMap.put(word, value);				//Lägg in uppdaterade värdet i mapen
				}
				else{										//Första gången ett ord dyker upp
					wordMap.put(word, 1);					//Lägg till ordet i mapen och sätt ordets frekvens till 1
				}
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		try {												//För att skriva till textfil
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
	
	
	private void computeFrequencyMap() throws FileNotFoundException {
		
		in = new BufferedReader(new FileReader(fileName));	//Starta om från början på textfilen
		
		String word = "";
		try{
			while( (word = getWord()) != null){
				
				if (wordMap2.containsKey(word) == true ){	//Om ordet redan finns i mapen
					int value;
					value = wordMap2.get(word) + 1 ;		//Hämta antal gånger ordet räknats hittills, lägg till 1
					wordMap2.put(word, value);				//Lägg in uppdaterade värdet i mapen
				}
				else{										//Första gången ett ord dyker upp
					wordMap2.put(word, 1);
				}
				
			}
			
			for(String s : wordMap2.keySet()){	//Använder wordMap som innehåller orden+frekvenser, för att skapa frekvensmapen
				TreeSet<String> treeSet = new TreeSet<String>();
				
				if(freqMap.get(wordMap2.get(s)) != null)	//Om det redan finns ord under en frekvens
					treeSet = freqMap.get(wordMap2.get(s));	//Så hämta treeSet'et med de orden
				
				treeSet.add(s);								//Lägg till ordet i det nya/hämtade treeSet
				freqMap.put(wordMap2.get(s), treeSet);		//Lägg in treeSet'et under rätt frekvens i frekvensmapen
			}
			
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		
		
		try {												//För att skriva till textfil
			TreeSet<String> emptySet = new TreeSet<String>();
			BufferedWriter out = new BufferedWriter(new FileWriter("frequencySorted.txt"));
			for(Integer number : freqMap.keySet()){
				if( !(freqMap.get(number).equals(emptySet)) ){
					out.write(number + ":\n");
					out.write("" + freqMap.get(number).toString());
					out.write("\n");
				}
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private void computeBackwardsOrder() throws FileNotFoundException {
		
		in = new BufferedReader(new FileReader(fileName));	//Starta om från början på textfilen
		
		String word = "";
		String rword = "";
		
		try{
			while( (word = getWord()) != null){
				rword = reverse(word);				//Vänder på ordet genom reverse-metoden
				reverseMap.put(rword, word);		//Lägger in bakvända ordet samt ordet
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}

		try {										//För att skriva till textfil
			BufferedWriter out = new BufferedWriter(new FileWriter("backwardsSorted.txt"));
			for(String s : reverseMap.keySet()){	//Går igenom mapen, som är sorterad efter de bakvända orden
				out.write(reverseMap.get(s));		//Skriver ut värdena i mapen, dvs de 'rättvända' orden
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
		public int compare(Integer s1,Integer s2) {	//Ger omvänd sortering, dvs returnerar omvänt vad "vanlig" komparator gör
			if(s1.compareTo(s2) > 0)	
				return -1;
			else if(s1.compareTo(s2) < 0)
				return 1;
			else
				return 0;
		}
	} 
	
}

