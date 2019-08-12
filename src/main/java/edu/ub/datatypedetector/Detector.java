package edu.ub.datatypedetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Detector {
	public String regexFilePath;
	public final int GROUPSOFDATA = 2;
	public String filePathToData;
	public int threadId;
	public int numTimesFunctionHasExecuted;
	public int numberOfColumns;
	public int firstThread;
	public List<Map<String, Integer>> dataTypesFromAllColumns;
	public ArrayList<List<Map<String, Integer>>> dataTypesFoundFromDataProvided;
	public ArrayList<List<Map<String, Integer>>> dominantDataTypesFromDataProvided;
	public Thread[] threadPerDataGroup;
	public Map<String, Integer> numElementsInHeader;
	public List<String> datatypesMatched;
	public int divideInHalf;
	public int numColumnsToCheck;

// take out the thread Id's.
	// think of what functions to use ---- ask yourself why i'm writing this? what is the end goal when you are writing the code/
	//specify why that exists...


	//not just the varaibles but also the code that is associated with the varaibles should change... research on how and why the varaibles should be declared in a class and not the way.... the problems of having static variables... etc.


	//avoid static methods in java.... static methods are usedulf for code that doesn't need to

	//make sure the functions are --- look at the encapsualtion and understand this concept in regard to

	//what are the components and what are there responsibilties. think about this for the functions
//
//

	// put the code that deals with type detection in one class and put the code that deals with the declaring of varaibles in another class

	public Detector() {
		regexFilePath = "none yet";
		filePathToData = "none yet";
		threadId = 0;
		numTimesFunctionHasExecuted = 0;
		numberOfColumns = 0;
		firstThread = 0;
		numElementsInHeader = new HashMap<String, Integer>();
		datatypesMatched = new ArrayList<String>();
		dataTypesFoundFromDataProvided = new ArrayList<List<Map<String, Integer>>>();
		dominantDataTypesFromDataProvided = new ArrayList<List<Map<String, Integer>>>();
		threadPerDataGroup = new Thread[GROUPSOFDATA];
		dataTypesFromAllColumns = new ArrayList<Map<String, Integer>>();
		divideInHalf = 2;
		numColumnsToCheck = numberOfColumns / divideInHalf;

	}

	public Detector(String filePathToData, String regexFilePath) {
		this.regexFilePath = regexFilePath;
		this.filePathToData = filePathToData;
		threadId = 0;
		numTimesFunctionHasExecuted = 0;
		numberOfColumns = 0;
		firstThread = 0;
		numElementsInHeader = new HashMap<String, Integer>();
		datatypesMatched = new ArrayList<String>();
		dataTypesFoundFromDataProvided = new ArrayList<List<Map<String, Integer>>>();
		dominantDataTypesFromDataProvided = new ArrayList<List<Map<String, Integer>>>();
		threadPerDataGroup = new Thread[GROUPSOFDATA];
		dataTypesFromAllColumns = new ArrayList<Map<String, Integer>>();
		divideInHalf = 2;
		numColumnsToCheck = numberOfColumns / divideInHalf;

	}

	public int getSizeOfListOfDataTypesMatched() {
		return datatypesMatched.size();
	}
	public List<String> getListOfDataTypesMatched() {
		return datatypesMatched;
	}
	public int getFirstThread() {
		return firstThread;
	}
	public int getThreadId() {
		return threadId;
	}
	public int getNumTimesFunctionHasExecuted() {
		return numTimesFunctionHasExecuted;
	}
	public String getRegexFilePath() {
		return regexFilePath;
	}
	public String getFilePathToData() {
		return filePathToData;
	}
	public int getSizeOfThreadPerDataGroup() {
		return threadPerDataGroup.length;
	}
	public int getNewThreadId() {
		return ++threadId;
	}
	public Thread[] getThreadInDataGroup() {
		return threadPerDataGroup;
	}
	public Thread getLastThreadInGroupOfThreads() {
		return getThreadInDataGroup()[getSizeOfThreadPerDataGroup() - 1];
	}
	public int getNumOfColsInCsv() {
		return numberOfColumns;
	}
	public ArrayList<List<Map<String, Integer>>> getDataTypesFoundFromDataProvided() {
		return dataTypesFoundFromDataProvided;
	}
	public ArrayList<List<Map<String, Integer>>> getDominantDataTypesFromDataProvided() {
		return dominantDataTypesFromDataProvided;
	}
	public List<Map<String, Integer>> getdataTypesFromAllColumns() {
		return dataTypesFromAllColumns;
	}

	public void addToTheDominantTypesOfDataProvided(List<Map<String, Integer>> newDominantTypes) {
		dominantDataTypesFromDataProvided.add(newDominantTypes);
	}
	public void addTypesToListOfDataTypesMatched(List<String> newDatatypesMatched) {
		this.datatypesMatched = newDatatypesMatched;
	}
	public void addToTheListOfDataTypesOfAllColumns(Map<String, Integer> dataTypesFromOneColumn) {
		this.dataTypesFromAllColumns.add(dataTypesFromOneColumn);
		System.out.println(dataTypesFromAllColumns);
		System.out.println(dataTypesFromAllColumns.size());

	}

	public void addDataTypesFromAllColumnsToCollectionOfTypesFromDataProvided() {
		dataTypesFoundFromDataProvided.add(dataTypesFromAllColumns);
	}
	public void incrementNumTimesFunctionHasExcuted() {
		this.numTimesFunctionHasExecuted += 1;
	}
	public void incrementFirstThread() {
		this.firstThread += 1;
	}
	public void determineNumberOfColsInCsv(Map<String, Integer> elementsFromHeader) {
		numElementsInHeader = elementsFromHeader;
		this.numberOfColumns = numElementsInHeader.size();
		numElementsInHeader.clear();
	}


}
