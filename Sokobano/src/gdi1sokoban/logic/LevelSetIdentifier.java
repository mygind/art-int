package gdi1sokoban.logic;

import gdi1sokoban.graphic.base.TextureManager;

/**
 * Class represents identifier of the level set
 * @author Stalker
 *
 */
public class LevelSetIdentifier extends Identifier{
	
	private TextureManager.Resource _preview;
	
	private String _uri;
	
	/**
	 * Gets preview of the level set
	 * @return
	 */
	public TextureManager.Resource getPreview(){
		return _preview;
	}
	
	/**
	 * Creates new level set identifier by the given parameters
	 * @param name
	 * @param id
	 * @param uri
	 */
	public LevelSetIdentifier(String name, int id, String uri){
		super(name, id);
		//TODO: Texture?
		this._uri =uri;
	}

}
