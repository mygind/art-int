package gdi1sokoban.planning.heuristics;

import gdi1sokoban.planning.Board;

import java.util.Random;

public class RandomHeuristic extends Heuristic {

    private static final int variance = 10000;

    private Random r;
	public RandomHeuristic(Board board) {
		super(board);
		r = new Random();
	}
	
	@Override
	public int estimate(Board b) {
	    return r.nextInt(10000);
	}
	
	@Override
	public String toString() {
	    return "rand";
	}
}
