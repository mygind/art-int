package gdi.ws0809.eigene;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URISyntaxException;
import java.util.LinkedList;

import gdi.ws0809.test.SokobanTest;
import gdi.ws0809.test.SokobanTestAdapter;
import gdi1sokoban.logic.Level;
import gdi1sokoban.logic.Move;
import gdi1sokoban.logic.Position;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class LevelSolverTest {
	private SokobanTest testee = null;
	
	@BeforeClass
	public static void init() {
		SokobanTestAdapter.init();
	}
	
	@Before
	public void before() {
		testee = new SokobanTestAdapter();
	}
	
	public String solveLevel(Level _level, int maxTime) {
		//LinkedList<Position> path = _level.findSolution();
		//if (path == null) return null;

		String result = "";
		_level.move(new Position(1, 1));
		/*while (!path.isEmpty()) {
			Position target = path.removeFirst();
			Position push = path.removeFirst();
			
			System.out.println(target);
			_level.move(target);
			_level.push(push);
		}*/
		System.out.println("Done");
		LinkedList<Move> moves = _level.getMoves();
		
		while (!moves.isEmpty()) {
			Move move = moves.removeFirst();
			LinkedList<Position> steps = move.getPath();
			Position stepStart = steps.removeFirst();
			while (!steps.isEmpty()) {
				
				Position stepEnd = steps.removeFirst();
				
				result += stepStart.getDirection(stepEnd);
				stepStart = stepEnd;
			}
		}
		return result;
	}
	
	@Test
	public void testGetSetMoves1() {
		String strings =
			"######\n"+
		    "#   @#\n"+
		    "# $  #\n"+
		    "# .  #\n"+
		    "######\n";
		String solution = "LLD";
		
		Level level = null;
		
		try {
			level = new Level(strings.split("\n"));
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		System.out.println(solveLevel(level, 1000));
		assertEquals(solution, solveLevel(level, 1000));
	}
}
