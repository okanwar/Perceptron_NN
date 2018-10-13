import java.io.*;
import java.util.*;
import java.lang.*;

public class PatternSet{

	protected String dataFile;
	protected Pattern[] patternSet;

	public PatternSet(String file){
		this.dataFile = file;
		patternSet = null;
	}

	public Pattern[] getPatternSet(){	
		return patternSet;
	}

}
