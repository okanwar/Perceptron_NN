import java.io.*;
import java.util.*;
import java.lang.*;

public class PatternSet {

	protected String dataFile;
	protected Pattern[] patternSet;
	protected int inputPatternSize, outputPatternSize, numberOfTrainingPatterns;
	protected boolean setInitialized;
	protected double[][] weights;
	protected double[] biasWeights;
	protected PerceptronSettings p_settings;

	public PatternSet(String file, PerceptronSettings ps) {
		this.dataFile = file;
		this.p_settings = ps;
		patternSet = null;
		setInitialized = false;
		weights = null;
		biasWeights = null;
		if (file != null)
			loadSetFromFile();
		initializeWeights();

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

	private void loadSetFromFile() {
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
		} catch (FileNotFoundException f) {
			System.out.println("Could not create pattern set for file:" + dataFile);
		} catch (Exception e) {
			System.out.println("Error parsing file. " + e);
		}
	}

	private void initializeWeights() {
		double weightValue = 0;
		weights = new double[outputPatternSize][inputPatternSize];
		biasWeights = new double[outputPatternSize];
		
		if (!p_settings.loadWeightsFromFile()) {
			//Initialize weights randomly or all to 0
			boolean randomWeights = p_settings.initializeWithRandomWeights();
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
			}
		} else {
			//Initialize weights from file
			BufferedReader reader = null;
			String line = "";
			try {
				reader = new BufferedReader(new FileReader(p_settings.getWeightsFile()));
//				int sizeofInputPattern = Integer.parseInt(reader.readLine().trim());
//				int sizeofOutputPattern = Integer.parseInt(reader.readLine().trim());
//				weights = new double[sizeofOutputPattern][sizeofInputPattern];
//				biasWeights = new double[sizeofOutputPattern];
				p_settings.setThresholdTheta(Double.parseDouble(reader.readLine().trim()));
				int patternIndex = 0;
				int readLine = 0;
				while ((line = reader.readLine()) != null) {
					if (!line.isEmpty()) {
//						System.out.println(line);
						if (readLine == 0) {
							double biasweight = Double.parseDouble(line.trim());
							biasWeights[patternIndex] = Double.parseDouble(line.trim());
//							System.out.println("BIAS WEIGHT:" + biasweight);

						} else if (patternIndex == outputPatternSize) {
							return;
						} else {
							// Readline is all weights for output index
							StringTokenizer st = new StringTokenizer(line, " ");
							int weightIndex = 0;
							while (st.hasMoreTokens()) {
//								System.out.print("SET WEIGHT FOR: [" + patternIndex + "][" + weightIndex + "]");
								String token = st.nextToken();
//								System.out.println("\t" + token);
								weights[patternIndex][weightIndex] = Double.parseDouble(token);
//								System.out.println(" to " + weights[patternIndex][weightIndex]);
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
				System.out.println("Error reading weights from file" + e);
			}
		}
	}
}
