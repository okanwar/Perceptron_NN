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
		deploymentSet = null;
	}

	public void trainNet() {
		//Create and validate training set
		trainingSet = new TrainingSet(p_settings);
		if (!validateTrainingVariables()) {
			System.exit(-1);
		} else {
			p_settings.setTrainingSet(trainingSet);
		}

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
				if (verboseTrain)
					trainingSet.sendIncrementStatus(currentEpoch, patternIndex);

				// Compute activation for single output neuron
				for (int outputNeuron = 0; outputNeuron < trainingSet.getOutputPatternSize(); outputNeuron++) {

					int output = computeActivation(computeYin(currentTrainingPattern, outputNeuron, true));

					// Check if weights need updating
					if (output != currentTrainingPattern.outputAt(outputNeuron)) {
						if (verboseTrain)
							trainingSet.sendBeginStatus(outputNeuron); // Log changes
						weightsChanged = true;
						trainingSet.updateBiasWeight(outputNeuron,
								calculateNewBiasWeight(currentTrainingPattern, outputNeuron)); // Update bias
						for (int inputNeuron = 0; inputNeuron < trainingSet.getInputPatternSize(); inputNeuron++) {
							trainingSet.updateInputWeightForIndex(outputNeuron, inputNeuron,
									calculateNewWeight(currentTrainingPattern, inputNeuron, outputNeuron)); // Update input weights													
						}
						if (verboseTrain)
							trainingSet.sendEndStatus();
					}
				}
			}

			// End of epoch
			currentEpoch++;

			// Check if should run another epoch
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
		trainingSet.weightsWriter(p_settings.getWeightsFile());
		if (verboseTrain)
			trainingSet.finalizeResults(p_settings.getTrainingFile());
	}

	public void deployNet() {
		//Create and validate deployment set
		deploymentSet = new DeploymentSet(p_settings);
		if (!validateDeploymentVariables())
			System.exit(-1);

		Pattern[] patternSet = deploymentSet.getPatternSet();
		int[] classification = new int[deploymentSet.getOutputPatternSize()];

		// Classify each pattern in deployment set
		for (int patternIndex = 0; patternIndex < deploymentSet.getNumberOfPatterns(); patternIndex++) {

			// Classify a single pattern
			for (int output = 0; output < deploymentSet.getOutputPatternSize(); output++) {
				// Compute yin for each output neuron
				double yin = computeYin(patternSet[patternIndex], output, false);
				int outputActivated = computeActivation(yin);
				classification[output] = outputActivated;
			}
			System.out.print("Classification for pattern " + patternIndex + " = [");
			for (int i = 0; i < classification.length; i++) {
				System.out.print(classification[i] + " ");
			}
			System.out.println("]");

			// Verify classification of pattern
			deploymentSet.isClassifiedCorrectly(classification, patternIndex);
		}
		System.out.println("Correclty classified:" + deploymentSet.numCorrectlyClassifiedPatterns()
				+ "\nIncorrectly classified:" + deploymentSet.numIncorrecltyClassifiedPatterns());
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

		if (verboseTrain)
			trainingSet.sendActivationStatus(yin, output);

		return output;
	}

	private double computeYin(Pattern p, int j, boolean training) {

		int rowsOfInput =  -1;
		int inputPatternSize = -1;
		double[][] weights = null;
		double sum = -1;
		
		//Set values according to train/deploy
		if(training){
			inputPatternSize = trainingSet.getInputPatternSize();
			rowsOfInput = trainingSet.getInputPatternSize() / trainingSet.getOutputPatternSize();
			weights = trainingSet.getWeights();
			sum = trainingSet.getBiasWeight(j);
		} else {
			inputPatternSize = deploymentSet.getInputPatternSize();
			rowsOfInput = deploymentSet.getInputPatternSize() / deploymentSet.getOutputPatternSize();
			weights = deploymentSet.getWeights();
			sum = deploymentSet.getBiasWeight(j);
		}
		
		//Run yin calculation
		for (int i = 0; i < inputPatternSize; i++) {
			try {
				sum += weights[j][i] * p.inputAt(i);
			} catch (Exception e) {
				System.out.println("Error adding to sum at indexes: input[" + i + "] output[" + j + "]");
			}
		}
		return sum;
	}

	private boolean validateTrainingVariables() {
		// p_settings
		if (p_settings == null) {
			System.out.println("Missing Perceptron settings.");
			return false;
		}

		// Training set
		if (p_settings.getTrainingFile() != null) {
			if (trainingSet == null) {
				System.out.println("Missing training set.");
				return false;
			} else {
				if(!trainingSet.setInitialized()) {
					System.out.println("Training set failed to initalize");
					return false;
				}
			}
		} else {
			System.out.println("Missing training file.");
			return false;
		}

		// Initialized weights
		if (trainingSet.getBiasWeights() == null || trainingSet.getWeights() == null) {
			System.out.println("Weights not initialized.");
			return false;
		}

		// Epochs set
		if (p_settings.getMaxEpochs() < 1) {
			System.out.println("Max epochs not set:" + p_settings.getMaxEpochs());
			return false;
		}

		// Trained weights file
		if (p_settings.getWeightsFile() == null) {
			System.out.println("Missing trained weights file.");
			return false;
		}

		// Learning rate
		if (p_settings.getLearningRate() <= 0) {
			System.out.println("Learning rate not set:" + p_settings.getLearningRate());
			return false;
		}

		// Threshold theta
		if (p_settings.getThresholdTheta() < 0) {
			System.out.println("Threshold theta not set:" + p_settings.getThresholdTheta());
			return false;
		}

		// Threshold weight changes
		if (p_settings.getWeightChangesThreshold() < 0) {
			System.out.println("Threshold weight changes not set:" + p_settings.getWeightChangesThreshold());
			return false;
		}
		return true;
	}

	private boolean validateDeploymentVariables() {
		// p_settings
		if (p_settings == null) {
			System.out.println("Missing perceptron settings.");
			return false;
		}

		// Deployment set
		if (p_settings.getDeploymentFile() != null) {
			if (deploymentSet == null) {
				System.out.println("Missing deployment set.");
				return false;
			} else {
				if(!deploymentSet.setInitialized()) {
					System.out.println("Deployment set failed to initalize.");
					return false;
				}
			}
		} else {
			System.out.println("Missing deployment file.");
			return false;
		}
		
		//Weights

		return true;
	}
}
