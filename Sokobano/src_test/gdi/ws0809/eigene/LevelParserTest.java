package gdi.ws0809.eigene;


import static org.junit.Assert.*;
import gdi1sokoban.logic.Level;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

public class LevelParserTest {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Tests for correct parsing of valid levels:
	 */
	@Test
	public void testValid1() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("#####");
		strings.add("#$@.#");
		strings.add("#####");

		Level level = null;
		try {
			level = new Level(strings);
			ArrayList<String> result = level.getBoard().toStringList();
			assertEquals(strings.toString(), result.toString());
		} catch (Exception exception) {
			exception.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Tests for correct parsing of valid levels:
	 */
	@Test
	public void testValid2() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("#####");
		strings.add("##.##");
		strings.add("#$@.#");
		strings.add("## ##");
		strings.add("#  $#");
		strings.add("#####");

		Level level = null;
		try {
			level = new Level(strings);
			ArrayList<String> result = level.getBoard().toStringList();
			assertEquals(strings.toString(), result.toString());
		} catch (Exception exception) {
			exception.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Tests for correct detection of leaky border:
	 */
	@Test
	public void test2() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#$@.  ");
		strings.add("######");

		try {
			Level level = new Level(strings);
			fail();
		} catch (Exception exception) {
		}
	}
	
	/**
	 * Tests for correct detection of element outside border:
	 */
	@Test
	public void test3() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("######");
		strings.add("#$@.#.");
		strings.add("######");

		try {
			Level level = new Level(strings);
			fail();
		} catch (Exception exception) {
		}
	}
	
	/**
	 * Tests for correct detection of element outside border:
	 */
	@Test
	public void test4() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("########");
		strings.add("  @#$@.#");
		strings.add("########");

		try {
			Level level = new Level(strings);
			fail();
		} catch (Exception exception) {
		}
	}
	
	/**
	 * Tests for correct detection of element outside border:
	 */
	@Test
	public void test5() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add("########");
		strings.add("   #$*.#");
		strings.add("########");

		try {
			Level level = new Level(strings);
			fail();
		} catch (Exception exception) {
		}
	}
	
	/**
	 * Tests for correct parsing of unreachable elements:
	 */
	@Test
	public void test6() {
		LinkedList<String> strings = new LinkedList<String>();
		strings.add(" ### ");
		strings.add("##.##");
		strings.add("#$@.#");
		strings.add(" ### ");
		strings.add("#  $#");
		strings.add(" ### ");

		try {
			Level level = new Level(strings);
			fail();
		} catch (Exception exception) {
		}
	}
}
