package gdi1sokoban.logic;

import java.io.Serializable;
import java.util.LinkedList;

public class Move implements Serializable{
	private LinkedList<Position> _steps = new LinkedList<Position>();
	private boolean _isPush;
	
	public Move(LinkedList<Position> steps, boolean position) {
		_steps = steps;
		_isPush = position;
	}
	
	public Move(Position step, boolean position) {
		_steps = new LinkedList<Position>();
		_steps.add(step);
		_isPush = position;
	}
	
	public LinkedList<Position> getPath() {
		return _steps;
	}
	
	public boolean isPush() {
		return _isPush;
	}
	
	public int getStepCount() {
		return _steps.size() - 1;
	}
}
