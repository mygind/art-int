package gdi1sokoban.logic;

import java.util.ArrayList;

public class UnmodifiableBoard {

	// These constants are tile-types:
	public static final int TYPE_NONE = 64; 
	public static final int TYPE_FREE = 1;  // Empty space outside the walls
	public static final int TYPE_FLOOR = 2; // Inside the walls, simply no wall
	public static final int TYPE_WALL = 4;
	public static final int TYPE_TARGET = 8;
	public static final int TYPE_CRATE = 16;
	public static final int TYPE_WORKER = 32;

	protected int[][] _tiles; // Tiles in [y][x]-order

	protected int _width;
	protected int _height;
	
	public UnmodifiableBoard(int width, int height) {
		_width = width;
		_height = height;
		_tiles = new int[height][width];
	}
	
	/**
	 * Converts the tiles array into a string, as it was done in reverse
	 * direction on construction.
	 * 
	 * @return the string in microban format
	 */
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int y = 0; y < _height; y++) {			
			boolean checkFree = false;
			for (int x = 0; x < _width; x++) {
				if (!isType(new Position(x, y), TYPE_FREE)) checkFree = true;
				else if (checkFree) break;
				stringBuilder.append(convertTileToChar(_tiles[y][x]));
			}
			stringBuilder.append("\n");
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * Converts the tiles array into a string list, as it was done in reverse
	 * direction on construction.
	 * 
	 * @return the string list in microban format
	 */
	public ArrayList<String> toStringList() {
		ArrayList<String> result = new ArrayList<String>(_height);

		for (int y = 0; y < _height; y++) {
			
			StringBuilder stringBuilder = new StringBuilder();
			for (int x = 0; x < _width; x++) {
				stringBuilder.append(convertTileToChar(_tiles[y][x]));
			}
			result.add(stringBuilder.toString());
		}
		
		return result;
	}

	/**
	 * Converts a character to its corresponding integer constant value.
	 * 
	 * @param c character to convert
	 * @return integer constant, e.g. TILE_WALL
	 */
	public static int convertCharToTile(char c) {
		switch (c) {
		case '#':
			return TYPE_WALL;
		case '@':
			return TYPE_FREE + TYPE_WORKER;
		case '$':
			return TYPE_FREE + TYPE_CRATE;
		case '*':
			return TYPE_FREE + TYPE_CRATE + TYPE_TARGET;
		case '+':
			return TYPE_FREE + TYPE_WORKER + TYPE_TARGET;
		case '.':
			return TYPE_FREE + TYPE_TARGET;
		default:
			return TYPE_FREE;
		}
	}

	/**
	 * Converts a character to its corresponding integer constant value.
	 * 
	 * @param c character to convert
	 * @return integer constant, e.g. TILE_WALL
	 */
	public static char convertTileToChar(int tile) {
		switch (tile) {
		case TYPE_WALL:
			return '#';
		case TYPE_FLOOR + TYPE_WORKER + TYPE_TARGET:
			return '+';
		case TYPE_FLOOR + TYPE_WORKER:
			return '@';
		case TYPE_FLOOR + TYPE_CRATE + TYPE_TARGET:
			return '*';
		case TYPE_FLOOR + TYPE_CRATE:
			return '$';
		case TYPE_FLOOR + TYPE_TARGET:
			return '.';
		default:
			return ' ';
		}
	}

	/**
	 * Checks whether the tile at the given position is of the given type. A
	 * tile can be part of several types at the same time.
	 * 
	 * @param pos position
	 * @param type the checked type, e.g TILE_FLOOR
	 * @return whether the tile is of the given type
	 */
	public boolean isType(Position pos, int type) {
		assert(inBounds(pos));
		return (_tiles[pos.getY()][pos.getX()] & type) == type;
	}

	/**
	 * Checks whether the tile at the given position is of at least one of the
	 * given types. Type combinations are created by addition of type constants.
	 * 
	 * @param pos position
	 * @param type the combination of types, e.g TILE_FLOOR + TILE_WORKER
	 * @return whether the tile is of at least one of the given types
	 */
	public boolean containsType(Position pos, int type) {
		assert(inBounds(pos));
		return (_tiles[pos.getY()][pos.getX()] & type) > 0;
	}
	
	/**
	 * Checks whether the given position lies within the level bounds.
	 * 
	 * @param pos position
	 * @return true whether the position is valid
	 */
	public boolean inBounds(Position pos) {
		return pos.inBounds(0, 0, _width, _height);
	}
	
	/**
	 * Checks whether the given flag is set on the specified tile.
	 * 
	 * @param pos position
	 */
	public boolean isFlag(Position pos, int flag) {
		assert(inBounds(pos));
		return (_tiles[pos.getY()][pos.getX()] & (flag << 8)) == flag << 8;
	}
	
	public int getWidth() {
		return _width;
	}
	
	public int getHeight() {
		return _height;
	}
}