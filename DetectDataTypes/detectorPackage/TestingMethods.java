package detectorPackage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.awt.color.*;
import java.io.File;
import java.io.FileNotFoundException;

public class TestingMethods {
	
	
	public   TestingMethods() throws FileNotFoundException {
		// TODO Auto-generated constructor stubb
		
		String fileName = "C:\\Users\\nm293\\eclipse-workspace\\DetectDataTypes\\detectorPackage\\data2.csv";
		File file = new File(fileName);
		
		Scanner filecols = new Scanner(file);
		String cols =  filecols.next();
		String [] getcols = cols.split(",");
		
		//creating a collection aj
		ArrayList<String[]> aj = new ArrayList<String[]>();
		
		//create the hash table
		Map<String, Integer> datatypes = new HashMap<>();
		
		//create an array of hash maps 
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		 
		
		   // Map<String, String> map = new HashMap<String, String>();
		    Map<String, String> dap = new HashMap<String, String>();
		 
		/*
		 * map.put("empid", "hello"); map.put("ename", "let's be friends");
		 * map.put("email", "shall we"); map.put("dob", "I care about me");
		 */
		    
		    //if the count is equal to the num of col, reset the count to 0 to start refilling the list 
		    
		    dap.put("empid", "drink");
		    dap.put("ename", "water");
		    dap.put("email", "now");
		 
		    
		
		//System.out.println(map);
		
		 
		
			//add elements from the csv to the collection
			while (filecols.hasNext()) {
				
				String colString =  filecols.next();
				String [] numcols = colString.split(",");
				aj.add(numcols);
				
			}//end while 
			int m = 0;
			
			//iterate through the iterable list created 
			for (String[] i : aj) {
				
				for (String j : i) {
					Map<String, String> map = new HashMap<String, String>();
					System.out.println("the values for j " + j + m);
					map.put("data", j);
					list.add(m, map);
					
					//System.out.println("did we get here " + list.get(0));
					m++;
					
					if (m == 3) {
						//reset m
						m = 0;
					}
					
					/*
					 * supposed to have each list index represent a hasha map of the number of cols
					 * current implementation is close beacause we have the maps being created each time
					 * 
					 * have the implementation from detector class to store the count for the types in the 
					 * hash maps that way you only have 3
					 * 
					 * next goal:
					 * 1. have the hash maps only create 3 times in the list 
					 * 2. rest of them they are supposed to be increasing the types in the maps already
					 * created 
					 * 3. figure out the hash map implementation.... so close!!!!
					 * 
					 * FIRST THING TO DO IS THIS BELOW!!! READ ABOUT THE LISTS
					 * read about lists in java and they will give you a clue how to access the maps stored
					 * in the list currently
					 * 
					 * leaving off here!
					- supposed to figure out how to get the list to have indivual maps == to the 
					 * 
					 * 
					*/
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					
					//create an array of hash tables based on the number of cols 
					
					//then create a for loop based on the number of cols that populate the hash table with ttypes found
					
					//when you iterate to the last col of the table reset the number of the hash tbale to the first hash table 
					//in the array to be accessed 
					
					//do this till the end of file 
					
					
					
					
					
					
					//create a hash table for each col
					
					//start with col 0
					
					String typefound = findType(j);
					if(datatypes.containsKey(typefound)) {
						 //increase the count for type found already
						datatypes.put(typefound, datatypes.get(typefound) + 1);
						
					}//end if 
					else {
						//add the type found to the hash map
						datatypes.put(typefound, 1);
						
					}//end else 
					
					//store the types in each col as they are read
					
					//finally, determine the types returned from each hash table in the respective col
					
					//System.out.println(j);
					
				}
				
			}
			
		/*
		 * for (int i = 0; i < 27; i++) {
		 * 
		 * System.out.println("did we get here " + list.get(i));
		 * 
		 * }
		 */
			
			System.out.println("did we get here " + list.get(1));
			
			
			
			
		
		filecols.close();
		
		
	}
	
	public static String findType(String stringtomatch) {
		 
		//declare expressions to match
		String integer = "([0-9]+)";
		String floatnum = "(\\d*\\.?\\d*)";
		String date = "(0?[1-9]|1[012])[- \\/.](0?[1-9]|[12][0-9]|3[01])[- \\/.](19|20)\\d\\d";
		String bool = "([Vv]+(erdade(iro)?)?|[Ff]+(als[eo])?|[Tt]+(rue)?|0|[\\+\\-]?1)";
		String timestamp = "([0-1][0-9]|[2][0-3]):([0-5][0-9])";
		String typeofString = "([a-zA-Z]+)";
		
		boolean matched = false;
		String stringtype = "String";
		
		//create array for the type to be checked 
		String[] regextypes = { bool, timestamp, date, integer, floatnum, typeofString};
		
		//determine the type in the string 
		for(int i = 0; i < regextypes.length; ++i) {
			
			//check if the string matches any regex
			matched = stringtomatch.matches(regextypes[i]);

			//break out of the loop if the type matches 
			if(matched == true) {

				if(regextypes[i] == date) {
					 stringtype = "date";
				}
				if(regextypes[i] == bool) {
					 stringtype = "bool";
				}
				if(regextypes[i] == integer) {
					 stringtype = "integer";
				}
				if(regextypes[i] == floatnum) {	
					 stringtype = "float";
				}
				if(regextypes[i] == timestamp) {	
					 stringtype = "time";
				}
				if(regextypes[i] == typeofString) {	
					 stringtype = "String";
				}
				
				return stringtype;
				
			}//end if 
			
		}//end for 
		
		return stringtype;
	 
	 }//end findType 
	
	
	public static void main(String args[]) throws FileNotFoundException {
		
		TestingMethods a = new TestingMethods();
		
	}


}
