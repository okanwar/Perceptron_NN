public class TrainingPair{

   private String[] input_array;
   private String[] output_array;
   private String classification_string; 
   public void getStrings(String input_string, String output_string, String classification) {
      
       StringTokenizer st = new StringTokenizer(input_string, " ");
       int n = 0;
       while(st.hasMoreTokens()) {
            input_array[n] = st.NextToken;
            n++;
       }
       st = newStringTokenizer(output_string, " ");
       int m = 0;
       while(st.hasMoreTokens()) {
           output_array[m] = st.NextToken;
           m++;
       }
       classification_string = classification;
   }

}
