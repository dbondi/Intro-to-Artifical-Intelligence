package hw5;

import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifierImpl implements NaiveBayesClassifier {
	private Instance[] m_trainingData;
	private int m_v;
	private double m_delta;
	public int m_sports_count, m_business_count;
	public int m_sports_word_count, m_business_word_count;
	private HashMap<String,Integer> m_map[] = new HashMap[2];

  /**
   * Trains the classifier with the provided training data and vocabulary size
   */
  @Override
  public void train(Instance[] trainingData, int v) {
    // For all the words in the documents, count the number of occurrences. Save in HashMap
    // e.g.
    // m_map[0].get("catch") should return the number of "catch" es, in the documents labeled sports
    // Hint: m_map[0].get("asdasd") would return null, when the word has not appeared before.
    // Use m_map[0].put(word,1) to put the first count in.
    // Use m_map[0].replace(word, count+1) to update the value
  	  m_trainingData = trainingData;
  	  m_v = v;
  	  m_map[0] = new HashMap<String,Integer>();
  	  m_map[1] = new HashMap<String,Integer>();
  	for(Instance e: trainingData){
			if(e.label.equals(Label.SPORTS)){
		 		 for(String word: e.words){
		 			 if(m_map[0].get(word)==null){
		 				m_map[0].put(word,1);
		 			 }
		 			 else{
		 				m_map[0].replace(word, m_map[0].get(word)+1);
		 			 }
		 		 }
			}
			if(e.label.equals(Label.BUSINESS)){
				for(String word: e.words){
		 			 if(m_map[1].get(word)==null){
		 				m_map[1].put(word,1);
		 			 }
		 			 else{
		 				m_map[1].replace(word, m_map[1].get(word)+1);
		 			 }
		 		 }
		}
				
  }
  	
  }

  /*
   * Counts the number of documents for each label
   */
  public void documents_per_label_count(Instance[] trainingData){
	  m_sports_count = 0;
	  m_business_count = 0;
	  for(Instance e: trainingData){
		  if(e.label.equals(Label.SPORTS)){
					m_sports_count++;
		  }
		  if(e.label.equals(Label.BUSINESS)){
					m_business_count++;
			}
					
	  }

  }

  /*
   * Prints the number of documents for each label
   */
  public void print_documents_per_label_count(){
  	  System.out.println("SPORTS=" + m_sports_count);
  	  System.out.println("BUSINESS=" + m_business_count);
  }


  /*
   * Counts the total number of words for each label
   */
  public void words_per_label_count(Instance[] trainingData){
	m_sports_word_count = 0;
    m_business_word_count = 0;
	for(Instance e: trainingData){

		  if(e.label.equals(Label.SPORTS)){
				m_sports_word_count = m_sports_word_count+e.words.length;
		  }
		  if(e.label.equals(Label.BUSINESS)){
				m_business_word_count = m_business_word_count+e.words.length;
		}
				
	}

  }

  /*
   * Prints out the number of words for each label
   */
  public void print_words_per_label_count(){
  	  System.out.println("SPORTS=" + m_sports_word_count);
  	  System.out.println("BUSINESS=" + m_business_word_count);
  }

  /**
   * Returns the prior probability of the label parameter, i.e. P(SPORTS) or P(BUSINESS)
   */
  @Override
  public double p_l(Label label) {
	  double ret = 0;
	  if(label.equals(Label.SPORTS)){
		
			ret=Double.valueOf(m_sports_count/Double.valueOf(m_sports_count+m_business_count));
	  }
	  if(label.equals(Label.BUSINESS)){
			ret=Double.valueOf(m_business_count/Double.valueOf(m_sports_count+m_business_count));
	}
    return ret;
  }

  /**
   * Returns the smoothed conditional probability of the word given the label, i.e. P(word|SPORTS) or
   * P(word|BUSINESS)
   */
  @Override
  public double p_w_given_l(String word, Label label) {
    // Calculate the probability with Laplace smoothing for word in class(label)
    double ret = 0;
    double value = 0;
    m_delta = 0.00001;
	if(label.equals(Label.SPORTS)){
    if(m_map[0].get(word)!=null){
      value=m_map[0].get(word);
    }
		ret=(m_delta+value)/Double.valueOf(Double.valueOf(Double.valueOf(m_v)*m_delta)+Double.valueOf(m_sports_word_count));
	}
	if(label.equals(Label.BUSINESS)){
    if(m_map[1].get(word)!=null){
      value=m_map[1].get(word);
    }
		ret=(m_delta+value)/Double.valueOf(Double.valueOf(Double.valueOf(m_v)*m_delta)+Double.valueOf(m_business_word_count));
    }
    return ret;
  }

  /**
   * Classifies an array of words as either SPORTS or BUSINESS.
   */
  @Override
  public ClassifyResult classify(String[] words) {

    // Sum up the log probabilities for each word in the input data, and the probability of the label
    // Set the label to the class with larger log probability
	  
    ClassifyResult ret = new ClassifyResult();
    documents_per_label_count(m_trainingData);
    words_per_label_count(m_trainingData);
    ret.label = Label.SPORTS;
    ret.log_prob_sports = Math.log(p_l(Label.SPORTS));
    ret.log_prob_business = Math.log(p_l(Label.BUSINESS));
    for(String word: words){
    	ret.log_prob_sports=ret.log_prob_sports+Math.log(p_w_given_l(word,Label.SPORTS));
    	ret.log_prob_business=ret.log_prob_business+Math.log(p_w_given_l(word,Label.BUSINESS));
    }
    if(ret.log_prob_sports<ret.log_prob_business){
    	ret.label=Label.BUSINESS;
    }
    return ret; 
  }
  
  /*
   * Constructs the confusion matrix
   */
  @Override
  public ConfusionMatrix calculate_confusion_matrix(Instance[] testData){

    // Count the true positives, true negatives, false positives, false negatives
	int TP, FP, FN, TN;
    TP = 0;
    FP = 0;
	FN = 0;
	TN = 0;
	
	for(Instance e: testData){
		if(e.label.equals(Label.SPORTS) && classify(e.words).label.equals(Label.SPORTS)){
			TP++;
		}
		else if(e.label.equals(Label.BUSINESS) && classify(e.words).label.equals(Label.BUSINESS)){
			TN++;
		}
		else if(e.label.equals(Label.SPORTS) && classify(e.words).label.equals(Label.BUSINESS)){
			FN++;
		}
		else if(e.label.equals(Label.BUSINESS) && classify(e.words).label.equals(Label.SPORTS)){
			FP++;
		}
	}
    return new ConfusionMatrix(TP,FP,FN,TN);
  }
  
}
