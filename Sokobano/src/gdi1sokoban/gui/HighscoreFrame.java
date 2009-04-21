package gdi1sokoban.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import gdi1sokoban.SoundMaster;
import gdi1sokoban.gui.event.*;
import gdi1sokoban.graphic.base.*;
import gdi1sokoban.logic.*;

public class HighscoreFrame extends MenuFrame {

	private Label _labelLevel;
	private Button _buttonLevel;
	private Button _buttonLevelNext;
	private Button _buttonLevelPrevious;
	
	private Label _labelLevelSet;
	private Button _buttonLevelSet;
	private Button _buttonLevelSetNext;
	private Button _buttonLevelSetPrevious;
	
	private Surface _labelBkgnd;
	private Label _labelName[] = new Label[10];
	private Label _labelScore[] = new Label[10];
	private Label _labelTime[] = new Label[10];

	private Button _buttonBack;
	
	private ArrayList<PlayerIdentifier> _playerIdentifiers;
	private ListIterator<PlayerIdentifier> _iPlayerIdentifier;
	private PlayerIdentifier _playerIdentifier;
	
	private ArrayList<LevelSetIdentifier> _levelSetIdentifiers;
	private ListIterator<LevelSetIdentifier> _iLevelSetIdentifier;
	private LevelSetIdentifier _levelSetIdentifier;
	
	private ArrayList<LevelIdentifier> _levelIdentifiers;
	private ListIterator<LevelIdentifier> _iLevelIdentifier;
	private LevelIdentifier _levelIdentifier;
	
	private HashMap<Integer, ArrayList<Highscore>> _highscores;
	
	private boolean _hasPlayer;
	
	HighscoreFrame(Frame frame) throws Exception {
		super(frame);
		initialize();
	}
	
	public HighscoreFrame(float x, float y, float width, float height) throws Exception {
		super(x, y, width, height);
		initialize();
	}
	
