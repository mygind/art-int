package planning;

import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.List;

public class Board {
    //Things[][] board;

    ArrayList<String> landscape;
    //ArrayList<Goal> goals; 
    //ArrayList<Box> boxes; // s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
    //ArrayList<Player> players; // s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]


    private int hash;    
    
    public Board(Board oldBoard){
	this(oldBoard.getLandscape() );
    }

    public Board(ArrayList<String> layout){
	landscape = new ArrayList<String>();
	for ( String line : layout ){
	    landscape.add(new String(line));
	}
    }

    // :::::: NEW INTERFACE ::::::

    private void setCharAt(int x, int y, char res) throws IndexOutOfBoundsException {
    	StringBuffer s = new StringBuffer(landscape.get(x));
	s.setCharAt(y, res);
	ĺandscape.set(x, s.toString());
    }

    private void addPlayer(int x, int y) throws IndexOutOfBoundsException, IllegalMoveException {
	char to = this.get(x,y);
	
	if ( ! this.isFree(x,y) ){
	    throw new IllegalMoveExceptio("Destination ("+x+","+y+") is not free. Occupied by ("+to+")");
	}
	
	char res = '@';
	if ( to == '.' ){
	    res = '+';
	}
	setCharAt(x,y,res);
    }

    private void removePlayer( int x, int y) throws IndexOutOfBoundsException, IllegalMoveException {
	char from = this.get(x,y);
	if ( ! boxAt(x,y) ) {
	    throw new IllegalMoveException("No box at ("+x+","+y+"). Slot contained: ["+from+"]"); 
	}
	char res = ' ';
	//if moving from goal square we still have a goal square there
	if ( from == '+' ){
	    res = '.';
	}
	
	setCharAt(x,y,charAt);
	

    }


    public void movePlayer ( int x, int y, int dx, int dy) throws IllegalMoveException {
	try {
	    
	    removePlayer(x,y);
	    addPlayer(x+dx,y+dy);
	    
	} catch (IndexOutOfBoundsException i) {
	    throw new IllegalMoveException("You moved out of the board");
	}
    }

    private void addBox(int x, int y) throws IndexOutOfBoundsException, IllegalMoveException {
	char to = this.get(x,y);
	
	if ( ! this.isFree(x,y) ){
	    throw new IllegalMoveExceptio("Destination ("+x+","+y+") is not free. Occupied by ("+to+")");
	}
	
	char res = '$';
	if ( to == '.' ){
	    res = '*';
	}
	setCharAt(x,y,res);
    }

    private void removeBox( int x, int y) throws IndexOutOfBoundsException, IllegalMoveException {
	char from = this.get(x,y);
	if ( ! boxAt(x,y) ) {
	    throw new IllegalMoveException("No box at ("+x+","+y+"). Slot contained: ["+from+"]"); 
	}
	char res = ' ';
	//if moving from goal square we still have a goal square there
	if ( from == '*' ){
	    res = '.';
	}
	
	setCharAt(x,y,charAt);
	

    }
    
    public void  moveBox (int x, int y, int dx, int dy) throws IllegalMoveException {
	try {
	    
	    removeBox(x,y);
	    addBox(x+dx,y+dy);

	} catch (IndexOutOfBoundsException i) {
	    throw new IllegalMoveException("You moved out of the board");
	}
    }
    
    
    public boolean playerAt(int x, int y) throws IndexOutOfBoundsException {
	return this.checkCharAt(x,y,new char[] {'@','+'});
    }
    
    public boolean boxAt ( int x, int y) throws IndexOutOfBoundsException {
	return this.checkCharAt(x,y,new char[] {'$','*'});
    }
    
    public boolean goalAt ( int x, int y)  throws IndexOutOfBoundsException {
	return this.checkCharAt(x,y,new char[] {'.','*','+'});
    }
    
    
    public boolean  isFree ( int x, int y) throws IndexOutOfBoundsException {
	return this.checkCharAt(x,y, new char[] {' ','_','.'});
    }
    
    private boolean checkCharAt(int x, int y, char[] chars) throws IndexOutOfBoundsException {
	for ( char c : chars ){
	    if ( this.get(x,y ) == c ){
		return true;
	    }
	}
	return false;
    }

    // private Board(){
    // 	goals = new ArrayList<Goal>();
    // 	hash = 0;
    // }
    
    // public Board(int x, int y){
    // 	this();
    // 	board = new Things[x][y];
    // }
	
    // public Board(Things[][] board){
    // 	this();
    // 	this.board = board;
	
    // }
	
    // public ArrayList<String> getLandscape(){
    // 	return landscape;
    // }
    
    // public char get(int x, int y) throws IndexOutOfBoundsException{
    // 	return landscape.get(x).charAt(y);
    // }

	
    // public void add(LinkedList<Thing> things, int x, int y){
		
    // 	board[x][y] = new Things(things);
    // }

    // public boolean isCompleted(){
    // 	return false;
    // }
	
    // /*
    //   public boolean isFree(int x, int y){
    // 	for(Thing t: board[x][y].getThings()){
    // 	    if(t instanceof ObstructingThing) return false;
    // 	}
    // 	return true;
    // }
    // */
	
    // public Board clone(){
    // 	Board nb = new Board(board.length, board[0].length);
    // 	for(int x = 0; x < board.length; x++){
    // 		for(int y = 0; y < board[x].length; y++){
    // 			LinkedList<Thing> newThings = new LinkedList<Thing>();
    // 			for(Thing thing: board[x][y].getThings()){
    // 				newThings.add(thing);
    // 			}
    // 			nb.add(newThings, x, y);
    // 		}
    // 	}
    	
    // 	return nb;
    // }
    
    // public String toString(){
    // 	StringBuilder s = new StringBuilder();
    // 	if(board.length > 0){
    // 		for(int y = 0; y < board[0].length; y++){
    // 			for(int x = 0; x < board.length; x++){
    // 				s.append(board[x][y]);
    // 			}
    // 			s.append("\n");
    // 		}
		
    // 	}
    // 	return s.toString();
    // }

    // public int getHash(){
    // 	if ( hash == 0 ){
	    
	    
    // 	}
    // 	return 0;
    // }
    
    // public boolean equals(Object o){
    // 	if(o == null || !(o instanceof Board)){
    // 		return false;
    // 	}
    // 	Board b = (Board)o;
    // 	for(int x = 0; x < board.length; x++){
    // 		for(int y = 0; y < board[x].length; y++){
    // 			try{
    // 				if(!board[x][y].equals(b.get(x, y))){
    // 					return false;
    // 				}
    // 			} catch (ArrayIndexOutOfBoundsException e){
    // 				return false;
    // 			}
    // 		}
    // 	}
    	
    	
    // 	return true;
    // }


}
