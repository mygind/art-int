package gdi1sokoban.gui.event;

public class KeyboardEvent extends Event {
	int _code;
	char _character;
	boolean _state;
	
	public KeyboardEvent(int code, char character, boolean state) {
		_code = code;
		_character = character;
		_state = state;
	}
	
	public int getCode() {
		return _code;
	}

	public char getCharacter() {
		return _character;
	}

	public boolean getState() {
		return _state;
	}
}
