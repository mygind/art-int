package gdi1sokoban.graphic;

import gdi1sokoban.graphic.base.ModelManager;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

/**
 * Ein 3D-Modell mit Animation / Material / Texturen. 
 */
public class Animation implements TimedRenderable {
	
	HashMap<String, ModelManager.Resource> _models = new HashMap<String, ModelManager.Resource>();
	HashMap<String, HashMap<String, Sequence>> _animations = new HashMap<String, HashMap<String, Sequence>>();
	HashMap<String, Sequence> _sequences = null;
	long _baseTime;
	
	public Animation(String name, HashMap<String, ModelManager.Resource> meshs, HashMap<String, HashMap<String, Sequence>> animations) {
		super();
		_models = meshs;
		_animations = animations;
	}

	public void startAnimation(String name, long time) {
		_sequences = _animations.get(name);
		_baseTime = time;
	}
	
	public void stopAnimation() {
		_sequences = null;
	}
	
	public long getAnimationTime() {
		long result = 0;
		
		for (Map.Entry<String, ModelManager.Resource> iMesh : _models.entrySet()) {
			long duration =_sequences.get(iMesh.getKey()).getDuration();
			if (duration > result) result = duration;
		}
		
		return result;
	}
	
	public void render() {
		render(0);
	}
	
	public void render(long time) {
		
		if (_sequences == null) {
			for (Map.Entry<String, ModelManager.Resource> iMesh : _models.entrySet()) {
				iMesh.getValue().getInstance().render();
			}
		}
		else {
			for (Map.Entry<String, ModelManager.Resource> iMesh : _models.entrySet()) {
				GL11.glPushMatrix();
				_sequences.get(iMesh.getKey()).render(time - _baseTime);
				iMesh.getValue().getInstance().render();
				GL11.glPopMatrix();
			}
		}
	}
}
