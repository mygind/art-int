package gdi1sokoban.logic;

import gdi1sokoban.exceptions.DuplicateException;
import gdi1sokoban.exceptions.IllegalFormatException;
import gdi1sokoban.exceptions.LevelFormatException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;




/**
 * Class provides management functions over Level Sets 
 * @author Stalker
 *
 */
public class LevelSetManager extends IdentifierManager{
	
	//path
	private String _lvlSetPath = "res"+File.separator+"levelSet"+File.separator; 
	
	//singleton instance
	private static LevelSetManager _instance = null;
	
	//list of level set identifiers
	private ArrayList<LevelSetIdentifier> _levelSetIdentifiers = null;
	
	
	/**
	 * Method gets list of IdentifierRecord of the level set
	 * @return list of identifierRecord
	 */
	public ArrayList<LevelSetIdentifier> getLevelSetIdentifiers() {
		return _levelSetIdentifiers;
	}
	
	/**
	 * Delete levelSet identifier
	 * @param id levelSet id
	 */
	public void delLevelSetIdentifier(int id){
		delIdentifierRecord(id);
		for(LevelSetIdentifier i :_levelSetIdentifiers){
			if(i.getId() == id){
				_levelSetIdentifiers.remove(i);
				break;
			}
		}
	}
	
	/**
	 * Add new levelSet Identifier by the given parameters
	 * @param name levelSet name
	 * @param uri levelSet uri
	 * @return level set id
	 * @throws DuplicateException
	 */
	public int addLevelSetIdentifier(String name, String uri)throws DuplicateException{
		addIdentifierRecord(name, uri);
		for(int i=0; i<_levelSetIdentifiers.size();i++){
			if((name.compareTo(_levelSetIdentifiers.get(i).getName()))<=0){
				_levelSetIdentifiers.add(i, new LevelSetIdentifier(name, this._lastId, uri));
				break;
			}
		}
		return this._lastId;
	}
	
	/**
	 * Gets levelSet object by the given id
	 * @param id levelSet id
	 * @return
	 */
	public LevelSet getLevelSet(int id){
		for(IdentifierRecord i : _identifierRecords){
			if(i.getId() == id){
				return new LevelSet(i, getLevelIdentifiers(id));
			}
		}
		return null;
	}
	
	/**
	 * singleton constructor
	 */
	private LevelSetManager(){
		super("res"+File.separator+"levelSet"+File.separator+"levelSets.xml");
		_levelSetIdentifiers = new ArrayList<LevelSetIdentifier>();
		for(IdentifierRecord i : _identifierRecords){
			_levelSetIdentifiers.add(new LevelSetIdentifier(i.getName(), i.getId(), i.getUri()));
		}
	}
	
	/**
	 * singleton instance
	 * @return LevelSetManager
	 */
	public static LevelSetManager getInstance(){
		if(_instance == null){
			_instance = new LevelSetManager();
		}
		return _instance;
	}
	
