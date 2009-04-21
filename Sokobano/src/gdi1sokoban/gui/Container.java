package gdi1sokoban.gui;

import java.util.LinkedList;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;

import gdi1sokoban.gui.event.*;

/**
 * A Container holds up to several Components as children. Mouse input and
 * keyboard focus are managed by this class.
 */
public class Container extends Component {
	
	LinkedList<Component> _children = new LinkedList<Component>();
	Component _focusChild;
	Component _hoverChild;
	
	Container(float x, float y, float width, float height) {
		super(x, y, width, height);
		_focusChild = this;
		_hoverChild = this;
	}
	
	public void add(Component child) {
		if (_focusChild == this) setFocus(child, true);
		_children.add(child);
	}
	
	public boolean processMouseEvent(MouseEvent event) {

		// Retrieve receiving component:
		Component receiver = _hoverChild;
		
		// Check whether hover has changed:
		if ((receiver == this) || (!receiver.contains(event.getX(), event.getY()))) {
			receiver = this;
			for (Component child : _children)
				if (child.contains(event.getX(), event.getY())) {
					receiver = child;
					break;
				}
		}
		
		if (event.getType() == MouseEvent.MOUSE_PRESSED)
			setFocus(receiver, true);
		
		if (_hoverChild != receiver) {

			if (_hoverChild != this)
				_hoverChild.processMouseEvent(new MouseEvent(MouseEvent.MOUSE_EXITED, event.getX(), event.getY(), event.getButton(), event.getState()));
			_hoverChild.setHover(false);
			_hoverChild = receiver;
			_hoverChild.processMouseEvent(new MouseEvent(MouseEvent.MOUSE_ENTERED, event.getX(), event.getY(), event.getButton(), event.getState()));
			_hoverChild.setHover(true);
		}
		
		if (receiver != this) receiver.processMouseEvent(event);
		super.processMouseEvent(event);
		return true;
	}
	
	public boolean processKeyboardEvent(KeyboardEvent event) {
		
		// Retrieve receiving component:
		Component receiver = _focusChild;
		
		// Apply focus change on [up]/[down] / [Enter]:
		if (event.getState())
		{
			if (_focusChild != this) {
				_focusChild.setFocus(false);
				ListIterator<Component> iChild = null;
				if (event.getCode() == Keyboard.KEY_UP) {
					if (!_children.isEmpty()) {
						
						if (_focusChild == this)
							iChild = _children.listIterator(_children.size());
						else
							iChild = _children.listIterator(_children.indexOf(_focusChild));
						
						while (iChild.hasPrevious()) {
							Component child = iChild.previous();
							if (child.isFocusable()) {
								_focusChild = child;
								break;
							}
						}
					}
				}
				else if (event.getCode() == Keyboard.KEY_DOWN) {
					if (!_children.isEmpty()) {
						
						if (_focusChild == this)
							iChild = _children.listIterator(0);
						else {
							iChild = _children.listIterator(_children.indexOf(_focusChild));
							if (iChild.hasNext()) iChild.next();
						}
						
						while (iChild.hasNext()) {
							Component child = iChild.next();
							if (child.isFocusable() && child.isVisible()) {
								_focusChild = child;
								break;
							}
						}
					}
				}
				_focusChild.setFocus(true);
			}
		}
		
		if (receiver != this) receiver.processKeyboardEvent(event);
		return super.processKeyboardEvent(event);
	}
	
	public void setHover(Component child, boolean hover) {
		_hoverChild.setHover(!hover);
		child.setHover(hover);
		_hoverChild = child;
	}

	public void setFocus(Component child, boolean focus) {
		_focusChild.setFocus(!focus);
		child.setFocus(focus);
		_focusChild = child;
	}

	public void render() {
		for (Component component : _children) {
			if (component.isVisible()) component.render();
		}
		super.render();
	}
}
