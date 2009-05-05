package planning;

public class PushToTarget extends Action {

	protected Box _box;
	
	public PushToTarget(Board b, int x, int y, int dx, int dy, Player p, Box box) throws IllegalActionException{
		super(b, x, y, dx, dy, p);
		if(b.isFree(x+dx+dx, y+dy+dy)){
		   if(b.get(x+dx, y+dy).contains(box)){
			   if(b.get(x+dx+dx, y+dy+dy).contains(new Goal())){
		
				   _box = box;
			   } else {
				   throw new IllegalActionException("Can't pushToTarget the box onto a non-goal square.");
			   }
		   } else {
			   throw new IllegalActionException("Can't push if there is no box.");
		   }
		} else {
			throw new IllegalActionException("Can't push because space beyond box isn't free.");
		}
	}
	
	@Override
	public Board perform() {
		Board nb = _b.clone();
		
		nb.get(_x+_dx, _y+_dy).remove(_box);
		nb.get(_x+_dx+_dx, _y+_dy+_dy).add(_box);
		
		nb.get(_x, _y).remove(_p);
		nb.get(_x+_dx, _y+_dy).add(_p);
		
		_box.setAtTarget(true);
		
		return nb;
	}
	
	public String toString(){
		return super.toString().toUpperCase();
	}

}
