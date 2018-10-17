import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

/*
 * DeploymentSet 
 * 
 * Defines functions and properties specific to deploying a net
 * 
 * @author Michael Dana, Om Kanwar
 */

public class DeploymentSet extends PatternSet {

	private int correctlyClassified, incorrectlyClassified;
	private String classificationHistory;

	/*
	 * Constructor
	 * @param ps PerceptronSettings object containing perceptron settings
	 */
	public DeploymentSet(PerceptronSettings ps) {
		super(ps.getDeploymentFile(), ps);
		correctlyClassified = 0;
		incorrectlyClassified = 0;
		classificationHistory = "";
//		printSet();
	}

	/*
	 * isClassifiedCorrectly - Determines if a given output was classified correctly given a pattern index
	 * @param calculatedOutput The output that is being tested for correctness
	 * @param patternIndex The pattern the output is being compared to
	 * @return Returns a boolean indicating a correct classification(true) or incorrect classification(false)
	 */
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

	/*
	 * numCorrectlyClassifiedPatterns - Returns number of correctly classified patterns
	 * @return Returns an int,the number of correctly classified patterns
	 */
	public int numCorrectlyClassifiedPatterns() {
		return correctlyClassified;
	}

	/*
	 * numIncorrecltyClassifiedPatterns - Returns the number of incorrectly classified patterns
	 * @return Returns an int, the number of incorrectly classified patterns
	 */
	public int numIncorrecltyClassifiedPatterns() {
		return incorrectlyClassified;
	}

	/*
	 * logResults - Logs the results of deployment to a file deplployment_results_deploymentfile.txt
	 */
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
