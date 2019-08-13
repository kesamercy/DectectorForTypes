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

	public void determineNumberOfColsInCsv(Map<String, Integer> elementsFromHeader) {
		numElementsInHeader = elementsFromHeader;
		this.numberOfColumns = numElementsInHeader.size();
		numElementsInHeader.clear();
	}


}
