package hw3;

/**
 * HW3
 * By Daniel Bondi
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.lang.Math;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 * 
 * You must add code for the 1 member and 4 methods specified below.
 * 
 * See DecisionTree for a description of default methods.
 */
public class DecisionTreeImpl extends DecisionTree {
  private DecTreeNode root;
  //ordered list of class labels
  private List<String> labels; 
  //ordered list of attributes
  private List<String> attributes; 
  //map to ordered discrete values taken by attributes
  private Map<String, List<String>> attributeValues; 
  //map for getting the index
  private HashMap<String,Integer> label_inv;
  private HashMap<String,Integer> attr_inv;

  
  /**
   * Answers static questions about decision trees.
   */
  DecisionTreeImpl() {
    // no code necessary this is void purposefully
  }

  /**
   * Build a decision tree given only a training set.
   * 
   * @param train: the training set
   */
  DecisionTreeImpl(DataSet train) {

    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    //  Homework requirement, learn the decision tree here
    // Get the list of instances via train.instances
    // You should write a recursive helper function to build the tree
    //
    // this.labels contains the possible labels for an instance
    // this.attributes contains the whole set of attribute names

    this.root = DecisionTreeLearning(train.instances,train.attributes,null,null);
    
  }
  
  /**
   * Helper function to build a decision tree
   * 
   * @param curInstances: instances left
   * @param curAttributes: attributes left
   * @param parrAttribute: parents attribute
   * @param parInstances: parents instances
   * 
   * @return DecTreeNode root node
   */
  DecTreeNode DecisionTreeLearning(List<Instance> curInstances, List<String> curAttributes, String parentAttr, List<Instance> parInstances){
	  if(curInstances.isEmpty()){
		  DecTreeNode nodeEmpty = new DecTreeNode(this.labels.get(getLabelIndex(majorityLabel(parInstances))),null,parentAttr,true);
		  return nodeEmpty; 
	  }
	  else if(sameLabel(curInstances)){
		  DecTreeNode nodeEmpty = new DecTreeNode(this.labels.get(getLabelIndex(majorityLabel(curInstances))),null,parentAttr,true);
		  return nodeEmpty;
		  
	  }
	  else if(curAttributes.isEmpty()){
		  DecTreeNode nodeEmpty = new DecTreeNode(this.labels.get(getLabelIndex(majorityLabel(curInstances))),null,parentAttr,true);
		  return nodeEmpty;
	  }
	  else{
		  String highestConEntropyName = null;
		  double highestConEntropyNum = -1;
		  double curConEntropy;
		  for(int i = 0; i< curAttributes.size(); i++){
			  curConEntropy = InfoGain(curInstances,curAttributes.get(i));
			  if(curConEntropy==highestConEntropyNum){
				  //tie breaker
				  //find smallest attribute index
				  if(getAttributeIndex(curAttributes.get(i)) < getAttributeIndex(highestConEntropyName)){
					  highestConEntropyNum = curConEntropy;
					  highestConEntropyName = curAttributes.get(i);
				  }
				  
			  }	  
			  else if(curConEntropy>highestConEntropyNum){
				 highestConEntropyNum = curConEntropy;
				 highestConEntropyName = curAttributes.get(i);
			  }
		  }
		  
	 boolean terminalOrLabel = curAttributes.size()==0 || sameLabel(curInstances); 
	 DecTreeNode tempNode = new DecTreeNode(this.labels.get(getLabelIndex(majorityLabel(curInstances))),highestConEntropyName,parentAttr,terminalOrLabel);

	 if(parentAttr==null){
		 root = tempNode;
	 }
	 
	 int nodeChildren = attributeValues.get(highestConEntropyName).size();

	 List<String> nextAttributes = new ArrayList<String>();
	 List<Instance> labelCount = new ArrayList<Instance>();
	 for(int i = 0; i < nodeChildren; i++){
		 
		 labelCount.clear();
		 nextAttributes.clear();
		 
		 //find instances
		 for(Instance newInstance: curInstances){
			 if(i==getAttributeValueIndex(highestConEntropyName,newInstance.attributes.get(getAttributeIndex(highestConEntropyName)))){
				 labelCount.add(newInstance);
			 }
		 }
		 //calculates next attributes
		 for(String attr: curAttributes){
			 if(!attr.equals(highestConEntropyName)){
				 nextAttributes.add(attr);
			 }
		 }
		 
		 DecTreeNode nextTree = DecisionTreeLearning(labelCount,nextAttributes,attributeValues.get((highestConEntropyName)).get(i),curInstances);
		 tempNode.addChild(nextTree);
	 }
	 return tempNode;
	 
	 }
  }

