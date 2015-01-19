package testing;

import weka.classifiers.trees.J48;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

import java.io.*;

public class Weka {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		//Read the data
		BufferedReader breader = new BufferedReader(new FileReader("E:/Studies/ML-Stuff/Weka/heart_train.arff"));
		
		//Create Instances
		Instances train = new Instances(breader);
		
		//Set the attribute which has the label value
		train.setClassIndex(train.numAttributes() -1);
		
		breader.close();
		
		//Create a decision tree
		J48 tree = new J48();
		
		// Build it using the training set
		tree.buildClassifier(train);
		
		System.out.println(tree);
		
		/* Naive Bayes classifier
		NaiveBayes nb = new NaiveBayes();
		nb.buildClassifier(train); 		
		
		// Build the classifier using cross validation method
		Evaluation eval = new Evaluation(train);
		eval.crossValidateModel(nb, train , 10, 3)  
		
		// Test the model
		eval.evaluateModel(nb, test);	
		
		// Print the result:
		String strSummary = eval.toSummaryString();
		System.out.println(strSummary);
 
		// Get the confusion matrix
		double[][] cmMatrix = eval.confusionMatrix();
		
		// label the test instances
		Instances labeled = new Instances(test);
		for(int i=0; i < test.numInstances(); i++)
		{
			double label = tree.classifyInstance(test.instance(i));
			labeled.instances(i).setClassValue(label);
		}		
		
		// save labelled data
		BufferedWriter writer = new BufferedWriter(new FileWriter(""E:/Studies/ML-Stuff/Weka/labeled.arff"));
		writer.write(labeled.toString());	*/

	}

}
