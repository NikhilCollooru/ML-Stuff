
abstract class DecisionTree {
	/**
	 * Evaluates the learned decision tree on a single instance.
	 * @return the classification of the instance
	 */
	abstract public String classify(Instance instance);
	
	/**
	 * Prints the tree in specified format. 
	 */
	abstract public void print();
	

	abstract public void rootInfoGain(DataSet train);
}
