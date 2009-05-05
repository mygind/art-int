package planning;

import java.util.LinkedList;
import java.util.List;

public class Level {

    Board board;
    List<Goal> goals;
	
    public Level(){
	this.goals = new LinkedList<Goal>();
    }
	
    public void intialize(List<String> asciimap) throws ParseException{
	int maxY = asciimap.size();
	int maxX = 0;
	for(String s: asciimap){
	    if(s.length() > maxX){
		maxX = s.length();
	    }
	}
	board = new Board(maxX, maxY);
				
	int x = 0;
	int y = 0;
		
	for (String string : asciimap) {
	    x = 0;

	    for (char c : string.toCharArray()) { // Converting to a charArray is the fastest: http://www.christianschenk.org/blog/iterating-over-the-characters-in-a-string/
		LinkedList<Thing> things = char2Thing(c, x, y);
		board.add(things, x, y);
			
		x++;
	    }
	    for(int i = x; i < maxX; i++){
		board.add(char2Thing(' ',x,y), x, y);
	    }
	    y++;
	}
    }
	
    private LinkedList<Thing> char2Thing(char c, int x, int y) throws ParseException{
	LinkedList<Thing> things = new LinkedList<Thing>();
		
	Thing t1 = null;
	Thing t2 = null;
	switch(c){
	case '#': //wall
	    t1 = new Wall();
	    things.add(t1);
	    break;
		
	case '@': //player
	    t1 = new Player();
	    things.add(t1);
	    break;
		
	case '+': //Player on goal square
	    t1 = new Player();
	    t2 = new Goal();
	    things.add(t1);
	    things.add(t2);
	    goals.add((Goal)t2);
	    break;
		
	case '$': //Box 	$ 	0x24
	    t1 = new Box();
	    things.add(t1);
	    break;
		
	case '*': //Box on goal square 	* 	0x2a
	    t1 = new Box();
	    t2 = new Goal();
	    things.add(t1);
	    things.add(t2);
	    goals.add((Goal)t2);
	    break;
			
	case '.': //Goal square 	. 	0x2e
	    t1 = new Goal();
	    things.add(t1);
	    goals.add((Goal)t1);
	    break;
		
	case '-': //Floor
	case '_': //Floor
	case ' ': //Floor 	(Space) 	0x20
		    
	    break;
			
	default:
	    throw new ParseException("Oh no a map we can't handle!");
	}
		
	return things;
    }
    public String toString() {
	return board.toString();
    }
}
