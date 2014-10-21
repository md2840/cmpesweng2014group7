package com.urbsource.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.urbsource.db.JDBCLoginDAO;
import com.urbsource.models.User;

public class UrbSourceUserDetailsService implements UserDetailsService {
	
	private JDBCLoginDAO userdao = new JDBCLoginDAO();
	
	public void setUserdao(JDBCLoginDAO userdao) {
		this.userdao = userdao;
	}
	
	/**
	 * Bu method spring'in user service detailsindeki methodun ba�tan yaz�lmas�d�r.
	 * Databasedeki user bilgileriyle authenticationa g�nderilecek spring user'� yarat�r.
	 */

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		User u = userdao.getLoginUser(username);
		
		if (u==null)
			throw new UsernameNotFoundException("user name not found");
		
		String U_USERNAME = u.getUsername();
		String U_PASSWORD = u.getPassword();
		int U_ID= u.getId();
		
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		
	
		//Spring configurasyonunda login d���ndaki sayfalara giri� i�in ROLE_USER olmal�, olursa �teki rollerin kontrol� angularjs ile halledilecek.
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		UrbSourceUser user = new UrbSourceUser(U_ID,U_USERNAME,U_PASSWORD,enabled,accountNonExpired,credentialsNonExpired,accountNonLocked,authorities);
		
		return user;
	}
	
	
}
