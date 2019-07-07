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

		List<Map<String, Integer>> dataTypesInFristrows = new ArrayList<Map<String, Integer>>();
		List<Map<String, Integer>> dataTypesInLastrows = new ArrayList<Map<String, Integer>>();
		List<Map<String, Integer>> dominantTypeFirstrows = new ArrayList<Map<String, Integer>>();
		List<Map<String, Integer>> dominantTypeLastrows = new ArrayList<Map<String, Integer>>();

		Thread t1 = new Thread(() -> {
			int firstThread = 1;

			processThreads(iterableString, firstThread, dataTypesInFristrows, dominantTypeFirstrows);

		}); // end the first thread

		Thread t2 = new Thread(() -> {
			int secondThread = 2;

			processThreads(iterableString, secondThread, dataTypesInLastrows, dominantTypeLastrows);

			// compare the results from the 2 lists of hash tables created
			for (int i = 0; i < dominantTypeLastrows.size(); i++) {

				if (dominantTypeLastrows.get(i).equals(dominantTypeFirstrows.get(i)) == true) {

					System.out.println(dominantTypeLastrows.get(i).equals(dominantTypeFirstrows.get(i)));
				}

				else {
					System.out.println("");
					System.out.println("Previous guessed type for col " + i + " is incorrect");
					System.out.println("Actual type for col " + i + " " + dominantTypeLastrows.get(i));
				}

			}

		}); // end the second thread

		t1.start();
		try {
			Thread.sleep(10);
		} catch (Exception e) {
		}
		t2.start();

	}// end processFile

	public static boolean testCondition(OperatorClass op, int var1, int var2) {

		OperatorClass optrClass = OperatorClass.GREATERTHAN;

		if (op == optrClass) {

			return Boolean.valueOf(op.isGreaterThan(var1, var2));
		}

		return Boolean.valueOf(op.isLessThan(var1, var2));
	}// end

	public static void processThreads(ArrayList<String[]> iterableString, int threadNum,
			List<Map<String, Integer>> listOfHashmaps, List<Map<String, Integer>> listOfDominantTypes) {

		String typefound = "none yet";
		int numrows = iterableString.size();
		int numcols, postnInList, count, numElementsAccesed;
		numcols = 0;
		postnInList = 0;
		count = 0;
		numElementsAccesed = 0;
		boolean conditionMet = true;
		numrows = numrows / 2;
		OperatorClass operator = OperatorClass.LESSTHAN;

		if (threadNum == 2) {

			operator = OperatorClass.GREATERTHAN;
		}

		// iterate through the iterable list
		for (String[] i : iterableString) {

			numcols = i.length;

			for (String j : i) {

				conditionMet = testCondition(operator, count, numrows);

				if (conditionMet == true) {

					typefound = findType(j);

					if (numElementsAccesed >= numcols) {

						addTypeToHashMap(listOfHashmaps.get(postnInList), typefound);
						++postnInList;
						if (postnInList >= numcols) {
							postnInList = 0;
						}
					} else {

						Map<String, Integer> colDataTypes = new HashMap<String, Integer>();
						addTypeToHashMap(colDataTypes, typefound);
						// add the hash map to the list
						listOfHashmaps.add(numElementsAccesed, colDataTypes);

					} // end else

					numElementsAccesed++;

				} // end if count

			} // end for col

			++count;

			try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}

		} // end for row

		// print the elements in the list of col types
		for (int i = 0; i < numcols; i++) {
			System.out.println("For col " + i + listOfHashmaps.get(i));
		}

		// return the highest type for each list index
		int highesttypenumber = 0;
		String dominantTypes = "none yet";

		for (int i = 0; i < listOfHashmaps.size(); i++) {

			Map<String, Integer> highestTypeInEachCol = new HashMap<String, Integer>();

			for (String datatype : listOfHashmaps.get(i).keySet()) {
				Integer thetype = listOfHashmaps.get(i).get(datatype);

				if (thetype > highesttypenumber) {
					highesttypenumber = thetype;
					dominantTypes = datatype;
				}
			}
			addTypeToHashMap(highestTypeInEachCol, dominantTypes);
			listOfDominantTypes.add(i, highestTypeInEachCol);
		}

	}// end process threads method

}// end DetectorClass
