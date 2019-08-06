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

	public static Detector myDetector;

	public static void main(String[] args) throws IOException {
		myDetector = new Detector(args[0], args[1]);

		guessDataTypesInEachColumn();

	}// end main

	public static void guessDataTypesInEachColumn() {
		for (int i = 0; i < myDetector.getSizeOfThreadPerDataGroup(); i++) {

			myDetector.getThreadInDataGroup()[i] = new Thread(() -> {
				myDetector.getThreadId();

				if (myDetector.getNumTimesFunctionHasExecuted() != 0) {
					myDetector.getNewThreadId();
				}

				findDataTypesPerColumn();

				myDetector.addDataTypesFromAllColumnsToCollectionOfTypesFromDataProvided(); // change the last part of
																							// the name

				myDetector.incrementNumTimesFunctionHasExcuted();
			});
			myDetector.getThreadInDataGroup()[i].start();
		}

		for (int i = 0; i < myDetector.getSizeOfThreadPerDataGroup(); i++) {
			try {
				myDetector.getThreadInDataGroup()[i].join();

				if (!myDetector.getLastThreadInGroupOfThreads().isAlive()) {
					determineDominantTypeInEachGroupOfData();
					determineIfDataTypesInBothDataGroupsMatch();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void findDataTypesPerColumn() {
		int col = 0;
		if (myDetector.getThreadId() != myDetector.getFirstThread()) {

			col = 1;
		}
		try {
			CSVParser analyizeCsvData = new CSVParser(new FileReader(myDetector.getFilePathToData()),
					CSVFormat.DEFAULT.withHeader());

			myDetector.determineNumberOfColsInCsv(analyizeCsvData.getHeaderMap());

			for (CSVRecord row : analyizeCsvData) {
				for (; col < myDetector.getNumOfColsInCsv(); col += 2) {
					Map<String, Integer> dataTypesFromOneColumn = new HashMap<String, Integer>(); // look into this...

					myDetector.addTypesToListOfDataTypesMatched(findMatchingRegexForDataType(row.get(col)));

					for (int i = 0; i < myDetector.getSizeOfListOfDataTypesMatched(); i++) {
						if (dataTypesFromOneColumn.containsKey(myDetector.getListOfDataTypesMatched().get(i))) {
							dataTypesFromOneColumn.put(myDetector.getListOfDataTypesMatched().get(i),
									dataTypesFromOneColumn.get(myDetector.getListOfDataTypesMatched().get(i)) + 1);
						} else {
							dataTypesFromOneColumn.put(myDetector.getListOfDataTypesMatched().get(i), 1);
						}
					}
					myDetector.addToTheListOfDataTypesOfAllColumns(dataTypesFromOneColumn);
				}
				if (myDetector.getThreadId() != myDetector.getFirstThread()) {

					col = 1;
				} else {
					col = 0;
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

	}// end findDataTypesPerColumn

	public static List<String> findMatchingRegexForDataType(String dataFromOneColumnCell) {
		int regexColumnInCsv = 1;
		int dataTypeOfRegex = 0;
		List<String> datatypesMatchedPerCol = new ArrayList<String>();
		try {
			CSVParser analyizeCsvForRegex = new CSVParser(new FileReader(myDetector.getRegexFilePath()),
					CSVFormat.TDF.withHeader());
			for (CSVRecord row : analyizeCsvForRegex) {
				if (dataFromOneColumnCell.matches(row.get(regexColumnInCsv))) {
					datatypesMatchedPerCol.add(row.get(dataTypeOfRegex));

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
		return datatypesMatchedPerCol;
	}// end findMatchingRegexForDataType

	public static void determineDominantTypeInEachGroupOfData() {
		int frequencyOfDominantType = 0;
		String dominantType = "none yet";
		List<Map<String, Integer>> dominantDataTypeInGroupOfData = new ArrayList<Map<String, Integer>>();

		for (int i = 0; i < myDetector.getDataTypesFoundFromDataProvided().get(myDetector.getThreadId()).size(); i++) {
			frequencyOfDominantType = 0;
			dominantType = "none yet";
			Map<String, Integer> dominantDataTypeInCol = new HashMap<String, Integer>();
			
			System.out.println(myDetector.getDataTypesFoundFromDataProvided());

			for (String currentDataType : myDetector.getDataTypesFoundFromDataProvided().get(myDetector.getThreadId())
					.get(i).keySet()) {
				Integer frequencyOfCurrentType = myDetector.getDataTypesFoundFromDataProvided()
						.get(myDetector.getThreadId()).get(i).get(currentDataType);
				if (frequencyOfCurrentType > frequencyOfDominantType) {
					frequencyOfDominantType = frequencyOfCurrentType;
					dominantType = currentDataType;
				}
			}
			System.out.println("The dominant type in col " + i + " " + dominantType);
			dominantDataTypeInCol.put(dominantType, frequencyOfDominantType);
			dominantDataTypeInGroupOfData.add(i, dominantDataTypeInCol);
		}
		myDetector.addToTheDominantTypesOfDataProvided(dominantDataTypeInGroupOfData);

	}

	public static void determineIfDataTypesInBothDataGroupsMatch() {
		boolean isDeterminedDataType = false;
		int dataTypesFromFirstGroup = 0;
		int dataTypesFromSecondGroup = 1;

		for (int i = 0; i < myDetector.getDominantDataTypesFromDataProvided().get(dataTypesFromSecondGroup)
				.size(); i++) {
			isDeterminedDataType = myDetector.getDominantDataTypesFromDataProvided().get(dataTypesFromSecondGroup).get(i)
					.equals(myDetector.getDominantDataTypesFromDataProvided().get(dataTypesFromFirstGroup).get(i));

			if (isDeterminedDataType == true) {
				System.out.println("Guessed type for col " + i + " matches? " + isDeterminedDataType);
			} else {
				System.out.println("Guessed type for col " + i + " matches? " + isDeterminedDataType);
				System.out.println("Actual type for col " + i + " "
						+ myDetector.getDominantDataTypesFromDataProvided().get(dataTypesFromSecondGroup).get(i));
				System.out.println("");
			}
		} // end for
	}// end determineIfGuessedTypeIsTrue

}
