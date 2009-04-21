package gdi1sokoban.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.event.ListSelectionEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import gdi1sokoban.SoundMaster;
import gdi1sokoban.exceptions.DuplicateException;
import gdi1sokoban.gui.event.*;
import gdi1sokoban.graphic.base.*;
import gdi1sokoban.logic.*;

public class ConfigFrame extends MenuFrame {

	private Label _labelResolution;
	private Button _buttonResolution;
	private Button _buttonResolutionNext;
	private Button _buttonResolutionPrevious;
	
	private Label _labelReflections;
	private Button _buttonReflections;
	
	private Button _buttonImport;
	
	private Label _labelFullscreen;
	private Button _buttonFullscreen;
	
	private Label _labelSound;
	private Button _buttonSound;

	private Label _labelInfo;
	private Button _buttonApply;
	private Button _buttonBack;
	
	GlobalConfig _globalConfig;
	ArrayList<DisplayMode> _modes;
	int _iMode;
	
	ConfigFrame(Frame frame) throws Exception {
		super(frame);
		initialize();
	}
	
	public ConfigFrame(float x, float y, float width, float height) throws Exception {
		super(x, y, width, height);
		initialize();
	}
	
	class ModeComparator implements Comparator<DisplayMode> {
		public int compare(DisplayMode arg0, DisplayMode arg1) {
			
			int comp = arg0.getFrequency() - arg1.getFrequency();
			
			if (comp == 0)
				comp = arg0.getBitsPerPixel() - arg1.getBitsPerPixel();
			
			if (comp == 0)
				comp = arg0.getWidth() - arg1.getWidth();
			
			if (comp == 0)
				comp = arg0.getHeight() - arg1.getHeight();
			
			return -comp;
		}
	}
	
	public void initialize() throws Exception {

		_globalConfig = ConfigManager.getInstance().getGlobalConfig();
		_modes = new ArrayList<DisplayMode>(Arrays.asList(Display.getAvailableDisplayModes()));
		Collections.sort(_modes, new ModeComparator());
		_iMode = -1;
		
		// Current resolution:
		for (int i = 0; i < _modes.size(); i++) {
			if (GlobalConfig.DisplayModeToString(_modes.get(i)).equals(_globalConfig.getResolution())) {
				_iMode = i;
				break;
			}
		}
		
		// Buttons & event handling:
		//_buttonImport = new Button(-0.2f, -0.45f, 0.4f, 0.08f, "Import Levelsets");
		//_buttonImport.addActionListener(new ButtonImportActionListener());
		//add(_buttonImport);
		
		// Resolution:
		_labelResolution = new Label(-0.1f, -0.36f, 0.2f, 0.06f, "Resolution*", Label.NONE);
		add(_labelResolution);
		
		_buttonResolutionPrevious = new Button(-0.49f, -0.3f, 0.08f, 0.08f, "<", Button.CAP_LEFT);
		_buttonResolutionPrevious.addActionListener(new ButtonResolutionPreviousActionListener());
		add(_buttonResolutionPrevious);
		
		_buttonResolution = new Button(-0.4f, -0.3f, 0.8f, 0.08f, _globalConfig.getResolution(), Button.CAP_NONE);
		_buttonResolution.addActionListener(new ButtonResolutionNextActionListener());
		add(_buttonResolution);
		
		_buttonResolutionNext = new Button(0.41f, -0.3f, 0.08f, 0.08f, ">", Button.CAP_RIGHT);
		_buttonResolutionNext.addActionListener(new ButtonResolutionNextActionListener());
		add(_buttonResolutionNext);
		
		_labelReflections = new Label(-0.1f, -0.21f, 0.2f, 0.06f, "Reflections", Label.NONE);
		add(_labelReflections);
		
		_buttonReflections = new Button(-0.2f, -0.15f, 0.4f, 0.08f, _globalConfig.isReflections() ? "On" : "Off");
		_buttonReflections.addActionListener(new ButtonReflectionsActionListener());
		add(_buttonReflections);
		
		_labelFullscreen = new Label(-0.1f, -0.06f, 0.2f, 0.06f, "Fullscreen*", Label.NONE);
		add(_labelFullscreen);
		
		_buttonFullscreen = new Button(-0.2f, 0.0f, 0.4f, 0.08f, _globalConfig.isFullscreen() ? "On" : "Off");
		_buttonFullscreen.addActionListener(new ButtonFullscreenActionListener());
		add(_buttonFullscreen);
		
		_labelSound = new Label(-0.1f, 0.09f, 0.2f, 0.06f, "Sound", Label.NONE);
		add(_labelSound);
		
		_buttonSound = new Button(-0.2f, 0.15f, 0.4f, 0.08f, _globalConfig.isSound() ? "On" : "Off");
		_buttonSound.addActionListener(new ButtonSoundActionListener());
		add(_buttonSound);
		
		_buttonBack = new Button(-0.4f, 0.3f, 0.395f, 0.08f, "Back");
		_buttonBack.addActionListener(new ButtonBackActionListener());
		_buttonBack.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/back_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonBack);
		
