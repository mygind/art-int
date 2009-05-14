package gdi1sokoban.planning;

import java.util.LinkedList;
import java.util.List;

public class HeuristicsAdder extends Heuristic {

	private List<Heuristic> heuristics;
	
	public HeuristicsAdder(Board board){
		super(board);
		
		this.heuristics = new LinkedList<Heuristic>();
	}
	
	public void add(Heuristic heuristic){
		heuristics.add(heuristic);
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

}
