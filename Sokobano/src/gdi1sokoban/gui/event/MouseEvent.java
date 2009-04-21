package gdi1sokoban.gui.event;

public class MouseEvent extends Event {
	
	private int _type;
	private float _x;
	private float _y;
	private int _button;
	private boolean _state;
	
	public static final int MOUSE_RELEASED = 1;
	public static final int MOUSE_PRESSED = 2;
	public static final int MOUSE_MOVED = 3;
	public static final int MOUSE_EXITED = 4;
	public static final int MOUSE_ENTERED = 5;
	
	public MouseEvent(int type, float mouseX, float mouseY, int button, boolean state) {
		_type = type;
		_x = mouseX;
		_y = mouseY;
		_button = button;
		_state = state;
	}
	
	public float getX() {
		return _x;
	}
	
	public float getY() {
		return _y;
	}
	
	public int getButton() {
		return _button;
	}
	
	public boolean getState() {
		return _state;
	}
	
	public int getType() {
		return _type;
	}
	
	public void setType(int type) {
		_type = type;
	}
}
