import java.io.*;
import java.util.Random;

public class TrainingSet extends PatternSet{

	private double[][] weights;
	private double [] biasWeights;
	private int inputPatternSize, outputPatternSize, numberOfTrainingPatterns;

	public TrainingSet(String trainingFile, boolean randomWeights){
		super(trainingFile);
		inputPatternSize = outputPatternSize = numberOfTrainingPatterns = -1;
		weights = null;
		biasWeights = null;
		loadTrainingSet();
		initializeWeights(randomWeights);
		printTrainingSet();
	}

	public double[][] getWeights(){
		return weights;
	}

	public double[] getWeightsForOutput(int output){
		return weights[output];
	}

	public double[] getBiasWeights(){
		return biasWeights;
	}

	public double getBiasWeight(int outputNeuron){
		return biasWeights[outputNeuron];
	}

	public void updateBiasWeight(int outputNeuron, double newBias){
		biasWeights[outputNeuron] = newBias;
	}

	public void updateInputWeightForIndex(int outputNeuron, int sampleIndex, double newWeight){
		weights[outputNeuron][sampleIndex] = newWeight;
	}

	public int getOutputPatternSize(){
		return outputPatternSize;
	}

	public int getInputPatternSize(){
		return inputPatternSize;
	}

	public int getNumberOfPatterns(){
		return numberOfTrainingPatterns;
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


	private void loadTrainingSet(){
		BufferedReader reader = null;
		String line = "";

		//Parse training set
		try {
			reader = new BufferedReader (new FileReader (super.dataFile));
			int firstThreeLines = 0;

			//Read first three lines
			inputPatternSize = Integer.parseInt(reader.readLine().trim());
			outputPatternSize = Integer.parseInt(reader.readLine().trim());
			numberOfTrainingPatterns = Integer.parseInt(reader.readLine().trim());

			//Setup training set
			weights = new double[outputPatternSize][inputPatternSize];	
			biasWeights = new double[outputPatternSize];
			super.patternSet = new Pattern[numberOfTrainingPatterns];

			//Begin reading training pairs
			int linesOfInputPattern = inputPatternSize / outputPatternSize; 
			int sampleIndex = 0;
			int trainingSetIndex = 0;
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
						Pattern p1 = new Pattern(inputPatternSize, outputPatternSize);
						p1.getStrings(inputPattern, outputPattern, classification);
						super.patternSet[trainingSetIndex]  = p1;
						trainingSetIndex++;

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


	public void printTrainingSet(){
		printTrainingPattern();
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

	public void printTrainingPattern(){
		if(super.patternSet != null){
			System.out.println("--- Training Set ---");
			for(int i = 0; i < super.patternSet.length; i++){
				super.patternSet[i].printPair();
			}
		}
	}


}
