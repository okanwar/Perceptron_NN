import java.io.*;
import java.util.Random;

public class TrainingSet extends PatternSet{
	private static final String DIVIDER = "--------------------------------------------------------------------------------------------------------------------\n";
	private double[][] weights;
	private double [] biasWeights;
	private double maxWeightChange;
	private String weightChanges;

	public TrainingSet(String trainingFile, boolean randomWeights){
		//Setup pattern set
		super(trainingFile);
		
		//Training Set
//		inputPatternSize = outputPatternSize = numberOfTrainingPatterns = -1;
		maxWeightChange = 0;
		weights = new double[outputPatternSize][inputPatternSize];	
		biasWeights = new double[outputPatternSize];
//		loadTrainingSet();
		initializeWeights(randomWeights);
		weightChanges = DIVIDER + "Training Results\n" + DIVIDER ;
		printTrainingSet();
	}

	public double[][] getWeights(){
		return weights;
	}

	public double[] getWeightsForOutput(int output){
		return weights[output];
	}
	
	public double getWeightsForOutputAt(int output, int input) {
		return weights[output][input];
	}

	public double[] getBiasWeights(){
		return biasWeights;
	}

	public double getBiasWeight(int outputNeuron){
		return biasWeights[outputNeuron];
	}

	public void updateBiasWeight(int outputNeuron, double newBias){
		double oldBiasWeight = biasWeights[outputNeuron];
		double weightChange = Math.abs(newBias - oldBiasWeight);
		if( weightChange > maxWeightChange ) maxWeightChange = weightChange;
		biasWeights[outputNeuron] = newBias;
		weightChanges += String.format("%15s%15f%15f%15f ", "Bias", oldBiasWeight, biasWeights[outputNeuron], weightChange) + " | bnew = bold + lr*t\n";
	}

	public void updateInputWeightForIndex(int outputNeuron, int inputNeuron, double newWeight){
		double oldWeight = weights[outputNeuron][inputNeuron];
		double weightChange = Math.abs(newWeight - oldWeight);
		if( weightChange > maxWeightChange ) maxWeightChange = weightChange;
		weights[outputNeuron][inputNeuron] = newWeight;
		weightChanges += String.format("%15s%15f%15f%15f ", "Input", oldWeight, newWeight, weightChange) + " | Wnew = Wold + lr*t*x\n";
	}
	
	public double getMaxWeightChange() {
		return maxWeightChange;
	}
	
	public void resetMaxWeightChange() {
		maxWeightChange = 0;
	}

	private void initializeWeights(boolean randomWeights){
		double weightValue = 0;
		Random rand = null;
		if(randomWeights){
			rand = new Random();
		}
		try{
			//Initialize Neuron weights
			for(int output = 0; output < outputPatternSize; output++){
				//Initialize all weights of input pattern for output neuron 
				for(int input = 0; input < inputPatternSize; input++){
					if(randomWeights){
						//Get random weight value
						weightValue = rand.nextDouble() - 0.5;
					}
					weights[output][input] = weightValue;
				}
			}

			//Initialize bias weigts
			weightValue = 0;
			for(int biasCount = 0; biasCount < outputPatternSize; biasCount++){
				if(randomWeights){
					//Get random weight value
					weightValue = rand.nextDouble() - 0.5;
				}
				biasWeights[biasCount] = weightValue;
			}

		} catch (Exception e){
			System.out.println("Error initializing weights. " + e);
		}
	}

	public void printTrainingSet(){
		printSet();
		printWeights();
	}

	public void printWeights(){
		System.out.print("--- Weights ---" + "\n");
		for(int o = 0; o < outputPatternSize; o++){
			System.out.print("OUTPUT[" + o + "]{");
			for(int i = 0; i < inputPatternSize; i++){
				System.out.print(" " + weights[o][i] + " ");
			}
			System.out.println("}");
			System.out.println("Wbias: " + biasWeights[o] + "\n");
		}
	}
	
	public void sendIncrementStatus(int epoch, int patternNumber ) {
		weightChanges += "\n" + DIVIDER + "Epoch " + epoch + " Pattern " + patternNumber + "\n" + DIVIDER + "\n";
	}
	
	public void sendActivationStatus(double yin, double activation) {
		weightChanges += DIVIDER + "Activation\n" + DIVIDER + "Yin = " + yin + "\nActivation = " + activation + "\n";
	}
	
	public void sendBeginStatus(int outputNeuron) {
		weightChanges += DIVIDER + "Weight Changes for Output Neuron " + outputNeuron + "\n" + DIVIDER +
				String.format("%15s%15s%15s%15s\n", "Type", "Wold", "Wnew", "Change");
	}
	
	public void sendEndStatus() {
		weightChanges += DIVIDER + "\n";
	}

	public void finalizeResults(String file) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file+"_training_output.txt"));
			writer.write(weightChanges);
			writer.close();
		} catch(Exception e) {
			System.out.println("Error writing verbose train results");
		}
	}
    public void weightsWriter(String input_file, PerceptronSettings threshold_theta) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("output_file.txt"));
            for (int i = 0; i < outputPatternSize; i++) {
                    writer.write(biasWeights[i]);
                for(int j = 0; j < inputPatternSize; j++) {
                    writer.write(weights[i][j] + " ");
                }
            }
            writer.write(threshold_theta.getThresholdTheta());
        }
}
