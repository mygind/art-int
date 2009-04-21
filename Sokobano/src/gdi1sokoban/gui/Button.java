package gdi1sokoban.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import gdi1sokoban.graphic.base.TextureDescriptor;
import gdi1sokoban.graphic.base.TextureManager;
import gdi1sokoban.gui.event.KeyboardEvent;
import gdi1sokoban.gui.event.KeyboardListener;
import gdi1sokoban.gui.event.MouseEvent;
import gdi1sokoban.gui.event.ActionEvent;
import gdi1sokoban.gui.event.MouseListener;

/**
 * This Button-class represents the standard click-button
 */
public class Button extends Label {
	
	public static final int ACTION_PRESSED = 10;
	
	private boolean isDown = false;
	private TextureManager.Resource _downLeft = null;
	private TextureManager.Resource _downMiddle = null;
	private TextureManager.Resource _downRight = null;
	
	private TextureManager.Resource _hoverLeft = null;
	private TextureManager.Resource _hoverMiddle = null;
	private TextureManager.Resource _hoverRight = null;
	
	public Button(float x, float y, float width, float height, String text, int cap) throws Exception {
		super(x, y, width, height, text);
		initialize(x, y, width, height, text, cap);
	}
	
	public Button(float x, float y, float width, float height, String text) throws Exception {
		super(x, y, width, height, text);
		initialize(x, y, width, height, text, CAP_BOTH);
	}
	
	private void initialize(float x, float y, float width, float height, String text, int cap) throws Exception {
		
		if ((cap != CAP_RIGHT) && (cap != CAP_NONE)) _backgroundLeft = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_normal_l.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		_backgroundMiddle = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_normal.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		if ((cap != CAP_LEFT) && (cap != CAP_NONE))  _backgroundRight = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_normal_r.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		
		if ((cap != CAP_RIGHT) && (cap != CAP_NONE)) _downLeft = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_down_l.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		_downMiddle = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_down.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		if ((cap != CAP_LEFT) && (cap != CAP_NONE))  _downRight = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_down_r.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		
		if ((cap != CAP_RIGHT) && (cap != CAP_NONE)) _hoverLeft = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_hover_l.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		_hoverMiddle = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_hover.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		if ((cap != CAP_LEFT) && (cap != CAP_NONE)) _hoverRight = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_hover_r.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
	
		addKeyboardListener(new ButtonKeyboardListener());
		addMouseListener(new ButtonMouseListener());
	}
	
	private class ButtonMouseListener implements MouseListener {
		public boolean mouseEvent(MouseEvent event) {
			if (event.getType() == MouseEvent.MOUSE_PRESSED) {
				isDown = true;
			}
			else if (event.getType() == MouseEvent.MOUSE_RELEASED) {
				isDown = false;
				processActionEvent(new ActionEvent(ACTION_PRESSED));
			}
			else if (event.getType() == MouseEvent.MOUSE_EXITED) {
				isDown = false;
			}
			return false;
		}
	}
	
	private class ButtonKeyboardListener implements KeyboardListener {
		public boolean keyboardEvent(KeyboardEvent event) {
			if (event.getState() && (event.getCode() == Keyboard.KEY_RETURN)) {
				processActionEvent(new ActionEvent(ACTION_PRESSED));
			}
			return false;
		}
	}
	
	public boolean isFocusable() {
		return isVisible();
	}
	
	public void setLeftTexture(TextureManager.Resource downLeft, TextureManager.Resource hoverLeft) {
		_downLeft = downLeft;
		_hoverLeft = hoverLeft;
	}
	
	public void setMiddleTexture(TextureManager.Resource downMiddle, TextureManager.Resource hoverMiddle) {
		_downMiddle = downMiddle;
		_hoverMiddle = hoverMiddle;
	}
	
	public void setRightTexture(TextureManager.Resource downRight, TextureManager.Resource hoverRight) {
		_downRight = downRight;
		_hoverRight = hoverRight;
	}
	
	public void render()
	{
		// Prepare rendering:
		GL11.glMatrixMode (GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		
		// Render the background to a quad:
		GL11.glLoadIdentity();
		GL11.glTranslatef(getX(),getY(),0);
		GL11.glScaled(getWidth(), getHeight(), 0);
		
		if (isDown) {
			renderBackground(_downLeft, _downMiddle, _downRight);
		}
		if ((getFocus() || getHover())) {
			renderBackground(_hoverLeft, _hoverMiddle, _hoverRight);
		}
		else {
			renderBackground(_backgroundLeft, _backgroundMiddle, _backgroundRight);
		} 
		
		renderIconText();
		
		GL11.glPopMatrix();
	}
}
