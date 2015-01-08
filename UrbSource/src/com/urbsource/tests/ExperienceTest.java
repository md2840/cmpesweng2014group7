package com.urbsource.tests;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.urbsource.models.Experience;
import com.urbsource.models.Experience;
import com.urbsource.models.Tag;
import com.urbsource.models.User;

/**
 * 
 */

/**
 * @author akif
 *
 */
public class ExperienceTest {

	/**
	 * Test method for {@link com.urbsource.models.Experience#Experience()}.
	 */
	@Test
	public final void testExperience() {
		Experience exp = new Experience();
		assertEquals(exp.getAuthor(), null);
		assertEquals(exp.getText(), "");
		assertEquals(exp.getTags().size(), 0);
		assertEquals(exp.getAddedTags().size(), 0);
		assertEquals(exp.getNumberOfComments(), 0);
		assertEquals(exp.getPoints(), 0);
		assertTrue("New experiences' IDs must be negative.", exp.getId() < 0);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#Experience(int, com.urbsource.models.User, java.lang.String, java.util.List)}.
	 */
	@Test
	public final void testExperienceIntUserStringListOfTag() {
		User u = new User();
		List<Tag> tags =  Arrays.asList(new Tag[] { new Tag("5", 0) });
		Experience exp = new Experience(5, u, "Test String", tags);
		assertEquals(exp.getAuthor(), u);
		assertEquals(exp.getText(), "Test String");
		assertEquals(exp.getTags().size(), 1);
		assertEquals(exp.getTags().get(0).getName(), "5");
		assertEquals(exp.getAddedTags().size(), 0);
		assertEquals(exp.getNumberOfComments(), 0);
		assertEquals(exp.getPoints(), 0);
		assertEquals(exp.getId(), 5);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#Experience(com.urbsource.models.User, java.lang.String, java.util.List)}.
	 */
	@Test
	public final void testExperienceUserStringListOfTag() {
		User u = new User();
		List<Tag> tags =  Arrays.asList(new Tag[] { new Tag("5", 0) });
		Experience exp = new Experience(u, "Test String", tags);
		assertEquals(exp.getAuthor(), u);
		assertEquals(exp.getText(), "Test String");
		assertEquals(exp.getTags().size(), 1);
		assertEquals(exp.getTags().get(0).getName(), "5");
		assertEquals(exp.getAddedTags().size(), 0);
		assertEquals(exp.getNumberOfComments(), 0);
		assertEquals(exp.getPoints(), 0);
		assertTrue("New experiences' IDs must be negative.", exp.getId() < 0);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#Experience(int, com.urbsource.models.User, java.lang.String, com.urbsource.models.Tag[])}.
	 */
	@Test
	public final void testExperienceIntUserStringTagArray() {
		User u = new User();
		Tag[] tags =  new Tag[] { new Tag("5", 0) };
		Experience exp = new Experience(5, u, "Test String", tags);
		assertEquals(exp.getAuthor(), u);
		assertEquals(exp.getText(), "Test String");
		assertEquals(exp.getTags().size(), 1);
		assertEquals(exp.getTags().get(0).getName(), "5");
		assertEquals(exp.getAddedTags().size(), 0);
		assertEquals(exp.getNumberOfComments(), 0);
		assertEquals(exp.getPoints(), 0);
		assertEquals(exp.getId(), 5);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#Experience(com.urbsource.models.User, java.lang.String, com.urbsource.models.Tag[])}.
	 */
	@Test
	public final void testExperienceUserStringTagArray() {
		User u = new User();
		Tag[] tags =  new Tag[] { new Tag("5", 0) };
		Experience exp = new Experience(u, "Test String", tags);
		assertEquals(exp.getAuthor(), u);
		assertEquals(exp.getText(), "Test String");
		assertEquals(exp.getTags().size(), 1);
		assertEquals(exp.getTags().get(0).getName(), "5");
		assertEquals(exp.getAddedTags().size(), 0);
		assertEquals(exp.getNumberOfComments(), 0);
		assertEquals(exp.getPoints(), 0);
		assertTrue("New experiences' IDs must be negative.", exp.getId() < 0);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#getId()}.
	 */
	@Test
	public final void testGetId() {
		Experience experience = new Experience();
		assertTrue(experience.getId() < 0);
		experience.setId(5);
		assertEquals(experience.getId(), 5);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#setId(int)}.
	 */
	@Test
	public final void testSetId() {
		Experience experience = new Experience();
		experience.setId(5);
		assertEquals(experience.getId(), 5);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#getText()}.
	 */
	@Test
	public final void testGetText() {
		Experience experience = new Experience();
		assertEquals(experience.getText(), "");
		experience.setText("4");
		assertEquals(experience.getText(), "4");
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#setText(java.lang.String)}.
	 */
	@Test
	public final void testSetText() {
		Experience experience = new Experience();
		experience.setText("4");
		assertEquals(experience.getText(), "4");
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#isTextChanged()}.
	 */
	@Test
	public final void testIsTextChanged() {
		Experience experience = new Experience();
		experience.setText("4");
		assertTrue(experience.isTextChanged());
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#isTagsChanged()}.
	 */
	@Test
	public final void testIsTagsChanged() {
		Experience exp = new Experience();
		Tag t = new Tag("istanbul", 57);
		exp.addTag(t);
		assertTrue(exp.isTagsChanged());
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#addTag(com.urbsource.models.Tag)}.
	 */
	@Test
	public final void testAddTag() {
		Experience exp = new Experience();
		Tag t = new Tag("istanbul", 57);
		exp.addTag(t);
		assertTrue(exp.getTags().contains(t));
		assertTrue(exp.getAddedTags().contains(t));
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#removeTag(com.urbsource.models.Tag)}.
	 */
	@Test
	public final void testRemoveTag() {
		Tag t = new Tag("istanbul", 57);
		Experience exp = new Experience(1, new User(), "Some Text", new Tag[] { t });
		exp.removeTag(t);
		assertTrue(exp.getRemovedTags().contains(t));
		assertFalse(exp.getTags().contains(t));
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#getTags()}.
	 */
	@Test
	public final void testGetTags() {
		Tag t = new Tag("istanbul", 57);
		Experience exp = new Experience(1, new User(), "Some Text", new Tag[] { t });
		assertTrue(exp.getTags().contains(t));
		exp.removeTag(t);
		assertFalse(exp.getTags().contains(t));
		exp.addTag(t);
		assertTrue(exp.getTags().contains(t));
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#getRemovedTags()}.
	 */
	@Test
	public final void testGetRemovedTags() {
		Tag t = new Tag("istanbul", 57);
		Experience exp = new Experience(1, new User(), "Some Text", new Tag[] { t });
		assertFalse(exp.getRemovedTags().contains(t));
		exp.removeTag(t);
		assertTrue(exp.getRemovedTags().contains(t));
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#getAddedTags()}.
	 */
	@Test
	public final void testGetAddedTags() {
		Experience exp = new Experience();
		Tag t = new Tag("istanbul", 57);
		assertFalse(exp.getAddedTags().contains(t));
		exp.addTag(t);
		assertTrue(exp.getAddedTags().contains(t));
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#getAuthor()}.
	 */
	@Test
	public final void testGetAuthor() {
		User u = new User();
		Experience experience = new Experience(u, "Test String", new Tag[] {});
		assertEquals(experience.getAuthor(), u);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#setAuthor(com.urbsource.models.User)}.
	 */
	@Test
	public final void testSetAuthor() {
		User u = new User();
		Experience experience = new Experience();
		experience.setAuthor(u);
		assertEquals(experience.getAuthor(), u);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#isSaved()}.
	 */
	@Test
	public final void testIsSaved() {
		Experience experience = new Experience();
		experience.setText("4");
		experience.setAsSaved();
		assertTrue(experience.isSaved());
		experience.setText("5");
		assertFalse(experience.isSaved());
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#setAsSaved()}.
	 */
	@Test
	public final void testSetAsSaved() {
		Experience experience = new Experience();
		experience.setAsSaved();
		assertTrue(experience.isSaved());
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#getMood()} and {@link com.urbsource.models.Experience#setMood(java.lang.String)}.
	 */
	@Test
	public final void testMood() {
		Experience exp = new Experience();
		exp.setMood("happy");
		assertEquals(exp.getMood(), "happy");
		exp.setMood("sad");
		assertEquals(exp.getMood(), "sad");
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#getSpam()}.
	 */
	@Test
	public final void testGetSpam() {
		Experience exp = new Experience();
		assertEquals(exp.getSpam(), 0);
		exp.setSpam(100);
		assertEquals(exp.getSpam(), 100);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#setSpam(int)}.
	 */
	@Test
	public final void testSetSpam() {
		Experience exp = new Experience();
		exp.setSpam(exp.SPAM_THRESHOLD-10);
		assertEquals(exp.getSpam(), exp.SPAM_THRESHOLD-10);
		exp.setSpam(exp.SPAM_THRESHOLD+1);
		assertEquals(exp.getSpam(), exp.SPAM_THRESHOLD+1);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#exceedsSpamLimit()}.
	 */
	@Test
	public final void testExceedsSpamLimit() {
		Experience exp = new Experience();
		exp.setSpam(exp.SPAM_THRESHOLD-10);
		assertFalse(exp.exceedsSpamLimit());
		exp.setSpam(exp.SPAM_THRESHOLD+1);
		assertTrue(exp.exceedsSpamLimit());
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#incrementSpam()}.
	 */
	@Test
	public final void testIncrementSpam() {
		Experience exp = new Experience();
		exp.setSpam(exp.SPAM_THRESHOLD-1);
		exp.incrementSpam();
		assertEquals(exp.getSpam(), exp.SPAM_THRESHOLD);
		assertFalse(exp.exceedsSpamLimit());
		exp.incrementSpam();
		assertTrue(exp.exceedsSpamLimit());
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#decrementSpam()}.
	 */
	@Test
	public final void testDecrementSpam() {
		Experience exp = new Experience();
		exp.setSpam(exp.SPAM_THRESHOLD+2);
		exp.decrementSpam();
		assertEquals(exp.getSpam(), exp.SPAM_THRESHOLD+1);
		assertTrue(exp.exceedsSpamLimit());
		exp.decrementSpam();
		assertEquals(exp.getSpam(), exp.SPAM_THRESHOLD);
		assertFalse(exp.exceedsSpamLimit());
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#getCreationTime()}.
	 */
	@Test
	public final void testGetCreationTime() {
		java.sql.Timestamp time = new java.sql.Timestamp(1234);
		Experience experience = new Experience();
		experience.setCreationTime(time);
		assertEquals(experience.getCreationTime(), time);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#setCreationTime(java.sql.Timestamp)}.
	 */
	@Test
	public final void testSetCreationTime() {
		java.sql.Timestamp time = new java.sql.Timestamp(1234);
		Experience experience = new Experience();
		experience.setCreationTime(time);
		assertEquals(experience.getCreationTime(), time);
		time = new java.sql.Timestamp(new java.util.Date().getTime());
		experience.setCreationTime(time);
		assertEquals(experience.getCreationTime(), time);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#getModificationTime()}.
	 */
	@Test
	public final void testGetModificationTime() {
		java.sql.Timestamp time = new java.sql.Timestamp(1234);
		Experience experience = new Experience();
		experience.setModificationTime(time);
		assertEquals(experience.getModificationTime(), time);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#setModificationTime(java.sql.Timestamp)}.
	 */
	@Test
	public final void testSetModificationTime() {
		java.sql.Timestamp time = new java.sql.Timestamp(1234);
		Experience experience = new Experience();
		experience.setModificationTime(time);
		assertEquals(experience.getModificationTime(), time);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#isUserMarkedSpam()} and {@link com.urbsource.models.Experience#setUserMarkedSpam(boolean)}.
	 */
	@Test
	public final void testUserMarkedSpam() {
		Experience exp = new Experience();
		assertFalse(exp.isUserMarkedSpam());
		exp.setUserMarkedSpam(true);
		assertTrue(exp.isUserMarkedSpam());
		exp.setUserMarkedSpam(false);
		assertFalse(exp.isUserMarkedSpam());
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#getNumberOfComments()} and {@link com.urbsource.models.Experience#setNumberOfComments(int)}.
	 */
	@Test
	public final void testGetNumberOfComments() {
		Experience exp = new Experience();
		assertEquals(exp.getNumberOfComments(), 0);
		exp.setNumberOfComments(7);
		assertEquals(exp.getNumberOfComments(), 7);
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#isUpvotedByUser()} and {@link com.urbsource.models.Experience#setUpvotedByUser(boolean)}.
	 */
	@Test
	public final void testIsUpvotedByUser() {
		Experience exp = new Experience();
		assertFalse(exp.isUpvotedByUser());
		exp.setUpvotedByUser(true);
		assertTrue(exp.isUpvotedByUser());
		exp.setUpvotedByUser(false);
		assertFalse(exp.isUpvotedByUser());
	}

	/**
	 * Test method for {@link com.urbsource.models.Experience#isDownvotedByUser()} and {@link com.urbsource.models.Experience#setDownvotedByUser(boolean)}.
	 */
	@Test
	public final void testIsDownvotedByUser() {
		Experience exp = new Experience();
		assertFalse(exp.isDownvotedByUser());
		exp.setDownvotedByUser(true);
		assertTrue(exp.isDownvotedByUser());
		exp.setDownvotedByUser(false);
		assertFalse(exp.isDownvotedByUser());
	}
	
	/**
	 * Test method for {@link com.urbsource.models.Experience#getTagNames()}.
	 */
	@Test
	public final void testGetTagNames() {
		Experience exp = new Experience();
		assertEquals(exp.getTagNames(), "");
		exp.addTag(new Tag("istanbul", 5));
		exp.addTag(new Tag("waffle", 6));
		assertEquals(exp.getTagNames(), "istanbul,waffle");
	}
}
