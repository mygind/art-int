package gdi1sokoban.logic;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ConfigManager {
	
	private static final String GL_CONFIG_PATH ="res"+File.separator+"config"+File.separator+"global.cfg";
	
	private static ConfigManager _instance;
	
	private GlobalConfig _config;
	
	public static ConfigManager getInstance() {
		if (_instance == null)
			_instance = new ConfigManager();
		
		return _instance;
	}
	
	public void saveGlobalConfig(GlobalConfig conf){
		try{
			FileOutputStream wr = new FileOutputStream(GL_CONFIG_PATH);
			ObjectOutputStream objstream = new ObjectOutputStream(wr);
			objstream. writeObject(conf);
			objstream.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public GlobalConfig getGlobalConfig(){
		if (_config == null) {
			_config = loadGlobalConfig();
		}
	    return _config;
	}
	
	private GlobalConfig loadGlobalConfig(){
		try{
			FileInputStream reader = new FileInputStream(GL_CONFIG_PATH);
			
			ObjectInputStream objstream = new ObjectInputStream(reader);
		    Object object = null;
		    
		    object = objstream.readObject();
		    
		    objstream.close();
		    return (GlobalConfig)object;
		}
	    catch(ClassNotFoundException e){
	    	e.printStackTrace();
	    }catch(IOException e){
	    	e.printStackTrace();
	    }
	    return null;
	}
}
