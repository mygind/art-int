package gdi1sokoban.planning;

public abstract class HeuristicChooser extends Heuristic {

	Heuristic heuristic;
	
	public HeuristicChooser(Board board) {
		super(board);
		this.heuristic = chooseHeuristic(board);
	}

	protected abstract Heuristic chooseHeuristic(Board b);
	
	@Override
	public int estimate(Board b) {
		return heuristic.estimate(b);
	}

	@Override
	public String toString() {
		return "" + heuristic;
	}

}
