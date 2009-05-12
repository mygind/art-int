package planning;

public class PushToTarget extends Action {

	protected Box _box;
	
	public PushToTarget(Board b, int x, int y, int dx, int dy, Player p) throws IllegalActionException{
		super(b, x, y, dx, dy, p);
		try{
			if(b.isFree(x+dx+dx, y+dy+dy)){
				if(b.boxAt(x+dx, y+dy)){
					if(b.goalAt(x+dx+dx, y+dy+dy)){
						
	//			int boxInt = 0;
	//			if((boxInt = b.get(x+dx, y+dy).getThings().indexOf(new Box())) != -1){
	//			   if(b.get(x+dx+dx, y+dy+dy).getThings().contains(new Goal())){
	//		
	//				   _box = (Box)b.get(x+dx, y+dy).getThings().get(boxInt);
				   } else {
					   throw new IllegalActionException("Can't pushToTarget the box onto a non-goal square.");
				   }
			   } else {
				   throw new IllegalActionException("Can't push if there is no box.");
			   }
			} else {
				throw new IllegalActionException("Can't push because space beyond box isn't free.");
			}
		} catch(IndexOutOfBoundsException e){
			throw new IllegalActionException("About to move out of the map: ("+(x+dx+dx)+", "+(y+dy+dy)+") is outside map.");
		}
	}
	
	@Override
	public Board perform() throws IllegalActionException{
		Board nb = new Board(_b);//.clone();
		
		nb.moveBox(_x+_dx, _y+_dy, _dx, _dy);
		
		nb.movePlayer(_x, _y, _dx, _dy);
		
//		nb.get(_x+_dx, _y+_dy).getThings().remove(_box);
//		nb.get(_x+_dx+_dx, _y+_dy+_dy).getThings().add(_box);
//		
//		nb.get(_x, _y).getThings().remove(_p);
//		nb.get(_x+_dx, _y+_dy).getThings().add(_p);
//		
//		_box.setAtTarget(true);
		
		return nb;
	}
	
	@Override
	public String toString(){
		if(_dx != 0){
			if(_dx == 1){
				return "R";
			} else {
				return "L";
			}
		} else {
			if(_dy == 1){
				return "D";
			} else {
				return "U";
			}
		}
	}
}
