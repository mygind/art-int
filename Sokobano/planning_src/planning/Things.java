package planning;

import java.util.LinkedList;

public class Things {

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
	if ( o instanceof this.getClass() ){
	    Things t  = (Things) o;

	    LinkedList<Thing> l = t.getThings().clone();
	    for ( Thing i : things ){
		if ( ! l.removeFirstOccurance(i) ){
		    return false;
		}
	    }
	} 

	return true;
    }
}
