package gdi1sokoban.control;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import gdi1sokoban.graphic.Animation;
import gdi1sokoban.graphic.AnimationLoader;
import gdi1sokoban.graphic.Geometry;
import gdi1sokoban.graphic.Movable;
import gdi1sokoban.graphic.Path;
import gdi1sokoban.graphic.Skin;
import gdi1sokoban.graphic.Tile;
import gdi1sokoban.graphic.Transformation;
import gdi1sokoban.graphic.base.ModelDescriptor;
import gdi1sokoban.graphic.base.ModelManager;
import gdi1sokoban.logic.Board;
import gdi1sokoban.logic.Level;
import gdi1sokoban.logic.Player;
import gdi1sokoban.logic.Position;
import gdi1sokoban.logic.event.LevelEvent;
import gdi1sokoban.logic.event.LevelListener;

public class Game {

	private Level _level;
	private Player _player;
	
	// Level center:
	private float _centerX, _centerY;
	
	// Static tile geometry:
	private Geometry _walls;
	private Geometry _floors;
	private Geometry _targets;
	
	// Movable tiles:
	private HashMap<Position, Movable<Animation>> _crates = new HashMap<Position, Movable<Animation>>();
	
	// Mouse selection:
	private Position _selectionPos;
	private Animation _selection;
	
	// Movable worker:
	Movable<Animation> _worker;
	int _workerDir;
	
	// Path highlight:
	boolean _highlightPath;
	boolean _highlightCratePath;
	Path _path;
	Path _cratePath;
	
	// Camera:
	private float _angleX = 0;
	private float _angleY = 0;
	
	private FloatBuffer _cameraTransform = FloatBuffer.allocate(16);
	private FloatBuffer _cameraPos = FloatBuffer.allocate(3);
	
	private FloatBuffer _projectionTransform = FloatBuffer.allocate(16);
	private FloatBuffer _skyTransform = FloatBuffer.allocate(16);
	
	// Lighting:
	FloatBuffer _light_specularBuffer;
	FloatBuffer _light_ambientBuffer;
	FloatBuffer _light_diffuseBuffer;
	
	private boolean _reflections;
	
	public Game(Level level, Player player, boolean reflections) throws Exception {
		_player = player;
		_level = level;
		_reflections = reflections;
		
		// Calculate level center position:
		_centerX = _level.getBoard().getWidth() / 2f;
		_centerY = _level.getBoard().getHeight() / 2f;
		
		// Initialize movable worker:
		_worker = new Movable<Animation>(_player.getWorker());
		_worker.getRenderable().startAnimation("stand", System.currentTimeMillis());
		_worker.getRenderable();
		
		//_workerPos = _level.getWorker();
		Transformation translation =
			new Transformation(
					new float[] {_level.getWorker().getX()-_centerX, 0, _level.getWorker().getY()-_centerY},
					null,
					null);
		
		_worker.add(translation, 0);
		
		// Initialize crates:
		for (Position position : _level.getCrates()) {
			Movable<Animation> crate = null;
			if (_level.findDeadlock(position)) {
				crate = new Movable<Animation>(_player.getSkin().getCrates().get(Skin.CRATE_DEADLOCK));
			}
			else if (_level.getBoard().isType(position, Board.TYPE_TARGET)) {
				crate = new Movable<Animation>(_player.getSkin().getCrates().get(Skin.CRATE_DEADLOCK));
			}
			else {
				crate = new Movable<Animation>(_player.getSkin().getCrates().get(Skin.CRATE_NORMAL));
			}
			_crates.put(position, crate);
			
			translation =
				new Transformation(
						new float[] {position.getX()-_centerX, 0, position.getY()-_centerY},
						null,
						null);
			
			crate.add(translation, 0);
		}
		
		// Initialize static tile geometry:
		_walls = buildGeometry(_player.getSkin().getWalls(), Board.TYPE_WALL, Board.TYPE_NONE);
		_floors = buildGeometry(_player.getSkin().getFloors(), Board.TYPE_FLOOR + Board.TYPE_WALL, Board.TYPE_NONE);
		_targets = buildGeometry(_player.getSkin().getTargets(), Board.TYPE_TARGET, Board.TYPE_NONE);
		
		// Load selection:
		_selection = null;
		_selection = AnimationLoader.load("res/mesh/selection.mdl");
		_selection.startAnimation("rest", System.currentTimeMillis());
		
		// Load highlight:
		_highlightPath = false;
		
		// Initialize path:
		_path = new Path(ModelManager.getInstance().getInstance(new ModelDescriptor("res/mesh/highlight.obj")), new LinkedList<Position>(), _centerX, _centerY);
		_cratePath = new Path(ModelManager.getInstance().getInstance(new ModelDescriptor("res/mesh/highlightCrate.obj")), new LinkedList<Position>(), _centerX, _centerY);
		
		// Initialize camera transformation:
		buildCamera(10, 0, 15);
		buildPerspective();
		buildSky();
		
		// Initialize light:
		buildLight();
		
		// initialize event handlers:
		_level.addListener(new LevelDeadlockListener());
		_level.addListener(new LevelPushListener());
		_level.addListener(new LevelPullListener());
		_level.addListener(new LevelMoveListener());
		_level.addListener(new LevelBackListener());
		_level.addListener(new LevelCheatListener());
	}
	
