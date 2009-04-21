package gdi1sokoban.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import gdi1sokoban.graphic.base.TextureDescriptor;
import gdi1sokoban.graphic.base.TextureManager;
import gdi1sokoban.gui.event.*;
import gdi1sokoban.logic.*;

public class PlayerFrame extends MenuFrame {

	private Label _labelLevelSet;
	private Button _buttonLevelSet;
	private Button _buttonLevelSetPrevious;
	private Button _buttonLevelSetNext;
	
	private Label _labelLevel;
	private Button _buttonLevel;
	private Button _buttonLevelNext;
	private Button _buttonLevelPrevious;
	
	private Label _labelStats;
	private Label _labelMoves;
	private Label _labelTime;
	
	private Button _buttonConfig;
	private Button _buttonRun;
	private Button _buttonResume;
	private Button _buttonBack;
	
	private Label _labelInfo;
	
	private Player _player;
	
	private ArrayList<LevelSetIdentifier> _levelSetIdentifiers;
	private ListIterator<LevelSetIdentifier> _iLevelSetIdentifier;
	private LevelSetIdentifier _levelSetIdentifier;
	
	private ArrayList<LevelIdentifier> _levelIdentifiers;
	private ListIterator<LevelIdentifier> _iLevelIdentifier;
	private LevelIdentifier _levelIdentifier;
	
	private HashMap<Integer, LevelStatistic> _levelStatistics;
	private LevelStatistic _levelStatistic;
	
	PlayerFrame(Frame frame, Player player) throws Exception {
		super(frame);
		_player = player;
		initFrame();
	}
	
	void initFrame() throws Exception {
		
		assert(_player != null);
		
		// Load level sets:
		_levelSetIdentifiers = LevelSetManager.getInstance().getLevelSetIdentifiers();
		_iLevelSetIdentifier = _levelSetIdentifiers.listIterator();
		assert(_iLevelSetIdentifier.hasNext());
		//_levelSetIdentifier = _iLevelSetIdentifier.next();
		
		// Current level set is last played set:
		while (_iLevelSetIdentifier.hasNext()) {
			_levelSetIdentifier = _iLevelSetIdentifier.next();
			if (_levelSetIdentifier.getId() == _player.getCurrentLevelSetId()) {
				break;
			}
		}
		
		// Load levels:
		_levelIdentifiers = LevelSetManager.getInstance().getLevelIdentifiers(_levelSetIdentifier.getId());
		_iLevelIdentifier = _levelIdentifiers.listIterator();
		assert(_iLevelIdentifier.hasNext());
		//_levelIdentifier = _iLevelIdentifier.next();
		
		// Current level is last played level:
		while (_iLevelIdentifier.hasNext()) {
			_levelIdentifier = _iLevelIdentifier.next();
			if (_levelIdentifier.getId() == _player.getCurrentLevelId()) {
				break;
			}
		}
		
		// Load statistics:
		_levelStatistics = PlayerManager.getInstance().getLevelSetStatistic(_levelSetIdentifier.getId(), _player.getId());
		_levelStatistic = _levelStatistics.get(_levelIdentifier.getId());
		
		// Components & event handling:
		// Level set:
		_labelLevelSet = new Label(-0.1f, -0.46f, 0.2f, 0.06f, "Set", Label.NONE);
		add(_labelLevelSet);
		
		_buttonLevelSetPrevious = new Button(-0.39f, -0.4f, 0.08f, 0.08f, "<", Button.CAP_LEFT);
		_buttonLevelSetPrevious.addActionListener(new ButtonLevelSetPreviousActionListener());
		add(_buttonLevelSetPrevious);
		
		_buttonLevelSet = new Button(-0.3f, -0.4f, 0.6f, 0.08f, _levelSetIdentifier.getName(), Button.CAP_NONE);
		_buttonLevelSet.addActionListener(new ButtonLevelSetNextActionListener());
		add(_buttonLevelSet);
		
		_buttonLevelSetNext = new Button(0.31f, -0.4f, 0.08f, 0.08f, ">", Button.CAP_RIGHT);
		_buttonLevelSetNext.addActionListener(new ButtonLevelSetNextActionListener());
		add(_buttonLevelSetNext);
		
		// Level:
		_labelLevel = new Label(-0.1f, -0.31f, 0.2f, 0.06f, "Level", Label.NONE);
		add(_labelLevel);
		
		_buttonLevelPrevious = new Button(-0.29f, -0.25f, 0.08f, 0.08f, "<", Button.CAP_LEFT);
		_buttonLevelPrevious.addActionListener(new ButtonLevelPreviousActionListener());
		add(_buttonLevelPrevious);
		
		_buttonLevel = new Button(-0.2f, -0.25f, 0.4f, 0.08f, _levelIdentifier.getName(), Button.CAP_NONE);
		_buttonLevel.addActionListener(new ButtonLevelNextActionListener());
		add(_buttonLevel);
		
		_buttonLevelNext = new Button(0.21f, -0.25f, 0.08f, 0.08f, ">", Button.CAP_RIGHT);
		_buttonLevelNext.addActionListener(new ButtonLevelNextActionListener());
		add(_buttonLevelNext);
		
		// Statistics:
		_labelStats = new Label(-0.1f, -0.16f, 0.2f, 0.06f, "Last time", Label.NONE);
		add(_labelStats);
		
		_labelMoves = new Label(-0.230f, -0.10f, 0.225f, 0.08f, "", Label.CAP_LEFT);
		_labelMoves.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/step_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_labelMoves);
		