	public void initialize() throws Exception {
		//SoundMaster.stopMainMenu();
		SoundMaster.playHighScore();
		
		// Loading the player list from disk:
		_playerIdentifiers = PlayerManager.getInstance().getPlayerIdentifiers();
		_iPlayerIdentifier = _playerIdentifiers.listIterator();
		_hasPlayer = _iPlayerIdentifier.hasNext();
		if (_hasPlayer) _playerIdentifier = _iPlayerIdentifier.next();
		
		// Load level sets:
		_levelSetIdentifiers = LevelSetManager.getInstance().getLevelSetIdentifiers();
		_iLevelSetIdentifier = _levelSetIdentifiers.listIterator();
		assert(_iLevelSetIdentifier.hasNext());
		_levelSetIdentifier = _iLevelSetIdentifier.next();
		
		// Load highscores:
		_highscores = LevelSetManager.getInstance().getHighScoreLists(_levelSetIdentifier.getId());
		
		// Load levels:
		_levelIdentifiers = LevelSetManager.getInstance().getLevelIdentifiers(_levelSetIdentifier.getId());
		_iLevelIdentifier = _levelIdentifiers.listIterator();
		assert(_iLevelIdentifier.hasNext());
		_levelIdentifier = _iLevelIdentifier.next();
		
		// Components & event handling:
		
		// Level set:
		_labelLevelSet = new Label(-0.35f, -0.46f, 0.2f, 0.06f, "Set", Label.NONE);
		add(_labelLevelSet);
		
		_buttonLevelSetPrevious = new Button(-0.49f, -0.4f, 0.06f, 0.06f, "<", Button.CAP_LEFT);
		_buttonLevelSetPrevious.addActionListener(new ButtonLevelSetPreviousActionListener());
		add(_buttonLevelSetPrevious);
		
		_buttonLevelSet = new Button(-0.42f, -0.4f, 0.395f, 0.06f, _levelSetIdentifier.getName(), Button.CAP_NONE);
		_buttonLevelSet.addActionListener(new ButtonLevelSetNextActionListener());
		add(_buttonLevelSet);
		
		_buttonLevelSetNext = new Button(-0.015f, -0.4f, 0.06f, 0.06f, ">", Button.CAP_RIGHT);
		_buttonLevelSetNext.addActionListener(new ButtonLevelSetNextActionListener());
		add(_buttonLevelSetNext);
		
		// Level:
		_labelLevel = new Label(0.20f, -0.46f, 0.2f, 0.06f, "Level", Label.NONE);
		add(_labelLevel);
		
		_buttonLevelPrevious = new Button(0.055f, -0.4f, 0.06f, 0.06f, "<", Button.CAP_LEFT);
		_buttonLevelPrevious.addActionListener(new ButtonLevelPreviousActionListener());
		add(_buttonLevelPrevious);
		
		_buttonLevel = new Button(0.125f, -0.4f, 0.295f, 0.06f, _levelIdentifier.getName(), Button.CAP_NONE);
		_buttonLevel.addActionListener(new ButtonLevelNextActionListener());
		add(_buttonLevel);
		
		_buttonLevelNext = new Button(0.43f, -0.4f, 0.06f, 0.06f, ">", Button.CAP_RIGHT);
		_buttonLevelNext.addActionListener(new ButtonLevelNextActionListener());
		add(_buttonLevelNext);
		
		// create highscore labels:
		_labelBkgnd = new Surface(-0.5f, -0.315f, 1.0f, 0.61f, Surface.CAP_NONE);
		_labelBkgnd.setBackgroundTexture(null, TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/bkgnd_highscore.png", GL11.GL_LINEAR, GL11.GL_LINEAR)), null);
		add(_labelBkgnd);
		for (int i = 0; i < 10; i++) {
			
			_labelName[i] = new Label(-0.5f, -0.31f + i * 0.06f, 0.6f, 0.06f, "", Label.NONE);
			add(_labelName[i]);
			
			_labelScore[i] = new Label(0.1f, -0.31f + i * 0.06f, 0.2f, 0.06f, "", Label.NONE);
			_labelScore[i].setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/step_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
			add(_labelScore[i]);
			
			_labelTime[i] = new Label(0.3f, -0.31f + i * 0.06f, 0.2f, 0.06f, "", Label.NONE);
			_labelTime[i].setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/clock_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
			add(_labelTime[i]);
		}
		displayHighscores(_levelIdentifier.getId());
		
		_buttonBack = new Button(-0.2f, 0.33f, 0.4f, 0.08f, "Back");
		_buttonBack.addActionListener(new ButtonBackActionListener());
		_buttonBack.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/back_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonBack);
		
		addKeyboardListener(new FrameKeyboardListener());
		
		_zoomTarget = 0.0f;
		_angleYTarget = -90;
		_timebase = System.currentTimeMillis();	
	}

