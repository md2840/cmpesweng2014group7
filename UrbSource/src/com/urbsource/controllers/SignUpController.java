/**
 * 
 */
package com.urbsource.controllers;

import javax.servlet.ServletRequest;

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.urbsource.db.JDBCUserDAO;
import com.urbsource.models.User;
import com.urbsource.sendEmail.SendEmail;

 

/**
 * The controller logic for signup process.
 * 
 * @author Mehmet Emre
 */
@Controller
@RequestMapping("/signup/*")
public class SignUpController {
	JDBCUserDAO dao;
    //@Autowired
    //private ReCaptcha reCaptchaService = null;
	     
	public SignUpController() {
		// Initialize DB connection
		dao = new JDBCUserDAO();
	}
	
	/**
	 * Signs up a new user by creating a User object in database.
	 * 
	 * @param model The model passed to controller via Spring.
	 * @return The response model-view pair
	 */
	@RequestMapping(value="/confirm", method=RequestMethod.POST)
	public ModelAndView SignUp(@ModelAttribute User u,ServletRequest request, Model model) {
		
		String challenge = request.getParameter("recaptcha_challenge_field");
        String response = request.getParameter("recaptcha_response_field");
        String remoteAddr = request.getRemoteAddr();
         
      //  ReCaptchaResponse reCaptchaResponse = reCaptchaService.checkAnswer(remoteAddr, challenge, response);
		if (! u.getPassword().equals(u.getPassword2())) {
			model.addAttribute("error", "password_mismatch");
		} else if (u.getPassword().isEmpty()) {
			model.addAttribute("error", "empty_password");
		} else if (! u.isEmailValid()) {
			model.addAttribute("error", "invalid_email");
		} else /*if(!reCaptchaResponse.isValid()){
			model.addAttribute("error", "wrong_captcha");
		} else */{
			try {
				dao.createUser(u);
				
				/**
				 *	After signing up, a mail is sent to user's email address.  
				 *	@param mailSubject Subject of the mail to be sent.
				 *	@param mailText Text body of the mail to be sent.
				 */
				String mailSubject = "UrbSource signup is successful";
				String mailText = "Signup is successful. Welcome.";
				SendEmail sendEmail = new SendEmail( u.getEmail(), mailSubject, mailText );
				sendEmail.sendMailToUser();
				
				return new ModelAndView("signup_success");
			} catch (DataIntegrityViolationException e) {
				model.addAttribute("error", "user_exists");
			}
		}
		return new ModelAndView("signup", "command", u);
	}
	
	/**
	 * Handles GET requests to SignUp page and renders signup form.
	 * 
	 * @param model The model passed to controller via Spring.
	 * @return The response model-view pair
	 */
	@RequestMapping(value="/confirm", method=RequestMethod.GET)
	public ModelAndView SignUpPage(Model model) {
		model.addAttribute("user", null);
		return new ModelAndView("signup", "command", new User());
	}
}
