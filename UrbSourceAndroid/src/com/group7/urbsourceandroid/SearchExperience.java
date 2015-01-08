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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Shows search page and shows results by getting 
 * information them from WEB API via HTTP requests. 
 * 
 * @author Gokce Yesiltas
 *
 */
public class SearchExperience extends Activity{
	private SessionManager session;
	private String username;
	private User user;
	private String responseText;
	private final String GET_EXP = "ge";
	private final String UPVOTE_EXP="ue";
	private final String DOWNVOTE_EXP="de";
	private final String DELETE_EXP="dele";
	private final String SPAM_EXP = "se";
	private List<Experience> searchResults;
	private ListView explist;
	private EditText searchText;
	private Button SearchExperience;
	private ActionListAdapter adapter;
	private String searchString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.search);

		session = new SessionManager(getApplicationContext());
		session.checkLogin();
		username = session.getUserDetails().get("name");

		searchResults = new ArrayList<Experience>();
		responseText="";
		searchString = "";

		//clears text area on clicking 
		searchText = (EditText) findViewById(R.id.searchtext);
		searchText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				searchText.setText("");
			}
		});
		//initializes the adapter for ActionListAdapter
		adapter = new ActionListAdapter(this, R.id.search_list, searchResults);
		explist = (ListView) findViewById(R.id.search_list);
		//connects the adapter to related field in the xml file
		explist.setAdapter(adapter);

		//sets a click listener for the seach button
		SearchExperience = (Button) findViewById(R.id.btn_search);
		SearchExperience.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//gets value from the text area
				searchString = searchText.getText().toString();
				//calls startSearch method to make HTTP request
				startSearch();
			}
		});

	}

	private void startSearch(){
		searchString = searchText.getText().toString();

		searchResults.clear();

		//executes asynchronized task to make HTTP requests 
		//for getting search results
		new MyAsyncTask().execute(GET_EXP);
	}

	/**
	 * Creates a customized adapter for the list view which lists 
	 * search results on the search page. Shows the elements of the 
	 * experiences and assigns click listener for them.
	 */
	private class ActionListAdapter extends ArrayAdapter<Experience> {
		private List<Experience> searchResults;

		public ActionListAdapter(Context context, int resourceId, 
				List<Experience> searchResults) {
			super(context, resourceId, searchResults);
			this.searchResults = searchResults;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.experience, null);
			}

			Experience experience = searchResults.get(position);
			if (experience != null) {

				TextView username = (TextView) view.findViewById(R.id.ex_username);
				if(username!=null){
					username.setText(experience.getAuthor().getUsername());
					username.setTag(new Integer(position));

					username.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent i = new Intent(getApplicationContext(), UserProfile.class);
							i.putExtra("username",searchResults.get(Integer.parseInt(v.getTag().toString())).getAuthor().getUsername());
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
					build.append(experience.getTags().get(0).getName());
					for(int i=1;i<experience.getTags().size();i++){
						build.append(",");
						build.append(experience.getTags().get(i).getName());
					}
					tags.setText(build.toString());
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
							i.putExtra("id",searchResults.get(Integer.parseInt(v.getTag().toString())).getId());
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
						if(searchResults.get(pos).isUpvotedByUser()){
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

				if(searchResults.get(position).isUpvotedByUser()){
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
						if(searchResults.get(pos).isDownvotedByUser()){
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

				if(searchResults.get(position).isDownvotedByUser()){
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
						if(searchResults.get(pos).isUserMarkedSpam()){
							spamButton.setImageResource(R.drawable.spam_inactive);                    	
						}else{
							spamButton.setImageResource(R.drawable.spam);

						}
						//executes asynchronized task to make HTTP requests 
						//for reporting the selected experience as spam
						new MyAsyncTask().execute(SPAM_EXP,v.getTag().toString());

					}
				});
				spam.setVisibility(View.VISIBLE);

				if(searchResults.get(position).isUserMarkedSpam()){
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
	 * search results, upvoting an experience, downvoting an experience, 
	 * reporting an experience, or deleting an experience. Makes HTTP 
	 * requests in background, and updates the page on the post execution.
	 */
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		private boolean getexp = false;
		private boolean get = false;
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
			if(params[0]==GET_EXP){
				getexp = true;
				getExperiences();
			}else if(params[0]==UPVOTE_EXP){
				upvote=true;
				upvoteExp(params[1]);
			}else if(params[0]==DOWNVOTE_EXP){
				downvote = true;
				downvoteExp(params[1]);
			}else if(params[0]==DELETE_EXP){
				delete = true;
				deleteExp(params[1]);
			}else if(params[0]==SPAM_EXP){
				spam=true;
				spamExp(params[1]);
			}
			return null;
		}

		protected void onPostExecute(Double result){
			//notifies the adapter to lists the experiences on the page
			if(getexp){
				adapter.notifyDataSetChanged();
			}
		}

		/**
		 * Makes HTTP post request to get results of the searching.
		 */
		public void getExperiences()  {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/experience/searchExperienceText"); 
			try {
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("text", searchString);
				Log.i("tag", searchString);

				JSONObject jsonobj2 = new JSONObject();
				jsonobj2.put("params", jsonobj);

				//sets header for http post request
				StringEntity se = new StringEntity(jsonobj2.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept", "application/json");
				httpPost.setEntity(se);
				HttpResponse response = httpclient.execute(httpPost);
				HttpEntity entity = (HttpEntity) response.getEntity(); 
				responseText = getASCIIContentFromEntity(entity);

				JSONObject myObject = new JSONObject(responseText);
				JSONArray jsona = new JSONArray(myObject.getString("experienceList"));

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
					searchResults.add(exp);

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
				jsonobj.put("id",searchResults.get(exp_pos).getId());
				jsonobj.put("undo",searchResults.get(exp_pos).isUpvotedByUser());

				undo = searchResults.get(exp_pos).isUpvotedByUser();

				if(searchResults.get(exp_pos).isUpvotedByUser()){
					searchResults.get(exp_pos).setUpvotedByUser(false);
				}else{
					searchResults.get(exp_pos).setUpvotedByUser(true);
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
				jsonobj.put("id",searchResults.get(exp_pos).getId());
				jsonobj.put("undo",searchResults.get(exp_pos).isDownvotedByUser());
				undo = searchResults.get(exp_pos).isDownvotedByUser();

				if(searchResults.get(exp_pos).isDownvotedByUser()){
					searchResults.get(exp_pos).setDownvotedByUser(false);
				}else{
					searchResults.get(exp_pos).setDownvotedByUser(true);
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

				innerJson.put("id",searchResults.get(exp_pos).getId());

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
			if(searchResults.get(exp_pos).isUserMarkedSpam()){
				httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobileunmarkSpam");

			}else{
				httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobilemarkSpam");

			}
			undo = searchResults.get(exp_pos).isUserMarkedSpam();
			if(undo){
				searchResults.get(exp_pos).setUserMarkedSpam(false);
			}else{
				searchResults.get(exp_pos).setUserMarkedSpam(true);
			}
			try {
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));

				jsonobj.put("IsLoggedIn", session.isLoggedIn());
				jsonobj.put("id",searchResults.get(exp_pos).getId());

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
