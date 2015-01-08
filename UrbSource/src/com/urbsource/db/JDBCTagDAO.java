package com.urbsource.db;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.urbsource.models.Tag;

/**
 * Data Access Object to do tag-related tasks via communicating with DB.
 * 
 * @author Mehmet Emre
 */
@Repository
public class JDBCTagDAO {

	private static JdbcTemplate jdbcTemplate;

	/**
	 * Set data source to be used by all {@link JDBCTagDAO} objects to access DB.
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
	 * Get tag from database, create the tag if it doesn't exist
	 * 
	 * @param tag The tag requested
	 * @return the created/fetched tag
	 */
	public Tag getTag(String tag) {
		tag = tag.trim().toLowerCase(); // keep all tags lowercase and trimmed
		// try getting tag from DB
		String sql = "SELECT * FROM tag WHERE name = ?";
		Tag t = null;
		try {
			t = jdbcTemplate.queryForObject(
				sql,
				new Object[] { tag },
				new BeanPropertyRowMapper<Tag>(Tag.class));
		} catch (EmptyResultDataAccessException e) {
			// if tag doesn't exist create the tag, then call self
			sql = "insert into tag (name) values(?)";
			// if update is successful, call self to get the tag from DB
			// otherwise return null
			if (jdbcTemplate.update(sql, tag) > 0)
				t = getTag(tag);
		}
		return t;
	}

	/**
	 * Get tag from database, create the tag if it doesn't exist
	 * 
	 * @param experience_id ID of experience whose tags are requested
	 * @return list of fetched tags
	 */
	public List<Tag> getTags(int experience_id) {
		String sql = "SELECT tag.name AS name, tag.id AS id FROM tag, rel_experience_tag "
				+ "WHERE tag.id = rel_experience_tag.tag_id AND rel_experience_tag.experience_id = ?";
		return jdbcTemplate.query(
				sql,
				new Object[] { experience_id },
				new BeanPropertyRowMapper<Tag>(Tag.class));
	}
}
