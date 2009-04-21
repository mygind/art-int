package gdi1sokoban.logic;

/**
 * This class is a "record" to store and retrieve some identification data  
 * @author Stalker
 *
 */
public class IdentifierRecord {
	
	private String _name;
	private int _id;
	private String _uri;
	
	/**
	 * Gets name of the record
	 * @return record name
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Gets id of the record
	 * @return record id
	 */
	public int getId() {
		return _id;
	}
	
	/**
	 * Gets uri of the record
	 * @return record uri
	 */
	public String getUri() {
		return _uri;
	}
	
	/**
	 * Creates new record with the given parameters 
	 * @param _name record name
	 * @param _id record id
	 * @param _uri record uri
	 */
	public IdentifierRecord(String _name, int _id, String _uri) {
		super();
		this._name = _name;
		this._id = _id;
		this._uri = _uri;
	}
}
