package gdi1sokoban.planning.heuristics;

import gdi1sokoban.planning.Board;


public class HeuristicsAdder extends HeuristicsCombiner {

	public HeuristicsAdder(Board board){
		super(board);
	}
	
	@Override
	public int estimate(Board b) {
		int sum = 0;
		for(Heuristic h: heuristics){
			if(sum < Integer.MAX_VALUE/2){
				sum += h.estimate(b);
			
			} else {
				return sum;
			}
		}
		return sum;
	}

	@Override
	public String toString() {	
		return "HeuristicsAdder{\n"+super.toString()+"\n}";
	}
}
