package gdi1sokoban.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import gdi1sokoban.SoundMaster;
import gdi1sokoban.control.Game;
import gdi1sokoban.graphic.base.TextureDescriptor;
import gdi1sokoban.graphic.base.TextureManager;
import gdi1sokoban.gui.event.ActionEvent;
import gdi1sokoban.gui.event.ActionListener;
import gdi1sokoban.gui.event.FrameEvent;
import gdi1sokoban.gui.event.KeyboardEvent;
import gdi1sokoban.gui.event.KeyboardListener;
import gdi1sokoban.logic.Highscore;
import gdi1sokoban.logic.Level;
import gdi1sokoban.logic.LevelIdentifier;
import gdi1sokoban.logic.LevelManager;
import gdi1sokoban.logic.LevelSetIdentifier;
import gdi1sokoban.logic.LevelSetManager;
import gdi1sokoban.logic.LevelStatistic;
import gdi1sokoban.logic.Player;
import gdi1sokoban.logic.PlayerManager;
import gdi1sokoban.logic.Savegame;

public class GameEndFrame extends Frame {

	private Button _buttonBack;
	private Button _buttonNext;
	private Button _buttonRepeat;

	private Label _labelStats;
	private Label _labelMoves;
	private Label _labelTime;
	
	private Label _labelStatsNow;
	private Label _labelMovesNow;
	private Label _labelTimeNow;

	private HashMap<Integer, LevelStatistic> _levelStatistics;
	private LevelSetIdentifier _levelSetIdentifier;
	private LevelStatistic _levelStatisticOld;
	private LevelStatistic _levelStatistic;
	private Level _level;
	private Player _player;
	private Game _game;
	
	private LevelIdentifier _levelIdentifier;
	
	public GameEndFrame(Frame frame, Game game, LevelSetIdentifier levelSetIdentifier, Level level, Player player, long time) throws Exception {
		super(frame);
		_level = level;
		_levelSetIdentifier = levelSetIdentifier;
		_player = player;
		_game = game;
		
		// Load old statistics:
		_levelStatistics = PlayerManager.getInstance().getLevelSetStatistic(_levelSetIdentifier.getId(), _player.getId());
		
		_levelStatisticOld = _levelStatistics.get(_level.getId());
		if (_levelStatisticOld == null) {
			_levelStatisticOld = new LevelStatistic(0, 0, false, false);
		}
		
		// Save actual statistics:
		_levelStatistic = new LevelStatistic(_level.getStepCount(), time, true, true);
		PlayerManager.getInstance().addLevelStatistic(_player, _levelStatistic, _level.getId(), _levelSetIdentifier.getId(), _player.getId());
		
		// Spiel speichern:
		Savegame savegame = new Savegame(_level.getMoves(), time, true);
		PlayerManager.getInstance().setSavegame(_player, _levelSetIdentifier.getId(), _level.getId(), _player.getId(), savegame);
		
		// Highscore:
		Highscore highscore = new Highscore(_player.getId(), _level.getStepCount(), time);
		LevelSetManager.getInstance().addHighScore(_levelSetIdentifier.getId(), _level.getId(), highscore);
		
		ArrayList<LevelIdentifier> levelIdentifiers = LevelSetManager.getInstance().getLevelIdentifiers(_levelSetIdentifier.getId());
		Iterator<LevelIdentifier> i = levelIdentifiers.iterator();
		while (i.hasNext()) {
			LevelIdentifier li = i.next();
			if (li.getId() == _level.getId()) {
				if (i.hasNext())
					_levelIdentifier = i.next();
				else _levelIdentifier = null;
			}
		}
		
		// Statistics:
		_labelStats = new Label(-0.1f, -0.36f, 0.2f, 0.06f, "Last time", Label.NONE);
		add(_labelStats);
		
		_labelMoves = new Label(-0.230f, -0.30f, 0.225f, 0.08f, String.valueOf(_levelStatisticOld.getMoves()), Label.CAP_LEFT);
		_labelMoves.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/step_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_labelMoves);
		
		_labelTime = new Label(0.005f, -0.30f, 0.225f, 0.08f, String.valueOf(_levelStatisticOld.getTime()/1000), Label.CAP_RIGHT);
		_labelTime.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/clock_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_labelTime);
		
		// Statistics now:
		_labelStatsNow = new Label(-0.1f, 0.14f, 0.2f, 0.06f, "This time", Label.NONE);
		add(_labelStatsNow);
		
		_labelMovesNow = new Label(-0.230f, 0.20f, 0.225f, 0.08f,  String.valueOf(_levelStatistic.getMoves()), Label.CAP_LEFT);
		_labelMovesNow.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/step_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_labelMovesNow);
		
		_labelTimeNow = new Label(0.005f, 0.20f, 0.225f, 0.08f,  String.valueOf(_levelStatistic.getTime()/1000), Label.CAP_RIGHT);
		_labelTimeNow.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/clock_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_labelTimeNow);
		
		_buttonBack = new Button(-0.5f, 0.35f, 0.3f, 0.08f, "End");
		_buttonBack.addActionListener(new ButtonBackActionListener());
		add(_buttonBack);
		
		_buttonRepeat = new Button(-0.15f, 0.35f, 0.3f, 0.08f, "Repeat");
		_buttonRepeat.addActionListener(new ButtonRepeatActionListener());
		add(_buttonRepeat);
		
		if (_levelIdentifier != null) {
			_buttonNext = new Button(0.2f, 0.35f, 0.3f, 0.08f, "Next");
			_buttonNext.addActionListener(new ButtonNextActionListener());
			add(_buttonNext);
			setFocus(_buttonNext, true);
		}
		else setFocus(_buttonRepeat, true);
		
		addKeyboardListener(new FrameKeyboardListener());
	}
	
	private class ButtonNextActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				try {
					LevelManager lm = new LevelManager(_levelSetIdentifier.getId());
					Level level = lm.getLevel(_levelIdentifier);
			
					_levelStatistic = _levelStatistics.get(_levelIdentifier.getId());
					if (_levelStatistic == null) { // This level was never played by this player
						_levelStatistic = new LevelStatistic(0, 0, false, false);
					}
					
					Frame nextFrame = new GameStartFrame(GameEndFrame.this, _levelSetIdentifier, level, _player, _levelStatistic);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonRepeatActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				try {
					LevelManager lm = new LevelManager(_levelSetIdentifier.getId());
					Level level = lm.getLevel(_level);
			
					Frame nextFrame = new GameStartFrame(GameEndFrame.this, _levelSetIdentifier, level, _player, _levelStatistic);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonBackActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				SoundMaster.playMainMenu();
				try {
					Frame nextFrame = new PlayerFrame(GameEndFrame.this, _player);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					
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
						Frame nextFrame = new PlayerFrame(GameEndFrame.this, _player);
						processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return false;
		}
	}
	
	static float x=0, y=0;
	long timebase = System.currentTimeMillis();
	public void render() {
		_game.render(System.currentTimeMillis());
		x = (System.currentTimeMillis() - timebase)/100.0f;
		_game.buildCamera(50, x, 40);
		super.render();
	}
}
