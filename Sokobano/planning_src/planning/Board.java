package planning;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Board {
	Things[][] board;
	
	public Board(int x, int y){
		board = new Things[x][y];
	}
	
	public List<Thing> get(int x, int y) throws IndexOutOfBoundsException{
		return board[x][y].getThings();
	}
	
	public void add(LinkedList<Thing> things, int x, int y){
		
		board[x][y] = new Things(things);
	}
}
