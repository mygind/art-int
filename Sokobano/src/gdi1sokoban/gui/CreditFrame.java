package gdi1sokoban.gui;

import java.awt.Font;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import gdi1sokoban.SoundMaster;
import gdi1sokoban.gui.event.*;
import gdi1sokoban.graphic.base.*;


public class CreditFrame extends MenuFrame {
	
	private FontManager.Resource _textRenderer;
	private Button _buttonExit;
	float _time;
	long _timebase;

	LinkedList<String> _lines = new LinkedList<String>();

	
	CreditFrame(Frame frame) throws Exception {
		super(frame);
		initialize();
	}
	
	public CreditFrame(float x, float y, float width, float height) throws Exception {
		super(x, y, width, height);
		initialize();
	}

	public void initialize() throws Exception {
		
		SoundMaster.playCredits();
		
		// Calculate the font-height in pixel in relation to the component-height:
		int pixelsHeight = (int)(getResolution() * 0.08f);
		AffineTransform trans = new AffineTransform();
		trans.scale(pixelsHeight, pixelsHeight);
		Font font = Label.getJavaFont().deriveFont(trans);
		
		_textRenderer = FontManager.getInstance().getInstance(new FontDescriptor(font, new float[] {1.0f, 1.0f, 1.0f, 0.8f}, new float[] {1, 0, 0, 0}));
		
		_lines.add("");
		_lines.add("");
		_lines.add("Sokoban");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("A GDI Production");
		_lines.add("CREDITS");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Producers:");
		_lines.add("Martin Tschirsich");
		_lines.add("Artem Vovk");
		_lines.add("Shuo Yang");
		_lines.add("Zijad Maksuti");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Project Manager:");
		_lines.add("Martin Tschirsich");
		_lines.add("Artem Vovk");
		_lines.add("Shuo Yang");
		_lines.add("Zijad Maksuti");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Cinematic Lead:");
		_lines.add("Zijad Maksuti");
		_lines.add("Shuo Yang");
		_lines.add("Artem Vovk");
		_lines.add("Martin Tschirsich");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Lead Programmers:");
		_lines.add("Martin Tschirsich");
		_lines.add("Artem Vovk");
		_lines.add("Zijad Maksuti");
		_lines.add("Shuo Yang");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Test Managers:");
		_lines.add("Artem Vovk");
		_lines.add("Martin Tschirsich");
		_lines.add("Zijad Maksuti");
		_lines.add("Shuo Yang");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Art Directors:");
		_lines.add("Shuo Yang");
		_lines.add("Martin Tschirsich");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Sound Designers:");
		_lines.add("Zijad Maksuti");
		_lines.add("Artem Vovk");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Mission Designers:");
		_lines.add("Thinking Rabbit, Inc.");
		_lines.add("Francois Marques");
		_lines.add("Eric F. Tchong");
		_lines.add("David W. Skinner");
		_lines.add("The GDI1-Team");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Support Specialists:");
		_lines.add("Zijad Maksuti");
		_lines.add("Martin Tschirsich");
		_lines.add("Artem Vovk");
		_lines.add("Shuo Yang");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Story and Scripts:");
		_lines.add("Zijad Maksuti");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Game Balance:");
		_lines.add("Artem Vovk");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Third Party Director:");
		_lines.add("Shuo Yang");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Community Director:");
		_lines.add("Martin Tschirsich");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Marketing Teams:");
		_lines.add("Ukraine: Artem Vovk");
		_lines.add("China: Shuo Yang");
		_lines.add("Serbia: Zijad Maksuti");
		_lines.add("Germany: Martin T.");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("International Content:");
		_lines.add("Zijad Maksuti");
		_lines.add("Artem Vovk");
		_lines.add("Shuo Yang");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Special Thanks:");
		_lines.add("Aftas Ardem");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("Very Special Thanks:");
		_lines.add("Our fans worldwide");
		_lines.add("");
		_lines.add("*");
		_lines.add("");
		_lines.add("More Special Thanks:");
		_lines.add("Steve Jobs");
		_lines.add("Steve Ballmer");
		_lines.add("Steve Wozniak");
		_lines.add("&");
		_lines.add("Hiroyuki Imabayashi");
		_lines.add("");
		_lines.add("");
		_lines.add("");
		_lines.add("");
		_lines.add("");
		_lines.add("");
		_lines.add("SoKoB.A.N.");
		_lines.add("(C) 2009");
		
		_timebase = System.currentTimeMillis();
		
		_buttonExit = new Button(-0.3f, 0.35f, 0.6f, 0.08f, "Back");
		_buttonExit.addActionListener(new ButtonExitActionListener());
		_buttonExit.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/back_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonExit);
		
