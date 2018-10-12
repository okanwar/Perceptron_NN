import java.io.*;
import java.util.*;
import java.lang.*;

public class Perceptron{
	private PerceptronSettings p_settings;
	private PatternSet trainingSet;

	public Perceptron(PerceptronSettings p_settings){
		this.p_settings = p_settings;
		p_settings.printNetInitializationSettings();
	
		//Set training file
		if(p_settings.hasTrainingFile()) {
			trainingSet = new PatternSet(p_settings.getTrainingFile(), true);	
		} else {
			trainingSet = null;
		}
}

	public void trainNet(){
		
	}
		
	public void deployNet(){

	}
}
