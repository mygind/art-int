package gdi1sokoban.planning;

import java.util.List;

public class SubGoalIndependence extends Heuristic {

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
		return sum;
	}
	
	private int estimateSingleBox(Player player, Box box, List<Goal> goals){
		int shortest = Integer.MAX_VALUE;
		for(Goal goal: goals){
			int dist = distance(box, goal);
			if(dist < shortest){
				shortest = dist;
			}
		}
		return shortest + distance(player, box);
	}
	
	private int distance(Thing t1, Thing t2){
		return Math.abs(t1.getX()-t2.getX()) +
				Math.abs(t1.getY() - t2.getY());
	}

}
