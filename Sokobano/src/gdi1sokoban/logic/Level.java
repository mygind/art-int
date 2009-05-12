package gdi1sokoban.logic;

import gdi1sokoban.exceptions.IllegalFormatException;
import gdi1sokoban.exceptions.LevelFormatException;
import gdi1sokoban.logic.event.LevelEvent;
import gdi1sokoban.logic.event.LevelListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;

/**
 * The level class is responsible for keeping track of the worker and crate
 * positions, movement, path finding, deadlock recognition, solving and other
 * things no one thought a level could do.
 */
public class Level extends LevelIdentifier{

	// Events:
	public static final int EVENT_DEADLOCK = 1;
	public static final int EVENT_STEP = 2;
	public static final int EVENT_BACK = 3;
	public static final int EVENT_PUSH = 4;
	public static final int EVENT_PULL = 5;
	public static final int EVENT_TARGET = 6;
	public static final int EVENT_SOLVED = 7;
	public static final int EVENT_CHEAT = 8;
	public static final int EVENT_STEPS = 9;
	public static final int EVENT_PUSHES = 10;
	
	// These constants are used by some algorithms:
	public static final int FLAG_CLOSED = 1;
	public static final int FLAG_OPEN = 2;
	public static final int FLAG_CRATE = 4;
	public static final int FLAG_VISITED = 8;
	
	private Board _board;
	
	private HashSet<Position> _crates = new HashSet<Position>();
	private ArrayList<Position> _targets = new ArrayList<Position>();
	private Position _worker;
	
	// Moves:
	LinkedList<Move> _moves = new LinkedList<Move>();
	ListIterator<Move> _iMove = null;
	
	// Observer:
	private LinkedList<LevelListener> _listeners = new LinkedList<LevelListener>();
	
	
	private String filename;
	/**
	 * Initializes the level from a given file corresponding to the microban
	 * rules.
	 * 
	 * @param filename
	 * @throws LevelFormatException
	 * @throws IllegalFormatException
	 * @throws IOException
	 */
	public Level(String levelFilename, LevelIdentifier ident) throws LevelFormatException,
			IllegalFormatException, IOException {
		super(ident);

		this.filename = levelFilename;
		// Create string representation from level file:
		int width = 0;
		int height = 0;
		LinkedList<String> strings = new LinkedList<String>();

		// Read each line of the file separately:
		BufferedReader reader = new BufferedReader(new FileReader(levelFilename));
		String line;

		while ((line = reader.readLine()) != null) {
			if (line.length() > width)
				width = line.length();
			if (containsCharTiles(line)) {
				strings.add(line);
				height++;
			}
		}
		reader.close();
		
		// Initialize the level from the read list of strings:
		initialize(strings, width, height);
	}
	
	public String getFilename() {
		return filename;
	}
	
	public boolean containsCharTiles(String string) {
		for (int i = 0; i < string.length(); i++) {
			if (Board.convertCharToTile(string.charAt(i)) != Board.TYPE_FREE)
				return true;
		}
		return true;
	}

	/**
	 * Initializes the level from a given list of strings. The length of the
	 * strings stored in the list must not vary.
	 * 
	 * @param strings the list of strings converted to tiles
	 * @throws IllegalFormatException
	 */
	public Level(LinkedList<String> strings) throws IllegalFormatException {
		super("test",0,"");
		int height = strings.size();
		int width = 0;
		
		if (height != 0)
			width = strings.get(0).length();

		initialize(strings, width, height);
	}
	
	public Level(String[] strings) throws IllegalFormatException {
		super("test",0,"");
		int height = strings.length;
		int width = 0;
		
		if (height != 0)
			width = strings[0].length();

		LinkedList<String> stringList = new LinkedList<String>();
		
		for (int i = 0; i < strings.length; i++) {
			stringList.add(strings[i]);
		}
		initialize(stringList, width, height);
	}
	
	// DO NOT DELETE OR MODIFY THIS CONSTRUCTOR!! AARRGH!
	public Level(String levelFilename)
			throws IllegalFormatException, IOException {
		super(new LevelIdentifier("", 0, ""));
		
		this.filename = levelFilename;
		
		// Create string representation from level file:
		int width = 0;
		int height = 0;
		LinkedList<String> strings = new LinkedList<String>();

		// Read each line of the file separately:
		BufferedReader reader = new BufferedReader(
				new FileReader(levelFilename));
		String line;

		while ((line = reader.readLine()) != null) {
			if (line.length() > width)
				width = line.length();
			if (containsCharTiles(line)) {
				strings.add(line);
				height++;
			}
		}
		reader.close();

		// Initialize the level from the read list of strings:
		initialize(strings, width, height);
	}
	
