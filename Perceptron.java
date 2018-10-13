import java.io.*;
import java.util.*;
import java.lang.*;

public class Perceptron{
	private PerceptronSettings p_settings;
	private TrainingSet trainingSet;

	public Perceptron(PerceptronSettings p_settings){
		this.p_settings = p_settings;
		p_settings.printNetInitializationSettings();

		//Set training file
		if(p_settings.hasTrainingFile()) {
			trainingSet = new TrainingSet(p_settings.getTrainingFile(), p_settings.initializeWithRandomWeights());
		} else {
			trainingSet = null;
		}
	}

	public void trainNet(){
		Pattern[] patternSet = trainingSet.getPatternSet();	
		double[] weights = trainingSet.getWeights();
		int[] calculatedOutput = new int[trainingSet.getOutputPatternSize()];

		//Run training algorithm
		int currentEpoch = 0;
		while(currentEpoch < p_settings.getMaxEpochs()){
			for(int j = 0; j < trainingSet.getOutputPatternSize(); j++){
				//Compute activation for single unit
				int yin = computeYin(j);
				int output = -1;
				if(yin > p_settings.getThresholdTheta()){
					output = 1;	
				} else if(yin < p_settings.getThresholdTheta()){
					output = -1;
				} else {
					output = 0;
				}
				calculatedOutput[j] = output;

				//Update weights
				if(output != patternSet[j].outputAt(j)){
					//Update bias

					//Update weight
					
				}
			}
		}
	}

	public void deployNet(){

	}

	private int computeYin(int j){
		return -1;
	}
}
