package planning;

import java.util.Stack;

public class GoSolveYourself {

    public static void main(String args[]){

	LevelParser lp = new LevelParser();
	try{		
		Level l = lp.parse(args[0]);
		Board b = l.getBoard();
		Solver solver = new BFSolver(l);
		
		System.out.println(b);
		
		
		Stack<SolutionPart> solution = solver.solve();
		
		String str = "";
		if(solution == null){
			str = "No Solution!";
		} else {
			SolutionPart s;
			while((s = solution.pop()) != null){
				str += s;
			}
		}
		System.out.println("### Solution ###");
		System.out.println(str);

		
	} catch (Exception e){
		e.printStackTrace();
	}

    }

}