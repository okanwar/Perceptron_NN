import java.lang.Integer;
import java.util.*;
import java.io.*;

/*
 *  Main
 *  
 *  Runs the training and classification of the perceptron net
 *  
 *  @author Michael Dana, Om Kanwar
 */
public class Main {

	public static void main(String[] args) {

		boolean quit = false;
		
		if (args.length == 0) {
			while(!quit) {
				// Run by prompts
				String mainPrompt = "---- Welcome to the perceptron net ----\n" + "(1) Train a new perceptron net\n"
						+ "(2) Train a net from file\n";

				// Get mode
				Scanner input = new Scanner(System.in);
				System.out.println(mainPrompt);
				int mode = input.nextInt();

				PerceptronSettings perceptronSettings = new PerceptronSettings();
				Perceptron p1 = new Perceptron(perceptronSettings, false);

				if (mode == 1) {
					// New net from scratch
					perceptronSettings.setUserSettings();

					// Create net
					p1.trainNet();
					perceptronSettings.setDeploymentFile(null);
					p1.deployNet();
				} else {
					// Load from file
					perceptronSettings.setWeightsFile(null, true);
					perceptronSettings.setDeploymentFile(null);
					p1.deployNet();

				}
				
				//Run again?
				System.out.print("Enter 1 to quit or 0 to continue ");
				int userIn = input.nextInt();
				System.out.println();
				if(userIn == 1) {
					quit = true;
				}
				
			}
		} else {
			// Run with specifications from file
			String userRunsFile = args[0];

			// Try to open file
			BufferedReader reader = null;
			String line = "";
			try {
				reader = new BufferedReader(new FileReader(userRunsFile));
				while ((line = reader.readLine()) != null) {
					// New perceptron
					PerceptronSettings perceptronSettings = null;

					// Determine mode
					if (!line.isEmpty()) {
						if (line.charAt(0) == '1') {
							// Extract net settings from file
							String[] userSettings = new String[8];
							StringTokenizer st = new StringTokenizer(line, " ");
							st.nextToken(); // Skip first token, it is just the
											// mode
							int index = 0;
							while (st.hasMoreTokens()) {
								userSettings[index] = st.nextToken();
								index++;
							}

							if (userSettings[1].equals("1")) {
								userSettings[1] = "true";
							} else {
								userSettings[1] = "false";
							}

							// Initialize perceptron with parsed settings
							Perceptron p1 = new Perceptron(new PerceptronSettings(userSettings), false);
							p1.trainNet();
							p1.deployNet();
						} else {
							// Load net from file
							perceptronSettings = new PerceptronSettings();
							Perceptron p1 = new Perceptron(perceptronSettings, false);

							StringTokenizer st = new StringTokenizer(line, " ");
							st.nextToken(); // Skip first token
							perceptronSettings.setWeightsFile(st.nextToken(), true);
							perceptronSettings.setDeploymentFile(st.nextToken());
							p1.deployNet();
						}
					}
				}
			} catch (Exception e) {
				System.out.println("Error reading runs from file. " + e);
			}
		}
	}
}
