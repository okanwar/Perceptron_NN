import java.io.*;
import java.util.*;
import java.lang.*;

public class PatternSet{

	protected String dataFile;
	protected Pattern[] patternSet;
	protected int inputPatternSize, outputPatternSize, numberOfTrainingPatterns;
	protected boolean setInitialized;

	public PatternSet(String file){
		this.dataFile = file;
		patternSet = null;
		setInitialized = false;
		if(file != null) loadSetFromFile();
	}

	public Pattern[] getPatternSet(){	
		return patternSet;
	}

	public int getOutputPatternSize(){
		return outputPatternSize;
	}

	public int getInputPatternSize(){
		return inputPatternSize;
	}

	public int getNumberOfPatterns(){
		return numberOfTrainingPatterns;
	}
	
	public void printSet(){
		if(patternSet != null){
			System.out.println("--- " + dataFile + "---");
			for(int i = 0; i < patternSet.length; i++){
				patternSet[i].printPair(); 
			}
		}
	}
	
	private void loadSetFromFile(){
		BufferedReader reader = null;
		String line = "";

		//Parse set
		try {
			reader = new BufferedReader (new FileReader (dataFile));
			int firstThreeLines = 0;

			//Read first three lines
			inputPatternSize = Integer.parseInt(reader.readLine().trim());
			outputPatternSize = Integer.parseInt(reader.readLine().trim());
			numberOfTrainingPatterns = Integer.parseInt(reader.readLine().trim());

			//Setup set
			patternSet = new Pattern[numberOfTrainingPatterns];

			//Begin reading samples
			int linesOfInputPattern = inputPatternSize / outputPatternSize; 
			int sampleIndex = 0;
			int trainingSetIndex = 0;
			String inputPattern = "";
			String outputPattern = "";
			String classification = "";

			//Read a single sample			
			while ((line = reader.readLine()) != null){
				if( !line.isEmpty() ){
					if(sampleIndex < linesOfInputPattern){
						//Reading input pattern
						inputPattern += line + " ";
					} else if(sampleIndex == linesOfInputPattern){
						//Reading output pattern
						outputPattern = line + " ";
					} else {
						//Reading classification
						classification = line;

						//Create new training pair
						Pattern p1 = new Pattern(inputPatternSize, outputPatternSize);
						p1.getStrings(inputPattern, outputPattern, classification);
						patternSet[trainingSetIndex]  = p1;
						trainingSetIndex++;

						//Reset
						sampleIndex = 0;
						inputPattern = "";
						outputPattern = "";

						continue;
					}
					sampleIndex++;
				}
			}
			setInitialized = true;
		} catch (FileNotFoundException f) {
			System.out.println("Could not create pattern set for file:" + dataFile);
		} catch (Exception e) {
			System.out.println("Error parsing file. " + e);
		}
	}
}
