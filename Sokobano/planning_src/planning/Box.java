package planning;

public class Box extends MoveableThing {

	private boolean atTarget;
	
	public Box(){
		super();
	}

	public boolean isAtTarget() {
		return atTarget;
	}
	
	public void setAtTarget(boolean atTarget) {
		this.atTarget = atTarget;
	}
	
}
