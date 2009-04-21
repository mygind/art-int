package gdi1sokoban.graphic.base;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;

import org.lwjgl.opengl.GL11;

/**
 * Renders text of a chosen font to the OpenGL-viewport.
 * On construction, a 10x10 character-grid is drawn to a texture. Thereafter,
 * for each character an OpenGL-displaylist is build containing a simple quad 
 * on which the specified part of the 10x10 texture is mapped. The rendering 
 * process itself is simple: for each character of a given string, the related
 * displaylist is executed.
 */
public class Font {
	
	TextureManager.Resource _fontGrid;
	int _displaylistBase;
	int[] _charWidths = new int[100];
	int _fontSize;

	/**
	 * Initializes the TextRenderer.
	 * 
	 * @param font the font defines appearance and size of the characters
	 * @param fgColorArray foreground color
	 * @param bgColorArray background color
	 * @throws Exception 
	 */
	public Font(java.awt.Font font, float[] fgColorArray, float[] bgColorArray) throws Exception {

		// 1. Create a texture containing a 10x10 grid of printable characters:
		
		// Calculate the size necessary for a texture image holding the character grid:
		LineMetrics lineMetrics = font.getLineMetrics("W", new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_ON, RenderingHints.VALUE_FRACTIONALMETRICS_ON));
		_fontSize = (int) Math.ceil(lineMetrics.getHeight());
		
		int fontImageSize = (int)(_fontSize * 10);
		
