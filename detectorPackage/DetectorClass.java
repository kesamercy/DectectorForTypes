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
	 * public static void runThreads() {
	 *
	 * int athirdOfNumRows = (1 / 3); int firstBatch = numrows / athirdOfNumRows;
	 *
	 * Thread t1 = new Thread(() -> {
	 *
	 * while (elementsAccessed != firstBatch) {
	 *
	 * ++firstBatch; try { Thread.sleep(1000); } catch (Exception e) { }
	 *
	 * } // end while });
	 *
	 * Thread t2 = new Thread(() -> {
	 *
	 * while (elementsAccessed >= firstBatch) { // first thread
	 *
	 * try { Thread.sleep(1000); } catch (Exception e) { } } // end while
	 * ++firstBatch; });
	 *
	 * t1.start(); try { Thread.sleep(10); } catch (Exception e) { } t2.start();
	 *
	 * }// end runThreads
	 */
	/*
	 * Method to add the datatype found to the respective hash map input: data type
	 * found input 2: Hash Map where type will be added return: none
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

		List<Map<String, Integer>> listOfHashMapsForNumCols = new ArrayList<Map<String, Integer>>();
		List<Map<String, Integer>> listOfValidatingHashMaps = new ArrayList<Map<String, Integer>>();
		List<Map<String, Integer>> listOfHighestTypeInCols = new ArrayList<Map<String, Integer>>();
		List<Map<String, Integer>> listHighestTypeInCols = new ArrayList<Map<String, Integer>>();

		Thread t1 = new Thread(() -> {

			int numElementsAccesed = 0;
			int postnInList = 0;
			String typefound = "none yet";

			iterableString.size();
			int numcols = 0;

			// iterate through the iterable list
			for (String[] i : iterableString) {

				numcols = i.length;

				//while (numElementsAccesed != firstBatch) {

				for (String j : i) {

					typefound = findType(j);

					if (numElementsAccesed >= numcols) {

						addTypeToHashMap(listOfHashMapsForNumCols.get(postnInList), typefound);
						++postnInList;
						if (postnInList >= numcols) {
							postnInList = 0;
						}
					} else {

						Map<String, Integer> colDataTypes = new HashMap<String, Integer>();
						addTypeToHashMap(colDataTypes, typefound);
						// add the hash map to the list
						listOfHashMapsForNumCols.add(numElementsAccesed, colDataTypes);

					} // end else

					numElementsAccesed++;

				} // end for col

				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}

				//} // end while

			} // end for row

			// print the elements in the list of col types
			for (int i = 0; i < numcols; i++) {
				System.out.println("For col " + i + listOfHashMapsForNumCols.get(i));
			}

			// return the highest type for each list index
			int highesttypenumber = 0;
			String dominantTypes = "none yet";

			for (int i = 0; i < listOfValidatingHashMaps.size(); i++) {

				Map<String, Integer> highestTypeInEachCol = new HashMap<String, Integer>();

				for (String datatype : listOfHashMapsForNumCols.get(i).keySet()) {
					Integer thetype = listOfHashMapsForNumCols.get(i).get(datatype);

					if (thetype > highesttypenumber) {
						highesttypenumber = thetype;
						dominantTypes = datatype;
					}

					addTypeToHashMap(highestTypeInEachCol, dominantTypes);
				}
				listOfHighestTypeInCols.add(i, highestTypeInEachCol);
			}

		}); // end the first thread

		Thread t2 = new Thread(() -> {

			int elementsAccesed = 0;
			int postnInList = 0;
			String typefound = "none yet";

			iterableString.size();
			int numbcols = 0;
			// iterate through the iterable list
			for (String[] i : iterableString) {

				numbcols = i.length;

				//while (count >= firstBatch) {

				for (String j : i) {

					typefound = findType(j);

					if (elementsAccesed >= numbcols) {

						addTypeToHashMap(listOfValidatingHashMaps.get(postnInList), typefound);
						++postnInList;
						if (postnInList >= numbcols) {
							postnInList = 0;
						}
					} else {

						Map<String, Integer> validateGuessedTypes = new HashMap<String, Integer>();
						addTypeToHashMap(validateGuessedTypes, typefound);
						// add the hash map to the list
						listOfValidatingHashMaps.add(elementsAccesed, validateGuessedTypes);

					} // end else

					elementsAccesed++;

				} // end for col

				//} // end while

				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}

			} // end for row

			// print the elements in the list of col types
			for (int i = 0; i < numbcols; i++) {
				System.out.println("For col " + i + listOfValidatingHashMaps.get(i));
			}

			// return the highest type for each list index
			int highesttypenum = 0;
			String dominantType = "none yet";

			for (int i = 0; i < listOfValidatingHashMaps.size(); i++) {

				Map<String, Integer> highestTypeInCol = new HashMap<String, Integer>();

				for (String datatype : listOfValidatingHashMaps.get(i).keySet()) {
					Integer thetype = listOfValidatingHashMaps.get(i).get(datatype);

					if (thetype > highesttypenum) {
						highesttypenum = thetype;
						dominantType = datatype;
					}

					addTypeToHashMap(highestTypeInCol, dominantType);
				}
				listHighestTypeInCols.add(i, highestTypeInCol);
			}

			// compare the results from the 2 lists of hash tables created
			for (int i = 0; i < listHighestTypeInCols.size(); i++) {

				System.out.println(listHighestTypeInCols.get(i).equals(listOfHighestTypeInCols.get(i)));

			}

		}); // end the second thread

		t1.start();
		try {
			Thread.sleep(10);
		} catch (Exception e) {
		}
		t2.start();



		// if the types for each col match, then return guess matched

		// if the types don't match, return, guess for second worker as actuall guess
		// and
		// set a flag

	}// end processFile

}// end DetectorClass
