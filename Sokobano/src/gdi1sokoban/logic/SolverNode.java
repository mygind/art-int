package gdi1sokoban.logic;

import java.nio.IntBuffer;

/**
 * 
 * Ein SolverNode ist Teil des Zugbaumes eines Sokoban-Spieles.
 * Jeder SolverNode ist mit dem entsprechenden SolverKey, der eindeutigen
 * Spielsituation verbunden. Über den SolverNode und seine Vorgänger lässt
 * sich die Entstehung eines SolverKeys aus der Startsituation zurück-
 * verfolgen. 
 * 
 * @author Martin
 */
public class SolverNode {
	/**
	 * @param _parent
	 * @param _worker
	 */
	public SolverNode(SolverNode _parent, Position _worker, IntBuffer key) {
		super();
		this._parent = _parent;
		this._worker = _worker;
		_key = key;
	}
	
	public SolverNode(Position _worker, IntBuffer key) {
		super();
		this._parent = null;
		this._worker = _worker;
		_key = key;
	}
	public Position getWorker() { return _worker;}
	public IntBuffer getKey() { return _key;}
	public SolverNode getParent() { return _parent;}
	SolverNode _parent;
	Position _worker;
	IntBuffer _key;
	//int _moves;
}
