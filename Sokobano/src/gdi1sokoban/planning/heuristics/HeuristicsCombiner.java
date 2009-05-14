package gdi1sokoban.planning.heuristics;

import gdi1sokoban.planning.Board;

import java.util.LinkedList;
import java.util.List;

public abstract class HeuristicsCombiner extends Heuristic {
	protected List<Heuristic> heuristics;
	
	public HeuristicsCombiner(Board board){
		super(board);
		
		this.heuristics = new LinkedList<Heuristic>();
	}
	
	public void add(Heuristic heuristic){
		heuristics.add(heuristic);
	}
	
	@Override
	public abstract int estimate(Board b);

	@Override
	public String toString() {
		String str = "";
		for(Heuristic h: heuristics){
			str += h + "\n";
		}
		return str;
	}
}
