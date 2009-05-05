package planning;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Board {
    Things[][] board;
	
    public Board(int x, int y){
	board = new Things[x][y];
    }
	
    public Board(Things[][] board){
	this.board = board;
    };
	
    public List<Thing> get(int x, int y) throws IndexOutOfBoundsException{
	return board[x][y].getThings();
    }
	
    public void add(LinkedList<Thing> things, int x, int y){
		
	board[x][y] = new Things(things);
    }
	
    public boolean isFree(int x, int y){
	for(Thing t: board[x][y].getThings()){
	    if(t instanceof ObstructingThing) return false;
	}
	return true;
    }
	
    public Board clone(){
	return new Board(board.clone());
    }
    
    public String toString(){
	StringBuilder s = new StringBuilder();
	for(int x = 0; x < board.length; x++){
	    for ( int y = 0; y < board[x].length; y++){
		s.append(board[x][y].toString()); 
	    }
	    s.append("\n");
	}
	return s.toString();
    }
}
