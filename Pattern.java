import java.util.*;

public class Pattern{

	private int[] input_array;
	private int[] output_array;
	private String classification_string; 

	public Pattern(int inputPatternSize, int outputPatternSize){
		input_array = new int[inputPatternSize];
		output_array = new int[outputPatternSize];
		classification_string = "";	
	}

	public void getStrings(String input_string, String output_string, String classification) {
		tokenizeInput(input_string);
		tokenizeOutput(output_string);
		classification_string = classification;
	}
	
	public int inputAt(int index){
		return input_array[index];
	}

	public int outputAt(int index){
		return output_array[index];
	}
	
	public int getOutputSize() {
		return output_array.length;
	}
	
	public int getInputSize() {
		return input_array.length;
	}

	public String getClassification() {
		return classification_string;
	}
	
	public void setClassification(String s) {
		classification_string = s;
	}
	
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

	public void tokenizeInput(String input_string) {
		StringTokenizer st = new StringTokenizer(input_string, " \t\n");
		int n = 0;
		while(st.hasMoreTokens()) {
			input_array[n] = Integer.parseInt(st.nextToken().trim());
			n++;
		}
	}
	
	public void tokenizeOutput(String output_string) {
		StringTokenizer st = new StringTokenizer(output_string, " ");
		int m = 0;
		while(st.hasMoreTokens()) {
			output_array[m] = Integer.parseInt(st.nextToken().trim());
			m++;
		}
	}
}
