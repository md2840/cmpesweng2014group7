
package com.group7.urbsourceandroid.models;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

public class Notification implements Serializable {
    
	private static final long serialVersionUID = 1L;
	private int id=-1;
	private User user;
	private String text;
    private Timestamp time;
	/**
	 * To be a bean.
	 */
	public Notification() {}
	
	
	public Notification(int id, User _user, String text) {
		this.text = text;
		this.id = id;
		this.user = _user;
		this.setTime(new Timestamp((new Date()).getTime()));
	}

	public Notification(User _user, String text) {
		this.text = text;
		this.user = _user;
		this.setTime(new Timestamp((new Date()).getTime()));
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


	public User getUser() {
		return user;
	}

	/**
	 * To be used by a BeanPropertyRowMapper
	 *  
	 */
	public void setUser(User _user) {
		this.user = _user;
	}


	public Timestamp getTime() {
		return time;
	}


	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	
}
