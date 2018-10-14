import java.io.*;
import java.util.*;
import java.lang.*;

public class Perceptron {
	private static final String DIVIDER = "----------------------------------------------------------\n";
	private PerceptronSettings p_settings;
	private TrainingSet trainingSet;
	private TrainingSet deploymentSet;
	private boolean verboseTrain;

	public Perceptron(PerceptronSettings p_settings, boolean verboseTrain) {
		this.p_settings = p_settings;
		this.verboseTrain = verboseTrain;
		p_settings.printNetInitializationSettings();

		// Set training file
		if (p_settings.hasTrainingFile()) {
			System.out.println("CREATING WITH RANDOM WEIGHTS:" + p_settings.initializeWithRandomWeights());
			trainingSet = new TrainingSet(p_settings.getTrainingFile(), p_settings.initializeWithRandomWeights());
		} else {
			trainingSet = null;
		}
	}

	public void trainNet() {
		// Setup
		Pattern[] patternSet = trainingSet.getPatternSet();
		double[][] weights = trainingSet.getWeights();
		int[] calculatedOutput = new int[trainingSet.getOutputPatternSize()];


		// Run training algorithm
		int currentEpoch = 0;
		boolean converged = false;
		boolean weightsChanged = false;
		
		while (currentEpoch < p_settings.getMaxEpochs() && !converged) {

			// Compute and classify a single pattern
			for (int patternIndex = 0; patternIndex < trainingSet.getNumberOfPatterns(); patternIndex++) {

				// Compute activation for single unit
				for (int j = 0; j < trainingSet.getOutputPatternSize(); j++) {
					
					int output = computeActivation(computeYin(patternSet[patternIndex], j));
					calculatedOutput[j] = output;

					// Update weights
					double[] neuronWeights = trainingSet.getWeightsForOutput(j);

					// Check if weights need updating
					if (output != patternSet[patternIndex].outputAt(j)) {
						weightsChanged = true;
						// Update bias for single output neuron
						double oldBiasWeight = trainingSet.getBiasWeight(j);
						double learningRate = p_settings.getLearningRate();
						int patternOutput = patternSet[patternIndex].outputAt(j);
						double newBiasWeight = oldBiasWeight + (learningRate * patternOutput);
						trainingSet.updateBiasWeight(j, newBiasWeight);

						// Update weight after computation of single output neuron
						double newWeight = -1;
						for (int sampleIndex = 0; sampleIndex < trainingSet.getInputPatternSize(); sampleIndex++) {
							// Find new weights for each sample to single output neuron
							double oldWeight = neuronWeights[sampleIndex];
							int patternInput = patternSet[patternIndex].inputAt(sampleIndex);
//							int patternOutput = patternSet[patternIndex].outputAt(j);
							newWeight = oldWeight + (p_settings.getLearningRate() * patternOutput * patternInput);
							trainingSet.updateInputWeightForIndex(j, sampleIndex, newWeight);
						}
					} 
				}
			}
			currentEpoch++;
			if (!weightsChanged) {
				converged = true;
			} else {
				weightsChanged = false;
			}
		}

		if (!converged) {
			System.out.println("Reached max epochs and failed to converge");
		} else {
			System.out.println("Converged after " + currentEpoch + " epochs");
		}
	}

	public void deployNet() {
		Pattern [] patternSet = deploymentSet.getPatternSet();
		int [] classification = new int[deploymentSet.getOutputPatternSize()];
		
		//Classify each pattern in deployment set
		for (int patternIndex = 0; patternIndex < deploymentSet.getInputPatternSize(); patternIndex++) {
			
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
		}
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
