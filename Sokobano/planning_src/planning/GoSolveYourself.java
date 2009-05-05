package planning;

public class GoSolveYourself {

    public static void main(String args[]){

	LevelParser lp = new LevelParser();
	try{
		Level l = lp.parse(args[0]);
		System.out.println(l);
	} catch (Exception e){
		e.printStackTrace();
	}

    }

}