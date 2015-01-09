package com.group7.urbsourceandroid.models;

import java.io.Serializable;
import java.util.regex.Pattern;



public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;

	private int expPoints=0;
	private int commentPoints=0;
	/**
	 * For password confirmation
	 */
	private String password2;
	private int numberOfExperiences;
	
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

	/**
	 * Karma is kind of user level that is dependent on both experience and
	 * comment points. We currently define it with formula :
	 * 
	 * floor(log(karma = alpha * expPoints + beta * commentPoints + 1)) + 1
	 * 
	 * where alpha and beta are coefficients and value in log is greater or equal to 1 so that
	 * karma is positive.
	 * 
	 * @return karma of the user.
	 */
	public int getKarma() {
		double pureKarma = expPoints + commentPoints + 1;
		if (pureKarma < 1)
			pureKarma = 1;
		return (int)Math.log(pureKarma) + 1;
	}

	public int getExperiencePoints() {
		return expPoints;
	}

	public void setExperiencePoints(int expPoints) {
		this.expPoints = expPoints;
	}

	public int getCommentPoints() {
		return commentPoints;
	}

	public void setCommentPoints(int commentPoints) {
		this.commentPoints = commentPoints;
	}
	
	public int getNumberOfExperiences() {
		return numberOfExperiences;
	}
	
	/**
	 * To be only used from DAO
	 * 
	 * @param numberOfExperiences
	 */
	public void setNumberOfExperiences(int numberOfExperiences) {
		this.numberOfExperiences = numberOfExperiences;
	}
	
	
}
