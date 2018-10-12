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

}
