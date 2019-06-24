package detectorPackage;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.color.*;
import java.io.File;
import java.io.FileNotFoundException;

public class TestingMethods {
	
	
	public   TestingMethods() throws FileNotFoundException {
		// TODO Auto-generated constructor stubb
		
		String fileName = "C:\\Users\\nm293\\git\\DectectorForTypes\\TypeDetector\\datatypeDetector\\datatypeDetectorPackage\\data2.csv";
		File file = new File(fileName);
		
		Scanner filecols = new Scanner(file);
		String cols =  filecols.next();
		String [] getcols = cols.split(",");
		
		//creating a collection aj
		ArrayList<String[]> aj = new ArrayList<String[]>();
			
			//add elements from the csv to the collection
			while (filecols.hasNext()) {
				
				String colString =  filecols.next();
				String [] numcols = colString.split(",");
				aj.add(numcols);
				
			}//end while 
			
			//iterate through the iterable list created 
			for (String[] i : aj) {
				
				for (String j : i) {
					
					//create a hash table for each col
					
					//store the types in each col as they are read
					
					//finally, determine the types returned from each hash table in the respective col
					
					System.out.println(j);
					
				}
				
			}
		
		filecols.close();
		
		
	}
	
	
	public static void main(String args[]) throws FileNotFoundException {
		
		TestingMethods a = new TestingMethods();
		
	}


}
