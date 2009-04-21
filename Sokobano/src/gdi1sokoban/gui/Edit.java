package gdi1sokoban.gui;

import org.lwjgl.input.Keyboard;

import gdi1sokoban.gui.event.*;

/**
 * This component implements the functionality of a simple text-edit box.
 */
public class Edit extends Button {

	public Edit(float f, float g, float h, float i, String text) throws Exception {
		super(f, g, h, i, text);
		KeyboardListener k = new EditKeyboardListener();
		addKeyboardListener(k);
	}
	
	public Edit(float f, float g, float h, float i, String text, int cap) throws Exception {
		super(f, g, h, i, text, cap);
		KeyboardListener k = new EditKeyboardListener();
		addKeyboardListener(k);
	}
	
	private class EditKeyboardListener implements KeyboardListener {
		public boolean keyboardEvent(KeyboardEvent event) {
			if (event.getState()) {
				if (event.getCode() == Keyboard.KEY_BACK) {
					int index = getText().length() - 1;
					if (index >= 0)
						setText(getText().substring(0, index));
				}
				else {
					String text = getText() + _font.getInstance().displayedString(String.valueOf(event.getCharacter()));
					if (_font.getInstance().calcTextPixelWidth(text) < getWidth()*getResolution())
						setText(text);
				}
			}
			return false;
		}
	}
	
	public void render() {
		String text = getText();
		boolean cursor = getFocus() &&  System.currentTimeMillis() % 1000 < 500;
		if (cursor) setText(getText()+"|");
		
		super.render();
		
		if (cursor) setText(text);
	}

}
