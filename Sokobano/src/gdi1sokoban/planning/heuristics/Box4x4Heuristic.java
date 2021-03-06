package gdi1sokoban.planning.heuristics;

import gdi1sokoban.planning.Board;
import gdi1sokoban.planning.Box;

import java.util.List;

public class Box4x4Heuristic extends Heuristic {

	public Box4x4Heuristic(Board board) {
		super(board);
	}
	
	@Override
	public int estimate(Board b) throws DeadLockException {
		List<Box> boxes = b.getBoxes();
		
		for(Box box: boxes){
			if(b.get(box.getX(), box.getY()) == '$'){
				if(isStuck(box.getX(), box.getY(), 1, 1, b) ||
				   isStuck(box.getX(), box.getY(), -1, 1, b) ||
				   isStuck(box.getX(), box.getY(), -1, -1, b) ||
				   isStuck(box.getX(), box.getY(), 1, -1, b)){
					throw new DeadLockException("" + b);
				}
			}
		}
		return 0;
	}
	
	private boolean isStuck(int x, int y, int dx, int dy, Board b){
		try{
		return 
		       isObstructing(b.get(x+dx, y)) &&
		       isObstructing(b.get(x+dx, y+dy)) &&
		       isObstructing(b.get(x, y+dy));
		} catch (IndexOutOfBoundsException e){
			//System.err.println("pos: ("+x+","+y+") dir: ("+(x+dx)+","+(y+dy)+")\n" + b);
			return false;
		}
	}
	
	private boolean isObstructing(char c){
		return c == '#' || c == '$' || c == '*';
	}

	@Override
	public String toString() {
		return "4";
	}
}
