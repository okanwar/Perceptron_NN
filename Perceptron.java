
public class Perceptron{
   private  PerceptronSettings p_settings;

	public Perceptron(PerceptronSettings p_settings){
	    .p_settings = p_settings;
   
    }
	public static train() {
        p_settings.getTrainingFile();
        BufferedReader reader = null;
        String line = "";
        try {
            reader = new BufferedReader (new FileReader (userRunsFile));
            while ((line = reader.readLine()) != null){
                StringTokenizer st = new StringTokenizer(line, " ");
                int index = 0;
                while(st.hasMoreTokens()) {
                    st.NextToken();
                    index++;
                }
            }
        } catch (Exception e) {}
    }
}
