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
		ArrayList<String[]> listOfData = new ArrayList<String[]>();
		Scanner input = new Scanner(System.in);
		int numToDetermineHowDataIsGrouped = 0;

		String filePath = "C:\\Users\\nm293\\eclipse-workspace\\DetectDataTypes\\detectorPackage\\data.csv";
		transformCsvToList(filePath, listOfData);

		System.out.println("Enter the number you wish to use to group the data when determing the data types");
		numToDetermineHowDataIsGrouped = input.nextInt();
		detectDataTypes(listOfData, numToDetermineHowDataIsGrouped);

	}// end main

	/*
	 * method to convert a csv file to a list input: filepath to csv file output:
	 * csv data in list form
	 */
	public static void transformCsvToList(String filePath, ArrayList<String[]> listOfData) {
		File fileObject = new File(filePath);
		String oneRowOfDataFromFile;
		String[] rowDataInArrayForm;

		try {
			Scanner fileData = new Scanner(fileObject);
			fileData.next(); // skip header

			while (fileData.hasNext()) { // populate the Array List with data from the file
				oneRowOfDataFromFile = fileData.next();
				rowDataInArrayForm = oneRowOfDataFromFile.split(",");
				listOfData.add(rowDataInArrayForm);
			} // end while

			fileData.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}// end transformcsv

	/*
	 * Method to detect data types from a list of data given. Input: Iterable List
	 * of data Output: List of data types detected per column.
	 *
	 */
	public static void detectDataTypes(ArrayList<String[]> listOfData, int numToDetermineHowDataIsGrouped) {
		List<Map<String, Integer>> dataTypesInFirstGroupOfData = new ArrayList<Map<String, Integer>>();
		List<Map<String, Integer>> dataTypesFromSecondGroupOfData = new ArrayList<Map<String, Integer>>();
		List<Map<String, Integer>> dominantDataTypeInFirstGroup = new ArrayList<Map<String, Integer>>();
		List<Map<String, Integer>> dominantDataTypeFromSecondGroup = new ArrayList<Map<String, Integer>>();

		Thread firstGroupOfData = new Thread(() -> {
			int dataGroupNumber = 1;

			guessDataTypesInEachColumn(listOfData, numToDetermineHowDataIsGrouped, dataGroupNumber,
					dataTypesInFirstGroupOfData, dominantDataTypeInFirstGroup);
		});

		Thread secondGroupOfData = new Thread(() -> {
			int dataGroupNumber = 2;

			guessDataTypesInEachColumn(listOfData, numToDetermineHowDataIsGrouped, dataGroupNumber,
					dataTypesFromSecondGroupOfData, dominantDataTypeFromSecondGroup);
			determineIfGuessedDataTypesIsTrue(dominantDataTypeInFirstGroup, dominantDataTypeFromSecondGroup);
		});

		firstGroupOfData.start();
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
		}
		secondGroupOfData.start();

	}

	/*
	 * method to find types in each column from the lsit of data given
	 */
	public static void guessDataTypesInEachColumn(ArrayList<String[]> listOfData, int numToDetermineHowDataIsGrouped,
			int dataGroupNumber, List<Map<String, Integer>> dataTypesInGroupOfData,
			List<Map<String, Integer>> dominantDataTypeInGroupOfData) {

		String typefound = "none yet";
		int numCols = 0;
		int postnInList = 0;
		int count = 0;
		int elementsVisited = 0;
		boolean isValidCondition = true;
		int numDataInList = listOfData.size();
		int numOfElementsInGroupOfData = numDataInList / numToDetermineHowDataIsGrouped;

		OperatorClass operator = OperatorClass.LESSTHAN;
		if (dataGroupNumber == 2) {
			operator = OperatorClass.GREATERTHAN;
		}
		for (String[] i : listOfData) {
			numCols = i.length;
			for (String j : i) {
				isValidCondition = determineIfGreaterThanOrLessThan(operator, count, numOfElementsInGroupOfData);

				if (isValidCondition == true) {
					typefound = findMatchingDataType(j);
					if (elementsVisited >= numCols) {
						addDataTypeFound(dataTypesInGroupOfData.get(postnInList), typefound);
						++postnInList;
						if (postnInList >= numCols) {
							postnInList = 0;
						}
					} else {

						Map<String, Integer> dataTypesForEachCol = new HashMap<String, Integer>();
						addDataTypeFound(dataTypesForEachCol, typefound);
						dataTypesInGroupOfData.add(elementsVisited, dataTypesForEachCol);

					} // end else
					elementsVisited++;
				} // end if count
			} // end for col
			++count;
		} // end for row

		// print the elements in the list of col types
		for (int i = 0; i < numCols; i++) {
			System.out.println("For col " + i + dataTypesInGroupOfData.get(i));
		}

		System.out.println("");

		determineDominantTypePerGroupOfData(dataTypesInGroupOfData, dominantDataTypeInGroupOfData);

		System.out.println(" ");

	}// end guesstypes per col

	/*
	 * method to determine highest type in each col
	 */
	public static void determineDominantTypePerGroupOfData(List<Map<String, Integer>> dataTypesInGroupOfData,
			List<Map<String, Integer>> dominantDataTypeInGroupOfData) {
		int frequencyOfDominantType = 0;
		String dominantType = "none yet";

		for (int i = 0; i < dataTypesInGroupOfData.size(); i++) {
			frequencyOfDominantType = 0;
			dominantType = "none yet";
			Map<String, Integer> dominantDataTypeInCol = new HashMap<String, Integer>();

			for (String currentDataType : dataTypesInGroupOfData.get(i).keySet()) {
				Integer frequencyOfCurrentType = dataTypesInGroupOfData.get(i).get(currentDataType);
				if (frequencyOfCurrentType > frequencyOfDominantType) {
					frequencyOfDominantType = frequencyOfCurrentType;
					dominantType = currentDataType;
				}
			}
			System.out.println("The dominant type in col " + i + " " + dominantType);
			addDataTypeFound(dominantDataTypeInCol, dominantType);
			dominantDataTypeInGroupOfData.add(i, dominantDataTypeInCol);
		}

	}

	/*
	 * Method to add the datatype found to the collection of data types input: data
	 * type found input 2: Hash Map where type will be added return: none
	 */
	public static void addDataTypeFound(Map<String, Integer> collectionOfDataTypes, String typefound) {
		String intAlsoRecognisedAsFloat = "integer";
		String floatType = "float";

		if (collectionOfDataTypes.containsKey(typefound)) {
			if (typefound.equalsIgnoreCase(intAlsoRecognisedAsFloat) == true) {
				addDataTypeFound(collectionOfDataTypes, floatType);
			}
			collectionOfDataTypes.put(typefound, collectionOfDataTypes.get(typefound) + 1);
		}
		else {
			if (typefound.equalsIgnoreCase(intAlsoRecognisedAsFloat) == true) {
				addDataTypeFound(collectionOfDataTypes, floatType);
			}
			collectionOfDataTypes.put(typefound, 1);
		}

	}// end addToHashMap

	/*
	 * method to determine if the guessed type in the data is true
	 */
	public static void determineIfGuessedDataTypesIsTrue(List<Map<String, Integer>> dominantDataTypeInFirstGroup, List<Map<String, Integer>> dominantDataTypeFromSecondGroup) {
		boolean isGuessedType = false;

		for (int i = 0; i < dominantDataTypeFromSecondGroup.size(); i++) {
			isGuessedType = dominantDataTypeFromSecondGroup.get(i).equals(dominantDataTypeInFirstGroup.get(i));

			if (isGuessedType == true) {
				System.out.println("Guessed type for col " + i + " matches? " + isGuessedType);
			} else {
				System.out.println("Guessed type for col " + i + " matches? " + isGuessedType);
				System.out.println("Actual type for col " + i + " " + dominantDataTypeFromSecondGroup.get(i));
				System.out.println("");
			}
		} // end for
	}// end determineIfGuessedTypeIsTrue

	/*
	 * Method to find the respective data type that matches the string Respective
	 * types is found by comparing it with regular expressions till a most suitable
	 * match is found input: String return: type determined from the string
	 */
	public static String findMatchingDataType(String stringWithoutDefinedDataType) {
		String integer = "([0-9]+)";
		String floatnum = "(\\d*\\.?\\d*)";
		String date = "(0?[1-9]|1[012])[- \\/.](0?[1-9]|[12][0-9]|3[01])[- \\/.](19|20)\\d\\d";
		String bool = "([Vv]+(erdade(iro)?)?|[Ff]+(als[eo])?|[Tt]+(rue)?|0|[\\+\\-]?1)";
		String timestamp = "([0-1][0-9]|[2][0-3]):([0-5][0-9])";
		String stringDataType = "([a-zA-Z]+)";
		boolean hasMatchedToRegex = false;
		String definedTypeForString = "String";
		String[] regexTypes = { bool, timestamp, date, integer, floatnum, stringDataType };

		for (int i = 0; i < regexTypes.length; ++i) {
			hasMatchedToRegex = stringWithoutDefinedDataType.matches(regexTypes[i]);

			if (hasMatchedToRegex == true) {
				if (regexTypes[i] == date) {
					definedTypeForString = "date";
				}
				if (regexTypes[i] == bool) {
					definedTypeForString = "bool";
				}
				if (regexTypes[i] == integer) {
					definedTypeForString = "integer";
				}
				if (regexTypes[i] == floatnum) {
					definedTypeForString = "float";
				}
				if (regexTypes[i] == timestamp) {
					definedTypeForString = "time";
				}
				if (regexTypes[i] == stringDataType) {
					definedTypeForString = "String";
				}
				return definedTypeForString;

			} // end if
		} // end for
		return definedTypeForString;

	}// end findMatchingDataType

	public static boolean determineIfGreaterThanOrLessThan(OperatorClass op, int var1, int var2) {

		OperatorClass optrClass = OperatorClass.GREATERTHAN;

		if (op == optrClass) {

			return Boolean.valueOf(op.isGreaterThan(var1, var2));
		}

		return Boolean.valueOf(op.isLessThan(var1, var2));
	}// end

}// end DetectorClass
