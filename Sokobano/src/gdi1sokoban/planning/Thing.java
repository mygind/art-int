package gdi1sokoban.planning;

public abstract class Thing {
	
	protected int x, y;
	
	public Thing(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setPosition(int x, int y){
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
    	return this.getClass().isInstance(o);
    }
    
    
}


