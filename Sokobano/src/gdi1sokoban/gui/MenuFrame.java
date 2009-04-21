package gdi1sokoban.gui;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import gdi1sokoban.graphic.base.*;

public class MenuFrame extends Frame {
	
	protected static ModelManager.Resource  _skybox;
	
	protected static FloatBuffer _projectionTransform = FloatBuffer.allocate(16);
	
	protected static float _angleYPrevious = 0;
	protected static float _angleXPrevious = 0;
	protected static float _zoomPrevious = 0;
	protected static float _angleY = 0;
	protected static float _angleX = 0;
	protected static float _zoom = -0.4f;
	
	protected float _angleXTarget;
	protected float _angleYTarget;
	protected float _zoomTarget;
	protected float _angleXCurrent;
	protected float _angleYCurrent;
	protected float _zoomCurrent;
	protected static long _timebase;
	
	public MenuFrame(Frame frame) throws Exception {
		super(frame);
		initialize();
	}
	
	public MenuFrame(float x, float y, float width, float height) throws Exception {
		super(x, y, width, height);
		initialize();
	}

	private void initialize() throws Exception {
		
		// Loading the resources:
		_skybox = ModelManager.getInstance().getInstance(new ModelDescriptor("res/mesh/skybox02.obj"));
		
		// Prepare projection matrix for later use:
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		
		float maxSize = Math.min(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		float width = Display.getDisplayMode().getWidth() / (maxSize * 2);
		float height = Display.getDisplayMode().getHeight() / (maxSize * 2);
		
		GLU.gluPerspective (85.0f, width / height, .1f, 1000f);
	    GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, _projectionTransform);
	    GL11.glPopMatrix();
	    
	    _angleXTarget = 0;
	    _angleYTarget = 0;
	    _zoomTarget = 0;
	    
	    _angleXPrevious = _angleX;
	    _angleYPrevious = _angleY;
	    _zoomPrevious = _zoom;
	    
	    _timebase = System.currentTimeMillis();
	}
	
	protected void renderBackground() {
		renderBackground(_skybox);
	}
	
	protected void renderBackground(ModelManager.Resource skybox) {
		
		_angleX = _angleXTarget - (_angleXTarget - _angleXPrevious) * (1 / (float)(1 + 0.04f*(System.currentTimeMillis() - _timebase)));
		_angleY = _angleYTarget - (_angleYTarget - _angleYPrevious) * (1 / (float)(1 + 0.04f*(System.currentTimeMillis() - _timebase)));
		_zoom = _zoomTarget - (_zoomTarget - _zoomPrevious) * (1 / (float)(1 + 0.001f*(System.currentTimeMillis() - _timebase)));
		
		// Clear scene:
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

		// Setup perspective matrix:
		GL11.glMatrixMode (GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadMatrix(_projectionTransform);
		
		// Setup model matrix:
		GL11.glMatrixMode (GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		
		float _time = (System.currentTimeMillis() - _timebase) / 80.0f;
		GL11.glRotatef((float)Math.sin(_time/15.0)*2.0f, 0, 1, 0);
		
		// Draw skybox:
        GL11.glTranslatef(0.0f, 0.05f, _zoom);
        GL11.glRotatef(_angleY, 1,0,0);
        GL11.glRotatef(_angleX, 0,1,0);
		
        skybox.getInstance().render();
        
        // Restore orthogonal matrix:
		GL11.glMatrixMode (GL11.GL_PROJECTION);
		GL11.glPopMatrix();
	}
	
	
	public void render() {
		
	    super.render();
	}
}