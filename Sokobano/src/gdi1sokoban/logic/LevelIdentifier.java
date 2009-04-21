package gdi1sokoban.logic;

import gdi1sokoban.graphic.base.TextureManager;

/**
 * Class represents identifier of the level
 * @author Stalker
 *
 */
public class LevelIdentifier extends Identifier{
	
	private TextureManager.Resource _preview;
	
	/**
	 * Gets preview of the Level 
	 * @return
	 */
	public TextureManager.Resource getPreview(){
		return _preview;
	}
	
	/**
	 * Craetes new LevelIdentifier object by the given parameters
	 * @param name level name
	 * @param id level id
	 * @param uri level uri
	 */
	public LevelIdentifier(String name, int id, String uri){
		super(name, id);
		//_preview = TextureManager.getInstance().getInstance( new TextureDescriptor(uri) );
		//TODO:??
	}
	
	/**
	 * Creates new LevelIdentifier Object by the given levelidentifier
	 * @param ident
	 */
	@Deprecated
	public LevelIdentifier(LevelIdentifier ident){
		super(ident.getName(), ident.getId());
		_preview = ident.getPreview();
	}
	

}
