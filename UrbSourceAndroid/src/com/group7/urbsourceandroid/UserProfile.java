package com.group7.urbsourceandroid;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.group7.urbsourceandroid.models.User;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class UserProfile extends Activity {
	private SessionManager session;
	private String username;
	private User user;
	private String responseText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("started", "started");
		setContentView(R.layout.userprofile);

		session = new SessionManager(getApplicationContext());
		session.checkLogin();
		username = session.getUserDetails().get("name");

		try {
			getUserData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextView username = (TextView) findViewById(R.id.pro_username);
		username.setText(this.username);

		String expPointS = Integer.toString(user.getExperiencePoints());
		TextView point = (TextView) findViewById(R.id.pro_point);
		point.setText(expPointS);

	}


	public void getUserData() throws JSONException {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		//HttpGet httpGet = new HttpGet("http://titan.cmpe.boun.edu.tr:8086/UrbSource/experience/recent");
		HttpPost httpPost = new HttpPost("http://10.0.3.2/UrbSource/user/mobileuserinfo"); //for genymotion

		try {

			// Add your data
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("username", username);

			StringEntity se = new StringEntity(jsonobj.toString());    
			se.setContentType("application/json;charset=UTF-8");
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
			httpPost.setHeader("Content-Type", "application/json");
			httpPost.setHeader("Accept", "application/json");

			httpPost.setEntity(se);
			HttpResponse response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity(); 
			responseText = getASCIIContentFromEntity(entity);
			
			JSONObject obj = new JSONObject(responseText);
			user.setExperiencePoints(obj.getInt("expPoint"));
			user.setCommentPoints(obj.getInt("commentPoint"));
			user.setNumberOfExperiences(obj.getInt("numOfExp"));


		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	public String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
		InputStream in = entity.getContent();


		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n>0) {
			byte[] b = new byte[4096];
			n =  in.read(b);


			if (n>0) out.append(new String(b, 0, n));
		}


		return out.toString();
	}
}
