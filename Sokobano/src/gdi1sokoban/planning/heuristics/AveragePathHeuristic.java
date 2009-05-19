package gdi1sokoban.planning.heuristics;

import gdi1sokoban.planning.Board;
import gdi1sokoban.planning.Box;
import gdi1sokoban.planning.Goal;
import gdi1sokoban.planning.Player;

import java.util.List;

public class AveragePathHeuristic extends ShortestPathHeuristic {

	public AveragePathHeuristic(Board board) {
		super(board);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int estimateSingleBox(Player player, Box box, List<Goal> goals){
		int distSum = 0;
		int noFreeGoals = 0;
		
		for(GoalPath gp : goalPaths){
			if(!usedGoals.contains(gp.g)){
				int dist = gp.path.get(box.getX()).get(box.getY()).intValue();
				
				distSum += dist;
				noFreeGoals++;
			}
		}
		
		if(noFreeGoals == 0){
			return 0;
		} else {
			return distSum/noFreeGoals;
		}
	}
    public String toString(){
	return "a";
    }
}
