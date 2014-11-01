/**
 * 
 */
package com.urbsource.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.urbsource.db.JDBCUserDAO;
import com.urbsource.models.User;

/**
 * Controller for user operations.
 * 
 * @author Mehmet Emre
 */
@Controller
@RequestMapping("/user/**")
public class UserController {

	JDBCUserDAO userDao;
	
	public UserController() {
		userDao = new JDBCUserDAO();
	}
	
	@RequestMapping(value="info", method=RequestMethod.GET)
	public ModelAndView userInfo(Model model) {
		User u = new User();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
		        u = userDao.getLoginUser(((UserDetails) auth.getPrincipal()).getUsername());
		}
		return new ModelAndView("userInfo", "command", u);
	}

	/**
	 * Updates a user profile by creating a User object in database.
	 * 
	 * @param model The model passed to controller via Spring.
	 * @return The response model-view pair
	 */
	@RequestMapping(value="/confirm", method=RequestMethod.POST)
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
}
