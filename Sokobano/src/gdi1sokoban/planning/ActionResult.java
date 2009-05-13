package gdi1sokoban.planning;

public class ActionResult {

	private Board state, parentState;
	private Action action;
	
	public ActionResult(Board state, Action action, Board parentState){
		this.state = state;
		this.action = action;
		this.parentState = parentState;
	}
	
	public Board getState() {
		return state;
	}
	
	public Action getAction() {
		return action;
	}
	
	public Board getParentState() {
		return parentState;
	}
	
}
