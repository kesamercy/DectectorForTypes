package edu.ub.datatypedetector;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

	public static void main(String[] args) throws IOException {
		String filePathtoData = args[0];
		String numToDivideDataIntoTwoGroups = args[1];
		regexFilePath = args[2];
		fractionOfRecordsInFirstGroup = Integer.parseInt(numToDivideDataIntoTwoGroups);

		guessDataTypesInEachColumn(filePathtoData);

		
	}// end main

	public static void guessDataTypesInEachColumn(String filePathToData) {
		ArrayList<List<Map<String, Integer>>> collectionOfDataTypesPerGroupOfData = new ArrayList<List<Map<String, Integer>>>();
		Thread[] threadPerDataGroup = new Thread[GROUPSOFDATA];

		for (int i = 0; i < threadPerDataGroup.length; i++) {

			threadPerDataGroup[i] = new Thread(() -> {
				int threadNum = 0;
				List<Map<String, Integer>> listOfDataTypeFromAGroup = new ArrayList<Map<String, Integer>>();

				listOfDataTypeFromAGroup = findDataTypesPerColumn(filePathToData, threadNum);
				collectionOfDataTypesPerGroupOfData.add(listOfDataTypeFromAGroup);
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
		
		//compare the results in the collectionOfDataTypesPerGroupOfData and return the greatest or not
		
	}// end findDataTypes

	public static List<Map<String, Integer>> findDataTypesPerColumn(String filePathToData, int threadNum) {
		int numberOfColumns = 0;
		int numberOfRows =  0;
		int count = 0;
		int firstThread = 1;  //think about making this a global variable
		int numElementsInFirstGroupOfData = 0;
		Map<String, Integer> numElementsInHeader;
		List<String> datatypesMatched = null;
		List<CSVRecord> numberOfRecorsInFile = null;
		List<Map<String, Integer>> listOfDataTypesPerColumn = null;

		try {
			CSVParser analyizeCsvData = new CSVParser(new FileReader(filePathToData), CSVFormat.DEFAULT.withHeader());
			numElementsInHeader = analyizeCsvData.getHeaderMap();
			numberOfRecorsInFile = analyizeCsvData.getRecords();
			/*
			 * i recognise that numrows method reads through the csv file first in order to return the number of rows.
			 * there's no other way i can think of on how we can get the fraction to be read first without having to parse
			 * the file atleast once. we need to know the number of records in order to determine the fraction that will be used
			 * to determine the first guess for the records.. csvParser has nothing else... tried to look into another parser
			 * most of them use csvparser under the hood...eg opencsv
			*/
			
			numberOfColumns = numElementsInHeader.size();
			numberOfRows = numberOfRecorsInFile.size();
			
			numElementsInHeader.clear();
//			numberOfRecorsInFile.clear();
			
			fractionOfRecordsInFirstGroup = numberOfRows / fractionOfRecordsInFirstGroup;
			
			/*
			 * have a while loop for second thread to skip particular data ---- didn't know how to get the iterator to start from a particular pstn -- help (?)
			 * 
			 * for (int i = 0; i < numToSkip; i++) { if (iterator.hasNext()) {
			 * iterator.next(); }
			 * 
			 * }
			 */
			
			for (CSVRecord row : analyizeCsvData) {
				if (threadNum != firstThread && count == 0) {
					for (int i = 0; i < fractionOfRecordsInFirstGroup; i++) {
						
						row.iterator();
					}
				}
				
				if(threadNum == 0){
					if (count == fractionOfRecordsInFirstGroup) {
						analyizeCsvData.close();
						break;
					}
					else {
						++count;
					}
					
				}
				for (int col = 0; col < numberOfColumns; col++) {
					datatypesMatched = matchDataToRegex(row.get(col));

					for (int i = 0; i < datatypesMatched.size(); i++) {
						if (listOfDataTypesPerColumn.get(col).containsKey(datatypesMatched.get(i))) {
							listOfDataTypesPerColumn.get(col).put(datatypesMatched.get(i),
									listOfDataTypesPerColumn.get(col).get(datatypesMatched.get(i)) + 1);
						} else {
							listOfDataTypesPerColumn.get(col).put(datatypesMatched.get(i), 1);
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

	public static List<String> matchDataToRegex(String dataFromOneColumnCell) {
		int regex = 1;
		int dataTypeOfRegex = 0;
		List<String> datatypesMatched = null;
		
		/*create an exception for if the type found is integer and it also matched to integer and float, 
		 * then increase the count for an integer and increase the count for a float
		 * 
		 * if the type found is for float, and integer and float, then increase the count for only
		 * floats
		*/
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