  boolean sameLabel(List<Instance> instances){
      // Suggested helper function
      // returns if all the instances have the same label
      // labels are in instances.get(i).label
	  boolean labelsSame = true;
	  String firstLabel;
	  if(instances.size()==0){
		  return false;
	  }
	  else{
		  firstLabel=instances.get(0).label;
	  }
      for(int i = 1; i < instances.size();i++){
    	  if(!firstLabel.equals(instances.get(i).label)){
    		  labelsSame = false;
    	  }
      }
      return labelsSame;
  }
  String majorityLabel(List<Instance> instances){
      // Suggested helper function
      // returns the majority label of a list of examples
	  int maxLabelValue = -1;
	  int maxLabelNum = 0;
	  int labelNum = labels.size();
	  
	  int[] labelCounter = new int[labelNum];
	
	  if(instances.size()==0){
		  return null;
	  }
	  //keep track label use in instances
	  for(Instance instance: instances){
		  labelCounter[getLabelIndex(instance.label)]++;
	  }
	  
	 //find label with largest use
	  for(int i = 0; i < labelNum; i++){
		  if(labelCounter[i]>maxLabelValue){
			  maxLabelNum = i;
			  maxLabelValue = labelCounter[i];
		  }
	  }
	  
      return labels.get(maxLabelNum);
  }
  double entropy(List<Instance> instances){
      // Suggested helper function
      // returns the Entropy of a list of examples
      
	  double entropy = 0;
	  double prob = 0;
	  int labelNum = labels.size();
	  int[] count = new int[labelNum];
	  for(Instance instance: instances){
		  count[getLabelIndex(instance.label)]++;
	  }

	  for(int i = 0; i < labelNum; i++){
		  if(count[i] != 0 && instances.size() != 0){
			  //calculates probability
			  prob = Double.valueOf(count[i])/Double.valueOf(instances.size());

			  //calculates entropy based on formula
			  entropy = entropy - prob*Math.log(prob)/Math.log(2);
		  }
	  }
	  return entropy;
	  
	  
	  
  }
  double conditionalEntropy(List<Instance> instances, String attr){
	  double conditionalEntropy = 0;
	  int attributeIndex = getAttributeIndex(attr);
	  int numAttributeTypes = attributeValues.get(attr).size();
	
	  
	  List<ArrayList<Instance>> attrInstances = new ArrayList<ArrayList<Instance>>();
	  ArrayList<Integer> attributeCount = new ArrayList<Integer>(numAttributeTypes);
	  
	  //keep track of attributes used
	  for(int i = 0; i < numAttributeTypes; i++){
		  attrInstances.add(new ArrayList<Instance>());
		  attributeCount.add(0);
	  }
	  
	  
	  int attributeValueIndex;
	  for(Instance instance: instances){
		  attributeValueIndex = getAttributeValueIndex(attr,instance.attributes.get(attributeIndex));
		
		  attrInstances.get(attributeValueIndex).add(instance);	
		  attributeCount.set(attributeValueIndex,attributeCount.get(attributeValueIndex)+1);
	  }
	  
	 
	  for(int i = 0; i < numAttributeTypes; i++){
		  //add together attribute entropy values
		  conditionalEntropy = conditionalEntropy + Double.valueOf(attributeCount.get(i))/Double.valueOf(instances.size())*Double.valueOf(entropy(attrInstances.get(i)));
	  }
	  
	  return conditionalEntropy;
  }
  double InfoGain(List<Instance> instances, String attr){
      // Suggested helper function
      // returns the info gain of a list of examples, given the attribute attr
      return entropy(instances) - conditionalEntropy(instances,attr);
  }
  @Override
  public String classify(Instance instance) {
      // Homework requirement
      // The tree is already built, when this function is called
      // this.root will contain the learnt decision tree.
      // write a recusive helper function, to return the predicted label of instance
	  DecTreeNode tempNode = this.root;
	  String curAttribute;
	  String curInstance;
	  boolean attrFound;
	  DecTreeNode newNode = null;
	  while(!tempNode.terminal){
		  
		 curAttribute = tempNode.attribute;
		 curInstance = instance.attributes.get(getAttributeIndex(curAttribute));
		 attrFound = false; 
		 
		 //search children nodes for instance values
		 for(DecTreeNode node: tempNode.children){
			 if(node.parentAttributeValue.equals(curInstance) && !attrFound){
				 attrFound=true;
				 newNode = node;
			 }
		 }
		 tempNode = newNode;
		 
	  }
	  
	  return tempNode.label;
  }
  @Override
  public void rootInfoGain(DataSet train) {
    this.labels = train.labels;
    this.attributes = train.attributes;
    this.attributeValues = train.attributeValues;
    // Homework requirement
    // Print the Info Gain for using each attribute at the root node
    // The decision tree may not exist when this funcion is called.
    // But you just need to calculate the info gain with each attribute,
    // on the entire training set.
    
    for(int i = 0; i < attributes.size();i++){
    	System.out.format(attributes.get(i)+" " +"%.5f\n", InfoGain(train.instances,attributes.get(i)));
    }
    return;
  }
  @Override
  public void printAccuracy(DataSet test) {
    // Homework requirement
    // Print the accuracy on the test set.
    // The tree is already built, when this function is called
    // You need to call function classify, and compare the predicted labels.
    // List of instances: test.instances 
    // getting the real label: test.instances.get(i).label
	 int numCorrect = 0;
	 int totalNum = test.instances.size();
	 for(int i = 0; i < totalNum;i++){
		 if(test.instances.get(i).label.equals(classify(test.instances.get(i)))){
			 numCorrect++;
		 }
	 }
	 if(totalNum!=0){
		 System.out.format("%.5f\n", Double.valueOf(numCorrect)/Double.valueOf(totalNum));
	 }
	 else{
		 System.out.format("%.5f\n",0);
	 }
    return;
  }
  
