package com.urbsource.tests;
import static org.junit.Assert.*;

import org.junit.Test;

import com.urbsource.models.Tag;

/**
 * 
 */

/**
 * @author Mehmet Emre
 *
 */
public class TagTest {

	/**
	 * Test method for {@link com.urbsource.models.Tag#Tag()}.
	 */
	@Test
	public final void testTag() {
		Tag t = new Tag();
		assertEquals(t.getName(), null);
		assertEquals(t.getId(), 0);
	}

	/**
	 * Test method for {@link com.urbsource.models.Tag#Tag(java.lang.String, int)}.
	 */
	@Test
	public final void testTagStringInt() {
		Tag t = new Tag("istanbul", 34);
		assertEquals(t.getName(), "istanbul");
		assertEquals(t.getId(), 34);
	}

	/**
	 * Test method for {@link com.urbsource.models.Tag#getId()}.
	 */
	@Test
	public final void testGetId() {
		Tag t = new Tag("istanbul", 34);
		assertEquals(t.getId(), 34);
		t = new Tag();
		assertEquals(t.getId(), 0);
	}

	/**
	 * Test method for {@link com.urbsource.models.Tag#setId(int)}.
	 */
	@Test
	public final void testSetId() {
		Tag t = new Tag();
		t.setId(34);
		assertEquals(t.getId(), 34);
	}

	/**
	 * Test method for {@link com.urbsource.models.Tag#getName()}.
	 */
	@Test
	public final void testGetName() {
		Tag t = new Tag();
		assertEquals(t.getName(), null);
		t = new Tag("istanbul", 34);
		assertEquals(t.getName(), "istanbul");
	}

	/**
	 * Test method for {@link com.urbsource.models.Tag#setName(java.lang.String)}.
	 */
	@Test
	public final void testSetName() {
		Tag t = new Tag();
		t.setName("istanbul");
		assertEquals(t.getName(), "istanbul");
	}

	/**
	 * Test method for {@link com.urbsource.models.Tag#equals(com.urbsource.models.Tag)}.
	 */
	@Test
	public final void testEqualsTag() {
		Tag t1 = new Tag("istanbul", 34);
		Tag t2 = new Tag("istanbul", 35);
		Tag t3 = new Tag("istanbul", 34);
		Tag t4 = new Tag("istanb", 35);
		assertTrue(t1.equals(t1));
		assertTrue(t1.equals(t2));
		assertTrue(t1.equals(t3));
		assertFalse(t1.equals(t4));
		assertFalse(t2.equals(t4));
		assertFalse(t3.equals(t4));
		assertFalse(t4.equals(t1));
	}

}
