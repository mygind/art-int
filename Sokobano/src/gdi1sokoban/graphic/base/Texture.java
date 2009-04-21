package gdi1sokoban.graphic.base;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

public class Texture {

	private int target;
    private int textureID;
    private int height;
    private int width;
    private int texWidth;
    private int texHeight;
    
    public Texture(int target, int textureID, int width, int height, int texWidth, int texHeight) {
        this.target = target;
        this.textureID = textureID;
        this.width = width;
        this.height = height;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }
    
    public void bind() {
      GL11.glBindTexture(target, textureID); 
    }
   
    public int getImageHeight() {
        return height;
    }

    public int getImageWidth() {
        return width;
    }
    
    public float getHeight() {
        return texHeight;
    }
    
    public float getWidth() {
        return texWidth;
    }
    
    public void finalize() {
    	 GL11.glDeleteTextures(IntBuffer.wrap(new int[] {textureID}));
    }
}
