package planning;

import java.util.LinkedList;

public class Things {
    private String floor = " ";

	private LinkedList<Thing> things;
	public Things(){
		things = new LinkedList<Thing>();
	}
	
	public Things(LinkedList<Thing> things){
		this.things = things;
	}
	
	public LinkedList<Thing> getThings() {
		return things;
	}
    public boolean equals(Object o ){
	if ( this.getClass().isInstance(o) ){
	    Things t  = (Things) o;

	    LinkedList<Thing> l = (LinkedList<Thing>) t.getThings().clone();
	    for ( Thing i : things ){
		if ( ! l.removeFirstOccurrence(i) ){
		    return false;
		}
	    }
	} 

	return true;
    }

    public void setFloor(char f){
	floor = "" + f;
    }

    public String toString(){

	if ( things.size() == 0){
	    return floor;
	} else if ( things.size() == 1){
	    Thing t = things.peek(); 
	    if ( t instanceof Wall ){
		return "#";
	    } else if (t instanceof Player ){
		return "@";
	    } else if ( t instanceof Box ){
		return "$";
	    } else if ( t instanceof Goal ){
		return ".";
	    } else {
		return "a";
	    }
	} else if ( things.size() == 2 && things.contains(new Goal()) ){
	    if ( things.contains(new Box()) ){
		return "*";
	    }else if (things.contains(new Player())){
		return "+";
	    }else {
		return "b";
	    }
	} else {
	    return "c";
	}
    }
}
