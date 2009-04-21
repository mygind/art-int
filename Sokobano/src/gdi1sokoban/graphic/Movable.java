package gdi1sokoban.graphic;

import org.lwjgl.opengl.GL11;


public class Movable<T extends TimedRenderable> implements TimedRenderable {
	
	T _t;
	Sequence _sequence;

	public Movable(T t) {
		_t = t;
		_sequence = new Sequence();
	}
	
	public void setRenderable(T t) {
		_t = t;
	}
	
	public T getRenderable() {
		return _t;
	}
	
	public void add(Transformation transformation, long duration) {
		
		_sequence.cut(System.currentTimeMillis());
		
		long time = 0;
		if (_sequence.getFinishingTime() > System.currentTimeMillis())
			time = _sequence.getFinishingTime();
		else  {
			time = System.currentTimeMillis();
			_sequence.add(_sequence.getFinishingTransformation(), time);
		}
		
		_sequence.add(transformation, time + duration);
	}
	
	public void forward() {
		_sequence.forward(System.currentTimeMillis());
	}
	
	public boolean isMoving() {
		return System.currentTimeMillis() < _sequence.getFinishingTime();
	}

	public void render() {
		render(0);
	}
	
	public void render(long time) {
		GL11.glPushMatrix();
		_sequence.render(time);
		_t.render(time);
		GL11.glPopMatrix();
	}
}
