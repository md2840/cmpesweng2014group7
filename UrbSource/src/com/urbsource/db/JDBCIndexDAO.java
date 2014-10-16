package com.urbsource.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCIndexDAO {

private static JdbcTemplate jdbcTemplate;
	
	public void setDataSource(DataSource dataSource) {

		try {
			jdbcTemplate = new JdbcTemplate(dataSource);
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	public String addName(String name){
		String sql = "INSERT INTO DEF_NAME (N_NAME)"
				+" VALUES (?)";
		String result = "";
		try {
			jdbcTemplate.update(sql, name);
			result="{\"success\":true}";
		} catch (DataAccessException e) {
			result="hata:"+e.getLocalizedMessage();
		}
		return result;
	}
	
	public HashMap<String,Object> getNames(){
		String sql = "SELECT * FROM DEF_NAME";
		List<Map<String,Object>> resultList = jdbcTemplate.queryForList(sql);
		HashMap<String,Object> result = new HashMap<String,Object>();
		result.put("result", resultList);
		result.put("success", true);
		return result;
	}
	
	public HashMap<String,Object> searchName(String query){
		String sql = "SELECT * FROM DEF_NAME WHERE UPPER(N_NAME) LIKE UPPER(CONCAT('%', ?, '%'))";
		List<Map<String,Object>> resultList = jdbcTemplate.queryForList(sql, new Object[] {query});
		HashMap<String,Object> result = new HashMap<String,Object>();
		result.put("result", resultList);
		result.put("success", true);
		return result;
	}
	
}
