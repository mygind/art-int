package gdi1sokoban.graphic;

import java.util.LinkedList;
import gdi1sokoban.logic.Position;
import gdi1sokoban.logic.UnmodifiableBoard;

public class Tile {
	
	int _rotation;
	boolean[][] _pattern;
	LinkedList<Model> _models;
	
	public Tile(boolean[][] pattern, LinkedList<Model> models) {
		_pattern = pattern;
		_models = models;
		_rotation = 0;
	}
	
	public boolean fitsRotated(UnmodifiableBoard board, Position pos, int tileType, int antiType) {
		if (fits(board, pos, tileType, antiType)) return true;
		rotate();
		if (fits(board, pos, tileType, antiType)) return true;
		rotate();
		if (fits(board, pos, tileType, antiType)) return true;
		rotate();
		if (fits(board, pos, tileType, antiType)) return true;
		rotate();
		return false;
	}
	
	public boolean fits(UnmodifiableBoard board, Position pos, int tileType, int antiType) {

		if (board.containsType(pos, antiType)) return false;
		
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				Position test = new Position(pos.getX() + x, pos.getY() + y);
				if (board.inBounds(test)) {
	
					// Part of the pattern does not match level tiles:
					if ((_pattern[y + 1][x + 1] && !board.containsType(test, tileType)))
					    //(!_pattern[y + 1][x + 1] && level.isType(posX + x, posY + y, tileType))))
							return false;
				} else {
					
					// Part of the pattern lies not within level bounds:
					if (_pattern[y + 1][x + 1])
						return false;
				}
			}
		}
		return true;
	}
	
	private void rotate() {
		
		boolean[][] rotated = new boolean[3][3];
		
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 3; y++) {
				rotated[y][x] = _pattern[2 - x][y];
			}
		}
		_rotation = (_rotation - 90) % 360;
		_pattern = rotated;
	}
	
	public boolean[][] getPattern() {
		return _pattern;
	}
	
	public int getRotation() {
		return _rotation;
	}
	
	public LinkedList<Model> getModels() {
		return _models;
	}
}