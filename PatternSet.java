
class PatternSet{
	String type;
	int dimension;
	int[] data;

	public PatternSet(){
		this(-1, "NA");
	}
	
	public PatternSet(int dimension, String type){
		this.dimension = dimension;
		this.type = type;
		data = new int[dimension];
	}
}
