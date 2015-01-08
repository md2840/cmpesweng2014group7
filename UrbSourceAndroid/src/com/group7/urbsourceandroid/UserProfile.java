package com.group7.urbsourceandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
/**
 * Shows user profile page for the wanted user by getting 
 * information about that user from WEB API via HTTP requests. 
 * 
 * @author Gokce Yesiltas
 *
 */
public class UserProfile extends Activity {
	private SessionManager session;
	private String username;
	private User user;
	private String responseText;
	private final String GET_USER = "gu";
	private final String GET_EXP = "ge";
	private final String UPVOTE_EXP="ue";
	private final String DOWNVOTE_EXP="de";
	private final String DELETE_EXP="dele";
	private final String SPAM_EXP = "se";
	private List<Experience> userExperiences;
	private String usernameOfTheDesiredUser;
	private TextView fullname;
	private TextView exppoint;
	private TextView compoint;
	private TextView numOfExp;
	private ListView explist;
	private ActionListAdapter adapter;
	private AlertDialog.Builder alert ;

	/**
	 * Creates user profile page and makes two HTTP requests 
	 * to get user informations and to get experiences of the user.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Sets view as userprofile.xml
		setContentView(R.layout.userprofile);

		//Gets informations from session
		session = new SessionManager(getApplicationContext());
		session.checkLogin(); //checks whether the user is logged in
		username = session.getUserDetails().get("name"); //gets username

		responseText="";
		Log.i("username", username);
		user = new User();

		//initialize userExperiences list
		userExperiences = new ArrayList<Experience>();
		//initializes the adapter for ActionListAdapter
		adapter = new ActionListAdapter(this, R.id.pro_list, userExperiences);
		explist = (ListView) findViewById(R.id.pro_list);
		//connects the adapter to related field in the xml file
		explist.setAdapter(adapter);

		//initializes an alert  
		alert =  new AlertDialog.Builder(this);

		Intent i = getIntent();
		// getting attached intent data to know which user page will be opened 
		usernameOfTheDesiredUser = i.getStringExtra("username");

		TextView username = (TextView) findViewById(R.id.pro_username);
		//if there is no attached intent data, user 
		//profile page will be opened for logged in user
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

		//executes asynchronized task to make HTTP requests 
		//for getting user's basic information
		new MyAsyncTask().execute(GET_USER, usernameOfTheDesiredUser);
		//executes asynchronized task to make HTTP requests 
		//for getting user's experiences
		new MyAsyncTask().execute(GET_EXP, usernameOfTheDesiredUser);

	}
	/**
	 * Creates a customized adapter for the list view which lists 
	 * experiences of the user on the user profile page. Shows the 
	 * elements of the experiences and sets click listener for them.
	 */
	private class ActionListAdapter extends ArrayAdapter<Experience> {
		private List<Experience> userExperiences;

		public ActionListAdapter(Context context, int resourceId, 
				List<Experience> experiences) {
			super(context, resourceId, experiences);
			this.userExperiences = experiences;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.experience, null);
			}

