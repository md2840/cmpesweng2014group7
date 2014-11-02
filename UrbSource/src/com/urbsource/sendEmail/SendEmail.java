package com.urbsource.sendEmail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
	
	String username;
	String password;
	
	Session session;
	
	public SendEmail(){
		username = "321group7@gmail.com";
	    password = "sevenheaven";
	    
	    Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");
	    
	    session = Session.getInstance(props,
	      new javax.mail.Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	        }
	      });
	}
	
	public void sendMailforSignup( String emailAddress) {
	    // TODO Auto-generated method stub
	
	    try {
	
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(username));
	        message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(emailAddress));
	       
	        message.setSubject("UrbSource signup is successful");
	        message.setText("Signup is successful. Welcome.");
	
	        Transport.send(message);
	
	        System.out.println("Message sent successfully");
	
	    } catch (MessagingException e) {
	        throw new RuntimeException(e);
	    }
	}
}