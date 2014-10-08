package com.urbsource.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.urbsource.models.User;

@Repository
public class JDBCLoginDAO {

	private static JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {
		System.out.println("constructor");

		try {
			this.jdbcTemplate = new JdbcTemplate(dataSource);
			System.out.println("constructortry");
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public User getLoginUser(String username){
		System.out.println("method" + username);
		User u;
		String sql = "SELECT * FROM DEF_USER_LOGIN WHERE U_USERNAME = '"+username+"'";
		u = JDBCLoginDAO.jdbcTemplate.queryForObject(sql, new RowMapper<User>(){
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				System.out.println(rowNum);
				User u = new User();
				u.setU_ID(rs.getInt("U_ID"));
				u.setU_USERNAME(rs.getString("U_USERNAME"));
				u.setU_PASSWORD(rs.getString("U_PASSWORD"));
				return u;
			}
		});
		System.out.print("hebedeeeee");
		int U_ID = u.getU_ID();
		//TODO bu i�lem ger�ekle�irken gereken �teki user bilgileri bu U_ID'den �ekilebilir.
		
		return u;
	}
}
