import java.lang.Integer;
import java.util.*;
import java.io.*;

public class Main{

	public static void main(String [] args){

		if(args.length == 0) {
			//Begin
			String mainPrompt = "---- Welcome to the perceptron net ----\n" +
				"(1) Train a new perceptron net\n" +
				"(2) Train a net from file\n";

			//			Scanner input = new Scanner(System.in);
			int userIn = Integer.parseInt(getInput(mainPrompt));

			switch(userIn){
				case 1:
					//New perceptron net from scratch			
					perceptronFromScratch(null);
					break;
				case 2:
					//Load perceptron net from file
					break;
				default:
					break;
			}


		} else {
			int requiredArguments = 8;
			//Passed arguments in

			//Validate args

			//Run
			if(args.length == requiredArguments ){
				if(args[0].equals("new")){
					String [] userSettings = new String [7];
					for(int i = 0; i < 7; i++){
						userSettings[i] = args[i+1];
					}
					perceptronFromScratch(userSettings);
				}
			} else {
				System.out.println(args.length);
			}
		}
	}

	private static void perceptronFromScratch(String [] userSettings){
		String [] prompts = new String[] {
			"Enter the training data file name:",
				"--- Initialized weight values ---\n" +
					"(1) Initialize weights to 0\n" +
					"(2) Initialize weights to random values between (-0.5,0.5)\n:",
				"Enter the maximum number of training epochs:",
				"Enter a file name to save the trained weight settings:",
				"Enter the learning rate alpha between 0 and 1, not including 0:",
				"Enter the threshold theta:",
				"Enter the threshold for measuring weight changes:"
		};

		//Get input 
		if(userSettings == null){
			userSettings = new String[prompts.length];
			for(int i = 0; i < prompts.length; i++){
				userSettings[i] = getInput(prompts[i]);
			}
		}

		printNetInitializationSettings(userSettings);
	}

	private static void trainPerceptronNet(boolean newNet){

		//Get data file
		System.out.print("Enter a file name for the training data: ");	

		//Create new net if required
		if(newNet){

			boolean zeroWeights;
			int maxEpochs;
			double learningRate, thresholdTheta, thresholdWeightChanges;
			String trainingFile, weightFile;

			//Get new net properties



		}



		//Open data file

		//Parse data file and created training set

		//Train Perceptron
	}

	private static void printNetInitializationSettings(String [] userSettings){
		String [] variables = new String[]{
			"Training File",
				"Initialize weights to random values",
				"Maximum number of training epochs",
				"Weight settings file",
				"Learning rate",
				"Threshold theta",
				"Threshold to measure weight changes"
		};

		System.out.println("Initializing net with settings:");
		System.out.println("------------------------------------");
		for(int i = 0; i < userSettings.length; i++){
			System.out.println(variables[i] + " : " + userSettings[i]);
		}
		System.out.println("------------------------------------");
	}

	private static String getInput(String prompt){
		Scanner input = new Scanner(System.in);
		System.out.print(prompt);
		String userIn = input.next();
		System.out.println();
		input.close();
		return userIn;

	} 

}
