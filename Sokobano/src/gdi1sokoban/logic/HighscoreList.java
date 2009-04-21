package gdi1sokoban.logic;

import java.util.ArrayList;

/**
 * This class represents List of highscores for given level 
 * @author Stalker
 * 
 */
public class HighscoreList {
	
	private IdentifierRecord _levelIdentifier;
	
	private ArrayList<Highscore> _scores = null;
	
	/**
	 * Create HighscoreList object with given parameters
	 * @param levelIdentifier identifier of the level
	 * @param scoreList list of highscore for this level
	 */
	public HighscoreList(IdentifierRecord levelIdentifier,
			ArrayList<Highscore> scoreList) {
		this._levelIdentifier = levelIdentifier;
		this._scores = scoreList;
	}
		
	/**
	 * Gets identifier of the level
	 * @return identifier of the level
	 */
	public IdentifierRecord getLevelIdentifier() {
		return _levelIdentifier;
	}
	
	/**
	 * Gets highscore list 
	 * @return highscore list
	 */
	public ArrayList<Highscore> getHighscores() {
		return _scores;
	}

}