	private class ButtonLevelNextActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayNextLevel();
			}
		}
	}
	
	private class ButtonLevelPreviousActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayPreviousLevel();
			}
		}
	}
	
	private class ButtonLevelSetNextActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayNextLevelSet();
			}
		}
	}
	
	private class ButtonLevelSetPreviousActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayPreviousLevelSet();
			}
		}
	}
	
	private class ButtonBackActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				SoundMaster.stopHighScore();
				try {
					Frame nextFrame = new StartFrame(HighscoreFrame.this);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (Exception e) {
					// Nothing should happen here:
					e.printStackTrace();
				}
			}
		}
	}

	private class FrameKeyboardListener implements KeyboardListener {
		public boolean keyboardEvent(KeyboardEvent event){
			switch (event.getCode()) {
			case Keyboard.KEY_ESCAPE :
				if (event.getState()) {
					SoundMaster.stopHighScore();
					try {
						Frame nextFrame = new StartFrame(HighscoreFrame.this);
						processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					} catch (Exception e) {
						// Nothing should happen here:
						e.printStackTrace();
					}
				}
			}
			return false;
		}
	}
	
	/**
	 * The next level set becomes the current level set.
	 */
	public void displayNextLevelSet() {
		
		// Display next level set name:
		if (!_iLevelSetIdentifier.hasNext()) {
			_iLevelSetIdentifier = _levelSetIdentifiers.listIterator();
		}
		_levelSetIdentifier = _iLevelSetIdentifier.next();
		
		displayLevelSet();
	}
	
	/**
	 * The previous level set becomes the current level set.
	 */
	public void displayPreviousLevelSet() {
		
		// Display previous level set name:
		if (!_iLevelSetIdentifier.hasPrevious()) {
			_iLevelSetIdentifier = _levelSetIdentifiers.listIterator(_levelSetIdentifiers.size());
		}
		_iLevelSetIdentifier.previous();
		
		if (!_iLevelSetIdentifier.hasPrevious()) {
			_iLevelSetIdentifier = _levelSetIdentifiers.listIterator(_levelSetIdentifiers.size());
		}
		_levelSetIdentifier = _iLevelSetIdentifier.previous();
		
		_iLevelSetIdentifier.next();
		
		displayLevelSet();
	}
	
	/**
	 * Updates the displayed level set to the current level set.
	 * Depending values are updated.
	 */
	public void displayLevelSet() {
		
		_buttonLevelSet.setText(_levelSetIdentifier.getName());
		
		// Load new levels:
		_levelIdentifiers = LevelSetManager.getInstance().getLevelIdentifiers(_levelSetIdentifier.getId());
		_iLevelIdentifier = _levelIdentifiers.listIterator();
		_levelIdentifier = _iLevelIdentifier.next();
		_buttonLevel.setText(_levelIdentifier.getName());
		
		// Load highscores:
		_highscores = LevelSetManager.getInstance().getHighScoreLists(_levelSetIdentifier.getId());
		
		displayHighscores(_levelIdentifier.getId());
	}
	
	public void displayNextLevel() {
		
		// Display next level name:
		if (!_iLevelIdentifier.hasNext()) {
			_iLevelIdentifier = _levelIdentifiers.listIterator();
		}
		_levelIdentifier = _iLevelIdentifier.next();
		_buttonLevel.setText(_levelIdentifier.getName());
		
		displayHighscores(_levelIdentifier.getId());
	}
	
	public void displayPreviousLevel() {
		
		// Display previous level name:
		if (!_iLevelIdentifier.hasPrevious()) {
			_iLevelIdentifier = _levelIdentifiers.listIterator(_levelIdentifiers.size());
		}
		_iLevelIdentifier.previous();
		
		if (!_iLevelIdentifier.hasPrevious()) {
			_iLevelIdentifier = _levelIdentifiers.listIterator(_levelIdentifiers.size());
		}
		_levelIdentifier = _iLevelIdentifier.previous();
		
		_levelIdentifier = _iLevelIdentifier.next();
		
		_buttonLevel.setText(_levelIdentifier.getName());
		
		displayHighscores(_levelIdentifier.getId());
	}
	
	/**
	 * Display highscores
	 * 
	 * @param levelId
	 */
	private void displayHighscores(int levelId) {
		for (int i = 0; i < 10; i++) {
			
			ArrayList<Highscore> entry = _highscores.get(levelId);
			
			
			String score = "-";
			String time = "-";
			String name = "-";
			
			if ((entry != null) && (entry.size() > i)) {
				// Read score:
				score = String.valueOf(entry.get(i).getScore());
			
				// Read Time:
				time = String.valueOf(entry.get(i).getTime()/1000);
			
				// Read player name:
				if (_hasPlayer) {
					if ((entry != null) && (entry.size() > i)) {
						_iPlayerIdentifier = _playerIdentifiers.listIterator();
						_playerIdentifier = _iPlayerIdentifier.next();
						
						while (_playerIdentifier.getId() != entry.get(i).getId()) {
							if (!_iPlayerIdentifier.hasNext()) {
								name = "deleted";
								break;
							}
							_playerIdentifier = _iPlayerIdentifier.next();
						}
						if (!name.equals("deleted")) name = _playerIdentifier.getName();
					}
				}
				else name = "deleted";
			}
			
			_labelName[i].setText(name);
			_labelScore[i].setText(score);
			_labelTime[i].setText(time);
		}
	}
	
	public void render() {
		
		renderBackground();
		//_title.access().render();

	    super.render();
	}
}