package gdi1sokoban.gui;

import java.util.ArrayList;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import gdi1sokoban.SoundMaster;
import gdi1sokoban.exceptions.DuplicateException;
import gdi1sokoban.gui.event.*;
import gdi1sokoban.graphic.base.*;
import gdi1sokoban.logic.*;

public class StartFrame extends MenuFrame {

	private Button _buttonPlayer;
	private Edit _editNewPlayer;
	private Button _buttonPlayerNext;
	private Button _buttonPlayerPrevious;
	
	private Button _buttonStart;
	private Button _buttonConfig;
	private Button _buttonHighscore;
	private Button _buttonAbout;
	private Button _buttonExit;
	private Label _labelInfo;
	
	private static ArrayList<PlayerIdentifier> _playerIdentifiers;
	private ListIterator<PlayerIdentifier> _iPlayerIdentifier;
	private PlayerIdentifier _playerIdentifier;
	private boolean _hasPlayer;
	private float _titleWidth;
	
	private TextureManager.Resource _title;
	
	StartFrame(Frame frame) throws Exception {
		super(frame);
		initialize();
	}
	
	public StartFrame(float x, float y, float width, float height) throws Exception {
		super(x, y, width, height);
		initialize();
	}
	
	public void initialize() throws Exception {
		
		SoundMaster.playMainMenu();
		_title = TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/title.png", GL11.GL_LINEAR, GL11.GL_LINEAR));
		_titleWidth = _title.getInstance().getImageWidth()/(float)_title.getInstance().getImageHeight()* getHeight() ;
		
		// Loading the player list from disk:
		_playerIdentifiers = PlayerManager.getInstance().getPlayerIdentifiers();
		_iPlayerIdentifier = _playerIdentifiers.listIterator();
		_hasPlayer = _iPlayerIdentifier.hasNext();
		if (_hasPlayer) _playerIdentifier = _iPlayerIdentifier.next();
		
		// Components & event handling:
		if (_hasPlayer) {
			_buttonPlayerPrevious = new Button(-0.34f, -0.2f, 0.08f, 0.08f, "<", Button.CAP_LEFT);
			_buttonPlayerPrevious.addActionListener(new ButtonPlayerPreviousActionListener());
			add(_buttonPlayerPrevious);
		
			_buttonPlayer = new Button(-0.25f, -0.2f, 0.5f, 0.08f, _playerIdentifier.getName(), Button.CAP_NONE);
			_buttonPlayer.addActionListener(new ButtonPlayerNextActionListener());
			add(_buttonPlayer);
			
			_buttonPlayerNext = new Button(0.26f, -0.2f, 0.08f, 0.08f, ">", Button.CAP_RIGHT);
			_buttonPlayerNext.addActionListener(new ButtonPlayerNextActionListener());
			add(_buttonPlayerNext);
		}
		
		if (_hasPlayer) {
			_editNewPlayer = new Edit(-0.25f, -0.2f, 0.5f, 0.08f, "new Player", Button.CAP_NONE);
			_editNewPlayer.addActionListener(new ButtonPlayerNextActionListener());
			_editNewPlayer.setVisibility(false);
		}
		else {
			_editNewPlayer = new Edit(-0.25f, -0.2f, 0.5f, 0.08f, "new Player", Button.CAP_BOTH);
			_editNewPlayer.setVisibility(true);
		}
		add(_editNewPlayer);
		
		_buttonStart = new Button(-0.20f, -0.08f, 0.4f, 0.08f, "Start");
		_buttonStart.addActionListener(new ButtonStartActionListener());
		_buttonStart.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/start_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonStart);
		
