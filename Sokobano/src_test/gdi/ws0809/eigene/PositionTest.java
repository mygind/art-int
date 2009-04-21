package gdi.ws0809.eigene;

import static org.junit.Assert.assertEquals;
import gdi1sokoban.logic.Position;

import org.junit.Test;


public class PositionTest {
	
	@Test
	public void Neighbor1() {
		Position pos = new Position(2, 2);
		Position top = new Position(2, 1);
		Position left = new Position(1, 2);
		Position right = new Position(3, 2);
		Position bottom = new Position(2, 3);
		
		Position wrong1 = new Position(3, 3);
		Position wrong2 = new Position(4, 3);
		Position wrong3 = new Position(1, 1);
		Position wrong4 = new Position(2, 4);
		
		assertEquals(true, pos.isNeighbor(top));
		assertEquals(true, pos.isNeighbor(left));
		assertEquals(true, pos.isNeighbor(right));
		assertEquals(true, pos.isNeighbor(bottom));
		
		assertEquals(false, pos.isNeighbor(wrong1));
		assertEquals(false, pos.isNeighbor(wrong2));
		assertEquals(false, pos.isNeighbor(wrong3));
		assertEquals(false, pos.isNeighbor(wrong4));
	}
	
	@Test
	public void Equals1() {
		Position pos = new Position(2, 2);
		Position equal = new Position(2, 2);
		
		assertEquals(pos, equal);
	}
	
	@Test
	public void Direction1() {
		Position pos = new Position(2, 2);
		Position top = new Position(2, 1);
		Position left = new Position(1, 2);
		Position right = new Position(3, 2);
		Position bottom = new Position(2, 3);
		
		assertEquals(top, pos.neighbor(0));
		assertEquals(right, pos.neighbor(1));
		assertEquals(bottom, pos.neighbor(2));
		assertEquals(left, pos.neighbor(3));
		
		assertEquals(top, pos.top());
		assertEquals(right, pos.right());
		assertEquals(bottom, pos.bottom());
		assertEquals(left, pos.left());
		
		assertEquals(0, pos.getDirection(top));
		assertEquals(1, pos.getDirection(right));
		assertEquals(2, pos.getDirection(bottom));
		assertEquals(3, pos.getDirection(left));
		
		assertEquals(top, top.neighbor(Position.TOP).neighbor(Position.BOTTOM));
	}
}
