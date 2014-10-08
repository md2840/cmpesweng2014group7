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
	 * Bu method spring'in user service detailsindeki methodun baþtan yazýlmasýdýr.
	 * Databasedeki user bilgileriyle authenticationa gönderilecek spring user'ý yaratýr.
	 */

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		User u = userdao.getLoginUser(username);
		
		if (u==null)
			throw new UsernameNotFoundException("user name not found");
		
		String U_USERNAME = u.getU_USERNAME();
		String U_PASSWORD = u.getU_PASSWORD();
		int U_ID= u.getU_ID();
		
		boolean enabled = true;
		boolean accountNonExpired = true;
		boolean credentialsNonExpired = true;
		boolean accountNonLocked = true;
		
	
		//Spring configurasyonunda login dýþýndaki sayfalara giriþ için ROLE_USER olmalý, olursa öteki rollerin kontrolü angularjs ile halledilecek.
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		UrbSourceUser user = new UrbSourceUser(U_ID,U_USERNAME,U_PASSWORD,enabled,accountNonExpired,credentialsNonExpired,accountNonLocked,authorities);
		
		return user;
	}
	
	
}
