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
						inputPattern += line.trim();
					} else if(sampleIndex == linesOfInputPattern){
						//Reading output pattern
						outputPattern = line.trim();
					} else {
						//Reading classification
						classification = line.trim();

						//Reset
						sampleIndex = 0;
						inputPattern = "";
						outputPattern = "";
						
						//Create new training pair

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