	private class LevelMoveListener implements LevelListener {
		public void event(LevelEvent event) {
			if (event.isType(Level.EVENT_STEP)) {
				
				moveWorker(event.getPosition(), event.getDirection(), "move");
			}
		}
	}
	
	private class LevelBackListener implements LevelListener {
		public void event(LevelEvent event) {
			if (event.isType(Level.EVENT_BACK)) {
				
				moveWorker(event.getPosition(), event.getDirection(), "move");
			}
		}
	}
	
	private class LevelPushListener implements LevelListener {
		public void event(LevelEvent event) {
			if (event.isType(Level.EVENT_PUSH)) {
				
				Position workerTarget = event.getPosition();
				Position crateTarget = event.getPosition().neighbor(event.getDirection());
				
				moveWorker(workerTarget, event.getDirection(), "move");
				moveCrate(workerTarget, crateTarget, event.getDirection(), "push");
			}
		}
	}
	
	private class LevelPullListener implements LevelListener {
		public void event(LevelEvent event) {
			if (event.isType(Level.EVENT_PULL)) {
				
				Position workerOrigin = event.getPosition();
				Position crateOrigin = event.getPosition().neighbor(Position.reverse(event.getDirection()));
				
				moveWorker(workerOrigin, event.getDirection(), "move");
				moveCrate(crateOrigin.neighbor(Position.reverse(event.getDirection())), crateOrigin,Position.reverse( event.getDirection()), "pull");
			}
		}
	}
	
	private class LevelCheatListener implements LevelListener {
		public void event(LevelEvent event) {
			if (event.isType(Level.EVENT_CHEAT)) {
				Position workerOrigin = event.getPosition();
				
				moveWorker(workerOrigin, event.getDirection(), "move");
				_worker.forward();
			}
		}
	}
	
	private class LevelDeadlockListener implements LevelListener {
		public void event(LevelEvent event) {
			if (event.isType(Level.EVENT_DEADLOCK)) {
				
				// Deadlocked crate rot färben:
				Movable<Animation> crate = _crates.get(event.getPosition());
				crate.setRenderable(_player.getSkin().getCrates().get(Skin.CRATE_DEADLOCK));
			}
		}
	}
	
	/**
	 * Restart the game.
	 */
	public void restart() {
		_level.rewind();
	}
	
	public boolean canSelect(Position position) {
		return !_level.getBoard().containsType(_selectionPos, Board.TYPE_WALL + Board.TYPE_FREE); 
	}
	
	/**
	 * Select tile at given position.
	 */
	public void select(Position position) {
		_selectionPos = position;
	}
	
	public boolean isSelected() {
		return _selectionPos != null;
	}
	
	/**
	 * Highlight path from worker position to given position.
	 */
	public void highlightPath(Position target) {
		_highlightPath = target != null;
		
		if (_highlightPath) {
			LinkedList<Position> positions = _level.findPath(target);
			if (positions != null) {
				_path.setPosition(positions);
			}
			else _highlightPath = false;
		}
	}
	
