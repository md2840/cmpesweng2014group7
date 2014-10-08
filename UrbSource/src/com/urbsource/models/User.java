package com.urbsource.models;

import java.io.Serializable;

public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int U_ID;
	private String U_USERNAME;
	private String U_PASSWORD;
	
	
	public int getU_ID() {
		return U_ID;
	}
	public void setU_ID(int u_ID) {
		U_ID = u_ID;
	}
	public String getU_USERNAME() {
		return U_USERNAME;
	}
	public void setU_USERNAME(String u_USERNAME) {
		U_USERNAME = u_USERNAME;
	}
	public String getU_PASSWORD() {
		return U_PASSWORD;
	}
	public void setU_PASSWORD(String u_PASSWORD) {
		U_PASSWORD = u_PASSWORD;
	}

}
