package planning;

import java.util.Collection;
import java.util.LinkedList;

public class Things {
    private String floor = "-";

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
    public boolean equals(Object o){
		if ( o == null){
			System.out.println("shit");
		}
		if(!(o instanceof Things) ){
			System.out.println("damin");
			return false;
		}
	    LinkedList<Thing> l  = ((Things) o).getThings();
	    
	    if(l.size() == things.size()){
	    	if(things.size() == 0){
	    		return true;
	    	} else if( things.size() == 1){
	    		return things.getFirst().equals(l.getFirst());
	    	} else if(things.size() == 2){
	    		return (things.getFirst().equals(l.getFirst()) && 
	    		        things.getLast().equals(l.getLast())) ||
	    		       
	    		       (things.getFirst().equals(l.getLast()) && 
	    		        things.getLast().equals(l.getFirst()));
	    	} else {
	    		Exception e = new Exception("Something is terribly wrong - size cant be more than 2");
	    		e.printStackTrace();
	    	}
	    
	    } 
	    
		return false;
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
	    }else if (things.contains(new Player(0, 0))){
		return "+";
	    }else {
		return "b";
	    }
	} else {
	    return "c";
	}
    }
}
