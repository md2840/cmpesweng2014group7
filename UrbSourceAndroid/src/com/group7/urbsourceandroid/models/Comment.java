package com.group7.urbsourceandroid.models;

import java.io.Serializable;


public class Comment implements Serializable {

	/**
	 * Compiler-generated serialization version UID.
	 */
	private static final long serialVersionUID = 9212137404203406823L;
	private int id=-1;
	private User author;
	private String text;
	
	
	public Comment() {
		
	}
	
	public Comment(User author, String text) {
		this();
		this.text = text;
		this.author = author;
	}
	
	
	
	public int getId() {
		return id;
	}
	
	public Comment setId(int id) {
		this.id = id;
		return this;
	}

	public String getText() {
		return text;
	}

	public Comment setText(String text) {
		this.text = text;
		return this;
	}

	public User getAuthor() {
		return author;
	}
	

	/**
	 * To be used by a BeanPropertyRowMapper
	 * 
	 * @param author ID for new experience
	 */
	public Comment setAuthor(User author) {
		this.author = author;
		return this;
	}
	


	
}
