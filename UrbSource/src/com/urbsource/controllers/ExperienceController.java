package com.urbsource.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
	JDBCExperienceVoteDAO voteDao;
	
	public ExperienceController(){
		userDao = new JDBCUserDAO();
		tagDao = new JDBCTagDAO();
		expDao = new JDBCExperienceDAO(userDao, tagDao);
		voteDao = new JDBCExperienceVoteDAO(); 
	}

	@RequestMapping(value="/recent", method=RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> recentAndPopular(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("experiences", expDao.getRecentAndPopularExperiences(10));
		return map;
	}
	
	@RequestMapping(value="/user/json/{userId}", method=RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> userExperienceJSON(@PathVariable(value="userId") int userId, HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("experiences", expDao.getExperiences(userDao.getUser(userId)));
		return map;
	}

	@RequestMapping(value="/user/{userId}", method=RequestMethod.GET)
	public ModelAndView userInfo(@PathVariable(value="userId") int userId, Model model) {
		User u = new User();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
		}
		model.addAttribute("user", u);
		return new ModelAndView("user_experience_list", "user_", userDao.getUser(userId));
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
		Experience exp = new Experience(u, json.getString("text"), tags).setMood(json.getString("mood"));
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
			id = json.getJSONObject("params").getInt("id");
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

	@RequestMapping(value="/downvote", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> downvote(HttpServletRequest request, HttpServletResponse response, Model model) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			map.put("success", false);
			map.put("error", "You must log in to vote experiences");
			return map;
		}
		try {
			JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
			int exp_id = json.getInt("id");
			boolean undo = json.getBoolean("undo");
			User u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
			Experience exp = expDao.getExperience(exp_id);
			if (undo)
				map.put("success", voteDao.deleteVote(exp, u));
			else
				map.put("success", voteDao.saveVote(exp, u, false));
		} catch (Exception e) {
			map.put("success", false);
			map.put("error", e.getMessage());
		}
		return map;
	}

	@RequestMapping(value="/upvote", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> upvote(HttpServletRequest request, HttpServletResponse response, Model model) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			map.put("success", false);
			map.put("error", "You must log in to vote experiences");
			return map;
		}
		try {
			JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
			int exp_id = json.getInt("id");
			boolean undo = json.getBoolean("undo");
			User u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
			Experience exp = expDao.getExperience(exp_id);
			if (undo)
				map.put("success", voteDao.deleteVote(exp, u));
			else
				map.put("success", voteDao.saveVote(exp, u, true));
		} catch (Exception e) {
			map.put("success", false);
			map.put("error", e.getMessage());
		}
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
		JSONArray json2 = json.getJSONObject("params").getJSONArray("tags");
		Tag[] tags = new Tag[json2.length()];
		for(int i =0; i<json2.length();i++){
			tags[i] = new Tag(json2.getJSONObject(i).getString("name"),json2.getJSONObject(i).getInt("id"));
		}
		map.put("experiences", expDao.getExperiences(tags));
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
	
	
	@RequestMapping(value="/editText", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> updateText(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result").getJSONObject("params");
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
		exp.setText(json.getString("text"));
		expDao.saveExperience(exp);
		map.put("success", true);
		return map;
	}

	@RequestMapping(value="/cleanUpExpired")
	public @ResponseBody HashMap<String, Object> cleanUpExpired(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("count", expDao.cleanUpExpiredExperiences());
		return map;
		
	}
	
	@RequestMapping(value="/markSpam", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> markSpam(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			map.put("success", false);
			map.put("error", "You must log in to report spam");
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
		Experience exp = expDao.getExperience(id);
		map.put("success", expDao.markSpam(exp));
		return map;
	}
	
	@RequestMapping(value="/unmarkSpam", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> unmarkSpam(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			map.put("success", false);
			map.put("error", "You must log in to undo your spam report");
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
		Experience exp = expDao.getExperience(id);
		map.put("success", expDao.unmarkSpam(exp));
		return map;
	}
	/*
	 * The Web API for the mobile access.
	 * @author = dilara kekulluoglu
	 * 
	 *
	 */
	
	@RequestMapping(value="/mobilerecent", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> mobilerecentAndPopular(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		String username = json.getString("username");
		
		List<Experience> list = expDao.getRecentAndPopularExperiences(10);
		for(int i=0; i<list.size(); i++){
			//configureVotes(username,list.get(i));
		}
		map.put("experiences", list);
		return map;
	}
	/**
	 * Handles POST requests to Create experience page  from mobile app
	 * 
	 * @author dilara kekulluoglu
	 */
	@RequestMapping(value="/mobilecreate", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> mobileCreate(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		String username = json.getString("username");
		boolean hasAuthority = json.getBoolean("IsLoggedIn");
		if(!hasAuthority){
			map.put("success", false);
			map.put("error", "You must log in to create experience");
			return map;
		}
		
		User u = userDao.getLoginUser(username);
		JSONArray tagArray = json.getJSONArray("tags");
		Tag tags[] = new Tag[tagArray.length()];
		for (int i = 0, len = tagArray.length(); i < len; ++i)
			tags[i] = tagDao.getTag(tagArray.getString(i));
		Experience exp = new Experience(u, json.getString("text"), tags).setMood(json.getString("mood"));
		map.put("success", expDao.createExperience(exp));
		return map;
		
	}
	
	/**
	 * Handles POST requests to delete experience page  from mobile app
	 * 
	 * @author dilara kekulluoglu
	 */
	@RequestMapping(value="/mobiledelete", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> mobileDelete(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		
		String username = json.getString("username");
		boolean hasAuthority = json.getBoolean("IsLoggedIn");
		if(!hasAuthority){
			map.put("success", false);
			map.put("error", "You must log in to delete experience");
			return map;
		}
		
		int id;
		try {
			id = json.getJSONObject("params").getInt("id");
		} catch (JSONException e) {
			map.put("success", false);
			map.put("error", "Experience ID is not given or not int, check how you call the API!");
			return map;
		}
		User u = userDao.getLoginUser(username);
		Experience exp = expDao.getExperience(id);
		if (u.getId() != exp.getAuthor().getId()) {
			map.put("success", false);
			map.put("error", "You cannot delete others' experiences");
			return map;
		}
		map.put("success", expDao.deleteExperience(exp));
		return map;
		
	}
	
	@RequestMapping(value="/mobiledownvote", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> mobileDownvote(HttpServletRequest request, HttpServletResponse response, Model model) throws JSONException, IOException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		
		String username = json.getString("username");
		boolean hasAuthority = json.getBoolean("IsLoggedIn");
		if(!hasAuthority){
			map.put("success", false);
			map.put("error", "You must log in to downvote experience");
			return map;
		}
		
		try {
			
			int exp_id = json.getInt("id");
			boolean undo = json.getBoolean("undo");
			User u = userDao.getLoginUser(username);
			Experience exp = expDao.getExperience(exp_id);
			if (undo)
				map.put("success", voteDao.deleteVote(exp, u));
			else
				map.put("success", voteDao.saveVote(exp, u, false));
		} catch (Exception e) {
			map.put("success", false);
			map.put("error", e.getMessage());
		}
		return map;
	}

	@RequestMapping(value="/mobileupvote", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> mobileUpvote(HttpServletRequest request, HttpServletResponse response, Model model) throws JSONException, IOException {
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		
		String username = json.getString("username");
		boolean hasAuthority = json.getBoolean("IsLoggedIn");
		if(!hasAuthority){
			map.put("success", false);
			map.put("error", "You must log in to downvote experience");
			return map;
		}
		try {
			int exp_id = json.getInt("id");
			boolean undo = json.getBoolean("undo");
			User u = userDao.getLoginUser(username);
			Experience exp = expDao.getExperience(exp_id);
			if (undo)
				map.put("success", voteDao.deleteVote(exp, u));
			else
				map.put("success", voteDao.saveVote(exp, u, true));
		} catch (Exception e) {
			map.put("success", false);
			map.put("error", e.getMessage());
		}
		return map;
	}
	
	
	@RequestMapping(value="/mobileupdate", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> mobileUpdate(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		
		String username = json.getString("username");
		boolean hasAuthority = json.getBoolean("IsLoggedIn");
		if(!hasAuthority){
			map.put("success", false);
			map.put("error", "You must log in to downvote experience");
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
		User u = userDao.getLoginUser(username);
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
	
	
	@RequestMapping(value="/mobileeditText", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> mobileUpdateText(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result").getJSONObject("params");
		
		String username = json.getString("username");
		boolean hasAuthority = json.getBoolean("IsLoggedIn");
		if(!hasAuthority){
			map.put("success", false);
			map.put("error", "You must log in to downvote experience");
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
		User u = userDao.getLoginUser(username);
		Experience exp = expDao.getExperience(id);
		if (u.getId() != exp.getAuthor().getId()) {
			map.put("success", false);
			map.put("error", "You can update only your experiences!");
			return map;
		}
		exp.setText(json.getString("text"));
		expDao.saveExperience(exp);
		map.put("success", true);
		return map;
	}

		
	@RequestMapping(value="/mobilemarkSpam", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> mobileMarkSpam(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		
		boolean hasAuthority = json.getBoolean("IsLoggedIn");
		if(!hasAuthority){
			map.put("success", false);
			map.put("error", "You must log in to downvote experience");
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
		Experience exp = expDao.getExperience(id);
		map.put("success", expDao.markSpam(exp));
		return map;
	}
	
	@RequestMapping(value="/mobileunmarkSpam", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> mobileUnmarkSpam(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		
		boolean hasAuthority = json.getBoolean("IsLoggedIn");
		if(!hasAuthority){
			map.put("success", false);
			map.put("error", "You must log in to downvote experience");
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
		Experience exp = expDao.getExperience(id);
		map.put("success", expDao.unmarkSpam(exp));
		return map;
	}
	
}
