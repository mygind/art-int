package gdi1sokoban.planning;

import gdi1sokoban.planning.heuristics.AveragePathHeuristic;
import gdi1sokoban.planning.heuristics.Box4x4Heuristic;
import gdi1sokoban.planning.heuristics.BoxOnGoalHeuristic;
import gdi1sokoban.planning.heuristics.CornerHeuristic;
import gdi1sokoban.planning.heuristics.HeuristicsAdder;
import gdi1sokoban.planning.heuristics.HeuristicsMultiplier;
import gdi1sokoban.planning.heuristics.RandomHeuristic;
import gdi1sokoban.planning.heuristics.ShortestPathHeuristic;
import gdi1sokoban.planning.heuristics.SubGoalIndependence;

import java.io.FileWriter;
import java.io.IOException;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.ArrayList;


public class GoSolveYourself {
    
    public static void main(String args[]){
	
	String statFilename = null;
	ArrayList<String> levelFilenames = new ArrayList<String>();
	int timeoutSecs = 30;
	boolean doStats = false;
	
	for ( int i = 0; i < args.length; i++ ){
	    
	    if ( args[i].equals("--")){
		break;
	    }else if ( args[i].equals("-s") ){
		if ( i < args.length){
		    doStats = true;
		    statFilename=args[i+1];
		    doStats = true;
		    i++;
		}else{
		    System.err.println("No statistics filename supplied");
		    System.exit(-1);
		}
	    }else if ( args[i].equals("-t") && i < args.length ){
		try { 
		    timeoutSecs = Integer.parseInt(args[i+1]);
		    i++;
		} catch (NumberFormatException e){
		    System.err.println("Error "+args[i+1]+" was not formatted correctly as an integer");
		}
	    }else{
		levelFilenames.add(args[i]);
	    }
	}
	
	FileWriter o = null;
	try {
	
	     o = new FileWriter(statFilename);
	}catch ( IOException e ){
	    System.err.println("This must not happen "+ e.getMessage());
	    System.exit(-1);
	}

    	for ( String file : levelFilenames){
	    
	    System.out.print(file+" : ");
	    LevelParser lp = new LevelParser();
	    try{		
		Level l = lp.parse(file);
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
		
		HeuristicsAdder h9 = new HeuristicsAdder(b);
		HeuristicsMultiplier h9a = new HeuristicsMultiplier(b);
		h9a.add(new BoxOnGoalHeuristic(b));
		//h9a.add(new BoxOnGoalHeuristic(b));
		h9a.add(new AveragePathHeuristic(b));
		h9a.add(new AveragePathHeuristic(b));
		h9.add(h9a);
		h9.add(new CornerHeuristic(b));
		h9.add(new Box4x4Heuristic(b));
		

		boolean[] run = {true, true, true, true, true, true, true, true, true, true};
		Solver[] solvers = {new BFSolver(new Board(l.getBoard())),
		                    new AstarSolver(new Board(l.getBoard()), new SubGoalIndependence(b)),
		                    new AstarSolver(new Board(l.getBoard()), new CornerHeuristic(b)),
		                    new AstarSolver(new Board(l.getBoard()), h4),
		                    new AstarSolver(new Board(l.getBoard()), h5),
		                    new AstarSolver(new Board(l.getBoard()), h6),
		                    new AstarSolver(new Board(l.getBoard()), h7),
		                    new AstarSolver(new Board(l.getBoard()), h8),
		                    new AstarSolver(new Board(l.getBoard()), h9),
		                    new AstarSolver(new Board(l.getBoard()), new RandomHeuristic(b))
				    
		};

		if(solvers.length != run.length){
		    throw new Exception("run != solvers");
		}
		
		
		Stack<SolutionPart> solution;
		long before, after;
		
		
		for(int i = 0; i < solvers.length; i++){
		    if(run[i]){
			
			RunSolver r = new RunSolver(solvers[i]);
			
			before = System.currentTimeMillis();

			r.start();
			Thread.sleep(1000*timeoutSecs);

			after = System.currentTimeMillis();
			
			if ( r.isDone() && doStats ){
			    r.getSolver().setExecutionTime(after-before);
			    o.write(r.getSolver().getStatistics());
			}
			
			if ( r.isDone() ){
			    printSolution(r.getSolution());
			    System.out.println((after-before) + "ms");
			}else{
			    System.out.println("Solver was killed after "+(after-before)+"ms");
			}
		    }
		}
		
	    } catch (Exception e){
		e.printStackTrace();
	    }
	}
	try {
	    o.close();
	}catch(IOException e){
	    System.err.println("Could not close file "+statFilename +" "+e.getMessage());
	    System.exit(-1);
	}

	
    }
    
    private static void printSolution(Stack<SolutionPart> solution){
    	String str = "";
	if(solution == null ||
	   solution.size() == 0){
	    str = "No Solution!";
	} else {
	    SolutionPart s;			try{
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

class RunSolver extends Thread {
    Solver s;
    Stack<SolutionPart> solution  = null;
    boolean done=false;
    
    public RunSolver(Solver s){
	this.s = s;
    }
    
    public void run(){
	solution = s.solve(true);
	done=true;
    }
    public boolean isDone(){
	return done;
    }
    
    public Solver getSolver(){
	return s;
    }
    public Stack<SolutionPart> getSolution(){
	return solution;
    }
    
}
