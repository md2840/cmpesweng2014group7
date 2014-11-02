package com.urbsource.sendEmail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *	@author Cafer Tayyar Yoruk
 *	@param username Email address of the UrbSource.
 *	@param password Password of the email address of the UrbSource.
 *	@param userEmailAddress Email address of the user.
 *	@param mailSubject Subject of the mail to be sent.
 *	@param mailText Text body of the mail to be sent.
 */

public class SendEmail {
	
	String username;
	String password;
	String userEmailAddress;
	String mailSubject;
	String mailText;
	
	Session session;
	
	public SendEmail( String userEmailAddress, String mailSubject, String mailText ){
		username = "321group7@gmail.com";
	    password = "sevenheaven";
	    
	    this.userEmailAddress = userEmailAddress;
		this.mailSubject = mailSubject;
		this.mailText = mailText;
	    
	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");
	    
	    session = Session.getInstance( props,
	      new javax.mail.Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication( username, password );
	        }
	      });
	}
	
	/**
	 *	A mail is sent to user's email address.
	 */
	public boolean sendMailToUser() {
	
	    try {
	
	        Message message = new MimeMessage( session );
	        message.setFrom( new InternetAddress( username ) );
	        message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse( userEmailAddress ) );
	       
	        message.setSubject( mailSubject );
	        message.setText( mailText );
	
	        Transport.send(message);
	
	    } catch (MessagingException e) {
	        e.printStackTrace();
	        return false;
	    }
	    
	    return true;
	}
}