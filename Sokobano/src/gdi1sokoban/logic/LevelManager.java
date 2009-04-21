package gdi1sokoban.logic;

import gdi1sokoban.exceptions.DuplicateException;
import gdi1sokoban.exceptions.IllegalFormatException;
import gdi1sokoban.exceptions.LevelFormatException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Class provides management functions over Levels
 * @author Stalker
 *
 */
public class LevelManager extends IdentifierManager{
	
	//level se id
	private int _levelSetId;
	//lis of level set identifiers
	private ArrayList<LevelIdentifier> _levelIdentifiers =null;
	
	/**
	 * Gets all level identifiers for the given level set
	 * @return
	 */
	public ArrayList<LevelIdentifier> getLevelIdentifiers() {
		return _levelIdentifiers;
	}
	
	/**
	 * Creates Level object by the given level set identifier
	 * @param lvlSet level set identifeir
	 */
	public LevelManager(LevelSetIdentifier lvlSet){
		super("res"+File.separator+"levelSet"+File.separator+lvlSet.getId()+File.separator+"levelIdentifiers.xml");
		this._levelSetId = lvlSet.getId();
		_levelIdentifiers = new ArrayList<LevelIdentifier>();
		for(IdentifierRecord i : _identifierRecords){
			_levelIdentifiers.add(new LevelIdentifier(i.getName(), i.getId(), i.getUri()));
		}
	}
	
	/**
	 * Creates Level Object by the given level set id
	 * @param levelSetId
	 */
	public LevelManager(int levelSetId){
		super("res"+File.separator+"levelSet"+File.separator+levelSetId+File.separator+"levelIdentifiers.xml");
		this._levelSetId = levelSetId;
		_levelIdentifiers = new ArrayList<LevelIdentifier>();
		for(IdentifierRecord i : _identifierRecords){
			_levelIdentifiers.add(new LevelIdentifier(i.getName(), i.getId(), i.getUri()));
		}
	}
	
	/**
	 * Gets level by the given level identifier
	 * @param ident level identifier
	 * @return new Level
	 * @throws IOException
	 * @throws IllegalFormatException
	 * @throws LevelFormatException
	 */
	public Level getLevel(LevelIdentifier ident) 
						throws IOException, IllegalFormatException, LevelFormatException{
		String lvlId;
		if(ident.getId()<10){
			lvlId="0"+ident.getId();
		}else{
			lvlId = Integer.toString(ident.getId());
		}
		String path ="res"+File.separator+"levelSet"+File.separator+_levelSetId+File.separator+"Level_"+lvlId+".txt";
		return new Level(path, ident);
	}
	
	
	protected void createIdentifierFile(){
		//TODO: 2 vers.	
	}
	
	protected void deleteIdentifierFile(IdentifierRecord delRecord){
		//TODO:Del picture ??
	}
	
	public int addLevel(String name, String uri){
		//TODO: 2 vers.
		return -1;
	}

	protected void delLevel(int id){
		delIdentifierRecord(id);
		for(LevelIdentifier i :_levelIdentifiers){
			if(i.getId() == id){
				_levelIdentifiers.remove(i);
				break;
			}
		}
		delHighscores(id);
		delPlayerFiles(id);
	}
	
	private void delPlayerFiles(int id){
		PlayerManager _pm = PlayerManager.getInstance();
		ArrayList<PlayerIdentifier> ident = new ArrayList<PlayerIdentifier>();
		ident = _pm.getPlayerIdentifiers();
		for(PlayerIdentifier i: ident){
			String path = "res"+File.separator+"player"+File.separator+i.getId()+File.separator+_levelSetId+File.separator;
			File toDel = new File(path+id+".soc");
			if(toDel.exists()){
				toDel.delete();
				try{
					Player plr = _pm.getPlayer(i.getId());
					//if((plr.getCurrentLevelSetId()==_levelSetId)&&(plr.getCurrentLevelId()==id))
						//_pm.addLastPlayedLevel(null, 0, plr.getId(), 0);
				}catch(IllegalFormatException e){
					e.printStackTrace();
				}
				
				
				try{
					DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
					Document doc = docBuilder.parse(new File(path+"levelSetInfo.xml"));
					NodeList levelList = doc.getElementsByTagName("level");
					for (int j = 0; j < levelList.getLength(); j++) {
						Node tmp = levelList.item(j);
						NamedNodeMap attr=tmp.getAttributes();
						int currLevelId =Integer.parseInt(attr.getNamedItem("id").getTextContent());
						if(currLevelId == id){
							Node first =doc.getFirstChild();
							first.removeChild(tmp);
							break;
						}
					}
					OutputFormat format = new OutputFormat(doc);
					FileOutputStream outputFile =new FileOutputStream(path+"levelSetInfo.xml");
					XMLSerializer ser = new XMLSerializer(outputFile,format);
					ser.serialize(doc.getDocumentElement());
					outputFile.close();
				}catch(ParserConfigurationException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}catch(SAXException e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private void delHighscores(int id){
		try{
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.parse(new File("res"+File.separator+"levelSet"+File.separator+_levelSetId+File.separator+"highScores.xml"));
			NodeList levelList = doc.getElementsByTagName("level");
			for (int i = 0; i < levelList.getLength(); i++) {
				Node tmp = levelList.item(i);
				NamedNodeMap attr=tmp.getAttributes();
				int currLevelId =Integer.parseInt(attr.getNamedItem("id").getTextContent());
				if(currLevelId == id){
					Node first =doc.getFirstChild();
					first.removeChild(tmp);
					break;
				}
			}
			OutputFormat format = new OutputFormat(doc);
			FileOutputStream outputFile =new FileOutputStream("res"+File.separator+"levelSet"+File.separator+_levelSetId+File.separator+"highScores.xml");
			XMLSerializer ser = new XMLSerializer(outputFile,format);
			ser.serialize(doc.getDocumentElement());
			outputFile.close();
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}
	}
	
	protected int addLevel(String path) throws DuplicateException{
		//TODO: default uri
		addIdentifierRecord(path, "");
		for(int i=0; i<_levelIdentifiers.size();i++){
			if((path.compareTo(_levelIdentifiers.get(i).getName()))<=0){
				_levelIdentifiers.add(i, new LevelIdentifier(path, this._lastId, ""));
				break;
			}
		}
		return this._lastId;
	}
}
