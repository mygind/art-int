package gdi1sokoban.gui;

import org.lwjgl.opengl.GL11;

import gdi1sokoban.graphic.base.*;

/**
 * This component implements the functionality of a simple label. 
 */
public class Surface extends Component {

	public static final int CAP_BOTH = 0;
	public static final int CAP_NONE = 1;
	public static final int CAP_LEFT = 2;
	public static final int CAP_RIGHT = 3;
	public static final int NONE = 4;
	
	protected TextureManager.Resource _backgroundLeft = null;
	protected TextureManager.Resource _backgroundMiddle = null;
	protected TextureManager.Resource _backgroundRight = null;
	
	public Surface(float x, float y, float width, float height) throws Exception {
		super(x, y, width, height);		
		initialize(x, y, width, height, CAP_NONE);
	}
	
	public Surface(float x, float y, float width, float height, int cap) throws Exception {
		super(x, y, width, height);		
		initialize(x, y, width, height, cap);
	}
	
	private void initialize(float x, float y, float width, float height, int cap) throws Exception {
		
		if ((cap != CAP_RIGHT) && (cap != CAP_NONE) && (cap != NONE)) _backgroundLeft = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_normal_l.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		if (cap != NONE) _backgroundMiddle = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_normal.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		if ((cap != CAP_LEFT) && (cap != CAP_NONE) && (cap != NONE))  _backgroundRight = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/btn_normal_r.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
	}
	
	public void setBackgroundTexture(TextureManager.Resource backgroundLeft, TextureManager.Resource backgroundMiddle, TextureManager.Resource backgroundRight) {
		_backgroundLeft = backgroundLeft;
		_backgroundMiddle = backgroundMiddle;
		_backgroundRight = backgroundRight;
	}
	
	public boolean isFocusable() {
		return false;
	}
	
	protected void renderBackground(TextureManager.Resource left, TextureManager.Resource middle, TextureManager.Resource right) {
		float capLeftWidth = 0;
		
		if (left != null) {
			capLeftWidth = left.getInstance().getImageWidth()/(float)left.getInstance().getImageHeight()* getHeight() ;
			
			GL11.glLoadIdentity();
			GL11.glTranslatef(getX(), getY(), 0);
			GL11.glScaled(capLeftWidth, getHeight(), 0);
			left.getInstance().bind();
			renderQuad();
		}
		
		float capRightWidth = 0;
		
		if (right != null) {
			capRightWidth = right.getInstance().getImageWidth()/(float)right.getInstance().getImageHeight()* getHeight() ;
			GL11.glLoadIdentity();
			GL11.glTranslatef(getX() + getWidth() - capRightWidth, getY(), 0);
			GL11.glScaled(capRightWidth, getHeight(), 0);
			right.getInstance().bind();
			renderQuad();
		}
		
		if (middle != null) {
			GL11.glLoadIdentity();
			GL11.glTranslatef(getX() + capLeftWidth, getY(), 0);
			GL11.glScaled(getWidth() - capLeftWidth - capRightWidth, getHeight(), 0);
			middle.getInstance().bind();
			renderQuad();
		}
	}
	
	protected void renderQuad() {
		GL11.glBegin(GL11.GL_QUADS); // Ein Viereck zeichnen
		GL11.glTexCoord2f(0, 0);  
		GL11.glVertex3f(0, 0, 0); // oben links
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(0, 1, 0); // oben rechts
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1, 1, 0); // unten rechts
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(1, 0, 0); // unten links
		GL11.glEnd();
	}
	
	public void render()
	{
		// Prepare rendering:
		GL11.glMatrixMode (GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		
		// Render the background to a quad:
		renderBackground(_backgroundLeft, _backgroundMiddle, _backgroundRight);
		
		GL11.glPopMatrix();
	}
}
