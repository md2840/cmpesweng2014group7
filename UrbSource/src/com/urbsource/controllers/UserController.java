/**
 * 
 */
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.urbsource.db.JDBCUserDAO;
import com.urbsource.models.User;
import com.urbsource.randomString.RandomString;
import com.urbsource.sendEmail.SendEmail;

/**
 * Controller for user operations.
 * 
 * @author Mehmet Emre
 */
@Controller
@RequestMapping("/user/*")
public class UserController {

	JDBCUserDAO userDao;

	public UserController() {
		userDao = new JDBCUserDAO();
	}

	@RequestMapping(value="info", method=RequestMethod.GET)
	public ModelAndView currentUserInfo(Model model) {
		User u = new User();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
		}
		model.addAttribute("user", u);
		return new ModelAndView("userInfo", "command", u);
	}

	@RequestMapping(value="info/{userId}", method=RequestMethod.GET)
	public ModelAndView userInfo(@PathVariable(value="userId") int userId, Model model) {
		User u = new User();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
		}
		model.addAttribute("user", u);
		return new ModelAndView("userInfo", "command", userDao.getUser(userId));
	}

	/**
	 * Updates a user profile by creating a User object in database.
	 * 
	 * @param model The model passed to controller via Spring.
	 * @return The response model-view pair
	 */
	@RequestMapping(value="/info", method=RequestMethod.POST)
	public ModelAndView SignUp(@ModelAttribute User u, Model model) {
		User user = new User();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			user = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
		}
		if (! u.getPassword().equals(u.getPassword2())) {
			model.addAttribute("error", "password_mismatch");
		} else if (! u.isEmailValid()) {
			model.addAttribute("error", "invalid_email");
		} else {
			try {
				user.setEmail(u.getEmail());
				user.setFirstName(u.getFirstName());
				user.setLastName(u.getLastName());
				if (! u.getPassword().isEmpty())
					user.setPassword(u.getPassword());
				userDao.saveUser(user);
			} catch (DataIntegrityViolationException e) {
				model.addAttribute("error", "user_exists");
			}
		}
		return new ModelAndView("userInfo", "command", user);
	}

	/***
	 * Updates user's password with a random string to reset 
	 * the password which is forgotten by user. Sends new password
	 * via email.
	 * 
	 * @author Gokce Yesiltas
	 * @param model The model passed to controller via Spring.
	 * @return The response model-view pair
	 */
	@RequestMapping(value="/forgot", method=RequestMethod.POST)
	public ModelAndView resetPassword(@ModelAttribute User u, Model model) {
		if (u.getEmail().isEmpty()) {
			model.addAttribute("error", "empty_email");
		} else if (! u.isEmailValid()) {
			model.addAttribute("error", "invalid_email");
		} else {
			User user = userDao.getUser(u.getEmail());
			if(user != null){
				RandomString rs = new RandomString(8);
				String password = rs.nextString();

				user.setPassword(password);
				userDao.saveUser(user);

				String mailSubject = "UrbSource Password Reset";
				String mailText = "We received a password reset request for your UrbSource account. Your new password is: " + password + ". You can change your password by logging in.";
				SendEmail sendEmail = new SendEmail( u.getEmail(), mailSubject, mailText );
				sendEmail.sendMailToUser();

				return new ModelAndView("forgot_pw_success");
			} else {
				model.addAttribute("error", "user_not_exist");
			}
		}
		return new ModelAndView("forgot_pw", "command", u);
	}

	/***
	 * Handles GET requests to forgot password page and renders forgot password form.
	 * 
	 * @param model The model passed to controller via Spring.
	 * @return The response model-view pair
	 * @author Gokce Yesiltas
	 */
	@RequestMapping(value="/forgot", method=RequestMethod.GET)
	public ModelAndView resetPassword(Model model) {
		model.addAttribute("user", null);
		return new ModelAndView("forgot_pw");
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
