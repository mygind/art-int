package gdi1sokoban.logic;

import java.util.ArrayList;

/**
 * Represents logically set of levels in socoban
 * @author Stalker
 *
 */
public class LevelSet extends LevelSetIdentifier{
	
	private ArrayList<LevelIdentifier> _levelIdentifiers = null;
	
	
	
	/**
	 * Gets all level identifiers
	 * @return level identifiers
	 */
	public ArrayList<LevelIdentifier> getLevelIdentifiers(){
		return _levelIdentifiers;
	}
	
	/**
	 * Creates levelset by given parameters
	 * @param name name of the level set
	 * @param id levelset id
	 * @param uri levelset uri
	 * @param levelIdentifiers list of levelidentifiers
	 */
	public LevelSet(String name, int id, String uri, ArrayList<LevelIdentifier> levelIdentifiers){
		super(name, id, uri);
		this._levelIdentifiers = levelIdentifiers;
	}
	
	/**
	 * Creates levelset by given parameters
	 * @param identRecord identifier of levelset
	 * @param levelIdentifiers list of levelidentifiers
	 */
	public LevelSet(IdentifierRecord identRecord, ArrayList<LevelIdentifier> levelIdentifiers){
		super(identRecord.getName(), identRecord.getId(), identRecord.getUri());
		this._levelIdentifiers = levelIdentifiers;
	}
	
	
	
	

}
