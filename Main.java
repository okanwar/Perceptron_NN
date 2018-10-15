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

			PerceptronSettings perceptronSettings = new PerceptronSettings();
			Perceptron p1 = new Perceptron(perceptronSettings ,false);

			if(mode == 1){
				//New net from scratch
				perceptronSettings.setSettings();
				
				//Create net
				p1.trainNet();
				perceptronSettings.setDeploymentFile(null);
				p1.deployNet();
			} else {
				//Load from file
				perceptronSettings.setWeightsFile(null);
				perceptronSettings.setDeploymentFile(null);
//				p1.setWeightsFromFile();
				p1.deployNet();

			}
		} else {
			//Run with specifications from file
			String userRunsFile = args[0];

			//Try to open file
			BufferedReader reader = null;
			String line = "";
			try{
				reader = new BufferedReader( new FileReader(userRunsFile));	
				while( (line = reader.readLine()) != null ){
					//New perceptron
					PerceptronSettings perceptronSettings = null;

					//Determine mode
					if( line.charAt(0) == '1' ){
						//Extract net settings from file
						String [] userSettings = new String[8];
						StringTokenizer st = new StringTokenizer(line, " ");
						st.nextToken(); 	//Skip first token, it is just the mode
						int index = 0;
						while(st.hasMoreTokens()){
							userSettings[index] = st.nextToken();
							index++;	
						}
						
						if(userSettings[1].equals("1")){
							userSettings[1] = "true";
						} else {
							userSettings[1] = "false";
						}

						//Initialize perceptron with parsed settings
						Perceptron p1 = new Perceptron(new PerceptronSettings(userSettings) ,true);
						p1.trainNet();
						p1.deployNet();
					} else {
						//Load net from file
					}
				}
			} catch (Exception e){

			}
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

}
