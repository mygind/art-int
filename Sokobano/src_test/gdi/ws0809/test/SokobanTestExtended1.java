package gdi.ws0809.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
* Public tests for the 1st extended Sokoban implementation level
* 
* @author Steven Arzt, Oren Avni
* @version 1.0
*/
public class SokobanTestExtended1 {

	/**
	 * the tested Sokoban implementation
	 */
	private SokobanTest testee = null;
	
	
	private static File validLevel;
	private static String validLevelString = "####\n# .#\n#  ###\n#*@  #\n#  $ #\n#  ###\n####\n";
	private static String validLevelMovedString = "####\n# .#\n#  ###\n#* @ #\n#  $ #\n#  ###\n####\n";
	private static String validLevelMoved2String = "####\n# .#\n#  ###\n#*  @#\n#  $ #\n#  ###\n####\n";

	private static String secondLevelString = "####\n#$.#\n#@ #\n####\n";
	
	private static File levelDir;
	private static File highScoreFile;

	
	@BeforeClass
	public static void init() {
		try {
			validLevel = new File(ClassLoader.getSystemClassLoader().getResource("valid.txt").toURI());
			levelDir = new File(ClassLoader.getSystemClassLoader().getResource("testlevel").toURI());
			highScoreFile = new File(levelDir, "highscore.txt");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} 
		SokobanTestAdapter.init();
	}
	
	@Before
	public void before() {
		testee = new SokobanTestAdapter();
	}
	
	/**
	 * Requirement "moveWorker"
	 */
	@Test
	public void moveWorker() throws Exception {
		testee.loadLevel(validLevel);
		testee.moveWorker('R');
		assertEquals("Worker not moved to the right.",validLevelMovedString,testee.currentLevelToString());
	}
	
	@Test
	public void doNotPerformInvalidMoves() throws Exception {
		testee.loadLevel(validLevel);
		testee.moveWorker('R');
		testee.moveWorker('R');
		testee.moveWorker('R');
		assertEquals("Worker not moved to the right.",validLevelMoved2String,testee.currentLevelToString());
	}
	
	/**
	 * Requirement "startNext"
	 */
	@Test
	public void loadLevelFromDir() throws Exception {
		testee.setLevelDir(levelDir);
		testee.startNextLevel();
		assertEquals("Current level should be the first level to start with.",validLevelString,testee.currentLevelToString());
		testee.startNextLevel();
		assertEquals("Current level is not the second level.",secondLevelString,testee.currentLevelToString());
	}
	
	/**
	 * Requirement "countSteps"
	 */
	@Test
	public void countSteps() throws Exception {
		testee.loadLevel(validLevel);
		testee.moveWorker('R');
		assertEquals("One step should have been performed.", 1, testee.getStepsInCurrentLevel());
		testee.moveWorker('R');
		assertEquals("Two steps should have been performed.", 2, testee.getStepsInCurrentLevel());
		testee.moveWorker('R');
		assertEquals("Two steps should have been performed. Did you count illegal moves?", 2, testee.getStepsInCurrentLevel());
	}
	
	/**
	 * Requirement "highScoreDB"
	 * @throws Exception 
	 */
	@Test
	public void highscoreDbCreate() throws Exception {
		if (highScoreFile.exists()) {
			highScoreFile.delete();
		}
		testee.setLevelDir(levelDir);
		testee.startNextLevel();
		
		testee.writeHighScoreFile();
		assertTrue("Highscore file 'highscore.txt' not written.",highScoreFile.exists());
		assertTrue("Could not delete highscore file. Did you leave any Reader/Writer open?", highScoreFile.delete());
	}
	
	@Test
	public void highscoreDbRead() throws Exception {
		if (highScoreFile.exists()) {
			highScoreFile.delete();
			assertFalse("Could not delete highscore file", highScoreFile.exists());
		}
		highScoreFile.createNewFile();
		FileWriter fw = new FileWriter(highScoreFile);
		String highScoreData = "1\ttestplayer\t2\n";
		fw.append(highScoreData);
		fw.flush();
		fw.close();
		testee.setLevelDir(levelDir);
		highScoreFile.delete();
		assertFalse("Could not delete highscore file", highScoreFile.exists());
		
		// As there might be a time or not in the data the student solutions
		// write out, we read 14 characters (which includes level number, player
		// name and step count) and append a \n as a line terminator in order to
		// accept both cases.
		
		testee.writeHighScoreFile();
		FileReader fr = new FileReader(highScoreFile);
		char[] buff = new char[15];
		fr.read(buff);
		fr.close();
		buff [14] = '\n';
		assertTrue("Could not delete highscore file. Did you leave any Reader/Writer open?", highScoreFile.delete());
	}
	
