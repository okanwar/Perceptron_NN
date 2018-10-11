import java.io.*;
import java.util.*;
import java.lang.*;

public class Perceptron{
	private  PerceptronSettings p_settings;
	private double[] weights;

	public Perceptron(PerceptronSettings p_settings){
		this.p_settings = p_settings;
	}
	public void train() {
		BufferedReader reader = null;
		String line = "";

		//Parse training set
		try {
			reader = new BufferedReader (new FileReader (p_settings.getTrainingFile()));
			int firstThreeLines = 0;
			int [] sampleSettings = new int[3];

			//Read first three lines
			for(int i= 0; i < 3; i++){
				sampleSettings[i] = Integer.parseInt(reader.readLine());
			}

			//Begin reading each sample
			int linesOfInputPattern = sampleSettings[0] / sampleSettings[1];
			int sampleIndex = 0;
			String inputPattern = "";
			String outputPattern = "";
			String classification = "";

			//Read a single sample			
			while ((line = reader.readLine()) != null){
				if( !line.isEmpty() ){
					if(sampleIndex < linesOfInputPattern){
						//Reading input pattern
						inputPattern += line + " ";
					} else if(sampleIndex == linesOfInputPattern){
						//Reading output pattern
						outputPattern = line + " ";
					} else {
						//Reading classification
						classification = line;

						//Create new training pair
						TrainingPair p1 = new TrainingPair(sampleSettings[0], sampleSettings[1]);
						p1.getStrings(inputPattern, outputPattern, classification);

						//Reset
						sampleIndex = 0;
						inputPattern = "";
						outputPattern = "";

						continue;
					}
					sampleIndex++;
				}
			}
		} catch (Exception e) {
			System.out.println("Error parsing file. " + e);
		}
	}
}
