package gdi1sokoban.planning;

import java.io.FileWriter;
import java.util.EmptyStackException;
import java.util.Stack;

import com.sun.xml.internal.ws.Closeable;

public class GoSolveYourself {

    public static void main(String args[]){

	LevelParser lp = new LevelParser();
	try{		
		Level l = lp.parse(args[0]);
		Board b = l.getBoard();
		Solver s1 = new BFSolver(l.getBoard());
		Solver s2 = new AstarSolver(l.getBoard(), new SubGoalIndependence());
		
		System.out.println("StartState:");
		System.out.println(b);
		
		boolean doStats = (args.length > 1);
		
		long before = System.currentTimeMillis();
		Stack<SolutionPart> solution = s1.solve(doStats);
		long after = System.currentTimeMillis();
		System.out.println(s1.getClass() + ": " + (after-before) + "ms");
			
		before = System.currentTimeMillis();
		solution = s2.solve(doStats);
		after = System.currentTimeMillis();
		System.out.println(s2.getClass() + ": " + (after-before) + "ms");
		
		if(doStats){
			String filename = args[1];
			FileWriter o1 = new FileWriter(filename + "_bf.stat");
			FileWriter o2 = new FileWriter(filename + "_astar.stat");
			o1.write(s1.getStatistics());
			o2.write(s2.getStatistics());
			o1.close();
			o2.close();
		}
		
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

		
	} catch (Exception e){
		e.printStackTrace();
	}

    }

}