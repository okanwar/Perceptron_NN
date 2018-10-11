import java.lang.Integer;
import java.util.*;
import java.io.*;

public class Main{

	public static void main(String [] args){

		if(args.length == 0){
			//Run by prompts
			String mainPrompt = "---- Welcome to the perceptron net ----\n" +
				"(1) Train a new perceptron net\n" +
				"(2) Train a net from file\n";
			
			//Get mode
			Scanner input = new Scanner(System.in);
			System.out.println(mainPrompt);
			int mode = input.nextInt();

			if(mode == 1){
				//New net from scratch
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

			String [] netSettings = getNetSettings(prompts);
			printNetInitializationSettings(netSettings);
			} else {
				//Load from file
			}
		} else {
			//Run with specifications from file
		}
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

	private static String [] getNetSettings(String [] prompts){
		String [] netSettings = new String[prompts.length];
		Scanner input = new Scanner(System.in);

		for(int i = 0; i < prompts.length; i++){
			System.out.print(prompts[i]);
			netSettings[i] = input.next();
			System.out.println();
		}
		return netSettings;
	}

}
