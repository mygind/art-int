package gdi1sokoban.graphic.base;

import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Hashtable;


import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * This TextureManager loads and manages texture objects. A texture is a 2D
 * mappable bitmap stored in video memory.
 */
public class TextureLoader {

	static private ColorModel _glAlphaColorModel =
		new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] {8,8,8,8},
			true,
			false,
			ComponentColorModel.TRANSLUCENT,
			DataBuffer.TYPE_BYTE);
    
	static private ColorModel _glColorModel =
    	new ComponentColorModel(
    		ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] {8,8,8,0},
			false,
			false,
			ComponentColorModel.OPAQUE,
			DataBuffer.TYPE_BYTE);
    
    /**
     * Creates a texture object.
     * 
     * @param filename
     * @return
     * @throws IOException
     */
    static public Texture load(String filename, int minFilter, int magFilter) throws IOException {
    	File file = null;
    	try {
    		file = new File(filename + ".tex");
    		return loadPreprocessed(ImageIO.read(file), minFilter, magFilter);
    	} catch (Exception exception) {
    		file =  new File(filename);
    		return load(ImageIO.read(file), minFilter, magFilter);
    	}
    }
    
    static public Texture load(BufferedImage image, int minFilter, int magFilter)
    {
		// Java-Image einlesen:
    	boolean minimized = false;
		int width = getClosestPower2(image.getWidth());
		int height = getClosestPower2(image.getHeight());
		
		while (width > GL11.GL_MAX_TEXTURE_SIZE) {
			width = getClosestPower2(image.getWidth()/2);
			width = getClosestPower2(image.getHeight()/2);
			minimized = true;
		}

		// Java-Image in ein OpenGL-kompatibles Format transformieren:
		BufferedImage compatibleImage;
		WritableRaster raster;
		
		if (image.getColorModel().hasAlpha()) {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 4, null);
			compatibleImage = new BufferedImage(_glAlphaColorModel, raster, false, new Hashtable<String, Object>());
		} else {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, width, height, 3, null);
			compatibleImage = new BufferedImage(_glColorModel, raster, false, new Hashtable<String, Object>());
		}
		
		// Copy the source image into the produced compatible image:
		Graphics graphics = compatibleImage.getGraphics();
		if (minimized)
			graphics.drawImage(image, 0, 0, width, height, null);
		else 
			graphics.drawImage(image, 0, 0, null);
		
		// Access binary pixeldata:
		byte[] data = ((DataBufferByte) compatibleImage.getRaster().getDataBuffer()).getData();
		
		// Store pixeldata in bytebuffer:
		//ByteBuffer imageBuffer = BufferUtils.createByteBuffer(data.length);
		//imageBuffer.put(data); 
		//imageBuffer.flip();
		ByteBuffer imageBuffer = ByteBuffer.wrap(data);
		imageBuffer.position(0);
		
		int pixelFormat = compatibleImage.getColorModel().hasAlpha() ? GL11.GL_RGBA : GL11.GL_RGB;
		
		// Create new Texture-ID:
		int textureID = createTextureID();
		
		// Bind texture:
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		
		// Set texture filter:
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter); 
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter); 
		
		GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE );
		GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE );
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 
				0,
				pixelFormat,
				width,
				height,
				0,
				pixelFormat,
				GL11.GL_UNSIGNED_BYTE,
				imageBuffer);
		
		// Create new texture-object:
		Texture texture = new Texture(GL11.GL_TEXTURE_2D, textureID, image.getWidth(), image.getHeight(), width, height); 
		
		return texture; 
	}
    
    static public Texture loadPreprocessed(BufferedImage image, int minFilter, int magFilter)
    {
		// Access binary pixeldata:
    	byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		
		// Store pixeldata in bytebuffer:
		ByteBuffer imageBuffer = BufferUtils.createByteBuffer(data.length);
		imageBuffer.put(data); 
		imageBuffer.flip();
		
		int pixelFormat = image.getColorModel().hasAlpha() ? GL11.GL_RGBA : GL11.GL_RGB;
		
		// Create new Texture-ID:
		int textureID = createTextureID();
		
		// Bind texture:
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		
		// Set texture filter:
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter); 
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter); 
		
		GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE );
		GL11.glTexParameterf( GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE );
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 
				0,
				pixelFormat,
				image.getWidth(),
				image.getHeight(),
				0,
				pixelFormat,
				GL11.GL_UNSIGNED_BYTE,
				imageBuffer);
		
		// Create new texture-object:
		Texture texture = new Texture(GL11.GL_TEXTURE_2D, textureID, image.getWidth(), image.getHeight(),image.getWidth(), image.getHeight()); 
		
		return texture; 
	}
    
    static private int createTextureID() { 
    	IntBuffer textureIDs = BufferUtils.createIntBuffer(1); 
    	GL11.glGenTextures(textureIDs); 
    	return textureIDs.get(0);
    }
	    
    /**
     * Calculates the closest greater power of 2 to the given number.
     * 
     * @param number The given number
     * @return the closest greater power of 2
     */
    static public int getClosestPower2(int number) {
        int result = 2;
        while (result < number) {
        	result *= 2;
        }
        return result;
    }
}
