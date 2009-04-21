package gdi1sokoban;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import gdi1sokoban.gui.event.*;
import gdi1sokoban.gui.*;
import gdi1sokoban.logic.ConfigManager;
import gdi1sokoban.logic.GlobalConfig;

/**
 * This class contains the main method and is responsible for setting up and
 * shutting down the OpenGL window. Mouse and keyboard events are transformed
 * into GUI events and sent to the active frame for further processing.
 */
public class Application implements FrameListener {

	Frame _frame;
	
	boolean running = true;
	
	/**
	 * The main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Application application = null;
		
		try {
			application = new Application();
			application.run();
		} catch (LWJGLException exception) {
			
			// Application initialization failed:
			exception.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Choose the suitable display mode according to the given parameters.
	 * 
	 * @param width
	 * @param height
	 * @param bpp	bits per pixel
	 * @return 		the suitable display mode
	 * @throws LWJGLException
	 */
	 private DisplayMode getValidDisplayMode(int width, int height, int colorDepth, int frequency) {

		// get all the modes, and find one that matches our settings.
        DisplayMode[] modes;
        try {
            modes = Display.getAvailableDisplayModes();
        } catch (final LWJGLException e) {
            return null;
        }

        // Try to find a best match.
        int best_match = -1; // looking for request size/bpp followed by exact or highest freq
        int match_freq = -1;
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].getWidth() != width) {
                continue;
            }
            if (modes[i].getHeight() != height) {
                continue;
            }
            if (colorDepth != 0 && modes[i].getBitsPerPixel() != colorDepth) {
                // should pick based on best match here too
                continue;
            }
            if (best_match == -1) {
                best_match = i;
                match_freq = modes[i].getFrequency();
            } else {
                final int cur_freq = modes[i].getFrequency();
                if (match_freq != frequency && // Previous is not a perfect match
                        (cur_freq == frequency || // Current is perfect match
                        match_freq < cur_freq)) // or is higher freq
                {
                    best_match = i;
                    match_freq = cur_freq;
                }
            }
        }

        if (best_match == -1) {
            return null; // none found;
        } else {
             return modes[best_match];
        }
    }

	 
	/**
	 * Choose the suitable display mode according to the given parameters.
	 * 
	 * @param width
	 * @param height
	 * @param bpp	bits per pixel
	 * @return 		the suitable display mode
	 * @throws LWJGLException
	 */
	private DisplayMode findDisplayMode(int width, int height, int bpp, int frequency) throws LWJGLException {
		DisplayMode[] modes = Display.getAvailableDisplayModes();
		for (int i = 0; i < modes.length; i++) {
			if (modes[i].getWidth() == width && modes[i].getHeight() == height
					&& modes[i].getBitsPerPixel() >= bpp
					&& modes[i].getFrequency() == frequency) {
				return modes[i];
			}
		}
		return null;
	}

	public Application() throws Exception
	{
		DisplayMode mode = null;
		GlobalConfig globalConfig = null;
		globalConfig = ConfigManager.getInstance().getGlobalConfig();
		
		// Load configuration:
		try {
			
			int resolutionX = GlobalConfig.DisplayModeToInt(globalConfig.getResolution(), 0);
			int resolutionY = GlobalConfig.DisplayModeToInt(globalConfig.getResolution(), 1);
			int frequency = GlobalConfig.DisplayModeToInt(globalConfig.getResolution(), 2);
			int bpp = GlobalConfig.DisplayModeToInt(globalConfig.getResolution(), 3);
			
			// Create display:
			Display.setTitle("Sokoban");
		
			//Display.setFullscreen(true);
			mode = getValidDisplayMode(resolutionX, resolutionY, bpp, frequency);
		
			// Mode not compatible:
			if (mode == null) throw new Exception();
			
			// Create display:
			Display.setDisplayMode(mode);
			Display.setFullscreen(globalConfig.isFullscreen());
			Display.create(new PixelFormat(8, 16, 8));
		}
		catch (Exception e) {
			
			// Config file invalid:
			mode = Display.getDesktopDisplayMode();
			globalConfig = new GlobalConfig(GlobalConfig.DisplayModeToString(mode), false, true, true);
			ConfigManager.getInstance().saveGlobalConfig(globalConfig);
			System.out.println("Invalid config file!");
			
			// Create display:
			Display.setDisplayMode(mode);
			Display.setFullscreen(globalConfig.isFullscreen());
			Display.create(new PixelFormat(8, 16, 8));
		}

		Display.makeCurrent();
		Display.update();
		
		// Create keyboard input accessor:
		Keyboard.create();
		Keyboard.enableRepeatEvents(true);
		
		Mouse.create();
		Mouse.setGrabbed(true);
		
		// Standard OpenGL-State:
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		// Setting up projection matrix:
		// The coordinate-system has its center (0,0) in the middle point of the
		// frame; a minimum extend of 0.5 in all directions is guaranteed.
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		
		float maxSize = Math.min(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		float width = Display.getDisplayMode().getWidth() / (maxSize * 2);
		float height = Display.getDisplayMode().getHeight() / (maxSize * 2);
		
		GL11.glOrtho(-width, width, height, -height, 1.0f, -1.0f);
		
		Component.setResolution(maxSize);
		
		// Create a frame spanning over the whole viewport:
		_frame = new StartFrame(-width, -height, width * 2, height * 2);
		_frame.addFrameListener(this);
	}
	
	public boolean frameEvent(FrameEvent event) {
		if (event.getType() == FrameEvent.FRAME_EXIT) {
			
			if (event.getParam() == null) {
				running = false;
			}
			else {
				_frame = (Frame) event.getParam();
				_frame.addFrameListener(this);
			}
		}
		return false;
	}
	
	public void run()
	{
		// This variable doesn't really need a comment, does it?
		boolean _mouseDown = false;
		
		while (running)
		{
			while (Keyboard.next()) {
				//KeyboardEvent event = new KeyboardEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventKeyState());
				_frame.processKeyboardEvent(new KeyboardEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventKeyState()));
			}
			
			while (Mouse.next()) {
				
				float maxSize = Math.min(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());

				float mouseX = (Mouse.getEventX() - Display.getDisplayMode().getWidth() / 2) / maxSize;
				float mouseY = (Display.getDisplayMode().getHeight() / 2 - Mouse.getEventY()) / maxSize;
				
				// Check whether mouse button is released:
				if (_mouseDown && !Mouse.getEventButtonState()) {
					_frame.processMouseEvent(new MouseEvent(MouseEvent.MOUSE_RELEASED, mouseX, mouseY, Mouse.getEventButton(), Mouse.getEventButtonState()));
					_mouseDown = false;
				}
				// Check whether mouse button is pressed:
				else if (!_mouseDown && Mouse.getEventButtonState()) {
					_frame.processMouseEvent(new MouseEvent(MouseEvent.MOUSE_PRESSED, mouseX, mouseY, Mouse.getEventButton(), Mouse.getEventButtonState()));
					_mouseDown = true;
				}
				
				// Mouse always moves:
				_frame.processMouseEvent(new MouseEvent(MouseEvent.MOUSE_MOVED, mouseX, mouseY, Mouse.getEventButton(), Mouse.isButtonDown(Mouse.getEventButton())));
			}
			
			Display.update();
			
			if (Display.isCloseRequested()) {
				// frame.exit();
				running = false;
			}

			if (Display.isVisible() || Display.isDirty()) {
				_frame.render();
			}
		}
		
		Display.destroy();

		System.exit(0); // Mac OS X compatibility
	}
}
