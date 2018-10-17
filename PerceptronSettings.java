import java.util.Scanner;
import java.lang.*;

/*
 * PerceptronSettings
 * 
 * This is a container to hold all the properties of the perceptron net
 * 
 * @author Michael Dana, Om Kanwar
 */

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
	private TrainingSet trainingSet;

	/*
	 * Constructor
	 */
	public PerceptronSettings() {
		this(null, null, false, -1, -1, -1, -1, null);
	}

	/*
	 * Constructor
	 * @param settings An array containing all the settings to initialize the net in the order:
	 * 	String trainingFile, String weightsFile, boolean randomWeightValues, int maxEpochs,
			double learningRate, double thresholdTheta, double thresholdWeightChanges, String deploymentFile 
	 */
	public PerceptronSettings(String[] settings) {
		this(settings[0], settings[3], Boolean.valueOf(settings[1]), Integer.parseInt(settings[2]),
				Double.parseDouble(settings[4]), Double.parseDouble(settings[5]), Double.parseDouble(settings[6]),
				settings[7]);
	}

	/*
	 * Constructor
	 * @param trainingFile The file to train the net with
	 * @param weightsFile The file to save the weights to
	 * @param randomWeightValues Boolean indicating whether to intialize the weights randomly or all to 0
	 * @param maxEpochs The maximum number of epochs to wait for the net to converge
	 * @param learningRate The learning rate of the net
	 * @param thresholdTheta The threshold to use for the activation function
	 * @param thresholdWeightChanges The threshold for weight changes to signal conversion
	 * @parma deploymentFile The file to deploy the net on
	 */
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
		this.trainingSet = null;
	}

	/*
	 * setUserSettings - Prompts the user for settings to create the net with
	 */
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

	/*
	 * setTrainingFile - Sets the training file
	 * @param trainingFile The file to set for training if null, user is prompted for file
	 */
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

	/*
	 * setTrainingWeights - Sets whether to initialize random weights
	 * @param randomWeights Signal to initialize with random weights, 1 = random weights, 0 = all weights 0, -1 = prompt user
	 */
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

	/*
	 * setMaxEpochs - Sets the maximum number of epochs
	 * @param maxEpochs The maximum number of epochs, -1 = prompt user for max epochs
	 */
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

	/*
	 * setLearningRate - Sets the learning rate value
	 * @param learningRate The value of learning rate to set to, -1 = prompt user
	 */
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

	
	/*
	 * setThresholdTheta - Sets the threshold theta for activation
	 * @param thresholdTheta The value of threshold for activation, anything < 0 will prompt user
	 */
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

	/*
	 * setThresholdWeightChanges - Sets the threshold for weight changes
	 * @param weightChangesThreshold The value of weight change threshold, anyhting < 0 will prompt user for value
	 */
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
	
	/*
	 * setDeploymentFile - Sets the deployment file of the net
	 * @param deploymentFile The file to deploy net on, null will prompt user for file
	 */
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

	/*
	 * setWeightsFile - Sets the file to save weights to
	 * @param file The file to save weights to, null will prompt user for file
	 * @param loadFromFile Boolean to indicate loading weights from the set file or false: save weights to this file
	 */
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

	/*
	 * printNetInitializationSettings - Prints intialized settings for net
	 */
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
	
	/*
	 * getTrainingSet - Returns the training set the net uses
	 * @return Returns the TrainingSet object the net uses
	 */
	public TrainingSet getTrainingSet() {
		return this.trainingSet;
	}
	
	/*
	 * setTrainingSet - Sets the training set the net uses
	 * @param ts The TrainingSet object
	 */
	public void setTrainingSet(TrainingSet ts) {
		this.trainingSet = ts;
	}

	/*
	 * getSettings - Gets the settings of the net as a string
	 * @return Returns a string representing the settings of the net
	 */
	public String[] getSettings() {
		return new String[] { trainingFile, String.valueOf(randomWeightValues), String.valueOf(maxEpochs), weightsFile,
				String.valueOf(learningRate), String.valueOf(thresholdTheta), String.valueOf(thresholdWeightChanges) };
	}

	/*
	 * getTrainingFile - Gets the training file
	 * @return Returns the training file
	 */
	public String getTrainingFile() {
		return trainingFile;
	}

	/*
	 * hasTrainingFile - indicates whether the net has a training file
	 * @return Boolean indicating a training file(true) or no file(false)
	 */
	public boolean hasTrainingFile() {
		if (trainingFile != null) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * getWeightsFile - Returns the weights file for the net
	 * @return Returns the weights file
	 */
	public String getWeightsFile() {
		return weightsFile;
	}
	
	/*
	 * loadWeightsFromFile - indicates whether the weights are loaded from file or not
	 * @return Boolean, true = loading from file , false = not loading from file or initializing weights
	 */
	public boolean loadWeightsFromFile(){
		return weightsFromFile;
	}

	/*
	 * getDeploymentFile - Returns the deployment file of the net
	 * @return Returns the nets deployment file
	 */
	public String getDeploymentFile() {
		return deploymentFile;
	}

	/*
	 * hasDeploymentFile - indicates whether the net has a deployment file or not
	 * @return Boolean, true = has deployment file, false = no deployment file
	 */
	public boolean hasDeploymentFile() {
		if (deploymentFile != null) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * initializeWithRandomWeights - Indicates whether the net is to be initialized with randome weights or not
	 * @param Returns a boolean indicating whether the net is to be initialized with randome weights or not
	 */
	public boolean initializeWithRandomWeights() {
		return randomWeightValues;
	}

	/*
	 * getMaxEpochs - Returns the max epochs for the net
	 * @return Returns the max epochs
	 */
	public int getMaxEpochs() {
		return maxEpochs;
	}

	/*
	 * getLearningRate - returns the learning rate of the net
	 * @return Returns the learning rate
	 */
	public double getLearningRate() {
		return learningRate;
	}

	/*
	 * getThresholdTheta - returns the threshold for activation of the net
	 * @return Returns the activation threshold for the net
	 */
	public double getThresholdTheta() {
		return thresholdTheta;
	}

	/*
	 * getWeightChangesThreshold - returns the weight changes threshold of th net
	 * @return Returns the weight changes threshold for the net
	 */
	public double getWeightChangesThreshold() {
		return thresholdWeightChanges;
	}

	/*
	 * validLearningRate - verifies the learning rate is valide
	 * @return Returns a boolean indicating a valid learning rate or invalid learning rate
	 */
	private boolean validLearningRate() {
		return (learningRate > 0 && learningRate <= 1);
	}

}
