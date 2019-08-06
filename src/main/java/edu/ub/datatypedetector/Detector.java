package edu.ub.datatypedetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Detector {
	private String regexFilePath;
	private final int GROUPSOFDATA = 2;
	private String filePathToData;
	private int threadId;
	private int numTimesFunctionHasExecuted;
	private int numberOfColumns;
	private int firstThread;
	private List<Map<String, Integer>> dataTypesFromAllColumns;
	private ArrayList<List<Map<String, Integer>>> dataTypesFoundFromDataProvided;
	private ArrayList<List<Map<String, Integer>>> dominantDataTypesFromDataProvided;
	private Thread[] threadPerDataGroup;
	private Map<String, Integer> numElementsInHeader;
	private List<String> datatypesMatched;

	

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
