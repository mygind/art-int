package gdi1sokoban.planning;

public class Box extends MoveableThing {

	private boolean atTarget;
	
	public Box(int x, int y){
		super(x, y);
	}

	public boolean isAtTarget() {
		return atTarget;
	}
	
	public void setAtTarget(boolean atTarget) {
		this.atTarget = atTarget;
	}
	
}
