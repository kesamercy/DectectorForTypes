package detectorPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DetectorClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String fileName = "C:\\Users\\nm293\\eclipse-workspace\\DetectDataTypes\\detectorPackage\\data2.csv";
		File file = new File(fileName);

		try {
			Scanner filecols = new Scanner(file);
			filecols.next(); // skip header

			// create a collection that will emulate the iterable string that will be passed
			ArrayList<String[]> iterableString = new ArrayList<String[]>();

			// populate the iterable string with values
			while (filecols.hasNext()) {

				String colString = filecols.next();
				String[] numcols = colString.split(",");
				iterableString.add(numcols);

			} // end while

			processData(iterableString);

			filecols.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// end main

	/*
	 * implementation for the background worker that kicks off determine the number
	 * of rows keep count for the times looped: when count for loop is is 1/3 of the
	 * no of rows, THEN break out of loop and guess the type.... store the type
	 * after the guess is done, use continue to proceed with the loop .... store the
	 * second type at EOF compare the types if they match, if yes, return type is
	 * consistent if they don't match, update the type alternatively, look at
	 * threads to implement this beahviour.....
	 */

	/*
	 * Method to add the datatype found to the respective hash map
	 * input: data type found
	 * input 2: Hash Map where type will be added
	 * return: none
	 */
	public static void addTypeToHashMap(Map<String, Integer> hashMapToAddType, String typefound) {

		if (hashMapToAddType.containsKey(typefound)) {
			// increase the count for type found already
			hashMapToAddType.put(typefound, hashMapToAddType.get(typefound) + 1);

		} // end if
		else {
			// add the new type found to the hash map
			hashMapToAddType.put(typefound, 1);

		} // end else

	}// end addToHashMap

	/*
	 * Method to find the respective data type that matches the string Respective
	 * types is found by comparing it with regular expressions till a most suitable
	 * match is found input: String return: type determined from the string
	 */
	public static String findType(String stringtomatch) {

		// declare expressions to match
		String integer = "([0-9]+)";
		String floatnum = "(\\d*\\.?\\d*)";
		String date = "(0?[1-9]|1[012])[- \\/.](0?[1-9]|[12][0-9]|3[01])[- \\/.](19|20)\\d\\d";
		String bool = "([Vv]+(erdade(iro)?)?|[Ff]+(als[eo])?|[Tt]+(rue)?|0|[\\+\\-]?1)";
		String timestamp = "([0-1][0-9]|[2][0-3]):([0-5][0-9])";
		String typeofString = "([a-zA-Z]+)";

		boolean matched = false;
		String stringtype = "String";

		// create array for the type to be checked
		String[] regextypes = { bool, timestamp, date, integer, floatnum, typeofString };

		// determine the type in the string
		for (int i = 0; i < regextypes.length; ++i) {

			// check if the string matches any regex
			matched = stringtomatch.matches(regextypes[i]);

			// break out of the loop if the type matches
			if (matched == true) {

				if (regextypes[i] == date) {
					stringtype = "date";
				}
				if (regextypes[i] == bool) {
					stringtype = "bool";
				}
				if (regextypes[i] == integer) {
					stringtype = "integer";
				}
				if (regextypes[i] == floatnum) {
					stringtype = "float";
				}
				if (regextypes[i] == timestamp) {
					stringtype = "time";
				}
				if (regextypes[i] == typeofString) {
					stringtype = "String";
				}

				return stringtype;

			} // end if

		} // end for

		return stringtype;

	}// end findType

	/*
	 * Method to process the Data when given an iterable String Process involves
	 * returning the dominant data types in eahc col input : Iterable String i.e
	 * Iterable<String[]> return: none
	 */
	public static void processData(ArrayList<String[]> iterableString) {

		int numElementsAccesed = 0;
		int postnInList = 0;
		String typefound = "none yet";
		int numcols = 0;
		List<Map<String, Integer>> listOfHashMapsForNumCols = new ArrayList<Map<String, Integer>>();

		// iterate through the iterable list created
		for (String[] i : iterableString) {

			numcols = i.length;

			for (String j : i) {

				typefound = findType(j);

				if (numElementsAccesed >= numcols) {

					addTypeToHashMap(listOfHashMapsForNumCols.get(postnInList), typefound);
					++postnInList;
					if (postnInList >= numcols) {
						postnInList = 0;
					}
				}
				else {

					Map<String, Integer> colDataTypes = new HashMap<String, Integer>();
					addTypeToHashMap(colDataTypes, typefound);
					// add the hash map to the list
					listOfHashMapsForNumCols.add(numElementsAccesed, colDataTypes);

				} // end else
				numElementsAccesed++;

			} // end for col

		} // end for row

		// print the elements in the list of col types
		for (int i = 0; i < numcols; i++) {
			System.out.println("For col " + i + listOfHashMapsForNumCols.get(i));
		}

	}// end processFile

}// end DetectorClass
