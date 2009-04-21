package gdi1sokoban.graphic;

import gdi1sokoban.graphic.base.ModelManager;
import gdi1sokoban.logic.SkinIdentifier;

import java.util.List;
import java.util.Vector;

public class Skin extends SkinIdentifier{
	
	public static final int CRATE_NORMAL = 0;
	public static final int CRATE_DEADLOCK = 1;
	public static final int CRATE_TARGET = 2;
	
	List<Tile> _walls;
	List<Tile> _targets;
	List<Tile> _floors;
	Vector<Animation> _crates;
	ModelManager.Resource _skybox;
	
	public Skin(List<Tile> walls, List<Tile> floors, List<Tile> targets, Vector<Animation> crates, ModelManager.Resource skybox, SkinIdentifier ident) {
		super(ident);
		_walls = walls;
		_floors = floors;
		_crates = crates;
		_targets = targets;
		_skybox = skybox;
	}
	
	public List<Tile> getWalls() {
		return _walls;
	}
	
	public List<Tile> getFloors() {
		return _floors;
	}
	
	public List<Tile> getTargets() {
		return _targets;
	}
	
	public Vector<Animation> getCrates() {
		return _crates;
	}
	
	public ModelManager.Resource getSkybox() {
		return _skybox;
	}
}
