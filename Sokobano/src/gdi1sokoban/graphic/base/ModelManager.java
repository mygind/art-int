package gdi1sokoban.graphic.base;

import gdi1sokoban.base.ResourceManager;
import gdi1sokoban.graphic.Model;

public class ModelManager extends ResourceManager<Model> {
	static private ModelManager _instance;
	public static ModelManager getInstance() {
		if (_instance == null) {
			_instance = new ModelManager();
		}
		return _instance;
	}
}
