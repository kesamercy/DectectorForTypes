package detectorPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TestingMethods {

	//test java enum classes here


	public   TestingMethods() throws FileNotFoundException {
		// TODO Auto-generated constructor stubb

		String fileName = "C:\\Users\\nm293\\eclipse-workspace\\DetectDataTypes\\detectorPackage\\data2.csv";
		File file = new File(fileName);

		Scanner filecols = new Scanner(file);
		String cols =  filecols.next();
		cols.split(",");

		//creating a collection to the iterable string aj
		ArrayList<String[]> aj = new ArrayList<String[]>();

		//create an array of hash maps
		List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();


		//add elements from the csv to the collection
		while (filecols.hasNext()) {

			String colString =  filecols.next();
			String [] numcols = colString.split(",");
			aj.add(numcols);

		}//end while
		int m = 0;
		int postnInList = 0;
		String typefound;

		//iterate through the iterable list created
		for (String[] i : aj) {

			for (String j : i) {

				Map<String, Integer> map = new HashMap<String, Integer>();
				//System.out.println("the values for j " + j + m);

				typefound = findType(j);

				if (m >= 3) {

					if (list.get(postnInList).containsKey(typefound)) {

						//increase the count for type found already
						list.get(postnInList).put(typefound, list.get(postnInList).get(typefound) + 1);

					}
					else {
						//add the type found to the hash map
						list.get(postnInList).put(typefound, 1);

					}//end else

					++postnInList;

					if (postnInList >= 3) {

						//reset the position in list to 0
						postnInList = 0;

					}
				}
				else {

					if(map.containsKey(typefound)) {
						//increase the count for type found already
						map.put(typefound, map.get(typefound) + 1);

					}//end if
					else {
						//add the type found to the hash map
						map.put(typefound, 1);

					}//end else

					list.add(m, map);

				}
				m++;



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
				 * 4. write a function for the get method so that you are just calling it since the
				 * implementation is the same
				 *
				 * FIRST THING TO DO IS THIS BELOW!!! READ ABOUT THE LISTS
				 * read about lists in java and they will give you a clue how to access the maps stored
				 * in the list currently
				 *
				 * leaving off here!
					- supposed to figure out how to get the list to have indivual maps == to the
				 *
				 *
				 * transfer implementation
				 * 1. make it work in detect implementation
				 * 2. put it in another method and call it
				 * 3. clean up the impelementation if you can
				 *
				 *
				 */


			}

		}

		for (int i = 0; i < 3; i++) {

			System.out.println("For col "+ i + list.get(i));

		}







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

		new TestingMethods();

	}


}
