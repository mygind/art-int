package gdi1sokoban;
import gdi1sokoban.logic.ConfigManager;

import javax.media.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import java.io.File;
import java.io.IOException;

/**
 * class: _Player defines a player
 * 
 *
 */
public class AudioPlayer {
	private Player audioPlayer = null;
	private String _path = null;
	private ControllerListener _alwaysListener = null;
	private ControllerListener _prefetchListener = null;
	private ControllerListener _soundListener = null;
	
	public AudioPlayer (String path) throws IOException, NoPlayerException, CannotRealizeException {
		_path = path;
		
Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);

		audioPlayer = Manager.createRealizedPlayer(new MediaLocator(new File(path).toURL().toExternalForm()));
	}
	
	/**
	 * starts the player
	 */
	public void play(){
		if (!ConfigManager.getInstance().getGlobalConfig().isSound()) return;
		
		if (_alwaysListener == null) {
			_soundListener = new SoundListener();
			audioPlayer.addControllerListener(_soundListener);
		}
		audioPlayer.start();

	}
	
	public void playPrefetch(){
		if (!ConfigManager.getInstance().getGlobalConfig().isSound()) return;
		
		if (_prefetchListener == null) {
			_prefetchListener = new SoundPrefetchListener();
			audioPlayer.addControllerListener(_prefetchListener);
		}
		audioPlayer.start();
		
	}
	/**
	 * stops the player and closes the thread
	 */
	public void stop(){
		if (audioPlayer!= null) {
		if (_alwaysListener != null) audioPlayer.removeControllerListener(_alwaysListener);
		if (_prefetchListener != null) audioPlayer.removeControllerListener(_prefetchListener);
		if (_soundListener != null) audioPlayer.removeControllerListener(_soundListener);
		audioPlayer.stop();
		//audioPlayer.close();
		//if (audioPlayer!= null) audioPlayer.deallocate();
		//audioPlayer = null;
		}
	}
	
	
	public void alwaysPlay(){
		if (!ConfigManager.getInstance().getGlobalConfig().isSound()) return;
		
		if (_alwaysListener == null) {
			_alwaysListener = new MusicListener();
			audioPlayer.addControllerListener(_alwaysListener);
		}
		audioPlayer.start();
		
	}
	/**
	 * stops the player
	 * without closing the thread
	 */
	public void stopPlaying() {
		if (audioPlayer == null)
			return;
		audioPlayer.stop();
	}

	/**
	 * determins whether
	 * the player is
	 * already playing
	 * @return state: int
	 */
	public int getState(){
		if (!ConfigManager.getInstance().getGlobalConfig().isSound()) return 0;
		
		return audioPlayer.getState();
	}
	
	private class MusicListener implements ControllerListener {
		public void controllerUpdate(ControllerEvent event){
			if (event instanceof EndOfMediaEvent) {
					audioPlayer.stop();
					audioPlayer.setMediaTime(new Time(0));
					audioPlayer.start();
			} 
		}		
	}
	
	private class SoundListener implements ControllerListener {
		public void controllerUpdate(ControllerEvent event){
			if (event instanceof EndOfMediaEvent) {
				audioPlayer.stop();
			} 
		}		
	}
	
	private class SoundPrefetchListener implements ControllerListener {
		public void controllerUpdate(ControllerEvent event){
			if (event instanceof EndOfMediaEvent) {
				audioPlayer.stop();
				audioPlayer.setMediaTime(new Time(0));
			} 
		}		
	}
	
	protected void finalize() {
		if (audioPlayer != null)  {
			audioPlayer.stop();
			audioPlayer.close();
			audioPlayer.deallocate();
		}
	}
}
