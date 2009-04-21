package gdi1sokoban.gui.event;

public class FrameEvent {
	public static final int FRAME_EXIT = 0;
	
	private int _type;
	private Object _param;
	
	public FrameEvent(int type, Object param) {
		_param = param;
	}
	
	public int getType() {
		return _type;
	}
	
	public Object getParam() {
		return _param;
	}
}
