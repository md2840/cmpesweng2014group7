package com.urbsource.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

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
			jdbcTemplate = new JdbcTemplate(dataSource);
			System.out.println("constructortry");
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public User getLoginUser(String username){
		System.out.println("method" + username);
		User u;
		String sql = "SELECT * FROM DEF_USER_LOGIN WHERE U_USERNAME = ?";
		u = JDBCLoginDAO.jdbcTemplate.queryForObject(
				sql,
				new Object[] { username },
				new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				System.out.println(rowNum);
				User u = new User();
				u.setId(rs.getInt("U_ID"));
				u.setUsername(rs.getString("U_USERNAME"));
				u.setPassword(rs.getString("U_PASSWORD"));
				return u;
			}
		});

		return u;
	}
	
	/**
	 * Saves User object to the database. Creates a new user object if user does
	 * not exist, otherwise only updates user information
	 * 
	 * @param user User object to save
	 * @author Mehmet Emre
	 */
	public void saveUser(User user){
		// Check whether user already exists
		String sql = "SELECT COUNT(1) FROM DEF_USER_LOGIN WHERE U_USERNAME = ? LIMIT 1";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, user.getUsername());
		
		if (count > 0) {
			// user exists, so update the user in database:
			sql = "update DEF_USER_LOGIN set U_PASSWORD=? where U_USERNAME = ?";
		} else {
			// user doesn't exist, insert user to database:
			sql = "insert into DEF_USER_LOGIN (U_PASSWORD, U_USERNAME) VALUES(?, ?)";
		}
		jdbcTemplate.update(sql, user.getPassword(), user.getUsername());
	}
}
