package gdi.ws0809.eigene;

import static org.junit.Assert.*;
import gdi1sokoban.exceptions.DuplicateException;
import gdi1sokoban.exceptions.IllegalFormatException;
import gdi1sokoban.logic.LevelSetManager;
import gdi1sokoban.logic.LevelStatistic;
import gdi1sokoban.logic.Move;
import gdi1sokoban.logic.Player;
import gdi1sokoban.logic.PlayerIdentifier;
import gdi1sokoban.logic.PlayerManager;
import gdi1sokoban.logic.Position;
import gdi1sokoban.logic.Savegame;
import gdi1sokoban.logic.SkinManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

/**
 * Tests for PlayerManager
 * @author Stalker
 *
 */
public class PlayerManagerTest {
	
	private PlayerManager _plr =null;
	
	private String _path; 
	
	//private int _id;
	
	@Before
	public void init(){
		_plr = PlayerManager.getInstance();
		_path = File.separator+"res"+File.separator+"player"+File.separator;
	}
	
	@Test
	public void testAddDelPlayer(){
		boolean duplicate =false;
		int id =-1;
		try{
			id = _plr.addPlayer("Ivan");
			//this._id = id;
			
			_plr.addPlayer("Ivan");
		}catch(DuplicateException e){
			duplicate =true;
		}
		assertTrue("Duplicate exception!", duplicate);
		assertNotSame("Wrong id!",-1, id);
		
		try{
			new FileReader(_path+id+File.separator+"player.xml");
		}catch(FileNotFoundException e){
			assertTrue("Folder or player.xml file doesn't exists!", true);
		}
		
		testDelPlayer(id);
		File f = new File(_path+id+File.separator+"player.xml");
		assertFalse(f.exists());
		
	}
	
	
	
	
	private void testDelPlayer(int id){
		System.out.println(id);
		_plr.delPlayer(id);
		//assertTrue(false);
	}
	
	
	@Test 
	public void testGetPlayer(){
		try {
			Display.create();
			int id = _plr.addPlayer("Igor");
			Player test = _plr.getPlayer(id);
			
			assertTrue("Wrong player",test.getName().equals("Igor"));
			assertTrue("Wrong player",test.getId() == id);
			assertTrue("Wrong player",test.getCurrentLevelId() == 0);
			assertTrue("Wrong player",test.getCurrentLevelSetId() == 0);
			//test.getSkin()
			_plr.delPlayer(id);
			Display.destroy();
		} catch (DuplicateException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			assertEquals("Duplicates!", true);
		}catch(IllegalFormatException e){
			e.printStackTrace();
		}catch(LWJGLException e){
			e.printStackTrace();
		}
	}

	
	@Test
	public void testSetPlayer(){
		try {
			Display.create();
			int id = _plr.addPlayer("Igor");
			PlayerIdentifier plr = new PlayerIdentifier("Vitja", id, "tester");
			SkinManager _sm = SkinManager.getInstance();
			
			Player test = new Player(plr, _sm.getSkin(0), 3, 2);
			_plr.setPlayer(test);
			
			Player out = _plr.getPlayer(id);
			assertEquals("Vitja", out.getName());
			//TODO:
			//assertEquals("tester", out.getWorker());
			assertEquals(0, out.getSkin().getId());
			assertEquals(2, out.getCurrentLevelId());
			assertEquals(3, out.getCurrentLevelSetId());
			_plr.delPlayer(id);
			Display.destroy();
			
		} catch (DuplicateException e) {
			// TODO Auto-generated catch block
			assertEquals("Duplicates!", true);
		}catch(IllegalFormatException e){
			e.printStackTrace();
		}catch(LWJGLException e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSaveLoadGame(){
		LinkedList<Move> moves = new LinkedList<Move>();
		LinkedList<Position> positions = new LinkedList<Position>();
		positions.add(new Position(2,3));
		positions.add(new Position(3,3));
		positions.add(new Position(3,4));
		positions.add(new Position(3,5));
		positions.add(new Position(3,6));
		moves.add(new Move(positions,false));
		Savegame save = new Savegame(moves, 345);
		LevelStatistic backup =_plr.getLevelStatistic(0, 1, 2);
		_plr.setSavegame(0, 2, 1, save, false);
		
		
		try {
			Savegame test =_plr.getSavegame(0, 2, 1);
			assertEquals(save.getTime(), test.getTime());
			assertEquals(save.getMoves().getFirst().getStepCount(), test.getMoves().getFirst().getStepCount());
			_plr.addLevelStatistic(backup, 2, 0, 1);
			
		} catch (IOException e) {
			assertTrue("WrongSave", false);
		}
		
	}
}
