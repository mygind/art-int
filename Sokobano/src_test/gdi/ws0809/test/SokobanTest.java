package gdi.ws0809.test;

import java.io.File;

/**
 * All public unit tests access your implementation using methods of this interface exclusively.
 * You have to provide an implementation of SokobanTestAdapter to map the methods of this interface 
 * to you implementation.
 * 
 * @version 1.1
 * @author daniel, Steven Arzt
 *
 */
public interface SokobanTest {

	/**
	 * Load a level and parse it into the internal representation.
	 * The loaded level is now the  and should
	 * e.g. be shown in the GUI.
	 * 
	 * @param lvl The file from which to load the level
	 * @throws Exception in case the level is not syntactically correct
	 */
	void loadLevel(File lvl) throws Exception;
	
	/**
	 * Returns the String representation of the <i>current</i> level.
	 * 
	 * @return the String representation
	 */
	String currentLevelToString();
	
	/**
	 * Returns whether the <i>current</i> level is solved. 
	 * 
	 * @return True if the current level has been solved, otherwise false
	 */
	boolean isSolved();

	/**
	 * Gets the current level's width
	 *
	 * @return The width of the current level
	 */
	int getLevelWidth();

	/**
	 * Gets the current level's height
	 * 
	 * @return The height of the current level
	 */
	int getLevelHeight();

	/**
	 * Gets the total number of walls in the current levels
	 * 
	 * @return The number of walls in the current level
	 */
	int getWallCount();

	/**
	 * Gets the total number of crates in the current levels
	 * 
	 * @return The number of crates in the current level
	 */
	int getCrateCount();

	/**
	 * Gets the total number of goals in the current levels
	 * 
	 * @return The number of goals in the current level
	 */
	int getGoalCount();
	
	/**
	 * Move the worker according to the rules. The direction to move is either
	 * right, left, up or down, encoded as 'R','L','U' or 'D'. 
	 *  
	 * @param direction The encoded direction to move the worker
	 */
	void moveWorker(char direction);

	/**
	 * Load a set of levels from a directory. The levels should be 
	 * sorted alphabetically. It should not load the first level yet.
	 * Loading the first level should only happen after a call to
	 * startNextLevel(). 
	 *
	 * @param levelDir the directory from which to load the levels
	 */
	void setLevelDir(File levelDir);


	/**
	 * The next level in a set of levels loaded with loadLevelDir
	 * becomes the <i>current</i> level. If there is no more level
	 * left, an exception may be thrown.
	 */
	void startNextLevel() throws Exception;

	/**
	 * Return the steps performed in the <i>current</i> level, since
	 * the last restart of the level. Remember that only the legal moves
     * count.
	 * 
	 * @return performed steps.
	 */
	int getStepsInCurrentLevel();

	/**
	 * Tells the Sokoban implementation to write the current 
	 * highscores to disk, as detailed in the documentation.
	 */
	void writeHighScoreFile();

	/**
	 * Set the player name used for highscores.
	 * 
	 * @param name The player name.
	 */
	void setPlayerName(String name);

	/**
	 * Gets the X coordinate of the worker's current position
	 * 
	 * @return The worker's position's current X coordinate
	 */
	int getWorkerPositionY();

	/**
	 * Gets the Y coordinate of the worker's current position
	 * 
	 * @return The worker's position's current Y coordinate
	 */
	int getWorkerPositionX();
	
	/**
	 * Gets whether there is a crate at the position identified by the specified
	 * X and Y coordinate
	 * 
	 * @param i The X coordinate of the grid position to check
	 * @param j The Y coordinate of the grid position to check
	 * @return True if there is a crate at the specified position, otherwise
	 * false
	 */
	boolean isCrateAt(int i, int j);

	/**
	 * Gets whether there is a wall at the position identified by the specified
	 * X and Y coordinate
	 * 
	 * @param i The X coordinate of the grid position to check
	 * @param j The Y coordinate of the grid position to check
	 * @return True if there is a wall at the specified position, otherwise
	 * false
	 */
	boolean isWallAt(int i, int j);

