package com.urbsource.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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

	/**
	 * Only to be used by Spring to set the data source
	 */
	public JDBCExperienceDAO() {
	}
	
	public JDBCExperienceDAO(JDBCUserDAO userDao, JDBCTagDAO tagDao) {
		super();
		this.userDao = userDao;
		this.tagDao = tagDao;
		this.insert = new SimpleJdbcInsert(jdbcTemplate)
						.withTableName("experience")
						.usingColumns("text", "author_id", "mood", "creation_time")
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
	 * Get a specific experience.
	 * 
	 * @param id ID of the experience
	 * @return Queried {@link Experience}
	 */
	public Experience getExperience(final int id) {
		String sql = "SELECT * FROM experience WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				User u = userDao.getUser(rs.getInt("author_id"));
				String text = rs.getString("text");
				List<Tag> tags = tagDao.getTags(id);
				Experience exp =  new Experience(id, u, text, tags)
					.setCreationTime(rs.getTimestamp("creation_time"))
					.setModificationTime(rs.getTimestamp("modification_time"))
					.setExpirationDate(rs.getDate("expiration_date"))
				    .setMood(rs.getString("mood")).setSpam(rs.getInt("spam"));
				User currentUser = userDao.getCurrentUser();
				if (currentUser != null)
					exp.setUserMarkedSpam(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM experience_spam WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {exp.getId(), currentUser.getId()}) > 0);
				exp.setNumberOfComments(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM comment WHERE experience_id=? LIMIT 1", new Object[] {exp.getId()}));
				return exp;
			}
			
		});
		
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
				Experience exp =  new Experience(id, u, text, tags)
				.setCreationTime(rs.getTimestamp("creation_time"))
				.setModificationTime(rs.getTimestamp("modification_time"))
				.setExpirationDate(rs.getDate("expiration_date"))
				.setMood(rs.getString("mood")).setSpam(rs.getInt("spam"));
				User currentUser = userDao.getCurrentUser();
				if (currentUser != null)
					exp.setUserMarkedSpam(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM experience_spam WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {exp.getId(), currentUser.getId()}) > 0);
				exp.setNumberOfComments(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM comment WHERE experience_id=? LIMIT 1", new Object[] {exp.getId()}));				
				return exp;
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
		String sql = "SELECT * FROM experience, rel_experience_tag AS rel WHERE rel.experience_id = experience.id AND rel.tag_id = ? ORDER BY experience.id DESC";
		return jdbcTemplate.query(sql, new Object[] { tag.getId() }, new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				List<Tag> tags = tagDao.getTags(id);
				User u = userDao.getUser(rs.getInt("author_id"));
				Experience e = new Experience(id, u, text, tags)
				.setCreationTime(rs.getTimestamp("creation_time"))
				.setModificationTime(rs.getTimestamp("modification_time"))
				.setExpirationDate(rs.getDate("expiration_date"))
				.setMood(rs.getString("mood")).setSpam(rs.getInt("spam"));
				User currentUser = userDao.getCurrentUser();
				if (currentUser != null)
					e.setUserMarkedSpam(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM experience_spam WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {e.getId(), currentUser.getId()}) > 0);
				e.setNumberOfComments(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM comment WHERE experience_id=? LIMIT 1", new Object[] {e.getId()}));
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

		Integer tag_ids[] = new Integer[tags.length];
		for (int i = 0; i < tags.length; ++i) {
			tag_ids[i] = tags[i].getId();
		}
		String qmarkstr = "?";
		for (int i = 1; i < tag_ids.length; ++i)
			qmarkstr += ",?";
		// The broken, old, rusty MySQL that lacks intelligent design completely
		// also doesn't support sending arrays in prepared statements. If hell
		// exists it must be a place where people are forced to use MySQL
		String sql = "SELECT * FROM experience WHERE EXISTS (" +
				"SELECT * FROM rel_experience_tag AS rel WHERE " +
				"rel.experience_id = experience.id AND rel.tag_id IN ("+ qmarkstr +")) ORDER BY experience.id DESC";
		
		System.out.println(sql);
		
		return jdbcTemplate.query(
				sql,
				tag_ids,
				new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				List<Tag> tags = tagDao.getTags(id);
				User u = userDao.getUser(rs.getInt("author_id"));
				Experience e = new Experience(id, u, text, tags)
				.setCreationTime(rs.getTimestamp("creation_time"))
				.setModificationTime(rs.getTimestamp("modification_time"))
				.setExpirationDate(rs.getDate("expiration_date"))
				.setMood(rs.getString("mood")).setSpam(rs.getInt("spam"));
				User currentUser = userDao.getCurrentUser();
				if (currentUser != null)
					e.setUserMarkedSpam(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM experience_spam WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {e.getId(), currentUser.getId()}) > 0);
				e.setNumberOfComments(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM comment WHERE experience_id=? LIMIT 1", new Object[] {e.getId()}));
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
		String sql = "SELECT * FROM experience WHERE UPPER(text) LIKE UPPER(CONCAT('%', ?, '%')) ORDER BY experience.id DESC";
		return jdbcTemplate.query(sql, new Object[] {text}, new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				List<Tag> tags = tagDao.getTags(id);
				User u = userDao.getUser(rs.getInt("author_id"));
				Experience e = new Experience(id, u, text, tags)
				.setCreationTime(rs.getTimestamp("creation_time"))
				.setModificationTime(rs.getTimestamp("modification_time"))
				.setExpirationDate(rs.getDate("expiration_date"))
				.setMood(rs.getString("mood")).setSpam(rs.getInt("spam"));
				User currentUser = userDao.getCurrentUser();
				if (currentUser != null)
					e.setUserMarkedSpam(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM experience_spam WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {e.getId(), currentUser.getId()}) > 0);
				e.setNumberOfComments(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM comment WHERE experience_id=? LIMIT 1", new Object[] {e.getId()}));
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
	public boolean createExperience(Experience exp) {
		// If experience already exists, exit immediately
		if (exp.getId() >= 0) {
			return false;
		}
		
		// Save experience to database
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("text", exp.getText());
		parameters.put("author_id", exp.getAuthor().getId());
		parameters.put("mood", exp.getMood());
		parameters.put("creation_time", new java.sql.Timestamp(new java.util.Date().getTime()));
		exp.setId(insert.executeAndReturnKey(parameters).intValue());
		SimpleJdbcInsert insertTag = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("rel_experience_tag")
			.usingColumns("tag_id", "experience_id")
			.usingGeneratedKeyColumns("id");
		MapSqlParameterSource sqlParameters[] = new MapSqlParameterSource[exp.getTags().size()];
		int i = 0;
		for (Tag t : exp.getTags()) {
			sqlParameters[i] = new MapSqlParameterSource();
			sqlParameters[i].addValue("experience_id", exp.getId());
			sqlParameters[i].addValue("tag_id", t.getId());
			i++;
		}
		insertTag.executeBatch(sqlParameters);
		exp.setAsSaved();
		return true;
	}

	/**
	 * Save an experience to the database. Create a new experience if it doesn't exist.
	 * 
	 * @param exp The experience to be saved.
	 * @return true if success, false if failure
	 */
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
		@SuppressWarnings("unchecked")
		HashMap<String, Object>[] H = new HashMap[0];
		int[] results = insertTag.executeBatch(sqlParameters.toArray(H));
		for (int i : results) {
			System.out.println(i);
		}
		exp.setAsSaved();
		return true;
		
	}
	
	public boolean deleteExperience(Experience exp) {
		if (exp.getId() < 0)
			return false;

		String sql = "DELETE FROM experience WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {exp.getId()});
		exp.setId(-1);
		return true;
	}

	/**
	 *  Get latest n experiences in the system.
	 * @param n Number of experiences to return.
	 * @return Latest n experiences in the system.
	 */
	public List<Experience> getRecentExperiences(int n) {
		return jdbcTemplate.query(
				"SELECT * FROM experience ORDER BY id DESC LIMIT ?",
				new Object[] { n },
				new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				List<Tag> tags = tagDao.getTags(id);
				User u = userDao.getUser(rs.getInt("author_id"));
				Experience e = new Experience(id, u, text, tags)
				.setCreationTime(rs.getTimestamp("creation_time"))
				.setModificationTime(rs.getTimestamp("modification_time"))
				.setExpirationDate(rs.getDate("expiration_date"))
				.setMood(rs.getString("mood")).setSpam(rs.getInt("spam"));
				User currentUser = userDao.getCurrentUser();
				if (currentUser != null)
					e.setUserMarkedSpam(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM experience_spam WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {e.getId(), currentUser.getId()}) > 0);
				e.setNumberOfComments(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM comment WHERE experience_id=? LIMIT 1", new Object[] {e.getId()}));
				e.setAsSaved();
				return e;
			}
			
		});
	}

	/**
	 * Get most popular n experiences in the system, which are the ones with maximum score.
	 * @param n Number of experiences to return.
	 * @return Most popular n experiences in the system.
	 */
	public List<Experience> getPopularExperiences(int n) {
		return jdbcTemplate.query(
				"SELECT * FROM experience ORDER BY (upvotes - downvotes) DESC LIMIT ?",
				new Object[] { n },
				new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				List<Tag> tags = tagDao.getTags(id);
				User u = userDao.getUser(rs.getInt("author_id"));
				Experience e = new Experience(id, u, text, tags)
				.setCreationTime(rs.getTimestamp("creation_time"))
				.setModificationTime(rs.getTimestamp("modification_time"))
				.setExpirationDate(rs.getDate("expiration_date"))
				.setMood(rs.getString("mood")).setSpam(rs.getInt("spam"));
				User currentUser = userDao.getCurrentUser();
				if (currentUser != null)
					e.setUserMarkedSpam(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM experience_spam WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {e.getId(), currentUser.getId()}) > 0);
				e.setNumberOfComments(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM comment WHERE experience_id=? LIMIT 1", new Object[] {e.getId()}));
				e.setAsSaved();
				return e;
			}
			
		});
	}
	
	/**
	 * Get hottest n experiences in the system, which are the ones with maximum score and are entered in last 2 weeks.
	 * @param n Number of experiences to return.
	 * @return Hottest n experiences in the system.
	 */
	public List<Experience> getHotExperiences(int n) {
		return jdbcTemplate.query(
				"SELECT * FROM experience WHERE creation_date > DATEADD(WEEK(), -2, NOW()) ORDER BY (upvotes - downvotes) DESC LIMIT ?",
				new Object[] { n },
				new RowMapper<Experience>() {

			@Override
			public Experience mapRow(ResultSet rs, int rowNumber)
					throws SQLException {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				List<Tag> tags = tagDao.getTags(id);
				User u = userDao.getUser(rs.getInt("author_id"));
				Experience e = new Experience(id, u, text, tags)
				.setCreationTime(rs.getTimestamp("creation_time"))
				.setModificationTime(rs.getTimestamp("modification_time"))
				.setExpirationDate(rs.getDate("expiration_date"))
				.setMood(rs.getString("mood")).setSpam(rs.getInt("spam"));
				User currentUser = userDao.getCurrentUser();
				if (currentUser != null)
					e.setUserMarkedSpam(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM experience_spam WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {e.getId(), currentUser.getId()}) > 0);
				e.setNumberOfComments(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM comment WHERE experience_id=? LIMIT 1", new Object[] {e.getId()}));
				e.setAsSaved();
				return e;
			}
			
		});
	}
	
	/**
	 * Filter tags according to query.
	 * @param query
	 * @return
	 */
	public Object getTagList(String query) {
		String sql = "SELECT * FROM tag WHERE UPPER(name) LIKE UPPER(CONCAT('%', ?, '%'))";
		List<Map<String,Object>> resultList = jdbcTemplate.queryForList(sql, new Object[] {query});
		return resultList;
	}
	
	public int cleanUpExpiredExperiences() {
		String sql = "DELETE FROM experience WHERE expiration_date <> NULL AND expiration_date < NOW()";
		return jdbcTemplate.update(sql);
	}

	public boolean markSpam(Experience e) {
		if (e.isUserMarkedSpam())
			return false;
		try {
			jdbcTemplate.update("INSERT INTO experience_spam (experience_id, user_id) VALUES(?, ?)", new Object[] {e.getId(), userDao.getCurrentUser().getId()});
			e.incrementSpam();
			if (e.exceedsSpamLimit()) {
				return deleteExperience(e);
			} else {
				saveExperience(e);
			}
		} catch (DataIntegrityViolationException ex) {
			return false;
		}
		return true;
	}

	public boolean unmarkSpam(Experience e) {
		if (! e.isUserMarkedSpam())
			return false;
		try {
			jdbcTemplate.update("DELETE FROM experience_spam WHERE experience_id=? AND user_id=?", new Object[] {e.getId(), userDao.getCurrentUser().getId()});
			e.decrementSpam();
			saveExperience(e);
		} catch (DataIntegrityViolationException ex) {
			return false;
		}
		return true;
	}
}
