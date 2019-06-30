package detectorPackage;

import java.awt.FontFormatException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.IOP.TaggedComponentHelper;

public class DetectorClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		
		String fileName = "C:\\Users\\nm293\\eclipse-workspace\\DetectDataTypes\\detectorPackage\\data2.csv";
		File file = new File(fileName);
		
		processFile(file); 
	}//end main
	
	
	
public static void processFile(File filename) {
		
		String typefound = "none yet";
		String [] colvalues = {};
		
		Map<String, Integer> datatypes = new HashMap<>();

		try {
			
			//determine the number of cols in the file 
			Scanner filecols = new Scanner(filename );
			String colString =  filecols.next();
			String [] numcols = colString.split(",");

			for (int i = 0; i < numcols.length; i++) {
				
				Scanner inputStream = new Scanner(filename );
				inputStream.next(); // skip the header
				
				while (inputStream.hasNext()) {
					String data = inputStream.next(); // returns the row or the whole line

					colvalues = data.split(","); // gets the col values in each row 
					
					typefound = findType(colvalues[i]);
					
					
					if(datatypes.containsKey(typefound)) {
						 //increase the count for type found already
						datatypes.put(typefound, datatypes.get(typefound) + 1);
						
					}//end if 
					else {
						//add the type found to the hash map
						datatypes.put(typefound, 1);
						
					}//end else 
					
					//determine the number of rows
					
					//keep count for the times looped:
					
					//when count for loop is  is 1/3 of the no of rows, break out of loop and guess the type.... store the type 
						
				} // end while
				
				//after the guess is done, use continue to proceed with the loop .... store the second type at EOF
				
				//compare the types if they match, if yes, return type is consistent
				
				//if they don't match, update the type 
				
				
				//alternatively, look at threads to implement this beahviour.....
				
				
				//determine the highest type from the hash table 
				  int highesttypenum = 0;
				  String dominantType = "none yet";
				  
				  for(String datatype : datatypes.keySet()) { 
					  Integer thetype = datatypes.get(datatype);
					  if(thetype > highesttypenum) { 
						  highesttypenum = thetype;
						  dominantType = datatype; }
				  }
				  
				  System.out.println();
				  System.out.printf("the dominant type is '%s', %d times", dominantType, highesttypenum);
				  
				  System.out.println();
				  System.out.println("type for col  " + i + datatypes);
				  
				  //remove the current data in the hash map
				  datatypes.clear();
				  
				  //close the file 
				  inputStream.close();
				
			}// end for 
			
			//close the file for finding the number of cols 
			filecols.close();
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // end catch
		
		
	}// end processFile
	
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

}//end DetectorClass
