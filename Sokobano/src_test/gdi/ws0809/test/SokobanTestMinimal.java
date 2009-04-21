package gdi.ws0809.test;


import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Public tests for the minimal Sokoban implementation level
 * 
 * @author Steven Arzt, Oren Avni
 * @version 1.0
 */
public class SokobanTestMinimal {
	
	/**
	 * the tested Sokoban implementation
	 */
	private SokobanTest testee = null;
	
	/**
	 * variables and constants needed for tests
	 */
	private static File validLevel;
	private static String validLevelString = "####\n# .#\n#  ###\n#*@  #\n#  $ #\n#  ###\n####\n";
	private static File levelWithoutMan;
	private static File solvedLevel;


	@BeforeClass
	public static void init() {
		try {
			validLevel = new File(ClassLoader.getSystemClassLoader().getResource("valid.txt").toURI());
			levelWithoutMan = new File(ClassLoader.getSystemClassLoader().getResource("noman.txt").toURI());
			solvedLevel = new File(ClassLoader.getSystemClassLoader().getResource("solved.txt").toURI());
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
	 * Requirements "parseLevel", "toString" & "checkSyntax"
	 * @throws Exception 
	 */
	@Test
	public void parseValidLevelFile() throws Exception {
		testee.loadLevel(validLevel);
		assertEquals("The string representation of the parsed level1 differs from file content",validLevelString, testee.currentLevelToString());
	}
	
	@Test(expected=Exception.class)
	public void parseInvalidLevel() throws Exception {
		testee.loadLevel(levelWithoutMan);
	}
	
	/**
	 * Requirements "detectSolved"
	 * @throws Exception 
	 */
	@Test
	public void detectSolved() throws Exception {
		testee.loadLevel(validLevel);
		assertFalse("Unsolved level detected as solved.", testee.isSolved());
		
		testee.loadLevel(solvedLevel);
		assertTrue("Solved level not detected as solved.", testee.isSolved());	
	}
	
	@Test
	public void testGetLevelWidth() throws Exception {
		testee.loadLevel(validLevel);
		assertEquals("The width of the first level should be", 6, testee.getLevelWidth());
	}
	
	@Test
	public void testGetLevelHeight () throws Exception {
		testee.loadLevel(validLevel);
		assertEquals("The height of the first level should be 7", 7, testee.getLevelHeight());
	}
	
	@Test
	public void testGetWallCount() throws Exception {
		testee.loadLevel(validLevel);
		assertEquals ("The first level contains 22 wall pieces", 22, testee.getWallCount());		
	}

	@Test
	public void testGetCrateCount() throws Exception	{
		testee.loadLevel(validLevel);
		assertEquals ("The first level contains 2 movable crates", 2, testee.getCrateCount());		
	}

	@Test
	public void testGetGoalCount() throws Exception {
		testee.loadLevel(validLevel);
		assertEquals ("The first level contains 2 goal positions", 2, testee.getGoalCount());		
	}

	


}

