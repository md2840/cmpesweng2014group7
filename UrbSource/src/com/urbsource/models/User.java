package com.urbsource.models;

import java.io.Serializable;
import java.util.regex.*;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	/**
	 * For password confirmation
	 */
	private String password2;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public boolean isEmailValid() {
		return Pattern.matches("\\w+@\\w+(\\w|[.-_])*\\.[a-zA-Z]+", email);
	}
}
