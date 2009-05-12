package planning;

public class Move extends Action {
	
	public Move(Board b, int x, int y, int dx, int dy, Player p) throws IllegalActionException{
		super(b, x, y, dx, dy, p);
		
		if(b.isFree(x+dx, y+dy)){
		
		} else {
			throw new IllegalActionException("Space is occupied");
		}
				
	}
	
	public Board perform(){
		Board nb = _b.clone();
		nb.movePlayer(_x, _y, _dx, _dy);
		_p.setPosition(_x+_dx, _y+_dy);
		
		return nb;
	}
	
	public String toString(){
		if(_dx != 0){
			if(_dx == 1){
				return "r";
			} else {
				return "l";
			}
		} else {
			if(_dy == 1){
				return "d";
			} else {
				return "u";
			}
		}
	}
}
