package gdi1sokoban.gui;

import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import gdi1sokoban.SoundMaster;
import gdi1sokoban.graphic.base.*;
import gdi1sokoban.gui.event.*;

/**
 * A frame is a specialized Container defining mouse-cursor and other basic
 * functionality of an entire game screen.
 */
public class Frame extends Container {
   	
	private LinkedList<FrameListener> _frameListener = new LinkedList<FrameListener>();
	private TextureManager.Resource _mouseCursor;
	private static float _mouseX, _mouseY;
	private boolean _mouseDown;
	protected boolean _showCursor = true;
	
    public Frame(Frame frame) {
		super(frame.getX(), frame.getY(), frame.getWidth(),frame.getHeight());
		initialize();
	}
    
    public Frame(float x, float y, float width, float height) {
		super(x, y, width, height);
		initialize();
	}
    
    private void initialize() {
    	SoundMaster.playMenuToggle();
    	
		try {
			_mouseCursor = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/cursor.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		addMouseListener(new FrameMouseListener());
    }
    
    private class FrameMouseListener implements MouseListener {
    	public boolean mouseEvent(MouseEvent event) {
    		if (event.getType() == MouseEvent.MOUSE_MOVED) {
    			_mouseX = event.getX();
    			_mouseY = event.getY();
    		}
    		if (event.getButton() == 0) {
    			_mouseDown = event.getState();
    		}
			return true;
    	}
    }
    
    public void addFrameListener(FrameListener listener) {
    	_frameListener.add(listener);
    }

    public boolean processFrameEvent(FrameEvent event) {
    	boolean result = false;
    	for (FrameListener listener : _frameListener)
    		result &= listener.frameEvent(event);
    	return result;
    }

    public void render() {		
		
		// Render the frame elements such as buttons and labels:
		super.render();
		
		// Mousecursor rendern:
		if (_showCursor) {
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glLoadIdentity();
			GL11.glTranslatef(_mouseX, _mouseY, 0);
			if (_mouseDown) {
				GL11.glScaled(0.07, 0.07, 1);
				//GL11.glTranslatef(-0.5f, -0.5f, 0);
				
			} else {
				GL11.glScaled(0.05, 0.05, 1);
				//GL11.glTranslatef(-0.5f, -0.5f, 0);
			}
			
			_mouseCursor.getInstance().bind();
			
			GL11.glBegin(GL11.GL_QUADS); 	// Ein Viereck zeichnen
			GL11.glTexCoord2f(0, 0); // oben links
			GL11.glVertex3f(0, 0, 0); // oben links
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(0, 1, 0); // oben rechts
			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(1, 1, 0); // unten rechts
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(1, 0, 0); // unten links
			GL11.glEnd();
		}
    }
}