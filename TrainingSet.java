public class TrainingSet{

	private int numberOfTrainingPairs, nextPairIndex;
	private TrainingPair[] trainingSet;
	private boolean setFull;
	
	public TrainingSet(int numPairs){
		numberOfTrainingPairs = numPairs;
		trainingSet = new TrainingPair[nextPairIndex];
		nextPairIndex = 0;
		setFull = false;
	}

	public boolean addPair(TrainingPair newPair){
		if(!setFull){
			trainingSet[nextPairIndex] = newPair;
			nextPairIndex++;
			return true;
		} else {
			return false;
		}
	}

	
}
