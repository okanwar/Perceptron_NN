import java.io.*;
import java.util.Random;
import java.util.StringTokenizer;

/*
 * TrainingSet
 * 
 * Defines a training set, this defines behavior for training and examining a pattern set
 * 
 * @author Michael Dana, Om Kanwar
 */

public class TrainingSet extends PatternSet {
	
	private double maxWeightChange;
	private String weightChanges;

	/*
	 * Constructor
	 * @parama ps The perceptron settings object the perceptron net was initialized with
	 */
	public TrainingSet(PerceptronSettings ps) {
		// Setup pattern set
		super(ps.getTrainingFile(), ps);

		// Training Set
		maxWeightChange = 0;
		weightChanges = DIVIDER + "Training Results\n" + DIVIDER;
	}
	
	/*
	 * getMaxWeightChange - Getter for max weight change
	 */
	public double getMaxWeightChange() {
		return maxWeightChange;
	}

	/*
	 * resetMaxWeightChange - Resets the max weight change to 0
	 */
	public void resetMaxWeightChange() {
		maxWeightChange = 0;
	}

	/*
	 * updateBiasWeight - updates the bias weight of a specific output neuron give the new weight
	 * @param outputNeuron The index of the ouput neuron to update
	 * @param newBias The new bias weight
	 */
	public void updateBiasWeight(int outputNeuron, double newBias) {
		double oldBiasWeight = biasWeights[outputNeuron];
		double weightChange = Math.abs(newBias - oldBiasWeight);
		if (weightChange > maxWeightChange)
			maxWeightChange = weightChange;
		biasWeights[outputNeuron] = newBias;
		
		//Log Changes
		weightChanges += String.format("%15s%15f%15f%15f ", "Bias", oldBiasWeight, biasWeights[outputNeuron],
				weightChange) + " | bnew = bold + lr*t\n";
	}

	/*
	 * updateInputWeightForIndex - Updates the weight of an input neuron to a specific ouptput neuron given the new weight
	 * @param outputNeuron The output neuron of the weight to update
	 * @param inputNeuron The input neuron of the weight to update
	 * @param newWeight The new weight to udate to
	 */
	public void updateInputWeightForIndex(int outputNeuron, int inputNeuron, double newWeight) {
		double oldWeight = weights[outputNeuron][inputNeuron];
		double weightChange = Math.abs(newWeight - oldWeight);
		if (weightChange > maxWeightChange)
			maxWeightChange = weightChange;
		weights[outputNeuron][inputNeuron] = newWeight;
		
		//Log changes
		weightChanges += String.format("%15s%15f%15f%15f ", "Input", oldWeight, newWeight, weightChange)
				+ " | Wnew = Wold + lr*t*x\n";
	}

	/*
	 * printTrainingSet - Prints the training set
	 */
	public void printTrainingSet() {
		printSet();
		printWeights();
	}

	/*
	 * printWeights - Prints the weights of the training set
	 */
	public void printWeights() {
		System.out.print("--- Weights ---" + "\n");
		for (int o = 0; o < outputPatternSize; o++) {
			System.out.print("OUTPUT[" + o + "]{");
			for (int i = 0; i < inputPatternSize; i++) {
				System.out.print(" " + weights[o][i] + " ");
			}
			System.out.println("}");
			System.out.println("Wbias: " + biasWeights[o] + "\n");
		}
	}

	/*
	 * sendIncrementStatus - A method that records a change in epoch for log file
	 */
	public void sendIncrementStatus(int epoch, int patternNumber) {
		weightChanges += "\n" + DIVIDER + "Epoch " + epoch + " Pattern " + patternNumber + "\n" + DIVIDER + "\n";
	}

	/*
	 * sendActivationStatus - A method that records the activation of an input sample for log file
	 */
	public void sendActivationStatus(double yin, double activation) {
		weightChanges += DIVIDER + "Activation\n" + DIVIDER + "Yin = " + yin + "\nActivation = " + activation + "\n";
	}

	/*
	 * sendBeginStatus - A method that logs the start of weight changes
	 */
	public void sendBeginStatus(int outputNeuron) {
		weightChanges += DIVIDER + "Weight Changes for Output Neuron " + outputNeuron + "\n" + DIVIDER
				+ String.format("%15s%15s%15s%15s\n", "Type", "Wold", "Wnew", "Change");
	}

	/*
	 * sendEndStatus - A method that logs the end of weights changes
	 */
	public void sendEndStatus() {
		weightChanges += DIVIDER + "\n";
	}

	/*
	 * finalizeResults - A method that records the logs of training to a log file
	 * @param file The name of the log file
	 */
	public void finalizeResults(String file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file + "_training_output.txt"));
			writer.write(weightChanges);
			writer.close();
		} catch (Exception e) {
			System.out.println("Error writing verbose train results");
		}
	}

	/*
	 * weightsWriter - A method that records the weights for the training set in a file
	 * @param input_file The name of the file to record weights
	 */
	public void weightsWriter(String input_file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(input_file));
			writer.write(Integer.toString(inputPatternSize) + "\n");
			writer.write(Integer.toString(outputPatternSize) + "\n");
			writer.write(Double.toString(p_settings.getThresholdTheta()) + "\n");
			for (int i = 0; i < outputPatternSize; i++) {
				writer.write(Double.toString(biasWeights[i]) + " \n");
				for (int j = 0; j < inputPatternSize; j++) {
					writer.write(Double.toString(weights[i][j]) + " ");
				}
				writer.newLine();
			}
			writer.close();
			
			System.out.println("\nSaved weights from training to: " + input_file + "\n");
			
		} catch (Exception e) {
			System.out.println("Error printing weights to file. " + e);
		}
	}
}
