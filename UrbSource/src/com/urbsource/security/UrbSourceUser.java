package com.urbsource.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.urbsource.db.JDBCLoginDAO;
import com.urbsource.models.User;

public class UrbSourceUser extends org.springframework.security.core.userdetails.User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Store the connection (or DAO) object for updating the User object from database.
	 */
	private static JDBCLoginDAO dao;
	
	private User u;
	
	/**
	 * Return the User model attached with this object. Used for accessing user details.
	 * 
	 * @return The user attached with this object
	 */
	public final User getUser() {
		return u;
	}
	
	/**
	 * Update the stored User object via fetching the new User object from the database.
	 * This function is used only in the rare cases which a user's data has been changed
	 * and it needs to be regenerated within same request for security purposes, instead
	 * of using this function
	 */
	public void updateUser() {
		// update user using DAO object
		u = dao.getLoginUser(u.getUsername());
	}
	

	public UrbSourceUser(User u,
			JDBCLoginDAO dao,
			boolean enabled,
			boolean accountNonExpired,
			boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities){
		
		super(u.getUsername(), u.getPassword(), enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		// Switch to the new database connection, it won't do no harm, and it might be useful
		// for cleaning old DB connections
		UrbSourceUser.dao = dao;
		this.u = u;
	}
}
