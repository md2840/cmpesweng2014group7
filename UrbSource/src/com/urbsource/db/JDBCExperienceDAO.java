package com.urbsource.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.urbsource.models.Tag;
import com.urbsource.models.User;
import com.urbsource.models.Experience;

@Repository
public class JDBCExperienceDAO {

	private static JdbcTemplate jdbcTemplate;
	private JDBCUserDAO userDao;
	private JDBCTagDAO tagDao;
	private SimpleJdbcInsert insert;

	public JDBCExperienceDAO(JDBCUserDAO userDao, JDBCTagDAO tagDao) {
		super();
		this.userDao = userDao;
		this.tagDao = tagDao;
	}
	
	public void setDataSource(DataSource dataSource) {
		try {
			jdbcTemplate = new JdbcTemplate(dataSource);
			this.insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("experience");
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * Get all experiences created by a user.
	 * 
	 * @param u The author of all experiences
	 * @return List of experiences created by u
	 */
	public List<Experience> getExperiences(final User u) {
		String sql = "SELECT * FROM experience WHERE author_id = ?";
		return jdbcTemplate.query(sql, new Object[] { u.getId() }, new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				List<Tag> tags = tagDao.getTags(id);
				return new Experience(id, u, text, tags);
			}
			
		});
		
	}
	
	/**
	 * Get experiences containing given tag.
	 * 
	 * @param tag Given tag
	 * @return List of experiences containing given tag
	 */
	public List<Experience> getExperiences(Tag tag) {
		String sql = "SELECT * FROM experience, rel_experience_tag AS rel WHERE rel.experience_id = experience.id AND rel.tag_id = ?";
		return jdbcTemplate.query(sql, new Object[] { tag.getId() }, new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				List<Tag> tags = tagDao.getTags(id);
				User u = userDao.getUser(id);
				Experience e = new Experience(id, u, text, tags);
				e.setAsSaved();
				return e;
			}
			
		});
	}

	/**
	 * Get experiences containing given tags.
	 * 
	 * @param tags List of tags for searching experiences containing them
	 * @return List of experiences containing given tags
	 */
	public List<Experience> getExperiences(final Tag[] tags) {
		
		return jdbcTemplate.query(
				new PreparedStatementCreator() {
					
					@Override
					public PreparedStatement createPreparedStatement(
							Connection conn) throws SQLException {
						Integer tag_ids[] = new Integer[tags.length];
						for (int i = 0; i < tags.length; ++i) {
							tag_ids[i] = tags[i].getId();
						}
						String sql = "SELECT * FROM experience WHERE EXISTS (" +
								"SELECT * FROM rel_experience_tag AS rel WHERE " +
								"rel.experience_id = experience.id AND rel.tag_id IN ?)";
						PreparedStatement p = conn.prepareStatement(sql);
						p.setArray(0, conn.createArrayOf("int", tag_ids));
						return p;
					}
					
				},
				new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				List<Tag> tags = tagDao.getTags(id);
				User u = userDao.getUser(id);
				Experience e = new Experience(id, u, text, tags);
				e.setAsSaved();
				return e;
			}
			
		});
		
	}
	
	/**
	 * Full-text search for experiences.
	 * 
	 * @param text the text looked up
	 * @return experiences containing given text
	 */
	public List<Experience> searchExperiences(String text) {
		// If our MySQL server had a newer version we would use 
		// the following query and get all the fantastic goodies
		// but we can't :/
		//String sql = "SELECT * FROM experience WHERE MATCH(text) AGAINST (?)";
		String sql = "SELECT * FROM experience WHERE UPPER(text) LIKE UPPER(CONCAT('%', ?, '%'))";
		return jdbcTemplate.query(sql, new Object[] {text}, new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				List<Tag> tags = tagDao.getTags(id);
				User u = userDao.getUser(id);
				Experience e = new Experience(id, u, text, tags);
				e.setAsSaved();
				return e;
			}
			
		});
		
	}
	
	/**
	 * Create a new experience. Use this *if and only if* you are creating
	 * a new experience, *never* use this to update an existing experience!
	 * 
	 * @param exp The experience to be created.
	 * @return true if success, false if failure
	 */
	@SuppressWarnings("unchecked")
	public boolean createExperience(Experience exp) {
		// If experience already exists, exit immediately
		if (exp.getId() >= 0) {
			return false;
		}
		
		// Save experience to database
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("text", exp.getText());
		parameters.put("author_id", exp.getAuthor().getId());
		exp.setId(insert.executeAndReturnKey(parameters).intValue());
		SimpleJdbcInsert insertTag = new SimpleJdbcInsert(jdbcTemplate).withTableName("rel_experience_tag");
		ArrayList<HashMap<String, Object>> sqlParameters = new ArrayList<HashMap<String,Object>>(exp.getTags().size());
		for (Tag t : exp.getTags()) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("experience_id", exp.getId());
			params.put("tag_id", t.getId());
			sqlParameters.add(params);
		}
		insertTag.executeBatch((HashMap<String, Object>[])sqlParameters.toArray());
		exp.setAsSaved();
		return true;
	}

	/**
	 * Save an experience to the database. Create a new experience if it doesn't exist.
	 * 
	 * @param exp The experience to be saved.
	 * @return true if success, false if failure
	 */
	@SuppressWarnings("unchecked")
	public boolean saveExperience(Experience exp) {
		if (exp.getId() < 0) {
			return createExperience(exp);
		}
		
		// Save experience to database
		String sql = "UPDATE experience SET text=?, author_id=? WHERE id=?";
		jdbcTemplate.update(sql, exp.getText(), exp.getAuthor().getId(), exp.getId());
		// Remove existing tags
		for (Tag t : exp.getAddedTags()) {
			sql = "DELETE rel_experience_tag WHERE experience_id = ? AND tag_id = ?";
			// unfortunately there is no batch delete in Spring
			jdbcTemplate.update(sql, exp.getId(), t.getId());
		}
		// Insert new tags
		SimpleJdbcInsert insertTag = new SimpleJdbcInsert(jdbcTemplate).withTableName("rel_experience_tag");
		ArrayList<HashMap<String, Object>> sqlParameters = new ArrayList<HashMap<String,Object>>(exp.getAddedTags().size());
		for (Tag t : exp.getAddedTags()) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("experience_id", exp.getId());
			params.put("tag_id", t.getId());
			sqlParameters.add(params);
		}
		insertTag.executeBatch((HashMap<String, Object>[])sqlParameters.toArray());
		exp.setAsSaved();
		return true;
		
	}
	
	public boolean deleteExperience(Experience exp) {
		if (exp.getId() < 0)
			return false;

		String sql = "DELETE experience WHERE experience_id = ?";
		jdbcTemplate.update(sql, exp.getId());
		exp.setId(-1);
		return true;
	}
}
