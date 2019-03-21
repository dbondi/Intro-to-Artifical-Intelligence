package hw4;

import java.util.*;

/**
 * The main class that handles the entire network
 * Has multiple attributes each with its own use
 */

public class NNImpl {
    private ArrayList<Node> inputNodes; //list of the output layer nodes.
    private ArrayList<Node> hiddenNodes;    //list of the hidden layer nodes
    private ArrayList<Node> outputNodes;    // list of the output layer nodes
    private int num;
    private ArrayList<Instance> trainingSet;    //the training set

    private double learningRate;    // variable to store the learning rate
    private int maxEpoch;   // variable to store the maximum number of epochs
    private Random random;  // random number generator to shuffle the training set

    /**
     * This constructor creates the nodes necessary for the neural network
     * Also connects the nodes of different layers
     * After calling the constructor the last node of both inputNodes and
     * hiddenNodes will be bias nodes.
     */

    NNImpl(ArrayList<Instance> trainingSet, int hiddenNodeCount, Double learningRate, int maxEpoch, Random random, Double[][] hiddenWeights, Double[][] outputWeights) {
        this.trainingSet = trainingSet;
        this.learningRate = learningRate;
        this.maxEpoch = maxEpoch;
        this.random = random;

        //input layer nodes
        inputNodes = new ArrayList<>();
        int inputNodeCount = trainingSet.get(0).attributes.size();
        int outputNodeCount = trainingSet.get(0).classValues.size();
        for (int i = 0; i < inputNodeCount; i++) {
            Node node = new Node(0);
            inputNodes.add(node);
        }

        //bias node from input layer to hidden
        Node biasToHidden = new Node(1);
        inputNodes.add(biasToHidden);

        //hidden layer nodes
        hiddenNodes = new ArrayList<>();
        for (int i = 0; i < hiddenNodeCount; i++) {
            Node node = new Node(2);
            //Connecting hidden layer nodes with input layer nodes
            for (int j = 0; j < inputNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(inputNodes.get(j), hiddenWeights[i][j]);
                node.parents.add(nwp);
            }
            hiddenNodes.add(node);
        }

        //bias node from hidden layer to output
        Node biasToOutput = new Node(3);
        hiddenNodes.add(biasToOutput);

        //Output node layer
        outputNodes = new ArrayList<>();
        for (int i = 0; i < outputNodeCount; i++) {
            Node node = new Node(4);
            //Connecting output layer nodes with hidden layer nodes
            for (int j = 0; j < hiddenNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(hiddenNodes.get(j), outputWeights[i][j]);
                node.parents.add(nwp);
            }
            outputNodes.add(node);
        }
    }

    /**
     * Get the prediction from the neural network for a single instance
     * Return the idx with highest output values. For example if the outputs
     * of the outputNodes are [0.1, 0.5, 0.2], it should return 1.
     * The parameter is a single instance
     */

    public int predict(Instance instance) {
    	ArrayList<Double> inputValues = instance.attributes;
    	
    	for(int i =1; i<inputValues.size();i++){
    		inputNodes.get(i).setInput(inputValues.get(i));
    	}
    	
    	//calculate output for hidden nodes
    	for(int i = 0; i<hiddenNodes.size();i++){
    		hiddenNodes.get(i).calculateOutput(hiddenNodes);
    	}
    	ArrayList<Double> outputValues = new ArrayList<Double>();
    	for(int i = 0; i<outputNodes.size();i++){
    		outputNodes.get(i).calculateOutput(outputNodes);
    		outputValues.add(outputNodes.get(i).getOutput());
    		
    	}
    	
    	
    	int highestValueIdx=0;
    	double highestValue=0;
    	int i =0;
    	for(double val: outputValues){
    		if(val>highestValue){
    			highestValue=val;
    			highestValueIdx=i;
    		}
    		i++;
    	}
    	
    	return highestValueIdx;
    	
    }
    
    public void intialize(Instance instance) {

    	for(int i =0; i<instance.attributes.size();i++){
    		this.inputNodes.get(i).setInput(instance.attributes.get(i));
    	}
    	
    	//calculate output for hidden nodes
    	for(int i = 0; i<hiddenNodes.size();i++){
    		this.hiddenNodes.get(i).calculateOutput(hiddenNodes);
    	}
    	for(int i = 0; i<outputNodes.size();i++){
    		this.outputNodes.get(i).calculateOutput(outputNodes);
    		
    	}
    	

    }
    
    public ArrayList<Double> predictDouble(Instance instance) {

    	ArrayList<Double> outputValues = new ArrayList<Double>();
    	
    	for(int i = 0; i<outputNodes.size();i++){
    		outputValues.add(outputNodes.get(i).getOutput());
    		
    	}
    	return outputValues;
    	
    }
   
    


    /**
     * Train the neural networks with the given parameters
     * <p>
     * The parameters are stored as attributes of this class
     */

    public void train() {
    	
    	double lossTotal=0.0;
    	
        for(int i=0; i<maxEpoch;i++){
        	Collections.shuffle(trainingSet, random);
        	lossTotal=0.0;
        	
        	for(Instance in: trainingSet){
        		
        		for(int k1 = 0; k1 < inputNodes.size();k1++){
        			inputNodes.get(k1).resetGradient();
        		}
        		for(int j1 = 0; j1 < outputNodes.size();j1++){
        			outputNodes.get(j1).resetGradient();
        		}
        		
        		for(int v1 = 0; v1 < hiddenNodes.size();v1++){
        			hiddenNodes.get(v1).resetGradient();
        		
        		}
        		intialize(in);
        		
        		for(int j = 0; j < outputNodes.size();j++){
        			outputNodes.get(j).calculateDelta(in.classValues.get(j));

        		}
        		for(int k = 0; k < hiddenNodes.size();k++){
        			hiddenNodes.get(k).calculateDelta(0);

        		}
        		for(int k = 0; k < outputNodes.size();k++){
        			outputNodes.get(k).updateWeight(learningRate);
        		}
        		for(int k = 0; k < hiddenNodes.size();k++){
        			hiddenNodes.get(k).updateWeight(learningRate);
        		}
        	
        		}
        	
        		for(Instance in:trainingSet){
        			intialize(in);
        			lossTotal+=loss(in);
            		
        		}
        		
        	
        	double print = 1/Double.valueOf(trainingSet.size())*lossTotal;
    		System.out.print("Epoch: "+i+", Loss: ");
    		System.out.printf("%.8e\n", print);
        }
        }
    	
    

    /**
     * Calculate the cross entropy loss from the neural network for
     * a single instance.
     * The parameter is a single instance
     */
    //pretty sure this is right
    private double loss(Instance instance) {
    	double crossOut = 0.0;
    	ArrayList<Double> percentValue = predictDouble(instance);
    	
        for(int i =0;i<outputNodes.size();i++){
        	
        	crossOut += Double.valueOf(instance.classValues.get(i))*Math.log(percentValue.get(i));
        	}
        return -crossOut;
    }
}
