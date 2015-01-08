package com.urbsource.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model representing a comment in database.
 * 
 * @author Mehmet Emre
 */
public class Comment implements Serializable {

	/**
	 * Compiler-generated serialization version UID.
	 */
	private static final long serialVersionUID = 3254838824076890283L;
	private int id=-1;
	private User author;
	private String text="";
	private boolean textChanged=false;
	private boolean saved=false;
	private Timestamp creationTime;
	private Timestamp modificationTime;
	private int exp;
	
	/**
	 * Default constructor. Needed for the class to be a bean.
	 */
	public Comment() {
		creationTime = new Timestamp(new java.util.Date().getTime());
		modificationTime = new Timestamp(new java.util.Date().getTime());
	}

	/**
	 * Creates a new {@link Comment} with given author and text, belonging to experience
	 * whose ID is given.
	 * 
	 * @param author author of new comment
	 * @param text content of new comment
	 * @param experienceId ID of {@link Experience} which this {@link Comment} belongs to
	 */
	public Comment(User author, String text, int experienceId) {
		this();
		this.text = text;
		this.author = author;
		this.setExperienceId(experienceId);
	}

	/**
	 * Creates a {@link Comment} with given id, author and text, belonging to experience
	 * whose ID is given.
	 * 
	 * @param id id of the comment
	 * @param author author of the comment
	 * @param text content of the comment
	 * @param experienceId ID of {@link Experience} which this {@link Comment} belongs to
	 */
	public Comment(int id, User author, String text, int experienceId) {
		this(author, text, experienceId);
		this.id = id;
	}

	/**
	 * Creates a {@link Comment} with given id, author and text, belonging to experience
	 * whose ID is given.
	 * 
	 * @param id id of the comment
	 * @param author author of the comment
	 * @param text content of the comment
	 * @param exp {@link Experience} which this {@link Comment} belongs to
	 */
	public Comment(int id, User author, String text, Experience exp) {
		this(id, author, text, exp.getId());
	}
	
	/**
	 * Creates a new {@link Comment} with given author and text, belonging to experience
	 * whose ID is given.
	 * 
	 * @param author author of new comment
	 * @param text content of new comment
	 * @param exp {@link Experience} which this {@link Comment} belongs to
	 */
	public Comment(User author, String text, Experience exp) {
		this(author, text, exp.getId());
	}
	
	/**
	 * Returns ID of this comment
	 * @return I of this comment
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets ID of this comment to given value
	 * 
	 * @param id new ID to be set
	 * @return this comment
	 */
	public Comment setId(int id) {
		this.id = id;
		if (id < 0)
			this.saved = false;
		return this;
	}

	/**
	 * Returns content of this comment
	 * 
	 * @return content of this comment
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets content of this comment to given value
	 * 
	 * @param text new content to be set
	 * @return this comment
	 */
	public Comment setText(String text) {
		this.text = text;
		this.textChanged = true;
		this.saved = false;
		return this;
	}

	/**
	 * Returns whether the content of comment has changed.
	 * 
	 * @return whether the content of comment has changed
	 */
	public boolean isTextChanged() {
		return textChanged;
	}

	/**
	 * Returns author of this comment
	 * 
	 * @return author of this comment
	 */
	public User getAuthor() {
		return author;
	}

	/**
	 * Sets author of this comment. To be used by a BeanPropertyRowMapper
	 * 
	 * @param authorId author ID for new comment
	 */
	public Comment setAuthor(User author) {
		this.author = author;
		return this;
	}
	
	/**
	 * Returns whether the comment is saved.
	 * 
	 * @return whether the comment is saved
	 */
	public boolean isSaved() {
		return saved;
	}
	
	/**
	 * Mark comment as saved.
	 * 
	 * @return this comment
	 */
	public Comment setAsSaved() {
		this.textChanged = false;
		this.saved = true;
		return this;
	}
	
	/**
	 * Returns creation time of this comment.
	 * 
	 * @return the creation time of this comment
	 */
	public Timestamp getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets creation time of this comment to given value.
	 * 
	 * @param creationTime the creation time to set
	 * @return this comment
	 */
	public Comment setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	/**
	 * Returns modification time of this comment.
	 * 
	 * @return the modification time of this comment
	 */
	public Timestamp getModificationTime() {
		return modificationTime;
	}

	/**
	 * Sets modification time of this comment to given value.
	 * 
	 * @param modificationTime the modification time to set
	 * @return this comment
	 */
	public Comment setModificationTime(Timestamp modificationTime) {
		this.modificationTime = modificationTime;
		return this;
	}

	/**
	 * Returns ID of experience which this comment belongs to.
	 * 
	 * @return ID of experience which this comment belongs to
	 */
	public int getExperienceId() {
		return exp;
	}

	/**
	 * Changes experience which this comment belongs to the one having given ID
	 * 
	 * @param exp ID of experience which this comment will belong to
	 * @return this comment
	 */
	public Comment setExperienceId(int exp) {
		this.exp = exp;
		return this;
	}
}
