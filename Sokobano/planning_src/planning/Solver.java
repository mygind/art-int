package planning;

public abstract class Solver {
	
	protected Level level;
	
	public Solver(Level startState) {
		this.level = startState;
	}
	
	public abstract boolean solve();

}
