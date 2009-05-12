package planning;

import java.util.Stack;

public abstract class Solver {
	
	protected Level level;
	
	public Solver(Level startState) {
		this.level = startState;
	}
	
	public abstract Stack<SolutionPart> solve();

}
