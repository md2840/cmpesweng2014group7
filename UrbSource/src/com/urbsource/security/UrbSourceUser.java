package com.urbsource.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UrbSourceUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int U_ID;
	private String U_USERNAME;
	private String U_PASSWORD;
	public int getU_ID() {
		return U_ID;
	}
	public void setU_ID(int u_ID) {
		U_ID = u_ID;
	}
	public String getU_USERNAME() {
		return U_USERNAME;
	}
	public void setU_USERNAME(String u_USERNAME) {
		U_USERNAME = u_USERNAME;
	}
	public String getU_PASSWORD() {
		return U_PASSWORD;
	}
	public void setU_PASSWORD(String u_PASSWORD) {
		U_PASSWORD = u_PASSWORD;
	}
	

	public UrbSourceUser(int U_ID,String U_USERNAME,String U_PASSWORD,boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,Collection<? extends GrantedAuthority> authorities){
		
		super(U_USERNAME, U_PASSWORD, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		
		this.U_ID = U_ID;
		this.U_USERNAME = U_USERNAME;
		this.U_PASSWORD = U_PASSWORD;
		
	}
}
