package gdi1sokoban.gui;

import java.util.ArrayList;
import java.util.ListIterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import gdi1sokoban.exceptions.IllegalFormatException;
import gdi1sokoban.graphic.Animation;
import gdi1sokoban.graphic.AnimationLoader;
import gdi1sokoban.graphic.base.TextureDescriptor;
import gdi1sokoban.graphic.base.TextureManager;
import gdi1sokoban.gui.event.*;
import gdi1sokoban.logic.*;

public class PlayerConfigFrame extends MenuFrame {

	private Edit _editName;
	
	private Label _labelWorker;
	private Button _buttonWorker;
	private Button _buttonWorkerNext;
	private Button _buttonWorkerPrevious;
	
	private Label _labelSkin;
	private Button _buttonSkin;
	private Button _buttonSkinNext;
	private Button _buttonSkinPrevious;
	
	private Button _buttonDelete;
	private Button _buttonConfirm;
	private Button _buttonDeny;
	
	private Button _buttonApply;
	
	private Label _labelInfo;
	private Button _buttonBack;
	
	private Player _player;
	
	private ArrayList<SkinIdentifier> _skinIdentifiers;
	private SkinIdentifier _skinIdentifier;
	private ListIterator<SkinIdentifier> _iSkinIdentifier;
	
	private String[] _workerUris;
	private Animation _worker;
	private int _iWorker = -1;
	//private TextureManager.Resource _skinPreview;
	
	PlayerConfigFrame(Frame frame, Player player) throws Exception {
		super(frame);
		_player = player;
		initialize();
	}
	
	public void initialize() throws Exception {
		
		// Display next skin name:
		_skinIdentifiers = SkinManager.getInstance().getSkinIdentifiers();
		_iSkinIdentifier = _skinIdentifiers.listIterator();
		_skinIdentifier = null;
		
		// Current skin:
		while (_iSkinIdentifier.hasNext()) {
			_skinIdentifier = _iSkinIdentifier.next();
			if (_skinIdentifier.getId() == _player.getSkin().getId()) {
				break;
			}
		}
		
		_workerUris = new String[] {"res/mesh/worker01.mdl", "res/mesh/worker02.mdl"};
		_worker = _player.getWorker();
		_worker.startAnimation("stand", 0);
		
		// Buttons & event handling:
		_editName = new Edit(-0.25f, -0.42f, 0.5f, 0.08f, _player.getName());
		add(_editName);
		
		_labelWorker = new Label(-0.1f, -0.33f, 0.2f, 0.06f, "Worker", Label.NONE);
		add(_labelWorker);
		
		_buttonWorkerPrevious = new Button(-0.29f, -0.27f, 0.08f, 0.08f, "<", Button.CAP_LEFT);
		_buttonWorkerPrevious.addActionListener(new ButtonWorkerPreviousActionListener());
		add(_buttonWorkerPrevious);
		
		_buttonWorker = new Button(-0.2f, -0.27f, 0.4f, 0.08f, "current", Button.CAP_NONE);
		_buttonWorker.addActionListener(new ButtonWorkerNextActionListener());
		add(_buttonWorker);
		
		_buttonWorkerNext = new Button(0.21f, -0.27f, 0.08f, 0.08f, ">", Button.CAP_RIGHT);
		_buttonWorkerNext.addActionListener(new ButtonWorkerNextActionListener());
		add(_buttonWorkerNext);
		
		_labelSkin = new Label(-0.1f, 0.02f, 0.2f, 0.06f, "Skin", Label.NONE);
		add(_labelSkin);
		
		_buttonSkinPrevious = new Button(-0.29f, 0.08f, 0.08f, 0.08f, "<", Button.CAP_LEFT);
		_buttonSkinPrevious.addActionListener(new ButtonSkinPreviousActionListener());
		add(_buttonSkinPrevious);
		
		_buttonSkin = new Button(-0.2f, 0.08f, 0.4f, 0.08f, _player.getSkin().getName(), Button.CAP_NONE);
		_buttonSkin.addActionListener(new ButtonSkinNextActionListener());
		add(_buttonSkin);
		
		_buttonSkinNext = new Button(0.21f, 0.08f, 0.08f, 0.08f, ">", Button.CAP_RIGHT);
		_buttonSkinNext.addActionListener(new ButtonSkinNextActionListener());
		add(_buttonSkinNext);
		
		_buttonDelete = new Button(-0.2f, 0.19f, 0.4f, 0.08f, "Delete?");
		_buttonDelete.addActionListener(new ButtonDeleteActionListener());
		_buttonDelete.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/delete_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonDelete);
		
		_buttonConfirm = new Button(-0.305f, 0.19f, 0.3f, 0.08f, "Yes");
		_buttonConfirm.addActionListener(new ButtonConfirmDeleteActionListener());
		_buttonConfirm.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/apply_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		_buttonConfirm.setVisibility(false);
		add(_buttonConfirm);
		
		_buttonDeny = new Button(0.005f, 0.19f, 0.3f, 0.08f, "No!");
		_buttonDeny.addActionListener(new ButtonDenyDeleteActionListener());
		_buttonDeny.setVisibility(false);
		add(_buttonDeny);
		
