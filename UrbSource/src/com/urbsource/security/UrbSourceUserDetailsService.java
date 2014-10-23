package com.urbsource.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.urbsource.db.JDBCUserDAO;
import com.urbsource.models.User;

public class UrbSourceUserDetailsService implements UserDetailsService {
	
	private JDBCUserDAO userdao = new JDBCUserDAO();
	
	public void setUserdao(JDBCUserDAO userdao) {
		this.userdao = userdao;
	}
	
	/**
	 * Create UserDetails object to be checked for credentials using Spring Security framework.
	 */

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		User u = userdao.getLoginUser(username);
		
		if (u==null)
			throw new UsernameNotFoundException("user name not found");
		
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		
	
		/*
		 * Pages besides public pages like login and sign up must require authority ROLE_USER.
		 */
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		UrbSourceUser user = new UrbSourceUser(u, userdao, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		
		return user;
	}
	
	
}
