package planning;

public abstract class Thing {
	protected int x, y; // Position on map
	
	public Thing(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
    
    public boolean equals(Object o){
	if ( o instanceof this.getClass() ){
	    return true;
	}else{
	    return false;
	}
    }
}


