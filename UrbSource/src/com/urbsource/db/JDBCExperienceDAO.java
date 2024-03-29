package com.urbsource.db;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sql.DataSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.urbsource.models.Comment;
import com.urbsource.models.Tag;
import com.urbsource.models.User;
import com.urbsource.models.Experience;

/**
 * Data Access Object to do experience-related tasks via communicating with DB.
 * 
 * @author Mehmet Emre
 */
@Repository
public class JDBCExperienceDAO {
	final int QUERY_LIMIT = 10;

	/**
	 * Row mapper class to convert query result rows to {@link Experience} objects.
	 * 
	 * @author Mehmet Emre
	 */
	private class ExperienceRowMapper implements RowMapper<Experience> {
		
		/**
		 * Map given {@link ResultSet} object to a {@link Experience} object.
		 * 
		 * @param rs a row of result
		 * @param rowNumber number of row in result table
		 * @return created {@link Experience} object
		 */
		@Override
		public Experience mapRow(ResultSet rs, int rowNumber) throws SQLException {
			int id = rs.getInt("id");
			User u = userDao.getUser(rs.getInt("author_id"));
			String text = rs.getString("text");
			List<Tag> tags = tagDao.getTags(id);
			Experience exp = new Experience(id, u, text, tags)
				.setCreationTime(rs.getTimestamp("creation_time"))
				.setModificationTime(rs.getTimestamp("modification_time"))
				.setExpirationDate(rs.getDate("expiration_date"))
			    .setMood(rs.getString("mood"))
			    .setPoints(rs.getInt("points"))
			    .setSpam(rs.getInt("spam"))
			    .setLocation(rs.getString("location"));
			// Try to set source of experience. If source is not given, default to empty
			// string.
			try {
				exp.setSource(rs.getString("source"));
			} catch (SQLException e) {
				exp.setSource("");
			}
			User currentUser = userDao.getCurrentUser();
			if (currentUser != null) {
				exp.setUserMarkedSpam(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM experience_spam WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {exp.getId(), currentUser.getId()}) > 0);
				SqlRowSet vote = jdbcTemplate.queryForRowSet("SELECT is_upvote FROM experience_vote WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {exp.getId(), currentUser.getId()});
				// If there is a vote
				if (vote.first()) {
					boolean isUpvote = vote.getBoolean("is_upvote");
					exp.setUpvotedByUser(isUpvote);
					exp.setDownvotedByUser(!isUpvote);
				}
			}
			exp.setNumberOfComments(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM comment WHERE experience_id=? LIMIT 1", new Object[] {exp.getId()}));
			return exp;
		}
	}
	
	private static JdbcTemplate jdbcTemplate;
	private JDBCUserDAO userDao;
	private JDBCTagDAO tagDao;
	private SimpleJdbcInsert insert;

	/**
	 * Only to be used by Spring to set the data source
	 */
	public JDBCExperienceDAO() {
	}
	
	/**
	 * The constructor to be used by controllers.
	 * 
	 * @param userDao {@link JDBCUserDAO} object to be used by this class to access users in DB
	 * @param userDao {@link JDBCTagDAO} object to be used by this class to access tags in DB
	 */
	public JDBCExperienceDAO(JDBCUserDAO userDao, JDBCTagDAO tagDao) {
		super();
		this.userDao = userDao;
		this.tagDao = tagDao;
		this.insert = new SimpleJdbcInsert(jdbcTemplate)
						.withTableName("experience")
						.usingColumns("text", "author_id", "mood", "creation_time","location", "expiration_date")
						.usingGeneratedKeyColumns("id");
	}

	/**
	 * Set data source to be used by all {@link JDBCExperienceDAO} objects to access DB.
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
	
	/**
	 * Get a specific experience.
	 * 
	 * @param id ID of the experience
	 * @return Queried {@link Experience}
	 */
	public Experience getExperience(final int id) {
		String sql = "SELECT * FROM experience WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, new ExperienceRowMapper());
		
	}
	
	/**
	 * Get all experiences created by a user.
	 * 
	 * @param u The author of all experiences
	 * @return List of experiences created by u
	 */
	public List<Experience> getExperiences(final User u) {
		String sql = "SELECT * FROM experience WHERE author_id = ?";
		return jdbcTemplate.query(sql, new Object[] { u.getId() }, new ExperienceRowMapper());
		
	}

	/**
	 * Get experiences containing one of given tags.
	 * 
	 * @param tag tag for querying
	 * @param limit maximum number of experiences required
	 * @return List of experiences containing given tag
	 */
	private List<Experience> getExperiences(List<Tag> tags, int limit) {
		if (limit <= 0 || tags.size() == 0)
			return new ArrayList<Experience>();
		StringBuilder tagSet = new StringBuilder("(").append(tags.get(0).getId());
		for (int i = 1, iMax = tags.size(); i < iMax; ++i) {
			tagSet.append(',').append(tags.get(i).getId());
		}
		tagSet.append(')');
		
		String sql = "SELECT * FROM experience, rel_experience_tag AS rel WHERE rel.experience_id = experience.id AND rel.tag_id in " + tagSet.toString() + " ORDER BY experience.points DESC LIMIT ?";
		return jdbcTemplate.query(sql, new Object[] { limit }, new ExperienceRowMapper());
	}
	
