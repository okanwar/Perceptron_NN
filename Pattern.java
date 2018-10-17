import java.util.*;

/*
 * Pattern
 * 
 * This object defines a pattern and gives access to the patterns data
 * 
 * @author Michael Dana, Om Kanwar
 */

public class Pattern{

	private int[] input_array;
	private int[] output_array;
	private String classification_string; 

	/*
	 * Constructor
	 * @param inputPatternSize The size of the input pattern
	 * @param outputPatternSize The size of the output pattern
	 */
	public Pattern(int inputPatternSize, int outputPatternSize){
		input_array = new int[inputPatternSize];
		output_array = new int[outputPatternSize];
		classification_string = "";	
	}

	/*
	 * getStrings - Tokenizes strings of input,output and classification for a pattern which are delimited
	 * @param input_string The input of the pattern
	 * @param output_string The output of the pattern
	 * @param classification The classification for this pattern
	 */
	public void getStrings(String input_string, String output_string, String classification) {
		tokenizeInput(input_string);
		tokenizeOutput(output_string);
		classification_string = classification;
	}
	
	
	/*
	 * inputAt - Returns the input of the pattern at a given index
	 * @param index The index of the requested input value
	 * @return Returns the input value of the pattern at the requested index
	 */
	public int inputAt(int index){
		return input_array[index];
	}

	/*
	 * outputAt - Returns the output of the pattern at a given index
	 * @param index The index of the requested output value
	 * @return Returns the output value of the pattern at the requested index
	 */
	public int outputAt(int index){
		return output_array[index];
	}
	
	/*
	 * getOutputSize - returns the size of the output for this pattern
	 * @return Returns the size of the output
	 */
	public int getOutputSize() {
		return output_array.length;
	}
	
	/*
	 * getInputSize - returns the size of the input
	 * @return Returns the size of the input
	 */
	public int getInputSize() {
		return input_array.length;
	}

	/*
	 * getClassificationVector - Returns the classification pattern/output pattern/classification vector of the pattern as a string
	 * @return The string representation of the classification pattern
	 */
	public String getClassificationVector(int index) {
		return Arrays.toString(output_array);
	}
	
	/*
	 * getClassification - Returns the classification label of the pattern 
	 * @return The classification label of the pattern
	 */
	public String getClassification() {
		return classification_string;
	}
	
	/*
	 * setClassification - Sets the classification of the pattern
	 * @param s The new classification of the pattern
	 */
	public void setClassification(String s) {
		classification_string = s;
	}
	
	/*
	 * printPair - Prints the pattern
	 */
	public void printPair(){
		for(int i = 0; i < input_array.length; i++){
			System.out.print(input_array[i] + " ");
			if( (i+1) % output_array.length == 0){
				System.out.println();
			}
		}
		System.out.println();

		for(int j = 0; j < output_array.length; j++){
			System.out.print(output_array[j] + " ");
		}
		System.out.println("\n" + classification_string + "\n");
	}

	/*
	 * tokenizeInput - Tokenizes the input of a pattern
	 * @param input_string The input of the pattern as a string delimiter by space or \n or \t
	 */
	public void tokenizeInput(String input_string) {
		StringTokenizer st = new StringTokenizer(input_string, " \t\n");
		int n = 0;
		while(st.hasMoreTokens()) {
			input_array[n] = Integer.parseInt(st.nextToken().trim());
			n++;
		}
	}
	
	/*
	 * tokenizeOutput - Tokenizes the output of a pattern
	 * @param output_string The output of the pattern as a string delimiter by space or \n or \t
	 */
	public void tokenizeOutput(String output_string) {
		StringTokenizer st = new StringTokenizer(output_string, " ");
		int m = 0;
		while(st.hasMoreTokens()) {
			output_array[m] = Integer.parseInt(st.nextToken().trim());
			m++;
		}
	}
}
