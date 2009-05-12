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
	
    public Things get(int x, int y) throws IndexOutOfBoundsException{
	return board[x][y];
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
    	Board nb = new Board(board.length, board[0].length);
    	for(int x = 0; x < board.length; x++){
    		for(int y = 0; y < board[x].length; y++){
    			LinkedList<Thing> newThings = new LinkedList<Thing>();
    			for(Thing thing: board[x][y].getThings()){
    				newThings.add(thing);
    			}
    			nb.add(newThings, x, y);
    		}
    	}
    	
    	return nb;
    }
    
    public String toString(){
	StringBuilder s = new StringBuilder();
	if(board.length > 0){
		for(int y = 0; y < board[0].length; y++){
			for(int x = 0; x < board.length; x++){
				s.append(board[x][y]);
			}
			s.append("\n");
		}
		
	}
	return s.toString();
    }
    
    public boolean equals(Object o){
    	if(o == null || !(o instanceof Board)){
    		return false;
    	}
    	Board b = (Board)o;
    	for(int x = 0; x < board.length; x++){
    		for(int y = 0; y < board[x].length; y++){
    			try{
    				if(!board[x][y].equals(b.get(x, y))){
    					return false;
    				}
    			} catch (ArrayIndexOutOfBoundsException e){
    				return false;
    			}
    		}
    	}
    	
    	
    	return true;
    }
}
