package gdi1sokoban.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import gdi1sokoban.SoundMaster;
import gdi1sokoban.control.Game;
import gdi1sokoban.gui.event.*;
import gdi1sokoban.graphic.base.*;
import gdi1sokoban.logic.*;
import gdi1sokoban.logic.event.LevelEvent;
import gdi1sokoban.logic.event.LevelListener;

/**
 * The GameFrame is shown during the real game. A Sokoban-level
 * is rendered and keyboard/mouse input is handled.
 */
public class GameFrame extends Frame {
 
	private Button _buttonUndo;
	private Button _buttonRestart;
	private Button _buttonRedo;
	private Button _labelBack;
	private Label _labelPoints;
	private Label _labelTime;
	private Label _labelPlayer;
	
	// State:
	private Game _game;
	private Player _player;
	private LevelSetIdentifier _levelSetIdentifier;
	private Level _level;
	private LevelStatistic _levelStatistic;
	
	private Position _selection;
	private String _input;
	boolean _push;
	static float _angleX, _angleY = 70, _zoom = 30, _mouseX,_mouseY;
	long _startTime;
	boolean _rotate;
	static boolean _freeCam = false;

	GameFrame(Frame frame, Game game, LevelSetIdentifier levelSetIdentifier, Level level, Player player, LevelStatistic levelStatistic, long time) throws Exception {
		super(frame);
		_game = game;
		_player = player;
		_levelSetIdentifier = levelSetIdentifier;
		_levelStatistic = levelStatistic;
		_level = level;
		_level.addListener(new LevelStepsListener());
		_level.addListener(new LevelPushesListener());
		_level.addListener(new LevelSolvedListener());
		_level.addListener(new LevelDeadlockedListener());
		_startTime = System.currentTimeMillis() - time;
		initialize();
	}
	
	void initialize() throws Exception {
		SoundMaster.stopMainMenu();
		//SoundMaster.prefetchPush();
		SoundMaster.playGame();
		
		// Pushmode becomes active when crate is selected:
		_push = false;
		
		// Components & event handling:
		_labelBack = new Button(-0.5f, -0.49f, 0.11f, 0.08f, "", Button.CAP_LEFT);
		_labelBack.addActionListener(new ButtonBackActionListener());
		_labelBack.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/back_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_labelBack);
		
		_labelPlayer = new Label(-0.38f, -0.49f, 0.45f, 0.08f, _level.getName());
		add(_labelPlayer);
		