		_buttonHighscore = new Button(-0.20f, 0.02f, 0.4f, 0.08f, "Top 10");
		_buttonHighscore.addActionListener(new ButtonHighscoreActionListener());
		_buttonHighscore.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/score_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonHighscore);
		
		_buttonConfig = new Button(-0.20f, 0.12f, 0.4f, 0.08f, "Config");
		_buttonConfig.addActionListener(new ButtonConfigActionListener());
		_buttonConfig.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/config_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonConfig);
		
		_buttonAbout = new Button(-0.20f, 0.22f, 0.4f, 0.08f, "Credits");
		_buttonAbout.addActionListener(new ButtonAboutActionListener());
		_buttonAbout.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/credits_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonAbout);
		
		_buttonExit = new Button(-0.20f, 0.32f, 0.4f, 0.08f, "Exit");
		_buttonExit.addActionListener(new ButtonExitActionListener());
		_buttonExit.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/exit_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonExit);
		
		_labelInfo = new Label(-0.20f, 0.42f, 0.4f, 0.08f, "", Label.NONE);
		add(_labelInfo);
		
		addKeyboardListener(new FrameKeyboardListener());
		_zoomTarget = 0.0f;
	}

	private class ButtonPlayerNextActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayNextPlayer();
			}
		}
	}
	
	private class ButtonPlayerPreviousActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayPreviousPlayer();
			}
		}
	}
	
	private class ButtonStartActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {

				try {
					// Load / create player:
					Player player = null;
					if (_editNewPlayer.isVisible()) {
						int playerId = PlayerManager.getInstance().addPlayer(_editNewPlayer.getText());
						player = PlayerManager.getInstance().getPlayer(playerId);
					}
					else  {
						player = PlayerManager.getInstance().getPlayer(_playerIdentifier.getId());
					}
					
					Frame nextFrame = new PlayerFrame(StartFrame.this, player);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (DuplicateException e) {
					setFocus(_editNewPlayer, true);
					_labelInfo.setText("Duplicate name: " + _editNewPlayer.getText());
				} catch (Exception e) {
					_labelInfo.setText("Error loading player");
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonHighscoreActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				Frame nextFrame;
				try {
					SoundMaster.stopMainMenu();
					nextFrame = new HighscoreFrame(StartFrame.this);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (Exception e) {
					_labelInfo.setText("Error loading highscores");
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonConfigActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				Frame nextFrame;
				try {
					
					nextFrame = new ConfigFrame(StartFrame.this);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (Exception e) {
					_labelInfo.setText("Error loading configuration");
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonAboutActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				Frame nextFrame;
				try {
					SoundMaster.stopMainMenu();
					nextFrame = new CreditFrame(StartFrame.this);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (Exception e) {
					_labelInfo.setText("Error loading credits");
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonExitActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				StartFrame.this.processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, null));
			}
		}
	}

	private class FrameKeyboardListener implements KeyboardListener {
		public boolean keyboardEvent(KeyboardEvent event) {
			switch (event.getCode()) {
			case Keyboard.KEY_ESCAPE :
				if (event.getState()) StartFrame.this.processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, null));
			}
			return false;
		}
	}
	
	private void displayNextPlayer() {
		
		// Display next player name if one exists, otherwise show
		// new player edit:
		if (_buttonPlayer.isVisible()) {
			
			if (_iPlayerIdentifier.hasNext()) {
				_playerIdentifier = _iPlayerIdentifier.next();
				_buttonPlayer.setText(_playerIdentifier.getName());
			}
			else {
				_buttonPlayer.setVisibility(false);
				_editNewPlayer.setVisibility(true);
				setFocus(_editNewPlayer, true);
			}
		}
		else {
			// Exit editing mode and show first available player name:
			_iPlayerIdentifier = _playerIdentifiers.listIterator();
			_playerIdentifier = _iPlayerIdentifier.next();
			_buttonPlayer.setText(_playerIdentifier.getName());
			_buttonPlayer.setVisibility(true);
			_editNewPlayer.setVisibility(false);
			setFocus(_buttonPlayer, true);
		}
	}
	
	private void displayPreviousPlayer() {
		
		// Display next player name if one exists, otherwise show
		// new player edit:
		if (_buttonPlayer.isVisible()) {
			
			if (_iPlayerIdentifier.hasPrevious()) {
				_iPlayerIdentifier.previous();
				
				if (_iPlayerIdentifier.hasPrevious()) {
					_playerIdentifier = _iPlayerIdentifier.previous();
					_iPlayerIdentifier.next();
					_buttonPlayer.setText(_playerIdentifier.getName());
				}
				else {
					_buttonPlayer.setVisibility(false);
					_editNewPlayer.setVisibility(true);
					setFocus(_editNewPlayer, true);
				}
				_buttonPlayer.setText(_playerIdentifier.getName());
			}
			else {
				_buttonPlayer.setVisibility(false);
				_editNewPlayer.setVisibility(true);
				setFocus(_editNewPlayer, true);
			}
		}
		else {
			// Exit editing mode and show first available player name:
			_iPlayerIdentifier = _playerIdentifiers.listIterator(_playerIdentifiers.size());
			_playerIdentifier = _iPlayerIdentifier.previous();
			_iPlayerIdentifier.next();
			_buttonPlayer.setText(_playerIdentifier.getName());
			_buttonPlayer.setVisibility(true);
			_editNewPlayer.setVisibility(false);
			setFocus(_buttonPlayer, true);
		}
	}
	
	public void render() {

		renderBackground();
		
		_title.getInstance().bind();
		
		float titleAnim = 0.4f*(1 / (float)(1 + 0.01f*(System.currentTimeMillis() - _timebase)));
		
		GL11.glBegin(GL11.GL_QUADS); // Ein Viereck zeichnen
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(-0.250f, -0.41f - titleAnim, 0); // oben links
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(-0.250f, -0.21f - titleAnim*0.8f, 0); // oben rechts
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(-0.250f + _titleWidth*0.2f, -0.21f - titleAnim*1.3f, 0); // unten rechts
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(-0.250f + _titleWidth*0.2f, -0.41f - titleAnim*1.5f, 0); // unten links
		GL11.glEnd();

	    super.render();
	}
}