		_labelTime = new Label(0.005f, -0.10f, 0.225f, 0.08f, "", Label.CAP_RIGHT);
		_labelTime.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/clock_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_labelTime);
		
		_buttonRun = new Button(-0.2f, 0.02f, 0.4f, 0.08f, "Run!");
		_buttonRun.addActionListener(new ButtonRunActionListener());
		_buttonRun.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/start_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonRun);
		
		_buttonResume = new Button(-0.2f, 0.12f, 0.4f, 0.08f, "Resume");
		_buttonResume.addActionListener(new ButtonResumeActionListener());
		_buttonResume.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/start_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonResume);
		
		_buttonConfig = new Button(-0.2f, 0.22f, 0.4f, 0.08f, "Config");
		_buttonConfig.addActionListener(new ButtonConfigActionListener());
		_buttonConfig.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/config_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonConfig);
		
		_buttonBack = new Button(-0.2f, 0.32f, 0.4f, 0.08f, "Back");
		_buttonBack.addActionListener(new ButtonBackActionListener());
		_buttonBack.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/back_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonBack);
		
		_labelInfo = new Label(-0.3f, 0.42f, 0.6f, 0.08f, "", Label.NONE);
		add(_labelInfo);
		
		addKeyboardListener(new FrameKeyboardListener());
		
		if ((_levelStatistic == null) || !_levelStatistic.isSaved() || _levelStatistic.isWon())
			_buttonResume.setVisibility(false);
		else
			_buttonResume.setVisibility(true);
		
		displayLevelStatistics();
		
		_zoomTarget = -0.0f;
		_angleXTarget = 90;
		_timebase = System.currentTimeMillis();	
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
	
	private class ButtonRunActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				try {
					LevelManager lm = new LevelManager(_levelSetIdentifier.getId());
					Level level = lm.getLevel(_levelIdentifier);
					
					_player.setCurrentLevelId(_levelIdentifier.getId());
					_player.setCurrentLevelSetId(_levelSetIdentifier.getId());
					
					Frame nextFrame = new GameStartFrame(PlayerFrame.this, _levelSetIdentifier, level, _player, _levelStatistic);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonResumeActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				try {
					
					LevelManager lm = new LevelManager(_levelSetIdentifier.getId());
					Level level = lm.getLevel(_levelIdentifier);
					
					// Spiel laden:
					Savegame savegame = PlayerManager.getInstance().getSavegame(_levelSetIdentifier.getId(), level.getId(), _player.getId());
					
					_player.setCurrentLevelId(_levelIdentifier.getId());
					_player.setCurrentLevelSetId(_levelSetIdentifier.getId());
					
					Frame nextFrame = new GameStartFrame(PlayerFrame.this, _levelSetIdentifier, level, _player, _levelStatistic, savegame);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
			
				} catch (Exception e) {
					_labelInfo.setText("Savegame invalid");
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonConfigActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				
				try {
					_player.setCurrentLevelId(_levelIdentifier.getId());
					_player.setCurrentLevelSetId(_levelSetIdentifier.getId());
					
					Frame nextFrame = new PlayerConfigFrame(PlayerFrame.this, _player);
					PlayerFrame.this.processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (Exception e) {
					_labelInfo.setText("Player configuration invalid");
				}
			}
		}
	}

	private class ButtonBackActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				
				try {
					Frame nextFrame = new StartFrame(PlayerFrame.this);
					PlayerFrame.this.processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (Exception e) {
					// TODO Auto-generated catch block
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
					
					try {
						Frame nextFrame = new StartFrame(PlayerFrame.this);
						PlayerFrame.this.processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					} catch (Exception e) {
						// TODO Auto-generated catch block
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
		
		// Show current level set name:
		_buttonLevelSet.setText(_levelSetIdentifier.getName());
		
		// Load new levels:
		_levelIdentifiers = LevelSetManager.getInstance().getLevelIdentifiers(_levelSetIdentifier.getId());
		_iLevelIdentifier = _levelIdentifiers.listIterator();
		_levelIdentifier = _iLevelIdentifier.next();
		_buttonLevel.setText(_levelIdentifier.getName());
		
		// Load new stats:
		_levelStatistics = PlayerManager.getInstance().getLevelSetStatistic(_levelSetIdentifier.getId(), _player.getId());
		_levelStatistic = _levelStatistics.get(_levelIdentifier.getId());

		if ((_levelStatistic == null) || !_levelStatistic.isSaved() || _levelStatistic.isWon())
			_buttonResume.setVisibility(false);
		else
			_buttonResume.setVisibility(true);
		
		displayLevelStatistics();
	}
	
	public void displayNextLevel() {
		
		// Display next level name:
		if (!_iLevelIdentifier.hasNext()) {
			_iLevelIdentifier = _levelIdentifiers.listIterator();
		}
		_levelIdentifier = _iLevelIdentifier.next();
		_buttonLevel.setText(_levelIdentifier.getName());
		
		displayLevelStatistics();
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
		
		displayLevelStatistics();
	}
	
	public void displayLevelStatistics() {
		
		_levelStatistic = _levelStatistics.get(_levelIdentifier.getId());
		
		if (_levelStatistic == null) { // This level was never played by this player
			_levelStatistic = new LevelStatistic(0, 0, false, false);
		}

		_labelMoves.setText(String.valueOf(_levelStatistic.getMoves()));
		_labelTime.setText(String.valueOf(_levelStatistic.getTime()/1000));
		
		if ((_levelStatistic == null) || !_levelStatistic.isSaved())// || _levelStatistic.isWon())
			_buttonResume.setVisibility(false);
		else {
			_buttonResume.setVisibility(true);
			if (_levelStatistic.isWon()) _buttonResume.setText("Replay");
			else  _buttonResume.setText("Resume");
		}
	}
	
	public void render() {
		renderBackground(_player.getSkin().getSkybox());
		super.render();
	}
}
