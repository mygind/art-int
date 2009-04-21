package gdi.ws0809.eigene;

import static org.junit.Assert.*;
import gdi1sokoban.exceptions.DuplicateException;
import gdi1sokoban.logic.Highscore;

import gdi1sokoban.logic.LevelSetIdentifier;
import gdi1sokoban.logic.LevelSetManager;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Stalker
 *
 */
@SuppressWarnings("deprecation")
public class LevelSetManagerTest {
	
	private LevelSetManager _lsm;
	
	private String _path;
	@Before
	public void init(){
		_lsm = LevelSetManager.getInstance();
		_path = "res"+File.separator+"levelSet"+File.separator;
	}
	
	@Test
	public void testAddGetRemoveLevelSet(){
		int id =-1;
		try{
			id = _lsm.addLevelSetIdentifier("TestSet", "test");
			_lsm.addLevelSetIdentifier("TestSet", "testdds");
		}catch(DuplicateException e){
			assertTrue("CheckDuplicates!", true);
		}
		assertNotSame("Id is not correct!",-1, id);
		boolean test =false;
		for(LevelSetIdentifier i :_lsm.getLevelSetIdentifiers()){
			if(i.getName().equals("TestSet"));
				test =true;
		}
		assertTrue("Identifier is not correct",test);
		File f1 = new File(_path+id+File.separator+"highScores.xml");
		File f2 = new File(_path+id+File.separator+"levelIdentifiers.xml");
		System.out.println(id);
		System.out.println(f1.exists());
		System.out.println(f2.exists());
		System.out.println(_path);
		if((!f1.exists())||(!f2.exists()))
			assertTrue("Ncesessary files doesnt exists!",false);
		
		_lsm.delLevelSetIdentifier(id);
		if((f1.exists())||(f2.exists()))
			assertTrue("Files haven't benn deleted!",false);
		
		
	}
	
	
	@Test 
	public void testAddGetRemoveHighscores(){
		int id =-1;
		try{
			id =_lsm.addLevelSetIdentifier("HighScoreTest", "high!");
		}catch(DuplicateException e){
			e.printStackTrace();
		}
		
		_lsm.addHighScore(3, id, 1, 5, 346);
		_lsm.addHighScore(8, id, 2, 14, 341);
		
		_lsm.addHighScore(3, id, 0, 14, 342);
		_lsm.addHighScore(2, id, 1, 3, 343);
		_lsm.addHighScore(1, id, 1, 6, 3243);
		
		_lsm.addHighScore(3, id, 1, 8, 327);
		_lsm.addHighScore(0, id, 1, 10, 327);
		_lsm.addHighScore(1, id, 1, 4, 327);
		_lsm.addHighScore(2, id, 1, 12, 327);
		
		_lsm.addHighScore(3, id, 1, 9, 327);
		
		_lsm.addHighScore(3, id, 1, 7, 327);
		_lsm.addHighScore(6, id, 1, 2, 327);
		_lsm.addHighScore(8, id, 1, 11, 327);
		_lsm.addHighScore(2, id, 1, 1, 327);
		_lsm.addHighScore(2, id, 1, 15, 327);
		_lsm.addHighScore(9, id, 1, 18, 327);
		
		_lsm.addHighScore(3, id, 1, 0, 327);
		_lsm.addHighScore(4, id, 1, 18, 327);
		
		_lsm.addHighScore(3, id, 1, 20, 327);
		
		HashMap<Integer, ArrayList<Highscore>> test =_lsm.getHighScoreLists(id);
		
		ArrayList<Highscore> list =test.get(1);
		assertEquals("Add size error",10, list.size());
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).getScore()!=(i)){
				assertTrue("Invalid sort!", false);
			}
		}
		
		_lsm.addHighScore(10, id, 1, 10, 789);
		for(int i = 0; i < list.size(); i++){
			if(list.get(i).getScore()!=(i)){
				assertTrue("Invalid sort!", false);
			}
			if((i==9)&&(list.get(i).getTime()==789)&&(list.get(i).getTime()==789))
				assertTrue("Invalid sort!", false);
			
				
		}
		
//		PlayerManager _pm = PlayerManager.getInstance();
//		int plr = -1;
//		try {
//			plr =_pm.addPlayer("ROBOT");
//		} catch (DuplicateException e) {
//			e.printStackTrace();
//		}
//		_pm.delPlayer(plr);
//		System.out.println(plr);
		
		
		_lsm.testRemoveHighScore(3);
		
		HashMap<Integer, ArrayList<Highscore>> test2 =_lsm.getHighScoreLists(id);
		ArrayList<Highscore> list2 =test2.get(1);
		
		assertEquals("Remove highscore error",5, list2.size());
		_lsm.delLevelSetIdentifier(id);
		
	}
	
	@Test
	public void testResetConfiguration(){
		try{
			_lsm.resetLevelSetConfig();
		}catch(DuplicateException e){
			assertTrue("Duplicates", false);
		}
		
	}

	
}
