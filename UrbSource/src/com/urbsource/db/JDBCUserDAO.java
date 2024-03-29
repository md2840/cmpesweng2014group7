package com.urbsource.db;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.urbsource.models.User;

/**
 * Data Access Object to do user-related tasks via communicating with DB.
 */
@Repository
public class JDBCUserDAO {

	private static JdbcTemplate jdbcTemplate;

	/**
	 * Set data source to be used by all {@link JDBCUserDAO} objects to access DB.
	 * 
	 * @param dataSource data source to be used for accessing the DB
	 */
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
		sql = "SELECT COUNT(*) FROM experience WHERE author_id = ?";
		u.setNumberOfExperiences(jdbcTemplate.queryForInt(
				sql,
				new Object[] { u.getId() }));
		return u;
	}
	
	/**
	 * This is for checking whether mobile login username exists
	 * */
	public User getMobileUser(String username){
		String sql = "SELECT * FROM user WHERE username = ?";
		List<User> ulist = JDBCUserDAO.jdbcTemplate.query(
				sql,
				new Object[] { username },
				new BeanPropertyRowMapper<User>(User.class));
		
		if(ulist.isEmpty()){
			return null;
		}else{
			return ulist.get(0);
		}
		
	}
	
	/**
	 * Get a user from database, whose ID is given.
	 * 
	 * @param id ID of requested user
	 * @return the fetched {@link User} object
	 * @author Mehmet Emre
	 */
	public User getUser(int id){
		String sql = "SELECT * FROM user WHERE id = ?";
		User u = JDBCUserDAO.jdbcTemplate.queryForObject(
				sql,
				new Object[] { id },
				new BeanPropertyRowMapper<User>(User.class));
		sql = "SELECT COUNT(*) FROM experience WHERE author_id = ?";
		int expNo = jdbcTemplate.queryForInt(
				sql,
				new Object[] { u.getId() });
		u.setNumberOfExperiences(expNo);
		return u;
	}
	
	/***
	 * Finds the user which is searched with email address.
	 * 
	 * @param email email address of the wanted user
	 * @return user which is looked for
	 * @author Gokce Yesiltas
	 */
	public User getUser(String email){
		String sql = "SELECT * FROM user WHERE email = ?";
		User u = JDBCUserDAO.jdbcTemplate.queryForObject(
				sql,
				new Object[] { email },
				new BeanPropertyRowMapper<User>(User.class));
		sql = "SELECT COUNT(*) FROM experience WHERE author_id = ?";
		u.setNumberOfExperiences(jdbcTemplate.queryForInt(
				sql,
				new Object[] { u.getId() }));
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
		String sql = "insert into user (password, first_name, last_name, email, experience_points, comment_points, username) VALUES(?, ?, ?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql,
				user.getPassword(),
				user.getFirstName(),
				user.getLastName(),
				user.getEmail(),
				user.getExperiencePoints(),
				user.getCommentPoints(),
				user.getUsername());
	}

	/**
	 * Deletes a user from database.
	 * 
	 * @param user User object to be deleted
	 * @return success of operation
	 * @author Mehmet Emre
	 */
	public boolean deleteUser(User user) {
		String sql = "DELETE user WHERE id = ?";
		jdbcTemplate.update(sql, user.getId());
		return true;
	}
	
    /**
     * Get current user object.
     * 
     * @return User model for current User, null if anonymous.
     */
    public User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken)
			return null;
		return getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
    }
}
