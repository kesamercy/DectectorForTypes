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

import javax.swing.LayoutFocusTraversalPolicy;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.omg.IOP.TaggedComponentHelper;

public class DetectDataTypes {

	public enum DataTypes {
		INTEGER, FLOAT, DATE, BOOLEAN, TIME, STRING;
	}

	public static String regexFilePath = "none yet";
	public static int numToDivideDataIntoTwoGroups = 0;
	public static final int GROUPSOFDATA = 2;

	public static void main(String[] args) throws IOException {
		String filePathtoData = args[0];
		String stringOfnumToDivideDataIntoTwoGroups = args[1];
		regexFilePath = args[2];
		numToDivideDataIntoTwoGroups = Integer.parseInt(stringOfnumToDivideDataIntoTwoGroups);

		guessDataTypesInEachColumn(filePathtoData);

		// implement with for loop for the second thread to skip the numb. records to
		// skip

		// then start reading the records from the suggested number

		

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//compare the results in the collectionOfDataTypesPerGroupOfData and return the greatest or not
		
	}// end findDataTypes

	public static List<Map<String, Integer>> findDataTypesPerColumn(String filePathToData, int threadNum) {
		int numColsInFile = 0;
		int count = 0;
		int numElementsInFirstGroupOfData = 0;
		Map<String, Integer> numElementsInHeader;
		List<String> datatypesMatched = null;
		List<Map<String, Integer>> listOfDataTypesPerColumn = null;

		try {
			CSVParser analyizeCsvData = new CSVParser(new FileReader(filePathToData), CSVFormat.DEFAULT.withHeader());
			numElementsInHeader = analyizeCsvData.getHeaderMap();
			numColsInFile = numElementsInHeader.size();
			numElementsInHeader.clear();
			
			//think about how to find the num of cols in the file
			
			//get the threads to only read specified data per thread
			
			/*
			 * have a while loop for second thread to skip particular data ---- didn't know how to get the iterator to start from a particular pstn -- help (?)
			 * 
			 * for (int i = 0; i < numToSkip; i++) { if (iterator.hasNext()) {
			 * iterator.next(); }
			 * 
			 * }
			 */
			
			for (CSVRecord row : analyizeCsvData) {
				for (int col = 0; col < numColsInFile; col++) {
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
				if (threadNum != 1) {
					//increment postn of record
				}
				else {
					if (count == numElementsInFirstGroupOfData) {
						//close the reader here
					}
					++count;
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
