import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

public class DeploymentSet extends PatternSet {

	private int correctlyClassified, incorrectlyClassified;
	private String classificationHistory;

	public DeploymentSet(PerceptronSettings ps) {
		super(ps.getDeploymentFile(), ps);
		correctlyClassified = 0;
		incorrectlyClassified = 0;
		classificationHistory = "";
//		printSet();
	}

	public boolean isClassifiedCorrectly(int[] calculatedOutput, int patternIndex) {
		Pattern classifiedPattern = super.patternSet[patternIndex];

		// Check output pattern
		for (int outputIndex = 0; outputIndex < classifiedPattern.getOutputSize(); outputIndex++) {
			if (classifiedPattern.outputAt(outputIndex) != calculatedOutput[outputIndex]) {
				classificationHistory += "Incorrectly classified pattern " + patternIndex + " expected: " + 
						classifiedPattern.getClassificationVector(patternIndex) + 
						" actual: " + Arrays.toString(calculatedOutput) + "\n";
				incorrectlyClassified++;
				return false;
			}
		}
		correctlyClassified++;
		classificationHistory += "Correctly classified pattern " + "\n";
		return true;
	}

	public int numCorrectlyClassifiedPatterns() {
		return correctlyClassified;
	}

	public int numIncorrecltyClassifiedPatterns() {
		return incorrectlyClassified;
	}

	public void logResults() {
		BufferedWriter writer = null;
		String outputFilename = "deployment_results_" + p_settings.getDeploymentFile() + ".txt";
		double acc = ((double)this.correctlyClassified / this.numberOfTrainingPatterns) * 100;
		try {
			writer = new BufferedWriter(new FileWriter(outputFilename));
			writer.write(DIVIDER + "Deployment Results on: " + p_settings.getDeploymentFile() + "\n" + DIVIDER);
			writer.write("Correctly classified: " + this.correctlyClassified + "\nIncorrectly classified: " + this.incorrectlyClassified +
					"\nAccuracy: " + acc + "%\n " + DIVIDER);
			writer.write("\n\n" + DIVIDER + "Full Classification History\n" + DIVIDER + "\n" + classificationHistory + DIVIDER);
			writer.close();
			
			System.out.println("\nRecorderd classification results in: " + outputFilename);

		} catch (Exception e) {
			System.out.println("Error printing results to file. " + e);
		}
	}

}
