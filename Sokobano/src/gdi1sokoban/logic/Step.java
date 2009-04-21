package gdi1sokoban.logic;

// CURRENTLY UNUSED
public class Step {
	
	int _step;
	
	public Step(int direction, boolean push) {
		assert(direction < 4);
		_step = direction + (push ? 0 : 0xF0);
	}
	
	public int getDirection() {
		return _step & 0xF;
	}
	
	public boolean isPush() {
		return (_step & 0xF0) == 0xF0;
	}
	
	public int toInt() {
		return _step;
	}
	
	public int hashCode() {
		return toInt();
	}
	
	public boolean equals(Step step) {
		return step.toInt() == _step;
	}
}
