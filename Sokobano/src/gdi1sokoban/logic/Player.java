package gdi1sokoban.logic;


import gdi1sokoban.exceptions.IllegalFormatException;
import gdi1sokoban.graphic.Animation;
import gdi1sokoban.graphic.Skin;

/**
 * This is you // look at you, for it is true // no one else to sue. yea
 */
public class Player extends PlayerIdentifier {
	
	private Skin _skin;
	private int _currentLevelSetId;
	private int _currentLevelId;
	private int _currentLevelScore;
	private long _currentLevelTime;
	private boolean _curretLevelIsSaved;
	
	
	public int getCurrentLevelId() {
		return _currentLevelId;
	}
	
	public void setCurrentLevelId(int id) {
		_currentLevelId = id;
	}
	
	public void setCurrentLevelSetId(int id) {
		_currentLevelSetId = id;
	}

	public int getCurrentLevelSetId() {
		return _currentLevelSetId;
	}
	
	public int getCurrentLevelScore(){
		return _currentLevelScore;
	}
	
	public long getCurrentLevelTime(){
		return _currentLevelTime;
	}
	
	public boolean getCurrentLevelIsSaved(){
		return _curretLevelIsSaved;
	}
	
	public Skin getSkin() {
		return _skin;
	}
	
	public void setSkin(Skin skin) {
		_skin = skin;
	}
	
	public Player(PlayerIdentifier ident, Skin skin, int currentLevelSetId, int currentLevelId) throws IllegalFormatException{
		super(ident);
		_currentLevelSetId = currentLevelSetId;
		_currentLevelId = currentLevelId;
		_skin = skin;
		_currentLevelScore = 0;
		_currentLevelTime = 0;
		_curretLevelIsSaved = false;
	}
		
	

	
	
	
}