		_buttonApply = new Button(0.005f, 0.32f, 0.395f, 0.08f, "Apply");
		_buttonApply.addActionListener(new ButtonApplyActionListener());
		_buttonApply.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/apply_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonApply);
		
		_buttonBack = new Button(-0.4f, 0.32f, 0.395f, 0.08f, "Back");
		_buttonBack.addActionListener(new ButtonBackActionListener());
		_buttonBack.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/back_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonBack);
		
		_labelInfo = new Label(-0.3f, 0.4f, 0.6f, 0.08f, "", Label.NONE);
		add(_labelInfo);
		
		addKeyboardListener(new FrameKeyboardListener());
		_zoomTarget = 0.2f;
		_angleXTarget = 90;
		_angleYTarget = -30;
		_timebase = System.currentTimeMillis();
	}
	
	private class ButtonWorkerNextActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayNextWorker();
			}
		}
	}
	
	private class ButtonWorkerPreviousActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayPreviousWorker();
			}
		}
	}
	
	private class ButtonSkinNextActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayNextSkin();
			}
		}
	}
	
	private class ButtonSkinPreviousActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayPreviousSkin();
			}
		}
	}
	
	private class ButtonDeleteActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				_buttonDelete.setVisibility(false);
				_buttonConfirm.setVisibility(true);
				_buttonDeny.setVisibility(true);
			}
		}
	}
	
	private class ButtonDenyDeleteActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				_buttonDelete.setVisibility(true);
				_buttonConfirm.setVisibility(false);
				_buttonDeny.setVisibility(false);
			}
		}
	}
	
	private class ButtonConfirmDeleteActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				
				try {
					PlayerManager.getInstance().delPlayer(_player.getId());

					Frame nextFrame = new StartFrame(PlayerConfigFrame.this);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					
				} catch (Exception e) {
					_labelInfo.setText("Unknown error");
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonBackActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				
				try {
					Frame nextFrame = new PlayerFrame(PlayerConfigFrame.this, _player);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					
				} catch (Exception e) {
					_labelInfo.setText("Unknown error");
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonApplyActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				if ((_player.getName().equals( _editName.getText())) || !PlayerManager.getInstance().checkDuplicates(_editName.getText())) {
					try {
						_player.setName(_editName.getText());
						if (_skinIdentifier != null)
							_player.setSkin(SkinManager.getInstance().getSkin(_skinIdentifier));
						
						if (_iWorker != -1)
							_player.setUri(_workerUris[_iWorker]);
						
						PlayerManager.getInstance().setPlayer(_player);
						
						Frame nextFrame = new PlayerFrame(PlayerConfigFrame.this, _player);
						processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					
					} catch (IllegalFormatException e1) {
						_labelInfo.setText("Skin could not be loaded");
					} catch (Exception e) {
						_labelInfo.setText("Unknown error");
						e.printStackTrace();
					}
				}
				else {
					setFocus(_editName, true);
					_labelInfo.setText("Duplicate name: " + _editName.getText());
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
						Frame nextFrame = new PlayerFrame(PlayerConfigFrame.this, _player);
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
	
	private void displayNextWorker() {
		
		// Display next worker name
		if (_iWorker == -1) _iWorker = 0;
		else if (_iWorker == 0) _iWorker = 1;
		else _iWorker = 0;
		
		try {
			_worker = AnimationLoader.load(_workerUris[_iWorker]);
			_worker.startAnimation("move", System.currentTimeMillis());
		} catch (Exception e) {
			_labelInfo.setText("Error loading worker");
		}
		_buttonWorker.setText("Worker " + _iWorker);
	}
	
	private void displayPreviousWorker() {
		
		// Display next worker name
		if (_iWorker == -1) _iWorker = 1;
		else if (_iWorker == 1) _iWorker = 0;
		else _iWorker = 1;
		
		try {
			_worker = AnimationLoader.load(_workerUris[_iWorker]);
			_worker.startAnimation("move", System.currentTimeMillis());
		} catch (Exception e) {
			_labelInfo.setText("Error loading worker");
		}
		_buttonWorker.setText("Worker " + _iWorker);
	}
	
	private void displayNextSkin() {
		
		// Display next skin name
		if (!_iSkinIdentifier.hasNext()) {
			_iSkinIdentifier = _skinIdentifiers.listIterator();
		}
		_skinIdentifier = _iSkinIdentifier.next();
		_buttonSkin.setText(_skinIdentifier.getName());
	}
	
	private void displayPreviousSkin() {
		
		// Display previous skin name
		if (!_iSkinIdentifier.hasPrevious()) {
			_iSkinIdentifier = _skinIdentifiers.listIterator(_skinIdentifiers.size());
		}
		_iSkinIdentifier.previous();
		
		if (!_iSkinIdentifier.hasPrevious()) {
			_iSkinIdentifier = _skinIdentifiers.listIterator(_skinIdentifiers.size());
		}
		_skinIdentifier = _iSkinIdentifier.previous();
		
		_iSkinIdentifier.next();
		
		_buttonSkin.setText(_skinIdentifier.getName());
	}
	
	public void render() {
		
		renderBackground(_player.getSkin().getSkybox());
		 
		// Prepare rendering:
		GL11.glMatrixMode (GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		
		// Render the icon to a quad:
		GL11.glLoadIdentity();
		GL11.glScaled(0.15f, 0.15f, 0.15f);
		GL11.glTranslatef(0, 0.05f, 0);
		
		GL11.glRotatef(210, 1, 0, 0);
		GL11.glRotatef(System.currentTimeMillis()/10 % 360, 0, 1, 0);
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		_worker.render(System.currentTimeMillis());
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPopMatrix();

	    super.render();
	}
}