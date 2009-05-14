package gdi1sokoban.planning.heuristics;

import gdi1sokoban.planning.Board;


public abstract class Heuristic {

	protected Board board;
	public Heuristic(Board board){
		this.board = new Board(board);
	}
	
	public abstract int estimate(Board b);

	public abstract String toString();
}
