package gdi1sokoban.logic;

import gdi1sokoban.exceptions.IllegalFormatException;
import gdi1sokoban.graphic.Skin;
import gdi1sokoban.graphic.SkinLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;


/**
 * This class represents some data manipulations with Skin Object
 * @author Stalker
 *
 */
public class SkinManager extends IdentifierManager{
	
	//singleton
	private static SkinManager _instance;
	//list of skin identifiers
	private ArrayList<SkinIdentifier> _skinIdentifiers = null;
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<SkinIdentifier> getSkinIdentifiers(){
		return _skinIdentifiers;
	}
	
	/**
	 * singletone contructor
	 */
	private SkinManager(){
		super("res"+File.separator+"skins"+File.separator+"skins.xml");
		_skinIdentifiers = new ArrayList<SkinIdentifier>();
		for(IdentifierRecord i : _identifierRecords){
			_skinIdentifiers.add(new SkinIdentifier(i.getName(), i.getId(), i.getUri()));
		}
	}
	
	/**
	 * Singletone method
	 * @return
	 */
	public static SkinManager getInstance(){
		if(_instance == null)
			_instance = new SkinManager();
		return _instance;
	}
	
	protected void createIdentifierFile(){
		
	}
	
	protected void deleteIdentifierFile(IdentifierRecord record){
		
	}
	
	/**
	 * Gets Skin by the given skin id	
	 * @param id ksin id
	 * @return new Skin
	 * @throws IllegalFormatException
	 */
	public Skin getSkin(int id) throws IllegalFormatException {
		for(SkinIdentifier i: _skinIdentifiers){
			if(i.getId()==id)
				return this.getSkin(i);
		}
		return null;
		
	}
	
	/**
	 * Gets skin by the given Skin identifier
	 * @param ident skin identifier
	 * @return new Skin
	 * @throws IllegalFormatException
	 */
	public Skin getSkin(SkinIdentifier ident) throws IllegalFormatException{
		Skin out = null;
		try{
			out = SkinLoader.load(ident);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		
		return out; 
	}
	
	/**
	 * Gets skin identifier by the given id
	 * @param id
	 * @return
	 */
	public SkinIdentifier getSkinIdentifier(int id) {
		for (SkinIdentifier skinIdentifier : _skinIdentifiers) 
			if (skinIdentifier.getId() == id) return skinIdentifier;
		return null;
	}
	

}
