package gdi1sokoban.planning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BFSolver extends Solver {
	
	public BFSolver(Board board) {
		super(board);
	}

	public Stack<SolutionPart> solve(boolean statMode){
		
		int maxDepth = 0;
		
		Queue<Board> unexploredStates = new LinkedList<Board>();	
		unexploredStates.add(startState);

		HashMap<Board, Integer> discoveredStates = new HashMap<Board, Integer>();
		discoveredStates.put(startState, new Integer(0));
		
		HashMap<Board, ActionResult> solution = new HashMap<Board, ActionResult>();
		
		statistics = "depth discovered_states\n";
		Board currentState;
		while((currentState = unexploredStates.poll()) != null && !killed){
			Queue<Action> unexploredActions = getPossibleActions(currentState, currentState.getPlayer());
			
			if(unexploredActions.size() > 0){
				
				Action currentAction;
				while((currentAction = unexploredActions.poll()) != null){
					try{
						Board newState = currentAction.perform();
						
						if(newState.isCompleted()){
							return getSolutionPath(currentState, currentAction, solution);
						}
						
						if(!discoveredStates.containsKey(newState)){
							int depth = discoveredStates.get(currentState).intValue()+1;
							
							discoveredStates.put(newState, new Integer(depth));
							
							unexploredStates.add(newState);

							solution.put(newState, new ActionResult(newState, currentAction, currentState));
							
						} else {
						}

					} catch (IllegalActionException e){
					}
				}
			} else {
			}
			if(statMode){
			    int  depth =discoveredStates.get(currentState).intValue()+1;
			    Integer d = new Integer(depth);
			    int stateSize = (discoveredStates.size());
			    if ( stateGrowth.get(d) == null || stateGrowth.get(d).intValue() <  
				 stateSize  ) {
				stateGrowth.put(new Integer(depth), new Integer(stateSize));
			    }
			}

		}

			return null;
	}
	
	@Override
	public String toString() {
		return "BFSolver";
	}
}