	@Test
	public void highscoreClear() throws Exception {
		if (highScoreFile.exists()) {
			highScoreFile.delete();
			assertFalse("Could not delete highscore file", highScoreFile.exists());
		}
		highScoreFile.createNewFile();
		FileWriter fw = new FileWriter(highScoreFile);
		String highScoreData = "1\ttestplayer\t2\n";
		fw.append(highScoreData);
		fw.flush();
		fw.close();
		testee.setLevelDir(levelDir);
		testee.clearHighscoreList();	
		testee.writeHighScoreFile();
		if (highScoreFile.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(highScoreFile));
			String line = br.readLine();
			br.close();
			assertTrue("Existing highscore data not treated properly.", null == line || "" == line);
			assertTrue("Could not delete highscore file. Did you leave any Reader/Writer open?", highScoreFile.delete());
		}
	}

	
	@Test
	public void highscoreDbWrite() throws Exception {
		if (highScoreFile.exists()) {
			highScoreFile.delete();
		}
		testee.setLevelDir(levelDir);
		testee.startNextLevel();

		testee.setPlayerName("testplayer");
		testee.startNextLevel();
		testee.startNextLevel();
		testee.moveWorker('R');
		try {
			testee.startNextLevel();
		} catch (Exception e) {
		}
		testee.writeHighScoreFile();
		BufferedReader br = new BufferedReader(new FileReader(highScoreFile));
		String line = br.readLine();
		String[] parts = line.split("\t");
		br.close();
		assertEquals("Solved level should be 3.", "3", parts[0]);
		assertEquals("Player name should be testplayer.", "testplayer",	parts[1]);
		assertEquals("Step count should be 1.", "1", parts[2]);
		assertTrue("Could not delete highscore file. Did you leave any Reader/Writer open?",
				highScoreFile.delete());
	}
	
	@Test
	public void testGetWorkerPositionX() throws Exception {
		testee.loadLevel(validLevel);
		assertEquals ("The worker inside the first level is located at X = 2)", 2, testee.getWorkerPositionX());		
	}

	@Test
	public void testGetWorkerPositionY() throws Exception {
		testee.loadLevel(validLevel);
		assertEquals ("The worker inside the first level is located at (2, 3)", 3, testee.getWorkerPositionY());		
	}

	@Test
	public void testIsCrateAt() throws Exception	{
		testee.loadLevel(validLevel);
		assertFalse("No crate at 0,0", testee.isCrateAt(0, 0));
		assertFalse("No crate at 0,3", testee.isCrateAt(0, 3));
		assertTrue("There should be a crate at 1,3", testee.isCrateAt(1, 3));
	}
	
	@Test
	public void testIsWallAt() throws Exception {
		testee.loadLevel(validLevel);
		assertTrue("Wall at 0,0", testee.isWallAt(0, 0));
		assertTrue("Wall at 0,3", testee.isWallAt(0, 3));
		assertTrue("Wall at 5,4", testee.isWallAt(5, 4));
		assertTrue("Wall at 5,5", testee.isWallAt(5, 5));
		assertFalse("No Wall at 2,2", testee.isWallAt(1, 1));
	}

	@Test
	public void testIsGoalAt() throws Exception {
		testee.loadLevel(validLevel);
		assertFalse ("No goal at 0,0", testee.isGoalAt(0, 0));
		assertFalse ("No goal at 0,3", testee.isGoalAt(0, 3));
		assertFalse ("No goal at 4,0", testee.isGoalAt(4, 0));
		assertFalse ("No goal at 4,2", testee.isGoalAt(4, 2));
		assertFalse ("No goal at 1,6", testee.isGoalAt(1, 6));
		assertFalse ("No goal at 4,1", testee.isGoalAt(4, 1));
		assertTrue ("Goal at 2,1", testee.isGoalAt(2, 1));
	}
	
	
	@Test
	public void testCanMoveCrate() throws Exception {
		testee.loadLevel(validLevel);
		assertTrue("Crate at 3,4 can be moved left", testee.canMoveCrate (3, 4, 'L'));
		assertTrue("Crate at 3,4 can be moved right", testee.canMoveCrate (3, 4, 'R'));
		assertFalse("Crate at 3,4 cannot be moved down", testee.canMoveCrate (3, 4, 'D'));
	}
	
	@Test
	public void testHighscoreCounts() throws Exception {
		if (highScoreFile.exists()) {
			highScoreFile.delete();
		}
		testee.setLevelDir(levelDir);
		assertEquals ("No highscores loaded yet", 0, testee.getHighscoreCount());
		assertTrue(testee.createHighscoreEntry("Habakuk", 1, 20, 12));
		assertEquals ("One highscore entry should be there.", 1, testee.getHighscoreCount());
		
		assertTrue(testee.createHighscoreEntry("Rainer Zufall", 2, 15, 5));
		assertEquals ("Two highscore entries should be there", 2, testee.getHighscoreCount());

		assertTrue(testee.createHighscoreEntry("Konstantin Opel", 1, 25, 8));
		assertEquals ("three highscore entries should be there", 3, testee.getHighscoreCount ());
		
		// fill the list
		for (int i = 3; i <= 10; i++)
			assertTrue (testee.createHighscoreEntry("Hans Wurst", 1, 25 + (i * 2), 10 + (i * 3)));
		assertEquals (11, testee.getHighscoreCount());

		assertFalse("Score to bad to be put in highscore table", testee.createHighscoreEntry("Arno Nyhm", 1, 250, 300));
		assertEquals("Still eleven entries in highscore table", 11, testee.getHighscoreCount());
		
		assertTrue ("Should override an highscore entry", testee.createHighscoreEntry("Kai Pirinha", 1, 30, 45));
		assertEquals("Still eleven entries in highscore table", 11, testee.getHighscoreCount());
	}
	
	@Test
	public void testGetBestPlayerName() {
		if (highScoreFile.exists()) {
			highScoreFile.delete();
		}
		testee.setLevelDir(levelDir);
		
		assertTrue(testee.createHighscoreEntry ("Habakuk", 1, 20, 12));
		assertEquals("Playername of best player should be set", "Habakuk", testee.getBestPlayerName());

		assertTrue(testee.createHighscoreEntry("Ich", 1, 15, 17));
		assertEquals("Ich", testee.getBestPlayerName());

		assertTrue(testee.createHighscoreEntry("Du", 1, 17, 23));
		assertEquals("Ich", testee.getBestPlayerName());
	}
	
	/*========================================================================*/
	
}