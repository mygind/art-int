package gdi1sokoban.planning;

public class BoxOnGoalHeuristic extends Heuristic {

	public BoxOnGoalHeuristic(Board board) {
		super(board);
	}
	@Override
	public int estimate(Board b) {
		int boxesNotGoaled = b.getBoxes().size();
		for(Box box: b.getBoxes()){
			if(b.get(box.getX(), box.getY()) == '*' ||
			   b.get(box.getX(), box.getY()) == '+' ||
			   b.get(box.getX(), box.getY()) == '.'){
				
				boxesNotGoaled--;
			}
		}
		
		return boxesNotGoaled;
	}

	@Override
	public String toString() {
		return "BoxOnGoalHeuristic";
	}

}
