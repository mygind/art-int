package gdi1sokoban.graphic;

import org.lwjgl.opengl.GL11;

public class DisplayList implements Renderable {
	int _displayList;
	
	public DisplayList(int displayList) {
		_displayList = displayList;
	}
	
	public void render() {
		GL11.glCallList(_displayList);
	}
	
	public void finalize() throws Throwable {
		GL11.glDeleteLists(_displayList, 1);
	}
}
