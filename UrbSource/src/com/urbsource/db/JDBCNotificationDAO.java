/*
 * @author Mustafa Demirel
 */
package com.urbsource.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.urbsource.models.Notification;
import com.urbsource.models.User;

@Repository
public class JDBCNotificationDAO {

	private static JdbcTemplate jdbcTemplate;
	private JDBCUserDAO userDao;
	private SimpleJdbcInsert insert;

	/**
	 * Only to be used by Spring to set the data source
	 */
	public JDBCNotificationDAO() {
	}
	
	public JDBCNotificationDAO(JDBCUserDAO userDao) {
		super();
		this.userDao = userDao;
		this.insert = new SimpleJdbcInsert(jdbcTemplate)
						.withTableName("notification")
						.usingColumns("text", "user_id")
						.usingGeneratedKeyColumns("id");
	}
	
	public void setDataSource(DataSource dataSource) {
		try {
			jdbcTemplate = new JdbcTemplate(dataSource);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a specific notification.
	 * 
	 * @param id ID of the notification
	 * @return Queried {@link Notification}
	 */
	public Notification getNotification(final int id) {
		String sql = "SELECT * FROM notification WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<Notification>() {

			@Override
			public Notification mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				User u = userDao.getUser(rs.getInt("author_id"));
				String text = rs.getString("text");
				Notification not =  new Notification(id, u, text);
				not.setTime(rs.getTimestamp("time"));
				return not;
			}
			
		});
		
	}
	
	/**
	 * 
	 * 
	 * @param u The user of all notifications
	 * @return List of notifications of u
	 */
	public List<Notification> getNotifications(final User u, final Timestamp t) {
		String sql = "SELECT * FROM notification WHERE user_id = ? AND time>?";
		return jdbcTemplate.query(sql, new Object[] { u.getId(),t }, new RowMapper<Notification>() {

			@Override
			public Notification mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				Notification not =  new Notification(id, u, text);
				not.setTime(rs.getTimestamp("time"));
				return not;
			}
			
		});
		
	}
	
	/**
	 * Get all notifications of a user.
	 * 
	 * @param u The user of all notifications
	 * @return List of notifications of u
	 */
	public List<Notification> getNotifications(final User u) {
		String sql = "SELECT * FROM notification WHERE user_id = ?";
		return jdbcTemplate.query(sql, new Object[] { u.getId() }, new RowMapper<Notification>() {

			@Override
			public Notification mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				Notification not =  new Notification(id, u, text);
				not.setTime(rs.getTimestamp("time"));
				return not;
			}
			
		});
		
	}


	/**
	 * Create a new Notification. Use this *if and only if* you are creating
	 * a new notification, *never* use this to update an existing Notification!
	 * 
	 * @param not The notification to be created.
	 * @return true if success, false if failure
	 */
	public boolean createNotification(Notification not) {
		// If Notification already exists, exit immediately
		if (not.getId() >= 0) {
			return false;
		}
		// Save experience to database
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("text", not.getText());
		parameters.put("user_id", not.getUser().getId());
		not.setId(insert.executeAndReturnKey(parameters).intValue());
		return true;
	}

	
	
	public boolean deleteNotification(Notification not) {
		if (not.getId() < 0)
			return false;

		String sql = "DELETE FROM notification WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {not.getId()});
		not.setId(-1);
		return true;
	}



}