	/**
	 * Gets whether there is a goal at the position identified by the specified
	 * X and Y coordinate
	 * 
	 * @param i The X coordinate of the grid position to check
	 * @param j The Y coordinate of the grid position to check
	 * @return True if there is a goal at the specified position, otherwise
	 * false
	 */
	boolean isGoalAt(int i, int j);

	/**
	 * Checks whether the crate at position (x, y) can be moved in direction c
	 * 
	 * @param i The x coordinate of the crate to check
	 * @param j The y coordinate of the crate to check
	 * @param c The direction in which the crate move shall be tested
	 * @return True if the crate at the specified position can be moved in the
	 * given direction, otherwise false 
	 */
	boolean canMoveCrate(int i, int j, char c);

	/**
	 * Gets the name of the best player (the one with with the least moves)
	 * over all levels. If Player A has completed level L1 with 10 moves and
	 * level L2 with 15 moves and player B has completed level L2 with 11 moves,
	 * the correct return value would be "A" as he has achieved to complete a
	 * level with only 10 moves.
	 * If the highscore list is empty, an empty string shall be returned.
	 * 
	 * @return The name of the player with the least moves
	 */
	String getBestPlayerName();

	/**
	 * Clears the highscore list. You can either remove all entries from the
	 * "highscore.txt" file or remove the whole file. If there is no highscore
	 * file at the moment, nothing should happen.
	 */
	void clearHighscoreList ();

	/**
	 * Creates a new entry in the highscore list if the given data describes a
	 * new highscore entry. If the specified entry already exists or has too
	 * many steps to be a highscore, nothing happens and false is returned.
	 * 
	 * @param playername The name of the player that has achieved the score
	 * @param i A unique number identifying the level in which the score has
	 * been reached
	 * @param j The count of moves the player has needed to complete the
	 * specified level
	 * @param k The time the player has needed to solve the level in seconds
	 * @return True if a new entry in the highscore list has been created,
	 * otherwise false
	 */
	boolean createHighscoreEntry (String playername, int i, int j, int k);

	/**
	 * Gets the count of entries in the highscore list
	 * 
	 * @return The count of entries in the highscore list
	 */
	int getHighscoreCount();
	
	/*===================================================================*/
	
	/**
	 * Undo the last move. If no move can be undone (e.g. no move has been performed) an Exception is thrown.
	 * 
	 * @throws Exception notifying the client that no move could be undone 
	 */
	void undoLastMove() throws Exception;

	/**
	 * Redo the last undone move. If no move can be redone (e.g. no move has been undone) an Exception is thrown.
	 * 
	 * @throws Exception notifying the client that no move could be redone 
	 */
	void redoLastUndoneMove() throws Exception;

	/**
	 * Save the current game state to the File.
	 * 
	 * @param f the file to save the game state in. 
	 */
	void saveGame(File f);

	/**
	 * Save the current game state to the File.
	 * 
	 * @param f the file to save the game state in. 
	 */
	void loadGame(File f);

	/*===================================================================*/

	/**
	 * Returns whether the current level is in a deadlock position.
	 * 
	 * @return returns whether the current level is in a deadlock position. 
	 */
	boolean isDeadlock();

	/**
	 * Moves the worker with the steps specified in the move sequence string
	 * which needs to be in the RLUD format.
	 * 
	 * @param Sequence The move sequence in the RLUD format
	 */
	void moveWorkerSequence(String Sequence);
	
	/**
	 * Checks whether all objects in the current level are located in
	 * a continuous wall boundary.
	 * @return True if the level is validly surrounded by walls, otherwise
	 * false
	 */
	boolean checkWallBounding ();

	/*===================================================================*/

	/**
	 * Computes a solution to the current level. The level itself remains unchanged.
	 * 
	 * @parm maxTime Maximal search time in milliseconds. The method should return null if no solution could be computed within this time frame. 
	 * @return A solution to the level in URLD notation or null, if no solution has been found 
	 */
	String solveLevel(int maxTime);

}
