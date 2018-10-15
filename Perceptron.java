import java.io.*;
import java.util.*;
import java.lang.*;

public class Perceptron {
	private PerceptronSettings p_settings;
	private TrainingSet trainingSet;
	private DeploymentSet deploymentSet;
	private boolean verboseTrain;
	private String verboseTrainFileOutput;

	public Perceptron(PerceptronSettings p_settings, boolean verboseTrain) {
		this.p_settings = p_settings;
		this.verboseTrain = verboseTrain;
		p_settings.printNetInitializationSettings();

		// Set training file
		if (p_settings.hasTrainingFile()) {
			trainingSet = new TrainingSet(p_settings.getTrainingFile(), p_settings.initializeWithRandomWeights());
		} else {
			trainingSet = null;
		}
		
		deploymentSet = null;
	}

	public void trainNet() {
		// Setup
		Pattern[] patternSet = trainingSet.getPatternSet();
		int currentEpoch = 0;
		boolean converged = false;
		boolean weightsChanged = false;
		boolean weightChangeThresholdHit = false;
		Pattern currentTrainingPattern = null;
		
		// Run training algorithm
		while (currentEpoch < p_settings.getMaxEpochs() && !converged && !weightChangeThresholdHit) {
			
			// Compute and train a single pattern
			for (int patternIndex = 0; patternIndex < trainingSet.getNumberOfPatterns(); patternIndex++) {
				currentTrainingPattern = patternSet[patternIndex];
				if(verboseTrain) trainingSet.sendIncrementStatus(currentEpoch, patternIndex);
				
				// Compute activation for single output neuron
				for (int outputNeuron = 0; outputNeuron < trainingSet.getOutputPatternSize(); outputNeuron++) {
					
					int output = computeActivation(computeYin(currentTrainingPattern, outputNeuron));

					// Check if weights need updating
					if (output != currentTrainingPattern.outputAt(outputNeuron)) {
						if(verboseTrain) trainingSet.sendBeginStatus(outputNeuron);		//Log changes
						weightsChanged = true;
						trainingSet.updateBiasWeight(outputNeuron, calculateNewBiasWeight(currentTrainingPattern, outputNeuron));				//Update bias
						for (int inputNeuron = 0; inputNeuron < trainingSet.getInputPatternSize(); inputNeuron++) {
							trainingSet.updateInputWeightForIndex(outputNeuron, inputNeuron, calculateNewWeight(currentTrainingPattern, inputNeuron, outputNeuron));		//Update input weights
						}
						if(verboseTrain) trainingSet.sendEndStatus();
					} 
				}
			}
			
			//End of epoch
			currentEpoch++;
			
			//Check if should run another epoch
			if (!weightsChanged || trainingSet.getMaxWeightChange() < p_settings.getWeightChangesThreshold()) {
				converged = true;
				weightChangeThresholdHit = true;
			} else {
				weightsChanged = false;
				trainingSet.resetMaxWeightChange();
			}
		}

		if (!converged) {
			System.out.println("Reached max epochs and failed to converge");
		} else {
			System.out.println("Converged after " + currentEpoch + " epochs");
		}
        trainingSet.weightsWriter(p_settings.getWeightsFile(), p_settings);		
		if(verboseTrain) trainingSet.finalizeResults(p_settings.getTrainingFile());
	}

	public void deployNet() {
		if(deploymentSet == null) {
			if(!p_settings.hasDeploymentFile()) p_settings.setDeploymentFile();
			deploymentSet = new DeploymentSet(p_settings.getDeploymentFile());
		}
		Pattern [] patternSet = deploymentSet.getPatternSet();
		int [] classification = new int[deploymentSet.getOutputPatternSize()];
		
		//Classify each pattern in deployment set
		for (int patternIndex = 0; patternIndex < deploymentSet.getNumberOfPatterns(); patternIndex++) {
			
			//Classify a single pattern
			for (int output = 0; output < deploymentSet.getOutputPatternSize(); output++) {
				// Compute yin for each output neuron
				double yin = computeYin(patternSet[patternIndex], output);
				int outputActivated = computeActivation(yin);
				classification[output] = outputActivated;
			}
			System.out.print("Classification for pattern " + patternIndex + " = [");
			for(int i = 0; i < classification.length; i++) {
				System.out.print(classification[i] + " ");
			}
			System.out.println("]");

			//Verify classification of pattern
			deploymentSet.isClassifiedCorrectly(classification, patternIndex);	
		}
		System.out.println("Correclty classified:" + deploymentSet.numCorrectlyClassifiedPatterns() + "\nCorrectly classified:" + deploymentSet.numIncorrecltyClassifiedPatterns());
	}

	public void setWeightsFromFile(){
		trainingSet = new TrainingSet(p_settings.getWeightsFile());
	}
	
	private double calculateNewWeight(Pattern p, int sampleNeuron, int outputNeuron) {
		double oldWeight = trainingSet.getWeightsForOutputAt(outputNeuron, sampleNeuron);
		int patternInput = p.inputAt(sampleNeuron);
		int patternOutput = p.outputAt(outputNeuron);
		double newWeight = oldWeight + (p_settings.getLearningRate() * patternOutput * patternInput);
		return newWeight;
	}
	
	private double calculateNewBiasWeight(Pattern p, int outputNeuron) {
		double oldBiasWeight = trainingSet.getBiasWeight(outputNeuron);
		double learningRate = p_settings.getLearningRate();
		int patternOutput = p.outputAt(outputNeuron);
		double newBiasWeight = oldBiasWeight + (learningRate * patternOutput);
		return newBiasWeight;
	}

	private int computeActivation(double yin) {
		int output = -1;
		if (yin > p_settings.getThresholdTheta()) {
			output = 1;
		} else if (yin < p_settings.getThresholdTheta()) {
			output = -1;
		} else {
			output = 0;
		}
		
		if(verboseTrain) trainingSet.sendActivationStatus(yin, output);
		
		return output;
	}
	
	private double computeYin(Pattern p, int j) {

		int position = -1;
		int rowsOfInput = trainingSet.getInputPatternSize() / trainingSet.getOutputPatternSize();
		double[][] weights = trainingSet.getWeights();
		double sum = trainingSet.getBiasWeight(j);
		for (int i = 0; i < trainingSet.getInputPatternSize(); i++) {
			try {
				sum += weights[j][i] * p.inputAt(i);
			} catch (Exception e) {
				System.out.println("Error adding to sum at indexes: input[" + i + "] output[" + j + "]");
			}
		}
		return sum;
	}
}
