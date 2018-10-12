import java.util.Scanner;
import java.lang.*;

public class PerceptronSettings{
	private static final String TRAINING_PROMPT = "Enter the training data file name:";
	private static final String INITIALIZED_WEIGHTS_PROMPT = "--- Initialized weight values ---\n" +
		"(1) Initialize weights to 0\n" +
		"(2) Initialize weights to random values between (-0.5,0.5)\n:";
	private static final String MAX_EPOCHS_PROMPT = "Enter the maximum number of training epochs:";
	private static final String WEIGHT_FILE_PROMPT = "Enter a file name to save the trained weight settings:";
	private static final String LEARNING_RATE_PROMPT = "Enter the learning rate alpha between 0 and 1, not including 0:";
	private static final String THRESHOLD_THETA_PROMPT = "Enter the threshold theta:";
	private static final String THRESHOLD_WEIGHT_CHANGE_PROMPT = "Enter the threshold for measuring weight changes:";
	private String trainingFile, weightsFile;
	private boolean randomWeightValues;
	private int maxEpochs;
	private double learningRate, thresholdTheta, thresholdWeightChanges;


	public PerceptronSettings(){
		this("", "", false, -1, -1, -1, -1);		
	}

	public PerceptronSettings(String [] settings){
		this(settings[0], settings[3], Boolean.valueOf(settings[1]), Integer.parseInt(settings[2]), Double.parseDouble(settings[4]), Double.parseDouble(settings[5]), Double.parseDouble(settings[6]));
	}	

	public PerceptronSettings( String trainingFile, String weightsFile, boolean randomWeightValues,
			int maxEpochs, double learningRate, double thresholdTheta, double thresholdWeightChanges){
		this.trainingFile = trainingFile;
		this.weightsFile = weightsFile;
		this.randomWeightValues = randomWeightValues;
		this.maxEpochs = maxEpochs;
		this.learningRate = learningRate;
		this.thresholdTheta = thresholdTheta;
		this.thresholdWeightChanges = thresholdWeightChanges;		
	}

	public void setSettings(){

		Scanner input = new Scanner(System.in);

		//Training file
		System.out.print(TRAINING_PROMPT);
		trainingFile = input.next();
		System.out.println();

		//Random weights
		System.out.print(INITIALIZED_WEIGHTS_PROMPT);
		if(input.nextInt() == 1){
			randomWeightValues = true;
		} else {
			randomWeightValues = false;
		}
		System.out.println();

		//Max epochs
		System.out.print(MAX_EPOCHS_PROMPT);
		maxEpochs = input.nextInt();
		System.out.println();

		//Weight file 
		System.out.print(WEIGHT_FILE_PROMPT);
		weightsFile = input.next();
		System.out.println();

		//learning rate
		do {
			System.out.print(LEARNING_RATE_PROMPT);
			learningRate = input.nextDouble();
			System.out.println();
		} while (!validLearningRate());

		//threshold theta
		System.out.print(THRESHOLD_THETA_PROMPT);
		thresholdTheta = input.nextDouble();
		System.out.println();

		//threshold weight changes
		System.out.print(THRESHOLD_WEIGHT_CHANGE_PROMPT);
		thresholdWeightChanges = input.nextDouble();
		System.out.println();

	}

	public void printNetInitializationSettings(){

		System.out.println("Initializing net with settings:");
		System.out.println("------------------------------------");
		System.out.println("Training File: " + trainingFile + "\n" +
			"Initialize weights to random values: " +  randomWeightValues + "\n" +
			"Maximum number of training epochs: " + maxEpochs + "\n" +
			"Weight settings file: " + weightsFile + "\n" +
			"Learning rate: " + learningRate + "\n" +
			"Threshold theta: " + thresholdTheta + "\n" +
			"Threshold to measure weight changes: " + thresholdWeightChanges
			);
		System.out.println("------------------------------------");
	}

	public String [] getSettings(){
		return new String[]{trainingFile, String.valueOf(randomWeightValues), String.valueOf(maxEpochs), 
			weightsFile, String.valueOf(learningRate), String.valueOf(thresholdTheta), String.valueOf(thresholdWeightChanges)};
	}

	public String getTrainingFile(){
		return trainingFile;
	}
	
	public String getWeightsFile(){
		return weightsFile;
	}

	public boolean initializeWithRandomWeights(){
		return randomWeightValues;
	}

	public int getMaxEpochs(){
		return maxEpochs;
	}

	public double getLearningRate(){
		return learningRate;
	}

	public double getThresholdTheta(){
		return thresholdTheta;
	}

	public double getWeightChangesThreshold(){
		return thresholdWeightChanges;
	}

	private boolean validLearningRate(){
		return (learningRate > 0 && learningRate <= 1);
	}

}
