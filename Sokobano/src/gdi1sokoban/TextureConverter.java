package gdi1sokoban;

import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

/**
 * This class converts textures from any format to a fast-to-load,
 * compatible PNG-formated picture.
 */
public class TextureConverter {
	
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
    
	public static void main(String[] args) {
		System.out.println("Given path: " + args[0]);
		convertDir(new File(args[0]));
	}

	public static void convertDir(File dir) {

	   File[] files = dir.listFiles();
	   if (files != null) {
	      for (int i = 0; i < files.length; i++) {
	    	 
	    	 if (files[i].isFile())
	    		 if ((files[i].getName().endsWith(".png")) ||
	    			 (files[i].getName().endsWith(".jpg")) ||
	    			 (files[i].getName().endsWith(".bmp")))
	    			 
	    		 convertFile(files[i]);
	    	 
	         if (files[i].isDirectory()) {
	        	 convertDir(files[i]);
	         }
	      }
	   }
	}
	
	static public void convertFile(File file) {

		System.out.println("Converting: " + file.getName());
		
	    BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
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
		
		try {
			ImageIO.write(compatibleImage, "png", new File(file.getPath() + ".tex"));
		} catch (IOException e) {
			System.out.println("File was not converted.");
			e.printStackTrace();
		}
	}
	
	static public int getClosestPower2(int number) {
		int result = 2;
		while (result < number) {
			result *= 2;
		}
		return result;
	}
}
