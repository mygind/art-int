package gdi1sokoban;

import java.io.File;
import java.io.IOException;

import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;

public class Audio extends Thread {

	// Sound sources:
	private static final String PATH = "res"+File.separator+"sounds"+File.separator;
	private static final String MAIN_MENU = PATH +"Menu 1.mp2";

	// Players:
	private AudioPlayer _mainMenu = null;
	
	// Instance:
	private static Audio _instance;
	
	protected Audio () {
	}
	
	public static Audio getInstance() {
		if (_instance == null) {
			_instance = new Audio();
			_instance.setPriority(MAX_PRIORITY);
		}
		return _instance;
	}
	
	/**
	 * plays a sound in Main Menu
	 */
	public void playMainMenu(){
		try{
			if(_mainMenu == null){
				_mainMenu = new AudioPlayer(MAIN_MENU);
				_mainMenu.alwaysPlay();
				}
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	  
	}
	
	/**
	 * stops the sound in Main Menu
	 */
	public void stopMainMenu(){
		if(_mainMenu == null)
			return;
		_mainMenu.stop();
		_mainMenu = null;
	}
	
	public void run() {
	}
}
