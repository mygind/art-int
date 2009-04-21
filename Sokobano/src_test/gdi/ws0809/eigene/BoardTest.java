package gdi.ws0809.eigene;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import gdi1sokoban.logic.Board;
import gdi1sokoban.logic.Position;


public class BoardTest {
	@Test
	public void testTypes1() {
		Position pos = new Position(0, 0);
		Board board = new Board(10, 10);
		board.addType(pos, Board.TYPE_CRATE);
		board.addType(pos, Board.TYPE_FLOOR);
		board.addType(pos, Board.TYPE_WORKER);
		board.addType(pos, Board.TYPE_TARGET);
		
		assertEquals("Type missing", true, board.isType(pos, Board.TYPE_CRATE));
		assertEquals("Type missing", true, board.isType(pos, Board.TYPE_FLOOR));
		assertEquals("Type missing", true, board.isType(pos, Board.TYPE_WORKER));
		assertEquals("Type missing", true, board.isType(pos, Board.TYPE_TARGET));
		
		board.removeType(pos, Board.TYPE_TARGET);
		assertEquals("Type missing", true, board.isType(pos, Board.TYPE_CRATE));
		assertEquals("Type missing", true, board.isType(pos, Board.TYPE_FLOOR));
		assertEquals("Type missing", true, board.isType(pos, Board.TYPE_WORKER));
		assertEquals("Type not removed", false, board.isType(pos, Board.TYPE_TARGET));
	}
	
	@Test
	public void testFlags1() {
		Position pos = new Position(0, 0);
		Board board = new Board(10, 10);
		int flag1 = 0;
		int flag2 = 1;
		int flag3 = 2;
		
		board.addType(pos, Board.TYPE_CRATE);
		board.addType(pos, Board.TYPE_FLOOR);
		
		board.addFlag(pos, flag1);
		board.addFlag(pos, flag2);
		board.addFlag(pos, flag3);
		
		board.addType(pos, Board.TYPE_WORKER);
		board.addType(pos, Board.TYPE_TARGET);
		
		assertEquals("Flag missing", true, board.isFlag(pos, flag1));
		assertEquals("Flag missing", true, board.isFlag(pos, flag2));
		assertEquals("Flag missing", true, board.isFlag(pos, flag3));
		
		board.removeFlag(pos, flag3);
		
		assertEquals("Flag missing", true, board.isFlag(pos, flag1));
		assertEquals("Flag missing", true, board.isFlag(pos, flag2));
		assertEquals("Flag not removed", false, board.isFlag(pos, flag3));
		
		assertEquals("Type missing", true, board.isType(pos, Board.TYPE_CRATE));
		assertEquals("Type missing", true, board.isType(pos, Board.TYPE_FLOOR));
		assertEquals("Type missing", true, board.isType(pos, Board.TYPE_WORKER));
		assertEquals("Type missing", true, board.isType(pos, Board.TYPE_TARGET));
	}
}