	public void highlightCratePath(Position crate) {
		_highlightCratePath = crate != null;
		
		if (_highlightCratePath) {

			// path between crate and wall / crate:
			int direction = _level.getWorker().getDirection(crate);
			LinkedList<Position> positions = new LinkedList<Position>();
			Position position = crate.neighbor(direction);

			while (!_level.getBoard().containsType(position, Board.TYPE_CRATE + Board.TYPE_WALL)) {
				positions.add(position);
				position = position.neighbor(direction);
			}
			
			if (!positions.isEmpty()) {
				_cratePath.setPosition(positions);
			}
			else _highlightCratePath = false;
		}
	}
	
	public void forward() {
		for (Movable<Animation> crate : _crates.values())
			crate.forward();
		
		 _worker.forward();
	}
	
	public boolean isMoving() {
		for (Movable<Animation> crate : _crates.values())
			if (crate.isMoving()) return true;
		
		if (_worker.isMoving()) return true;
		return false;
	}
	
	public float dirToDegree(int dir) {
		switch (dir) {
			case Position.RIGHT: return 90f;
			case Position.TOP: return 180f;
			case Position.LEFT: return 270f;
			default: return 0f;
		}
	}
	
	public float[] buildRotation(int dir) {
		return new float[] {0, dirToDegree(dir), 0};
	}

	/**
	 * Moves the Worker to the given target using the given animation.
	 * 
	 * @param target
	 * @param animation
	 */
	private void moveWorker(Position target, int direction, String animation) {
		
		Position source = target.neighbor(Position.reverse(direction));
		
		// Add the new position to the worker movement:
		
		// Rotate depending to the direction:
		if (_workerDir != direction) {
			float xDiff = target.getX()-source.getX();
			float yDiff = target.getY()-source.getY();
			
			Transformation rotation =
				new Transformation(
					new float[] {target.getX()-xDiff/2f-_centerX, 0, target.getY()-yDiff/2f-_centerY},
					buildRotation(direction),
					null);
			
			Transformation translation =
			new Transformation(
					new float[] {target.getX()-_centerX, 0, target.getY()-_centerY},
					buildRotation(direction),
					null);
			_worker.add(rotation, 100);
			_worker.add(translation, 100);
		} else {
			Transformation translation =
				new Transformation(
					new float[] {target.getX()-_centerX, 0, target.getY()-_centerY},
					buildRotation(direction),
						null);
			_worker.add(translation,  200);//_worker.getRenderable().getAnimationTime());
			//TODO
		}
		//_worker.getRenderable().startAnimation(animation, System.currentTimeMillis());
		
		_workerDir = direction;
	}
	
	private void moveCrate(Position start, Position target, int direction, String animation) {
		// TODO: Crate on target - change appearance
		Movable<Animation> crate = _crates.get(start);
		
		target = _level.getWorker().neighbor(direction);
		// Add the new position to the crate movement:
		Transformation translation =
			new Transformation(
					new float[] {target.getX()-_centerX, 0, target.getY()-_centerY},
					null,
					null);
		
		// Rotate depending to the direction:
		//Transformation rotation = buildRotation(direction);
		//crate.add(rotation, 1);

		//crate.getRenderable().startAnimation(animation, System.currentTimeMillis());
		crate.add(translation, 200);//_worker.getRenderable().getAnimationTime());
		
		_crates.remove(start);
		_crates.put(target, crate);
	}
	
	
	/**
	 * Create geometry for static tiles:
	 * 
	 * @return geometry
	 */
	private Geometry buildGeometry(List<Tile> tiles, int type, int antiType) {
		
		Geometry geometry = new Geometry();
		Random random = new Random(0);
		
		for (int x = 0; x < _level.getBoard().getWidth(); x++) {
			for (int y = 0; y < _level.getBoard().getHeight(); y++) {

					Transformation transformation = new Transformation();
					
					// Translate to tile position:
					transformation.setPosition(new float[] {x - _centerX, 0f, y - _centerY});
					
					// Initialize current tile model regarding the tile pattern:
					for (Tile tile : tiles) {
						if (tile.fitsRotated(_level.getBoard(), new Position(x, y), type, antiType)) {

							// Choose random fitting tile model:
							int iModel = random.nextInt(tile.getModels().size());
							
							// Add tile model to geometry:
							transformation.setRotation(new float[] {0f, tile.getRotation(), 0f});
							geometry.add(tile.getModels().get(iModel), transformation);
							
							break;
					}
				}
			}
		}
		geometry.build();
	
		return geometry;
	}
	
