package gdi.ws0809.eigene;

import static org.junit.Assert.*;
import gdi1sokoban.logic.Level;
import gdi1sokoban.logic.Position;

import java.util.LinkedList;

import org.junit.Test;


public class PathFinderTest {

	@Test
	public void test1() {
		
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#@   #");
		strings.add("#  ###");
		strings.add("# #  #");
		strings.add("#  # #");
		strings.add("## # #");
		strings.add("#$.  #");
		strings.add("######");
		
		String result = 
			"[[1, 1]," +
			" [1, 2]," +
			" [1, 3]," +
			" [1, 4]," +
			" [2, 4]," +
			" [2, 5]," +
			" [2, 6]," +
			" [3, 6]," +
			" [4, 6]," +
			" [4, 5]," +
			" [4, 4]," +
			" [4, 3]," +
			" [3, 3]]";
		
		Level level = null;
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		LinkedList<Position> path = level.findPath(new Position(3, 3));
		if (path == null) fail("Existing path not found!");
		assertEquals(path.toString(), result);
	}
	
	@Test
	public void test2() {
		
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#    #");
		strings.add("#  ###");
		strings.add("# #@ #");
		strings.add("# ## #");
		strings.add("#  # #");
		strings.add("#$.  #");
		strings.add("######");
		
		String result = 
			"[[3, 3]," +
			" [4, 3]," +
			" [4, 4]," +
			" [4, 5]," +
			" [4, 6]," +
			" [3, 6]," +
			" [2, 6]," +
			" [2, 5]," +
			" [1, 5]," +
			" [1, 4]," +
			" [1, 3]," +
			" [1, 2]," +
			" [1, 1]]";
		
		Level level = null;
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		LinkedList<Position> path = level.findPath(new Position(1, 1));
		if (path == null) fail("Existing path not found!");
		assertEquals(path.toString(), result);
	}

}
