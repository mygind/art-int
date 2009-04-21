package gdi1sokoban.logic;

/**
 * This class represents level statistic for player
 * @author Stalker
 *
 */
public class LevelStatistic {
	
	private int _moves;
	
	private long _time;
	
	private boolean _saved;
	
	private boolean _won;
	
	@Deprecated
	private int _levelId;
	
	/**
	 * Get players moves
	 * @return moves
	 */
	public int getMoves(){
		return _moves;
	}
	
	public void setWon(boolean w){
		_won = w;
	}
	
	/**
	 * Get players play time
	 * @return time
	 */
	public long getTime(){
		return _time;
	}
	
	/**
	 * Gets true if the lavel was saved
	 * @return is saved
	 */
	public boolean isSaved(){
		return _saved;
	}
	
	public void setSaved(boolean w){
		_saved = w;
	}
	
	/**
	 * Gets level id
	 * @return level id
	 */
	@Deprecated
	public int getLevelId(){
		return _levelId; 
	}
	
	/**
	 * Creates new level statistic for player by the given parameters
	 * @param moves
	 * @param time
	 * @param saved
	 * @param levelId
	 */
	@Deprecated
	public LevelStatistic(int moves, long time, boolean saved, int levelId){
		this._moves = moves;
		this._time = time;
		this._saved = saved;
		this._levelId = levelId;
	}
	
	/**
	 * Creates new level statistic for player by the given parameters
	 * @param moves
	 * @param time
	 * @param saved
	 */
	public LevelStatistic(int moves, long time, boolean saved, boolean won){
		this._moves = moves;
		this._time = time;
		this._saved = saved;
		this._won = won;
		//this._levelId = levelId;
	}
	
	/**
	 * IsWon
	 * @return isWon
	 */
	public boolean isWon(){
		return _won;
	}
}
