package com.urbsource.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.urbsource.models.Comment;
import com.urbsource.models.Experience;
import com.urbsource.models.User;

@Repository
public class JDBCCommentDAO {
	final int QUERY_LIMIT = 10;
	
	private class CommentRowMapper implements RowMapper<Comment> {
		@Override
		public Comment mapRow(ResultSet rs, int rowNumber) throws SQLException {
			int id = rs.getInt("id");
			User u = userDao.getUser(rs.getInt("author_id"));
			String text = rs.getString("text");
			Comment comment = new Comment(id, u, text, rs.getInt("experience_id"))
				.setCreationTime(rs.getTimestamp("creation_time"))
				.setModificationTime(rs.getTimestamp("modification_time"));
			return comment;
		}
	}
	
	private static JdbcTemplate jdbcTemplate;
	private JDBCUserDAO userDao;
	private SimpleJdbcInsert insert;

	/**
	 * Only to be used by Spring to set the data source
	 */
	public JDBCCommentDAO() {
	}
	
	public JDBCCommentDAO(JDBCUserDAO userDao) {
		super();
		this.userDao = userDao;
		this.insert = new SimpleJdbcInsert(jdbcTemplate)
						.withTableName("comment")
						.usingColumns("text", "author_id", "experience_id", "creation_time")
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
	 * Get a specific comment.
	 * 
	 * @param id ID of the comment
	 * @return Queried {@link Comment}
	 */
	public Comment getComment(final int id) {
		String sql = "SELECT * FROM comment WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, new CommentRowMapper());
		
	}
	

	/**
	 * Get comments belonging given experience
	 * 
	 * @param exp {@link Experience} whose comments are queried
	 * @return List of comments belonging given experience
	 */
	public List<Comment> getComments(Experience exp) {
		String sql = "SELECT * FROM comment WHERE experience_id = ? ORDER BY creation_time DESC";
		return jdbcTemplate.query(sql, new Object[] { exp.getId() }, new CommentRowMapper());
	}
	
	/**
	 * Create a new comment. Use this *if and only if* you are creating
	 * a new comment, *never* use this to update an existing comment!
	 * 
	 * @param comment The comment to be created.
	 * @return true if success, false if failure
	 */
	public boolean createComment(Comment comment) {
		// If experience already exists, exit immediately
		if (comment.getId() >= 0) {
			return false;
		}
		
		// Save experience to database
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("text", comment.getText());
		parameters.put("author_id", comment.getAuthor().getId());
		parameters.put("experience_id", comment.getExperienceId());
		parameters.put("creation_time", new java.sql.Timestamp(new java.util.Date().getTime()));
		comment.setId(insert.executeAndReturnKey(parameters).intValue());
		comment.setAsSaved();
		return true;
	}

	/**
	 * Save an comment to the database. Create a new comment if it doesn't exist.
	 * 
	 * @param comment The experience to be saved.
	 * @return true if success, false if failure
	 */
	public boolean saveComment(Comment comment) {
		if (comment.getId() < 0) {
			return createComment(comment);
		}
		
		// Save comment to database
		String sql = "UPDATE comment SET text=?, author_id=? WHERE id=?";
		jdbcTemplate.update(sql, comment.getText(), comment.getAuthor().getId(), comment.getId());
		comment.setAsSaved();
		return true;
	}
	
	public boolean deleteComment(Comment comment) {
		if (comment.getId() < 0)
			return false;

		String sql = "DELETE FROM comment WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {comment.getId()});
		comment.setId(-1);
		return true;
	}
}
