package gdi1sokoban.planning;

public abstract class Thing {
	
	public Thing(){
	}

    public boolean equals(Object o){
    	return this.getClass().isInstance(o);
    }
}


