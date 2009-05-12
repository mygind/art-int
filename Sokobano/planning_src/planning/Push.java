package planning;

public class Push extends Action {

	protected Box _box;
	
	public Push(Board b, int x, int y, int dx, int dy, Player p) throws IllegalActionException{
		super(b, x, y, dx, dy, p);
		if(b.isFree(x+dx+dx, y+dy+dy)){
			if(b.boxAt(x+dx, y+dy)){
				if(!b.goalAt(x+dx+dx, y+dy+dy)){
					// _box = ???
					
			   } else {
				   throw new IllegalActionException("Can't push the box onto a goal square.");
			   }
		   } else {
			   throw new IllegalActionException("Can't push if there is no box.");
		   }
		} else {
			throw new IllegalActionException("Can't push because space beyond box isn't free.");
		}
	}
	
	public Board perform() throws IllegalActionException{
		Board nb = new Board(_b);
		
		nb.moveBox(_x+_dx, _y+_dy, _dx, _dy);
//		nb.get(_x+_dx, _y+_dy).getThings().remove(_box);
//		nb.get(_x+_dx+_dx, _y+_dy+_dy).getThings().add(_box);
		
		nb.movePlayer(_x, _y, _dx, _dy);
//		nb.get(_x, _y).getThings().remove(_p);
//		nb.get(_x+_dx, _y+_dy).getThings().add(_p);
		
		//_box.setAtTarget(false);
		
		return nb;
	}
	
	public String toString(){
		return super.toString().toUpperCase();
	}
}