			Experience experience = userExperiences.get(position);
			if (experience != null) {

				TextView username = (TextView) view.findViewById(R.id.ex_username);
				if(username!=null){
					username.setText(experience.getAuthor().getUsername());
					username.setTag(new Integer(position));

					username.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent i = new Intent(getApplicationContext(), UserProfile.class);
							i.putExtra("username",userExperiences.get(Integer.parseInt(v.getTag().toString())).getAuthor().getUsername());
							startActivity(i);
						}

					});
				}
				TextView location = (TextView) view.findViewById(R.id.ex_location);
				//location add
				location.setText("");
				TextView mood = (TextView) view.findViewById(R.id.ex_mood);

				mood.setText(experience.getMood()+" Experience");
				TextView content = (TextView) view.findViewById(R.id.ex_content);
				if(content!=null){
					content.setText(experience.getText());
				}
				TextView tags = (TextView) view.findViewById(R.id.ex_tags);
				if(tags!=null){
					StringBuilder build = new StringBuilder();
					if(experience.getTags() != null){
						build.append(experience.getTags().get(0).getName());
						for(int i=1;i<experience.getTags().size();i++){
							build.append(",");
							build.append(experience.getTags().get(i).getName());
						}

						tags.setText(build.toString());
					}else{
						tags.setText("");
					}
				}

				TextView comment = (TextView) view.findViewById(R.id.ex_comment);
				if(comment!=null){
					if(experience.getNumberOfComments()==0){
						comment.setText("No comments");
					}else if(experience.getNumberOfComments()==1){
						comment.setText("1 comment");
					}else{
						comment.setText(experience.getNumberOfComments()+" comments");            		
					}
					comment.setTag(new Integer(position));

					comment.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent i = new Intent(getApplicationContext(), ShowExperience.class);
							i.putExtra("id",userExperiences.get(Integer.parseInt(v.getTag().toString())).getId());
							startActivity(i);
						}
					});
				}

				ImageButton upvote = (ImageButton) view.findViewById(R.id.ex_upvote);
				upvote.setTag(new Integer(position));
				upvote.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int pos = Integer.parseInt(v.getTag().toString());
						ImageButton upButton = (ImageButton) v;
						if(userExperiences.get(pos).isUpvotedByUser()){
							upButton.setImageResource(R.drawable.arrow_up_inactive);                    	
						}else{
							upButton.setImageResource(R.drawable.arrow_up);

						}
						//executes asynchronized task to make HTTP requests 
						//for upvoting the selected experience
						new MyAsyncTask().execute(UPVOTE_EXP,v.getTag().toString());
					}
				});
				upvote.setVisibility(View.VISIBLE);

				if(userExperiences.get(position).isUpvotedByUser()){
					upvote.setImageResource(R.drawable.arrow_up);                    	
				}else{
					upvote.setImageResource(R.drawable.arrow_up_inactive);
				}

				ImageButton downvote = (ImageButton) view.findViewById(R.id.ex_downvote);
				downvote.setTag(new Integer(position));
				downvote.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int pos = Integer.parseInt(v.getTag().toString());
						ImageButton downButton = (ImageButton) v;
						if(userExperiences.get(pos).isDownvotedByUser()){
							downButton.setImageResource(R.drawable.arrow_down_inactive);                    	
						}else{
							downButton.setImageResource(R.drawable.arrow_down);
						}
						//executes asynchronized task to make HTTP requests 
						//for downvoting the selected experience
						new MyAsyncTask().execute(DOWNVOTE_EXP,v.getTag().toString());
					}
				});
				downvote.setVisibility(View.VISIBLE);

				if(userExperiences.get(position).isDownvotedByUser()){
					downvote.setImageResource(R.drawable.arrow_down);                    	
				}else{
					downvote.setImageResource(R.drawable.arrow_down_inactive);
				}

				ImageButton spam = (ImageButton) view.findViewById(R.id.ex_spam);
				spam.setTag(new Integer(position));
				spam.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						int pos = Integer.parseInt(v.getTag().toString());
						ImageButton spamButton = (ImageButton) v;
						if(userExperiences.get(pos).isUserMarkedSpam()){
							spamButton.setImageResource(R.drawable.spam_inactive);                    	
						}else{
							spamButton.setImageResource(R.drawable.spam);
						}
						//executes asynchronized task to make HTTP requests 
						//for reporting the selected experience
						new MyAsyncTask().execute(SPAM_EXP,v.getTag().toString());

					}
				});
				spam.setVisibility(View.VISIBLE);

				if(userExperiences.get(position).isUserMarkedSpam()){
					spam.setImageResource(R.drawable.spam);                    	
				}else{
					spam.setImageResource(R.drawable.spam_inactive);
				}

				ImageButton delete = (ImageButton) view.findViewById(R.id.btn_delete);
				delete.setTag(new Integer(position));
				delete.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						//executes asynchronized task to make HTTP requests 
						//for deleting the selected experience
						new MyAsyncTask().execute(DELETE_EXP,v.getTag().toString());
					}
				});

				delete.setVisibility(View.INVISIBLE);

				Log.i("name experience user",experience.getAuthor().getUsername());
				//makes changes according to whether the experience 
				//is belong the logged in user or not
				if(experience.getAuthor().getUsername().equals(session.getUserDetails().get("name"))){
					delete.setVisibility(View.VISIBLE);
					spam.setVisibility(View.INVISIBLE);
					upvote.setVisibility(View.INVISIBLE);
					downvote.setVisibility(View.INVISIBLE);
				}
			}


			return view;
		}
	}

	/**
	 * Creates asynchronized task to make HTTP requests for getting 
	 * user's basic information, user's experiences, upvoting an 
	 * experience, downvoting an experience, reporting an experience, or
	 * deleting an experience. Makes HTTP requests in background, and 
	 * updates the page on the post execution.
	 */
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		private boolean getuser = false;
		private boolean getexp = false;
		private boolean upvote = false;
		private boolean downvote = false;
		private boolean delete = false;
		private boolean spam = false;
		private boolean undo = false;   
		private int position;
		/**
		 * According to parameters calls necessary methods to make HTTP requests.
		 */
		@Override
		protected Double doInBackground(String... params) {
			if(params[0]==GET_USER){
				getuser = true;
				Log.i("username2", params[1]);
				//gets user's basic informations
				getUserData(params[1]);
			}else if(params[0]==GET_EXP){
				getexp = true;
				Log.i("username for exp", params[1]);
				//gets experiences of the user
				getUserExperiences(params[1]);
			}else if(params[0]==UPVOTE_EXP){
				upvote=true;
				//upvotes the selected experience
				upvoteExp(params[1]);
			}else if(params[0]==DOWNVOTE_EXP){
				downvote = true;
				//downvotes the selected experience
				downvoteExp(params[1]);
			}else if(params[0]==DELETE_EXP){
				delete = true;
				//deletes the selected experience
				deleteExp(params[1]);
			}else if(params[0]==SPAM_EXP){
				spam=true;	
				//reports the selected experience as a spam
				spamExp(params[1]);
			}
			return null;
		}
		/**
		 * According to what the asynchronized task did in 
		 * the background, updates the page.
		 */
		protected void onPostExecute(Double result){
			if(getuser){
				//Sets the user's basic informations
				Log.i("user", user.getFirstName());
				exppoint.setText(Integer.toString(user.getExperiencePoints()));
				compoint.setText(Integer.toString(user.getCommentPoints()));
				numOfExp.setText(Integer.toString(user.getNumberOfExperiences()));
				fullname.setText(user.getFirstName() + " " + user.getLastName());
			}else if(getexp){
				//notifies the adapter to lists the experiences on the page
				adapter.notifyDataSetChanged();
			}else if(upvote){
				//creates an alert to let the user know
				try {
					JSONObject myObject = new JSONObject(responseText);
					if(myObject.getBoolean("success")){
						alert.setTitle("Success");
						if(undo){
							alert.setMessage("You unliked the experience");
							undo =false;
							alert.show();
						}else{
							alert.setMessage("You liked the experience");
							alert.show();
						} 
					}else{
						alert.setTitle("error");
						alert.setMessage(myObject.getString("error"));
						alert.show();  
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}else if(downvote){
				//creates an alert to let the user know
				try {
					JSONObject myObject = new JSONObject(responseText);

					if(myObject.getBoolean("success")){
						alert.setTitle("success");

						if(undo){
							alert.setMessage("You took back the dislike");
							undo = false;
							alert.show();
						}else{
							alert.setMessage("You disliked the experience");
							alert.show();  
						}
					}else{
						alert.setTitle("error");
						alert.setMessage(myObject.getString("error"));
						alert.show();  
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}else if(delete){
				//creates an alert to let the user know
				try {
					JSONObject myObject = new JSONObject(responseText);

					if(myObject.getBoolean("success")){
						alert.setTitle("success");
						alert.setMessage("You deleted your experience");
						alert.show();  
					}else{
						alert.setTitle("error");
						alert.setMessage(myObject.getString("error"));
						alert.show();  
					}
					userExperiences.remove(position);
					adapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}	
			}else if(spam){
				try {
					JSONObject myObject = new JSONObject(responseText);

					if(myObject.getBoolean("success")){
						alert.setTitle("success");

						if(undo){
							alert.setMessage("You took back the spam alert");
							undo = false;
							alert.show();
						}else{
							alert.setMessage("You marked the experience as spam");
							alert.show();  
						}
					}else{
						alert.setTitle("error");
						alert.setMessage(myObject.getString("error"));
						alert.show();  
					}
				}catch (JSONException e) {
					e.printStackTrace();
				}	
			}
		}
		/**
		 * Makes HTTP post request to get experiences of the user.
		 * @param usernameOfTheDesiredUser username of the user which we want to see experiences of
		 */
		public void getUserExperiences(String usernameOfTheDesiredUser)  {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/user"); //for genymotion
			try {
				JSONObject jsonobj = new JSONObject();

				//adds username of the user logged in
				jsonobj.put("username", username);
				//adds username of the user that we want to see profile page of
				jsonobj.put("wantedUsername",usernameOfTheDesiredUser);

				Log.i("wanted username", usernameOfTheDesiredUser);
				Log.i("login olan user", username);

				//sets header for http post request
				StringEntity se = new StringEntity(jsonobj.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept", "application/json");
				httpPost.setEntity(se);
				
				//executes http post request
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = (HttpEntity) response.getEntity(); 
				responseText = getASCIIContentFromEntity(entity);

				JSONObject myObject = new JSONObject(responseText);
				JSONArray jsona = new JSONArray(myObject.getString("experiences"));

				//adds experiences to the list one by one
				for(int i=0; i<jsona.length();i++){  
					JSONObject jsonObj = jsona.getJSONObject(i);
					Experience exp = new Experience();
					User u = new User();
					
					//gets the experience
					exp.setId(jsonObj.getInt("id"));
					exp.setText(jsonObj.getString("text"));
					exp.setMood(jsonObj.getString("mood"));
					exp.setSpam(jsonObj.getInt("spam"));
					exp.setUserMarkedSpam(jsonObj.getBoolean("userMarkedSpam"));
					exp.setUpvotedByUser(jsonObj.getBoolean("upvotedByUser"));
					exp.setDownvotedByUser(jsonObj.getBoolean("downvotedByUser"));
					exp.setNumberOfComments(jsonObj.getInt("numberOfComments"));

					//gets author of the experience
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
					
					//gets tags of the experience
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
				e.printStackTrace();			
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Makes HTTP post request to upvote the selected experience.
		 * @param pos position of the selected experience in the list
		 */
		public void upvoteExp(String pos){

			int exp_pos = Integer.parseInt(pos);
			position = exp_pos;
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobileupvote");

			try {
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));

				jsonobj.put("IsLoggedIn", session.isLoggedIn());
				jsonobj.put("id",userExperiences.get(exp_pos).getId());
				jsonobj.put("undo",userExperiences.get(exp_pos).isUpvotedByUser());

				undo = userExperiences.get(exp_pos).isUpvotedByUser();

				if(userExperiences.get(exp_pos).isUpvotedByUser()){
					userExperiences.get(exp_pos).setUpvotedByUser(false);
				}else{
					userExperiences.get(exp_pos).setUpvotedByUser(true);
				}
				
				//sets header for http post request
				StringEntity se = new StringEntity(jsonobj.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept", "application/json");
				httpPost.setEntity(se);
				
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = response.getEntity();

				String text = getASCIIContentFromEntity(entity);
				responseText=text;

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		/**
		 * Makes HTTP post request to downvote the selected experience.
		 * @param pos position of the selected experience in the list
		 */
		public void downvoteExp(String pos){
			int exp_pos = Integer.parseInt(pos);
			position = exp_pos;

			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobiledownvote");

			try {
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));

				jsonobj.put("IsLoggedIn", session.isLoggedIn());
				jsonobj.put("id",userExperiences.get(exp_pos).getId());
				jsonobj.put("undo",userExperiences.get(exp_pos).isDownvotedByUser());
				undo = userExperiences.get(exp_pos).isDownvotedByUser();

				if(userExperiences.get(exp_pos).isDownvotedByUser()){
					userExperiences.get(exp_pos).setDownvotedByUser(false);
				}else{
					userExperiences.get(exp_pos).setDownvotedByUser(true);
				}
				
				//sets header for http post request
				StringEntity se = new StringEntity(jsonobj.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept", "application/json");

				httpPost.setEntity(se);
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httpPost);

				HttpEntity entity = response.getEntity();

				String text = getASCIIContentFromEntity(entity);
				responseText=text;

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * Makes HTTP post request to delete the selected experience.
		 * @param pos position of the selected experience in the list
		 */
		public void deleteExp(String pos){
			int exp_pos = Integer.parseInt(pos);
			position = exp_pos;
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobiledelete");

			try {
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));

				jsonobj.put("IsLoggedIn", session.isLoggedIn());
				JSONObject innerJson = new JSONObject();

				innerJson.put("id",userExperiences.get(exp_pos).getId());

				jsonobj.put("params", innerJson);

				//sets header for http post request
				StringEntity se = new StringEntity(jsonobj.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept", "application/json");

				httpPost.setEntity(se);
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httpPost);

				HttpEntity entity = response.getEntity();

				String text = getASCIIContentFromEntity(entity);
				responseText=text;
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Makes HTTP post request to report the selected experience as a spam.
		 * @param pos position of the selected experience in the list
		 */
		public void spamExp(String pos){
			int exp_pos = Integer.parseInt(pos);
			position = exp_pos;
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost;
			if(userExperiences.get(exp_pos).isUserMarkedSpam()){
				httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobileunmarkSpam");
			}else{
				httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobilemarkSpam");
			}
			undo = userExperiences.get(exp_pos).isUserMarkedSpam();
			if(undo){
				userExperiences.get(exp_pos).setUserMarkedSpam(false);
			}else{
				userExperiences.get(exp_pos).setUserMarkedSpam(true);
			}
			try {
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));

				jsonobj.put("IsLoggedIn", session.isLoggedIn());
				jsonobj.put("id",userExperiences.get(exp_pos).getId());

				//sets header for http post request
				StringEntity se = new StringEntity(jsonobj.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept", "application/json");

				httpPost.setEntity(se);
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httpPost);

				HttpEntity entity = response.getEntity();

				String text = getASCIIContentFromEntity(entity);
				responseText=text;

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/**
		 * Makes HTTP post request to get basic informations of the user.
		 * @param usernameOfTheDesiredUser username of the user which we want to see experiences of
		 */
		public void getUserData(String usernameOfTheDesiredUser)  {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobileuserinfo"); //for genymotion
			try {
				Log.i("username3", usernameOfTheDesiredUser);

				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username", usernameOfTheDesiredUser);
				
				//sets header for http post request
				StringEntity se = new StringEntity(jsonobj.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept", "application/json");
				httpPost.setEntity(se);

				//executes HTTP post request
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = (HttpEntity) response.getEntity(); 
				Log.i("res", response.getStatusLine().toString());

				responseText = getASCIIContentFromEntity(entity);

				JSONObject obj = new JSONObject(responseText);
				String suc = String.valueOf(obj.getBoolean("success"));
				Log.i("suc", suc);
				JSONObject userJ = new JSONObject(obj.getString("user"));
				//gets user informations
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
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
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