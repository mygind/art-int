package gdi1sokoban.gui.event;

public class ActionEvent extends Event {
	
	int _command;
	
	public ActionEvent(int command) {
		_command = command;
	}
	
	public int getCommand() {
		return _command;
	}
}
