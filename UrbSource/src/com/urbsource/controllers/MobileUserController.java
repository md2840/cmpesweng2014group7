package com.urbsource.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.urbsource.db.JDBCCommentDAO;
import com.urbsource.db.JDBCExperienceDAO;
import com.urbsource.db.JDBCExperienceVoteDAO;
import com.urbsource.db.JDBCTagDAO;
import com.urbsource.db.JDBCUserDAO;
import com.urbsource.models.Comment;
import com.urbsource.models.Experience;
import com.urbsource.models.Tag;
import com.urbsource.models.User;

@Controller
@RequestMapping("/mobile/*")
public class MobileUserController {

	JDBCUserDAO userDao;
	JDBCTagDAO tagDao;
	JDBCExperienceDAO expDao;
	JDBCExperienceVoteDAO voteDao;
	JDBCCommentDAO commentDao;
	
	public MobileUserController(){
		userDao = new JDBCUserDAO();
		tagDao = new JDBCTagDAO();
		expDao = new JDBCExperienceDAO(userDao, tagDao);
		voteDao = new JDBCExperienceVoteDAO(); 
		commentDao = new JDBCCommentDAO(userDao);
	}

	/**
	 * Handles HTTP get requests for user information page from mobile application.
	 * 
	 * 
	 * @author Gokce Yesiltas 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 * @throws IOException
	 */
	@RequestMapping(value="/mobileuserinfo", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> userInfo(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		String username;
		try {
			username = json.getString("username");
			User u = userDao.getMobileUser(username);
			map.put("user", u);
			map.put("success",true );
			//map.put("commentPoint", u.getCommentPoints());
			//map.put("numOfExp", u.getNumberOfExperiences());

		} catch (JSONException e) {
			map.put("error", e);
			map.put("success",false );
			throw e;
		}
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
//			expDao.configureVotes(username, list.get(i));
		}
		map.put("experiences", list);
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


	/**
	 * Sends the experience of the given id with the details
	 * */
	@RequestMapping(value="/id/mobile", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> getExperienceMobile(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		String username = json.getString("username");
		int expId = json.getInt("expId");

		Experience e = expDao.getExperience(expId);

//		expDao.configureVotes(username, e);
		map.put("experiences", e);
		return map;
	}

	/**
	 * Method to return all the experiences of a given wantedUsername
	 * Experiences will be returned after likes are configured.
	 * */
	@RequestMapping(value="/user", method=RequestMethod.POST)
	public @ResponseBody HashMap<String,Object> userExperienceMobile( HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		String username = json.getString("username");
		String wantedUsername = json.getString("wantedUsername");

		List<Experience> list = expDao.getExperiences(userDao.getMobileUser(wantedUsername));
		if(!username.equalsIgnoreCase(wantedUsername)){
			for(int i=0; i<list.size(); i++){
//				expDao.configureVotes(username, list.get(i));
			}
		}
		map.put("experiences", list );
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
		Experience exp = new Experience(u, json.getString("text"), tags).setMood(json.getString("mood"))
																		.setLocation(json.getString("location"));
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
	
	@RequestMapping(value="/createcomment", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> createComment(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map= new HashMap<String, Object>();;
		try {
			
			JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
			
			String username = json.getString("username");
			boolean hasAuthority = json.getBoolean("IsLoggedIn");
			if(!hasAuthority){
				map.put("success", false);
				map.put("error", "You must log in to create experience");
				return map;
			}

			
			User u = userDao.getLoginUser(username);
			Timestamp date;
			try {
			    DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
		        java.util.Date creationDate = df.parse(json.getString("creationTime"));
				date = new Timestamp(creationDate.getTime());
			} catch (Exception e) {
				date = new Timestamp(new java.util.Date().getTime());
			}
			Comment comment = new Comment(u, json.getString("text"), json.getInt("experienceId")).setCreationTime(date);
			map.put("success", commentDao.createComment(comment));
		} catch (JSONException e) {
			
			map.put("success", false);
			map.put("error", e.getMessage());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return map;
		
	}

	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> delete(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			
			JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
			String username = json.getString("username");
			boolean hasAuthority = json.getBoolean("IsLoggedIn");
			if(!hasAuthority){
				map.put("success", false);
				map.put("error", "You must log in to create experience");
				return map;
			}

			int id;
			try {
				id = json.getInt("id");
			} catch (JSONException e) {
				map.put("success", false);
				map.put("error", "Comment ID is not given or not int, check how you call the API!");
				return map;
			}
			User u = userDao.getLoginUser(username);
			Comment comment = commentDao.getComment(id);
			if (u.getId() != comment.getAuthor().getId()) {
				map.put("success", false);
				map.put("error", "You cannot delete others' comments");
				return map;
			}
			map.put("success", commentDao.deleteComment(comment));
		} catch (JSONException e) {
			map.put("success", false);
			map.put("error", e.getMessage());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping(value="/getcomments", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> getComments(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map= new HashMap<String, Object>();;
		try {
			
			JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
			
			String username = json.getString("username");
			boolean hasAuthority = json.getBoolean("IsLoggedIn");
			if(!hasAuthority){
				map.put("success", false);
				map.put("error", "You must log in to see comments");
				return map;
			}

			int id = json.getInt("id");
			Experience exp = expDao.getExperience(id);
			
			map.put("comments",commentDao.getComments(exp));
			map.put("success", true);
			
			
		} catch (JSONException e) {
			
			map.put("success", false);
			map.put("error", e.getMessage());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			map.put("success", false);
			map.put("error", e1.getMessage());
			
		}
		return map;
		
	}


	@RequestMapping(value="/mobilemarkSpam", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> mobileMarkSpam(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
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
		Experience exp = expDao.getExperience(id);
		User u = userDao.getMobileUser(username);
		map.put("success", expDao.markSpamMobile(exp,u));
		return map;
	}

	@RequestMapping(value="/mobileunmarkSpam", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> mobileUnmarkSpam(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
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
		Experience exp = expDao.getExperience(id);
		User u = userDao.getMobileUser(username);
		map.put("success", expDao.unmarkSpamMobile(exp,u));
		return map;
	}

}

