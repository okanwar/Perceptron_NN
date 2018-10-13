import java.io.*;

public class TrainingSet extends PatternSet{

	private double[] weights;
	private int inputPatternSize, outputPatternSize, numberOfTrainingPatterns;

	public TrainingSet(String trainingFile, boolean randomWeights){
		super(trainingFile);
		inputPatternSize = outputPatternSize = numberOfTrainingPatterns = -1;
		weights = null;
		loadTrainingSet();
		initializeWeights(randomWeights);
	}

	public double[] getWeights(){
		return weights;
	}

	public int getOutputPatternSize(){
		return outputPatternSize;
	}

	public int getInputPatternSize(){
		return inputPatternSize;
	}

	private void initializeWeights(boolean randomWeights){
		int weightValue = 0;
		try{
			for(int i = 0; i < inputPatternSize; i++){
				if(randomWeights){
					//Get random weight value
				}
				weights[i] = weightValue;
			}

			System.out.print("--- Weights ---" + "\n{");
			for(int i = 0; i < inputPatternSize; i++){
				System.out.print(" " + weights[i] + " ");
			}
			System.out.println("}");
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
			inputPatternSize = Integer.parseInt(reader.readLine());
			outputPatternSize = Integer.parseInt(reader.readLine());
			numberOfTrainingPatterns = Integer.parseInt(reader.readLine());

			//Setup training set
			weights = new double[inputPatternSize];
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
		printTrainingSet();
	}


	public void printTrainingSet(){
		if(super.patternSet != null){
			System.out.println("--- Training Set ---");
			for(int i = 0; i < super.patternSet.length; i++){
				super.patternSet[i].printPair();
			}
		}
	}


}
