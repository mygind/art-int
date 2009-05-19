package gdi1sokoban.planning;

import java.util.Collections;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public abstract class Solver {
	
	protected Board startState;
	protected Stack<SolutionPart> finalSolution;
        protected HashMap<Integer,Integer> stateGrowth;

	protected String statistics;
        protected boolean killed =false;
        protected long execTime;

	public Solver(Board startState) {
		this.startState = startState;
		this.statistics = "";
		this.stateGrowth = new HashMap<Integer,Integer>();
	}
	
	public String getSolutionString(){
		if(finalSolution != null){
			StringBuilder sb = new StringBuilder();
			boolean done = false;
			while(!done){
				try{
				sb.append(finalSolution.pop());
				} catch(EmptyStackException e){
					done = true;
				}
			}
			return sb.toString();
		
		} else { 
			return "";
		}
	}
	
    public void killMe(){
	killed =true;
    }

	public abstract Stack<SolutionPart> solve(boolean statMode);

	public String getStatistics() {
		return statistics;
	}
	
	protected Queue<Action> getPossibleActions(Board state, Player player) {
		int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		Queue<Action> actions = new LinkedList<Action>();
		
		for (int i = 0; i < directions.length; i++) {
			try {
				Move m = new Move(state, player.getX(), player.getY(),
						directions[i][0], directions[i][1], player);
				actions.add(m);
			} catch (IllegalActionException e) {
			}
			try {
				Push p = new Push(state, player.getX(), player.getY(),
						directions[i][0], directions[i][1], player);
				actions.add(p);
			} catch (IllegalActionException e) {
			}
			try {
				PushToTarget ptt = new PushToTarget(state, player.getX(),
						player.getY(), directions[i][0], directions[i][1],
						player);
				actions.add(ptt);
			} catch (IllegalActionException e) {
			}

		}
		
		return actions;
	}
	
	protected Stack<SolutionPart> getSolutionPath(Board lastState, Action lastAction, HashMap<Board, ActionResult> solution){
		Stack<SolutionPart> newSolution = new Stack<SolutionPart>();
		newSolution.add(new SolutionPart(lastState, lastAction));
		
		ActionResult ar;
		while((ar = solution.get(lastState)) != null){			
			newSolution.add(new SolutionPart(ar.getParentState(), ar.getAction()));
			
			lastState = ar.getParentState();
		}
		this.finalSolution = newSolution;
		return newSolution;		
	}
	
	public abstract String toString();
    
        public void setExecutionTime(long execTime){
	    this.execTime = execTime;
	}

    public HashMap<Integer, Integer> getGrowthHistory(){
	return stateGrowth;
    }

        public String getStats(){

	    StringBuilder stats = new StringBuilder();
	    ArrayList<String> list = new ArrayList<String>();
	    
	    //all depths
	    for ( Integer i : stateGrowth.keySet() ){
		stats.append(i+"\t"+stateGrowth.get(i)+"\n");
	    }
	    
	    return stats.toString();
	}

}
