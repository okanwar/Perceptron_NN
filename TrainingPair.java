import java.util.*;

public class TrainingPair{

	private String[] input_array;
	private String[] output_array;
	private String classification_string; 

	public TrainingPair(int inputPatternSize, int outputPatternSize){
		input_array = new String[inputPatternSize];
		output_array = new String[outputPatternSize];
		classification_string = "";	
	}

	public void getStrings(String input_string, String output_string, String classification) {

		System.out.println(input_string + "\n" + output_string);
		StringTokenizer st = new StringTokenizer(input_string, " ");
		int n = 0;
		while(st.hasMoreTokens()) {
			input_array[n] = st.nextToken();
			n++;
		}
		st = new StringTokenizer(output_string, " ");
		int m = 0;
		while(st.hasMoreTokens()) {
			output_array[m] = st.nextToken();
			m++;
		}
		classification_string = classification;

	}

}
