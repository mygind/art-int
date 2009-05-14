package gdi1sokoban.planning;


public abstract class Heuristic {

	protected Board board;
	public Heuristic(Board board){
		this.board = new Board(board);
	}
	
	public abstract int estimate(Board b);
		
}