		addKeyboardListener(new FrameKeyboardListener());
		 _angleXTarget = 0;
		 _angleYTarget = 45;
		 _zoomTarget = 0.3f;
	}

	private class ButtonExitActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				SoundMaster.stopCredits();
				Frame nextFrame;
				try {
					nextFrame = new StartFrame(CreditFrame.this);
					CreditFrame.this.processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private class FrameKeyboardListener implements KeyboardListener {
		public boolean keyboardEvent(KeyboardEvent event) {
			switch (event.getCode()) {
			case Keyboard.KEY_ESCAPE :
				if (event.getState()) {
					SoundMaster.stopCredits();
					Frame nextFrame;
					try {
						nextFrame = new StartFrame(CreditFrame.this);
						CreditFrame.this.processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return false;
		}
	}
	
	public void render() {
		
		renderBackground();
		
		// Prepare rendering:
		GL11.glMatrixMode (GL11.GL_MODELVIEW);
		
		// Render the background to a quad:
		GL11.glLoadIdentity();
		GL11.glTranslatef(-0.5f,-0.5f,0);
			
		//_background.access().bind();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(0.1f, 0.1f, 0.1f, 0.6f);
		GL11.glBegin(GL11.GL_QUADS); // Ein Viereck zeichnen
		GL11.glTexCoord2f(0, 0); // oben links
		GL11.glVertex3f(0, 0, 0); // oben links
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(0, 1, 0); // oben rechts
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(1, 1, 0); // unten rechts
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(1, 0, 0); // unten links
		GL11.glEnd();
		GL11.glColor4f(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		_time = (System.currentTimeMillis() - _timebase) / 80.0f;
		if (_time > 1650.0f) _time = 1650.0f;
		
		float i  = _time / 120f - 0.3f;
		for (String line : _lines) {
			GL11.glLoadIdentity();
			GL11.glTranslatef(-0.5f,  -0.5f -  i, 0);
			
			float length = _textRenderer.getInstance().calcTextWidth(line);
			float spacing = 1 - length * 0.1f;
			GL11.glTranslatef(spacing/2.0f, 0, 0);
			GL11.glScalef(0.1f, 0.1f, 0);
			
			GL11.glTranslatef(length/2.0f,  0, 0);
			float s = (float) (1 - Math.sin(i*10)*Math.sin(i*10)*1 + Math.cos(i*10)*Math.cos(i*100)*1);
			GL11.glRotatef((float)(Math.sin(length)*10 *s*0.5f ), 0, 0, 1);
			GL11.glTranslatef(-length/2.0f, 0, 0);
			
			if (i < -0.7f)
			 GL11.glRotatef((i + 0.7f)*900, 1, 0, 0);
			
			if (i > -0.05f) {
				
				 GL11.glTranslatef(0,  1.0f, 0);
				 GL11.glRotatef((i + 0.05f)*900, 1, 0, 0);
				 GL11.glTranslatef(0,  -1.0f, 0);
			}
			
			if ((i > -0.8f) && (i < 0.05f))
			  _textRenderer.getInstance().render(line);
			
			i -= 0.1f;
		}
		
	    super.render();
	}
}