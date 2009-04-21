package gdi1sokoban.logic.event;

import gdi1sokoban.logic.Position;

public class LevelEvent {
	int _type;
	int _direction;
	Position _pos;
	
	public LevelEvent(int type, Position pos, int direction) {
		_type = type;
		_pos = pos;
		_direction = direction;
	}
	
	public int getType() {
		return _type;
	}
	
	public int getDirection() {
		return _direction;
	}
	
	public Position getPosition() {
		return _pos;
	}
	
	public boolean isType(int type) {
		return _type == type;
	}
}
