package gdi1sokoban.planning;

import java.util.ArrayList;

public class CornerBoard extends Board {

	public CornerBoard(ArrayList<String> layout) {
		super(layout);
	}

	public CornerBoard(Board oldBoard){
		this(oldBoard.getLandscape() );
	}
	
	public void set(int x, int y, char c) {
		setCharAt(x, y, c);
	}
}
