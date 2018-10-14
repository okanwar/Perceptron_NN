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
		BufferedWriter writer = null;
		String fileOutput = "";

		// Create file if verbose train
		if (verboseTrain) {
			try {
				writer = new BufferedWriter(new FileWriter(
						p_settings.getTrainingFile() + "_" + p_settings.getMaxEpochs() + "_epoch_training.txt"));

			} catch (Exception e) {
				System.out.println("Failed to create training output file");
			}
		}

		// Run training algorithm
		int currentEpoch = 0;
		boolean converged = false;
		boolean weightsChanged = false;
		while (currentEpoch < p_settings.getMaxEpochs() && !converged) {
			if (verboseTrain) {
				fileOutput += " -- Epoch " + currentEpoch + "\n";
			}

			// Compute and classify a single pattern
			for (int patternIndex = 0; patternIndex < trainingSet.getNumberOfPatterns(); patternIndex++) {

				// Compute activation for single unit
				for (int j = 0; j < trainingSet.getOutputPatternSize(); j++) {
					double yin = computeYin(patternSet[patternIndex], j);

					// Get Status for verbose train
					if (verboseTrain) {
						fileOutput += DIVIDER + "\n\n" + DIVIDER + "\t--- Pattern " + patternIndex + " Output Pattern "
								+ j + " Epoch " + currentEpoch + "---\n" + DIVIDER + "\tYin = " + yin + "\n"
								+ "\tThreshold theta = " + p_settings.getThresholdTheta() + "\n";
					}

					// Calculate activation for neuron
					int output = -1;
					if (yin > p_settings.getThresholdTheta()) {
						output = 1;
					} else if (yin < p_settings.getThresholdTheta()) {
						output = -1;
					} else {
						output = 0;
					}
					calculatedOutput[j] = output;

					// Get changes for verbose train
					if (verboseTrain) {
						fileOutput += "\n\tOutput = " + output + " Target = " + patternSet[j].outputAt(j) + "\n";
					}

					// Update weights
					double[] neuronWeights = trainingSet.getWeightsForOutput(j);

					// Check if weights need updating
					int target = patternSet[patternIndex].outputAt(j);
					if (output != patternSet[patternIndex].outputAt(j)) {
						weightsChanged = true;
						// Update bias for single output neuron
						double oldBiasWeight = trainingSet.getBiasWeight(j);
						double newBiasWeight = oldBiasWeight
								+ (p_settings.getLearningRate() * patternSet[patternIndex].outputAt(j));
						trainingSet.updateBiasWeight(j, newBiasWeight);

						// Get changes for verbose train
						if (verboseTrain) {
							fileOutput += "\n\t--- Updating Weights --- \n" + DIVIDER + "\tBias Weight "
									+ String.format("Old bias:%10f New Bias:%10f\n", oldBiasWeight, newBiasWeight)
									+ String.format("\t[%8s]%10s%10s", "Index", " Old Weight ", " New Weight ") + "\n";
						}

						// Update weight after computation of single output neuron
						double newWeight = -1;
						for (int sampleIndex = 0; sampleIndex < trainingSet.getInputPatternSize(); sampleIndex++) {
							// Find new weights for each sample to single output neuron
							double oldWeight = neuronWeights[sampleIndex];
							newWeight = oldWeight + (p_settings.getLearningRate() * patternSet[patternIndex].outputAt(j)
									* patternSet[patternIndex].inputAt(sampleIndex));
							trainingSet.updateInputWeightForIndex(j, sampleIndex, newWeight);

							// Print
							if (verboseTrain) {
								fileOutput += "\t[" + String.format("%8d", sampleIndex) + "]" + " "
										+ String.format("%9f%10f", oldWeight, newWeight) + "\n";
							}
						}

					} else {
//						System.out.println("Did not updated weights with output:" + output + " and target:" + patternSet[j].outputAt(j) + " for a pattern " + patternIndex + " and output " + j);
					}
				}
//				System.out.println();
				if (verboseTrain) {
					fileOutput += DIVIDER + "\n\n";
				}
			}
			currentEpoch++;
			if (verboseTrain) {
				fileOutput += DIVIDER + "\n";
			}
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

		// Close file for verbose train
		if (verboseTrain) {
			try {
				writer.write(fileOutput);
				writer.close();
			} catch (Exception e) {
				System.out.println("Error closing writer");
			}
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
