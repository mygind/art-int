package gdi1sokoban.graphic;

import org.lwjgl.opengl.GL11;

public class Transformation {
	float[] _rotation;
	float[] _position;
	float[] _scale;
	
	public Transformation() {
		_rotation = new float[] {0, 0, 0};
		_position = new float[] {0, 0, 0};
		_scale = new float[] {1, 1, 1};
	}

	public Transformation(float[] position, float[]  rotation, float[]  scale) {
		_rotation = rotation == null ? new float[] {0, 0, 0} : rotation;
		_position = position == null ? new float[] {0, 0, 0} : position;
		_scale = scale == null ? new float[] {1, 1, 1} : scale;
	}
	
	public void setPosition(float[] position) {
		_position = position;
	}
	
	public float[] getPosition() {
		return _position;
	}
	
	public void setRotation(float[] rotation) {
		_rotation = rotation;
	}
	
	public float[] getRotation() {
		return _rotation;
	}
	
	public float[] getScale() {
		return _scale;
	}
	
	public void render() {
		
		GL11.glTranslatef(_position[0], _position[1], _position[2]);
		
		GL11.glScalef(_scale[0], _scale[1], _scale[2]);
		
		GL11.glRotatef(_rotation[0], 1, 0, 0);
		GL11.glRotatef(_rotation[1], 0, 1, 0);
		GL11.glRotatef(_rotation[2], 0, 0, 1);
	}
	
	public void render(float interpolation) {

		GL11.glTranslatef(_position[0] * interpolation ,
				  _position[1] * interpolation,
				  _position[2] * interpolation);
		
		GL11.glScalef(_scale[0] * interpolation,
					  _scale[1] * interpolation,
					  _scale[2] * interpolation);
		
		GL11.glRotatef(_rotation[0] * interpolation, 1, 0, 0);
		GL11.glRotatef(_rotation[1] * interpolation, 0, 1, 0);
		GL11.glRotatef(_rotation[2] * interpolation, 0, 0, 1);
	}
	
	public void render(Transformation transformation, float interpolation) {
		
		float t1 = 1 - interpolation;
		float t2 = interpolation;
		
		float angleY = -_rotation[1] + transformation.getRotation()[1];
		if (angleY > 180)
			angleY -= 360;
		else if (angleY < -180)
			angleY += 360;

		GL11.glTranslatef(_position[0] * t1 + transformation.getPosition()[0] * t2,
				  _position[1] * t1 + transformation.getPosition()[1] * t2,
				  _position[2] * t1 + transformation.getPosition()[2] * t2);
		
		GL11.glScalef(_scale[0] * t1 + transformation.getScale()[0] * t2,
					  _scale[1] * t1 + transformation.getScale()[1] * t2,
					  _scale[2] * t1 + transformation.getScale()[2] * t2);
		
		GL11.glRotatef(_rotation[0] * t1 + transformation.getRotation()[0] * t2, 1, 0, 0);
		GL11.glRotatef(_rotation[1] + angleY * interpolation, 0, 1, 0);
		GL11.glRotatef(_rotation[2] * t1 + transformation.getRotation()[2] * t2, 0, 0, 1);
	}
	
	protected void build() {
		 /* FloatBuffer m = FloatBuffer.allocate(16);
	        
	        GL11.glRotatef(_angleY, 1.0f, 0.0f, 0f);
	        GL11.glRotatef(_angleX, 0.0f, 1.0f, 0f);
	        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, m);
	        _cameraPos.put(0, 0);
	        _cameraPos.put(1, 0);
	        _cameraPos.put(2, _zoom);
	        FloatBuffer _temp = FloatBuffer.allocate(3);
	        _temp.put(0, _cameraPos.get(0) * m.get(0) + _cameraPos.get(1) * m.get(1) + _cameraPos.get(2) * m.get(2));
	        _temp.put(1, _cameraPos.get(0) * m.get(4) + _cameraPos.get(1) * m.get(5) + _cameraPos.get(2) * m.get(6));
	        _temp.put(2, _cameraPos.get(0) * m.get(8) + _cameraPos.get(1) * m.get(9) + _cameraPos.get(2) * m.get(10));
	        
	        _cameraPos = _temp;
	        GL11.glLoadIdentity();
	        GLU.gluLookAt(_cameraPos.get(0), _cameraPos.get(1), _cameraPos.get(2), 0, 0, 0, 0, 1, 0);	
	        
	        FloatBuffer cameraTransform = FloatBuffer.allocate(16);
	        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, cameraTransform);*/
	}
}