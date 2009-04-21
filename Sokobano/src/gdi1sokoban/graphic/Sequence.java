package gdi1sokoban.graphic;

import java.util.Vector;

public class Sequence {

	Vector<Transformation> _transformations = new Vector<Transformation>();
	Vector<Long> _times = new Vector<Long>();
	int _baseIndex;
	long _startTime;
	boolean repeat = false;
	
	public Sequence(Vector<Transformation> transformations, Vector<Long> times) {
		set(transformations, times);
	}
	
	public Sequence() {
		_transformations.add(new Transformation());
		_times.add(0l); // This (long) feels wrong but Java is too strong
	}
	
	public Sequence(Transformation transformation, long time) {
		_transformations.add(transformation);
		_times.add(time); 
	}

	public void set(Vector<Transformation> transformations, Vector<Long> times) {
		_transformations = transformations;
		_times = times;
		restart();
	}
	
	public void add(Transformation transformation, long time) {
		_transformations.add(transformation);
		_times.add(time);
	}
	
	public void forward(long time) {

		_baseIndex = 0;
		
		Transformation last = getFinishingTransformation();
		_transformations.clear();
		_times.clear();
		_transformations.add(last); 
		_times.add(time); 
	}
	
	public Transformation getFinishingTransformation() {
		return _transformations.lastElement();
	}
	
	public void cut(long time) {
		// TODO: Delete unused Data 
	}
	
	public long getFinishingTime() {
		return _times.lastElement() + _startTime;
	}
	
	public void restart() {
		_baseIndex = 0;
		_startTime = 0;
	}
	
	public long getDuration() {
		return _times.lastElement();
	}
	
	public int calcBaseIndex(long time) {
		
		// Aktuellen Basisindex erhöhen, wenn die Zielzeit überschritten ist:
		if (_baseIndex < _times.size()) { // Basisindex ist noch nicht am Ende
			if (_times.get(_baseIndex) <= (time - _startTime)) return _baseIndex + 1;
		}
		else if (repeat) { // Automatisch wiederholen
			_baseIndex = 0;
			_startTime = time;
			if (_baseIndex < _times.size())
				if (_times.get(_baseIndex) <= (time - _startTime)) return _baseIndex + 1;
		}
		
		return _baseIndex;
	}
	
	// Times:           [100] [200] [900]
	// Transformations: [001] [002] [003]
	// _iTime ist der Index, der aktuellen Transformation / Basiszeit
	// _iTime+1 ist der Index, der Zieltransformation / Zielzeit
	public void render(long time) {

		// Es gibt keine Transformationen in der Sequenz:
		if (_times.isEmpty()) return;

		_baseIndex = calcBaseIndex(time);
		
		time = time - _startTime;
		
		if (_baseIndex == 0) {

			// Aktuelle Transformation ist erste der Sequenz:
			float interpolate = time /(float) _times.get(0);
			_transformations.get(0).render(interpolate);
		}
		else if (_baseIndex < _times.size()) {
			
			// Aktuelle Transformation ist in der Mitte der Sequenz:
			float interpolate = (time - _times.get(_baseIndex - 1)) /(float) (_times.get(_baseIndex) - _times.get(_baseIndex - 1));
			_transformations.get(_baseIndex - 1).render(_transformations.get(_baseIndex), interpolate);
		}
		else {

			// Aktuelle Transformation ist letzte der Sequenz:
			_transformations.get(_baseIndex - 1).render();
		}
	}
}