import java.io.*;
import java.util.*;
import java.lang.*;

public class TrainingSet{

	private String trainingFile, deploymentFile;
	private TrainingPair[] trainingSet;
	private boolean setFull;
	
	public TrainingSet(String trainingFile, String deploymentFile){
		this.trainingFile = trainingFile;
		this.deploymentFile = deploymentFile;

		if(trainingFile != null){
			loadTrainingSet();
			printTrainingSet();
		}
	}

	public void deployNet(){

	}

	public void trainNet(){

	}

	private void loadTrainingSet(){
		BufferedReader reader = null;
		String line = "";

		//Parse training set
		try {
			reader = new BufferedReader (new FileReader (trainingFile));
			int firstThreeLines = 0;
			int [] sampleSettings = new int[3];

			//Read first three lines
			for(int i= 0; i < 3; i++){
				sampleSettings[i] = Integer.parseInt(reader.readLine());
			}

			//Setup training set
			trainingSet = new TrainingPair[sampleSettings[2]];

			//Begin reading training pairs
			int linesOfInputPattern = sampleSettings[0] / sampleSettings[1];
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
						TrainingPair p1 = new TrainingPair(sampleSettings[0], sampleSettings[1]);
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
