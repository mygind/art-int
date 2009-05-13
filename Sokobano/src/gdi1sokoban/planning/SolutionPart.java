package gdi1sokoban.planning;

public class SolutionPart {

	private Board state;
	private Action action;
	
	public SolutionPart(Board state, Action action){
		this.state = state;
		this.action = action;
	}
	
	public Board getState() {
		return state;
	}
	
	public Action getAction() {
		return action;
	}
	
	@Override
	public String toString() {
		return "" + action;
	}
}
