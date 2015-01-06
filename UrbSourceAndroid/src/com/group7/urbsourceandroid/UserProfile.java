package com.group7.urbsourceandroid;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.group7.urbsourceandroid.models.Experience;
import com.group7.urbsourceandroid.models.Tag;
import com.group7.urbsourceandroid.models.User;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class UserProfile extends Activity {
	private SessionManager session;
	private String username;
	private User user;
	private String responseText;
	private final String GET_USER = "gu";
	private final String GET_EXP = "ge";
	private List<Experience> userExperiences;
	private String usernameOfTheDesiredUser;
	private TextView fullname;
	private TextView exppoint;
	private TextView compoint;
	private TextView numOfExp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.userprofile);

		session = new SessionManager(getApplicationContext());
		session.checkLogin();
		username = session.getUserDetails().get("name");
		
		responseText="";
		Log.i("username", username);
		user = new User();
		Intent i = getIntent();
		// getting attached intent data
		usernameOfTheDesiredUser = i.getStringExtra("username");

		TextView username = (TextView) findViewById(R.id.pro_username);
		if(usernameOfTheDesiredUser==null){
			usernameOfTheDesiredUser = this.username;
			username.setText(this.username);
		}else{
			username.setText(usernameOfTheDesiredUser);
		}
		exppoint = (TextView) findViewById(R.id.pro_exppoint);
		compoint = (TextView) findViewById(R.id.pro_compoint);
		fullname = (TextView) findViewById(R.id.pro_name);
		numOfExp = (TextView) findViewById(R.id.pro_numexp);
		
		new MyAsyncTask().execute(GET_USER, usernameOfTheDesiredUser);

	}
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		private boolean getuser = false;
		private boolean getexp = false;
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			if(params[0]==GET_USER){
				getuser = true;
				Log.i("username2", params[1]);
				getUserData(params[1]);
			}else if(params[0]==GET_EXP){
				getexp = true;
				getUserExperiences(params[1]);
			}
			return null;
		}
		protected void onPostExecute(Double result){
			if(getuser){
				Log.i("user", user.getFirstName());
				exppoint.setText(Integer.toString(user.getExperiencePoints()));
				compoint.setText(Integer.toString(user.getCommentPoints()));
				numOfExp.setText(Integer.toString(user.getNumberOfExperiences()));
				fullname.setText(user.getFirstName() + " " + user.getLastName());

			}else if(getexp){
				//listview eklenince adapter.notifydatasetchanged de
			}
		}

		public void getUserExperiences(String username)  {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			//HttpGet httpGet = new HttpGet("http://titan.cmpe.boun.edu.tr:8086/UrbSource/experience/recent");
			HttpPost httpPost = new HttpPost("http://10.0.3.2/UrbSource/user/mobile"); //for genymotion
			try {
				// Add your data
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username", username);

				if(usernameOfTheDesiredUser==null){
					jsonobj.put("wantedUsername", username);

				}else{
					jsonobj.put("wantedUsername",usernameOfTheDesiredUser);
				}

				StringEntity se = new StringEntity(jsonobj.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept", "application/json");
				httpPost.setEntity(se);
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = (HttpEntity) response.getEntity(); 
				responseText = getASCIIContentFromEntity(entity);

				JSONObject myObject = new JSONObject(responseText);
				JSONArray jsona = new JSONArray(myObject.getString("experiences"));

				for(int i=0; i<jsona.length();i++){  // teker teker experience e gir.
					JSONObject jsonObj = jsona.getJSONObject(i);
					Experience exp = new Experience();
					User u = new User();
					exp.setId(jsonObj.getInt("id"));
					exp.setText(jsonObj.getString("text"));
					exp.setMood(jsonObj.getString("mood"));
					//exp.setCreationTime(Timestamp.valueOf(jsonObj.getString("creationTime")));
					//exp.setExpirationDate(Date.valueOf(jsonObj.getString("expirationDate")));
					//exp.setModificationTime(Timestamp.valueOf(jsonObj.getString("modificationTime")));
					exp.setSpam(jsonObj.getInt("spam"));
					exp.setUserMarkedSpam(jsonObj.getBoolean("userMarkedSpam"));
					exp.setUpvotedByUser(jsonObj.getBoolean("upvotedByUser"));
					exp.setDownvotedByUser(jsonObj.getBoolean("downvotedByUser"));
					exp.setNumberOfComments(jsonObj.getInt("numberOfComments"));

					//USER çekme tarafı ileride ayrı method yap kolay olsun.
					JSONObject userJ = new JSONObject(jsonObj.getString("author"));
					u.setId(userJ.getInt("id"));
					u.setCommentPoints(userJ.getInt("commentPoints"));
					u.setEmail(userJ.getString("email"));
					u.setExperiencePoints(userJ.getInt("experiencePoints"));
					u.setFirstName(userJ.getString("firstName"));
					u.setLastName(userJ.getString("lastName"));
					u.setNumberOfExperiences(userJ.getInt("numberOfExperiences"));
					u.setPassword(userJ.getString("password"));
					u.setPassword2(userJ.getString("password2"));
					u.setUsername(userJ.getString("username"));
					exp.setAuthor(u);
					//TAG çekme tarafı
					JSONArray Tags = new JSONArray(jsonObj.getString("tags"));
					for(int j=0;j<Tags.length();j++){
						JSONObject tagObj = Tags.getJSONObject(j);
						Tag t = new Tag();
						t.setId(tagObj.getInt("id"));
						t.setName(tagObj.getString("name"));
						exp.addTag(t);
					}
					userExperiences.add(exp);

				}


			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void getUserData(String username)  {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobileuserinfo"); //for genymotion
			try {
				// Add your data

				Log.i("username3", username);
				
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username", username);

				StringEntity se = new StringEntity(jsonobj.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept", "application/json");
				httpPost.setEntity(se);
				
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = (HttpEntity) response.getEntity(); 
				Log.i("res", response.getStatusLine().toString());
				
				
				
				responseText = getASCIIContentFromEntity(entity);
				Log.i("res",responseText);
				
				
				JSONObject obj = new JSONObject(responseText);
				String suc = String.valueOf(obj.getBoolean("success"));
				Log.i("suc", suc);
				JSONObject userJ = new JSONObject(obj.getString("user"));
				user.setId(userJ.getInt("id"));
				user.setCommentPoints(userJ.getInt("commentPoints"));
				user.setEmail(userJ.getString("email"));
				user.setExperiencePoints(userJ.getInt("experiencePoints"));
				user.setFirstName(userJ.getString("firstName"));
				user.setLastName(userJ.getString("lastName"));
				user.setNumberOfExperiences(userJ.getInt("numberOfExperiences"));
				user.setPassword(userJ.getString("password"));
				user.setPassword2(userJ.getString("password2"));
				user.setUsername(userJ.getString("username"));
				
				Log.i("response", user.getEmail());

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
}