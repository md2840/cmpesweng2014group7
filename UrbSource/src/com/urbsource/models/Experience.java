package com.urbsource.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Experience implements Serializable {

	/**
	 * Compiler-generated serialization version UID.
	 */
	private static final long serialVersionUID = 9212137404203406823L;
	private int id=-1;
	private User author;
	private String text;
	private ArrayList<Tag> tags;
	private ArrayList<Tag> removedTags=new ArrayList<Tag>();
	private ArrayList<Tag> addedTags=new ArrayList<Tag>();
	private boolean textChanged=false;
	private boolean saved=false;
	
	/**
	 * To be a bean.
	 */
	public Experience() {}
	
	public Experience(int id, User author, String text, List<Tag> tags) {
		this.text = text;
		this.id = id;
		this.tags = new ArrayList<Tag>();
		this.tags.addAll(tags);
		this.author = author;
	}

	public Experience(User author, String text, List<Tag> tags) {
		this.text = text;
		this.tags = new ArrayList<Tag>(tags);
		this.author = author;
	}
	
	public Experience(int id, User author, String text, Tag[] tags) {
		this.text = text;
		this.id = id;
		this.tags = new ArrayList<Tag>(Arrays.asList(tags));
		this.author = author;
	}

	public Experience(User author, String text, Tag[] tags) {
		this.text = text;
		this.tags = new ArrayList<Tag>(Arrays.asList(tags));
		this.author = author;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
		if (id < 0)
			this.saved = false;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.textChanged = true;
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
	public void setAuthor(User author) {
		this.author = author;
	}
	
	public boolean isSaved() {
		return saved;
	}
	
	public void setAsSaved() {
		this.saved = true;
	}
}
