package gdi1sokoban.graphic.base;

import java.awt.image.BufferedImage;
import java.io.IOException;

import gdi1sokoban.base.ResourceDescriptor;

public class TextureDescriptor extends ResourceDescriptor<Texture> {

	BufferedImage _image = null;
	private String _name = null;
	private int _minFilter;
	private int _magFilter;
	
	public TextureDescriptor(String filename, int minFilter, int magFilter) {
		_name = filename;
		_minFilter = minFilter;
		_magFilter = magFilter;
	}
	
	public TextureDescriptor(String name, BufferedImage image, int minFilter, int magFilter) {
		_name = name;
		_image = image;
		_minFilter = minFilter;
		_magFilter = magFilter;
	}
	
	@Override
	public Texture build() throws IOException {
		if (_image == null) return TextureLoader.load(_name, _minFilter, _magFilter);
		else return TextureLoader.load(_image, _minFilter, _magFilter);
	}

	@Override
	public String getId() {
		return _name;
	}
}
