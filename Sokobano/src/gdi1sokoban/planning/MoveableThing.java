package gdi1sokoban.planning;

public abstract class MoveableThing extends ObstructingThing {

	public MoveableThing(int x, int y){
		super(x, y);
	}
	
	/**
	 * Move x in x-axis and y in y-axis. Must be exactly one step so that abs(x+y) = 1 and 0 <= x <= 1 and 0 <= y <= 1
	 * @param x move object x on the x-axis. x = {-1, 0, -1}
	 * @param y move object y on the y-axis. y = {-1, 0, -1}
	 */
//	public void move(int x, int y){
//		if(((x + y == 1) || (x + y) == -1) &&
//		   (x == -1 || x == 0 || x == 1) &&
//		   (y == -1 || y == 0 || y == 1)){ // Maybe not nescessary to check for
//			
//			this.x += x;
//			this.y += y;
//		}
//	}
}
