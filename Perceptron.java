import java.io.*;
import java.util.*;
import java.lang.*;

public class Perceptron{
	private PerceptronSettings p_settings;
	private TrainingSet trainingSet;
	private boolean verboseTrain;

	public Perceptron(PerceptronSettings p_settings, boolean verboseTrain){
		this.p_settings = p_settings;
		this.verboseTrain= verboseTrain;
		p_settings.printNetInitializationSettings();

		//Set training file
		if(p_settings.hasTrainingFile()) {
			System.out.println("CREATING WITH RANDOM WEIGHTS:" + p_settings.initializeWithRandomWeights());
			trainingSet = new TrainingSet(p_settings.getTrainingFile(), p_settings.initializeWithRandomWeights());
		} else {
			trainingSet = null;
		}
	}

	public void trainNet(){
		//Setup
		Pattern[] patternSet = trainingSet.getPatternSet();	
		double[][] weights = trainingSet.getWeights();
		int[] calculatedOutput = new int[trainingSet.getOutputPatternSize()];
		BufferedWriter writer = null;

		if(verboseTrain){
			try {
				writer = new BufferedWriter(new FileWriter(p_settings.getTrainingFile()+"_"+ p_settings.getMaxEpochs()+"_epoch_training.txt"));

			} catch (Exception e){
				System.out.println("Failed to create training output file");
			}
		}

		//Run training algorithm
		int currentEpoch = 0;
		boolean converged = false;
		while(currentEpoch < p_settings.getMaxEpochs() || !converged){
			if(verboseTrain) {
				try{
					writer.write("--- Training ---\n\n -- Epoch " + currentEpoch );
				} catch (Exception e){
					System.out.println("Error writing verbose output");
				}
			}

			//Compute and classify a single pattern
			for(int patternIndex= 0; patternIndex < trainingSet.getNumberOfPatterns(); patternIndex++ ) {

				//Compute activation for single unit
				for(int j = 0; j < trainingSet.getOutputPatternSize(); j++){
					double yin = computeYin(patternSet[patternIndex], j);
					//Print Status
					if(verboseTrain){
						try {
							writer.newLine();
							writer.write("\t -- Pattern " + patternIndex + " Output Pattern " + j + "\n" +
									"\tYin = " + yin + "\n" +
									"\tThreshold theta = " + p_settings.getThresholdTheta() + "\n"
								    );
						} catch (Exception e){
							System.out.println("Error writing yin verbose training data");
						}
					}	

					int output = -1;
					if(yin > p_settings.getThresholdTheta()){
						output = 1;	
					} else if(yin < p_settings.getThresholdTheta()){
						output = -1;
					} else {
						output = 0;
					}

					if(verboseTrain){
						String operation = "";
						if(output == 1 ){
							operation = ">";
						} else if(output == -1){
							operation = "<";
						} else{
							operation = "in range of +-";
						}
						try{
							writer.write(output + " " + operation + " " + p_settings.getThresholdTheta());
							writer.newLine();
						} catch(Exception e){
							System.out.println("Error writing output verbose train data");
						}
					}


					calculatedOutput[j] = output;

					//Update weights
					double[] neuronWeights = trainingSet.getWeightsForOutput(j);


					if(output != patternSet[j].outputAt(j)){
						//Update bias for single output neuron
						double oldBiasWeight = trainingSet.getBiasWeight(j);
						double newBiasWeight = oldBiasWeight + (p_settings.getLearningRate() * patternSet[patternIndex].outputAt(j));
						trainingSet.updateBiasWeight(j, newBiasWeight); 
						if(verboseTrain){
							try{
								writer.write("\t--- Updating Weights --- \n\tBias Weight " + String.format("Old bias:%10f New Bias:%10f", oldBiasWeight, newBiasWeight));
								writer.write("\n\t[Index]   Old Weight New Weight\n------------------------------\n");
							} catch(Exception e){
								System.out.println("Error writing verbose train data.");
							}
						}

						//Update weight after computation of single output neuron
						double newWeight = -1;
						for(int sampleIndex = 0; sampleIndex < trainingSet.getInputPatternSize(); sampleIndex++){
							double oldWeight = neuronWeights[sampleIndex];
							newWeight = oldWeight + (p_settings.getLearningRate() * patternSet[patternIndex].outputAt(j) * patternSet[patternIndex].inputAt(sampleIndex));
							trainingSet.updateInputWeightForIndex(j, sampleIndex, newWeight);
							if(verboseTrain){
								try{
									writer.write("\t[" + String.format("%8d",sampleIndex) + "]" + " " + String.format("%9f%10f", oldWeight,newWeight));
									writer.newLine();
								} catch (Exception e){
									System.out.println("Error writing update bias verbose train data");
								}
							}
						}	
						if(verboseTrain){
							try{
								writer.write("-----------------------------\n\n");
							} catch(Exception e){
								System.out.println("Error writing verbose train");
							}
						}

					} else {
						System.out.println("Converged at epoch " + currentEpoch);
						converged = true;
					}
				}
			}
			if(verboseTrain){
				try{
					writer.write("-----------------------------\n\n");
				} catch(Exception e){
					System.out.println("Error writing verbose train");
				}
			}
			currentEpoch++;
		}
		if(verboseTrain){
			try{
				writer.close() ;
			} catch(Exception e){
				System.out.println("Error closing writer");
			}
		}
	}

	public void deployNet(){

	}

	private double computeYin(Pattern p, int j){

		int position = -1;
		int rowsOfInput = trainingSet.getInputPatternSize() / trainingSet.getOutputPatternSize(); 
		double [][] weights = trainingSet.getWeights();
		double sum = trainingSet.getBiasWeight(j);
		for(int i = 0; i < rowsOfInput; i++){
			try {
				sum += weights[i][j] * p.inputAt(i);
			} catch (Exception e){
				System.out.println("Error adding to sum at indexes: input[" + i +"] output[" + j + "]");
			}
		}
		return sum;
	}
}
