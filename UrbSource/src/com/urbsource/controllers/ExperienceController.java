package com.urbsource.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.urbsource.db.*;
import com.urbsource.models.*;

@Controller
@RequestMapping("/experience/*")
public class ExperienceController {
	
	JDBCUserDAO userDao;
	JDBCTagDAO tagDao;
	JDBCExperienceDAO expDao;
	
	public ExperienceController(){
		userDao = new JDBCUserDAO();
		tagDao = new JDBCTagDAO();
		expDao = new JDBCExperienceDAO(userDao, tagDao);
	}
	
	@RequestMapping(value="/recent", method=RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> recent(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("experiences", expDao.getRecentExperiences(10));
		return map;
	}

	@RequestMapping(value="/create", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> create(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			map.put("success", false);
			map.put("error", "You must log in to create experience");
			return map;
		}
		User u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
		JSONArray tagArray = json.getJSONArray("tags");
		Tag tags[] = new Tag[tagArray.length()];
		for (int i = 0, len = tagArray.length(); i < len; ++i)
			tags[i] = tagDao.getTag(tagArray.getString(i));
		Experience exp = new Experience(u, json.getString("text"), tags);
		map.put("success", expDao.createExperience(exp));
		return map;
		
	}

	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> delete(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			map.put("success", false);
			map.put("error", "You must log in to delete your experiences");
			return map;
		}
		int id;
		try {
			id = json.getInt("id");
		} catch (JSONException e) {
			map.put("success", false);
			map.put("error", "Experience ID is not given or not int, check how you call the API!");
			return map;
		}
		User u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
		Experience exp = expDao.getExperience(id);
		if (u.getId() != exp.getAuthor().getId()) {
			map.put("success", false);
			map.put("error", "You cannot delete others' experiences");
			return map;
		}
		map.put("success", expDao.deleteExperience(exp));
		return map;
		
	}
	
	@RequestMapping(value="/searchTag", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> searchTag(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		map.put("tagList", expDao.getTagList(json.getJSONObject("params").getString("query")));
		return map;
	}
	
	@RequestMapping(value="searchExperienceTag", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> searchExperienceTag(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		Tag tag = new Tag(json.getJSONObject("params").getString("name"), json.getJSONObject("params").getInt("id"));
		map.put("experienceList", expDao.getExperiences(tag));
		return map;
	}
	
	@RequestMapping(value="searchExperienceText", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> searchExperienceText(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		map.put("experienceList", expDao.searchExperiences(json.getJSONObject("params").getString("text")));
		return map;
	}
	
	public String getBody(HttpServletRequest request) throws IOException
	{
		String body = null;
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			InputStream inputStream = request.getInputStream();
			if (inputStream != null) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		body = stringBuilder.toString();
		body = "{ \"result\": "+body + "}";
		return body;
	}
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> update(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			map.put("success", false);
			map.put("error", "You must log in to update your experiences");
			return map;
		}
		int id;
		try {
			id = json.getInt("id");
		} catch (JSONException e) {
			map.put("success", false);
			map.put("error", "Experience ID is not given or not int, check how you call the API!");
			return map;
		}
		User u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
		Experience exp = expDao.getExperience(id);
		if (u.getId() != exp.getAuthor().getId()) {
			map.put("success", false);
			map.put("error", "You can update only your experiences!");
			return map;
		}
		JSONArray tagArray = json.getJSONArray("tags");
		Tag tags[] = new Tag[tagArray.length()];
		for (int i = 0, len = tagArray.length(); i < len; ++i)
			tags[i] = tagDao.getTag(tagArray.getString(i));
		HashSet<Tag> oldTags = new HashSet<Tag>(exp.getTags());
		oldTags.removeAll(Arrays.asList(tags));
		for (Tag t : oldTags) {
			exp.removeTag(t);
		}
		for (Tag t : tags) {
			exp.addTag(t);
		}
		exp.setText(json.getString("text"));
		expDao.saveExperience(exp);
		map.put("success", true);
		return map;
	}

}
