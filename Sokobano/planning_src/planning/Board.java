package planning;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Board {
	ArrayList<ArrayList<List<Thing>>> board;
	
	public Board(){
		
	}
	
	public List<Thing> get(int x, int y) throws IndexOutOfBoundsException{
		return board.get(y).get(x);
	}
	
	public void add(List<Thing> things, int x, int y){
		for(int i = y-board.size()+1; i > 0; i--){
			board.add(new ArrayList<List<Thing>>());
		}
		for(int i = x-board.get(y).size()+1; i > 0; i--){
			board.get(y).add(new LinkedList<Thing>());
		}
		
		board.get(y).get(x).addAll(things);
	}
}
