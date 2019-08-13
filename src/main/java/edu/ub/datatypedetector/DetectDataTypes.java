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
import java.util.Objects;
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

	public static void main(String[] args) throws IOException, InterruptedException {
		myDetector = new Detector(args[0], args[1]);

		guessDataTypesInEachColumn();

	}// end main

	public static void guessDataTypesInEachColumn() throws InterruptedException {
		for (int i = 0; i < myDetector.threadPerDataGroup.length; i++) {

			myDetector.threadPerDataGroup[i] = new Thread(() -> {
				if (myDetector.numTimesFunctionHasExecuted != 0) {
					++(myDetector.threadId);
				}
				System.out.println("Starting thread ID..." + myDetector.threadId);

				findDataTypesPerColumn();

			});
			myDetector.threadPerDataGroup[i].start();

			myDetector.threadPerDataGroup[i].join();

			++myDetector.numTimesFunctionHasExecuted;

			// determine the most dominant type from each col read, return the most dominant from each col

		}
	}

	public static void findDataTypesPerColumn() {
		int col = 0;
		int positionInList = 0;
		int numTimesRowHasExecuted = 0;
		List<Map<String, Integer>> dataTypesFromAllColumns = new ArrayList<Map<String, Integer>>();

		if (myDetector.threadId != myDetector.firstThread) {
			col = 1;
		}
		try {
			CSVParser analyizeCsvData = new CSVParser(new FileReader(myDetector.filePathToData),
					CSVFormat.DEFAULT.withHeader());

			myDetector.determineNumberOfColsInCsv(analyizeCsvData.getHeaderMap());

			for (CSVRecord row : analyizeCsvData) {
				for (; col < myDetector.numberOfColumns; col += 2) {
					Map<String, Integer> dataTypesFromOneColumn = new HashMap<String, Integer>();

					myDetector.datatypesMatched = findMatchingRegexForDataType(row.get(col));

					if (numTimesRowHasExecuted < 1) {
						for (int i = 0; i < myDetector.datatypesMatched.size(); i++) {
							if (dataTypesFromOneColumn.containsKey(myDetector.datatypesMatched.get(i))) {
								dataTypesFromOneColumn.put(myDetector.datatypesMatched.get(i),
										dataTypesFromOneColumn.get(myDetector.datatypesMatched.get(i)) + 1);
							} else {
								dataTypesFromOneColumn.put(myDetector.datatypesMatched.get(i), 1);
							}
						}

						dataTypesFromAllColumns.add(dataTypesFromOneColumn);
					} // end if
					else {
						for (int i = 0; i < myDetector.datatypesMatched.size(); i++) {
							if (dataTypesFromAllColumns.get(positionInList)
									.containsKey(myDetector.datatypesMatched.get(i))) {
								dataTypesFromAllColumns.get(positionInList).put(myDetector.datatypesMatched.get(i),
										dataTypesFromAllColumns.get(positionInList)
												.get(myDetector.datatypesMatched.get(i)) + 1);
							} else {
								dataTypesFromAllColumns.get(positionInList).put(myDetector.datatypesMatched.get(i), 1);
							}
						} // end for
					} // end else
					if (positionInList >= dataTypesFromAllColumns.size() - 1) {
						positionInList = 0;
					} else {
						++positionInList;
					}
				}
				++numTimesRowHasExecuted;
				if (myDetector.threadId == myDetector.firstThread) {

					col = 0;

				} else {
					col = 1;
				}
			} // end for row

			System.out.println(dataTypesFromAllColumns);
			myDetector.dataTypesFoundFromDataProvided.add(dataTypesFromAllColumns);
			System.out.println("from the main" + myDetector.dataTypesFoundFromDataProvided);

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
			CSVParser analyizeCsvForRegex = new CSVParser(new FileReader(myDetector.regexFilePath),
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

		for (int i = 0; i < myDetector.dataTypesFoundFromDataProvided.get(myDetector.threadId).size(); i++) {
			frequencyOfDominantType = 0;
			dominantType = "none yet";
			Map<String, Integer> dominantDataTypeInCol = new HashMap<String, Integer>();

			System.out.println(myDetector.dataTypesFoundFromDataProvided);

			for (String currentDataType : myDetector.dataTypesFoundFromDataProvided.get(myDetector.threadId).get(i)
					.keySet()) {
				Integer frequencyOfCurrentType = myDetector.dataTypesFoundFromDataProvided
						.get(myDetector.threadId).get(i).get(currentDataType);
				if (frequencyOfCurrentType > frequencyOfDominantType) {
					frequencyOfDominantType = frequencyOfCurrentType;
					dominantType = currentDataType;
				}
			}
			System.out.println("The dominant type in col " + i + " " + dominantType);
			dominantDataTypeInCol.put(dominantType, frequencyOfDominantType);
			dominantDataTypeInGroupOfData.add(i, dominantDataTypeInCol);
		}
		myDetector.dominantDataTypesFromDataProvided.add(dominantDataTypeInGroupOfData);

	}//end determine dominant type
}