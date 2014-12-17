
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Map;
 import java.util.HashMap;
 import java.lang.Math;
 import java.lang.Object;

public class NaiveBayesClassifierImpl implements NaiveBayesClassifier {
	/**
	 * Trains the classifier with the provided training data and vocabulary size
	 */
	double inst_count=0.0;
	int spam_count=0;
	int ham_count=0;
	int s_word_count=0;
	int h_word_count=0;
	int Vsize;
	Map<String, Integer> spam_dict = new HashMap<String, Integer>();
	Map<String, Integer> ham_dict = new HashMap<String, Integer>();
	 
	@Override
	public void train(Instance[] trainingData, int v)
	{
      Vsize = v;
	  
	  for ( Instance inst : trainingData)
	  {
	    inst_count++;
		if(inst.label == Label.SPAM)
		{
		  spam_count++;
		  for(String word : inst.words)
		  {
			s_word_count ++;
			if(spam_dict.containsKey(word))
			  spam_dict.put(word, spam_dict.get(word) + 1);
			else
			  spam_dict.put(word, 1); 
		  }
		}
		else if(inst.label == Label.HAM)
		{
		  ham_count++;
		  for(String word : inst.words)
          {		  
			h_word_count++;
			if(ham_dict.containsKey(word))
			  ham_dict.put(word, ham_dict.get(word) + 1);
			else
			  ham_dict.put(word, 1); 
		  }
		}
	  }
	}


	@Override
	public double p_l(Label label) {
		// Implement
		if (label == Label.SPAM)
		  return (spam_count/inst_count);
		else
		  return (ham_count/inst_count);
	}


	@Override
	public double p_w_given_l(String word, Label label) {
		// Implement
		double lambda = 0.00001;

		if (label == Label.SPAM)
		{
		  if(spam_dict.containsKey(word))
			return ((spam_dict.get(word)+lambda)/(Vsize*lambda + s_word_count));
		  else
			 return (lambda/(Vsize*lambda + s_word_count));
		}
		else
		{ 
		  if(ham_dict.containsKey(word))
			return ((ham_dict.get(word)+lambda)/(Vsize*lambda + h_word_count));
		  else
			 return (lambda/(Vsize*lambda + h_word_count));
		}

	}
	
	/**
	 * Classifies an array of words as either SPAM or HAM. 
	 */
	@Override
	public ClassifyResult classify(String[] words) {
		// Implement
		ClassifyResult res = new ClassifyResult();
		double tot_spam=0.0;
		double tot_ham=0.0;
		for( String abc : words)
		{
		  tot_spam = tot_spam + Math.log(p_w_given_l(abc, Label.SPAM));
		  tot_ham = tot_ham + Math.log(p_w_given_l(abc, Label.HAM));
		}
		res.log_prob_spam = (Math.log(p_l(Label.SPAM)) + tot_spam);
		res.log_prob_ham = (Math.log(p_l(Label.HAM)) + tot_ham);
		if (res.log_prob_spam > res.log_prob_ham)
		  res.label = Label.SPAM;
		else
		  res.label = Label.HAM;
		return res;
	}
}
