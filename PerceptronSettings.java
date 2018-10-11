
public class PerceptronSettings{
	private String trainingFile, weightsFile;
	private boolean randomWeightValues;
	private int maxEpochs;
	private double learningRate, thresholdTheta, thresholdWeightChanges;

	public PerceptronSettings(){
		this("", "", false, -1, -1, -1, -1);		
	}
	
	public PerceptronSettings( String trainingFile, String weightsFile, boolean randomWeightValues,
		int maxEpochs, double learningRate, double thresholdTheta, double thresholdWeightChanges){
		this.trainingFile = trainingFile;
		this.weightsFile = weightsFile;
		this.randomWeightValues = randomWeightValues;
		this.maxEpochs = maxEpochs;
		this.learningRate = learningRate;
		this.thresholdTheta = thresholdTheta;
		this.thresholdWeightChanges = thresholdWeightChanges;		
	}

	public void setSettings(){
			
	}

	private boolean validLearningRate(){
		return (learningRate > 0 && learningRate <= 1);
	}

}