		_buttonApply = new Button(0.005f, 0.3f, 0.395f, 0.08f, "Apply");
		_buttonApply.addActionListener(new ButtonApplyActionListener());
		_buttonApply.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/apply_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonApply);
		
		_labelInfo = new Label(-0.3f, 0.4f, 0.6f, 0.08f, "*changes require restart", Label.NONE);
		add(_labelInfo);
		
		addKeyboardListener(new FrameKeyboardListener());
		_zoomTarget = 0.0f;
		_angleXTarget = -90;
		_timebase = System.currentTimeMillis();
	}
	
	private class ButtonImportActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				
				try {
					int levelSetId = LevelSetManager.getInstance().addLevelSetIdentifier("Neues Set", "");
					LevelManager lm = new LevelManager(levelSetId);
					lm.addLevel("Level 1", "");
						
				} catch (DuplicateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
	private class ButtonResolutionNextActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayNextResolution();
			}
		}
	}
	
	private class ButtonResolutionPreviousActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				displayPreviousResolution();
			}
		}
	}
	
	private class ButtonReflectionsActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				
				_globalConfig.setReflections(!_globalConfig.isReflections());
				_buttonReflections.setText(_globalConfig.isReflections() ? "On" : "Off");
			}
		}
	}
	
	private class ButtonFullscreenActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				
				_globalConfig.setFullscreen(!_globalConfig.isFullscreen());
				_buttonFullscreen.setText(_globalConfig.isFullscreen() ? "On" : "Off");
			}
		}
	}
	
	private class ButtonSoundActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				
				_globalConfig.setSound(!_globalConfig.isSound());
				_buttonSound.setText(_globalConfig.isSound() ? "On" : "Off");
			}
		}
	}
	
	private class ButtonApplyActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				
				/*DisplayMode current = Display.getDisplayMode();
				
				try {
					
					// Recreate display:
					Display.destroy();
					if (_iMode != -1) {
						Display.setDisplayMode(_modes[_iMode]);
					}
					else Display.setDisplayMode(current);
					Display.setFullscreen(_globalConfig.isFullscreen());
					Display.create(new PixelFormat(8, 16, 8));
					
					// Standard OpenGL-State:
					GL11.glEnable(GL11.GL_CULL_FACE);
					GL11.glCullFace(GL11.GL_BACK);
					
					// Setting up projection matrix:
					// The coordinate-system has its center (0,0) in the middle point of the
					// frame; a minimum extend of 0.5 in all directions is guaranteed.
					GL11.glMatrixMode(GL11.GL_PROJECTION);
					GL11.glLoadIdentity();
					
					float maxSize = Math.min(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
					float width = Display.getDisplayMode().getWidth() / (maxSize * 2);
					float height = Display.getDisplayMode().getHeight() / (maxSize * 2);
					
					GL11.glOrtho(-width, width, height, -height, -1.0f, 1.0f);
					
					Component.setResolution(maxSize);
					
					// Flush resource managers:
					TextureManager.getInstance().flush();
					ModelManager.getInstance().flush();
					FontManager.getInstance().flush();
				
					Display.setFullscreen(_globalConfig.isFullscreen());
					Display.makeCurrent();
					Display.update();
					//ConfigManager.saveGlobalConfig(_globalConfig);
					
					// Goto next frame:
					Frame nextFrame = new StartFrame(ConfigFrame.this);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (Exception e) {
					
				try {
					// Create display:
					Display.destroy();
					Display.setDisplayMode(current);
					Display.create(new PixelFormat(8, 16, 8));
					
					// Standard OpenGL-State:
					GL11.glEnable(GL11.GL_CULL_FACE);
					GL11.glCullFace(GL11.GL_BACK);
					
					// Setting up projection matrix:
					// The coordinate-system has its center (0,0) in the middle point of the
					// frame; a minimum extend of 0.5 in all directions is guaranteed.
					GL11.glMatrixMode(GL11.GL_PROJECTION);
					GL11.glLoadIdentity();
					
					float maxSize = Math.min(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
					float width = Display.getDisplayMode().getWidth() / (maxSize * 2);
					float height = Display.getDisplayMode().getHeight() / (maxSize * 2);
					
					GL11.glOrtho(-width, width, height, -height, -1.0f, 1.0f);
					
					Component.setResolution(maxSize);
					
					// Flush resource managers:
					TextureManager.getInstance().flush();
					ModelManager.getInstance().flush();
					FontManager.getInstance().flush();
				
					Display.makeCurrent();
					Display.update();
					
					// Goto next frame:
					Frame nextFrame = new StartFrame(ConfigFrame.this);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				}catch(Exception e1) {}
				}*/
				ConfigManager.getInstance().saveGlobalConfig(_globalConfig);
				
				try {
					SoundMaster.stopMainMenu();
					SoundMaster.playMainMenu();
					Frame nextFrame = new StartFrame(ConfigFrame.this);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonBackActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				
				try {
					Frame nextFrame = new StartFrame(ConfigFrame.this);
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
						Frame nextFrame = new StartFrame(ConfigFrame.this);
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
	
	private void displayNextResolution() {
		
		if (_iMode == -1) {
			if (_modes.size() > 0) _iMode = _modes.size() - 1;
		}
		else if (_iMode - 1 > 0) {
			_iMode--;
		}
		else _iMode = _modes.size() - 1;
		
		_globalConfig.setResolution(GlobalConfig.DisplayModeToString(_modes.get(_iMode)));
		_buttonResolution.setText(GlobalConfig.DisplayModeToString(_modes.get(_iMode)));
	}
	
	private void displayPreviousResolution() {
		
		if (_iMode == -1) {
			if (_modes.size() > 0) _iMode = 0;
		}
		else if (_iMode + 1 < _modes.size()) {
			_iMode++;
		}
		else _iMode =  0;
		
		_globalConfig.setResolution(GlobalConfig.DisplayModeToString(_modes.get(_iMode)));
		_buttonResolution.setText(GlobalConfig.DisplayModeToString(_modes.get(_iMode)));
	}
	
	public void render() {
		
		renderBackground();

	    super.render();
	}
}