	/**
	 * Fill the rest of given experience list until its size reaches {@code QUERY_LIMIT} using
	 * semantic search.
	 * 
	 * @param experiences list of experiences to be filled
	 * @param tags tags that will be used by semantic search
	 * @return updated list of experiences
	 */
	private List<Experience> fillUsingSemanticSearch(List<Experience> experiences, Tag[] tags) {
		for (Experience exp : experiences)
			if (exp.getSource() == null || exp.getSource().isEmpty())
				exp.setSource("search");
		HttpURLConnection http = null;
		HashSet<String> semanticTagNames = new HashSet<String>();
		for (Tag tag : tags) {
			try {
				// Query ConceptNet5 to get related words (tag names)
				URL url = new URL("http://conceptnet5.media.mit.edu/data/5.2/assoc/c/en/" + URLEncoder.encode(tag.getName().replace(' ', '_')) + "?filter=/c/en");
				http = (HttpURLConnection) url.openConnection();
				http.setRequestMethod("GET");
				http.setUseCaches (false);
				http.setDoInput(true);
				http.setDoOutput(false);
				//Get Response    
				InputStream is = http.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				String line;
				StringBuffer response = new StringBuffer(); 
				
				while((line = rd.readLine()) != null) {
					response.append(line);
					response.append('\r');
				}
				
				rd.close();
				
				// Construct tag array
				JSONArray results = new JSONObject(response.toString()).getJSONArray("similar");
				for (int i = 0; i < results.length(); ++i) {
					String[] categories = results.getJSONArray(i).getString(0).split("/");
					String tagName = categories[categories.length - 1];
					semanticTagNames.addAll(Arrays.asList(tagName.split("_")));
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				if (http != null)
					http.disconnect();
			}
		}
		ArrayList<Tag> semanticTags = new ArrayList<Tag>();
		
		for (String tagName : semanticTagNames)
			semanticTags.add(tagDao.getTag(tagName));
			
		experiences.addAll(getExperiences(semanticTags, QUERY_LIMIT - experiences.size()));
		
		for (Experience exp : experiences)
			if (exp.getSource() == null || exp.getSource().isEmpty())
				exp.setSource("semantic");
		
		if (experiences.size() >= QUERY_LIMIT)
			return experiences;

		// Even semantic tagging didn't give enough results, fill the rest with popular and recent experiences
		experiences.addAll(getRecentAndPopularExperiences(QUERY_LIMIT - experiences.size()));
		return experiences;
	}
	
	/**
	 * Get experiences containing given tag or similar tags using semantic tagging.
	 * 
	 * @param tag tag for querying
	 * @return List of experiences containing given tag
	 */
	public List<Experience> getSimilarExperiences(Tag tag) {
		String sql = "SELECT * FROM experience, rel_experience_tag AS rel WHERE rel.experience_id = experience.id AND rel.tag_id = ? ORDER BY experience.id DESC LIMIT " + QUERY_LIMIT;
		List<Experience> experiences = jdbcTemplate.query(sql, new Object[] { tag.getId() }, new ExperienceRowMapper());
		if (experiences.size() >= QUERY_LIMIT)
			return experiences;
		return fillUsingSemanticSearch(experiences, new Tag[] { tag });
	}

	/**
	 * Get experiences containing given tags or similar tags.
	 * 
	 * @param tags List of tags for searching experiences containing them
	 * @return List of experiences containing given tags
	 */
	public List<Experience> getSimilarExperiences(final Tag[] tags) {

		Integer tag_ids[] = new Integer[tags.length];
		for (int i = 0; i < tags.length; ++i) {
			tag_ids[i] = tags[i].getId();
		}
		String tagQuery = "EXISTS (SELECT 1 FROM rel_experience_tag AS rel WHERE rel.experience_id = experience.id AND rel.tag_id = ?)";
		StringBuilder qmarkstr = new StringBuilder(tagQuery);
		for (int i = 1; i < tag_ids.length; ++i) {
			qmarkstr.append(" AND ");
			qmarkstr.append(tagQuery);
		}
		// The broken, old, rusty MySQL that lacks intelligent design completely
		// also doesn't support sending arrays in prepared statements. If hell
		// exists it must be a place where people are forced to use MySQL
		String sql = "SELECT * FROM experience WHERE " + qmarkstr + " ORDER BY experience.id DESC LIMIT " + QUERY_LIMIT;
		List<Experience> experiences = jdbcTemplate.query(sql, tag_ids, new ExperienceRowMapper());
		
		if (experiences.size() >= QUERY_LIMIT)
			return experiences;
		
		return fillUsingSemanticSearch(experiences, tags);
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
		String sql = "SELECT * FROM experience WHERE UPPER(text) LIKE UPPER(CONCAT('%', ?, '%')) ORDER BY experience.id DESC LIMIT " + QUERY_LIMIT;
		List<Experience> experiences = jdbcTemplate.query(sql, new Object[] { text.trim() }, new ExperienceRowMapper());

		if (experiences.size() >= QUERY_LIMIT)
			return experiences;
		
		ArrayList<Tag> tags = new ArrayList<Tag>();
		for (String word : text.trim().split("\\W")) {
			word = word.trim();
			if (word.isEmpty())
				continue;
			tags.add(tagDao.getTag(word));
		}
		return fillUsingSemanticSearch(experiences, tags.toArray(new Tag[] {}));
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
		parameters.put("location", exp.getLocation());
		parameters.put("creation_time", exp.getCreationTime());
		parameters.put("expiration_date", exp.getExpirationDate());
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
		insertTag.executeBatch(sqlParameters.toArray(H));
		exp.setAsSaved();
		return true;
		
	}
	
	/**
	 * Delete given experience from database.
	 * 
	 * @param exp experience to be deleted from database
	 * @return success of delete operation
	 */
	public boolean deleteExperience(Experience exp) {
		if (exp.getId() < 0)
			return false;

		String sql = "DELETE FROM experience WHERE id = ?";
		jdbcTemplate.update(sql, new Object[] {exp.getId()});
		exp.setId(-1);
		return true;
	}

	/**
	 *  Get n experiences chosen among latest experiences and most popular experiences in the system.
	 *  
	 * @param n Number of experiences to return.
	 * @return A selection of n experiences among latest and most popular experiences in the system.
	 */
	public List<Experience> getRecentAndPopularExperiences(int n) {
		List<Experience> experiences = jdbcTemplate.query(
				"(SELECT 'recent' AS source, experience.* FROM experience ORDER BY modification_time DESC LIMIT ?) "
				+ "UNION ALL (SELECT 'popular', experience.* FROM experience ORDER BY points DESC LIMIT ?)",
				new Object[] { n/2, n - n/2 /* in case n is odd, there will be one more popular experience */ },
				new ExperienceRowMapper());
		Collections.shuffle(experiences, new Random(2));
		return experiences;
	}

	/**
	 * Get most popular n experiences in the system, which are the ones with maximum score.
	 * 
	 * @param n Number of experiences to return.
	 * @return Most popular n experiences in the system.
	 */
	public List<Experience> getPopularExperiences(int n) {
		return jdbcTemplate.query(
				"SELECT * FROM experience ORDER BY points DESC LIMIT ?",
				new Object[] { n },
				new ExperienceRowMapper());
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
				new ExperienceRowMapper());
	}
	
