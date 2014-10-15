package com.urbsource.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.urbsource.db.JDBCIndexDAO;

@Controller
@RequestMapping("/Index/*")
public class IndexController {
	
	
	JDBCIndexDAO jdb;
	
	public IndexController(){
		System.out.println("index controller");
		jdb = new JDBCIndexDAO();
	}
	
	@RequestMapping(value="/getNames")
	public @ResponseBody HashMap<String,Object> getNames(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		return jdb.getNames();
	}
	
	@RequestMapping(value="/addName")
	public @ResponseBody String addNames(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		
		JSONObject obj = new JSONObject(getBody(request));
		return jdb.addName(obj.getJSONObject("result").getJSONObject("params").getString("N_NAME"));
		
	}
	
	@RequestMapping(value="searchName")
	public @ResponseBody HashMap<String,Object> searchNames(HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		JSONObject obj = new JSONObject(getBody(request));
		return jdb.searchName(obj.getJSONObject("result").getJSONObject("params").getString("query"));
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
		System.out.println("body " +body);
		return body;
	}
}