		// Create a buffered image for the character grid:
		BufferedImage image = new BufferedImage(fontImageSize, fontImageSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = image.createGraphics(); 
		
		// Clear image with background color (make transparent if color has alpha value):
		Color bgColor = new Color(bgColorArray[0], bgColorArray[1], bgColorArray[2], bgColorArray[3]);
		
		if (bgColor.getAlpha() < 255)
			graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, (float)bgColor.getAlpha() / 255.0f));
		
		graphics2D.setColor(bgColor);
		graphics2D.fillRect(0, 0, fontImageSize, fontImageSize);
		
		// Prepare to draw characters in foreground color:
		Color fgColor = new Color(fgColorArray[0], fgColorArray[1], fgColorArray[2], fgColorArray[3]);
		
		graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
		graphics2D.setColor(fgColor);
		graphics2D.setFont(font);
		
		graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		
		// Calculate maximum height (descent + height + ascent = total height):
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		int vBorder = fontMetrics.getMaxAscent();
		int advance = fontMetrics.charWidth('W');
		int hBorder = (int) ((float)(_fontSize - advance) / 2f);
		
		// Draw the grid of 100 characters:
		for (int x = 0; x < 10; x++)
			for (int y = 0; y < 10; y++) {
				char character = (char) (32 + ((y * 10) + x));
				graphics2D.drawString(String.valueOf(character), x * _fontSize + hBorder, y * _fontSize + vBorder);
				_charWidths[(y * 10) + x] = fontMetrics.charWidth(character);
			}

		graphics2D.dispose();
			
		// Create a texture holding the grid image:
		_fontGrid = TextureManager.getInstance().getInstance(new TextureDescriptor(buildId(font, fgColorArray, bgColorArray), image, GL11.GL_LINEAR, GL11.GL_LINEAR));

		// 2. Create 100 display lists, one for each character:
		
		// Create a displaylist for each of the 100 characters in texture:
		_displaylistBase = GL11.glGenLists(100); 

		float fontSizeUV = _fontGrid.getInstance().getImageWidth() / (_fontGrid.getInstance().getWidth() * 10.0f);
		
		for (int i = 0; i < 100; i++) {
			
			int x = (i % 10);  // column
			int y = (i / 10);  // row

			// Access characters from left to right, bottom to top:
			float textureCoordU = x * fontSizeUV;
			float textureCoordV = y * fontSizeUV;
			
			// Create a new displaylist for each character:
			GL11.glNewList(_displaylistBase + i, GL11.GL_COMPILE);
			
			// Render quadratic character-polygon:
			GL11.glBegin(GL11.GL_QUADS);

			// Top-left edge:
			GL11.glTexCoord2f(textureCoordU, textureCoordV);
			GL11.glVertex2i(0, 0);
			
			// Bottom-left edge:
			GL11.glTexCoord2f(textureCoordU, textureCoordV + fontSizeUV);
			GL11.glVertex2f(0, 1);
			
			// Bottom-right edge:
			GL11.glTexCoord2f(textureCoordU + fontSizeUV, textureCoordV + fontSizeUV);
			GL11.glVertex2f(1, 1);
			
			// Top-right edge:
			GL11.glTexCoord2f(textureCoordU + fontSizeUV, textureCoordV);
			GL11.glVertex2f(1/*-0.001f*/, 0);
			
			GL11.glEnd();
			
			// Move the quad to the right (advance cursor):
			GL11.glTranslatef(_charWidths[i] / (float) _fontSize, 0, 0);

			GL11.glEndList();
		}
	}
	
	/**
	 * Draws the given text to the OpenGL-viewport
	 * 
	 * @param text the text to be drawn
	 */
	public void render(String text)
	{
		// Enable alpha blending for transparent backgrounds:
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		// Bind the grid texture:
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		_fontGrid.getInstance().bind();
		
		// Prepare to render in 2D:
		//GL11.glLoadIdentity();	
		//GL11.glMatrixMode(GL11.GL_PROJECTION);
		//GL11.glLoadIdentity();
		//GL11.glOrtho(0.0f, Display.getDisplayMode().getWidth(),Display.getDisplayMode().getHeight(), 0.0f, -1.0f, 1.0f);
		//GL11.glTranslatef(x, y, 0);
		
		// Render the text:
		for(int i=0; i < text.length(); i++) {
			int index = text.charAt(i) - 32;
			if (index >= 0 && index < 100)
				GL11.glCallList(_displaylistBase + index);
			}
	}
	
	/**
	 * builds a string from the given text containing only those characters that
	 * can be displayed by the TextRenderer. 
	 * For example, 'Klöße' becomes 'Kle'.
	 * 
	 * @param text the text to be converted
	 * @return the original text without non-displayable characters
	 */
	public String displayedString(String text) {
		StringBuilder result = new StringBuilder();
		
		for(int i=0; i < text.length(); i++) {
			int index = text.charAt(i) - 32;
			if (index >= 0 && index < 100)
				result.append(text.charAt(i));
			}
		return result.toString();
	}
	
	/**
	 * Calculates the width of the given text as he appears on the OpenGL
	 * viewport in pixel.
	 * 
	 * @param text the text whose width is calculated
	 * @return the width in pixels
	 */
	public float calcTextPixelWidth(String text) {
		float result = 0.0f;
		for(int i=0; i < text.length(); i++) {
			int index = text.charAt(i) - 32;
			if ((index >= 0) && (index < 100))
				result += (float)_charWidths[index];
			}
		return result;
	}
	
	/**
	 * Calculates the width of the given text as he appears on the OpenGL
	 * viewport in model units.
	 * 
	 * @param text the text whose width is calculated
	 * @return the width in model units
	 */
	public float calcTextWidth(String text) {
		
		return calcTextPixelWidth(text) / (float) _fontSize;
	}
	
	/**
	 * Calculates the maximum height in pixels of the current font.
	 * 
	 * @return the height in pixels
	 */
	public int getTextHeight() {
		return (int) _fontSize;
	}
	
	public static String buildId(java.awt.Font font, float[] fgColor, float[] bgColor) {
		StringBuilder stringBuilder = new StringBuilder();
		return stringBuilder.append(font.getName())
		.append(font.getTransform())
		.append(fgColor[0]).append(fgColor[1]).append(fgColor[2]).append(fgColor[3])
		.append(bgColor[0]).append(bgColor[1]).append(bgColor[2]).append(bgColor[3])
		.toString();
	}

	protected void finalize() {
		GL11.glDeleteLists(_displaylistBase, 100);
	}
}
