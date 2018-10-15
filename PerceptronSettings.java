import java.util.Scanner;
import java.lang.*;

public class PerceptronSettings {
	private static final String NET_CREATION_PROMPT = "(1) Train a new perceptron net\n"
			+ "(2) Train a net from file\n";
	private static final String TRAINING_PROMPT = "Enter the training data file name:";
	private static final String INITIALIZED_WEIGHTS_PROMPT = "--- Initialized weight values ---\n"
			+ "(0) Initialize weights to 0\n" + "(1) Initialize weights to random values between (-0.5,0.5)\n:";
	private static final String MAX_EPOCHS_PROMPT = "Enter the maximum number of training epochs:";
	private static final String WEIGHT_FILE_PROMPT = "Enter file name for trained weight settings:";
	private static final String LEARNING_RATE_PROMPT = "Enter the learning rate alpha between 0 and 1, not including 0:";
	private static final String THRESHOLD_THETA_PROMPT = "Enter the threshold theta:";
	private static final String THRESHOLD_WEIGHT_CHANGE_PROMPT = "Enter the threshold for measuring weight changes:";
	private static final String DEPLOYMENT_FILE_PROMPT = "Enter the testing/deployment file name:";
	private String trainingFile, weightsFile, deploymentFile;
	private boolean randomWeightValues, weightsFromFile;
	private int maxEpochs;
	private double learningRate, thresholdTheta, thresholdWeightChanges;
	private Scanner input;

	public PerceptronSettings() {
		this(null, null, false, -1, -1, -1, -1, null);
	}

	public PerceptronSettings(String[] settings) {
		this(settings[0], settings[3], Boolean.valueOf(settings[1]), Integer.parseInt(settings[2]),
				Double.parseDouble(settings[4]), Double.parseDouble(settings[5]), Double.parseDouble(settings[6]),
				settings[7]);
	}

	public PerceptronSettings(String trainingFile, String weightsFile, boolean randomWeightValues, int maxEpochs,
			double learningRate, double thresholdTheta, double thresholdWeightChanges, String deploymentFile) {
		this.trainingFile = trainingFile;
		this.weightsFile = weightsFile;
		this.randomWeightValues = randomWeightValues;
		this.maxEpochs = maxEpochs;
		this.learningRate = learningRate;
		this.thresholdTheta = thresholdTheta;
		this.thresholdWeightChanges = thresholdWeightChanges;
		this.deploymentFile = deploymentFile;
		this.weightsFile = weightsFile;
		this.input = null;
	}

	public void setSettings() {

//		input = new Scanner(System.in);
//
//		// Training file
//		System.out.print(TRAINING_PROMPT);
//		trainingFile = input.next();
//		System.out.println();
//
//		// Random weights
//		System.out.print(INITIALIZED_WEIGHTS_PROMPT);
//		if (input.nextInt() == 1) {
//			randomWeightValues = true;
//		} else {
//			randomWeightValues = false;
//		}
//		System.out.println();
//
//		// Max epochs
//		System.out.print(MAX_EPOCHS_PROMPT);
//		maxEpochs = input.nextInt();
//		System.out.println();
//
//		// Weight file
//		System.out.print(WEIGHT_FILE_PROMPT);
//		weightsFile = input.next();
//		System.out.println();
//
//		// learning rate
//		do {
//			System.out.print(LEARNING_RATE_PROMPT);
//			learningRate = input.nextDouble();
//			System.out.println();
//		} while (!validLearningRate());
//
//		// threshold theta
//		System.out.print(THRESHOLD_THETA_PROMPT);
//		thresholdTheta = input.nextDouble();
//		System.out.println();
//
//		// threshold weight changes
//		System.out.print(THRESHOLD_WEIGHT_CHANGE_PROMPT);
//		thresholdWeightChanges = input.nextDouble();
//		System.out.println();

	}

	public void setUserSettings() {
		Scanner input = new Scanner(System.in);
		
		//Prompt user for all settings
		this.setTrainingFile(null);
		this.setTrainingWeights(-1);
		this.setMaxEpochs(-1);
		this.setWeightsFile(null, false);
		this.setLearningRate(-1);
		this.setThresholdTheta(-1);
		this.setThresholdWeightChanges(-1);
	}

	public void setTrainingFile(String trainingFile) {
		if (trainingFile == null) {
			input = new Scanner(System.in);
			System.out.println(TRAINING_PROMPT);
			this.trainingFile = input.next();
			System.out.println();
		} else {
			this.trainingFile = trainingFile;
		}
	}

