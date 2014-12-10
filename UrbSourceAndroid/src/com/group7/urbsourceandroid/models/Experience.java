package com.group7.urbsourceandroid.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.Date;
import java.sql.Timestamp;

public class Experience implements Serializable {

	/**
	 * Compiler-generated serialization version UID.
	 */
	private static final long serialVersionUID = 9212137404203406823L;
	private int id=-1;
	private User author;
	private String text;
	private String mood="good";
	private ArrayList<Tag> tags;
	private ArrayList<Tag> removedTags=new ArrayList<Tag>();
	private ArrayList<Tag> addedTags=new ArrayList<Tag>();
	private boolean textChanged=false;
	private boolean saved=false;
	private int spam = 0;
	public final int SPAM_THRESHOLD = 5000;
	private Timestamp creationTime;
	private Timestamp modificationTime;
	private Date expirationDate;
	private boolean userMarkedSpam=false;
	
	/**
	 * To be a bean.
	 */
	public Experience() {
		creationTime = new Timestamp(new java.util.Date().getTime());
		modificationTime = new Timestamp(new java.util.Date().getTime());
		expirationDate = new Date(new java.util.Date().getTime());
	}
	
	public Experience(int id, User author, String text, List<Tag> tags) {
		this(author, text, tags);
		this.id = id;
	}

	public Experience(User author, String text, List<Tag> tags) {
		this();
		this.text = text;
		this.tags = new ArrayList<Tag>(tags);
		this.author = author;
	}
	
	public Experience(int id, User author, String text, Tag[] tags) {
		this(author, text, tags);
		this.id = id;
	}

	public Experience(User author, String text, Tag[] tags) {
		this();
		this.text = text;
		this.tags = new ArrayList<Tag>(Arrays.asList(tags));
		this.author = author;
	}
	
	public int getId() {
		return id;
	}
	
	public Experience setId(int id) {
		this.id = id;
		if (id < 0)
			this.saved = false;
		return this;
	}

	public String getText() {
		return text;
	}

	public Experience setText(String text) {
		this.text = text;
		this.textChanged = true;
		return this;
	}

	public boolean isTextChanged() {
		return textChanged;
	}

	public boolean isTagsChanged() {
		return !(addedTags.isEmpty() && removedTags.isEmpty());
	}

	public boolean addTag(Tag t) {
		if (tags.contains(t))
			return false;

		tags.add(t);
		
		if (removedTags.contains(t))
			removedTags.remove(t);
		else
			addedTags.add(t);

		saved = false;
		return true;
	}
	
	public boolean removeTag(Tag t) {
		if (removedTags.contains(t))
			return false;

		tags.add(t);
		
		if (addedTags.contains(t))
			addedTags.remove(t);
		else
			removedTags.add(t);
		
		saved = false;
		return true;
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}
	public ArrayList<Tag> getRemovedTags() {
		return removedTags;
	}

	public ArrayList<Tag> getAddedTags() {
		return addedTags;
	}

	public User getAuthor() {
		return author;
	}

	/**
	 * To be used by a BeanPropertyRowMapper
	 * 
	 * @param authorId author ID for new experience
	 */
	public Experience setAuthor(User author) {
		this.author = author;
		return this;
	}
	
	public boolean isSaved() {
		return saved;
	}
	
	public Experience setAsSaved() {
		this.saved = true;
		return this;
	}

	public String getMood() {
		return mood;
	}

	public Experience setMood(String mood) {
		this.mood = mood.trim().toLowerCase();
		return this;
	}

	public int getSpam() {
		return spam;
	}
	
	public Experience setSpam(int spam) {
		this.spam = spam;
		return this;
	}
	
	public boolean exceedsSpamLimit() {
		return spam > SPAM_THRESHOLD;
	}

	public void incrementSpam() {
		this.spam++;
	}
	
	public void decrementSpam() {
		this.spam--;
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
	public Experience setCreationTime(Timestamp creationTime) {
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
	public Experience setModificationTime(Timestamp modificationTime) {
		this.modificationTime = modificationTime;
		return this;
	}

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate the expirationDate to set
	 */
	public Experience setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
		return this;
	}

	/**
	 * @return the userMarkedSpam
	 */
	public boolean isUserMarkedSpam() {
		return userMarkedSpam;
	}

	/**
	 * @param userMarkedSpam the userMarkedSpam to set
	 */
	public Experience setUserMarkedSpam(boolean userMarkedSpam) {
		this.userMarkedSpam = userMarkedSpam;
		return this;
	}
}
