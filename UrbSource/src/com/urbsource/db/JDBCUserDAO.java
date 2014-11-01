package com.urbsource.db;

import javax.sql.DataSource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.urbsource.models.Experience;
import com.urbsource.models.User;

@Repository
public class JDBCUserDAO {

	private static JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {

		try {
			jdbcTemplate = new JdbcTemplate(dataSource);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public User getLoginUser(String username){
		String sql = "SELECT * FROM user WHERE username = ?";
		User u = JDBCUserDAO.jdbcTemplate.queryForObject(
				sql,
				new Object[] { username },
				new BeanPropertyRowMapper<User>(User.class));
		sql = "SELECT COUNT(id) FROM experience WHERE author_id = ?";
		u.setNumberOfExperiences(jdbcTemplate.queryForInt(
				sql,
				new Object[] { u.getId() }));
		return u;
	}
	
	public User getUser(int id){
		String sql = "SELECT * FROM user WHERE id = ?";
		User u = JDBCUserDAO.jdbcTemplate.queryForObject(
				sql,
				new Object[] { id },
				new BeanPropertyRowMapper<User>(User.class));
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
		String sql = "SELECT COUNT(1) FROM user WHERE username = ? LIMIT 1";
		int count = jdbcTemplate.queryForObject(sql, Integer.class, user.getUsername());
		if (! user.isEmailValid()) {
			throw new DataIntegrityViolationException("User email is invalid");
		}
		if (count > 0) {
			// user exists, so update the user in database:
			sql = "update user set password=?, first_name=?, last_name=?, email=?, experience_points=?, comment_points=? where username = ?";
			jdbcTemplate.update(sql,
					user.getPassword(),
					user.getFirstName(),
					user.getLastName(),
					user.getEmail(),
					user.getExperiencePoints(),
					user.getCommentPoints(),
					user.getUsername());
			} else {
			// user doesn't exist, insert user to database:
			this.createUser(user);
		}
	}

	/**
	 * Creates a new user in database, if user exists throws an DataIntegrityViolationException.
	 * 
	 * @param user User object to create
	 * @author Mehmet Emre
	 */
	public void createUser(User user) {
		if (! user.isEmailValid()) {
			throw new DataIntegrityViolationException("User email is invalid");
		}
		String sql = "insert into user (password, first_name, last_name, email, experience_points, comment_points, username) VALUES(?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql,
				user.getPassword(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getExperiencePoints(),
				user.getCommentPoints(),
				user.getUsername());
	}
	
	public boolean deleteUser(User user) {
		String sql = "DELETE user WHERE id = ?";
		jdbcTemplate.update(sql, user.getId());
		return true;
	}
}