	/**
	 * Creates level set directory and two xml files: highScores.xml and levelIdentifiers.xml
	 */
	protected void createIdentifierFile(){
		//TODO:In 2 ver smart add picture(takes given picture and copies it into res direcotry)
		String dirPass = this._pathToIdentifierFile.substring(0, this._pathToIdentifierFile.indexOf("levelSets.xml"));
		File f = new File(dirPass+this._lastId);
		f.mkdir();
		File highScore = new File(dirPass+this._lastId+File.separator+"highScores.xml");
		File lvlIdent = new File(dirPass+this._lastId+File.separator+"levelIdentifiers.xml");
		try{
			highScore.createNewFile();
			FileWriter writer = new FileWriter(highScore);
			String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><levelSetHighScore></levelSetHighScore>";
			writer.write(str);
			writer.close();
			
			lvlIdent.createNewFile();
			FileWriter lvlWriter = new FileWriter(lvlIdent);
			String lvl = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><levels></levels>";
			lvlWriter.write(lvl);
			lvlWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		
		
	}
	
	/**
	 * Deletes level set directory with all files within
	 */
	protected void deleteIdentifierFile(IdentifierRecord delRecord){
		//TODO:Del picture ??
		String dirPass = this._pathToIdentifierFile.substring(0, this._pathToIdentifierFile.indexOf("levelSets.xml"));
		File f = new File(dirPass+delRecord.getId());
		deleteDirectory(f);
		File uri = new File(delRecord.getUri());
		uri.delete();
	}
	
	/**
	 * Add new highscore result
	 * @param levelSetId level set id
	 * @param levelId level id
	 * @param score highscore of the player
	 * @return operation success
	 */
	public boolean addHighScore(int levelSetId, int levelId, Highscore score){
		return addHighScore(score.getId(), levelSetId, levelId, score.getScore(), score.getTime());
	}
	
	/**
	 * Add new highscore result	
	 * @param playerId player id
	 * @param levelSetId levelSet id
	 * @param levelId level id
	 * @param score player score
	 * @param time player time
	 * @return operation success
	 */
	public boolean addHighScore(int playerId, int levelSetId, int levelId, int score, long time){
		boolean levelFound = false;
		boolean isAdded = false;
		
		try {
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(_lvlSetPath+levelSetId+File.separator+"highScores.xml"));
			
			Node first = doc.getFirstChild();
			NodeList levelNodes = doc.getElementsByTagName("level");
			
			Element newStatisticElement = doc.createElement("PlayerStatistic");
			newStatisticElement.setAttribute("id", Integer.toString(playerId));
			newStatisticElement.setAttribute("score", Integer.toString(score));
			newStatisticElement.setAttribute("time", Long.toString(time));
						
			// Iterate over all level:
			for (int i = 0; i < levelNodes.getLength(); i++) {
				
				// Get the current level:
				Node levelNode = levelNodes.item(i);
				NamedNodeMap levelAttributes = levelNode.getAttributes();
				
				// Check whether this is the level we want to add new scores:
				if (levelId == Integer.parseInt(levelAttributes.getNamedItem("id").getTextContent())) {
					
					levelFound = true;
					
					// Get the current level statistics:
					Element levelElement = (Element) levelNode;
					NodeList statisticNodes = levelElement.getElementsByTagName("PlayerStatistic");

					// Iterate over the statistics:
					for (int j = 0; j < statisticNodes.getLength(); j++) {
						
						// Get the current level statistic:
						Node statisticNode = statisticNodes.item(j);
						NamedNodeMap statisticAttributes = statisticNode.getAttributes();
						
						// Check whether this is the statistic we want to add our score before:
						if ((score < Integer.parseInt(statisticAttributes.getNamedItem("score").getTextContent()))) {
							
							// Add new statistic before the current statistic:
							levelNode.insertBefore(newStatisticElement, statisticNode);
							
							// Check whether there are now more than 10 statistics:
							if (statisticNodes.getLength() > 10) {
								levelNode.removeChild(levelNode.getLastChild());
							}
							
							isAdded = true;
							break;
						}
					}
					
					// If not inserted, check whether there is free space at the bottom of the list:
					if (!isAdded) {
						if (statisticNodes.getLength() < 10) {
							// Insert statistic at the bottom:
							levelNode.appendChild(newStatisticElement);
							
							isAdded = true;
						}
					}
					break;
				}
			}
			
			// This level was played for the first time:
			if (!levelFound) {

				// Create the level entry first:
				Element levelElement = doc.createElement("level");
				levelElement.setAttribute("id", Integer.toString(levelId));
				first.appendChild(levelElement);
				levelElement.appendChild(newStatisticElement);
				
				isAdded = true;
			}
			
			// If statistics have changed, write back to file:
			if (isAdded) {
				OutputFormat format = new OutputFormat(doc);
				FileOutputStream fileOut = new FileOutputStream(_lvlSetPath + levelSetId + File.separator + "highScores.xml");
				XMLSerializer ser = new XMLSerializer(fileOut,format);
				ser.serialize(doc.getDocumentElement());
				fileOut.close();
			}
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return isAdded;
	}
	
	/**
	 * Removes highscore for removed player
	 * @param playerId player id
	 */
	void removeHighscore(int playerId){
		for(LevelSetIdentifier i: _levelSetIdentifiers){
			try{
				DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
				Document doc = docBuilder.parse(new File(_lvlSetPath+i.getId()+File.separator+"highScores.xml"));
				NodeList playerStatNameList = doc.getElementsByTagName("PlayerStatistic");
				//Node first =doc.getFirstChild();
				ArrayList<Node> nodeToDelete = new ArrayList<Node>();
				for (int j = 0; j < playerStatNameList.getLength(); j++) {
					Node tmp = playerStatNameList.item(j);
					NamedNodeMap attr=tmp.getAttributes();
					int currPlrId =Integer.parseInt(attr.getNamedItem("id").getTextContent());
					if(playerId == currPlrId){
						nodeToDelete.add(tmp);
						
					}
				}
				for(Node j: nodeToDelete){
					Element parent =(Element)j.getParentNode();
					parent.removeChild(j);
				}
				
				OutputFormat format = new OutputFormat(doc);
				FileOutputStream outputFile =new FileOutputStream(_lvlSetPath+i.getId()+File.separator+"highScores.xml");
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

	/**
	 * Gets map of HighScoreList with level id as keys
	 * @param id level set id
	 * @return map of HighScoreList with level id as keys
	 */
	public HashMap<Integer, ArrayList<Highscore>> getHighScoreLists(int id){
		HashMap<Integer, ArrayList<Highscore>> attrSet = new HashMap<Integer, ArrayList<Highscore>>();
		try{
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(_lvlSetPath+id+File.separator+"highScores.xml"));
			
			NodeList levelNameList = doc.getElementsByTagName("level");
			//int currId
			for (int i = 0; i < levelNameList.getLength(); i++) {
				Node tmp = levelNameList.item(i);
				//System.out.println(tmp.getNodeName());
				NamedNodeMap attr=tmp.getAttributes();
				//String test = attr.getNamedItem("id").getTextContent();
				//System.out.println(test);
				//Integer intd =Integer.parseInt(test);
				int currLevelId =Integer.parseInt(attr.getNamedItem("id").getTextContent());
				//String currName = attr.getNamedItem("name").getTextContent();
				//System.out.println(tmp.getFirstChild().getNodeValue());
				//System.out.println(tmp.getNextSibling().getNodeValue());
				
				Element el = (Element)tmp;
				
				NodeList playerScores = el.getElementsByTagName("PlayerStatistic");
				//NodeList playerScores = tmp.getChildNodes();
				//System.out.println(playerScores.getLength());
								
				
				ArrayList<Highscore> scores = new ArrayList<Highscore>();
				for (int j = 0; j < playerScores.getLength(); j++) {
					Node plrStats = playerScores.item(j);
					//System.out.println(plrStats.getNodeName());
					//if(!plrStats.getNodeName().equals("PlayerStatistic"))
					//	continue;
					//System.out.println(plrStats.getNodeName());
					NamedNodeMap stats=plrStats.getAttributes();
					scores.add(new Highscore(Integer.parseInt(stats.getNamedItem("id").getTextContent()), 
							Integer.parseInt(stats.getNamedItem("score").getTextContent()), 
							Long.parseLong(stats.getNamedItem("time").getTextContent())));
				}
				
//				ArrayList<IdentifierRecord> levels = getLevelIdentifiers(id);
//				IdentifierRecord lvlIdent= null;
//				for(IdentifierRecord j: levels){
//					if(j.getId() == currLevelId){
//						lvlIdent = j;
//					}
//						
//				}
				attrSet.put(currLevelId, scores);
				
			}
			//this._lastId = lastId;
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}
		return attrSet;
		
	}
	
	
	/**
	 * Gets level identifier list by the given level set id
	 * @param id level set id
	 * @return list of level identifiers
	 */
	public ArrayList<LevelIdentifier> getLevelIdentifiers(int id){
		LevelManager lvlMan = new LevelManager(id);
		return lvlMan.getLevelIdentifiers();
	}
	
	/**
	 * Gets level by parameters
	 * @param levelSetId level set id
	 * @param ident LevelIdentifier
	 * @return Level
	 * @throws LevelFormatException
	 * @throws IllegalFormatException
	 * @throws IOException
	 */
	public Level getLevel(int levelSetId, LevelIdentifier ident) 
								throws LevelFormatException, IllegalFormatException, IOException{
		LevelManager lvlMan = new LevelManager(levelSetId);
		return lvlMan.getLevel(ident);
	}
	
	/**
	 * Only for tests
	 * @param playerId player id
	 */
	@Deprecated
	public void testRemoveHighScore(int playerId){
		removeHighscore(playerId);
	}
	
	public void resetLevelSetConfig() throws DuplicateException{
		//TODO: ver2
		for(LevelSetIdentifier i: _levelSetIdentifiers){
			LevelManager man = new LevelManager(i);
			ArrayList<LevelIdentifier> list =man.getLevelIdentifiers();
			for(LevelIdentifier j: list){
				String lvlId = new String();
				if(j.getId()<10){
					lvlId = "level_0"+j.getId()+".txt"; 
				}else{
					lvlId = "level_"+j.getId()+".txt";
				}
					
				File lvlFile = new File(_lvlSetPath+i.getId()+File.separator+lvlId);
				if(!lvlFile.exists())
					man.delLevel(j.getId());
			}
			
			File dir = new File(_lvlSetPath+i.getId()+File.separator);
			String[] newFiles =dir.list(new LevelFilter(list));
			for(int j =0; j< newFiles.length;j++){
				man.addLevel(newFiles[j]);
			}
			
		}
		
		
	}
	
	private class LevelFilter implements FilenameFilter{
		
		private ArrayList<LevelIdentifier> _list = null;
		public LevelFilter(ArrayList<LevelIdentifier> list){
			this._list =list;
		}
		
		public boolean accept(File dir, String name){
			if(!name.matches("level_\\d\\d.txt"))
				return false;
			String strID =name.substring(6, name.indexOf(".txt"));
			int id = Integer.parseInt(strID);
			//System.out.println(index);
			
			for(LevelIdentifier i: _list){
				if(i.getId() == id)
					return false;
			}
			return true;
		}
		
	
		
	}
	
	public static void main(String... args){
		LevelSetManager _lsm = LevelSetManager.getInstance();
		try{
			_lsm.resetLevelSetConfig();
		}catch(DuplicateException e){
			e.printStackTrace();
		}
	}
	
	
}

