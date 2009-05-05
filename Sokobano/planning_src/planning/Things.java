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
}