	/**
	 * Initializes the level from a given list of strings. Performs format
	 * checking and tile conversions.
	 * 
	 * @param strings the list of strings converted to tiles
	 * @throws IllegalFormatException
	 */
	private void initialize(List<String> strings, int width, int height) throws IllegalFormatException {

		// Create integer array from string list:
		_board = new Board(width, height);

		ListIterator<String> iString = strings.listIterator();
		for (int y = 0; y < height; y++) {

			String line = iString.next();
			for (int x = 0; x < line.length(); x++) {

				_board.addType(new Position(x, y), Board.convertCharToTile(line.charAt(x)));

				// Get worker position:
				if (_board.isType(new Position(x, y), Board.TYPE_WORKER)) {
					_worker = new Position(x, y);
				}
			}

			// Fill the remaining space at the end of line:
			for (int x = line.length(); x < width; x++)
				_board.addType(new Position(x, y), Board.TYPE_FREE);

		}

		// Fill the inner space of the level with solid floor tiles:
		// An exception will be thrown if the wall surrounding the player is leaky
		if ((_worker == null) || (!_board.fillType(_worker, Board.TYPE_FREE, Board.TYPE_FLOOR)))
			throw new IllegalFormatException();
		
		// Check for illegal elements / wrong element amount:
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				Position pos = new Position(x, y);
				
				// Check whether an element is outside the wall:
				if (_board.containsType(pos, Board.TYPE_WORKER + Board.TYPE_TARGET + Board.TYPE_CRATE)) {
					
					// Check if an element is outside the wall bounding:
					if (_board.isType(pos, Board.TYPE_FREE))
						throw new IllegalFormatException();

					// Check if another worker exists:
					if (_board.isType(pos, Board.TYPE_WORKER))
						if (!_worker.equals(pos)) throw new IllegalFormatException();
					
					// Count targets:
					if (_board.isType(pos, Board.TYPE_TARGET))
						_targets.add(pos);
					
					// Count crates:
					if (_board.isType(pos, Board.TYPE_CRATE))
						_crates.add(pos);
				}
			}
		}

		if ((_crates.size() < 1) || (_targets.size() != _crates.size()))
			throw new IllegalFormatException();
		
		// Initialize empty move list:
		_iMove = _moves.listIterator();
	}
	
	/**
	 * Looks for a deadlock in the whole level. Only three types of
	 * deadlocks are recognized: Square, corner and wall deadlock.
	 * 
	 * @return true if level is in deadlock
	 * @see isDeadCorner, isDeadSquare, isDeadWall
	 */
	public boolean findDeadlock() {
		
		// Check for deadlocks only occurring around crates:
		for (Position crate : _crates) {
			if (findDeadlock(crate)) return true;
		}
		
		return false;
	}
	
	/**
	 * Looks for a deadlock at the given crate position. Only three types of
	 * deadlocks are recognized: Square, corner and wall deadlock.
	 * 
	 * @return true if level is in deadlock
	 * @see isDeadCorner, isDeadSquare, isDeadWall
	 */
	public boolean findDeadlock(Position crate) {
		
		// Deadlock only if crate is not on a target:
		return (!_board.isType(crate, Board.TYPE_TARGET) &&
			   (isDeadCorner(crate) ||
		     	isDeadSquare(crate) || 
		     	isDeadWall(crate, Position.TOP   ) ||
		     	isDeadWall(crate, Position.RIGHT ) ||
		     	isDeadWall(crate, Position.BOTTOM) ||
		     	isDeadWall(crate, Position.LEFT  )));
	}
	
	/**
	 * Checks whether a tile at given position is in an edge of walls.
	 * 
	 * @param x
	 * @param y
	 * @return true whether the tile is in an edge
	 */
	protected boolean isDeadCorner(Position crate) {
		return (_board.isType(crate.right() , Board.TYPE_WALL) && _board.isType(crate.bottom(), Board.TYPE_WALL)) ||
			   (_board.isType(crate.bottom(), Board.TYPE_WALL) && _board.isType(crate.left()  , Board.TYPE_WALL)) ||
			   (_board.isType(crate.left()  , Board.TYPE_WALL) && _board.isType(crate.top()   , Board.TYPE_WALL)) ||
			   (_board.isType(crate.top()   , Board.TYPE_WALL) && _board.isType(crate.right() , Board.TYPE_WALL));
	}
	
	/**
	 * Checks whether a tile at given position is surrounded by walls / crates
	 * in the lower right corner.
	 * 
	 * @param x
	 * @param y
	 * @return true whether there is a dead square
	 */
	private boolean isDeadSquare(Position crate) {
		return (_board.containsType(crate.bottom()    , Board.TYPE_CRATE + Board.TYPE_WALL) &&
				_board.containsType(crate.left()      , Board.TYPE_CRATE + Board.TYPE_WALL) && 
				_board.containsType(crate.bottomLeft(), Board.TYPE_CRATE + Board.TYPE_WALL)) ||
				
			   (_board.containsType(crate.left()   , Board.TYPE_CRATE + Board.TYPE_WALL) &&
				_board.containsType(crate.topLeft(), Board.TYPE_CRATE + Board.TYPE_WALL) &&
				_board.containsType(crate.top()    , Board.TYPE_CRATE + Board.TYPE_WALL)) ||
				
			   (_board.containsType(crate.top()     , Board.TYPE_CRATE + Board.TYPE_WALL) &&
				_board.containsType(crate.topRight(), Board.TYPE_CRATE + Board.TYPE_WALL) &&
				_board.containsType(crate.right()   , Board.TYPE_CRATE + Board.TYPE_WALL)) ||
				
			   (_board.containsType(crate.right()      , Board.TYPE_CRATE + Board.TYPE_WALL) &&
				_board.containsType(crate.bottomRight(), Board.TYPE_CRATE + Board.TYPE_WALL) &&
				_board.containsType(crate.bottom()     , Board.TYPE_CRATE + Board.TYPE_WALL));
	}
	
	/**
	 * Checks whether a crate is pushed against a wall and can not moved away 
	 * from it towards a goal.
	 * 
	 * @param crateX position of the crate
	 * @param crateY position of the crate
	 * @param wallX position of the possible wall
	 * @param wallY position of the possible wall
	 * @return
	 */
	protected boolean isDeadWall(Position crate, int direction) {

		Position wall = crate.neighbor(direction);
		
		if (!_board.isType(wall, Board.TYPE_WALL)) return false;
		
		if (crate.getY() == wall.getY()) { // Wall is in y direction:
			
			// Do while path in y+ direction is not blocked:
			Position test = crate.bottom();
			while (_board.isType(test, Board.TYPE_FLOOR)) {
				if (_board.isType(test, Board.TYPE_TARGET)) return false;
				if (_board.isType(test.neighbor(direction), Board.TYPE_FLOOR)) return false;
				test.moveDown();
			}

			// Do while path in y- direction is not blocked:
			test = crate.top();
			while (_board.isType(test, Board.TYPE_FLOOR)) {
				if (_board.isType(test, Board.TYPE_TARGET)) return false;
				if (_board.isType(test.neighbor(direction), Board.TYPE_FLOOR)) return false;
				test.moveUp();
			}
			return true;
			
		} else { // Wall is in x direction:
			// Do while path in x direction is not blocked:
			
			Position test = crate.right();
			while (!_board.isType(test, Board.TYPE_WALL)) {
				if (_board.isType(test, Board.TYPE_TARGET)) return false;
				if (!_board.isType(test.neighbor(direction), Board.TYPE_WALL)) return false;
				test.moveRight();
			}
			
			// Do while path in x direction is not blocked:
			test = crate.left();
			while (!_board.isType(test, Board.TYPE_WALL)) {
				if (_board.isType(test, Board.TYPE_TARGET)) return false;
				if (!_board.isType(test.neighbor(direction), Board.TYPE_WALL)) return false;
				test.moveLeft();
			}
			return true;
		}
	}
	
	/**
	 * Searches for the shortest path from the current worker position to the
	 * given target using the a*-algorithm
	 * 
	 * @param x position x
	 * @param y position y
	 * @return list of positions from worker to target
	 */
	public LinkedList<Position> findPath(Position target) {
		
        PriorityQueue<PathFinderNode> openQueue = new PriorityQueue<PathFinderNode>();
        HashMap<Long, PathFinderNode> openMap = new HashMap<Long, PathFinderNode>();
        
        LinkedList<Position> cleanup = new LinkedList<Position>();
        
        PathFinderNode start = new PathFinderNode(new Position(_worker), target);
        
        // Open set contains the initial node:
        openQueue.add(start);
        cleanup.add(start.getPosition());
        
        // The lightest node from the open set:
        PathFinderNode lightest = null;
        
        while (!openQueue.isEmpty()) {
        	
            // Get the open node with the lowest fScore:
            lightest = openQueue.poll();
            
            // Path found:
            if (lightest.getPosition().equals(target)) break;

            // Flag the node as closed / done:
            // Removed, already done in if-clause: cleanup.add(lightest.getPosition());
            _board.addFlag(lightest.getPosition(), FLAG_CLOSED);
            
            // Visit all but closed neighbors:
            for (int i = 0; i < 4; i++) {
            	
            	Position neighbor = lightest.getPosition().neighbor(i);
            	 
            	if (!_board.isFlag(neighbor, FLAG_CLOSED) && !_board.isType(neighbor, Board.TYPE_WALL) && !_board.isType(neighbor, Board.TYPE_CRATE)) {

                    PathFinderNode neighborNode = null;
                    
                    int tentativeG = lightest.getGScore() + 1; // + distance(lightest, x, y);
                    
                    if (!_board.isFlag(neighbor, FLAG_OPEN)) {

                        neighborNode = new PathFinderNode(neighbor, target);
                        neighborNode.setParent(lightest);
                        neighborNode.setGScore(tentativeG);
                        
                        // Add neighbor to open set:
                        openQueue.add(neighborNode);
                        openMap.put(neighbor.toLong(), neighborNode);
                        cleanup.add(neighbor);
                        _board.addFlag(neighbor, FLAG_OPEN);
                    }
                    else {
                    	neighborNode = openMap.get(neighbor.toLong());
                    	
                    	if (tentativeG < neighborNode.getGScore()) { 
                    		neighborNode.setParent(lightest);
                    		neighborNode.setGScore(tentativeG);
                    	}
                    }
                }
            }
        }
        
        // Cleanup:
        for (Position position : cleanup) {
        	_board.removeFlag(position, FLAG_OPEN + FLAG_CLOSED);
        }
        
        if (lightest.getPosition().equals(target))
        	return reconstructPath(lightest);
        
        // No path found:
        return null;
	}

	/**
	 * Retrieves the path to the root node from a given PathFinderNode.
	 * 
	 * @param node
	 * @return list of positions
	 */
	private LinkedList<Position> reconstructPath(PathFinderNode node) {

		LinkedList<Position> path = new LinkedList<Position>();

		while (node.getParent() != null) {
			path.push(node.getPosition());
			node = node.getParent();
		}
		path.push(node.getPosition());

		return path;
	}
	
	/**
	 * Checks whether all crates are placed on targets.
	 * 
	 * @return true if level is solved
	 */
	public boolean isSolved() {
		for (Position crate : _crates) {
			if (!_board.isType(crate, Board.TYPE_TARGET)) return false;
		}
		
		return true;
	}
	
	/**
	 * Checks whether all crates are placed on targets.
	 * 
	 * @return true if level is solved
	 */
	public boolean isSolved(Position crate) {
		return (_board.isType(crate, Board.TYPE_TARGET)) && isSolved();
	}
	
	/**
	 * Return if there is a move to undo.
	 * 
	 * @return true if undo is possible
	 */
	public boolean canUndo() {
		return _iMove.hasPrevious();
	}
	
	/**
	 * Return if there is a move to redo.
	 * 
	 * @return true if redo is possible
	 */
	public boolean canRedo() {
		return _iMove.hasNext();
	}
	
	/**
	 * Undoes the last move. Multiple calls to undo() cause multiple moves to be
	 * undone. 
	 * 
	 * @return true if a move could have been undone
	 */
	public boolean undo() {
		if (!canUndo()) return false;
		
		Move move = _iMove.previous();
		
		// Cheat:
		if ((move.getPath().size() == 2) && (!move.getPath().getFirst().isNeighbor(move.getPath().get(1))))
			undoCheat(move.getPath().get(0));
		
		Iterator<Position> iStep = move.getPath().descendingIterator();
		if (iStep.hasNext()) iStep.next();
		while (iStep.hasNext()) {
			undoStep(iStep.next(), move.isPush());
		}
		
		return true;
	}
	
	/**
	 * Redoes the last move. Multiple calls to redo() cause multiple moves to be
	 * undone. 
	 * 
	 * @return true if a move could have been redone
	 */
	public boolean redo() {
		if (!canRedo()) return false;
		
		Move move = _iMove.next();
		
		Iterator<Position> iStep = move.getPath().iterator();
		
		// Cheat:
		if ((move.getPath().size() == 2)&& (!move.getPath().getFirst().isNeighbor(move.getPath().get(1))))
			undoCheat(move.getPath().get(1));
			
		if (iStep.hasNext()) iStep.next();
		while (iStep.hasNext()) {
			step(iStep.next());
		}
		return true;
	}
	
	/**
	 * Executes the cheat code. The worker position becomes the given position
	 * if it is a legal worker position inside the wall bounding.
	 * 
	 * @param position the new worker position
	 * @return true if the execution was successful
	 */
	public boolean cheat(Position position) {
		if (!_board.inBounds(position) || (_board.containsType(position, Board.TYPE_FREE + Board.TYPE_WALL + Board.TYPE_CRATE))) 
			return false;
		
		// Execute cheat move:
		LinkedList<Position> path = new LinkedList<Position>();
		path.add(_worker);
		path.add(position);
		addMove(new Move(path, false));
		
		_worker = position;
		processEvent(new LevelEvent(EVENT_CHEAT, position, 0));
		return true;
	}
	
	public boolean undoCheat(Position position) {
	
		_worker = position;
		processEvent(new LevelEvent(EVENT_CHEAT, position, 0));
		return true;
	}

	/**
	 * Move the worker to the target using the shortest possible route.
	 * 
	 * @param target target worker position
	 * @return true if the move was executed
	 */
	public boolean move(Position target) {
		
		LinkedList<Position> path = findPath(target);
		
		// No route found:
		if (path == null) return false;
		
		addMove(new Move(path, false));
		
		// Execute move:
		Iterator<Position> iStep = path.iterator();
		if (iStep.hasNext()) iStep.next();
		while (iStep.hasNext()) {
			step(iStep.next());
		}
		
		processEvent(new LevelEvent(EVENT_STEPS, target, 0));
		return true;
	}
	
	/**
	 * Push the crate in front of the worker to the target.
	 * 
	 * @param target target the crate should be pushed to
	 * @return true if the push was executed
	 */
	public boolean push(Position target) {
		
		// Check whether the target is a free tile:
		if (_board.inBounds(target) && _board.containsType(target, Board.TYPE_WALL + Board.TYPE_CRATE))
			return false;
		
		int direction = _worker.getDirection(target);
		
		// Worker and crate are not on a horizontal / vertical line:
		if (direction < 0) return false;
		
		// Check whether a crate is a neighbor of the worker at the specified direction:
		Position crate = _worker.neighbor(direction);
		if (!_board.isType(crate, Board.TYPE_CRATE)) return false;
			
		// Tiles between crate and target have to be free tiles:
		Position position = crate.neighbor(direction);
		while (!position.equals(target)) {
			if (_board.containsType(position, Board.TYPE_WALL + Board.TYPE_CRATE))
				return false;
			
			position = position.neighbor(direction);
		}
		
		// Execute move:
		LinkedList<Position> path = new LinkedList<Position>();
		path.add(_worker);

		position = crate;
		while (!position.equals(target)) {
			path.add(position);
			position = position.neighbor(direction);
		}
		
		addMove(new Move(path, true));
		
		for (Position step: path) {
			if (!step.equals(path.getFirst()))
				step(step);
		}
		
		processEvent(new LevelEvent(EVENT_PUSHES, target, 0));
		return true;
	}
	
	/**
	 * Adds a single move to the move list.
	 * 
	 * @param move move to add to the end of the list
	 */
	private void addMove(Move move) {
		
		// Delete possible redo moves:
		while (_iMove.hasNext()) {
			_iMove.next();
			_iMove.remove();
		}
		
		// Add move to the end of the move list:
		_iMove.add(move);
	}
	
	/**
	 * Execute a single worker step. Crates are moved if possible.
	 * 
	 * @param target neighbor position the worker should jump to
	 * @return true if step is successfully executed
	 */
	private boolean step(Position target) {
		
		assert(_worker.isNeighbor(target));
		assert(!_board.isType(target, Board.TYPE_WALL));
		
		int direction = _worker.getDirection(target);
		
		// A crate is blocking the way:
		if (_board.isType(target, Board.TYPE_CRATE)) {
			
			// Position where the crate is pushed to:
			Position crateTarget = target.neighbor(direction);
			
			// Check whether the crate can be pushed:
			assert(!_board.containsType(crateTarget, Board.TYPE_WALL + Board.TYPE_CRATE));
			
			// Move worker:
			_board.removeType(_worker, Board.TYPE_WORKER);
			_board.addType(target, Board.TYPE_WORKER);
			_worker = target;
			
			// Move crate from workerTarget to crateTarget:
			_board.removeType(target, Board.TYPE_CRATE);
			_board.addType(crateTarget, Board.TYPE_CRATE);
			_crates.remove(target);
			_crates.add(crateTarget);
			
			processEvent(new LevelEvent(EVENT_PUSH, target, direction));
			
			// Check for deadlock:
			if (findDeadlock(crateTarget)) {
				processEvent(new LevelEvent(EVENT_DEADLOCK, crateTarget, direction));
			}
			
			// Check for solution:
			if (isSolved(crateTarget)) {
				processEvent(new LevelEvent(EVENT_SOLVED, crateTarget, direction));
			}
		}
		else {
			
			// Move worker:
			_board.removeType(_worker, Board.TYPE_WORKER);
			_board.addType(target, Board.TYPE_WORKER);
			
			_worker = target;
			processEvent(new LevelEvent(EVENT_STEP, target, direction));
		}
		
		return true;
	}
	
	/**
	 * Undo a previously executed step.
	 * 
	 * @param origin position the worker was placed before the step
	 * @param isPush true if the step included a crate push
	 * @return true if the step could have been undone
	 */
	private boolean undoStep(Position origin, boolean isPush) {
		
		assert(_worker.isNeighbor(origin));
		assert(!_board.isType(origin, Board.TYPE_WALL));
		
		int direction = _worker.getDirection(origin);
		
		// A crate has to be pulled back:
		if (isPush) {
			
			// Position where the pulled crate should be:
			Position crate = _worker.neighbor(Position.reverse(direction));
			
			// Check whether there is a crate that can be pulled:
			assert(_board.isType(crate, Board.TYPE_CRATE));
			
			// Move crate from crate to _worker:
			_board.removeType(crate, Board.TYPE_CRATE);
			_board.addType(_worker, Board.TYPE_CRATE);
			_crates.remove(crate);
			_crates.add(_worker);
			
			// Move worker:
			_board.removeType(_worker, Board.TYPE_WORKER);
			_board.addType(origin, Board.TYPE_WORKER);
			
			_worker = origin;
			
			processEvent(new LevelEvent(EVENT_PULL, origin, direction));
		}
		else
		{
			// Move worker:
			_board.removeType(_worker, Board.TYPE_WORKER);
			_board.addType(origin, Board.TYPE_WORKER);
			
			_worker = origin;
			
			processEvent(new LevelEvent(EVENT_BACK, origin, direction));
		}
		
		return true;
	}
	
	/**
	 * Adds a level listener to the level.
	 * 
	 * @param listener
	 */
	public void addListener(LevelListener listener) {
		_listeners.add(listener);
	}
	
	public void detachListener(LevelListener listener) {
		_listeners.remove(listener);
	}
	
	public void processEvent(LevelEvent event) {
		for (LevelListener listener : _listeners) {
			listener.event(event);
		}
	}
	
	public LinkedList<Move> getMoves() {
		return _moves;
	}
	
	public void rewind() {
		while (canUndo())
			undo();
	}
	
	public void forward() {
		while (canRedo())
			redo();
	}
	
	public boolean isValidMove(Move move) {
		// TODO: External moves should be confirmed valid before executing.
		return true;
	}
	
	public void setMoves(LinkedList<Move> moves) {
		rewind();
		if (moves != null) {
			_moves = moves;
		}
		else {
			_moves = new LinkedList<Move>();
		}
		_iMove = _moves.listIterator();
	}

	public ArrayList<Position> getTargets() {
		return _targets;
	}
	
	public HashSet<Position> getCrates() {
		return _crates;
	}
	
	public UnmodifiableBoard getBoard() {
		return (UnmodifiableBoard) _board;
	}
	
	public Position getWorker() {
		return _worker;
	}
	
	/**
	 * Retrieves the amount of steps currently executed.
	 * 
	 * @return number of steps
	 */
	public int getStepCount() {

		if (!_iMove.hasPrevious()) return 0;
		
		int stepCount = 0;
		Move lastMove = _iMove.previous();
		_iMove.next();
		
		for (Move move : _moves) {
			stepCount += move.getStepCount();
			if (move.equals(lastMove)) break;
		}
		return stepCount;
	}
	
	public int getTotalStepCount() {
		int stepCount = 0;
		
		for (Move move : _moves) {
			stepCount += move.getStepCount();
		}
		return stepCount;
	}

	/**
	 * This tiny method soon will become the mighty solver.
	 * Der Solver funktioniert folgenderma�en:
	 * Zuerst wird der Key der Startposition gebildet.
	 * Dann wird 
	 * 
	 * @return
	 */
	public LinkedList<Position> findSolution() {
		
		HashMap<IntBuffer, LinkedList<Integer>> keys = new HashMap<IntBuffer, LinkedList<Integer>>();
		int maxDepth = 100; // Maximale Suchtiefe:
		
		// 1. Startzustand aufzeichnen:
		// ---------------------------------	
		// Key des Startzustandes bilden:
		IntBuffer rootKey = IntBuffer.allocate(_crates.size());
		int index = 0;
		for (int x = 1; x < _board.getWidth() - 1; x++) {
			for (int y = 1; y < _board.getHeight() - 1; y++) {
				if (_board.isType(new Position(x, y), Board.TYPE_CRATE)) {
					_board.addFlag(new Position(x, y), FLAG_CRATE);
					rootKey.put(index, x + (y << 8));
					index++;
				}
			}
		}
	
		// Crate-Tiles mit Flags versehen:
		for (Position crate : _crates) {
			_board.addFlag(crate, FLAG_CRATE);
		}
		
		// Trage ersten Key ein:
		LinkedList<Position> solution = new LinkedList<Position>();
		System.out.println("Startzustand done");
		
		for (int depth = 2; depth < maxDepth; depth*=1.5) {
			System.out.println(depth);
			if (findSolutionNode(solution, 0, depth, new HashMap<IntBuffer, LinkedList<Integer>>(), rootKey, _worker))
				break;
		}
		
		// Cleanup:
		for (int x = 0; x < _board.getWidth(); x++) {
			for (int y = 0; y < _board.getHeight(); y++) {
				_board.removeFlag(new Position(x, y), FLAG_CRATE);
			}
		}

		return solution;
	}
	
	public boolean findSolutionNode(LinkedList<Position> solution, int depth, int maxDepth, HashMap<IntBuffer, LinkedList<Integer>> keys, IntBuffer key, Position worker) {
		
		// Alle Crates auf Verschiebbarkeit testen:
		for (int i = 0; i < key.capacity(); i++) {

		 	// Pruefe alle Verschiebungen auf L�sung:
        	if (findSolutionPush(solution, depth, maxDepth, keys, key, i, Position.TOP, worker)) return true;
        	if (findSolutionPush(solution, depth, maxDepth, keys, key, i,  Position.LEFT, worker)) return true;
        	if (findSolutionPush(solution, depth, maxDepth, keys, key, i,  Position.BOTTOM, worker)) return true;
        	if (findSolutionPush(solution, depth, maxDepth, keys, key, i,  Position.RIGHT, worker)) return true;
		}
		
		// Es wurde hier und in den Unterb�umen keine L�sung gefunden:
		return false;
	}
	
	// Crate verschiebar:
	public boolean findSolutionPush(LinkedList<Position> solution, int depth, int maxDepth, HashMap<IntBuffer, LinkedList<Integer>> keys, IntBuffer parentKey, int index, int direction, Position worker) {
		
		Position crate = new Position(parentKey.get(index));
		Position crateTarget = crate.neighbor(direction);
		Position workerTarget = crate;
		
		// Kann die Crate �berhaupt verschoben werden?
		if (_board.isType(crateTarget, Board.TYPE_WALL) ||
			_board.isFlag(crateTarget, FLAG_CRATE)) return false;
		
		// Kann der Worker die Crate �berhaupt zum Verschieben erreichen?
		int length = existsPath(worker, workerTarget.neighbor(Position.reverse(direction)));
		if ((length == -1) || (depth + length > maxDepth)) return false;
		
		// Verschiebung der Crate auf Spielfeld eintragen:
		_board.removeFlag(crate, FLAG_CRATE);
		_board.addFlag(crateTarget, FLAG_CRATE);
		 
		// Auf Deadlock testen:
		if (!(!_board.isType(crateTarget, Board.TYPE_TARGET) &&
		   (isDeadCorner(crateTarget) || isDeadSquareA(crateTarget) || isDeadWall(crateTarget, direction)))) {
		
		    // Neuen Key bilden:
			IntBuffer testKey = buildKey(parentKey, index, crateTarget);
			 
			if (addKey(keys, testKey, workerTarget)) {
			
				// Auf L�sung testen:
				if (_board.isType(crateTarget, Board.TYPE_TARGET) && isSolution(testKey)) {
			    	
					solution.addLast(worker);
			    	solution.addLast(workerTarget.neighbor(Position.reverse(direction)));
					System.out.println("  Solution found!");
			    	return true; 
				 }

			     // L�sungspfad erweitern:
				 solution.addLast(worker);
				 solution.addLast(workerTarget.neighbor(Position.reverse(direction)));
				 if (findSolutionNode(solution, depth + length, maxDepth, keys, testKey, workerTarget)) return true;
				 solution.removeLast();
				 solution.removeLast();
		    }
		}
	
		// Verschiebung der Crate auf Spielfeld r�ckg�ngig machen:
		_board.removeFlag(crateTarget, FLAG_CRATE);
		_board.addFlag(crate, FLAG_CRATE);
		
		// Es wurde in diesem Unterbaum keine L�sung gefunden:
		return false;
	}
	
	// Vor add testen, ob Key schon existiert....
	public boolean addKey(HashMap<IntBuffer, LinkedList<Integer>> keys, IntBuffer key, Position worker) {

		LinkedList<Integer> positions = keys.get(key);
		    
		boolean situationFound = false;
		boolean positionIsOld = false;
		    
		// Wenn es diesen Key schonmal gegeben hat:
		if ((positions != null)) {
		    	
		    // Pr�fe, ob der Key auch diese Situation beschreibt, d.h.
		    // ob der Worker die Workerposition des Keys erreichen kann:
		    for (Integer position : positions) {
		    	
		    	situationFound = existsPath(new Position(position), worker) != -1;
		    	if (situationFound) break;
		    }

		    if (!situationFound) return false;
		    
		    // Situation ist eindeutig neu, zu Key hinzuf�gen:
		    positions.add(worker.toInt());
		    keys.put(key, positions);
		}
		else { // Key ist neu, Key hinzuf�gen:
	
			positions = new LinkedList<Integer>();
			positions.add(worker.toInt());
			keys.put(key, positions);
		}
		return true;
	}
	
	public boolean isSolution(IntBuffer key) {
		
		for (int i = 1; i < key.capacity(); i++) {
			int x = key.get(i) & 0xFF;
			int y = key.get(i) >> 8;
				if (!_board.isType(new Position(x, y), Board.TYPE_TARGET)) return false;
		}
		
		return true;
	}
	
	public int[] copyKey(int[] parentKey) {
		
		// ParentKey kopieren:
		return parentKey.clone();
	}
	
	public IntBuffer buildKey(IntBuffer key, int index, Position worker) {
		
		// Kopiere key:
		int[] arrayKey = new int[key.capacity()];
		System.arraycopy(key.array(), 0, arrayKey, 0, arrayKey.length);
		IntBuffer newKey = IntBuffer.wrap(arrayKey);
		
		// Finde Index des n�chstgr��eren Elementes:
		int successor;
		for (successor = 1; successor < newKey.capacity(); successor++) {
			int xcoord = newKey.get(successor) & 0xFF;
			int ycoord = newKey.get(successor) >> 8;
			if (xcoord > worker.getX()) break;
			if ((xcoord == worker.getX()) && (ycoord > worker.getY())) break;
			if ((xcoord == worker.getX()) && (ycoord == worker.getY())) {
				newKey.put(successor, worker.toInt());
				return newKey;
			}
		}
		
		// Neuer Index ist kleiner als alter:
		if (index < successor) {
			
			System.arraycopy(newKey.array(), index + 1, newKey.array(), index, successor - index - 1);
			newKey.put(successor - 1, worker.toInt());
		}
		else if (index > successor) {
			System.arraycopy(newKey.array(), successor, newKey.array(), successor + 1, index - successor);
			newKey.put(successor, worker.toInt());
		}
		else {
			newKey.put(successor, worker.toInt());
		}
		
		return newKey;
	}

	private boolean isDeadSquareA(Position test) {
		int x = test.getX();
		int y = test.getY();
		return ((_board.isType(new Position(x, y+1), Board.TYPE_WALL)||_board.isFlag(new Position(x, y+1), FLAG_CRATE)) && (_board.isType(new Position(x-1, y), Board.TYPE_WALL)||_board.isFlag(new Position(x-1, y), FLAG_CRATE)) && (_board.isType(new Position(x-1, y+1), Board.TYPE_WALL)||_board.isFlag(new Position(x-1, y+1), FLAG_CRATE)))||
			   ((_board.isType(new Position(x-1, y), Board.TYPE_WALL)||_board.isFlag(new Position(x-1, y), FLAG_CRATE))  && (_board.isType(new Position(x-1, y-1), Board.TYPE_WALL)||_board.isFlag(new Position(x-1, y-1), FLAG_CRATE)) && (_board.isType(new Position(x, y-1), Board.TYPE_WALL)||_board.isFlag(new Position(x, y-1), FLAG_CRATE)))||
			   ((_board.isType(new Position(x, y-1), Board.TYPE_WALL)||_board.isFlag(new Position(x, y-1), FLAG_CRATE))  && (_board.isType(new Position(x+1, y-1), Board.TYPE_WALL)||_board.isFlag(new Position(x+1, y-1), FLAG_CRATE)) && (_board.isType(new Position(x+1, y), Board.TYPE_WALL)||_board.isFlag(new Position(x+1, y), FLAG_CRATE)))||
			   ((_board.isType(new Position(x+1, y), Board.TYPE_WALL)||_board.isFlag(new Position(x+1, y), FLAG_CRATE))  && (_board.isType(new Position(x+1, y+1), Board.TYPE_WALL)||_board.isFlag(new Position(x+1, y+1), FLAG_CRATE)) && (_board.isType(new Position(x, y+1), Board.TYPE_WALL)||_board.isFlag(new Position(x, y+1), FLAG_CRATE)));
	}
	
	public int existsPath(Position source, Position target) {

		if (_board.isType(target, Board.TYPE_WALL)) return -1;
		
        PriorityQueue<PathFinderNode> openQueue = new PriorityQueue<PathFinderNode>();
        HashMap<Long, PathFinderNode> openMap = new HashMap<Long, PathFinderNode>();
        
        LinkedList<Position> cleanup = new LinkedList<Position>();
        
        PathFinderNode start = new PathFinderNode(new Position(_worker), target);
        
        // Open set contains the initial node:
        openQueue.add(start);
        cleanup.add(start.getPosition());
        
        // The lightest node from the open set:
        PathFinderNode lightest = null;
        
        while (!openQueue.isEmpty()) {
        	
            // Get the open node with the lowest fScore:
            lightest = openQueue.poll();
            
            // Path found:
            if (lightest.getPosition().equals(target)) break;

            // Flag the node as closed / done:
            // Removed, already done in if-clause: cleanup.add(lightest.getPosition());
            _board.addFlag(lightest.getPosition(), FLAG_CLOSED);
            
            // Visit all but closed neighbors:
            for (int i = 0; i < 4; i++) {
            	
            	Position neighbor = lightest.getPosition().neighbor(i);
            	
            	if (!_board.isFlag(neighbor, FLAG_CLOSED) && !_board.isType(neighbor, Board.TYPE_WALL) && !_board.isFlag(neighbor, FLAG_CRATE)) {

                    PathFinderNode neighborNode = null;
                    
                    int tentativeG = lightest.getGScore() + 1; // + distance(lightest, x, y);
                    
                    if (!_board.isFlag(neighbor, FLAG_OPEN)) {

                        neighborNode = new PathFinderNode(neighbor, target);
                        neighborNode.setParent(lightest);
                        neighborNode.setGScore(tentativeG);
                        
                        // Add neighbor to open set:
                        openQueue.add(neighborNode);
                        openMap.put(neighbor.toLong(), neighborNode);
                        cleanup.add(neighbor);
                        _board.addFlag(neighbor, FLAG_OPEN);
                    }
                    else {
                    	neighborNode = openMap.get(neighbor.toLong());
                    	
                    	if (tentativeG < neighborNode.getGScore()) { 
                    		neighborNode.setParent(lightest);
                    		neighborNode.setGScore(tentativeG);
                    	}
                    }
                }
            }
        }
        
        // Cleanup:
        for (Position position : cleanup) {
        	_board.removeFlag(position, FLAG_OPEN + FLAG_CLOSED);
        }
        
        if (lightest.getPosition().equals(target))
        	return calcPathSize(lightest);
        
        // No path found:
        return -1;
	}
	
	private int calcPathSize(PathFinderNode node) {

		int result = 0;
		
		while (node.getParent() != null) {
			result++;
			node = node.getParent();
		}
		result++;

		return result;
	}
	
	public void play(LinkedList<Position> solution) {

		_board.removeType(this.getWorker(), Board.TYPE_WORKER);
		
		for (Position p : solution) {
			
			int dirX =  p.getX()-getWorker().getX();
			int dirY =  p.getY()-getWorker().getY();
			
			if (_board.isType(new Position(p.getX(), p.getY()), Board.TYPE_CRATE)) {
				_board.removeType(new Position(p.getX(), p.getY()), Board.TYPE_CRATE);
				_board.addType(new Position(p.getX() + dirX, p.getY() + dirY), Board.TYPE_CRATE);
			}
			
			_worker = p;
			
			_board.addType(_worker, Board.TYPE_WORKER);
			
			// Visualisieren:
			System.out.println(_board.toString());
			
			_board.removeType(this.getWorker(), Board.TYPE_WORKER);
			
			try {
				Thread.sleep(800);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		LinkedList<String> strings = new LinkedList<String>();
		
		/*strings.add(" ####### ");
		strings.add(" #  +  # ");
		strings.add("##$#*#$##");
		strings.add("#   .   #");
		strings.add("# $ . $ #");
		strings.add("## #*# ##");
		strings.add(" #  .  # ");
		strings.add(" ####### ");*/
		 
		/*strings.add("#######"); // 16 Moves
		strings.add("#@    #");
		strings.add("#     #");
		strings.add("#  $  #");
		strings.add("#     #");
		strings.add("#    .#");
		strings.add("#######");*/
		
		/*strings.add("##########"); // 28 Moves
		strings.add("#@   $  .#");
		strings.add("#  $. .$ #");
		strings.add("##########");*/
		
		strings.add("####  "); // 33 Moves
		strings.add("# .#  ");
		strings.add("#  ###");
		strings.add("#*@  #");
		strings.add("#  $ #");
		strings.add("#  ###");
		strings.add("####  ");
		
		/*strings.add(" #####    "); // 61 Moves
		strings.add("## @ #### ");
		strings.add("#  #  . ##");
		strings.add("# #      #");
	 	strings.add("# $$ #.  #");
	 	strings.add("##    ####");
		strings.add(" ##   #   ");
		strings.add("  #####   ");*/
		
		/*strings.add(" ############# "); // 60 < Moves < 70
		strings.add("##           # ");
		strings.add("# # $      $# #");
		strings.add("#    #.@.#    #");
		strings.add("## #$ . . $ # #");
		strings.add(" #   # # ##   #");
		strings.add(" #####    #####");
		strings.add("     ######    ");*/
		
		/*strings.add("     #####"); // 84 Moves
		strings.add(" #####   #");
		strings.add("##  +  # #");
	    strings.add("# $$.$$  #");
	    strings.add("#  #.# ###");
	    strings.add("##  .  #  ");
	    strings.add(" #######  ");*/
	    
	    /*strings.add("   ###      "); // 109 < Moves < 150
	    strings.add("  ## # #### ");
	    strings.add(" ##  ###  # ");
	    strings.add("## $      # ");
	    strings.add("#   @$ #  # ");
	    strings.add("### $###  # ");
	    strings.add("  #  #..  # ");
	    strings.add(" ## ##.# ## ");
	    strings.add(" #      ##  ");
	    strings.add(" #     ##   ");
	    strings.add(" #######    ");*/
		
	   /* strings.add("         #####"); // 170 < Moves < 225
	    strings.add("         #   #");
	    strings.add("########## # #");
	    strings.add("#.     #  $  #");
	    strings.add("#.  @  #    ##");
	    strings.add("#.# #######  #");
	    strings.add("#         $$ #");
	    strings.add("##  #####    #");
	    strings.add(" ####   ######");*/
		
		/*strings.add("#######  "); // 789 < Moves < 1228
		strings.add("#.... ###");
		strings.add("#..##$$ #");
		strings.add("#.    $ #");
		strings.add("#   $$$@#");
		strings.add("#####  ##");
		strings.add("  #  $ # ");
		strings.add("  #   ## ");
		strings.add("  #####  ");*/
		
		/*strings.add("     ##### ##################     "); // Moves < 620 
	 	strings.add("    #     #                  #    ");
	 	strings.add("   #  #  #  ################  #   ");
	 	strings.add("  #  #  #  #                #  #  ");
	 	strings.add(" #  #  #  #  ##############  #  # ");
	 	strings.add("#  #  #  #  #              #  #  #");
	 	strings.add("# #  #  #  #  ############  #  # #");
	 	strings.add("# # #  #  #  #            #  #   #");
	 	strings.add("# # # #  #  #  ##########  #  #  #");
	 	strings.add("# # # # #  #  #          #  #  # #");
	 	strings.add("# # # # # #  #  ########  #  #  # ");
	 	strings.add("# # # # # # #  #        #  #  #  #");
	 	strings.add("# # # # # # # #  ######  #  #  # #");
	 	strings.add("# # # # # # # # #      #  #  # # #");
	 	strings.add("# # # # # # # # # ####  #  # # # #");
	 	strings.add("# # # # # # # #       #  # # # # #");
	 	strings.add("# # # # # # #   ## ##  # # # # # #");
	 	strings.add("# # # # # #  ## ##   # # # # # # #");
	 	strings.add("# # # # #  #    @.$# # # # # # # #");
	 	strings.add("# # # #  #  #### # # # # # # # # #");
	 	strings.add("# # #  #  #      # # # # # # # # #");
	 	strings.add("# #  #  #  ######  # # # # # # # #");
	 	strings.add("#  #  #  #        #  # # # # # # #");
	 	strings.add(" #  #  #  ########  #  # # # # # #");
	 	strings.add("# #  #  #          #  #  # # # # #");
	 	strings.add("#  #  #  ##########  #  #  # # # #");
	 	strings.add("#   #  #            #  #  #  # # #");
	 	strings.add("# #  #  ############  #  #  #  # #");
	 	strings.add("#  #  #              #  #  #  #  #");
	 	strings.add(" #  #  ##############  #  #  #  # ");
	 	strings.add("  #  #                #  #  #  #  ");
	 	strings.add("   #  ################  #  #  #   ");
	 	strings.add("    #                  #     #    ");
	 	strings.add("     ################## #####     ");*/

		/*strings.add("     #######     "); // 49 < Moves
		strings.add("######     ######");
	    strings.add("#  . ..$#$.. .  #");
	    strings.add("#  $ $  .  $ $  #");
	    strings.add("###$####@####$###");
	    strings.add("#  $ $  .  $ $  #");
	    strings.add("#  . ..$#$.. .  #");
	    strings.add("######     ######");
	    strings.add("     #######     ");*/
	
		/*strings.add("            ####  "); 49 < Moves
		strings.add("           ##  ###");
		strings.add("          ##  @  #");
		strings.add("         ##  *$ .#");
		strings.add("        ##  **  ##");
		strings.add("       ##  **  ## ");
		strings.add("      ##  **  ##  ");
		strings.add("     ##  **  ##   ");
		strings.add("    ##  **  ##    ");
		strings.add("   ##  **  ##     ");
		strings.add("  ##  **  ##      ");
		strings.add(" ##  **  ##       ");
		strings.add("##  **  ##        ");
		strings.add("#  **  ##         ");
		strings.add("# **  ##          ");
		strings.add("#  ####           ");
		strings.add("####              ");*/
	   
	    /*strings.add("   ######          "); // 33 < Moves
	    strings.add("   #....###########");
	    strings.add(" ###**.*.         #");
	    strings.add(" #  .*.###### $ $ #");
	    strings.add(" # #.*.....#### ###");
	    strings.add(" # # #$##.*.   $  #");
	    strings.add(" # # # $ .*.# #$# #");
	    strings.add(" #  $   ##..#$#   #");
	    strings.add(" # $.##  ###  #$$ #");
	    strings.add(" ## ### $  # ##   #");
	    strings.add(" #      #  $ .### #");
	    strings.add(" ##### #  $# $$ # #");
	    strings.add("     # #   # $  # #");
	    strings.add("     # ##$##  # # #");
	    strings.add("     #      $$$ $ #");
	    strings.add("     #######     @#");
	    strings.add("           ########");*/

		/*strings.add("   ####                       "); // Godlike
		strings.add("  ##  #############           ");
		strings.add(" ##    .......... #           ");
		strings.add("##    #  ####$### ##          ");
		strings.add("#      # #      #  ##         ");
		strings.add("#     #  # $$$  # # #####     ");
		strings.add("#####  # . .# ### . . . ##    ");
		strings.add("    #   $. .#  # $$ $ $ @#    ");
		strings.add("   ####### ### #   #######    ");
		strings.add("   # $   $   # ## ###         ");
		strings.add("   #    $  $ #  # ## # ####   ");
		strings.add("   # $### ####  # # # ##  ####");
		strings.add("   #  $ $$$  # ## #  ##      #");
		strings.add("   ##        # ## #### $$$   #");
		strings.add("    ## ####### .....     #####");
		strings.add("    #  $         ######  #    ");
		strings.add("    #   ######  ##    ####    ");
		strings.add("    #####    ####             ");*/

		Level blob = null;
		try {
			blob = new Level(strings);
			ArrayList<String> result = blob.getBoard().toStringList();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		LinkedList<Position> solution = blob.findSolution();
		System.out.println(solution);
		blob.play(solution);
	}
}
