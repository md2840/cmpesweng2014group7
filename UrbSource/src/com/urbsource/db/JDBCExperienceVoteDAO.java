package com.urbsource.db;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.urbsource.models.User;
import com.urbsource.models.Experience;

@Repository
public class JDBCExperienceVoteDAO {
	
	@Autowired
	private static JdbcTemplate jdbcTemplate;
	
	private SimpleJdbcInsert insert;

	
	public void setDataSource(DataSource dataSource) {
		try {
			jdbcTemplate = new JdbcTemplate(dataSource);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a specific experience's upvote.
	 * 
	 * @param id ID of the experience
	 * @return Queried {@link Integer}
	 */
	public int getUpvote(final int id) {
		String sql = "SELECT COUNT(*) FROM experience_vote WHERE is_upvote=1 AND experience_id = ? ";
		return jdbcTemplate.queryForObject(sql, new Object[] { id },Integer.class); 

			
	}
	
	/**
	 * Get a specific experience's downvote.
	 * 
	 * @param id ID of the experience
	 * @return Queried {@link Integer}
	 */
	public int getDownvote(final int id) {
		String sql = "SELECT COUNT(*) FROM experience_vote WHERE is_upvote=0 AND experience_id = ? ";
		return jdbcTemplate.queryForObject(sql, new Object[] { id },Integer.class); 
			
	}
	
	
	/**
	 * Create a new experience vote. 
	 * 
	 * @param exp The experience to be added vote
	 * @param u User that adds the vote
	 * @param Vote  whether its upvote and downvote
	 * @return true if success, false if failure
	 */
	public boolean createVote(Experience exp,User u, boolean isUpvote) {
		// If vote already exists, exit immediately
		String sql = "SELECT COUNT(*) FROM experience_vote WHERE experience_id = ? AND user_id = ?";
		Integer count=0;
		
		count = jdbcTemplate.queryForObject(sql, new Object[] {exp.getId(),u.getId()},Integer.class);
		System.out.println(count);
		
		
		if (count > 0) {
			return false;
		}
		insert = new SimpleJdbcInsert(jdbcTemplate)
		.withTableName("experience_vote")
		.usingColumns("experience_id", "user_id", "is_upvote");
		
		// Save vote to database
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("experience_id", exp.getId());
		parameters.put("user_id", u.getId());
		parameters.put("is_upvote", isUpvote ? 1 : 0);
	    insert.execute(parameters);
		sql = "UPDATE experience SET votes=votes+1, points=points+? WHERE id = ?";
		jdbcTemplate.update(sql, isUpvote ? 1 : -1, exp.getId());
		return true;
	}

	/**
	 * Save an experience vote to the database. Create a new experience vote if it doesn't exist.
	 * 
	 * @param exp The experience to be added vote
	 * @param u User that adds the vote
	 * @param Vote  whether its upvote and downvote
	 * @return true if success, false if failure
	 */
	public boolean saveVote(Experience exp, User u, boolean isUpvote) {
		
		String sql = "SELECT COUNT(*) FROM experience_vote WHERE experience_id = ? AND user_id = ?";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { exp.getId(),u.getId() },Integer.class); 
	
		if (count == 0) {
			return createVote(exp, u, isUpvote);
		}
		
		// Save vote to database
		sql = "UPDATE experience_vote SET is_upvote=? WHERE experience_id = ? AND user_id = ?";
		jdbcTemplate.update(sql, isUpvote ? 1 : 0, exp.getId(), u.getId());
		sql = "UPDATE experience SET points=points+? WHERE id = ?";
		jdbcTemplate.update(sql, isUpvote ? 2 : -2, exp.getId());
		return true;
		
	}
	
	public boolean deleteVote(Experience exp,User u) {
		String sql = "SELECT COUNT(*) FROM experience_vote WHERE experience_id = ? AND user_id = ?";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { exp.getId(),u.getId() },Integer.class); 
	
		if (count == 0)
			return false;

		sql = "SELECT is_upvote FROM experience_vote WHERE experience_id = ? AND user_id = ?";
		boolean isUpvote  = jdbcTemplate.queryForObject(sql, new Object[] { exp.getId(),u.getId() }, Integer.class) > 0;
		
		sql = "DELETE FROM experience_vote WHERE experience_id = ? AND user_id = ?";
		jdbcTemplate.update(sql, new Object[] {exp.getId(),u.getId()});
		sql = "UPDATE experience SET votes=votes-1, points=points+? WHERE id = ?";
		jdbcTemplate.update(sql, isUpvote ? -1 : 1, exp.getId());
		return true;
	}
	
}