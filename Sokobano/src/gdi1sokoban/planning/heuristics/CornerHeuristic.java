package gdi1sokoban.planning.heuristics;

import gdi1sokoban.planning.Board;
import gdi1sokoban.planning.Box;
import gdi1sokoban.planning.CornerBoard;
import gdi1sokoban.planning.Goal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CornerHeuristic extends Heuristic {

	HashSet<Integer> illegalBoxPositions;
	List<Corner> corners;
	
	public CornerHeuristic(Board board) {
		super(board);
		
		CornerBoard b = new CornerBoard(board);
		
		illegalBoxPositions = new HashSet<Integer>();
		corners = new ArrayList<Corner>();
		
		// Initialize the goal, box and player lists
		b.getBoxes();
		
		ArrayList<String> landscape = board.getLandscape();

		//System.out.println(b);
		
		// Finding corners:
		// o = outer corner
		// i = inner corner
		// TODO d = dead end - not yet implemented... 
		for(int x = 0; x < landscape.size(); x++){
			for(int y = 0; y < landscape.get(x).length(); y++){
				char c = b.get(x, y);
				
				markCorner(x, y, 1, 1, b);
				markCorner(x, y, -1, 1, b);
				markCorner(x, y, -1, -1, b);
				markCorner(x, y, 1, -1, b);
			}
		}
		//System.out.println(b);
		
		// Marking illegal areas between corners
		for(Corner c: corners){
			if(c.c == 'i'){
				int x = c.x;
				int y = c.y;
				markIllegal(x, y);
				
				int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
				for(int[] d: dirs){
					try{
						if(b.get(x+d[0], y+d[1]) != '#'){
						// If not a wall search for a corner
							int dist = 1;
							// Increase distance until you find a corner
							while((b.get(x+dist*d[0],y+dist*d[1]) != 'i') &&
							      (b.get(x+dist*d[0],y+dist*d[1]) != 'o')){
								dist++;
							}
							if(b.get(x+dist*d[0],y+dist*d[1]) == 'i'){
								for(;dist > 0; dist--){
									markIllegal(x+d[0]*dist, y+d[1]*dist);
								}
							}
							
						}
					} catch (IndexOutOfBoundsException e){
						// Ignore as this means we are out of the level (i think)
						// We still have to check the other directions!
					}
				}
			}
		}
		
		// Remove marking of squares on line with goal square
		for(Goal g : b.getGoals()){
			int x = g.getX();
			int y = g.getY();
			if(unMarkIllegal(x, y)){
				int[][] dirs = {{0,1},{1,0}};
				for(int[] d: dirs){
					try{
						// If a wall is found in one direction, clean in other direction
						if(b.get(x+d[0], y+d[1])=='#' ||
						   b.get(x-d[0], y-d[1])=='#'){
							
							int dist = 1;
							while(b.get(x+d[1]*dist, y+d[0]*dist) != '#'){
								unMarkIllegal(x+d[1]*(dist-1), y+d[0]*(dist-1));
								dist++;
							}
							dist = -1;
							while(b.get(x+d[1]*dist, y+d[0]*dist) != '#'){
								unMarkIllegal(x+d[1]*(dist+1), y+d[0]*(dist+1));
								dist--;
							}
						}
					
						
					} catch (IndexOutOfBoundsException e){
						// hmmm
					}
				}
			}
		}
		
		drawIllegalArrays(b);
		
		//System.out.println(b);
		
	}
	
	private void markCorner(int x, int y, int dx, int dy, CornerBoard b){
		try{
			if(b.get(x, y) == '#'){
				if(b.get(x+dx, y) == '#' && b.get(x, y+dy) == '#'){
					if(b.get(x+dx, y+dy) != '#'){
						mark(x+dx, y+dy, 'i', b);
					}
				} else if(b.get(x+dx, y) != '#' && b.get(x, y+dy) != '#'){
					if(b.get(x+dx, y+dy) != '#'){
						mark(x+dx, y+dy, 'o', b);
					}
				}
			}
		} catch (IndexOutOfBoundsException e){
			// Ignore this
		}
	}
	
	public HashSet<Integer> getIllegalBoxPositions() {
		return illegalBoxPositions;
	}
	
	private void mark(int x, int y, char c, CornerBoard b){
		corners.add(new Corner(x, y, c));
		b.set(x, y, c);
		
	}
	
	private void markIllegal(int x, int y){
		illegalBoxPositions.add(new Integer(x<<16 | y));
		// y = p&0xff;
		// x = p >>> 16;
	}
	
	private boolean unMarkIllegal(int x, int y){
		return illegalBoxPositions.remove(new Integer(x<<16 | y));
	}
	
	private void drawIllegalArrays(CornerBoard b){
		for(Integer i : illegalBoxPositions){
			int x = i.intValue() >>> 16;
			int y = i.intValue() & 0xff;
			b.set(x, y, '?');
		}
	}
	
	@Override
	public int estimate(Board b) {
		List<Box> boxes = b.getBoxes();
		
		
		for(Box box: boxes){
			Integer pos = new Integer(box.getX()<<16 | box.getY());
			if(illegalBoxPositions.contains(pos)){
				return Integer.MAX_VALUE/2;
			}
		}
		return 0;
	}
	
	private class Corner{
		public int x, y;
		public char c;
		
		public Corner(int x, int y, char c){
			this.x = x;
			this.y = y;
			this.c = c;
		}
	}

	@Override
	public String toString() {
		return "CornerHeuristic";
	}
}
