import java.io.*;
import java.util.*;
import java.lang.*;

public class PatternSet{

	private String trainingFile, deploymentFile;
	private Pattern[] trainingSet;
	private Pattern[] deploymentSet;
	private double[] weights;
	private boolean isTrainingSet;

	public PatternSet(String file, boolean trainingSet){
		if(trainingSet){
			this.trainingFile = file;
			this.isTrainingSet = true;
			this.deploymentFile = null;

			//Load training set
			loadTrainingSet();
			printTrainingSet();
		} else {
			this.trainingFile = null;
			this.isTrainingSet = false;
			this.deploymentFile = file;
			this.weights = null;
		}


	}

	public Pattern[] getPatternSet(){	
		return trainingSet;
	}

	public boolean isTrainingSet(){
		return isTrainingSet;
	}
	
	public double[] getWeights(){
		return weights;
	}

	private void loadTrainingSet(){
		BufferedReader reader = null;
		String line = "";

		//Parse training set
		try {
			reader = new BufferedReader (new FileReader (trainingFile));
			int firstThreeLines = 0;

			//Read first three lines
			int inputPatternSize = Integer.parseInt(reader.readLine());
			int outputPatternSize = Integer.parseInt(reader.readLine());
			int numberOfTrainingPatterns = Integer.parseInt(reader.readLine());

			//Setup training set
			weights = new double[inputPatternSize];
			trainingSet = new Pattern[numberOfTrainingPatterns];

			//Begin reading training pairs
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
						trainingSet[trainingSetIndex]  = p1;
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
		} catch (Exception e) {
			System.out.println("Error parsing file. " + e);
		}
	}

	public void printTrainingSet(){
		if(trainingSet != null){
			System.out.println("--- Training Set ---");
			for(int i = 0; i < trainingSet.length; i++){
				trainingSet[i].printPair();
			}
		}
	}


}
