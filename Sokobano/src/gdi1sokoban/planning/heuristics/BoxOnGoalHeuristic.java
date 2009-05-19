package gdi1sokoban.planning.heuristics;

import gdi1sokoban.planning.Board;
import gdi1sokoban.planning.Box;

public class BoxOnGoalHeuristic extends Heuristic {

	private int lowest;
	public BoxOnGoalHeuristic(Board board) {
		super(board);
		this.lowest = Integer.MAX_VALUE;
	}
	@Override
	public int estimate(Board b) throws DeadLockException {
		int boxesNotGoaled = b.getBoxes().size();
		for(Box box: b.getBoxes()){
			if(b.get(box.getX(), box.getY()) == '*'){
				boxesNotGoaled--;
				if(boxesNotGoaled < lowest){
					lowest = boxesNotGoaled;
					//System.out.println(lowest);
					//System.out.println(b);
				}
			}
		}
		
		return boxesNotGoaled;
	}

	@Override
	public String toString() {
		return "B";
	}

}
