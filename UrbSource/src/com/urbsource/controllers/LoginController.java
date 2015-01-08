package com.urbsource.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.urbsource.db.JDBCUserDAO;
import com.urbsource.models.User;
import com.urbsource.sendEmail.SendEmail;

	/**
	 * The controller logic for login process for mobile.
	 * 
	 * @author Dilara Kekulluoglu
	 */
	@Controller
	@RequestMapping("/mobilelogin/*")
	public class LoginController {
		JDBCUserDAO dao;
	        
		public LoginController() {
			// Initialize DB connection
			dao = new JDBCUserDAO();
		}
		
		/**
		 * Logins the user by checking whether such a user exists.
		 *
		 * @author dilara kekulluoglu		 
		 */
		
		@RequestMapping(value="/confirm", method=RequestMethod.POST)
		public @ResponseBody HashMap<String,Object> SignUp(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
			HashMap<String, Object> map = new HashMap<String, Object>();
			JSONObject json = (new JSONObject(getBody(request))).getJSONObject("result");
			String username = json.getString("username");
			String password = json.getString("password");
			
				User u = dao.getMobileUser(username);
				
				if(u == null){
					map.put("success", false);
					map.put("error", "Username does not exist");
					
				}else if(!(u.getPassword()).equals(password)){
					map.put("success", false);
					map.put("error", "Password is not correct");
					
				}else{
					map.put("success",true);
					
				}
				
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
