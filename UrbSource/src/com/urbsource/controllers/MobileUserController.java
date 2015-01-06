package com.urbsource.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.urbsource.db.JDBCUserDAO;
import com.urbsource.models.User;

@Controller
@RequestMapping("/mobile/*")
public class MobileUserController {
	
	JDBCUserDAO userDao;

	public MobileUserController() {
		userDao = new JDBCUserDAO();
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
