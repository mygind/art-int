package gdi1sokoban.planning;

public abstract class Action {
	protected int _dx, _dy, _x, _y;
	protected Player _p;
	protected Board _b;
	
	public Action (Board b, int x, int y, int dx, int dy, Player p) throws IllegalActionException{
		if(legalDir(dx, dy)){
			if(b.playerAt(x, y)){
			
				_dx = dx;
				_dy = dy;
				
				_x = x;
				_y = y;
				
				_b = b;
				_p = p;
			} else {
				throw new IllegalActionException("Player is not located at this position (" + x + "," + y + ").");
			}
		} else {
			throw new IllegalActionException("The direction is not legal.");
		}
	}
	
	public boolean legalDir(int x, int y){
		return ((x + y == 1 || x + y == -1) &&
		        (x == -1 || x == 0 || x == 1) &&
		        (y == -1 || y == 0 || y == 1)); // Maybe not nescessary to check for
	}
	
	public abstract Board perform() throws IllegalActionException;
	
	@Override
	public abstract String toString();
}
