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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.urbsourcemyedition.models.Comment;
import com.example.urbsourcemyedition.models.Experience;
import com.example.urbsourcemyedition.models.Tag;
import com.example.urbsourcemyedition.models.User;

public class ShowExperience extends Activity{

	private SessionManager session;
	private String responseText;
	private AlertDialog.Builder alert;
	private ListView listView;
	private List<Comment> comments;
	private ActionListAdapter adapter;
	private final String GET_EXPERIENCE="ge";
	private final String GET_COMMENTS="gc";
	private final String DELETE_EXP="de";
	private final String EDIT_EXP="ee";
	private final String DELETE_CMT = "dc";
	private final String ADD_COMMENT = "ac";
	private String commentTxt;
			
	private int experience_id;
	private Experience exp;
	private TextView exp_username,exp_content,exp_tags,exp_mood,exp_comment,exp_location;
	private Button edit_exp, add_comment, save_exp;
	private EditText exp_edittext,exp_edittags,comment_add_text,exp_savecomment;
	private ImageButton exp_delete;
	//commentlerle birlikte experience göster burda
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.showexperience);
        
        session = new SessionManager(this.getApplicationContext());
        session.checkLogin();
        
        responseText="";
        alert =  new AlertDialog.Builder(this);
        
        Intent i = getIntent();
        // getting attached intent data
        experience_id = i.getIntExtra("id", -1);
        Log.i("exp_id",Integer.toString(experience_id));
        
        exp = new Experience();
        
        exp_username = (TextView) findViewById(R.id.exp_username);
        exp_content = (TextView) findViewById(R.id.exp_content);
        exp_tags = (TextView) findViewById(R.id.exp_tags);
        exp_mood = (TextView) findViewById(R.id.exp_mood);
        exp_comment = (TextView) findViewById(R.id.exp_comment);
        exp_location = (TextView) findViewById(R.id.exp_location);
        exp_location.setVisibility(View.INVISIBLE);
        
        exp_edittext = (EditText)findViewById(R.id.edit_content);
        exp_edittags = (EditText)findViewById(R.id.edit_tags);
        exp_edittext.setVisibility(View.GONE);
        exp_edittags.setVisibility(View.GONE);
        exp_savecomment = (EditText)findViewById(R.id.cmt_content);
        
        exp_delete = (ImageButton) findViewById(R.id.button_delete);
        edit_exp = (Button)  findViewById(R.id.button_edit);
        save_exp = (Button)  findViewById(R.id.button_save);
        add_comment = (Button)  findViewById(R.id.cmt_save);
        
        save_exp.setVisibility(View.GONE);
        exp_delete.setVisibility(View.GONE);
        edit_exp.setVisibility(View.GONE);
        
       edit_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	exp_content.setVisibility(View.GONE);
            	exp_tags.setVisibility(View.GONE);
            	exp_edittext.setText(exp_content.getText().toString());
            	exp_edittags.setText(exp_tags.getText().toString());
            	exp_edittext.setVisibility(View.VISIBLE);
            	exp_edittags.setVisibility(View.VISIBLE);
            	edit_exp.setVisibility(View.GONE);
            	save_exp.setVisibility(View.VISIBLE);
            	
            	
            }
            });
       
       save_exp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
        	   
        	exp.setText(exp_edittext.getText().toString());
        	String tags = exp_edittags.getText().toString();
        	if(tags.contains(",")){
				String[] splitTags = tags.split(",");
				for(int i=0;i<splitTags.length;i++){
					Tag t= new Tag();
					t.setName(splitTags[i]);
					exp.addTag(t);
				}
			}else{
				Tag t= new Tag();
				t.setName(tags);
				exp.addTag(t);
			}
			
           	exp_edittext.setVisibility(View.GONE);
           	exp_edittags.setVisibility(View.GONE);
           	edit_exp.setVisibility(View.VISIBLE);
           	save_exp.setVisibility(View.GONE);
           	new MyAsyncTask().execute(EDIT_EXP);
           	
           }
           });
       
       add_comment.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
        	   
        	
           	String text = exp_savecomment.getText().toString();
           	exp_savecomment.setText("");
           	new MyAsyncTask().execute(ADD_COMMENT,text);
           	
           }
           });
        listView = (ListView) findViewById(R.id.cmt_list);
        comments = new ArrayList<Comment>();
        adapter = new ActionListAdapter(this, 
                R.id.cmt_list, comments);
        listView.setAdapter(adapter);
        getExperience();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
          @Override
          public void run() {
        	  getComments();
          }
        }, 2000);
       
        
	}
	
	private void getExperience(){
		if(experience_id == -1){
			alert.setMessage("Experience does not exist");
			alert.show();
		}else{
			Log.i("id",Integer.toString(experience_id));
			exp.setId(experience_id);
	        new MyAsyncTask().execute(GET_EXPERIENCE);
		}
		
	}
	
	private void getComments(){
		new MyAsyncTask().execute(GET_COMMENTS);
	}
	
	private class ActionListAdapter extends ArrayAdapter<Comment> {
	    private List<Comment> comments;

	    public ActionListAdapter(Context context, int resourceId, 
	                             List<Comment> comments) {
	        super(context, resourceId, comments);
	        this.comments = comments;
	              
	    }
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) 
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.experience, null);
        }

        Comment comment = comments.get(position);
        if (comment != null) {

            
            TextView username = (TextView) view.findViewById(R.id.cmt_username);
            if(username!=null){
            	username.setText(comment.getAuthor().getUsername());
            	username.setTag(new Integer(position));
                
            	username.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    	Intent i = new Intent(getApplicationContext(), UserProfile.class);
                    	i.putExtra("username",comments.get(Integer.parseInt(v.getTag().toString())).getAuthor().getUsername());
                        startActivity(i);
                    }
                    
            	});
            }
          
            TextView content = (TextView) view.findViewById(R.id.cmt_content);
            if(content!=null){
            	content.setText(comment.getText());
            }
            
            ImageButton delete = (ImageButton) view.findViewById(R.id.cmt_delete);
            delete.setTag(new Integer(position));
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	//delete httppost
                	new MyAsyncTask().execute(DELETE_CMT,v.getTag().toString());
                }
                });
            
            delete.setVisibility(View.INVISIBLE);
            if(comment.getAuthor().getUsername().equalsIgnoreCase(session.getUserDetails().get("name"))){
            	delete.setVisibility(View.VISIBLE);
            }
        
        } 
        return view;
    }

}
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		 
	   	private boolean getExp = false;
	   	private boolean getComments = false;
	   	private boolean deleteExp = false;
	   	private boolean editExp = false;
	   	private boolean deleteCmt = false;
	   	private boolean addCmt = false;
	   	
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			if(params[0] == GET_EXPERIENCE){
				get_Exp();
				getExp = true;
			}else if(params[0] == GET_COMMENTS){
				get_Cmts();
				getComments = true;
			}else if(params[0] == DELETE_EXP){
				deleteExp();
				deleteExp = true;
			}else if(params[0] == EDIT_EXP){
				editExp();
				editExp = true;
			}else if(params[0] == DELETE_CMT){
				deleteCmt(params[1]);
				deleteCmt = true;
			}else if(params[0] == ADD_COMMENT){
				addCmt(params[1]);
				addCmt = true;
			}
				
			
			return null;
		}
 
		protected void onPostExecute(Double result){
			if(getExp){
				exp_username.setText(exp.getAuthor().getUsername());
				exp_content.setText(exp.getText());
				StringBuilder build = new StringBuilder();
            	build.append(exp.getTags().get(0).getName());
        		for(int i=1;i<exp.getTags().size();i++){
        			build.append(",");
            		build.append(exp.getTags().get(i).getName());
              	}
            	exp_tags.setText(build.toString());
            	exp_mood.setText(exp.getMood());
            	exp_comment.setText(exp.getNumberOfComments()+"");
            	
            	if(exp.getAuthor().getUsername().equalsIgnoreCase(session.getUserDetails().get("name"))){
            		exp_delete.setVisibility(View.VISIBLE);
            		edit_exp.setVisibility(View.VISIBLE);
            	}
			}else if(getComments){
				
				JSONObject myObject;
				try {
					myObject = new JSONObject(responseText);
				
				if(myObject.getBoolean("success")){
					
					 JSONArray jsona = new JSONArray(myObject.getString("comments"));
					 
					 for(int i=0; i<jsona.length();i++){  // teker teker comment gir.
						 JSONObject jsonObj = jsona.getJSONObject(i);
						 Comment cmt = new Comment();
						 User u = new User();
						 cmt.setId(jsonObj.getInt("id"));
						 cmt.setText(jsonObj.getString("text"));
						 
					     
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
					     cmt.setAuthor(u);
					    
					     
					     comments.add(cmt);
					    
					 }
					 
					adapter.notifyDataSetChanged();
					
					
				}else{
					alert.setTitle("error");
					alert.setMessage(myObject.getString("error"));
		    		alert.show();  
				}
				
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}else if(deleteExp){
				alert.setMessage("You deleted the experience");
				alert.show();
				finish();
			}else if(editExp){
				
				 exp_content.setText(exp.getText());
					StringBuilder build = new StringBuilder();
		        	build.append(exp.getTags().get(0).getName());
		    		for(int i=1;i<exp.getTags().size();i++){
		    			build.append(",");
		        		build.append(exp.getTags().get(i).getName());
		          	}
		        	
		        	exp_tags.setText(build.toString());
		        	
		           	exp_content.setVisibility(View.VISIBLE);
		           	exp_tags.setVisibility(View.VISIBLE);
		           	exp_edittext.setText("");
		           	exp_edittags.setText("");
		           	exp_edittext.setVisibility(View.GONE);
		           	exp_edittags.setVisibility(View.GONE);

				try {
					JSONObject myObject = new JSONObject(responseText);
					if(myObject.getBoolean("success")){
					   alert.setMessage("You edited the experience");
					   alert.show();
					  					}else{
							 String error = myObject.getString("error");
							 alert.setMessage(error);
							 alert.show();
							 exp_content.setVisibility(View.VISIBLE);
					         exp_tags.setVisibility(View.VISIBLE);
						    
						
					}
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
			}else if(deleteCmt){
				
				alert.setMessage("You deleted your comment");
				alert.show();
			}else if(addCmt){
				alert.setMessage("You created a comment");
				alert.show();
				 Comment newCmt = new Comment();
				 newCmt.getAuthor().setUsername(session.getUserDetails().get("name"));
				 newCmt.setText(commentTxt);
				 comments.add(newCmt);
				adapter.notifyDataSetChanged();
			}
			
		}
		
	  private void get_Exp(){
		// Create a new HttpClient and Post Header
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/id/mobile");
					try {
						// Add your data
						JSONObject jsonobj = new JSONObject();
						jsonobj.put("username",session.getUserDetails().get("name"));
						jsonobj.put("expId",experience_id);
						StringEntity se = new StringEntity(jsonobj.toString());    
						se.setContentType("application/json;charset=UTF-8");
						se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
						httppost.setHeader("Content-Type", "application/json");
						httppost.setHeader("Accept", "application/json");
					
						httppost.setEntity(se);
						// Execute HTTP Post Request
						HttpResponse response = httpclient.execute(httppost);
						HttpEntity entity = response.getEntity();
						 
						responseText = getASCIIContentFromEntity(entity);
					 Log.i("response", responseText);
					 JSONObject myObject = new JSONObject(responseText);
						
					 JSONObject jsonObj = new JSONObject(myObject.getString("experiences"));;
					 User u = new User();
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
				     
				    
						  
		 
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	  }
	  
	  private void get_Cmts(){
		// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/getcomments");
			try {
				// Add your data
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));
				jsonobj.put("IsLoggedIn", session.isLoggedIn());
				
				jsonobj.put("id",experience_id);
				StringEntity se = new StringEntity(jsonobj.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httppost.setHeader("Content-Type", "application/json");
				httppost.setHeader("Accept", "application/json");
			
				httppost.setEntity(se);
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				 
				responseText = getASCIIContentFromEntity(entity);
			 Log.i("response", responseText);
					
				  

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
	  
	  public void deleteExp(){
			
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobiledelete");
			
			
			try {
					
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));
				
				jsonobj.put("IsLoggedIn", session.isLoggedIn());
				JSONObject innerJson = new JSONObject();
				
				innerJson.put("id",exp.getId());
				 
				jsonobj.put("params", innerJson);
				
				
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	  public void deleteCmt(String pos){
			int position = Integer.parseInt(pos);
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/delete");
			
			
			try {
					
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));
				
				jsonobj.put("IsLoggedIn", session.isLoggedIn());
				jsonobj.put("id",comments.get(position).getId());
				
				
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	  
	  public void addCmt(String commentText){
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/createcomment");
			
			
			try {
					
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));
				
				jsonobj.put("IsLoggedIn", session.isLoggedIn());
				jsonobj.put("text", commentText);
				jsonobj.put("experienceId",experience_id);
				commentTxt = commentText; 
				
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	  public void editExp(){
			
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobileupdate");
			
			
			try {
					
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));
				
				jsonobj.put("IsLoggedIn", session.isLoggedIn());
							
				jsonobj.put("id",exp.getId());
				 
				jsonobj.put("text",exp.getText());
				JSONArray jsona = new JSONArray(exp.getTags());
				jsonobj.put("tags",jsona);
				
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
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
