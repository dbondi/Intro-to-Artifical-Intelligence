package hw2;

public class studentAI extends Player {
    private int maxDepth;

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public void move(BoardState state) {
        move = alphabetaSearch(state,maxDepth);
    }

    public int alphabetaSearch(BoardState state, int maxDepth) {
    	int alpha = Integer.MIN_VALUE;
    	int nextMov = -1;
    	for(int i = 0; i < 6 ; i++){
    		if(state.isLegalMove(1, i)){
    			int v = minValue(state.applyMove(1,i),maxDepth,maxDepth-1,alpha,Integer.MAX_VALUE);
    			if(alpha == Integer.MIN_VALUE){
    				nextMov = i;
    				alpha = v;
    			}
    			else if(alpha < v){
    				nextMov = i;
    				alpha = v;
    			}
    		}
    		
    	}
    	return nextMov;
    }
    public int maxValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta) {
    	if(currentDepth==0){
    		return sbe(state);
    	}
    	int v = Integer.MIN_VALUE;
    	for(int i = 0; i < 6 ; i++){
    		if(state.isLegalMove(1, i)){
    			int nextDepth = currentDepth - 1;
    			v = Math.max(v, minValue(state.applyMove(1, i),maxDepth,nextDepth,alpha,beta));
    			if(v>=beta){
    				return v;
    			}
    			alpha=Math.max(alpha, v);
    		}
    	}
    	return v;
    }

    public int minValue(BoardState state, int maxDepth, int currentDepth, int alpha, int beta) {
    	if(currentDepth==0){
    		return sbe(state);
    	}
    	int v = Integer.MAX_VALUE;
    	for(int i = 0; i < 6 ; i++){
    		if(state.isLegalMove(2, i)){
    			int nextDepth = currentDepth - 1;
    			v = Math.min(v, maxValue(state.applyMove(2, i),maxDepth,nextDepth,alpha,beta));
    			if(v<=alpha){
    				return v;
    			}
    			beta=Math.min(beta, v);
    		}
    	}
    	return v;
    }

    public int sbe(BoardState state){
    	return state.getMyScore(1)-state.getMyScore(2);
    }


}