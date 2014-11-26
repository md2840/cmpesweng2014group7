package com.urbsource.models;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.urbsource.db.JDBCUserDAO;

public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;

	private int expPoints=0;
	private int commentPoints=0;
	/**
	 * For password confirmation
	 */
	private String password2;
	private int numberOfExperiences;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
	
		this.password = password;
		
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public boolean isEmailValid() {
		return Pattern.matches("\\w+@\\w+(\\w|[.-_])*\\.[a-zA-Z]+", email);
	}

	/**
	 * Karma is kind of user level that is dependent on both experience and
	 * comment points. We currently define it with formula :
	 * 
	 * floor(log(karma = alpha * expPoints + beta * commentPoints + 1)) + 1
	 * 
	 * where alpha and beta are coefficients and value in log is >= 1 so that
	 * karma is positive.
	 * 
	 * @return karma of the user.
	 */
	public int getKarma() {
		double pureKarma = expPoints + commentPoints + 1;
		if (pureKarma < 1)
			pureKarma = 1;
		return (int)Math.log(pureKarma) + 1;
	}

	public int getExperiencePoints() {
		return expPoints;
	}

	public void setExperiencePoints(int expPoints) {
		this.expPoints = expPoints;
	}

	public int getCommentPoints() {
		return commentPoints;
	}

	public void setCommentPoints(int commentPoints) {
		this.commentPoints = commentPoints;
	}
	
	public int getNumberOfExperiences() {
		return numberOfExperiences;
	}
	
	/**
	 * To be only used from DAO
	 * 
	 * @param numberOfExperiences
	 */
	public void setNumberOfExperiences(int numberOfExperiences) {
		this.numberOfExperiences = numberOfExperiences;
	}
	
	/**
	 * The algorithm for securing passwords
	 * 
	 * @author dilara kekulluoglu
	 */ 
	private static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();
         
        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }
     
    private static String getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }
     
    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }
}