	public void setTrainingWeights(int randomWeights) {
		if (randomWeights == -1) {
			System.out.print(INITIALIZED_WEIGHTS_PROMPT);
			if (input.nextInt() == 1) {
				this.randomWeightValues = true;
			} else {
				this.randomWeightValues = false;
			}
			System.out.println();
		} else if (randomWeights == 0) {
			this.randomWeightValues = false;
		} else if (randomWeights == 1) {
			this.randomWeightValues = true;
		} else {
			System.out.println("Invalid value for random weights.");
			System.exit(-1);
		}
	}

	public void setMaxEpochs(int maxEpochs) {
		if (maxEpochs == -1) {
			do {
				System.out.print(MAX_EPOCHS_PROMPT);
				this.maxEpochs = input.nextInt();
				System.out.println();
				if (this.maxEpochs < 1)
					System.out.println(this.maxEpochs + " is not a valid value for max epochs");
			} while (this.maxEpochs < 1);
		} else {
			this.maxEpochs = maxEpochs;
		}
	}

	public void setLearningRate(double learningRate) {
		if (learningRate == -1) {
			do {
				System.out.print(LEARNING_RATE_PROMPT);
				this.learningRate = input.nextDouble();
				System.out.println();
			} while (!validLearningRate());
		} else {
			this.learningRate = learningRate;
		}
	}

	public void setThresholdTheta(double thresholdTheta) {
		if (thresholdTheta < 0) {
			do {
				System.out.print(THRESHOLD_THETA_PROMPT);
				this.thresholdTheta = input.nextDouble();
				System.out.println();
				if (this.thresholdTheta < -0.1)
					System.out.println("Threshold theta must be at least 0");
			} while (this.thresholdTheta < -0.1);
		} else {
			this.thresholdTheta = thresholdTheta;
		}
	}

	public void setThresholdWeightChanges(double weightChangesThreshold){
		if(weightChangesThreshold < 0){
			do {
				System.out.print(THRESHOLD_WEIGHT_CHANGE_PROMPT);
				this.thresholdWeightChanges = input.nextDouble();
				System.out.println();
				if (this.thresholdWeightChanges < -0.1)
					System.out.println("Threshold theta must be at least 0");
			} while (this.thresholdWeightChanges < -0.1);
		} else {
			this.thresholdWeightChanges = weightChangesThreshold;
		}
	}
	
	public void setDeploymentFile(String deploymentFile) {
		if (deploymentFile == null) {
			input = new Scanner(System.in);
			System.out.print(DEPLOYMENT_FILE_PROMPT);
			this.deploymentFile = input.next();
			System.out.println();
		} else {
			this.deploymentFile = deploymentFile;
		}
	}

	public void setWeightsFile(String file, boolean loadFromFile) {
		if (file == null) {
			input = new Scanner(System.in);
			System.out.print(WEIGHT_FILE_PROMPT);
			this.weightsFile = input.next();
			System.out.println();
		} else {
			this.weightsFile = file;
		}
		this.weightsFromFile = loadFromFile;
	}

	public void printNetInitializationSettings() {

		System.out.println("\nInitializing net with settings:");
		System.out.println("------------------------------------");
		System.out.println(
				"Training File: " + trainingFile + "\n" + "Initialize weights to random values: " + randomWeightValues
						+ "\n" + "Maximum number of training epochs: " + maxEpochs + "\n" + "Weight settings file: "
						+ weightsFile + "\n" + "Learning rate: " + learningRate + "\n" + "Threshold theta: "
						+ thresholdTheta + "\n" + "Threshold to measure weight changes: " + thresholdWeightChanges);
		System.out.println("------------------------------------");
	}

	public String[] getSettings() {
		return new String[] { trainingFile, String.valueOf(randomWeightValues), String.valueOf(maxEpochs), weightsFile,
				String.valueOf(learningRate), String.valueOf(thresholdTheta), String.valueOf(thresholdWeightChanges) };
	}

	public String getTrainingFile() {
		return trainingFile;
	}

	public boolean hasTrainingFile() {
		if (trainingFile != null) {
			return true;
		} else {
			return false;
		}
	}

	public String getWeightsFile() {
		return weightsFile;
	}
	
	public boolean loadWeightsFromFile(){
		return weightsFromFile;
	}

	public String getDeploymentFile() {
		return deploymentFile;
	}

	public boolean hasDeploymentFile() {
		if (deploymentFile != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean initializeWithRandomWeights() {
		return randomWeightValues;
	}

	public int getMaxEpochs() {
		return maxEpochs;
	}

	public double getLearningRate() {
		return learningRate;
	}

	public double getThresholdTheta() {
		return thresholdTheta;
	}

	public double getWeightChangesThreshold() {
		return thresholdWeightChanges;
	}

	private boolean validLearningRate() {
		return (learningRate > 0 && learningRate <= 1);
	}

	private String getValue(Scanner input, String prompt) {
		// threshold weight changes
		System.out.print(prompt);
		String userin = input.next();
		System.out.println();
		return userin;
	}

}
