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

			PerceotronSettings perceptronSettings = new PerceotronSettings();

			if(mode == 1){
				//New net from scratch
				perceptronSettings.setSettings();
				
				//Create net
				printNetInitializationSettings(perceptronSettings.getSettings());
			} else {
				//Load from file
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
					PerceotronSettings perceptronSettings = null;

					//Determine mode
					if( line.charAt(0) == '1' ){
						//Extract net settings
						String [] userSettings = new String[7];
						StringTokenizer st = new StringTokenizer(line, " ");
						st.nextToken(); 	//Skip first token, it is just the mode
						int index = 0;
						while(st.hasMoreTokens()){
							userSettings[index] = st.nextToken();
							index++;	
						}
						//Initialize perceptron with parsed settings
						perceptronSettings = new PerceotronSettings(userSettings);
						printNetInitializationSettings(perceptronSettings.getSettings());
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

	// private static String [] getNetSettings(String [] prompts){
	// 	String [] netSettings = new String[prompts.length];
	// 	Scanner input = new Scanner(System.in);

	// 	for(int i = 0; i < prompts.length; i++){
	// 		System.out.print(prompts[i]);
	// 		netSettings[i] = input.next();
	// 		System.out.println();
	// 	}
	// 	return netSettings;
	// }

}
