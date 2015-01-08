package com.urbsource.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.HashMap;

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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.urbsource.db.*;
import com.urbsource.models.*;

@Controller
@RequestMapping("/notification/*")
public class NotificationController {
	
	JDBCUserDAO userDao;
	JDBCNotificationDAO notDao;
	
	public NotificationController(){
		userDao = new JDBCUserDAO();
		notDao = new JDBCNotificationDAO(userDao);
	}
	
	@RequestMapping(value="/recent", method=RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> recent(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			map.put("success", false);
			map.put("error", "You must log in to delete your notifications");
			return map;
		}
		User u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());

		map.put("notifications", notDao.getNotifications(u, new Timestamp(json.getLong("time"))));
		return map;
	}
	
	@RequestMapping(value="/all", method=RequestMethod.GET)
	public @ResponseBody HashMap<String,Object> all(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			map.put("success", false);
			map.put("error", "You must log in to delete your notifications");
			return map;
		}
		User u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());

		map.put("notifications", notDao.getNotifications(u));
		return map;
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public @ResponseBody HashMap<String, Object> delete(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AnonymousAuthenticationToken) {
			map.put("success", false);
			map.put("error", "You must log in to delete your notifications");
			return map;
		}
		int id;
		try {
			id = json.getInt("id");
		} catch (JSONException e) {
			map.put("success", false);
			map.put("error", "Notification ID is not given or not int, check how you call the API!");
			return map;
		}
		User u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
		Notification not = notDao.getNotification(id);
		if (u.getId() != not.getUser().getId()) {
			map.put("success", false);
			map.put("error", "You cannot delete others' notifications");
			return map;
		}
		map.put("success", notDao.deleteNotification(not));
		return map;
		
	}

	
	/**
	 * @author Setenay Ronael
	 * @param request
	 * @return
	 * @throws IOException
	 */
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
	

}
