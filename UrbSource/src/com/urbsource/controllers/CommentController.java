package com.urbsource.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.urbsource.db.JDBCCommentDAO;
import com.urbsource.db.JDBCTagDAO;
import com.urbsource.db.JDBCUserDAO;
import com.urbsource.models.Comment;
import com.urbsource.models.User;

@Controller
@RequestMapping("/comment/*")
public class CommentController {
	
	JDBCUserDAO userDao;
	JDBCTagDAO tagDao;
	JDBCCommentDAO commentDao;
	
	public CommentController(){
		userDao = new JDBCUserDAO();
		commentDao = new JDBCCommentDAO(userDao);
	}

	@RequestMapping(value="/create", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> create(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth instanceof AnonymousAuthenticationToken) {
				map.put("success", false);
				map.put("error", "You must log in to create experience");
				return map;
			}
			User u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
			Timestamp date;
			try {
			    DateFormat df = new SimpleDateFormat("yyyy-mm-dd"); 
		        java.util.Date creationDate = df.parse(json.getString("creationTime"));
				date = new Timestamp(creationDate.getTime());
			} catch (Exception e) {
				date = new Timestamp(new java.util.Date().getTime());
			}
			Comment comment = new Comment(u, json.getString("text"), json.getInt("experienceId")).setCreationTime(date);
			map.put("success", commentDao.createComment(comment));
		} catch (JSONException e) {
			map = new HashMap<String, Object>();
			map.put("success", false);
			map.put("error", e.getMessage());
		}
		return map;
		
	}

	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> delete(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map;
		try {
			map = new HashMap<String, Object>();
			JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth instanceof AnonymousAuthenticationToken) {
				map.put("success", false);
				map.put("error", "You must log in to delete your comments");
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
			User u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
			Comment comment = commentDao.getComment(id);
			if (u.getId() != comment.getAuthor().getId()) {
				map.put("success", false);
				map.put("error", "You cannot delete others' comments");
				return map;
			}
			map.put("success", commentDao.deleteComment(comment));
		} catch (JSONException e) {
			map = new HashMap<String, Object>();
			map.put("success", false);
			map.put("error", e.getMessage());
		}
		return map;
	}

	public String getBody(HttpServletRequest request)
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
			return "{'result': ''}";
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					return "{'result': ''}";
				}
			}
		}
		body = stringBuilder.toString();
		body = "{ \"result\": "+body + "}";
		return body;
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> update(HttpServletRequest request, HttpServletResponse response) {
		HashMap<String, Object> map;
		try {
			map = new HashMap<String, Object>();
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
				map.put("error", "Comment ID is not given or not int, check how you call the API!");
				return map;
			}
			User u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
			Comment comment = commentDao.getComment(id);
			if (u.getId() != comment.getAuthor().getId()) {
				map.put("success", false);
				map.put("error", "You can update only your experiences!");
				return map;
			}
			comment.setText(json.getString("text"));
			commentDao.saveComment(comment);
			map.put("success", true);
		} catch (JSONException e) {
			map = new HashMap<String, Object>();
			map.put("success", false);
			map.put("error", e.getMessage());
		}
		return map;
	}
}