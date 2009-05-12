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
		Player player = level.getPlayer();
		
		Queue<Board> unexploredStates = new LinkedList<Board>();	
		unexploredStates.add(level.getBoard());
		
		HashMap<Board, ActionResult> solution = new HashMap<Board, ActionResult>();
		
		Board currentState;
		while((currentState = unexploredStates.poll()) != null){
			System.out.println("GO...");
			Queue<Action> unexploredActions = getPossibleActions(currentState, player);
			
			if(unexploredActions != null){
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
							return finalSolution;
						}
						
						if(solution.containsKey(newState)){
							unexploredStates.add(newState);
							
							solution.put(newState, new ActionResult(newState, currentAction, currentState));
						}
					} catch (IllegalActionException e){
						System.out.println(e.getMessage());
					}
				}
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

		if(actions.size() == 0){
			System.out.println("Error no solutions in:");
			System.out.println(state);
		}
		
		return actions;
	}

}
