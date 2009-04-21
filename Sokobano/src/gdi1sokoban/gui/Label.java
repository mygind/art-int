package gdi1sokoban.gui;

import java.awt.geom.AffineTransform;
import java.io.FileInputStream;

import org.lwjgl.opengl.GL11;

import gdi1sokoban.base.ResourceManager;
import gdi1sokoban.graphic.base.*;

/**
 * This component implements the functionality of a simple label. 
 */
public class Label extends Surface {
	
	private static java.awt.Font _javaFont = null;
	
	private TextureManager.Resource _icon = null;
	
	protected ResourceManager<Font>.Resource _font;
	private String _text;

	public Label(float x, float y, float width, float height, String text) throws Exception {
		super(x, y, width, height);		
		initialize(text);
	}
	
	public Label(float x, float y, float width, float height, String text, int cap) throws Exception {
		super(x, y, width, height, cap);		
		initialize(text);
	}
	
	private void initialize(String text) throws Exception {
		// Load font from file:
		if (_javaFont == null)
			_javaFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new FileInputStream("res/fonts/AMERIKA_.ttf"));
		//setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 1), new float[] {1.0f, 0.8f, 0.6f, 0.6f}, new float[] {1, 0, 0, 0});
		setFont(_javaFont, new float[] {1.0f, 1.0f, 1.0f, 0.8f}, new float[] {1, 0, 0, 0});
		
		setText(text);
	}
	
	public void setIconTexture(TextureManager.Resource icon) {
		_icon = icon;
	}
	
	public TextureManager.Resource getIconTexture() {
		return _icon;
	}
	
	public static java.awt.Font getJavaFont() {
		return _javaFont;
	}
	
	public void setText(String text) {
		_text = _font.getInstance().displayedString(text);
	}
	
	public String getText() {
		return _text;
	}
	
	public FontManager.Resource getFont() {
		return _font;
	}
	
	public void setFont(java.awt.Font font, float[] fgColor, float[] bgColor) throws Exception {
		
		// Calculate the font-height in pixel in relation to the component-height:
		font = font.deriveFont(1);
		
		int pixelsHeight = (int)(getResolution() * getHeight());
		AffineTransform trans = new AffineTransform();
		trans.scale(pixelsHeight, pixelsHeight);
		font = font.deriveFont(trans);
		
		_font = FontManager.getInstance().getInstance(new FontDescriptor(font, fgColor, bgColor));
	}
	
	public boolean isFocusable() {
		return false;
	}
	
	protected void renderIconText() {
		
		// Render the icon to a quad:
		GL11.glLoadIdentity();
		GL11.glTranslatef(getX() + getHeight()*0.1f + getHeight()/7f, getY() + getHeight()*0.1f, 0);
		GL11.glScaled(getHeight() * 0.8f, getHeight() * 0.8f, 0);
		
		float iconWidth = 0;
		if (_icon != null) {
			iconWidth = _icon.getInstance().getImageWidth()/(float)_icon.getInstance().getImageHeight()* getHeight() ;
			_icon.getInstance().bind();
			renderQuad();
		}
		
		// Renders the text with a 10 pixel resolution to a grid of 1x1-quads:
		GL11.glLoadIdentity();
		GL11.glTranslatef(getX() + iconWidth / 2f, getY(), 0);
		
		float spacing = getWidth() - iconWidth / 2f - _font.getInstance().calcTextWidth(_text) * getHeight();
		GL11.glTranslatef(spacing/2.0f, 0, 0);
		GL11.glScaled(getHeight(), getHeight(), 0);

		_font.getInstance().render(_text);
	}
	
	public void render()
	{
		// Prepare rendering:
		GL11.glMatrixMode (GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		
		// Render the background to a quad:
		renderBackground(_backgroundLeft, _backgroundMiddle, _backgroundRight);
		renderIconText();

		GL11.glPopMatrix();
	}
}
