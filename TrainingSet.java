import java.io.*;
import java.util.Random;
import java.util.StringTokenizer;

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

	public TrainingSet(String weightsFile){
		super(null);
		readWeights(weightsFile);
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

	public void readWeights(String weightsFile){
		BufferedReader reader = null;
		String line = "";
		try{
			reader = new BufferedReader( new FileReader(weightsFile) );
			int sizeofInputPattern = Integer.parseInt(reader.readLine().trim());
			int sizeofOutputPattern = Integer.parseInt(reader.readLine().trim());
			weights = new double[sizeofOutputPattern][sizeofInputPattern];
			biasWeights = new double[sizeofOutputPattern];
			int patternIndex = 0;
			int readLine = 0;
			while((line = reader.readLine()) != null){
				if(!line.isEmpty()){
					System.out.println(line);
					if(readLine == 0){
						double biasweight = Double.parseDouble(line.trim());
						biasWeights[patternIndex] = Double.parseDouble(line.trim());	
						System.out.println("BIAS WEIGHT:" + biasweight);	

					} else if(patternIndex == sizeofOutputPattern){
						return;	
					} else {
						//Readline is all weights for output index
						StringTokenizer st = new StringTokenizer(line, " ");
						int weightIndex = 0;
						while(st.hasMoreTokens()){
							System.out.print("SET WEIGHT FOR: [" + patternIndex + "][" + weightIndex + "]");
							String token = st.nextToken();
							System.out.println("\t" + token);
							weights[patternIndex][weightIndex] = Double.parseDouble(token);
							System.out.println(" to " + weights[patternIndex][weightIndex]);	
							weightIndex++;
						}
					}


					readLine++;
					if(readLine == 2){
						//Read both weights
						readLine = 0;
						patternIndex++;
					}

				}
			}
		} catch (Exception e){
			System.out.println("Error reading weights from file" + e);
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
			writer.write(Integer.toString(inputPatternSize));
			writer.newLine();
			writer.write(Integer.toString(outputPatternSize));
			writer.newLine();
			for (int i = 0; i < outputPatternSize; i++) {
				writer.write(Double.toString(biasWeights[i]) + " \n");
				for(int j = 0; j < inputPatternSize; j++) {
					writer.write(Double.toString(weights[i][j]) + " ");
				}
				writer.newLine();
			}
			writer.write(Double.toString(threshold_theta.getThresholdTheta()));
			writer.close();
		} catch (Exception e) {}
	}
}
