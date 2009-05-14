package gdi1sokoban.planning;

import gdi1sokoban.planning.heuristics.Box4x4Heuristic;
import gdi1sokoban.planning.heuristics.BoxOnGoalHeuristic;
import gdi1sokoban.planning.heuristics.CornerHeuristic;
import gdi1sokoban.planning.heuristics.HeuristicsAdder;
import gdi1sokoban.planning.heuristics.HeuristicsMultiplier;
import gdi1sokoban.planning.heuristics.ShortestPathHeuristic;
import gdi1sokoban.planning.heuristics.SubGoalIndependence;

import java.io.FileWriter;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


public class GoSolveYourself {

    public static void main(String args[]){

    	
	LevelParser lp = new LevelParser();
	try{		
		Level l = lp.parse(args[0]);
		Board b = l.getBoard();

		HeuristicsAdder h4 = new HeuristicsAdder(b);
		h4.add(new CornerHeuristic(b));
		h4.add(new SubGoalIndependence(b));
		
		HeuristicsAdder h5 = new HeuristicsAdder(b);
		h5.add(new CornerHeuristic(b));
		h5.add(new SubGoalIndependence(b));
		h5.add(new Box4x4Heuristic(b));
		
		HeuristicsAdder h6 = new HeuristicsAdder(b);
		HeuristicsMultiplier h6a = new HeuristicsMultiplier(b);
		h6a.add(new SubGoalIndependence(b));
		h6a.add(new SubGoalIndependence(b));
		h6.add(h6a);
		h6.add(new CornerHeuristic(b));
		h6.add(new Box4x4Heuristic(b));
		
		HeuristicsAdder h7 = new HeuristicsAdder(b);
		HeuristicsMultiplier h7a = new HeuristicsMultiplier(b);
		h7a.add(new ShortestPathHeuristic(b));
		h7a.add(new ShortestPathHeuristic(b));
		h7.add(h7a);
		h7.add(new CornerHeuristic(b));
		h7.add(new Box4x4Heuristic(b));
		
		HeuristicsAdder h8 = new HeuristicsAdder(b);
		HeuristicsMultiplier h8a = new HeuristicsMultiplier(b);
		h8a.add(new BoxOnGoalHeuristic(b));
		h8a.add(new BoxOnGoalHeuristic(b));
		//h8a.add(new ShortestPathHeuristic(b));
		h8a.add(new ShortestPathHeuristic(b));
		h8.add(h8a);
		h8.add(new CornerHeuristic(b));
		h8.add(new Box4x4Heuristic(b));
		
		boolean[] run = {false, false, false, false, false, false, true, true};
		Solver[] solvers = {new BFSolver(new Board(l.getBoard())),
		                    new AstarSolver(new Board(l.getBoard()), new SubGoalIndependence(b)),
		                    new AstarSolver(new Board(l.getBoard()), new CornerHeuristic(b)),
		                    new AstarSolver(new Board(l.getBoard()), h4),
		                    new AstarSolver(new Board(l.getBoard()), h5),
		                    new AstarSolver(new Board(l.getBoard()), h6),
		                    new AstarSolver(new Board(l.getBoard()), h7),
		                    new AstarSolver(new Board(l.getBoard()), h8)
		};

		if(solvers.length != run.length){
			throw new Exception("run != solvers");
		}
		
		//System.out.println("StartState:");
		//System.out.println(b);
		
		boolean doStats = (args.length > 1);
		
		Stack<SolutionPart> solution;
		long before, after;
		
		System.out.println("Times:");
		for(int i = 0; i < solvers.length; i++){
			if(run[i]){
				before = System.currentTimeMillis();
				solution = solvers[i].solve(doStats);
				after = System.currentTimeMillis();
				//System.out.println(solvers[i] + ": " + (after-before) + "ms");
				System.out.println((after-before) + "ms");
				//printSolution(solution);
			}
		}
		
		if(doStats){
			for(int i = 0; i < solvers.length; i++){
				if(run[i]){
					String filename = args[1];
					FileWriter o = new FileWriter(filename + "_s"+i+".stat");
					o.write(solvers[i].getStatistics());
					o.close();
				}
			}
		}
		
		
	} catch (Exception e){
		e.printStackTrace();
	}

    }

    private static void printSolution(Stack<SolutionPart> solution){
    	String str = "";
		if(solution == null ||
				solution.size() == 0){
			str = "No Solution!";
		} else {
			SolutionPart s;
			try{
				while((s = solution.pop()) != null){
					str += s;
				}
			} catch (EmptyStackException e){
				// Stack empty, that means we are done
			}
		}
		System.out.println("### Solution ###");
		System.out.println(str);
    }
}
