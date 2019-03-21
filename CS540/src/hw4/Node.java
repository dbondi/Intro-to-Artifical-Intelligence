package hw4;

import java.util.*;

/**
 * Class for internal organization of a Neural Network.
 * There are 5 types of nodes. Check the type attribute of the node for details.
 * Feel free to modify the provided function signatures to fit your own implementation
 */

public class Node {
    private int type = 0; //0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
    public ArrayList<NodeWeightPair> parents = null; //Array List that will contain the parents (including the bias node) with weights if applicable

    private double inputValue = 0.0;
    private double outputValue = 0.0;
    private double outputGradient = 0.0;
    private double delta = 0.0; //input gradient

    //Create a node with a specific type
    Node(int type) {
        if (type > 4 || type < 0) {
            System.out.println("Incorrect value for node type");
            System.exit(1);

        } else {
            this.type = type;
        }

        if (type == 2 || type == 4) {
            parents = new ArrayList<>();
        }
    }

    //For an input node sets the input value which will be the value of a particular attribute
    public void setInput(double inputValue) {
        if (type == 0) {    //If input node
            this.inputValue = inputValue;
        }
    }

    /**
     * Calculate the output of a node.
     * You can get this value by using getOutput()
     */
    public double calculateOutputDenominator(ArrayList<Node> inputNodes) {
    	double inputVal = 0.0;
    	for(Node input: inputNodes){
    		inputVal += Math.exp(input.calculateOutputNumeratorNew(inputNodes));
    	}
    		
    	
    	return inputVal;
    }
    
    public double calculateOutputNumeratorNew(ArrayList<Node> inputNodes) {
    	double inputVal = 0.0;
    	for(NodeWeightPair parent: parents){
    		inputVal += (parent.node.getOutput())*parent.weight;	
    	}

    	return inputVal;
    }
    
    public double calculateOutputNumerator() {
    	double inputVal = 0.0;
    	for(NodeWeightPair parent: parents){
    		
    		inputVal += (parent.node.getOutput())*parent.weight;	
    	}

    	return inputVal;
    }

    public void calculateOutput(ArrayList<Node> inputNodes) {
    	

        if (type == 2 || type == 4) {   //Not an input or bias node
        	double inputVal = calculateOutputNumerator();
        	if(type==2){
        		outputValue = Math.max(0, inputVal);
        	}
        	if(type==4){
        		outputValue = Math.exp(inputVal)/calculateOutputDenominator(inputNodes);
        		
        	}
        	
        }
        
    }
    

    //Gets the output value
    public double getOutput() {

        if (type == 0) {    //Input node
            return inputValue;
        } else if (type == 1 || type == 3) {    //Bias node
            return 1.00;
        } else {
            return outputValue;
        }

    }
    
    public double calculateOutputPrime(double value) {
       if(value<=0.0){
    	   return 0.0;
       }
       else{
    	   return 1.0;
       }
    }

    //Calculate the delta value of a node.
    public double calculateDeltaOut(double value) {
        return value-getOutput();
    }
    public double calculateDeltaHid() {
    	
        	double isInput=0.0;
        		double multiply=calculateOutputPrime(Math.max(0, calculateOutputNumerator()));
        		isInput=multiply*outputGradient;
        		        	
        	return isInput;
        
    }
    public void resetGradient() {
    	outputGradient=0.0;
    }
    public void calculateDelta(double value){
    	
    	if(type == 2){
    		this.delta=calculateDeltaHid();
    		
    	}
    	if(type == 4){
    		this.delta=calculateDeltaOut(value);
    		
    		for(NodeWeightPair parent: parents){
    			parent.node.outputGradient+=delta*parent.weight;
    		}
   
    	}

    }



    //Update the weights between parents node and current node
    public void updateWeight(double learningRate) {
        if (type == 2 || type == 4) {
        	for(NodeWeightPair pi: parents){
        		pi.weight=pi.weight+learningRate*delta*pi.node.getOutput();

        	}
        }
    }
}


