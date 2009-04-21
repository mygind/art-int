package gdi1sokoban.logic;

import gdi1sokoban.graphic.base.TextureManager;

/**
 * Class represents identifier of the skin
 * @author Stalker
 *
 */
public class SkinIdentifier extends Identifier{
	
	private TextureManager.Resource _preview;
	private String _uri;
	
	/**
	 * Gets skin preview
	 * @return skin preview
	 */
	public TextureManager.Resource getPriview(){
		return _preview;
	}
	
	/**
	 * Creates new skin identifier by the given parameters
	 * @param name skin name
	 * @param id skin id
	 * @param uri skin uri
	 */
	public SkinIdentifier(String name, int id, String uri){
		super(name,id);
		this._uri = uri;
		
	}
	
	@Deprecated
	public SkinIdentifier(SkinIdentifier ident){
		super(ident.getName(), ident.getId());
		this._preview = ident.getPriview();
	}
	

}
