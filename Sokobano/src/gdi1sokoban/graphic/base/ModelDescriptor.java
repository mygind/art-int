package gdi1sokoban.graphic.base;

import org.lwjgl.opengl.GL11;

import gdi1sokoban.base.ResourceDescriptor;
import gdi1sokoban.graphic.Model;

public class ModelDescriptor extends ResourceDescriptor<Model> {

	String _filename;
	int _minFilter;
	int _magFilter;
	
	public ModelDescriptor(String filename, int minFilter, int magFilter) {
		_filename = filename;
		_minFilter = minFilter;
		_magFilter = magFilter;
	}
	
	public ModelDescriptor(String filename) {
		_filename = filename;
		_minFilter = GL11.GL_LINEAR;
		_magFilter = GL11.GL_LINEAR;
	}
	
	@Override
	public Model build() throws Exception {
		return ModelLoader.load(_filename, _minFilter, _magFilter);
	}

	@Override
	public String getId() {
		return _filename;
	}
}
