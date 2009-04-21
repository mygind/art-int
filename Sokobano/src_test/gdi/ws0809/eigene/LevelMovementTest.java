package gdi.ws0809.eigene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import gdi1sokoban.logic.Level;
import gdi1sokoban.logic.Move;
import gdi1sokoban.logic.Position;

import java.util.LinkedList;

import org.junit.Test;


public class LevelMovementTest {

	@Test
	public void testInvalidMoves1() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#.$ @#");
		strings.add("##$  #");
		strings.add("#. * #");
		strings.add("######");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals("Incorrect undo indicator", false, level.canUndo());
		assertEquals("Incorrect redo indicator", false, level.canRedo());
		assertEquals("Incorrect move count", 0, level.getStepCount());
		assertEquals("Illegal move accepted", false, level.move(new Position(5, 1)));
		assertEquals("Incorrect move count", 0, level.getStepCount());
		assertEquals("Illegal move executed", strings, level.getBoard().toStringList());
	}
	
	@Test
	public void testInvalidMoves2() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#.$ @#");
		strings.add("##$  #");
		strings.add("#. * #");
		strings.add("######");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals("Incorrect undo indicator", false, level.canUndo());
		assertEquals("Incorrect redo indicator", false, level.canRedo());
		assertEquals("Incorrect move count", 0, level.getStepCount());
		assertEquals("Illegal move accepted", false, level.move(new Position(1, 1)));
		assertEquals("Incorrect move count", 0, level.getStepCount());
		assertEquals("Illegal move executed", strings, level.getBoard().toStringList());
	}
	
	@Test
	public void testLegalMoves1() {
		String strings =
			"######\n" +
		    "#.$ @#\n"+
		    "##  $#\n"+
		    "#. * #\n"+
		    "######\n";
		
		Level level = null;
		
		try {
			level = new Level(strings.split("\n"));
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		String result1 =
			"######\n" +
		    "#.$  #\n"+
		    "##@ $#\n"+
		    "#. * #\n"+
		    "######\n";
		
		String result2 =
			"######\n" +
		    "#.$  #\n"+
		    "##  $#\n"+
		    "#+ * #\n"+
		    "######\n";
		
		String result3 =
			"######\n" +
		    "#.$  #\n"+
		    "## @$#\n"+
		    "#. * #\n"+
		    "######\n";
		
		assertEquals("Incorrect undo indicator", false, level.canUndo());
		assertEquals("Incorrect redo indicator", false, level.canRedo());
		
		assertEquals("Incorrect move count", 0, level.getStepCount());
		assertEquals("Legal move rejected", true, level.move(new Position(2, 2)));
		assertEquals("Wrong execution", result1, level.getBoard().toString());
		assertEquals("Incorrect move count", 3, level.getStepCount());
		
		assertEquals("Legal move rejected", true, level.move(new Position(1, 3)));
		assertEquals("Wrong execution", result2, level.getBoard().toString());
		assertEquals("Incorrect move count", 5, level.getStepCount());
		
		assertEquals("Legal undo rejected", true, level.undo());
		assertEquals("Wrong undo", result1, level.getBoard().toString());
		assertEquals("Incorrect move count", 3, level.getStepCount());
		
		assertEquals("Legal undo rejected", true, level.undo());
		assertEquals("Wrong undo", strings, level.getBoard().toString());
		assertEquals("Incorrect move count", 0, level.getStepCount());
		
		assertEquals("Illegal undo accepted", false, level.undo());
		assertEquals("Incorrect move count", 0, level.getStepCount());
		
		assertEquals("Incorrect undo indicator", false, level.canUndo());
		assertEquals("Incorrect redo indicator", true, level.canRedo());
		
		assertEquals("Legal redo rejected", true, level.redo());
		assertEquals("Wrong redo", result1, level.getBoard().toString());
		assertEquals("Incorrect move count", 3, level.getStepCount());
		
		assertEquals("Incorrect undo indicator", true, level.canUndo());
		assertEquals("Incorrect redo indicator", true, level.canRedo());
		
		assertEquals("Legal redo rejected", true, level.redo());
		assertEquals("Wrong redo", result2, level.getBoard().toString());
		assertEquals("Incorrect move count", 5, level.getStepCount());
		
		assertEquals("Legal move rejected", true, level.move(new Position(3, 2)));
		assertEquals("Wrong move", result3, level.getBoard().toString());
		assertEquals("Incorrect move count", 8, level.getStepCount());
		
		assertEquals("Legal undo rejected", true, level.undo());
		assertEquals("Wrong undo", result2, level.getBoard().toString());
		assertEquals("Incorrect move count", 5, level.getStepCount());
		
		assertEquals("Legal undo rejected", true, level.undo());
		assertEquals("Wrong undo", result1, level.getBoard().toString());
		assertEquals("Incorrect move count", 3, level.getStepCount());
		
		assertEquals("Level solved wrong recognized", false, level.isSolved());
		assertEquals("Level deadlock wrong recognized", true, level.findDeadlock());
		
		assertEquals("Legal undo rejected", true, level.undo());
		assertEquals("Wrong undo", strings, level.getBoard().toString());
		assertEquals("Incorrect move count", 0, level.getStepCount());
		
		assertEquals("Incorrect undo indicator", false, level.canUndo());
		assertEquals("Incorrect redo indicator", true, level.canRedo());
	}
	
	@Test
	public void testLegalPushes1() {
		String strings =
			"######\n" +
		    "#. $@#\n"+
		    "##  $#\n"+
		    "#  *.#\n"+
		    "######\n";
		
		Level level = null;
		
		try {
			level = new Level(strings.split("\n"));
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		String result1 =
			"######\n" +
		    "#*@  #\n"+
		    "##  $#\n"+
		    "#  *.#\n"+
		    "######\n";
		
		String result2 =
			"######\n" +
		    "#*  @#\n"+
		    "##  $#\n"+
		    "#  *.#\n"+
		    "######\n";
		
		String result3 =
			"######\n" +
		    "#*   #\n"+
		    "##  @#\n"+
		    "#  **#\n"+
		    "######\n";
		
		assertEquals("Incorrect move count", 0, level.getStepCount());
		assertEquals("Legal push rejected", true, level.push(new Position(1, 1)));
		assertEquals("Wrong execution", result1, level.getBoard().toString());
		assertEquals("Incorrect move count", 2, level.getStepCount());
		
		assertEquals("Level solved wrong recognized", false, level.isSolved());
		assertEquals("Level deadlock wrong recognized", false, level.findDeadlock());
		
		assertEquals("Illegal push accepted", false, level.push(new Position(4, 1)));
		assertEquals("Illegal execution", result1, level.getBoard().toString());
		assertEquals("Incorrect move count", 2, level.getStepCount());
		
		assertEquals("Level solved wrong recognized", false, level.isSolved());
		assertEquals("Level deadlock wrong recognized", false, level.findDeadlock());
		
		assertEquals("Legal move rejected", true, level.move(new Position(4, 1)));
		assertEquals("Wrong execution", result2, level.getBoard().toString());
		assertEquals("Incorrect move count", 4, level.getStepCount());
		
		assertEquals("Level solved wrong recognized", false, level.isSolved());
		assertEquals("Level deadlock wrong recognized", false, level.findDeadlock());
		
		assertEquals("Legal push rejected", true, level.push(new Position(4, 3)));
		assertEquals("Wrong execution", result3, level.getBoard().toString());
		assertEquals("Incorrect move count", 5, level.getStepCount());
		
		assertEquals("Incorrect undo indicator", true, level.canUndo());
		assertEquals("Incorrect redo indicator", false, level.canRedo());
		
		assertEquals("Level solved not recognized", true, level.isSolved());
		assertEquals("Level deadlock wrong recognized", false, level.findDeadlock());
	
		assertEquals("Legal undo rejected", true, level.undo());
		assertEquals("Wrong undo", result2, level.getBoard().toString());
		assertEquals("Incorrect move count", 4, level.getStepCount());
		
		assertEquals("Level solved not recognized", false, level.isSolved());
		assertEquals("Level deadlock wrong recognized", false, level.findDeadlock());
	
		assertEquals("Legal undo rejected", true, level.undo());
		assertEquals("Wrong undo", result1, level.getBoard().toString());
		assertEquals("Incorrect move count", 2, level.getStepCount());
		
		assertEquals("Legal undo rejected", true, level.undo());
		assertEquals("Wrong undo", strings, level.getBoard().toString());
		assertEquals("Incorrect move count", 0, level.getStepCount());
		
		assertEquals("Illegal undo accepted", false, level.undo());
		assertEquals("Wrong undo", strings, level.getBoard().toString());
		assertEquals("Incorrect move count", 0, level.getStepCount());
		
		assertEquals("Incorrect undo indicator", false, level.canUndo());
		assertEquals("Incorrect redo indicator", true, level.canRedo());
	}
	
	@Test
	public void testGetSetMoves1() {
		String strings =
			"######\n" +
		    "#.$ @#\n"+
		    "##  $#\n"+
		    "#  *.#\n"+
		    "######\n";
		
		Level level = null;
		
		try {
			level = new Level(strings.split("\n"));
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		String result1 =
			"######\n" +
		    "#*@  #\n"+
		    "##  $#\n"+
		    "#  *.#\n"+
		    "######\n";
		
		LinkedList<Position> positions1 = new LinkedList<Position>();
		positions1.add(new Position(4, 1));
		positions1.add(new Position(3, 1));
		Move move1 = new Move(positions1, false);
		
		LinkedList<Position> positions2 = new LinkedList<Position>();
		positions2.add(new Position(3, 1));
		positions2.add(new Position(2, 1));
		Move move2 = new Move(positions2, true);
		
		LinkedList<Move> moves = new LinkedList<Move>();
		moves.add(move1);
		moves.add(move2);
		
		assertEquals("Incorrect move count", 0, level.getStepCount());
		
		level.setMoves(moves);
		level.forward();
		
		assertEquals("getMoves wrong result", moves, level.getMoves());
		assertEquals("Wrong execution", result1, level.getBoard().toString());
		assertEquals("Incorrect move count", 2, level.getStepCount());
		
		assertEquals("Level solved wrong recognized", false, level.isSolved());
		assertEquals("Level deadlock wrong recognized", false, level.findDeadlock());
		
		assertEquals("Illegal push accepted", false, level.push(new Position(4, 1)));
		assertEquals("Illegal execution", result1, level.getBoard().toString());
		assertEquals("Incorrect move count", 2, level.getStepCount());
		
		assertEquals("Level solved wrong recognized", false, level.isSolved());
		assertEquals("Level deadlock wrong recognized", false, level.findDeadlock());
			
		assertEquals("Incorrect undo indicator", true, level.canUndo());
		assertEquals("Incorrect redo indicator", false, level.canRedo());
		
		assertEquals("Legal undo rejected", true, level.undo());
		assertEquals("Legal undo rejected", true, level.undo());
		assertEquals("Wrong undo", strings, level.getBoard().toString());
		assertEquals("Incorrect move count", 0, level.getStepCount());
		
		assertEquals("Illegal undo accepted", false, level.undo());
		assertEquals("Wrong undo", strings, level.getBoard().toString());
		assertEquals("Incorrect move count", 0, level.getStepCount());
		
		assertEquals("Incorrect undo indicator", false, level.canUndo());
		assertEquals("Incorrect redo indicator", true, level.canRedo());
	}
}
