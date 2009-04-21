package gdi1sokoban.graphic.base;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * A Set of OpenGL-states. 
 * Rendering a material has the same effect as setting the different states
 * manually one-by-one.
 **/
public class Material {
	
	String _name;
	TextureManager.Resource _texture;
	FloatBuffer _ambientBuffer;
	FloatBuffer _diffuseBuffer;
	
	/**
	 * Initializes the material with the given values.
	 * 
	 * @param name the name of the material
	 * @param ambient the ambient color
	 * @param diffuse the diffuse color
	 * @param texture the texture
	 */
	Material(String name, float[] ambient, float[] diffuse, TextureManager.Resource texture) {
		_name = name;
		setAmbient(ambient);
		setDiffuse(diffuse);
		_texture = texture;
	}
	
	/**
	 * @return name of the material
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Sets the ambient color. The amount of ambient color shown is dependent 
	 * on the ambient light value. Ambient light is undirected.
	 *  
	 * @param ambient the ambient color
	 */
	public void setAmbient(float[] ambient) {
		if ((ambient == null) || (ambient.length != 4)) ambient = new float[] {0.2f, 0.2f, 0.2f, 1.0f};
		_ambientBuffer = BufferUtils.createFloatBuffer(ambient.length);
		_ambientBuffer.put(ambient);
		_ambientBuffer.flip();
	}
	
	/**
	 * Sets the diffuse color. The amount of diffuse color shown is dependent 
	 * on the diffuse light value. Diffuse light is directed.
	 * 
	 * @param diffuse the diffuse color
	 */
	public void setDiffuse(float[] diffuse) {
		if ((diffuse == null) || (diffuse.length != 4)) diffuse = new float[] {0.8f, 0.8f, 0.8f, 1.0f};
		_diffuseBuffer = BufferUtils.createFloatBuffer(diffuse.length);
		_diffuseBuffer.put(diffuse);
		_diffuseBuffer.flip();
	}
	
	/** 
	 * Sets the texture. Textures are only applied during rendering if OpenGL
	 * is in texture rendering mode.
	 * 
	 * @param texture a reference to the texture to apply.
	 */
	public void setTexture(TextureManager.Resource texture) {
		_texture = texture;
	}
	
	/**
	 * Applies the specified OpenGL state-changes.
	 */
	public void render(){
		if (_texture != null) _texture.getInstance().bind();
		//GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, _ambientBuffer);
		//GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, _diffuseBuffer);
	}
}
