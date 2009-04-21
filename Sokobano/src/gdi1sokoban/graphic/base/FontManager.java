package gdi1sokoban.graphic.base;

import gdi1sokoban.base.ResourceManager;

public class FontManager extends ResourceManager<Font> {
	static private FontManager _instance;
	public static FontManager getInstance() {
		if (_instance == null) {
			_instance = new FontManager();
		}
		return _instance;
	}
}
