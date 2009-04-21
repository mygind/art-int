package gdi1sokoban.logic;

/**
 * This class represents highscore results for one player in one game
 * @author Stalker
 *
 */
public class Highscore {
	
	private int _id;
	private int _score;
	private long _time;
	
	/**
	 * Creates highscore Object by given parameters
	 * @param id player id
	 * @param score player score
	 * @param time player time
	 */
	public Highscore(int id, int score, long time) {
	
		this._id = id;
		this._score = score;
		this._time = time;
	}
	
	/**
	 * Gets player id
	 * @return player id
	 */
	public int getId() {
		return _id;
	}
	
	/**
	 * Gets score of the player
	 * @return score of the player
	 */
	public int getScore() {
		return _score;
	}
	
	/**
	 * Gets player time 
	 * @return player time
	 */
	public long getTime() {
		return _time;
	}
		
	
}
