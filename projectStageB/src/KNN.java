
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
 
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;

public class KNN {

		public static BufferedReader readDataFile(String filename) {
			BufferedReader inputReader = null;
	 
			try {
				inputReader = new BufferedReader(new FileReader(filename));
				
			} catch (FileNotFoundException ex) {
				System.err.println("File not found: " + filename);
			}
	 
			return inputReader;
		}
	 
		public static void main(String[] args) throws Exception {
			BufferedReader datafile = readDataFile("sentimentData.txt");
	 
			Instances data = new Instances(datafile);
			data.setClassIndex(data.numAttributes() - 1);
	 
			//do not use first and second
			Instance first = data.instance(7);
			//Instance second = data.instance(1);
			data.delete(7);
			//data.delete(2);
	 
			Classifier ibk = new IBk();		
			ibk.buildClassifier(data);
	 
			double class1 = ibk.classifyInstance(first);
			double class2 = ibk.classifyInstance(first);
			
			System.out.println("first: " + class1 + "\nsecond: " + class2);
		}
	
}
