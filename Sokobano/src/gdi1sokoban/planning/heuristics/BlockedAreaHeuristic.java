package gdi1sokoban.planning.heuristics;

import gdi1sokoban.planning.Board;
import gdi1sokoban.planning.Box;
import gdi1sokoban.planning.CornerBoard;

import java.util.HashSet;
import java.util.PriorityQueue;

import com.sun.media.sound.HsbParser;

public class BlockedAreaHeuristic extends Heuristic {
	
	CornerBoard cornerBoard;
	
	public BlockedAreaHeuristic(Board board) {
		super(board);
		CornerHeuristic ch = new CornerHeuristic(board);
		this.cornerBoard = ch.getCornerBoard();
	}
	@Override
	public int estimate(Board b) throws DeadLockException {
		int x = b.getPlayer().getX();
		int y = b.getPlayer().getY();
		int sx = 0;
		int sy = 0;
		for(Box box: b.getBoxes()){
			int sidesReached = 0;
			int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
			for(int[] d: dirs){
				int destX = box.getX()+d[0];
				int destY = box.getY()+d[1];
				if(canFindPath(x, y, destX, destY, b)){
					sidesReached++;
					sx = destX;
					sy = destY;
				}
			}
			if(sidesReached == 0){
				//throw new DeadLockException("Deadlock reached:\n"+b);
			} else if(sidesReached == 1){
				// Potentially stuck...
			} else {
				return 0;
			}
		}
		return 0;
	}
	
	private boolean canFindPath(int x, int y, int destX, int destY, Board b){
		PriorityQueue<Pos> q = new PriorityQueue<Pos>();
		HashSet<Pos> visited = new HashSet<Pos>();
		q.add(new Pos(x, y, destX, destY));
		
		while(!q.isEmpty()){
			Pos p = q.poll();
			visited.add(p);
			
			int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
			for(int[] d: dirs){
				Pos newPos = new Pos(p.x+d[0], p.y+d[1], destX, destY);
				if(newPos.x == destX && newPos.y == destY){
					return true;
				}
				if(visited.contains(newPos)){
					continue;
				} else {
					q.add(newPos);
				}
			}
		}
		
		return false;
	}
	

	@Override
	public String toString() {
		return "BlockedAreaHeuristic";
	}

	private class Pos{
		public int x,y,val;
		public Pos(int x, int y, int val){
			this.x = x;
			this.y = y;
			this.val = val;
		}
		
		public Pos(int x, int y, int destX, int destY){
			this.x = x;
			this.y = y;
			this.val = Math.abs(x-destX)+Math.abs(y-destY);
		}
		
		public void setVal(int val){
			this.val = val;
		}
		
		public int dist(Pos o){
			return Math.abs(x-o.x)+Math.abs(y-o.y);
		}
	}
}