	/**
	 * Filter tags according to query.
	 * 
	 * @param query
	 * @return
	 */
	public Object getTagList(String query) {
		String sql = "SELECT * FROM tag WHERE UPPER(name) LIKE UPPER(CONCAT('%', ?, '%'))";
		List<Map<String,Object>> resultList = jdbcTemplate.queryForList(sql, new Object[] {query});
		return resultList;
	}
	
	/**
	 * Remove expired transient experiences from database.
	 * 
	 * @return number of removed experiences
	 */
	public int cleanUpExpiredExperiences() {
		String sql = "DELETE FROM experience WHERE expiration_date <> NULL AND expiration_date < NOW()";
		return jdbcTemplate.update(sql);
	}

	/**
	 * Mark given experience as spam by current user.
	 * 
	 * @param e experience to be marked as spam
	 * @return success of operation
	 */
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
	
	public boolean markSpamMobile(Experience e,User u) {
		
		try {
			jdbcTemplate.update("INSERT INTO experience_spam (experience_id, user_id) VALUES(?, ?)", new Object[] {e.getId(), u.getId()});
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
	public boolean unmarkSpamMobile(Experience e,User u) {
		
		try {
			jdbcTemplate.update("DELETE FROM experience_spam WHERE experience_id=? AND user_id=?", new Object[] {e.getId(), u.getId()});
			e.decrementSpam();
			saveExperience(e);
		} catch (DataIntegrityViolationException ex) {
			return false;
		}
		return true;
	}

	/**
	 * Unmark given experience as spam by current user.
	 * 
	 * @param e experience whose spam mark will be removed
	 * @return success of operation
	 */
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
	
	/**
	 * Sets the upvotes, downvotes and spams for experiences given username
	 * for mobile users since userDao.getUser wont work for them.
	 */
	public void configureVotes(String username, Experience exp){
		
		User currentUser = userDao.getLoginUser(username);
		if (currentUser != null) {
			exp.setUserMarkedSpam(jdbcTemplate.queryForInt("SELECT COUNT(*) FROM experience_spam WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {exp.getId(), currentUser.getId()}) > 0);
			SqlRowSet vote = jdbcTemplate.queryForRowSet("SELECT is_upvote FROM experience_vote WHERE experience_id=? AND user_id=? LIMIT 1", new Object[] {exp.getId(), currentUser.getId()});
			// If there is a vote
			if (vote.first()) {
				boolean isUpvote = vote.getBoolean(0);
				exp.setUpvotedByUser(isUpvote);
				exp.setDownvotedByUser(!isUpvote);
			}
		}
		
	}
}