		_labelPoints = new Label(0.08f, -0.49f, 0.2f, 0.08f, "0");
		_labelPoints.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/step_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_labelPoints);
		
		_labelTime = new Label(0.29f, -0.49f, 0.21f, 0.08f, "0", Label.CAP_RIGHT);
		_labelTime.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/clock_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_labelTime);
		
		_buttonUndo = new Button(-0.16f, 0.41f, 0.1f, 0.08f, "", Label.CAP_LEFT);
		_buttonUndo.addActionListener(new ButtonUndoActionListener());
		_buttonUndo.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/undo_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonUndo);
		
		_buttonRestart = new Button(-0.05f, 0.41f, 0.1f, 0.08f, "", Label.CAP_NONE);
		_buttonRestart.addActionListener(new ButtonRestartActionListener());
		_buttonRestart.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/restart_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonRestart);
		
		_buttonRedo = new Button(0.06f, 0.41f, 0.1f, 0.08f, "", Label.CAP_RIGHT);
		_buttonRedo.addActionListener(new ButtonRedoActionListener());
		_buttonRedo.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/redo_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonRedo);
		
		addMouseListener(new GameFrameMouseMoveListener());
		addMouseListener(new GameFrameMouseClickListener());
		addKeyboardListener(new FrameKeyboardListener());
		setFocus(this, true);
	}
	
	private class ButtonBackActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				SoundMaster.stopGame();
				SoundMaster.playMainMenu();
				
				save();
				
				try {
					Frame nextFrame;
					nextFrame = new PlayerFrame(GameFrame.this, _player);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonUndoActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				undo();
				setFocus(GameFrame.this, true);
			}
		}
	}
	
	private class ButtonRedoActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				redo();
				setFocus(GameFrame.this, true);
			}
		}
	}
	
	private class ButtonRestartActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				_startTime = System.currentTimeMillis();
				restart();
				setFocus(GameFrame.this, true);
			}
		}
	}
	
	private class GameFrameMouseMoveListener implements MouseListener {
		public boolean mouseEvent(MouseEvent event) {
			_showCursor = true;
			
			_selection = _game.pick(event.getX(), event.getY());
			if (_selection == null)  _game.select(null);
			else if (!_rotate) _game.select(_selection);
			
			// Calculate Camera position:
			if (event.getButton() == 1) _rotate = Mouse.getEventButtonState();
			if (event.getType() == MouseEvent.MOUSE_RELEASED) {
				//_game.move(position);
			}
			if (event.getType() == MouseEvent.MOUSE_MOVED) {			
				if (Mouse.isButtonDown(1)) {
					_angleX += (event.getX()-_mouseX)*200;
					if (_angleX < 0) _angleX+=360;
					_angleY += (event.getY()-_mouseY)*200;
					if (_angleY < 3) _angleY = 3;
					if (_angleY >= 89.999f) _angleY = 89.999f;  // Mac OS X compatibility
					
				}
				_mouseX = event.getX();
				_mouseY = event.getY();
				_game.buildCamera(_angleY, _angleX, _zoom);
			}
			
			// Highlight path if not in push mode:
			if ((!_push) && !_rotate) _game.highlightPath(_selection);
			return false;
		}
	}
	
	private class GameFrameMouseClickListener implements MouseListener {
		public boolean mouseEvent(MouseEvent event) {
			if ((event.getType() == MouseEvent.MOUSE_PRESSED) &&
			   (Mouse.getEventButton() == 0) && !_rotate) {
				
				if (_selection != null) {
					_game.select(_selection);
					
					// In push mode, push crate:
					if (_push) {
						if (_game.isMoving()) _game.forward();
						_level.push(_selection);
						_push = false;
						
						// Delete possible highlighed crate path:
						_game.highlightCratePath(null);
					}
					else {
						// Activate push mode if position is crate:
						if (_level.getBoard().isType(_selection, Board.TYPE_CRATE) &&
							_level.getWorker().isNeighbor(_selection)) {
							_push = true;
							
							// Delete possible highlighed path:
							_game.highlightPath(null);
							
							// Highligh crate path:
							_game.highlightCratePath(_selection);
						}
						else {
							if (_game.isMoving()) _game.forward();
							_level.move(_selection);
						}
					}
				}
			}
			return false;
		}
	}
	
	private class FrameKeyboardListener implements KeyboardListener {
		public boolean keyboardEvent(KeyboardEvent event) {
			if (event.getState()) {
				switch (event.getCode()) {
				case Keyboard.KEY_ESCAPE :
					if (event.getState()) {
						try {
							SoundMaster.stopGame();
							SoundMaster.playMainMenu();
							
							save();
							
							Frame nextFrame;
							nextFrame = new PlayerFrame(GameFrame.this, _player);
							processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
					
				case Keyboard.KEY_W : 
				case Keyboard.KEY_ADD : 
				case Keyboard.KEY_EQUALS :
					if (!_rotate && (_zoom > 1)) _zoom /= 1.08f;
					break;
					
				case Keyboard.KEY_S : 
				case Keyboard.KEY_MINUS : 
				case Keyboard.KEY_SUBTRACT : 
					if (!_rotate && (_zoom < 100)) _zoom *= 1.08f;
					break;
					
				case Keyboard.KEY_C :
					_freeCam = !_freeCam;
					break;
					
				case Keyboard.KEY_Q : 
					_angleX-=90;
					if (_angleX < 0) _angleX+=360;
					break;
					
				case Keyboard.KEY_E : 
					_angleX+=90;
					break;
					
				case Keyboard.KEY_BACK :
				case Keyboard.KEY_Z : 
				case Keyboard.KEY_U :
					undo();
					break;
					
				case Keyboard.KEY_Y : 
				case Keyboard.KEY_R :
					redo();
					break;
					
				case Keyboard.KEY_N :
					_startTime = System.currentTimeMillis();
					_push = false;
					_game.highlightCratePath(null);
					if (!_rotate) _game.highlightPath(_selection);
					restart();
					break;
					
				case Keyboard.KEY_LEFT:
					move(Position.LEFT);
					break;
				
				case Keyboard.KEY_RIGHT:
					move(Position.RIGHT);
					break;
					
				case Keyboard.KEY_UP:
					move(Position.TOP);
					break;
					
				case Keyboard.KEY_DOWN:
					move(Position.BOTTOM);
					break;
				}
				
				if ((event.getCode() != Keyboard.KEY_RSHIFT)&&(event.getCode() != Keyboard.KEY_LSHIFT)) {
					if (event.getCharacter() == '#') {
						if (_input.matches(".*osj[0-9]{1,4},[0-9]{1,4}")) {
							SoundMaster.playCheat();
							String[] _limiters = _input.split("[0-9]{1,4}");
							int start2 = _input.lastIndexOf(_limiters[_limiters.length - 1]) + _limiters[_limiters.length - 1].length();
							int start1 = _input.lastIndexOf(_limiters[_limiters.length - 2]) + _limiters[_limiters.length - 2].length();
							int coordX = (Integer.parseInt(_input.substring(start1, start2-1)));
							int coordY = (Integer.parseInt(_input.substring(start2)));
							_level.cheat(new Position(coordX, coordY));
							_push = false;
							if (!_rotate) _game.highlightPath(_selection);
							_game.highlightCratePath(null);
						}
					}
					else
						_input += event.getCharacter();
					
					if (_input.length() == 15)
						_input = _input.substring(1, 15);
				}
			}
			return false;
		}
	}
	
	private class LevelStepsListener implements LevelListener {
		public void event(LevelEvent event) {
			if (event.isType(Level.EVENT_STEPS)) {
				SoundMaster.playMove();
			}
		}
	}
	
	private class LevelPushesListener implements LevelListener {
		public void event(LevelEvent event) {
			if (event.isType(Level.EVENT_PUSHES)) {
				SoundMaster.playPush();
			}
		}
	}
	
	private class LevelSolvedListener implements LevelListener {
		public void event(LevelEvent event) {
			if (event.isType(Level.EVENT_SOLVED)) {
				SoundMaster.playLevelSolved();
				SoundMaster.stopGame();
				
				try {
					Frame nextFrame;
					nextFrame = new GameEndFrame(GameFrame.this, _game, _levelSetIdentifier, _level, _player, System.currentTimeMillis() - _startTime);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class LevelDeadlockedListener implements LevelListener {
		public void event(LevelEvent event) {
			if (event.isType(Level.EVENT_DEADLOCK)) {
				SoundMaster.playDeadlock();
			}
		}
	}
	
	private void save() {
		// Spiel speichern:
		
		Savegame savegame = new Savegame(_level.getMoves(), (System.currentTimeMillis() - _startTime), false);
		PlayerManager.getInstance().setSavegame(_player,_levelSetIdentifier.getId(), _level.getId(), _player.getId(), savegame);
		
		_levelStatistic.setWon(false);
		_levelStatistic.setSaved(true);
		
		PlayerManager.getInstance().addLevelStatistic(_player,_levelStatistic, _level.getId(), _levelSetIdentifier.getId(), _player.getId());
	}
	
	private void undo() {
		if (_level.canUndo()) {
			if (_game.isMoving()) _game.forward();
			_level.undo();
			SoundMaster.playMove();
			_push = false;
			if (_game.isSelected()) _game.highlightPath(_selection);
			_game.highlightCratePath(null);
		}
	}
	
	private void redo() {
		if (_level.canRedo()) {
			if (_game.isMoving()) _game.forward();
			_level.redo();
			SoundMaster.playMove();
			_push = false;
			if (_game.isSelected()) _game.highlightPath(_selection);
			_game.highlightCratePath(null);
		}
	}
	
	private void restart() {
		_level.rewind();
		_level.setMoves(null);
		_game.forward();
		_game.highlightPath(_selection);
		_game.highlightCratePath(null);
	}
	
	public void move(int direction) {
		_angleX %= 360f;
		
		float fdir = _angleX;
		if ((fdir >= 315) || (fdir < 45)) direction += 0;
		else if ((fdir >= 45) && (fdir < 135)) direction += 1;
		else if ((fdir >= 135) && (fdir < 225)) direction += 2;
		else  direction += 3;
		direction %= 4;
		
		if (_game.isMoving()) _game.forward();
		_push = false;
		_showCursor = false;
		_game.select(null);
		_game.highlightPath(null);
		_game.highlightCratePath(null);
		
		if (!_level.move(_level.getWorker().neighbor(direction)))
			_level.push(_level.getWorker().neighbor(direction).neighbor(direction));
	}

	public void render() {

		if (!_rotate && !_freeCam) {
			int min = (int)(_angleX / 90) * 90;
			int max = min + 90;
			float add = (_angleX - min - 45) / 100.0f;
		
			if (add < 0)
				if ((_angleX-=2) > min) _angleX-=2;
				else _angleX = min;
			else 
				if ((_angleX+=2) < max) _angleX+=2;
				else _angleX = max;
		}

		_labelPoints.setText(String.valueOf(_level.getStepCount()));
		_labelTime.setText(String.valueOf((System.currentTimeMillis() - _startTime) / 1000));
		_game.buildCamera(_angleY, _angleX, _zoom);
		_game.render(System.currentTimeMillis());

		super.render();
	}
}