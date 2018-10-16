import java.io.*;
import java.util.Random;
import java.util.StringTokenizer;

public class TrainingSet extends PatternSet {
	private static final String DIVIDER = "--------------------------------------------------------------------------------------------------------------------\n";
	private double maxWeightChange;
	private String weightChanges;

	public TrainingSet(PerceptronSettings ps) {
		// Setup pattern set
		super(ps.getTrainingFile(), ps);

		// Training Set
		maxWeightChange = 0;
		weightChanges = DIVIDER + "Training Results\n" + DIVIDER;
	}

	public void updateBiasWeight(int outputNeuron, double newBias) {
		double oldBiasWeight = biasWeights[outputNeuron];
		double weightChange = Math.abs(newBias - oldBiasWeight);
		if (weightChange > maxWeightChange)
			maxWeightChange = weightChange;
		biasWeights[outputNeuron] = newBias;
		weightChanges += String.format("%15s%15f%15f%15f ", "Bias", oldBiasWeight, biasWeights[outputNeuron],
				weightChange) + " | bnew = bold + lr*t\n";
	}

	public void updateInputWeightForIndex(int outputNeuron, int inputNeuron, double newWeight) {
		double oldWeight = weights[outputNeuron][inputNeuron];
		double weightChange = Math.abs(newWeight - oldWeight);
		if (weightChange > maxWeightChange)
			maxWeightChange = weightChange;
		weights[outputNeuron][inputNeuron] = newWeight;
		weightChanges += String.format("%15s%15f%15f%15f ", "Input", oldWeight, newWeight, weightChange)
				+ " | Wnew = Wold + lr*t*x\n";
	}

	public double getMaxWeightChange() {
		return maxWeightChange;
	}

	public void resetMaxWeightChange() {
		maxWeightChange = 0;
	}

	public void printTrainingSet() {
		printSet();
		printWeights();
	}

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

	public void sendIncrementStatus(int epoch, int patternNumber) {
		weightChanges += "\n" + DIVIDER + "Epoch " + epoch + " Pattern " + patternNumber + "\n" + DIVIDER + "\n";
	}

	public void sendActivationStatus(double yin, double activation) {
		weightChanges += DIVIDER + "Activation\n" + DIVIDER + "Yin = " + yin + "\nActivation = " + activation + "\n";
	}

	public void sendBeginStatus(int outputNeuron) {
		weightChanges += DIVIDER + "Weight Changes for Output Neuron " + outputNeuron + "\n" + DIVIDER
				+ String.format("%15s%15s%15s%15s\n", "Type", "Wold", "Wnew", "Change");
	}

	public void sendEndStatus() {
		weightChanges += DIVIDER + "\n";
	}

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
			
			System.out.println("\nSaved weights from training to: " + input_file);
			
		} catch (Exception e) {
			System.out.println("Error printing weights to file. " + e);
		}
	}
}
