package gdi.ws0809.eigene;


import static org.junit.Assert.*;
import gdi1sokoban.logic.Level;

import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class DeadlockTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDeadCorner1() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("  ## ");
		strings.add(" #$ #");
		strings.add("# .@#");
		strings.add(" ### ");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(true, level.findDeadlock());
	}
	
	@Test
	public void testDeadCorner2() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add(" ####");
		strings.add("#.@ #");
		strings.add("# $# ");
		strings.add("###  ");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(true, level.findDeadlock());
	}
	
	@Test
	public void testDeadCorner3() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#** .#");
		strings.add("#*$# #");
		strings.add("# #@ #");
		strings.add("######");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(true, level.findDeadlock());
	}
	
	@Test
	public void testDeadSquare1() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add(" ### ");
		strings.add("#$.@#");
		strings.add("#$.# ");
		strings.add(" ### ");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(true, level.findDeadlock());
	}
	
	@Test
	public void testDeadSquare2() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add(" ### ");
		strings.add("#$ @#");
		strings.add("# . #");
		strings.add(" ### ");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(true, level.findDeadlock());
	}
	
	@Test
	public void testDeadSquare3() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#@ $ #");
		strings.add("# #$ #");
		strings.add("# *$ #");
		strings.add("# #..#");
		strings.add("# $..#");
		strings.add("######");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(true, level.findDeadlock());
	}
	
	@Test
	public void testDeadWall1() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#@   #");
		strings.add("#    #");
		strings.add("#  . #");
		strings.add("#   $#");
		strings.add("#    #");
		strings.add("######");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(true, level.findDeadlock());
	}
	
	@Test
	public void testDeadWall2() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#@ . #");
		strings.add("#    #");
		strings.add("#  $ #");
		strings.add("######");
		strings.add("#    #");
		strings.add("######");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(true, level.findDeadlock());
	}
	
	@Test
	public void testNoDeadlock1() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add(" ### ");
		strings.add("#   #");
		strings.add("# $@#");
		strings.add("# .# ");
		strings.add(" ##  ");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(false, level.findDeadlock());
	}
	
	@Test
	public void testNoDeadlock2() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add(" ### ");
		strings.add("#   #");
		strings.add("# $@#");
		strings.add(" #. #");
		strings.add(" ### ");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(false, level.findDeadlock());
	}
	
	@Test
	public void testNoDeadlock3() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#   @#");
		strings.add("# ** #");
		strings.add("# .$ #");
		strings.add("######");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(false, level.findDeadlock());
	}
	
	@Test
	public void testNoDeadlock4() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#.$ @#");
		strings.add("##$  #");
		strings.add("#. * #");
		strings.add("######");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(false, level.findDeadlock());
	}
	
	@Test
	public void testNoDeadlock5() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("# @  #");
		strings.add("# $ *#");
		strings.add("# $..#");
		strings.add("######");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(false, level.findDeadlock());
	}
	
	@Test
	public void testNoDeadlock6() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#@   #");
		strings.add("#    #");
		strings.add("#. $ #");
		strings.add("######");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(false, level.findDeadlock());
	}
	
	@Test
	public void testNoDeadlock7() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#@ . #");
		strings.add("# $  #");
		strings.add("####$#");
		strings.add("####.#");
		strings.add("######");
		
		Level level = null;
		
		try {
			level = new Level(strings);
		} catch (Exception exception) {
			fail("Error in level class!");
		}
		
		assertEquals(false, level.findDeadlock());
	}
}