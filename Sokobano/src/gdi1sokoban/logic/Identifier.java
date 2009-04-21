package gdi1sokoban.logic;

/**
 * This class represents identifier of some Object
 * @author Stalker
 *
 */
public class Identifier {
	
	private String _name;
	private int _id;
	
	/**
	 * Gets name of the identifier
	 * @return
	 */
	public String getName(){
		return _name;
	}
	
	public void setName(String name){
		this._name = name;
	}
	
	/**
	 * Gets id of the identifier
	 * @return
	 */
	public int getId(){
		return _id;
	}
		
	/**
	 * Creates identifier Object by the given parameters
	 * @param name identifier name
	 * @param id identifier id
	 */
	public Identifier(String name, int id){
		this._id = id;
		this._name = name;
	}

}
