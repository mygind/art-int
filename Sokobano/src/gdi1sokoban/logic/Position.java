package gdi1sokoban.logic;

import java.io.Serializable;



public class Position implements Serializable{
	
	public static final int TOP = 0;
	public static final int RIGHT = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	
	private int _x;
	private int _y;
	
	public Position(int x, int y) {
		_x = x;
		_y = y;
	}
	
	public Position(int position) {
		_x = position & 0xFF;
		_y = position >> 8;
	}
	
	public Position(Position pos) {
		_x = pos.getX();
		_y = pos.getY();
	}
	
	/**
	 * Sets the x y coordinates of the position accordingly	
	 * 
	 * @param x the new x coordinate
	 * @param y the new y coordinate
	 */
	public void set(int x, int y) {
		_x = x;
		_y = y;
	}
	
	/**
	 * Returns the x coordinate of this position
	 * 
	 * @return the x coordinate
	 */
	public int getX() {
		return _x;
	}

	/**
	 * Sets the x coordinate of the position accordingly
	 * 
	 * @param _x the new x coordinate
	 */
	public void setX(int _x) {
		this._x = _x;
	}

	/**
	 * Returns the y coordinate of this position
	 * 
	 * @return the y coordinate
	 */
	public int getY() {
		return _y;
	}

	/**
	 * Sets the y coordinate of the position accordingly
	 * 
	 * @param _y the new y coordinate
	 */
	public void setY(int _y) {
		this._y = _y;
	}
	
	/**
	 * Returns the string representation of the position in form of [x, y]
	 * 
	 * @return the string representation
	 */
	public String toString() {
		return "[" + _x + ", " + _y + "]";
	}
	
	/**
	 * Returns the position of the immediate neighbor of this position in the given direction
	 * 
	 * @param direction the direction in which the neighbor lies
	 * @return the position of the immediate neighbor in the given direction
	 */
	public Position neighbor(int direction) {
		assert((direction < 4) && (direction >= 0));
    	return new Position(_x + ((2 - direction) % 2), _y + ((direction - 1) % 2));
	}
	
	/**
	 * Returns the position of the neighbor to the top left of this position
	 * 
	 * @return the position of the neighbor to the top left
	 */
	public Position topLeft() {
		return new Position(_x - 1, _y - 1);
	}
	
	/**
	 * Returns the position of the neighbor to the top of this position
	 * 
	 * @return the position of the neighbor to the top
	 */
	public Position top() {
		return new Position(_x, _y - 1);
	}
	
	/**
	 * Returns the position of the neighbor to the top right of this position
	 * 
	 * @return the position of the neighbor to the top right
	 */
	public Position topRight() {
		return new Position(_x + 1, _y - 1);
	}
	
	/**
	 * Returns the position of the neighbor to the left of this position
	 * 
	 * @return the position of the neighbor to the left
	 */
	public Position left() {
		return new Position(_x - 1, _y);
	}
	
	/**
	 * Returns the position of the neighbor to the right of this position
	 * 
	 * @return the position of the neighbor to the right
	 */
	public Position right() {
		return new Position(_x + 1, _y);
	}
	
	
	/**
	 * Returns the position of the neighbor to the bottom left of this position
	 * 
	 * @return the position of the neighbor to the bottom left
	 */
	public Position bottomLeft() {
		return new Position(_x - 1, _y + 1);
	}
	
	/**
	 * Returns the position of the neighbor to the bottom of this position
	 * 
	 * @return the position of the neighbor to the bottom
	 */
	public Position bottom() {
		return new Position(_x, _y + 1);
	}
	
	/**
	 * Returns the position of the neighbor to the bottom right of this position
	 * 
	 * @return the position of the neighbor to the bottom right
	 */
	public Position bottomRight() {
		return new Position(_x + 1, _y + 1);
	}
	
	/**
	 * Moves this position one step ahead in the given direction
	 * 
	 * @param direction the given direction
	 */
	public void move(int direction) {
		assert((direction < 4) && (direction >= 0));
		_x += (2 - direction) % 2;
    	_y += (direction - 1) % 2;
	}
	
	/**
	 * Moves this position one step down
	 */
	public void moveDown() {
		_y++;
	}
	
	/**
	 * Moves this position one step up
	 */
	public void moveUp() {
		_y--;
	}
	

	/**
	 * Moves this position one step to the left
	 */
	public void moveLeft() {
		_x--;
	}
	
	/**
	 * Moves this position one step to the right
	 */
	public void moveRight() {
		_x++;
	}
	
	
	public long toLong() {
		return _x + ((long) _y << 16);
	}
	
	public int toInt() {
		return _x + (_y << 8);
	}
	
	/**
	 * Checks whether this position is within the given bound
	 * 
	 * @param x1 the left bound
	 * @param y1 the top bound
	 * @param x2 the right bound
	 * @param y2 the bottom bound
	 * @return true if this position is within the given bound
	 */
	public boolean inBounds(int x1, int y1, int x2, int y2) {
		return ((_x >= x1) && (_x < x2) &&
				(_y >= y1) && (_y < y2));
	}
	
	
	public boolean isNeighbor(Position position) {
		return ((position.getX() == _x) && (Math.abs(position.getY() - _y) <= 1)) ||
			   ((position.getY() == _y) && (Math.abs(position.getX() - _x) <= 1));
	}
	
	/**
	 * Returns the direction from this position to the given position
	 * 
	 * @param position the given position
	 * @return the direction from this position to the given position
	 */
	public int getDirection(Position position) {
		if (position.getX() == _x) {
			if (position.getY() > _y) return BOTTOM;
			else return TOP;
		}
		else if (position.getY() == _y) {
			if (position.getX() > _x) return RIGHT;
			else return LEFT;
		}
		return -1;
	}
	
	public int hashCode() {
		return toInt();
	}
	
	public boolean equals(Object object) {
		if (object instanceof Position)
			return ((((Position)object).getX() == _x) && (((Position)object).getY() == _y));
		return super.equals(object);
	}
	
	/**
	 * Returns the inverse direction of the given direction
	 * 
	 * @param direction the given direction
	 * @return the inverse direction of the given direction
	 */
	public static int reverse(int direction) {
		assert((direction < 4) && (direction >= 0));
		return (direction + 2) % 4;
	}
}
