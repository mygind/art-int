package gdi1sokoban.logic;

/**
 * This node is used internally by the path finding algorithm.
 */
public class PathFinderNode implements Comparable<PathFinderNode> {
	
	private Position _position;
	private PathFinderNode _parent;
	private int _gScore; // movement cost from starting point to this point
	private int _hScore; // estimated movement cost to final destination
	
	public PathFinderNode(Position pos, Position target) {
		_position = pos;
		setHScore(target);
	}
	
	void setParent(PathFinderNode parent) {
		_parent = parent;
	}
	

	void setGScore(int gScore) {
		_gScore = gScore;
	}
	
	private void setHScore(Position target) {
		_hScore = Math.abs(target.getX() - _position.getX()) + Math.abs(target.getY() - _position.getY());
	}
	
	int getGScore() {
		return _gScore;
	}
	
	public int getFScore() {
		return _gScore + _hScore;
	}
	
	Position getPosition() {
		return _position;
	}
	
	PathFinderNode getParent() {
		return _parent;
	}

	public int compareTo(PathFinderNode pathFinderPosition) {
		
		return getFScore() - pathFinderPosition.getFScore();
	}
	
}
