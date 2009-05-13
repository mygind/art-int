package gdi1sokoban.planning;

import java.util.EmptyStackException;
import java.util.Stack;

public abstract class Solver {
	
	protected Level level;
	protected Stack<SolutionPart> solution;
	
	public Solver(Level startState) {
		this.level = startState;
	}
	
	public String getSolutionString(){
		if(solution != null){
			StringBuilder sb = new StringBuilder();
			boolean done = false;
			while(!done){
				try{
				sb.append(solution.pop());
				} catch(EmptyStackException e){
					done = true;
				}
			}
			return sb.toString();
		
		} else { 
			return "";
		}
	}
	
	public abstract Stack<SolutionPart> solve();

}
