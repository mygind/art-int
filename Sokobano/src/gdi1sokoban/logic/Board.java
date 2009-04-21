package gdi1sokoban.logic;

public class Board extends UnmodifiableBoard {
	
	public Board(int width, int height) {
		super(width, height);
	}
	
	/**
	 * Adds the type to the tile at the given position.
	 * 
	 * @param pos position
	 */
	public void addType(Position pos, int type) {
		assert(inBounds(pos));
		_tiles[pos.getY()][pos.getX()] += type;
	}
	
	/**
	 * 'Fills' the src tiles from the given position with dst.
	 * 
	 * @param pos starting position
	 * @return true whether there is a wall surrounding the floor
	 */
	public boolean fillType(Position pos, int src, int dst) {
		if (!inBounds(pos)) return false;

		if (isType(pos, src)) {

			addType(pos, dst);
			removeType(pos, src);

			// Fill neighbors:
			return fillType(pos.top(), src, dst) && fillType(pos.bottom(), src, dst) && 
				   fillType(pos.left(), src, dst) && fillType(pos.right(), src, dst);
		}
		return true;
	}
	
	/**
	 * Removes a type from the tile at the given position.
	 * 
	 * @param pos position
	 */
	public void removeType(Position pos, int type) {
		assert(inBounds(pos));
		_tiles[pos.getY()][pos.getX()] &= (~type);
	}

	/**
	 * Applies a 16-bit flag to the tile at the given position.
	 * 
	 * @param pos position
	 * @param value byte
	 */
	public void addFlag(Position pos, int flag) {
		assert(inBounds(pos));
		_tiles[pos.getY()][pos.getX()] += (flag << 8);
	}

	/**
	 * Removes a flag from the tile at the given position.
	 * 
	 * @param pos position
	 */
	public void removeFlag(Position pos, int flag) {
		assert(inBounds(pos));
		_tiles[pos.getY()][pos.getX()] &= (((~flag) << 8) + 0xFF);
	}
	
	public UnmodifiableBoard getUnmodifiable() {
		return (UnmodifiableBoard) this;
	}
	
	public int get(int x, int y) {
		return _tiles[y][x];
	}
}