package gdi1sokoban.logic;

import gdi1sokoban.exceptions.DuplicateException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
 * This class provides basic operations with Identifier records 
 * @author Stalker
 *
 */
public abstract class IdentifierManager {
	
	//last id in identifiers
	protected int _lastId;
	//path to identifier xml file
	protected String _pathToIdentifierFile;
	//list of identifiers
	protected ArrayList<IdentifierRecord> _identifierRecords;
	
	/**
	 * Creates identifierManager by given path and sets intern variables
	 * 
	 * @param path path to xml file
	 */
	protected IdentifierManager(String path){
		this._pathToIdentifierFile = path;
		this._identifierRecords = getIdentifiers();
	}
		

	/**
	 * Sorts Identifier Objects by name
	 */
	private void sortIdentifierRecordName(){
		Collections.sort(this._identifierRecords,new IdentifierComparator());
	}
	
	/**
	 * Gets identifier records 
	 * @return identifier records
	 */
	protected ArrayList<IdentifierRecord> getIdentifiers(){
		ArrayList<IdentifierRecord> records = new ArrayList<IdentifierRecord>();
		try{
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(_pathToIdentifierFile));
			NodeList nameList = doc.getElementsByTagName("identifier");
			int lastId =0;
			for (int i = 0; i < nameList.getLength(); i++) {
				Node tmp = nameList.item(i);
				NamedNodeMap attr=tmp.getAttributes();
				int currId =Integer.parseInt(attr.getNamedItem("id").getTextContent());
				if(lastId < currId)
					lastId =currId;
				IdentifierRecord record = new IdentifierRecord (attr.getNamedItem("name").getTextContent(),
						 currId, attr.getNamedItem("uri").getTextContent());
				records.add(record);
				
			}
			this._lastId = lastId;
			
				
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}
		//Collections.sort(records,new IdentifierComparator());
		return records;
	}
	
	/**
	 * Checks records for duplicates
	 * @param name record name
	 * @return is duplicate
	 */
	public boolean checkDuplicates(String name){
		for (IdentifierRecord i: this._identifierRecords){
			if(i.getName().equals(name))
				return true;
		}
		return false;
	}
		
	/**
	 * Add new Identifier by creating some files(or directories) and also updates
	 * _identifierRecords and xml file
	 * @param name name of the Record
	 * @param uri record uri
	 */
	protected int addIdentifierRecord(String name, String uri) throws DuplicateException{
		this._lastId++;
		addIdentifierRecord(_lastId, name, uri) ;
		return _lastId;
	}
	
	protected void addIdentifierRecord(int id, String name, String uri) throws DuplicateException{
		try{
			if (checkDuplicates(name))
				throw new DuplicateException();
			//write to xml
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(_pathToIdentifierFile));
			Element setElement =doc.createElement("identifier");
			setElement.setAttribute("name", name);
			setElement.setAttribute("uri", uri);
			setElement.setAttribute("id", Integer.toString(id));
			Node first =doc.getFirstChild();
			first.appendChild(setElement);
			//Write XML			
			OutputFormat format = new OutputFormat(doc);
			FileOutputStream outputStr = new FileOutputStream(_pathToIdentifierFile);
			XMLSerializer ser = new XMLSerializer(outputStr,format);
			ser.serialize(doc.getDocumentElement());
			outputStr.close();
			//
			
			//write to list and sorts
			this._identifierRecords.add(new IdentifierRecord(name,id, uri));
			//sortIdentifierRecordName();
			//
			
			
			//Create Directory
			createIdentifierFile();
			//
			
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}
	}
	
	public void changeIdentifierRecord(int id, String name, String uri) {
		try{
			IdentifierRecord delRecord = null;
			//delete from xml
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(_pathToIdentifierFile));
			NodeList removeList = doc.getElementsByTagName("identifier");
			for (int i = 0; i < removeList.getLength(); i++) {
				Node tmp = removeList.item(i);
				NamedNodeMap attr=tmp.getAttributes();
				if(id == Integer.parseInt(attr.getNamedItem("id").getTextContent())){
					delRecord = new IdentifierRecord(attr.getNamedItem("name").getTextContent(),
							id,
							attr.getNamedItem("uri").getTextContent());
					attr.getNamedItem("name").setTextContent(name);
					attr.getNamedItem("uri").setTextContent(uri);
				}
			}
			
			OutputFormat format = new OutputFormat(doc);
			FileOutputStream outputFile =new FileOutputStream(_pathToIdentifierFile);
			XMLSerializer ser = new XMLSerializer(outputFile,format);
			ser.serialize(doc.getDocumentElement());
			outputFile.close();
			
			//change from list
			int index = 0;
			for(IdentifierRecord ident : _identifierRecords){
				if (id == ident.getId()){
					break;
				}
				index++;
			}
			_identifierRecords.set(index, delRecord);
		}
		catch (Exception e) {};	
	}
	
	/**
	 * Creates identifier files 
	 */
	protected abstract void createIdentifierFile();
		
	/**
	 * Removes Identifier by deleting files, updates _identifierRecords
	 * and xml file
	 * @param id record id
	 */
	protected void delIdentifierRecord(int id){
		try{
			IdentifierRecord delRecord = null;
			//delete from xml
			DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
			Document doc = docBuilder.parse(new File(_pathToIdentifierFile));
			Node first =doc.getFirstChild();
			NodeList removeList = doc.getElementsByTagName("identifier");
			for (int i = 0; i < removeList.getLength(); i++) {
				Node tmp = removeList.item(i);
				NamedNodeMap attr=tmp.getAttributes();
				if(id == Integer.parseInt(attr.getNamedItem("id").getTextContent())){
					delRecord = new IdentifierRecord(attr.getNamedItem("name").getTextContent(),
							id,
							attr.getNamedItem("uri").getTextContent());
					first.removeChild(tmp);
				}
			}
			
			OutputFormat format = new OutputFormat(doc);
			FileOutputStream outputFile =new FileOutputStream(_pathToIdentifierFile);
			XMLSerializer ser = new XMLSerializer(outputFile,format);
			ser.serialize(doc.getDocumentElement());
			outputFile.close();
							
			//
			
			//delete from list
			for(IdentifierRecord i : this._identifierRecords){
				if (id == i.getId()){
					this._identifierRecords.remove(i);
					break;
				}
			}
			//
			//Delete Directory
			deleteIdentifierFile(delRecord);
			//
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}
	
	}
	
	/**
	 * Deletes identifier files
	 * @param delRecord record to delete
	 */
	protected abstract void deleteIdentifierFile(IdentifierRecord delRecord);
	
	/**
	 * Recursively deletes directory, and all in the directory
	 * @param path path to directory to delete
	 * @return
	 */
	protected boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return (path.delete());
	    
	  }
	
	

}


/**
 * Comparator, that compares Identifier objects by name
 * @author Stalker
 *
 */
class IdentifierComparator implements Comparator<IdentifierRecord>{
	
	
	public int compare(IdentifierRecord thisIdentifierRecord, IdentifierRecord compareIdentifierRecord){
		
		int retValue=0;
		String thisIdentifierName=(thisIdentifierRecord).getName();
		String comparedIdentifierName=(compareIdentifierRecord).getName();
		
		if (!(thisIdentifierName.equals(comparedIdentifierName))){
			retValue=(thisIdentifierName.compareTo(comparedIdentifierName));
		}
		return retValue;
		
	}

}


