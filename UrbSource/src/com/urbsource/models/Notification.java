/**
 * @author Mustafa Demirel
 */
package com.urbsource.models;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;
/**
 * Model representing a notification in database.
 * 
 * @author Mustafa Demirel
 */
public class Notification implements Serializable {
	/**
	 * Compiler-generated serialization version UID.
	 */
	private static final long serialVersionUID = 1L;
	private int id=-1;
	private User user;
	private String text;
    private Timestamp time;
	/**
	 * Default constructor. Needed for the class to be a bean.
	 */
	public Notification() {}
	
	/**
	 * Creates an {@link Notification} with given id, user and text.
	 * @author Mustafa Demirel
	 * @param id ID of the notification
	 * @param user user of the notification
	 * @param text content of the notification
	 */
	public Notification(int id, User _user, String text) {
		this.text = text;
		this.id = id;
		this.user = _user;
		this.setTime(new Timestamp((new Date()).getTime()));
	}
	/**
	 * Creates an {@link Experience} with given user and text.
	 * @author Mustafa Demirel
	 * @param user user of the notification
	 * @param text content of the notification
	 */
	public Notification(User _user, String text) {
		this.text = text;
		this.user = _user;
		this.setTime(new Timestamp((new Date()).getTime()));
	}
	
	/**
	 * Returns ID of the notification.
	 * 
	 * @return ID of the notification
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Sets id of the notification to given value
	 * 
	 * @param id content to be set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Returns text of the notification.
	 * 
	 * @return text of the notification
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets text content of the notification to given value
	 * 
	 * @param text content to be set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns user of the notification.
	 * 
	 * @return user of the notification
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets user of the notification to given value
	 * 
	 * @param user content to be set
	 */
	public void setUser(User _user) {
		this.user = _user;
	}

	/**
	 * Returns time of the notification.
	 * 
	 * @return time of the notification
	 */
	public Timestamp getTime() {
		return time;
	}

	/**
	 * Sets time of the notification to given value
	 * 
	 * @param time content to be set
	 */
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	
}
