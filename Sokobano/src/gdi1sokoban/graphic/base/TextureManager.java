package gdi1sokoban.graphic.base;

import gdi1sokoban.base.ResourceManager;

public class TextureManager extends ResourceManager<Texture> {
	static private TextureManager _instance;
	public static TextureManager getInstance() {
		if (_instance == null) {
			_instance = new TextureManager();
		}
		return _instance;
	}
}
