package edu.ub.datatypedetector;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.channels.NonReadableChannelException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class DetectDataTypes {

	public enum DataTypes {
		INTEGER, FLOAT, DATE, BOOLEAN, TIME, STRING;
		// comparing enums is much faster... than comparing the strings....
		// this is better in the long run...

		// think about replacing the line number in the REGEX file... so that it's much
		// faster
		// the line number can be traced back to the file with enum.
	}

	public static String regexFilePath = "none yet";
	public static int fractionOfRecordsInFirstGroup = 0;
	public static final int GROUPSOFDATA = 2;
	public static String filePathToData = "none yet";
	public static ArrayList<List<Map<String, Integer>>> dataTypesFoundFromDataProvided = new ArrayList<List<Map<String, Integer>>>();
	public static ArrayList<List<Map<String, Integer>>> dominantDataTypesFromDataProvided = new ArrayList<List<Map<String, Integer>>>();
	public static Thread[] threadPerDataGroup = new Thread[GROUPSOFDATA];
	public static int threadNum = 0;

	// can't have 2 data guesses at the same time.... also: might not be clear to someone else that they need to set a bunch of variables .... the reason why this may not be efficient.

	// create  a new class that has this as instance variables..... instead of having the static varibales here....
	// it also helps to put the initalization step in one place.... you have a construtor so that takes care of it.
	// take the static varaibles and turn them into class varaibles....


	// static variables are generally not the best option.... might want to change this.....
	// sepearete the logic from the parts of the code that perform the functions.

	public static void main(String[] args) throws IOException {
		filePathToData = args[0];
		String numToDivideDataIntoTwoGroups = args[1]; //we can do awy with this... not needed for now
		regexFilePath = args[2];
		fractionOfRecordsInFirstGroup = Integer.parseInt(numToDivideDataIntoTwoGroups);

		guessDataTypesInEachColumn();
		determineDominantTypePerGroupOfData();

	}// end main

	public static void guessDataTypesInEachColumn() {
		for (int i = 0; i < threadPerDataGroup.length; i++) {

			// if you decalre it as final, then you can use it in the thread..

			// think about how to implement the thread numbers
			threadPerDataGroup[i] = new Thread(() -> {
				int threadNum = 0; //or pass it as a parameter...
				int numTimesFunctionHasExecuted = 0;
				if (numTimesFunctionHasExecuted != 0) {
					++threadNum;
				}
				List<Map<String, Integer>> listOfDataTypesFoundInEachColumn = new ArrayList<Map<String, Integer>>();

				listOfDataTypesFoundInEachColumn = findDataTypesPerColumn();
				dataTypesFoundFromDataProvided.add(listOfDataTypesFoundInEachColumn);
				++numTimesFunctionHasExecuted;
			});
			threadPerDataGroup[i].start();
		}
		for (int i = 0; i < threadPerDataGroup.length; i++) {
			try {
				threadPerDataGroup[i].join();

				if (!threadPerDataGroup[threadPerDataGroup.length - 1].isAlive()) {
					determineIfGuessedDataTypesIsTrue();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}// end findDataTypes

	public static List<Map<String, Integer>> findDataTypesPerColumn() {
		int numberOfColumns = 0;
		int col = 0;
		int firstThread = 0;
		Map<String, Integer> numElementsInHeader;
		List<String> datatypesMatched = new ArrayList<String>();
		List<Map<String, Integer>> listOfDataTypesPerColumn = new ArrayList<Map<String, Integer>>();
		if (threadNum != firstThread) {

			col = 1;
		}
		try {
			CSVParser analyizeCsvData = new CSVParser(new FileReader(filePathToData), CSVFormat.DEFAULT.withHeader());
			numElementsInHeader = analyizeCsvData.getHeaderMap();
			numberOfColumns = numElementsInHeader.size();
			numElementsInHeader.clear();

			for (CSVRecord row : analyizeCsvData) {
				for (col; col < numberOfColumns; col+=2) {  //col should be reset..... here look into some issues
					datatypesMatched = findMatchingRegexForDataType(row.get(col));
					//first store the type found into a hash map then add it to the listofdatatypes per column

					for (int i = 0; i < datatypesMatched.size(); i++) {
						if (!listOfDataTypesPerColumn.isEmpty()) {
							if (listOfDataTypesPerColumn.get(col).containsKey(datatypesMatched.get(i))) {
								listOfDataTypesPerColumn.get(col).put(datatypesMatched.get(i),
										listOfDataTypesPerColumn.get(col).get(datatypesMatched.get(i)) + 1);
							} else {
								listOfDataTypesPerColumn.get(col).put(datatypesMatched.get(i), 1);
							}
						}//end if for empty
						else {
							//alternatively, store the data into a hash map then add it to the list.
//							listOfDataTypesPerColumn.add(col, listOfDataTypesPerColumn.set(index, element));
						}

					}
				}

			}
			analyizeCsvData.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listOfDataTypesPerColumn;
	}// end findDataTypesPerColumn

	public static List<String> findMatchingRegexForDataType(String dataFromOneColumnCell) {
		int regexColumnInCsv = 1;
		int dataTypeOfRegex = 0;
		List<String> datatypesMatched = new ArrayList<String>();
		dataFromOneColumnCell = "4";
		try {
			CSVParser analyizeCsvForRegex = new CSVParser(new FileReader(regexFilePath),
					CSVFormat.DEFAULT.withHeader());
			for (CSVRecord row : analyizeCsvForRegex) {
				if (dataFromOneColumnCell.matches(row.get(regexColumnInCsv))) {
					datatypesMatched.add(row.get(dataTypeOfRegex));
				}
			}
			analyizeCsvForRegex.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return datatypesMatched;
	}// end findMatchingRegexForDataType

	public static void determineDominantTypePerGroupOfData() {
		int frequencyOfDominantType = 0;
		String dominantType = "none yet";
		List<Map<String, Integer>> dominantDataTypeInGroupOfData = new ArrayList<Map<String, Integer>>();

		for (int i = 0; i < dataTypesFoundFromDataProvided.get(threadNum).size(); i++) {
			frequencyOfDominantType = 0;
			dominantType = "none yet";
			Map<String, Integer> dominantDataTypeInCol = new HashMap<String, Integer>();

			for (String currentDataType : dataTypesFoundFromDataProvided.get(threadNum).get(i).keySet()) {
				Integer frequencyOfCurrentType = dataTypesFoundFromDataProvided.get(threadNum).get(i)
						.get(currentDataType);
				if (frequencyOfCurrentType > frequencyOfDominantType) {
					frequencyOfDominantType = frequencyOfCurrentType;
					dominantType = currentDataType;
				}
			}
			System.out.println("The dominant type in col " + i + " " + dominantType);
			dominantDataTypeInCol.put(dominantType, frequencyOfDominantType);
			dominantDataTypeInGroupOfData.add(i, dominantDataTypeInCol);
		}
		dominantDataTypesFromDataProvided.add(dominantDataTypeInGroupOfData);

	}

	public static void determineIfGuessedDataTypesIsTrue() {
		boolean isGuessedType = false;

		for (int i = 0; i < dominantDataTypesFromDataProvided.get(2).size(); i++) {
			isGuessedType = dominantDataTypesFromDataProvided.get(2).get(i)
					.equals(dominantDataTypesFromDataProvided.get(1).get(i));

			if (isGuessedType == true) {
				System.out.println("Guessed type for col " + i + " matches? " + isGuessedType);
			} else {
				System.out.println("Guessed type for col " + i + " matches? " + isGuessedType);
				System.out.println("Actual type for col " + i + " " + dominantDataTypesFromDataProvided.get(2).get(i));
				System.out.println("");
			}
		} // end for
	}// end determineIfGuessedTypeIsTrue

}
