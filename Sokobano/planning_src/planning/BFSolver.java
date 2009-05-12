package planning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BFSolver extends Solver {

	public BFSolver(Level level) {
		super(level);
	}

	public boolean solve(){
		HashMap<Board, Queue<Action>> stateActionsNotExplored = new HashMap<Board, Queue<Action>>();	

		Player player = level.getPlayer();
		Board currentState = level.getBoard();
		Queue<Action> actions = getPossibleActions(currentState, player);



		while()

			return false;
	}

	private Queue<Action> getPossibleActions(Board state, Player player) {
		int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

		Queue<Action> actions = new LinkedList<Action>();
		
		for (int i = 0; i < directions.length; i++) {
			try {
				Move m = new Move(state, player.getX(), player.getY(),
						directions[i][0], directions[i][1], player);
				actions.add(m);

			} catch (IllegalActionException e) {

			}
			try {
				Push p = new Push(state, player.getX(), player.getY(),
						directions[i][0], directions[i][1], player);
				actions.add(p);
			} catch (IllegalActionException e) {

			}
			try {
				PushToTarget ptt = new PushToTarget(state, player.getX(),
						player.getY(), directions[i][0], directions[i][1],
						player);
				actions.add(ptt);
			} catch (IllegalActionException e) {

			}

		}

		return null;
	}

}
