package gdi1sokoban.planning.heuristics;

import gdi1sokoban.planning.Board;

public class HeuristicsMultiplier extends HeuristicsCombiner {

	 public HeuristicsMultiplier(Board board){
		super(board);
	}
		
	@Override
	public int estimate(Board b) throws DeadLockException {
		int product = 1;
		for(Heuristic h: heuristics){
			if(product < Integer.MAX_VALUE/2){
				product *= h.estimate(b);
			
			} else {
				return product;
			}
		}
		return product;
	}

	@Override
	public String toString() {	
		return "*{"+super.toString()+"}";
	}

}