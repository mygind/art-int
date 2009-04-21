package gdi.ws0809.eigene;

// DEPRECATED TEST CASES!!
// SEE PathFinderTest.java
//XXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXX
//XXXXXXXXXXXXXXXXXXXXXXXXX
import static org.junit.Assert.*;

import java.util.LinkedList;

import gdi1sokoban.logic.Level;
import gdi1sokoban.logic.Position;

import org.junit.Before;
import org.junit.Test;

public class PathFindingTest {

	Level level;
	@Before
	public void setUp() throws Exception {
		int[][] array = new int[][]
		                {{Level.TILE_WALL, Level.TILE_WALL, Level.TILE_WALL, Level.TILE_WALL}, 
						 {Level.TILE_WALL, Level.TILE_FLOOR, Level.TILE_FLOOR, Level.TILE_WALL},
						 {Level.TILE_WALL, Level.TILE_FLOOR, Level.TILE_FLOOR, Level.TILE_WALL},
						 {Level.TILE_WALL, Level.TILE_FLOOR, Level.TILE_FLOOR, Level.TILE_WALL},
						 {Level.TILE_WALL, Level.TILE_WALL, Level.TILE_WALL, Level.TILE_WALL}};
		level = new Level(array);
		level.setWorker(1, 1);
	}
	
	@Test (timeout = 1000)
	public void test1() {
		
		LinkedList<Position> expectedPath = new LinkedList<Position>();
		expectedPath.add(new Position(1, 2));
		expectedPath.add(new Position(1, 3));
		LinkedList<Position> path = level.findPath(1, 3);
		assertEquals(path.toString(), expectedPath.toString());
	}

}
