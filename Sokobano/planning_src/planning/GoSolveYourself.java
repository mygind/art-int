package planning;

import java.util.EmptyStackException;
import java.util.Stack;

public class GoSolveYourself {

    public static void main(String args[]){

	LevelParser lp = new LevelParser();
	try{		
		Level l = lp.parse(args[0]);
		Board b = l.getBoard();
		Solver solver = new BFSolver(l);
		
		System.out.println("StartState:");
		System.out.println(b);
		
		
		Stack<SolutionPart> solution = solver.solve();
		
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