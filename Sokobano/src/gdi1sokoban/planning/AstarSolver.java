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
	boolean doPrint;
	
	public AstarSolver(Board board, Heuristic heuristic) {
		this(board, heuristic, false);
		
	}
	
	public AstarSolver(Board board, Heuristic heuristic, boolean doPrint) {
		super(board);
		this.heuristic = heuristic;
		this.doPrint = doPrint;
	}
	
	@Override
	public Stack<SolutionPart> solve(boolean statMode) {
		HashSet<Board> exploredStates = new HashSet<Board>();
		
		ArrayList<Estimate> unexploredStates = new ArrayList<Estimate>();
		Estimate startEstimate = new Estimate(startState, heuristic.estimate(startState), 0);
		unexploredStates.add(startEstimate);
		HashMap<Board, ActionResult> solution = new HashMap<Board, ActionResult>();
	
		int depth = 0;		
		
		statistics = "depth discovered_states estimated\n";
		while(!unexploredStates.isEmpty()){
			Estimate currentEstimate = unexploredStates.get(0);
			Board currentState = currentEstimate.getBoard();
			
			if(currentState.isCompleted()){
				return getSolutionPath(currentState, solution.get(currentState).getAction(), solution);
			} else if(currentEstimate.getTotalValue() >= Integer.MAX_VALUE/2){
				System.out.println("Nooooo!");
				System.out.println(currentState);
				try{System.in.read();}catch(Exception e){};
			}
			
			unexploredStates.remove(0);
			exploredStates.add(currentState);
			
			Queue<Action> actions = getPossibleActions(currentState, currentState.getPlayer());
			
			if(statMode){
				statistics += currentEstimate.getStepValue() + " " +
						(exploredStates.size()+unexploredStates.size()) + " " +
						currentEstimate.getEstimatedValue() + "\n";
			}
			if(doPrint){
				if(currentEstimate.getStepValue() > depth){
					depth = currentEstimate.getStepValue(); 
					System.out.println("depth discovered_states estimated\n" + 
							           currentEstimate.getStepValue() + " " +
					                   (exploredStates.size()+unexploredStates.size()) + " " +
					                   currentEstimate.getEstimatedValue() + "\n");
				}
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

	@Override
	public String toString() {
		return "Astar using:{\n" + heuristic + "\n}";
	}
}
