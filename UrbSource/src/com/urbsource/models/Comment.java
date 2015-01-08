package com.urbsource.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Date;
import java.sql.Timestamp;

public class Comment implements Serializable {

	/**
	 * 
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
	 * To be a bean.
	 */
	public Comment() {
		creationTime = new Timestamp(new java.util.Date().getTime());
		modificationTime = new Timestamp(new java.util.Date().getTime());
	}

	public Comment(User author, String text, int experienceId) {
		this();
		this.text = text;
		this.author = author;
		this.setExperienceId(experienceId);
	}

	public Comment(int id, User author, String text, int experienceId) {
		this(author, text, experienceId);
		this.id = id;
	}

	public Comment(int id, User author, String text, Experience exp) {
		this(id, author, text, exp.getId());
	}
	
	public Comment(User author, String text, Experience exp) {
		this(author, text, exp.getId());
	}
	
	public int getId() {
		return id;
	}
	
	public Comment setId(int id) {
		this.id = id;
		if (id < 0)
			this.saved = false;
		return this;
	}

	public String getText() {
		return text;
	}

	public Comment setText(String text) {
		this.text = text;
		this.textChanged = true;
		this.saved = false;
		return this;
	}

	public boolean isTextChanged() {
		return textChanged;
	}

	public User getAuthor() {
		return author;
	}

	/**
	 * To be used by a BeanPropertyRowMapper
	 * 
	 * @param authorId author ID for new experience
	 */
	public Comment setAuthor(User author) {
		this.author = author;
		return this;
	}
	
	public boolean isSaved() {
		return saved;
	}
	
	public Comment setAsSaved() {
		this.textChanged = false;
		this.saved = true;
		return this;
	}
	
	/**
	 * @return the creationTime
	 */
	public Timestamp getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public Comment setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
		return this;
	}

	/**
	 * @return the modificationTime
	 */
	public Timestamp getModificationTime() {
		return modificationTime;
	}

	/**
	 * @param modificationTime the modificationTime to set
	 */
	public Comment setModificationTime(Timestamp modificationTime) {
		this.modificationTime = modificationTime;
		return this;
	}

	public int getExperienceId() {
		return exp;
	}

	public Comment setExperienceId(int exp) {
		this.exp = exp;
		return this;
	}
}
