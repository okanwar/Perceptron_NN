import java.io.BufferedReader;
import java.io.FileReader;

public class DeploymentSet extends PatternSet {

	int correctlyClassified, incorrectlyClassified; 

	public DeploymentSet(PerceptronSettings ps) {
		super(ps.getDeploymentFile(), ps);
		correctlyClassified = 0; 
		incorrectlyClassified = 0;
//		printSet();
	}

	public boolean isClassifiedCorrectly(int [] calculatedOutput, int patternIndex){
		Pattern classifiedPattern = super.patternSet[patternIndex];
		
		//Check output pattern
		for(int outputIndex= 0; outputIndex < classifiedPattern.getOutputSize(); outputIndex++){
			if(classifiedPattern.outputAt(outputIndex) != calculatedOutput[outputIndex]){
				incorrectlyClassified++;
				return false;
			}
		}
		correctlyClassified++;
		return true;
	}

	public int numCorrectlyClassifiedPatterns(){
		return correctlyClassified;
	}

	public int numIncorrecltyClassifiedPatterns(){
		return incorrectlyClassified;
	}

}