	private void buildSky() {
		
		GL11.glMatrixMode (GL11.GL_PROJECTION);
		GL11.glPushMatrix();
        GL11.glLoadIdentity();
        
		float maxSize = Math.min(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		float width = Display.getDisplayMode().getWidth() / (maxSize * 2);
		float height = Display.getDisplayMode().getHeight() / (maxSize * 2);
		
		GLU.gluPerspective (85.0f, width / height, .1f, 1000f);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, _skyTransform);
		GL11.glPopMatrix();
	}
	
	private void renderSky() {
		// 1. Skybox:
		// Projection:
		GL11.glMatrixMode (GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glMultMatrix(_skyTransform);
		
		// Rotate and render mesh:
		GL11.glMatrixMode (GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_LIGHTING);

       // GL11.glTranslatef(0, 0, -0.4f);
        GL11.glRotatef (_angleX, 1,0,0);
        GL11.glRotatef (_angleY, 0,1,0);
       
        _player.getSkin().getSkybox().getInstance().render();

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glMatrixMode (GL11.GL_PROJECTION);
        GL11.glPopMatrix();
	}
	
	private void renderSkyMirror() {
		// 1. Skybox:
		// Projection:
		GL11.glMatrixMode (GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glMultMatrix(_skyTransform);
		
		// Rotate and render mesh:
		GL11.glMatrixMode (GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glScalef(1f, -1f, 1f);
        GL11.glRotatef (-_angleX, 1,0,0);
        GL11.glRotatef (_angleY, 0,1,0);
       
    	GL11.glColor4f(1,1,1,0.4f);
		
        _player.getSkin().getSkybox().getInstance().render();
        
        GL11.glColor4f(1,1,1,1);
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        GL11.glMatrixMode (GL11.GL_PROJECTION);
        GL11.glPopMatrix();
	}
	
	public void buildCamera(float x, float y, float zoom) {
		// Kamera:
		GL11.glMatrixMode (GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
        GL11.glLoadIdentity();
        
        // Build a rotation matrix:
        GL11.glRotatef(x, 1.0f, 0.0f, 0f);
        GL11.glRotatef(y, 0.0f, 1.0f, 0f);
        
		FloatBuffer m = FloatBuffer.allocate(16); 
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, m);
        
        // Build the eye vector:
        _angleX = x;
        _angleY = y;
        _cameraPos.put(0, 0);
        _cameraPos.put(1, 0);
        _cameraPos.put(2, zoom);
        
        // Rotate the eye vector:
        FloatBuffer temp = FloatBuffer.allocate(3);
        temp.put(0, _cameraPos.get(0) * m.get(0) + _cameraPos.get(1) * m.get(1) + _cameraPos.get(2) * m.get(2));
        temp.put(1, _cameraPos.get(0) * m.get(4) + _cameraPos.get(1) * m.get(5) + _cameraPos.get(2) * m.get(6));
        temp.put(2, _cameraPos.get(0) * m.get(8) + _cameraPos.get(1) * m.get(9) + _cameraPos.get(2) * m.get(10));
        _cameraPos = temp;
        
        // Build the camera transform using the eye vector:
        GL11.glLoadIdentity();
        GLU.gluLookAt(_cameraPos.get(0), _cameraPos.get(1), _cameraPos.get(2), 0, 0, 0, 0, 1, 0);	
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, _cameraTransform);
        
        GL11.glPopMatrix();
	}

	private void buildPerspective() {
		
		GL11.glMatrixMode (GL11.GL_PROJECTION);
		GL11.glPushMatrix();
        GL11.glLoadIdentity();
        
		float maxSize = Math.min(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
		float width = Display.getDisplayMode().getWidth() / (maxSize * 2);
		float height = Display.getDisplayMode().getHeight() / (maxSize * 2);
		
		GLU.gluPerspective(35.0f, width / height, .1f, 1000f);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, _projectionTransform);
		GL11.glPopMatrix();
	}
	
	private void buildLight() {
		// 4. Lighting:
		float specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
		_light_specularBuffer = BufferUtils.createFloatBuffer(specular.length);
		_light_specularBuffer.put(specular);
		_light_specularBuffer.flip();
		
		float diffuse[] = {1.7f, 1.7f, 1.7f, 1.0f};
		_light_diffuseBuffer = BufferUtils.createFloatBuffer(diffuse.length);
		_light_diffuseBuffer.put(diffuse);
		_light_diffuseBuffer.flip();
		
		float ambient[] = {0.3f, 0.3f, 0.3f, 1.0f};
		_light_ambientBuffer = BufferUtils.createFloatBuffer(ambient.length);
		_light_ambientBuffer.put(ambient);
		_light_ambientBuffer.flip();

		float light_direction[] = { 1, 1f, 0, 0.0f };
		FloatBuffer light_directionBuffer = BufferUtils.createFloatBuffer(light_direction.length);
		light_directionBuffer.put(light_direction);
		light_directionBuffer.flip();
		
		/*float light_spotdirection[] = { -1, -0.5f, 0, 0};
		FloatBuffer light_spotdirectionBuffer = BufferUtils.createFloatBuffer(light_spotdirection.length);
		light_spotdirectionBuffer.put(light_spotdirection);
		light_spotdirectionBuffer.flip();*/
		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, _light_diffuseBuffer);
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, _light_ambientBuffer);
		//GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, light_specularBuffer);
		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, light_directionBuffer);
		//GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPOT_DIRECTION, light_spotdirectionBuffer);
		
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, _light_diffuseBuffer);
		GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, _light_ambientBuffer);
		//GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, light_specularBuffer);
		
		/*GL11.glMateriali(GL11.GL_FRONT, GL11.GL_SHININESS, 5);
		
		
		//GL11.glLightf( GL11.GL_LIGHT0, GL11.GL_SPOT_CUTOFF, 8f); // Streuungswinkel
		//GL11.glLightfv( GL11.GL_LIGHT5, GL11.GL_SPOT_DIRECTION, dir);
		//GL11.glLightf( GL11.GL_LIGHT0, GL11.GL_SPOT_EXPONENT, 90f); // Fokus


		//GL11.glLightf(GL11.GL_LIGHT0, GL11.GL_CONSTANT_ATTENUATION, 2.5f);
		//GL11.glLightf(GL11.GL_LIGHT0, GL11.GL_LINEAR_ATTENUATION, 10.5f);
		//GL11.glLightf(GL11.GL_LIGHT0, GL11.GL_QUADRATIC_ATTENUATION, 100.9f);*/
	}
	
	private void renderLight() {
		float light_direction[] = { 1, 1f, 0, 0.0f };
		FloatBuffer light_directionBuffer = BufferUtils.createFloatBuffer(light_direction.length);
		light_directionBuffer.put(light_direction);
		light_directionBuffer.flip();
		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, light_directionBuffer);
	}
	
	private void renderMirrorLight() {
		float light_direction[] = { 1, -1f, 0, 0.0f };
		FloatBuffer light_directionBuffer = BufferUtils.createFloatBuffer(light_direction.length);
		light_directionBuffer.put(light_direction);
		light_directionBuffer.flip();
		
		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, light_directionBuffer);
	}

	/**
	 * Rotates a vector 90 degrees around its z-axis.
	 * 
	 * @param v vector
	 * @return rotated vector
	 */
	private Vector4f rotate90(Vector4f v) {
		float d = v.x;
		v.x = v.y;
		v.y = -d;
		return v;
	}
	
	/**
	 * Tests whether a point lies within a polygon.
	 * 
	 * @param p1 polygon1
	 * @param p2 polygon2
	 * @param p3 polygon3
	 * @param p4 polygon4
	 * @param p  point
	 * @return true whether the point lies within the polygon
	 */
	private boolean insideRect(Vector4f p1, Vector4f p2, Vector4f p3, Vector4f p4, Vector4f p) {
		p1.z = p2.z = p3.z = p4.z = 0;
		p1.w = p2.w = p3.w = p4.w = 0;
		p.z = 0;
		p.w = 0;
		Vector4f n = new Vector4f();
		Vector4f test = new Vector4f();
		
		Vector4f.sub(p1, p2, n);
		Vector4f.sub(p, p1, test);
		if (Vector4f.dot(rotate90(n), test) >= 0f) return false;
		
		Vector4f.sub(p3, p4, n);
		Vector4f.sub(p, p3, test);
		if (Vector4f.dot(rotate90(n), test) >= 0f) return false;
		
		Vector4f.sub(p2, p3, n);
		Vector4f.sub(p, p2, test);
		if (Vector4f.dot(rotate90(n), test) >= 0f) return false;
		
		Vector4f.sub(p4, p1, n);
		Vector4f.sub(p, p4, test);
		if (Vector4f.dot(rotate90(n), test) >= 0f) return false;
		return true;
	}
	
	/**
	 * Returns the tile lying under the mouse cursor.
	 * 
	 * @param screenX cursor coordinate
	 * @param screenY cursor coordinate
	 * @return position of the tile or null
	 */
	public Position pick(float screenX, float screenY) {
	
		//Bildschirmpunkt:
		Vector4f screen = new Vector4f(screenX, -screenY, 0, 0);
		
		// Projection Transformation holen:
	    Matrix4f projection = new Matrix4f();
	    projection.load(_projectionTransform.duplicate());
	    
	    // Kamera Transformation holen:
		Matrix4f camera = new Matrix4f();
		camera.load(_cameraTransform.duplicate());
		
		for (int x = 0; x < _level.getBoard().getWidth(); x++) {
			for (int y = 0; y < _level.getBoard().getHeight(); y++) {

				// Die 4 Eckpunkte des Tiles definieren:
				Vector4f rectTopLeft = new Vector4f(x - 0.5f - _centerX, 0, y - 0.5f - _centerY, 1);
				Vector4f rectTopRight = new Vector4f(x + 0.5f - _centerX, 0, y - 0.5f - _centerY, 1);
				Vector4f rectBottomLeft = new Vector4f(x - 0.5f - _centerX, 0, y + 0.5f - _centerY, 1);
				Vector4f rectBottomRight = new Vector4f(x + 0.5f - _centerX, 0, y + 0.5f - _centerY, 1);
				
				// Kamera & Projektionstransformation auf Eckpunkte anwenden:
				Matrix4f.transform(camera, rectTopLeft, rectTopLeft);
				Matrix4f.transform(projection, rectTopLeft, rectTopLeft);
				
				Matrix4f.transform(camera, rectTopRight, rectTopRight);
				Matrix4f.transform(projection, rectTopRight, rectTopRight);
				
				Matrix4f.transform(camera, rectBottomRight, rectBottomRight);
				Matrix4f.transform(projection, rectBottomRight, rectBottomRight);
				
				Matrix4f.transform(camera, rectBottomLeft, rectBottomLeft);
				Matrix4f.transform(projection, rectBottomLeft, rectBottomLeft);
				
				// Eckpunkte normalisieren nach perspekt. Transform:
				rectTopLeft.x/=rectTopLeft.w;
				rectTopLeft.y/=rectTopLeft.w;
				
				rectTopRight.x/=rectTopRight.w;
				rectTopRight.y/=rectTopRight.w;
				
				rectBottomRight.x/=rectBottomRight.w;
				rectBottomRight.y/=rectBottomRight.w;
				
				rectBottomLeft.x/=rectBottomLeft.w;
				rectBottomLeft.y/=rectBottomLeft.w;
				
				// Viewport-Transformation auf Eckpunkte anwenden:
				float maxSize = Math.min(Display.getDisplayMode().getWidth(), Display.getDisplayMode().getHeight());
				float width = Display.getDisplayMode().getWidth() / (maxSize * 2f);
				float height = Display.getDisplayMode().getHeight() / (maxSize * 2f);
				
				rectTopLeft.x = width * rectTopLeft.x;
				rectTopLeft.y = height * rectTopLeft.y;
				
				rectTopRight.x = width * rectTopRight.x;
				rectTopRight.y = height * rectTopRight.y;
				
				rectBottomLeft.x = width * rectBottomLeft.x;
				rectBottomLeft.y = height * rectBottomLeft.y;
				
				rectBottomRight.x = width * rectBottomRight.x;
				rectBottomRight.y = height * rectBottomRight.y;
				
				// Testen, ob tile auf Bildschirm sichtbar:
				if ((rectTopLeft.w<=0) && (rectTopRight.w<=0) && (rectBottomLeft.w<=0) && (rectBottomRight.w<=0)) continue;
				
				// Testen, ob Bildschirmpunkt innerhalb der auf den Bildschirm transformierten Eckpunkte:
				if (insideRect(rectTopLeft, rectTopRight, rectBottomRight, rectBottomLeft, screen)) {
					return new Position(x, y);
				}
			}
		}
		return null;
	}
	
	
	private void renderCrates(long time) {
		// Render crates according to their status (target / normal):
		for (Map.Entry<Position, Movable<Animation>> iCrate : _crates.entrySet()) {

			// Crate auf Target anders faerben:
			if (_level.findDeadlock(iCrate.getKey())) {

				GL11.glColor4f(1,0.4f,0.4f,1f);
				GL11.glDisable(GL11.GL_LIGHTING);
				iCrate.getValue().setRenderable(_player.getSkin().getCrates().get(Skin.CRATE_DEADLOCK));
				iCrate.getValue().render(time);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glColor4f(1,1,1,1f);
			}
			else if (_level.getBoard().isType(iCrate.getKey(), Board.TYPE_TARGET)) {
				GL11.glColor4f(0,1,1,1f);
				GL11.glDisable(GL11.GL_LIGHTING);
				iCrate.getValue().setRenderable(_player.getSkin().getCrates().get(Skin.CRATE_TARGET));
				iCrate.getValue().render(time);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glColor4f(1,1,1,1f);
			}
			else {
				iCrate.getValue().render(time);
			}
		}
	}

	public void render(long time) {
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);
		GL11.glDisable(GL11.GL_BLEND);
		
		renderSky();

		// Set an acceptable perspective:
		GL11.glMatrixMode (GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glMultMatrix(_projectionTransform);

		// Render light:
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glMultMatrix(_cameraTransform);
		
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_LIGHT0);
		
		renderLight();
		
		if (_reflections) {
		
			// 1. Stencil: Boden ausschneiden:
			GL11.glEnable(GL11.GL_STENCIL_TEST);
			GL11.glStencilFunc(GL11.GL_ALWAYS, 1, 1);
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
			GL11.glCullFace(GL11.GL_BACK);
			_floors.render();
			
			// 2. Reflektionen zeichnen:
			renderMirrorLight();
			
			GL11.glStencilFunc(GL11.GL_EQUAL, 1, 1);
			GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);	
			GL11.glCullFace(GL11.GL_FRONT);
			
			renderSkyMirror();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			
			// Mirror model transformation along y axis:
			GL11.glScalef(1, -1, 1);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			_walls.render();
			renderCrates(time);
			_worker.render(time);
			
			// Restore model transformation and lighting:
			GL11.glScalef(1, -1, 1);
			renderLight();
			
			// 3. Draw floor:
			GL11.glDisable(GL11.GL_STENCIL_TEST);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glCullFace(GL11.GL_BACK);

			// Transparency of the mirror surface:
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_LIGHTING);
			
			_floors.render();
			
			GL11.glEnable(GL11.GL_LIGHTING);
			
			_targets.render();
		}
		else {
			// 3. Draw floor:
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			_floors.render();
			
			GL11.glEnable(GL11.GL_BLEND);
			_targets.render();
		}
		
		// 4. Draw selection:
		if (_selectionPos != null) {
			
			// Render camera:
			GL11.glLoadIdentity();
			GL11.glMultMatrix(_cameraTransform);

			GL11.glDisable(GL11.GL_LIGHTING);
			
			GL11.glTranslatef(_selectionPos.getX() - _centerX, 0f, _selectionPos.getY() - _centerY);
			if (!_level.getBoard().containsType(_selectionPos, Board.TYPE_WALL + Board.TYPE_FREE)) 
				_selection.render();
			
			// Restore camera:
			GL11.glLoadIdentity();
			GL11.glMultMatrix(_cameraTransform);
		}
		
		// Render path:
		if (_highlightPath) {
			_path.render();
		}
		
		if (_highlightCratePath) {
			_cratePath.render();
		}
		
		// 4. Draw upper board:
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		
		renderCrates(time);
		_walls.render();
		_worker.render(time);
			
		GL11.glMatrixMode (GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		
		// Restore default OpenGL state:
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
}
