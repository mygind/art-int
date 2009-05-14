package gdi1sokoban.planning;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ShortestPathHeuristic extends SubGoalIndependence{

	List<GoalPath> goalPaths;
	HashSet<Integer> illegalPositions;
	HashSet<Goal> usedGoals;
	
	public ShortestPathHeuristic(Board board) {
		super(board);
		
		usedGoals = new HashSet<Goal>();
		
		intializePathBoards(board);
	}
	
	private void intializePathBoards(Board b){
		CornerHeuristic ch = new CornerHeuristic(b);
		illegalPositions = ch.getIllegalBoxPositions();
		
		
		goalPaths = new LinkedList<GoalPath>();
		
		List<Goal> goals = b.getGoals();
		for(Goal g : goals){
			goalPaths.add(new GoalPath(createPaths(b, g), g));
		}
		//printGoalPaths();
	}
	
	private void printGoalPaths(){

		for(ArrayList<Integer> a :goalPaths.get(0).path){
			for(Integer i : a){
				if(i.intValue() > 99){
					System.out.format("### ");
				} else {
					System.out.format("% 3d ", i.intValue());
				}
			}
			System.out.println("");
			
		}
	}
	private ArrayList<ArrayList<Integer>> createPaths(Board b, Goal g){
		
		// Initialiser paths med maxValue i alle værdier
		ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();
		for(String str : b.getLandscape()){
			ArrayList<Integer> toAdd = new ArrayList<Integer>();
			for(char c : str.toCharArray()){
				toAdd.add(new Integer(Integer.MAX_VALUE));
			}
			paths.add(toAdd);
		}
		
		Queue<Pos> q = new LinkedList<Pos>();
		q.add(new Pos(g.getX(), g.getY()));
		paths.get(g.getX()).set(g.getY(), new Integer(0));
		
		while(!q.isEmpty()){
			Pos p = q.poll();
			int[][] dirs = {{-1,0},{1,0},{0,-1},{0,1}};
			for(int[] d: dirs){
				
				// if the new position is not a wall and is a legalBoxPosition do...
				if(b.get(p.x+d[0],p.y+d[1]) != '#' &&
				   !illegalPositions.contains( new Integer( (p.x+d[0]) << 16 | (p.y+d[1]) ) ) ){
					// New value = formerValue + 1
					int newVal = paths.get(p.x).get(p.y).intValue()+1;
					
					// If newValue < formerValue => replace it
					if(paths.get(p.x+d[0]).get(p.y+d[1]).intValue() > newVal){
						paths.get(p.x+d[0]).set(p.y+d[1], new Integer(newVal));
						q.add(new Pos(p.x+d[0], p.y+d[1]));
					}
				}
				
			}
		}
		
		return paths;
	}
	/*@Override
	public int estimate(Board b) {
		usedGoals = new HashSet<Goal>();
		for(Goal g : b.getGoals()){
			if(b.get(g.getX(), g.getY()) == '*'){
				usedGoals.add(g);
			}
		}
		
		return super.estimate(b);
	}*/
	
	@Override
	protected  int estimateSingleBox(Player player, Box box, List<Goal> goals){
		int nearestGoal = Integer.MAX_VALUE/2;
		for(GoalPath gp : goalPaths){
			if(!usedGoals.contains(gp.g)){
				int dist = gp.path.get(box.getX()).get(box.getY()).intValue();
				if(dist < nearestGoal){
					nearestGoal = dist;
					//System.out.println(":");
				}
			}
		}
		
		return nearestGoal;//+distance(player, box);
	}

	private class GoalPath{
		public ArrayList<ArrayList<Integer>> path;
		public Goal g;
		
		public GoalPath(ArrayList<ArrayList<Integer>> path, Goal g){
			this.path = path;
			this.g = g;
		}
		
	}
	
	private class Pos{
		public int x,y;
		public Pos(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	@Override
	public String toString() {
		return "ShortestPathHeuristic";
	}
}
