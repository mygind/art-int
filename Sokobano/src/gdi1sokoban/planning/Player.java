package gdi1sokoban.planning;

public class Player extends MoveableThing {

	private int x, y; 
	
	public Player(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
