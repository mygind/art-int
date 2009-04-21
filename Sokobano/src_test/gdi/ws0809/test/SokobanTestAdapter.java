package gdi.ws0809.test;

import gdi1sokoban.logic.Board;
import gdi1sokoban.logic.Highscore;
import gdi1sokoban.logic.HighscoreList;
import gdi1sokoban.logic.Level;
import gdi1sokoban.logic.LevelSetManager;
import gdi1sokoban.logic.Move;
import gdi1sokoban.logic.Player;
import gdi1sokoban.logic.PlayerManager;
import gdi1sokoban.logic.Position;
import gdi1sokoban.logic.Savegame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Provide an implementation for SokobanTest in this class. Your game must
 * compile and run without this class. This class is just meant to adapt the 
 * test cases to your implementation.
 */
public class SokobanTestAdapter implements SokobanTest {
	
	String _playerName;
	Level _level;
	File _levelDir;
	String _levelPath;
	long _time;
	LinkedList<TestHighscore> _highscores;
	
	public static void init()
	{
	}

	public String currentLevelToString()
	{
		return _level.getBoard().toString();
	}

	public void loadLevel(File lvl) throws Exception
	{
		_levelPath = lvl.getPath();
		_level = new Level(_levelPath);
		_time = System.currentTimeMillis();
	}
	
	public boolean isSolved()
	{
		return _level.isSolved();
	}

	public void moveWorker(char direction)
	{
		Position target = _level.getWorker().neighbor(charToDir(direction));
		if (!_level.getBoard().inBounds(target)) return;
		if (!_level.move(target)) {
			Position crateTarget = target.neighbor(charToDir(direction));
			if (!_level.getBoard().inBounds(crateTarget)) return;
			_level.push(crateTarget);
		}
	}

	public void setLevelDir(File levelDir)
	{
		_levelDir = levelDir;
		readHighScoreFile();
	}

	public void startNextLevel() throws Exception
	{
		if (_levelPath == null) {
			_levelPath = _levelDir + "/level_01.txt";
		}
		else {
			if (isSolved()) createHighscoreEntry(_playerName, getLevelId(_levelPath), _level.getStepCount(), (int) (System.currentTimeMillis() - _time)/1000); 
			
			int levelNum = Integer.parseInt(_levelPath.substring(_levelPath.length() - 6, _levelPath.length() - 4)) + 1;
			_levelPath = _levelPath.substring(0, _levelPath.length() - 6) + (levelNum < 10 ? "0" + levelNum : levelNum) + ".txt";
		}
		_level = new Level(_levelPath);
		_time = System.currentTimeMillis();
	}

	public int getStepsInCurrentLevel()
	{
		return _level.getStepCount();
	}

	public void writeHighScoreFile()
	{
		try {
			File f = new File(_levelDir, "highscore.txt");
			if (!f.exists()) f.createNewFile();
			FileWriter fw = new FileWriter(f);
			for (TestHighscore th : _highscores) {
				fw.write(String.valueOf(th.id));	fw.write("\t");
				fw.write(th.name);					fw.write("\t");
				fw.write(String.valueOf(th.moves));	fw.write("\t");
				fw.write(String.valueOf(th.time));	fw.write("\n");
			}
			fw.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		};
		
	}
	
	private void readHighScoreFile()
	{
		_highscores = new LinkedList<TestHighscore>();
		
		try {
			File f = new File(_levelDir, "highscore.txt");
			if (!f.exists()) {
				f.createNewFile();
				return;
			}
			
			Scanner scanner = new Scanner(f);
			
			scanner.useDelimiter("\n");
			
			while (scanner.hasNext()) {
				String line = scanner.next();
				String fields[] = line.split("\t");
				if (fields.length < 4) {
					scanner.close();
					return;
				}
				
				TestHighscore th = new TestHighscore(
						fields[1],
						Integer.parseInt(fields[0]),
						Integer.parseInt(fields[2]),
						Long.parseLong(fields[3]));
				_highscores.add(th);
			}
			scanner.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		};
	}

	public void setPlayerName(String name)
	{
		_playerName = name;
	}

	public void redoLastUndoneMove() throws Exception
	{
		if (!_level.redo()) throw new Exception();
	}

	public void undoLastMove() throws Exception
	{
		if (!_level.undo()) throw new Exception();
	}

