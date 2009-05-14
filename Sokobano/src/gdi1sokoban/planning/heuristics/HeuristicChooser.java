package gdi1sokoban.planning.heuristics;

import gdi1sokoban.planning.Board;

public class HeuristicChooser extends Heuristic {

	Heuristic heuristic;
	
	public HeuristicChooser(Board board) {
		super(board);
		this.heuristic = chooseHeuristic(board);
	}

	protected Heuristic chooseHeuristic(Board b){
		
		
		return null;
	}
	
	@Override
	public int estimate(Board b) throws DeadLockException{
		return heuristic.estimate(b);
	}

	@Override
	public String toString() {
		return "" + heuristic;
	}

}