  @Override
  /**
   * Print the decision tree in the specified format
   * Do not modify
   */
  public void print() {

    printTreeNode(root, null, 0);
  }

  /**
   * Prints the subtree of the node with each line prefixed by 4 * k spaces.
   * Do not modify
   */
  public void printTreeNode(DecTreeNode p, DecTreeNode parent, int k) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < k; i++) {
      sb.append("    ");
    }
    String value;
    if (parent == null) {
      value = "ROOT";
    } else {
      int attributeValueIndex = this.getAttributeValueIndex(parent.attribute, p.parentAttributeValue);
      value = attributeValues.get(parent.attribute).get(attributeValueIndex);
    }
    sb.append(value);
    if (p.terminal) {
      sb.append(" (" + p.label + ")");
      System.out.println(sb.toString());
    } else {
      sb.append(" {" + p.attribute + "?}");
      System.out.println(sb.toString());
      for (DecTreeNode child : p.children) {
        printTreeNode(child, p, k + 1);
      }
    }
  }

  /**
   * Helper function to get the index of the label in labels list
   */
  private int getLabelIndex(String label) {
    if(label_inv == null){
        this.label_inv = new HashMap<String,Integer>();
        for(int i=0; i < labels.size();i++)
        {
            label_inv.put(labels.get(i),i);
        }
    }
    return label_inv.get(label);
  }
 
  /**
   * Helper function to get the index of the attribute in attributes list
   */
  private int getAttributeIndex(String attr) {
    if(attr_inv == null)
    {
        this.attr_inv = new HashMap<String,Integer>();
        for(int i=0; i < attributes.size();i++)
        {
            attr_inv.put(attributes.get(i),i);
        }
    }
    return attr_inv.get(attr);
  }

  /**
   * Helper function to get the index of the attributeValue in the list for the attribute key in the attributeValues map
   */
  private int getAttributeValueIndex(String attr, String value) {
    for (int i = 0; i < attributeValues.get(attr).size(); i++) {
      if (value.equals(attributeValues.get(attr).get(i))) {
        return i;
      }
    }
    return -1;
  }
}
