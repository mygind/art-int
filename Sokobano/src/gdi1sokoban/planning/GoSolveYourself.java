package gdi1sokoban.planning;

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

		boolean[] run = {false, false, false, false, true};
		Solver[] solvers = {new BFSolver(new Board(l.getBoard())),
		                    new AstarSolver(new Board(l.getBoard()), new SubGoalIndependence(b)),
		                    new AstarSolver(new Board(l.getBoard()), new CornerHeuristic(b)),
		                    new AstarSolver(new Board(l.getBoard()), h4),
		                    new AstarSolver(new Board(l.getBoard()), h5)};

		if(solvers.length != run.length){
			throw new Exception("run != solvers");
		}
		
		System.out.println("StartState:");
		System.out.println(b);
		
		boolean doStats = (args.length > 1);
		
		Stack<SolutionPart> solution;
		long before, after;
		
		for(int i = 0; i < solvers.length; i++){
			if(run[i]){
				before = System.currentTimeMillis();
				solution = solvers[i].solve(doStats);
				after = System.currentTimeMillis();
				System.out.println(solvers[i].getClass() + ": " + (after-before) + "ms");
				printSolution(solution);
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
