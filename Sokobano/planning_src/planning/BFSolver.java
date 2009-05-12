package planning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class BFSolver extends Solver {
	
	public BFSolver(Level level) {
		super(level);
	}

	public Stack<SolutionPart> solve(){
		
		int maxDepth = 0;
		
		Queue<Board> unexploredStates = new LinkedList<Board>();	
		unexploredStates.add(level.getBoard());

		HashMap<Board, Integer> discoveredStates = new HashMap<Board, Integer>();
		discoveredStates.put(level.getBoard(), new Integer(0));
		
		HashMap<Board, ActionResult> solution = new HashMap<Board, ActionResult>();
		
		Board currentState;
		while((currentState = unexploredStates.poll()) != null){
			Queue<Action> unexploredActions = getPossibleActions(currentState, currentState.getPlayer());
			
			if(unexploredActions.size() > 0){
				
				Action currentAction;
				while((currentAction = unexploredActions.poll()) != null){
					try{
						Board newState = currentAction.perform();
						
						if(newState.isCompleted()){
							Stack<SolutionPart> finalSolution = new Stack<SolutionPart>();
							finalSolution.add(new SolutionPart(currentState, currentAction));
							
							ActionResult ar;
							while((ar = solution.get(currentState)) != null){			
								finalSolution.add(new SolutionPart(ar.getParentState(), ar.getAction()));
								
								currentState = ar.getParentState();
							}
							this.solution = finalSolution;
							return finalSolution;
						}
						
						if(!discoveredStates.containsKey(newState)){
							int depth = discoveredStates.get(currentState).intValue()+1;
							if(depth > maxDepth){
								maxDepth = depth;
								//System.out.println("Depth: " + maxDepth + " States: " + discoveredStates.size());
							}
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
		}

			return null;
	}

	private Queue<Action> getPossibleActions(Board state, Player player) {
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

}
