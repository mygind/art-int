package gdi1sokoban.planning;

import java.util.List;

public class Box4x4Heuristic extends Heuristic {

	public Box4x4Heuristic(Board board) {
		super(board);
	}
	
	@Override
	public int estimate(Board b) {
		List<Box> boxes = b.getBoxes();
		
		for(Box box: boxes){
			if(b.get(box.getX(), box.getY()) == '$'){
				if(isStuck(box.getX(), box.getY(), 1, 1, b) ||
				   isStuck(box.getX(), box.getY(), -1, 1, b) ||
				   isStuck(box.getX(), box.getY(), -1, -1, b) ||
				   isStuck(box.getX(), box.getY(), 1, -1, b)){
					return Integer.MAX_VALUE/2;
				}
			}
		}
		return 0;
	}
	
	private boolean isStuck(int x, int y, int dx, int dy, Board b){
		return 
		       isObstructing(b.get(x+dx, y)) &&
		       isObstructing(b.get(x+dx, y+dy)) &&
		       isObstructing(b.get(x, y+dy));
	}
	
	private boolean isObstructing(char c){
		return c == '#' || c == '$' || c == '*';
	}

	@Override
	public String toString() {
		return "Box4x4Heuristic";
	}
}
