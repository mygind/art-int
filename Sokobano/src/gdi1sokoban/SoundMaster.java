package gdi1sokoban;


import java.io.File;
import java.io.IOException;
import javax.media.CannotRealizeException;
import javax.media.NoPlayerException;

/**
 * class: SoundMaster implements
 * all needed sounds and functions
 * 
 *
 */
public class SoundMaster {
// Sound sources:
	private static final String PATH = "res"+File.separator+"sounds"+File.separator;
	private static final String MAIN_MENU = PATH +"Menu 1.mp2";
	private static final String MENU_TOGGLE = PATH+"menuToggle.wav";
	private static final String GAME = PATH+"During play - lotus.mp2";
	private static final String DEAD_LOCK = PATH+"Deadlock electro.mp2";
	private static final String WORKER_MOVE = PATH+"workerMove.wav";
	private static final String LEVEL_SOLVED = PATH+"Cheer.mp2";
	private static final String CREDITS = PATH+"Credits2.mp2";
	private static final String UNDO = PATH+"undo.mp2";
	private static final String REDO = PATH+"redo.mp2";
	private static final String WALL = PATH+"wall.mp2";
	private static final String CHEAT = PATH+"cheat.mp2";
	private static final String PUSH = PATH+"push.mp2";
	private static final String HIGHSCORE = PATH+"Highscore.mp2";

// Players:
	
	private static AudioPlayer _wall = null;
	private static AudioPlayer _redo = null;
	private static AudioPlayer _undo = null;
	private static AudioPlayer _levelSolved = null;
	private static AudioPlayer _workerMove = null;
	private static AudioPlayer _menuToggle = null;
	private static AudioPlayer _deadlock = null;
	private static AudioPlayer _mainMenu = null;
	private static AudioPlayer _game = null;
	private static AudioPlayer _credits = null;
	private static AudioPlayer _cheat = null;
	private static AudioPlayer _push = null;
	private static AudioPlayer _highscore = null;

	
	public SoundMaster () throws NoPlayerException, CannotRealizeException, IOException{
		
	}
	
	/**
	 * plays a sound in Main Menu
	 */
	public static void playMainMenu(){
		try{
			if(_mainMenu == null){
				_mainMenu = new AudioPlayer (MAIN_MENU);
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
	public static void stopMainMenu(){
		if(_mainMenu == null)
			return;
		_mainMenu.stop();
		_mainMenu = null;
	}
	
	/**
	 * plays a sound on menu-toggling
	 */
	public static void playMenuToggle(){
		if (_menuToggle == null)
		try{
			_menuToggle = new AudioPlayer (MENU_TOGGLE);
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		_menuToggle.playPrefetch();
	}
	/**
	 * stops the sound on menu-toggling
	 * perhaps not needed due to the sounds length
	 */
	public static void stopMenuToggle(){
		_menuToggle.stop();
	}
	/**
	 * plays a game-sound
	 */
	public static void playGame(){
		try{
			_game = new AudioPlayer (GAME);
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		_game.alwaysPlay();
	}
	/**
	 * stops the game-sound 
	 */
	public static void stopGame(){
		_game.stop();
	}
	/**
	 * plays a sound on deadlock
	 */
	public static void playDeadlock(){
		try{
			_deadlock = new AudioPlayer (DEAD_LOCK);
			
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		_deadlock.play();
	}
	/**
	 * stops the sound on deadlock
	 * perhaps not needed due to the sounds length
	 */
	@Deprecated
	public static void stopDeadlock(){
		_deadlock.stop();
	}
	/**
	 * plays a sound when the worker moves
	 */
	public static void playWorkerMove(){
		try{
			_workerMove = new AudioPlayer (WORKER_MOVE);
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		_workerMove.play();
	}
	/**
	 * stops the sound on workers move
	 * perhaps not needed due to the sounds length
	 */
	@Deprecated
	public static void stopWorkerMove(){
		_workerMove.stop();
	}
	
	/**
	 * plays a sound on solved level
	 */
	public static void playLevelSolved(){
		try{
			_levelSolved = new AudioPlayer (LEVEL_SOLVED);
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		_levelSolved.play();
	}
	/**
	 * stops the sound on solved level
	 * perhaps not needed due to the sounds length
	 */
	@Deprecated
	public static void stopLevelSolved(){
		_levelSolved.stop();
	}
	/**
	 * plays the sound on credits
	 */
	public static void playCredits(){
		try{
			
			_credits = new AudioPlayer (CREDITS);
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		_credits.alwaysPlay();
	}
	/**
	 * stops the sound on credits
	 */
	public static void stopCredits(){
		_credits.stop();
	}
	
	/**
	 * plays a sound on
	 * move-undo
	 */
	public static void playUndo(){
		try{
			_undo = new AudioPlayer (UNDO);
			
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		_undo.playPrefetch();
	}
	
	/**
	 * plays a sound
	 * on move-redo
	 */
	public static void playMove(){
		if (_redo == null)
		try{
			_redo = new AudioPlayer (REDO);
			
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		_redo.playPrefetch();
	}
	
	/**
	 * plays a sound if
	 * a player tries
	 * to move a wall
	 */
	public static void playWall(){
		try{
			_wall = new AudioPlayer (WALL);
			
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		_wall.play();
	}
	/**
	 * plays a sound
	 * on using cheat
	 */
	public static void playCheat(){
		try{
			_cheat = new AudioPlayer (CHEAT);
			
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		_cheat.play();
	}

	/**
	 * playes a sound
	 * on pushing crates
	 */
	public static void playPush(){
		try{
			if(_push == null )
				_push = new AudioPlayer (PUSH);}
		catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		_push.playPrefetch();
	}
	
	
	/**
	 * plays a sound
	 * on highscore-menu
	 */
	public static void playHighScore(){
		try{
			_highscore = new AudioPlayer(HIGHSCORE);
			_highscore.alwaysPlay();
			
		}catch(CannotRealizeException e){
			e.printStackTrace();
		}catch(NoPlayerException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * stops the sound
	 * on highscore_menu
	 */
	public static void stopHighScore(){
		if(_highscore != null) _highscore.stop();
	}
}