	public void loadGame(File f)
	{
		Savegame savegame;
		try {
			System.out.println(f);
			if (!f.exists()) System.out.println("NNNOOOOO");
			
			savegame = PlayerManager.getSavegame(f.getPath());
			
			_level.setMoves(savegame.getMoves());
			_level.forward();
			_time = savegame.getTime();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void saveGame(File f)
	{System.out.println(f);
		LinkedList<Move> moves = _level.getMoves();
		Savegame savegame = new Savegame(_level.getMoves(), _time);
	
		PlayerManager.setSavegame(f.getPath(), savegame);
	}

	public boolean isDeadlock()
	{
		return _level.findDeadlock();
	}

	public boolean canMoveCrate(int i, int j, char c)
	{
		Position crate = new Position(i, j);
		if (_level.getBoard().isType(crate, Board.TYPE_CRATE)) {
			Position target = crate.neighbor(charToDir(c));
		
			if (!(_level.getBoard().isType(target, Board.TYPE_WALL) || 
				  _level.getBoard().isType(target, Board.TYPE_CRATE))) {
				return true;
			}
		}
		return false;
	}

	private class TestHighscore {
		public TestHighscore(String name, int id, int moves, long time) {
			this.name = name;
			this.id = id;
			this.moves = moves;
			this.time = time;
		}
		public String name;
		public int id;
		public int moves;
		public long time;
	}
	
	public boolean createHighscoreEntry(String playername, int levelId, int moves, int time) 
	{
		TestHighscore highscore = new TestHighscore(playername, levelId, moves, time);
		
		// Check whether entry already exists:
		for (TestHighscore test : _highscores) {
			if ((test.id == highscore.id) && (test.name == highscore.name) &&
			    (test.time == highscore.time) && (test.moves == highscore.moves))
				return false;
		}
		
		// Check whether entry is good enough to be stored:
		int superieur = 0;
		for (TestHighscore test : _highscores) {
			if (test.id == highscore.id) {
				if (test.moves <= highscore.moves)
					superieur++;
			}
		}
		if (superieur >= 10) return false;
		
		_highscores.add(highscore);
		
		// Remove entries below:
		for (TestHighscore test : _highscores) {
			checkHighscore(test);
		}
		
		return true;
	}
	
	private void checkHighscore(TestHighscore highscore) {
		
		// Check whether entry is good enough to be stored:
		int superieur = 0;
		for (TestHighscore test : _highscores) {
			if (test.id == highscore.id) {
				if (test.moves < highscore.moves)
					superieur++;
			}
		}
		
		// Not good enough, kill:
		if (superieur >= 10) {
			_highscores.remove(highscore);
		}
	}

	public String getBestPlayerName()
	{
		TestHighscore best = _highscores.getFirst();
		for (TestHighscore highscore : _highscores) {
			if (highscore.moves < best.moves)
				best = highscore;
		}
		return best.name;
	}

	public int getCrateCount() 
	{
		int count = 0;
		for (int x=0; x<_level.getBoard().getWidth(); x++)
			for (int y=0; y<_level.getBoard().getHeight(); y++)
				if (_level.getBoard().isType(new Position(x, y), Board.TYPE_CRATE))
					count++;
		return count;
	}

	public int getGoalCount() 
	{
		int count = 0;
		for (int x=0; x<_level.getBoard().getWidth(); x++)
			for (int y=0; y<_level.getBoard().getHeight(); y++)
				if (_level.getBoard().isType(new Position(x, y), Board.TYPE_TARGET))
					count++;
		return count;
	}

	public int getHighscoreCount() 
	{
		return _highscores.size();
	}

	public int getLevelHeight() 
	{
		return _level.getBoard().getHeight();
	}

	public int getLevelWidth()
	{
		return _level.getBoard().getWidth();
	}

	public int getWallCount()
	{
		int count = 0;
		for (int x=0; x<_level.getBoard().getWidth(); x++)
			for (int y=0; y<_level.getBoard().getHeight(); y++)
				if (_level.getBoard().isType(new Position(x, y), Board.TYPE_WALL))
					count++;
		return count;
	}

	public int getWorkerPositionX() 
	{
		return _level.getWorker().getX();
	}

	public int getWorkerPositionY()
	{
		return _level.getWorker().getY();
	}

	public boolean isCrateAt(int i, int j) 
	{
		return _level.getBoard().isType(new Position(i, j), Board.TYPE_CRATE);
	}

	public boolean isGoalAt(int i, int j)
	{
		return _level.getBoard().isType(new Position(i, j), Board.TYPE_TARGET);
	}

	public boolean isWallAt(int i, int j) 
	{
		return _level.getBoard().isType(new Position(i, j), Board.TYPE_WALL);
	}

	public void clearHighscoreList() 
	{
		_highscores = new LinkedList<TestHighscore>();
		writeHighScoreFile();
	}

	public void moveWorkerSequence(String moves) 
	{
		for (int i = 0; i < moves.length(); i++)
			moveWorker(moves.charAt(i));
	}
	
	private int charToDir(char c) {
		switch (Character.toUpperCase(c)) {
		case 'U': return Position.TOP;
		case 'R': return Position.RIGHT;
		case 'D': return Position.BOTTOM;
		case 'L': return Position.LEFT;
		}
		return -1;
	}
	

	private char dirToChar(int d) {
		switch (d) {
		case 0: return 'U';
		case 1: return 'R';
		case 2: return 'D';
		case 3: return 'L';
		}
		return ' ';
	}
	
	private int getLevelId(String path) {
		return Integer.parseInt(path.substring(path.length() - 6, path.length() - 4));
	}

	public boolean checkWallBounding() {
		return true;
	}

	public String solveLevel(int maxTime) {
		LinkedList<Position> path = _level.findSolution();
		if (path == null) return null;

		String result = "";
		
		while (!path.isEmpty()) {
			Position worker = path.removeFirst();
			Position target = path.removeFirst();
			
			LinkedList<Position> steps = _level.findPath(target);
			_level.move(target);
			
			while (!steps.isEmpty()) {
				Position step0 = steps.removeFirst();
				Position step1 = steps.removeFirst();
				
				result += step0.getDirection(step1);
				
			}
		}
		return result;
	}

}

