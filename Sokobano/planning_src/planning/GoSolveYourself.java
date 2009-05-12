package planning;

public class GoSolveYourself {

    public static void main(String args[]){

	LevelParser lp = new LevelParser();
	try{		
		Level l = lp.parse(args[0]);
		Board b = l.getBoard();

		Player p = l.getPlayer();

		System.out.println("~~~ Step 1 ~~~");
		System.out.println("P(" + p.getX() + "," + p.getY() + ")");
		System.out.print(b);
		
		Action a = new Move(b , p.getX(), p.getY(), 1, 0, p);
		Board c = a.perform();
		
		System.out.println("~~~ Step 2 ~~~");
		System.out.println("P(" + p.getX() + "," + p.getY() + ")");
		System.out.print(c);
		
		a = new Move(c , p.getX(), p.getY(), -1, 0, p);
		Board d = a.perform();
		
		System.out.println(b.equals(c));
		System.out.println(b.equals(d));
		
		System.out.println("~~~ Step 3 ~~~");
		System.out.println("P(" + p.getX() + "," + p.getY() + ")");
		System.out.print(d);
	} catch (Exception e){
		e.printStackTrace();
	}

    }

}