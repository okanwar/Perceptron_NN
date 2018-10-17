import java.io.*;
import java.util.*;
import java.lang.*;

public class PatternSet {

	protected static final String DIVIDER = "--------------------------------------------------------------------------------------------------------------------\n";
	protected String dataFile;
	protected Pattern[] patternSet;
	protected int inputPatternSize, outputPatternSize, numberOfTrainingPatterns;
	protected boolean setInitialized;
	protected double[][] weights;
	protected double[] biasWeights;
	protected PerceptronSettings p_settings;
	protected boolean setLoaded, weightsInitialized;

	public PatternSet(String file, PerceptronSettings ps) {
		this.dataFile = file;
		this.p_settings = ps;
		this.inputPatternSize = 0;
		this.outputPatternSize = 0;
		this.numberOfTrainingPatterns = 0;
		this.patternSet = null;
		this.weights = null;
		this.biasWeights = null;
		this.setLoaded = false;
		this.weightsInitialized = false;

		if (file != null)
			this.setLoaded = loadSetFromFile();
		this.weightsInitialized = initializeWeights();

		this.setInitialized = this.setLoaded && this.weightsInitialized;

	}

	public boolean setInitialized() {
		return this.setInitialized;
	}

	public Pattern[] getPatternSet() {
		return patternSet;
	}

	public int getOutputPatternSize() {
		return outputPatternSize;
	}

	public int getInputPatternSize() {
		return inputPatternSize;
	}

	public int getNumberOfPatterns() {
		return numberOfTrainingPatterns;
	}

	public double[][] getWeights() {
		return weights;
	}

	public double[] getWeightsForOutput(int output) {
		return weights[output];
	}

	public double getWeightsForOutputAt(int output, int input) {
		return weights[output][input];
	}

	public double[] getBiasWeights() {
		return biasWeights;
	}

	public double getBiasWeight(int outputNeuron) {
		return biasWeights[outputNeuron];
	}

	public void printSet() {
		if (patternSet != null) {
			System.out.println("--- " + dataFile + "---");
			for (int i = 0; i < patternSet.length; i++) {
				patternSet[i].printPair();
			}
		}
	}

