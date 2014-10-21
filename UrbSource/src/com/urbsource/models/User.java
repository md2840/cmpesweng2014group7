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
		if (! Pattern.matches("\\w+@\\w+(\\w|[.-_])*\\.[a-zA-Z]+", email)) {
			throw new IllegalArgumentException("The string '" + email + "' is not a valid e-mail address");
		}
		this.email = email;
	}

}
