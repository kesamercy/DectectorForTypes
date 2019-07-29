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
		//comparing enums is much faster... than comparing the strings....
		//this is better in the long run...
		
		//think about replacing the line number in the REGEX file... so that it's much faster
		//the line number can be traced back to the file with enum. 
	}

	public static String regexFilePath = "none yet";
	public static int fractionOfRecordsInFirstGroup = 0;
	public static final int GROUPSOFDATA = 2;
	public static String filePathToData = "none yet";
	public static ArrayList<List<Map<String, Integer>>> collectionOfDataTypesPerGroupOfData = new ArrayList<List<Map<String, Integer>>>();
	public static Thread[] threadPerDataGroup = new Thread[GROUPSOFDATA];
	public static ArrayList<Map<String, Integer>> sortedDataTypes = new ArrayList<Map<String, Integer>>();
	

	public static void main(String[] args) throws IOException {
		filePathToData = args[0];
		String numToDivideDataIntoTwoGroups = args[1];
		regexFilePath = args[2];
		fractionOfRecordsInFirstGroup = Integer.parseInt(numToDivideDataIntoTwoGroups);

		guessDataTypesInEachColumn();
		
		//compare the results in the collectionOfDataTypesPerGroupOfData and return the greatest or not	
	}// end main
	
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
			dominantDataTypeInCol.put(dominantType, frequencyOfDominantType);
			dominantDataTypeInGroupOfData.add(i, dominantDataTypeInCol);
		}

	}
	
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

	public static void guessDataTypesInEachColumn() {
		for (int i = 0; i < threadPerDataGroup.length; i++) {

			//think about how to implement the thread numbers 
			threadPerDataGroup[i] = new Thread(() -> {
				int threadNum = 0;
				int numTimesFunctionHasExecuted = 0;
				if (numTimesFunctionHasExecuted != 0) {
					++threadNum;
				}
				List<Map<String, Integer>> listOfDataTypeFromAGroup = new ArrayList<Map<String, Integer>>();

				listOfDataTypeFromAGroup = findDataTypesPerColumn(threadNum);
				collectionOfDataTypesPerGroupOfData.add(listOfDataTypeFromAGroup);
				++numTimesFunctionHasExecuted;
			});
			threadPerDataGroup[i].start();
		}
		for (int i = 0; i < threadPerDataGroup.length; i++) {
			try {
				threadPerDataGroup[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}// end findDataTypes

	public static List<Map<String, Integer>> findDataTypesPerColumn(int threadNum) {
		int numberOfColumns = 0;
		int col = 0;
		int firstThread = 1;  //think about making this a global variable
		Map<String, Integer> numElementsInHeader;
		List<String> datatypesMatched = null;
		List<Map<String, Integer>> listOfDataTypesPerColumn = null;
		if (threadNum != firstThread) {
			
			col = 1;
		}
		try {
			CSVParser analyizeCsvData = new CSVParser(new FileReader(filePathToData), CSVFormat.DEFAULT.withHeader());
			numElementsInHeader = analyizeCsvData.getHeaderMap();
			numberOfColumns = numElementsInHeader.size();	
			numElementsInHeader.clear();

			for (CSVRecord row : analyizeCsvData) {
				for (col = 0; col < numberOfColumns; col++) {
					datatypesMatched = matchDataToRegex(row.get(col));
					
					for (int i = 0; i < datatypesMatched.size(); i++) {
						if (listOfDataTypesPerColumn.get(col).containsKey(datatypesMatched.get(i))) {
							listOfDataTypesPerColumn.get(col).put(datatypesMatched.get(i),
									listOfDataTypesPerColumn.get(col).get(datatypesMatched.get(i)) + 1);
						} else {
							listOfDataTypesPerColumn.get(col).put(datatypesMatched.get(i), 1);
						}
					}
					++col;
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

	public static List<String> matchDataToRegex(String dataFromOneColumnCell) {
		int regex = 1;
		int dataTypeOfRegex = 0;
		List<String> datatypesMatched = null;
		try {
			CSVParser analyizeCsvForRegex = new CSVParser(new FileReader(regexFilePath),
					CSVFormat.DEFAULT.withHeader());
			for (CSVRecord row : analyizeCsvForRegex) {
				if (dataFromOneColumnCell.matches(row.get(regex))) {
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
	}// end matchDataToRegex

}
