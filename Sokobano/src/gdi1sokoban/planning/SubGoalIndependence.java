package gdi1sokoban.planning;

import java.util.List;

public class SubGoalIndependence extends Heuristic {

	public SubGoalIndependence(Board board) {
		super(board);
	}
	
	@Override
	public int estimate(Board b) {
		List<Box> boxes = b.getBoxes();
		List<Goal> goals = b.getGoals();
		
		Player player = b.getPlayer();
		
		int sum = 0;
		for(Box box: boxes){
			sum += estimateSingleBox(player, box, goals);
		}
		
		// TODO Auto-generated method stub
		return sum + distanceToNearestBox(player, boxes);
	}
	
	protected int estimateSingleBox(Player player, Box box, List<Goal> goals){
		int shortest = Integer.MAX_VALUE;
		for(Goal goal: goals){
			int dist = distance(box, goal);
			if(dist < shortest){
				shortest = dist;
			}
		}
		return shortest;// + distance(player, box);
	}
	
	protected int distance(Thing t1, Thing t2){
		return Math.abs(t1.getX()-t2.getX()) +
				Math.abs(t1.getY() - t2.getY());
	}
	
	protected int distanceToNearestBox(Player p, List<Box> b){
		int dist = Integer.MAX_VALUE;
		for(Box box : b){
			int newDist = distance(p, box);
			if(newDist < dist){
				dist = newDist;
			}
		}
		return dist;
	}

	@Override
	public String toString() {
		return "SubGoalIndependence";
	}
}
