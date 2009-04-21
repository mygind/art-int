package gdi1sokoban.logic;

import java.io.FileNotFoundException;

import gdi1sokoban.exceptions.IllegalFormatException;
import gdi1sokoban.graphic.Animation;
import gdi1sokoban.graphic.AnimationLoader;

/**
 * Class represents identifier of the player
 * @author Stalker
 *
 */
public class PlayerIdentifier extends Identifier{
	
	//TODO: set default uri
	protected final static String DEFAULT_URI="res/mesh/worker01.mdl";
	
	private Animation _worker;
	
	private String _uri;
	
	/**
	 * Gets worker animation
	 * @return
	 */
	public Animation getWorker() {
		return _worker;
	}
	
	/**
	 * Gets uri
	 * @return uri
	 */
	public String getUri(){
		return this._uri;
	}
	
	/**
	 * Sets uri
	 * @param uri uri to set
	 */
	public void setUri(String uri){
		this._uri = uri;
		//TODO: create model from uri
		try {
			_worker = AnimationLoader.load(uri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Creates new player identifier by the given parameters
	 * @param name player name
	 * @param id player id
	 * @param uri player uri
	 */
	public PlayerIdentifier(String name, int id, String uri) {
		super(name, id);
		this._uri = uri;
		//TODO: create model from uri
		try {
			_worker = AnimationLoader.load(uri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Creates new player identifier by the given parameters
	 * @param name player name
	 * @param id player id
	 */
	public PlayerIdentifier(String name, int id) {
		super(name, id);
		this._uri = DEFAULT_URI;
		//TODO: create model from uri
		try {
			_worker = AnimationLoader.load(_uri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Deprecated
	public PlayerIdentifier(PlayerIdentifier ident){
		super(ident.getName(), ident.getId());
		this._uri = ident.getUri();
		this._worker = ident.getWorker();

	}
}
