package gdi.ws0809.test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
* Public tests for the 3rd extended Sokoban implementation level
* 
* @author Steven Arzt, Oren Avni
* @version 1.0
*/
public class SokobanTestExtended3 {

		/**
		 * the tested Sokoban implementation
		 */
		private SokobanTest testee = null;
		private static File deadlockLevel;
		
		@BeforeClass
		public static void init() {
			try {
				deadlockLevel = new File(ClassLoader.getSystemClassLoader().getResource("deadlock.txt").toURI());
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
		 * Requirement "deadlock"
		 * @throws Exception 
		 */
		@Test
		public void deadlock() throws Exception {
			testee.loadLevel(deadlockLevel);System.out.println("Level: " + testee.currentLevelToString());
			assertFalse("Initially there is no deadlock.", testee.isDeadlock());
			testee.moveWorker('R');
			testee.moveWorker('R');
			System.out.println("Level: " + testee.currentLevelToString());
			assertTrue("Deadlock not detected. Now there are four items in a square.", testee.isDeadlock());
		}
}
