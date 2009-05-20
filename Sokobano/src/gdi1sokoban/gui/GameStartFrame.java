package gdi1sokoban.gui;

import gdi1sokoban.SoundMaster;
import gdi1sokoban.control.Game;
import gdi1sokoban.graphic.base.TextureDescriptor;
import gdi1sokoban.graphic.base.TextureManager;
import gdi1sokoban.gui.event.ActionEvent;
import gdi1sokoban.gui.event.ActionListener;
import gdi1sokoban.gui.event.FrameEvent;
import gdi1sokoban.gui.event.KeyboardEvent;
import gdi1sokoban.gui.event.KeyboardListener;
import gdi1sokoban.logic.ConfigManager;
import gdi1sokoban.logic.GlobalConfig;
import gdi1sokoban.logic.Level;
import gdi1sokoban.logic.LevelSetIdentifier;
import gdi1sokoban.logic.LevelStatistic;
import gdi1sokoban.logic.Player;
import gdi1sokoban.logic.Savegame;
import gdi1sokoban.planning.AstarSolver;
import gdi1sokoban.planning.Board;
import gdi1sokoban.planning.LevelParser;
import gdi1sokoban.planning.heuristics.AveragePathHeuristic;
import gdi1sokoban.planning.heuristics.Box4x4Heuristic;
import gdi1sokoban.planning.heuristics.BoxOnGoalHeuristic;
import gdi1sokoban.planning.heuristics.CornerHeuristic;
import gdi1sokoban.planning.heuristics.HeuristicsAdder;
import gdi1sokoban.planning.heuristics.HeuristicsMultiplier;

import java.util.LinkedList;
import java.util.Queue;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;


//vor dem Start: -> game mit 
//     Rotation rendern, keine Eingaben zulassen.
//     Wenn schon Moves verf�gbar, mit doppelter Geschwindigkeit rendern
//     Wenn keine Replay:
//       Nach max. 3 Sek. automatisch zum Game(Play)Frame
//       Mit Klick zum Game(Play)Frame oder zur�ck.
// = GameStartFrame
public class GameStartFrame extends Frame {

	private Button _buttonBack;
	private Button _buttonStart;
	private Label _labelPoints;
	private Label _labelTime;
	private Label _labelPlayer;
	
	private LevelStatistic _levelStatistic;
	private Level _level;
	private LevelSetIdentifier _levelSetIdentifier;
	private Player _player;
	private Game _game;
	private long _time;
	
	// Following by Mygind and Stengaard
	private Button _buttonPlanning;
	
	public GameStartFrame(Frame frame, LevelSetIdentifier levelSetIdentifier, Level level, Player player, LevelStatistic levelStatistic) throws Exception {
		super(frame);
		initialize(frame, levelSetIdentifier, level, player, levelStatistic, null);
	}
		
	public GameStartFrame(Frame frame, LevelSetIdentifier levelSetIdentifier, Level level, Player player, LevelStatistic levelStatistic, Savegame savegame) throws Exception {
		super(frame);
		initialize(frame, levelSetIdentifier, level, player, levelStatistic, savegame);
	}
	
	public void initialize(Frame frame, LevelSetIdentifier levelSetIdentifier, Level level, Player player, LevelStatistic levelStatistic, Savegame savegame) throws Exception {
		
		_level = level;
		_levelSetIdentifier = levelSetIdentifier;
		_player = player;
		_levelStatistic = levelStatistic;
		
		GlobalConfig globalConfig = ConfigManager.getInstance().getGlobalConfig();
		
		_buttonBack = new Button(-0.5f, -0.49f, 0.11f, 0.08f, "", Button.CAP_LEFT);
		_buttonBack.addActionListener(new ButtonBackActionListener());
		_buttonBack.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/back_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_buttonBack);
		
		_labelPlayer = new Label(-0.38f, -0.49f, 0.45f, 0.08f, _level.getName());
		add(_labelPlayer);
		
