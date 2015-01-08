/**
 * 
 */
package com.urbsource.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.urbsource.models.Comment;
import com.urbsource.models.Experience;
import com.urbsource.models.User;

/**
 * @author akif
 *
 */
public class CommentTest {

	/**
	 * Test method for {@link com.urbsource.models.Comment#Comment()}.
	 */
	@Test
	public final void testComment() {
		Comment comment = new Comment();
		assertEquals("New comments created by Comment#Comment() shouldn't have an author.", comment.getAuthor(), null);
		assertEquals(comment.getText(), "");
		assertTrue("New comments' ID must be negative.", comment.getId() < 0);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#Comment(com.urbsource.models.User, java.lang.String, int)}.
	 */
	@Test
	public final void testCommentUserStringInt() {
		User u = new User();
		Comment comment = new Comment(u, "Test String", 1357);
		assertEquals(comment.getAuthor(), u);
		assertEquals(comment.getText(), "Test String");
		assertEquals(comment.getExperienceId(), 1357);
		assertTrue("New comments' ID must be negative.", comment.getId() < 0);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#Comment(int, com.urbsource.models.User, java.lang.String, int)}.
	 */
	@Test
	public final void testCommentIntUserStringInt() {
		User u = new User();
		Comment comment = new Comment(5, u, "Test String", 1357);
		assertEquals(comment.getAuthor(), u);
		assertEquals(comment.getText(), "Test String");
		assertEquals(comment.getExperienceId(), 1357);
		assertEquals(comment.getId(), 5);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#Comment(int, com.urbsource.models.User, java.lang.String, com.urbsource.models.Experience)}.
	 */
	@Test
	public final void testCommentIntUserStringExperience() {
		User u = new User();
		Experience e = new Experience();
		e.setId(1234);
		Comment comment = new Comment(5, u, "Test String", e);
		assertEquals(comment.getAuthor(), u);
		assertEquals(comment.getText(), "Test String");
		assertEquals(comment.getExperienceId(), e.getId());
		assertEquals(comment.getId(), 5);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#Comment(com.urbsource.models.User, java.lang.String, com.urbsource.models.Experience)}.
	 */
	@Test
	public final void testCommentUserStringExperience() {
		User u = new User();
		Experience e = new Experience();
		e.setId(1234);
		Comment comment = new Comment(u, "Test String", e);
		assertEquals(comment.getAuthor(), u);
		assertEquals(comment.getText(), "Test String");
		assertEquals(comment.getExperienceId(), e.getId());
		assertTrue(comment.getId() < 0);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#getId()}.
	 */
	@Test
	public final void testGetId() {
		Comment comment = new Comment();
		assertTrue(comment.getId() < 0);
		comment.setId(5);
		assertEquals(comment.getId(), 5);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#setId(int)}.
	 */
	@Test
	public final void testSetId() {
		Comment comment = new Comment();
		comment.setId(5);
		assertEquals(comment.getId(), 5);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#getText()}.
	 */
	@Test
	public final void testGetText() {
		Comment comment = new Comment();
		assertEquals(comment.getText(), "");
		comment.setText("4");
		assertEquals(comment.getText(), "4");
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#setText(java.lang.String)}.
	 */
	@Test
	public final void testSetText() {
		Comment comment = new Comment();
		comment.setText("4");
		assertEquals(comment.getText(), "4");
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#isTextChanged()}.
	 */
	@Test
	public final void testIsTextChanged() {
		Comment comment = new Comment();
		comment.setText("4");
		comment.setAsSaved();
		assertFalse(comment.isTextChanged());
		comment.setText("5");
		assertTrue(comment.isTextChanged());
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#getAuthor()}.
	 */
	@Test
	public final void testGetAuthor() {
		User u = new User();
		Comment comment = new Comment(u, "Test String", 1);
		assertEquals(comment.getAuthor(), u);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#setAuthor(com.urbsource.models.User)}.
	 */
	@Test
	public final void testSetAuthor() {
		User u = new User();
		Comment comment = new Comment();
		comment.setAuthor(u);
		assertEquals(comment.getAuthor(), u);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#isSaved()}.
	 */
	@Test
	public final void testIsSaved() {
		Comment comment = new Comment();
		comment.setText("4");
		comment.setAsSaved();
		assertTrue(comment.isSaved());
		comment.setText("5");
		assertFalse(comment.isSaved());
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#setAsSaved()}.
	 */
	@Test
	public final void testSetAsSaved() {
		Comment comment = new Comment();
		comment.setAsSaved();
		assertTrue(comment.isSaved());
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#getCreationTime()}.
	 */
	@Test
	public final void testGetCreationTime() {
		java.sql.Timestamp time = new java.sql.Timestamp(1234);
		Comment comment = new Comment();
		comment.setCreationTime(time);
		assertEquals(comment.getCreationTime(), time);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#setCreationTime(java.sql.Timestamp)}.
	 */
	@Test
	public final void testSetCreationTime() {
		java.sql.Timestamp time = new java.sql.Timestamp(1234);
		Comment comment = new Comment();
		comment.setCreationTime(time);
		assertEquals(comment.getCreationTime(), time);
		time = new java.sql.Timestamp(new java.util.Date().getTime());
		comment.setCreationTime(time);
		assertEquals(comment.getCreationTime(), time);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#getModificationTime()}.
	 */
	@Test
	public final void testGetModificationTime() {
		java.sql.Timestamp time = new java.sql.Timestamp(1234);
		Comment comment = new Comment();
		comment.setModificationTime(time);
		assertEquals(comment.getModificationTime(), time);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#setModificationTime(java.sql.Timestamp)}.
	 */
	@Test
	public final void testSetModificationTime() {
		java.sql.Timestamp time = new java.sql.Timestamp(1234);
		Comment comment = new Comment();
		comment.setModificationTime(time);
		assertEquals(comment.getModificationTime(), time);
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#getExperienceId()}.
	 */
	@Test
	public final void testGetExperienceId() {
		User u = new User();
		Comment comment = new Comment(u, "Test String", 1357);
		assertEquals(comment.getExperienceId(), 1357);
		Experience e = new Experience();
		e.setId(1234);
		comment = new Comment(5, u, "Test String", e);
		assertEquals(comment.getExperienceId(), e.getId());
	}

	/**
	 * Test method for {@link com.urbsource.models.Comment#setExperienceId(int)}.
	 */
	@Test
	public final void testSetExperienceId() {
		Comment comment = new Comment();
		comment.setExperienceId(12);
		assertEquals(comment.getExperienceId(), 12);
	}

}
