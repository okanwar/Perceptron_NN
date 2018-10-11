import java.io.*;
import java.util.*;
import java.lang.*;

public class Perceptron{
	private  PerceptronSettings p_settings;
	private int [] inputPattern, outputPattern;
	private double[] weights;

	public Perceptron(PerceptronSettings p_settings){
		this.p_settings = p_settings;
		inputPattern = null;
		outputPattern = null;
	}
	public void train() {
		BufferedReader reader = null;
		String line = "";
		try {
			reader = new BufferedReader (new FileReader (p_settings.getTrainingFile()));
			int firstThreeLines = 0;
			int [] sampleSettings = new int[3];
			while ((line = reader.readLine()) != null){
				//Get sample settings
				if( firstThreeLines < 3 ) {
					sampleSettings[firstThreeLines] = Integer.parseInt(line);
					firstThreeLines++;
				} else {
					//No longer reading sample settins, so parse training set
					inputPattern = new int[sampleSettings[0]];
					outputPattern = new int[sampleSettings[1]];

					StringTokenizer st = new StringTokenizer(line, " ");
					int index = 0;
					while(st.hasMoreTokens()) {
						//SampleSettings[0] is the input pattern size, so first n numbers are input pattern
						if(index < sampleSettings[0]){
							inputPattern[index] = Integer.parseInt(st.nextToken());
						} else if (index % sampleSettings[0] < sampleSettings[1]){
							outputPattern[index % sampleSettings[0]] = Integer.parseInt(st.nextToken());
						} else{
							
						}
					}
				}

			}
		} catch (Exception e) {}
	}
}
