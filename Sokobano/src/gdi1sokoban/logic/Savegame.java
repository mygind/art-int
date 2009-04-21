package gdi1sokoban.logic;

import java.io.Serializable;
import java.util.LinkedList;

public class Savegame implements Serializable{
	
	private static final long serialVersionUID = 1184400735809225051L;

	private LinkedList<Move> _moves;
	private long _time;
	private boolean _isWon;
	
	/**
	 * Creates a new savegame
	 * 
	 * @param moves
	 * @param time
	 */
	public Savegame(LinkedList<Move> moves, long time, boolean isWon) {
		_moves = moves;
		_time = time;
		_isWon = isWon;
	}
	
	public LinkedList<Move> getMoves() {
		return _moves;
	}
	
	public long getTime() {
		return _time;
	}
	
	public boolean isWon() {
		return _isWon;
	}
}