		_labelPoints = new Label(0.08f, -0.49f, 0.2f, 0.08f, String.valueOf(_levelStatistic.getMoves()));
		_labelPoints.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/step_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_labelPoints);
		
		_labelTime = new Label(0.29f, -0.49f, 0.21f, 0.08f, String.valueOf(_levelStatistic.getTime()/1000), Label.CAP_RIGHT);
		_labelTime.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/clock_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
		add(_labelTime);
		
		// Resume or replay:
		if (savegame != null) {
			_level.setMoves(savegame.getMoves());
			_time = savegame.getTime();
			_labelPoints.setText(String.valueOf(_level.getTotalStepCount()));
			_labelTime.setText(String.valueOf(_time/1000));
		}
		else {
			_time = 0;
		}
		
		// No replay:
		if (!(savegame != null && savegame.isWon())) {
			_buttonStart = new Button(-0.2f, 0.35f, 0.4f, 0.08f, "Start", Button.CAP_BOTH);
			_buttonStart.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/start_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
			_buttonStart.addActionListener(new ButtonStartActionListener());
			add(_buttonStart);
			setFocus(_buttonStart, true);
			
			// Following by Mygind and Stengaard
			_buttonPlanning = new Button(-0.2f - 0.5f, 0.35f, 0.4f, 0.08f, "Planning", Button.CAP_BOTH);
			_buttonPlanning.setIconTexture(TextureManager.getInstance().getInstance(new TextureDescriptor("res/textures/gui/start_icon.png", GL11.GL_LINEAR, GL11.GL_LINEAR)));
			_buttonPlanning.addActionListener(new ButtonPlanningActionListener());
			add(_buttonPlanning);
			//setFocus(_buttonStart, true);
		}

		addKeyboardListener(new FrameKeyboardListener());

		_game = new Game(level, player, globalConfig.isReflections());
	}
	
	private class ButtonStartActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				try {
					// Resume level:
					_level.forward();
					_game.forward();
					
					Frame nextFrame = new GameFrame(GameStartFrame.this, _game, _levelSetIdentifier, _level, _player, _levelStatistic, _time);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class PlanningThread extends Thread{
		GameFrame gf;
		String filename;
		
		public PlanningThread(GameFrame gf, String filename){
			this.gf = gf;
			this.filename = filename;
		}
		
		public void run(){
			try{
				gdi1sokoban.planning.LevelParser lp = new LevelParser();

				// Creating planner
				Board b = lp.parse(filename).getBoard();
				HeuristicsAdder h9 = new HeuristicsAdder(new Board(b));
				HeuristicsMultiplier h9a = new HeuristicsMultiplier(new Board(b));
				h9a.add(new BoxOnGoalHeuristic(new Board(b)));
				h9a.add(new BoxOnGoalHeuristic(new Board(b)));
				h9a.add(new AveragePathHeuristic(new Board(b)));
				h9a.add(new AveragePathHeuristic(new Board(b)));
				h9.add(h9a);
				h9.add(new CornerHeuristic(new Board(b)));
				h9.add(new Box4x4Heuristic(new Board(b)));
				
				gdi1sokoban.planning.AstarSolver solver = new gdi1sokoban.planning.AstarSolver(new Board(b), h9);
				solver.solve(false);
				String solution = solver.getSolutionString();
				System.out.println(solution);
				
				long sleepTime = 1000;
	
				Queue<KeyboardEvent> moves = generateMoves(solution);
				
				for(KeyboardEvent kb: moves){
					try{Thread.sleep(sleepTime);}catch(Exception e){};
					gf.processKeyboardEvent(kb);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		private Queue<KeyboardEvent> generateMoves(String solution){
			char[] chars = solution.toLowerCase().toCharArray();
			Queue<KeyboardEvent> moves = new LinkedList<KeyboardEvent>();
			for(int i = 0; i < chars.length; i++){
				KeyboardEvent kb = null;
				switch(chars[i]){
					case 'u':
						kb = new KeyboardEvent(Keyboard.KEY_UP, ' ', true);
						break;
					case 'd':
						kb = new KeyboardEvent(Keyboard.KEY_DOWN, ' ', true);
						break;
					case 'l':
						kb = new KeyboardEvent(Keyboard.KEY_LEFT, ' ', true);
						break;
					case 'r':
						kb = new KeyboardEvent(Keyboard.KEY_RIGHT, ' ', true);
						break;
					default: System.err.println("NOOOOOOO!"); 
				}
				if(kb != null){
					moves.add(kb);
				}
				
			}
			return moves;
		}
	}
	
	private class ButtonPlanningActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				try {
					// Resume level:
					_level.forward();
					_game.forward();
					
					Frame nextFrame = new GameFrame(GameStartFrame.this , _game, _levelSetIdentifier, _level, _player, _levelStatistic, _time);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
					// and then some...
					
					PlanningThread pt = new PlanningThread((GameFrame)nextFrame, _level.getFilename());
					pt.start();
					/*gf.render();
					gf.move(Position.TOP);
					gf.render();
					gf.move(Position.LEFT);
					gf.render();
					gf.move(Position.TOP);
					gf.render();
					gf.move(Position.RIGHT);
					gf.render();
					gf.move(Position.BOTTOM);
					gf.render();
					gf.move(Position.BOTTOM);
					gf.render();
					gf.move(Position.BOTTOM);
					gf.render();
					*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class ButtonBackActionListener implements ActionListener {
		public void actionEvent(ActionEvent event) {
			if (event.getCommand() == Button.ACTION_PRESSED) {
				SoundMaster.playMainMenu();
				try {
					Frame nextFrame = new PlayerFrame(GameStartFrame.this, _player);
					processFrameEvent(new FrameEvent(FrameEvent.FRAME_EXIT, nextFrame));
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
					
					try {
						Frame nextFrame = new PlayerFrame(GameStartFrame.this, _player);
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
	
	static float x=0, y=0;
	long timebase = System.currentTimeMillis();
	public void render() {
		if (_level.canRedo() && !_game.isMoving())
			_level.redo();
		_game.render(System.currentTimeMillis());
		x = (System.currentTimeMillis() - timebase)/100.0f;
		_game.buildCamera(65, x, 40);
		super.render();
	}
}