	private boolean loadSetFromFile() {
		BufferedReader reader = null;
		String line = "";

		// Parse set
		try {
			reader = new BufferedReader(new FileReader(dataFile));
			int firstThreeLines = 0;

			// Read first three lines
			inputPatternSize = Integer.parseInt(reader.readLine().trim());
			outputPatternSize = Integer.parseInt(reader.readLine().trim());
			numberOfTrainingPatterns = Integer.parseInt(reader.readLine().trim());

			// Setup set
			patternSet = new Pattern[numberOfTrainingPatterns];

			// Begin reading samples
			int linesOfInputPattern = inputPatternSize / outputPatternSize;
			int sampleIndex = 0;
			int trainingSetIndex = 0;
			String inputPattern = "";
			String outputPattern = "";
			String classification = "";

			// Read a single sample
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					if (sampleIndex < linesOfInputPattern) {
						// Reading input pattern
						inputPattern += line + " ";
					} else if (sampleIndex == linesOfInputPattern) {
						// Reading output pattern
						outputPattern = line + " ";
					} else {
						// Reading classification
						classification = line;

						// Create new training pair
						Pattern p1 = new Pattern(inputPatternSize, outputPatternSize);
						p1.getStrings(inputPattern, outputPattern, classification);
						patternSet[trainingSetIndex] = p1;
						trainingSetIndex++;

						// Reset
						sampleIndex = 0;
						inputPattern = "";
						outputPattern = "";

						continue;
					}
					sampleIndex++;
				}
			}
			setInitialized = true;
			return true;
		} catch (FileNotFoundException f) {
			System.out.println("Could not create pattern set for file:" + dataFile);
			return false;
		} catch (Exception e) {
			System.out.println("Error parsing file. " + e);
			return false;
		}
	}

	private boolean initializeWeights() {

		weights = new double[outputPatternSize][inputPatternSize];
		biasWeights = new double[outputPatternSize];
		boolean successfulWeightInitialization = true;

		if (p_settings.getTrainingSet() != null) {
			// For deploying immediately after training
			setWeightsFromTrainingSet();
		} else {
			// Not deploying after training, so two choices
			if (!p_settings.loadWeightsFromFile()) {
				// Initializing weights from scratch
				successfulWeightInitialization = initializeWeightsFromSettings(
						p_settings.initializeWithRandomWeights());
			} else {
				// Loading weights from file
				successfulWeightInitialization = setWeightsFromFile(p_settings.getWeightsFile());
			}
		}

		return successfulWeightInitialization;
	}

	private void setWeightsFromTrainingSet() {
		weights = p_settings.getTrainingSet().getWeights();
		biasWeights = p_settings.getTrainingSet().getBiasWeights();
	}

	private boolean initializeWeightsFromSettings(boolean randomWeights) {
		// Initialize weights randomly or all to 0
		double weightValue = 0;
		Random rand = null;
		if (randomWeights) {
			rand = new Random();
		}
		try {
			// Initialize Neuron weights
			for (int output = 0; output < outputPatternSize; output++) {
				// Initialize all weights of input pattern for output neuron
				for (int input = 0; input < inputPatternSize; input++) {
					if (randomWeights) {
						// Get random weight value
						weightValue = rand.nextDouble() - 0.5;
					}
					weights[output][input] = weightValue;
				}
			}

			// Initialize bias weigts
			weightValue = 0;
			for (int biasCount = 0; biasCount < outputPatternSize; biasCount++) {
				if (randomWeights) {
					// Get random weight value
					weightValue = rand.nextDouble() - 0.5;
				}
				biasWeights[biasCount] = weightValue;
			}

		} catch (Exception e) {
			System.out.println("Error initializing weights. " + e);
			return false;
		}
		return true;
	}

	private boolean setWeightsFromFile(String weightsFile) {
		// Initialize weights from file
		BufferedReader reader = null;
		String line = "";
		try {
			reader = new BufferedReader(new FileReader(weightsFile));
			int inputSize = Integer.parseInt(reader.readLine().trim());
			int outputSize = Integer.parseInt(reader.readLine().trim());
			
			//Verify weights match pattern set
			if (this.inputPatternSize != inputSize || this.outputPatternSize != outputSize) {
				System.out.println("Input and output sizes of weights do not match the pattern set.\n" +
					"Expected input size:" + this.inputPatternSize + " actual:" + inputSize + 
					"\nExpected output size:" + this.outputPatternSize + " actual:" + outputSize);
				return false;
			}
			
			//Threshold
			p_settings.setThresholdTheta(Double.parseDouble(reader.readLine().trim()));
			
			//Begin reading weights
			int patternIndex = 0;
			int readLine = 0;
			while ((line = reader.readLine()) != null) {
				if (!line.isEmpty()) {
					if (readLine == 0) {
						// Read bias weight
						double biasweight = Double.parseDouble(line.trim());
						biasWeights[patternIndex] = Double.parseDouble(line.trim());

					} else if (patternIndex == outputPatternSize) {
						// Read all weights, return 
						return true;
					} else {
						// Readline is all weights for output index
						StringTokenizer st = new StringTokenizer(line, " ");
						int weightIndex = 0;
						while (st.hasMoreTokens()) {
							String token = st.nextToken();
							weights[patternIndex][weightIndex] = Double.parseDouble(token);
							weightIndex++;
						}
					}

					readLine++;
					if (readLine == 2) {
						// Read both weights
						readLine = 0;
						patternIndex++;
					}

				}
			}
		} catch (Exception e) {
			System.out.println("Error reading weights from file. " + e);
			return false;
		}
		return true;
	}
}
