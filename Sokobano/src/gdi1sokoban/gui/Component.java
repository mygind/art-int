package gdi1sokoban.gui;

import java.util.LinkedList;

import gdi1sokoban.gui.event.*;

/**
 * This class represents the basic graphic component capable of handling
 * mouse and keyboard events. It has a certain extend on the screen
 * and can obtain the input focus.
 */
public class Component {
	
    private static float _resolution;
	private boolean _focus;
	private boolean _hover;
	private boolean _disabled = false;
	private float _x;
	private float _y;
	private float _width;
	private float _height;
	private boolean _isVisible = true;

	LinkedList<MouseListener> _mouseListener = new LinkedList<MouseListener>();
	LinkedList<KeyboardListener> _keyboardListener = new LinkedList<KeyboardListener>();
	LinkedList<ActionListener> _actionListener = new LinkedList<ActionListener>();
	
	Component(float x, float y, float width, float height) {	
		_x = x;
		_y = y;
		_width = width;
		_height = height;
		setFocus(false);
		setHover(false);
	}
	
	public void addMouseListener(MouseListener listener) {
		_mouseListener.add(listener);
	}
	
	public void addKeyboardListener(KeyboardListener listener) {
		_keyboardListener.add(listener);
	}
	
	public void addActionListener(ActionListener listener) {
		_actionListener.add(listener);
	}
	
	public static float getResolution() {
		return _resolution;
	}
	
	public void render() {
	}
	
	public boolean processMouseEvent(MouseEvent event) {
		boolean result = false;
		for (MouseListener listener : _mouseListener)
			result &= listener.mouseEvent(event);
		return result;
	}
	
	public boolean processKeyboardEvent(KeyboardEvent event) {
		boolean result = false;
		for (KeyboardListener listener : _keyboardListener)
			result &= listener.keyboardEvent(event);
		return result;
	}
	
	public void processActionEvent(ActionEvent event) {
		for (ActionListener listener : _actionListener)
			listener.actionEvent(event);
	}
	
	public float getX() {
		return _x;
	}
	
	public float getY() {
		return _y;
	}
	
	public float getWidth() {
		return _width;
	}
	
	public float getHeight() {
		return _height;
	}
	
	public void setVisibility(boolean isVisible) {
		_isVisible = isVisible;
	}
	
	public boolean isVisible() {
		return _isVisible;
	}
	
	protected void setFocus(boolean focus) {
		_focus = focus;
	}
	
	protected void setHover(boolean hover) {
		_hover = hover;
	}
	
	public boolean isFocusable() {
		return isVisible();
	}
	
	public boolean getFocus() {
		return _focus;
	}
	
	public boolean getHover() {
		return _hover;
	}
	
	public void disable() {
		_disabled = true;
	}
	
	public void enable() {
		_disabled = false;
	}
	
	public boolean isEnabled() {
		return _disabled;
	}
	
	/**
	 * Pixels per unit.
	 */
	static public void setResolution(float resolution) {
		_resolution = resolution;
	}
	
	protected boolean contains(float x, float y) {
		return (isVisible() && (x > _x) && (y > _y) && (x < _x + _width) && (y < _y + _height));
	}
}
