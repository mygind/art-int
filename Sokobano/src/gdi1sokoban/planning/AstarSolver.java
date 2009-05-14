package gdi1sokoban.planning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import java.util.Stack;

public class AstarSolver extends Solver {

	Heuristic heuristic;
	
	public AstarSolver(Board board, Heuristic heuristic) {
		super(board);
		this.heuristic = heuristic;
	}
	
	@Override
	public Stack<SolutionPart> solve(boolean statMode) {
		HashSet<Board> exploredStates = new HashSet<Board>();
		
		ArrayList<Estimate> unexploredStates = new ArrayList<Estimate>();
		Estimate startEstimate = new Estimate(startState, heuristic.estimate(startState), 0);
		unexploredStates.add(startEstimate);
		HashMap<Board, ActionResult> solution = new HashMap<Board, ActionResult>();
	
				
		statistics = "depth discovered_states estimated\n";
		while(!unexploredStates.isEmpty()){
			Estimate currentEstimate = unexploredStates.get(0);
			Board currentState = currentEstimate.getBoard();
			
			if(currentState.isCompleted()){
				return getSolutionPath(currentState, solution.get(currentState).getAction(), solution);
			}
			
			unexploredStates.remove(0);
			exploredStates.add(currentState);
			
			Queue<Action> actions = getPossibleActions(currentState, currentState.getPlayer());
			
			if(statMode){
				statistics += currentEstimate.getStepValue() + " " +
						(exploredStates.size()+unexploredStates.size()) + " " +
						currentEstimate.getEstimatedValue() + "\n";
			}
			
			while(!actions.isEmpty()){
				
				Action action = actions.poll();
				try{
					Board newState = action.perform();
					
					if(exploredStates.contains(newState)){
						continue; // Skip this action	
					}
					
					int newStep = currentEstimate.getStepValue() + 1;
					boolean updateSolution = false;	
					
					int index = unexploredStates.indexOf(newState);
					if(index < 0){
						Estimate newEstimate = new Estimate(newState, heuristic.estimate(newState), newStep);
						unexploredStates.add(newEstimate);
						Collections.sort(unexploredStates);
						updateSolution = true;
					} else {
						Estimate formerEstimate = unexploredStates.get(index);						
						if(newStep < formerEstimate.getStepValue()){
							formerEstimate.setStepValue(newStep);
							updateSolution = true;
						}
					}
					
					if(updateSolution){
						solution.remove(newState);
						solution.put(newState, new ActionResult(newState, action, currentState));
					}
					
				}catch(IllegalActionException e){
					
				}
			}
			
		}
		// TODO Auto-generated method stub
		return null;
	}

}
