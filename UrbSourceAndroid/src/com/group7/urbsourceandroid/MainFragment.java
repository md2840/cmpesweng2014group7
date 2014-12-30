package com.group7.urbsourceandroid;



import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.group7.urbsourceandroid.models.Experience;
import com.group7.urbsourceandroid.models.Tag;
import com.group7.urbsourceandroid.models.User;


public class MainFragment extends Fragment{

	private ListView listView;
	private List<Experience> recentExperiences;
	private ActionListAdapter adapter;
	private SessionManager session;
	private String responseText;
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View view = inflater.inflate(R.layout.mainfragment, container, false);
        
        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();
        
        listView = (ListView) view.findViewById(R.id.exp_list);
        recentExperiences = new ArrayList<Experience>();
        adapter = new ActionListAdapter(getActivity(), 
                R.id.exp_list, recentExperiences);
       listView.setAdapter(adapter);
       addExperiences();
      
        return view;
	}   
	
	public void addExperiences(){
		
		new MyAsyncTask().execute();
	}
	
	private class ActionListAdapter extends ArrayAdapter<Experience> {
	    private List<Experience> recentExperiences;

	    public ActionListAdapter(Context context, int resourceId, 
	                             List<Experience> recentExperiences) {
	        super(context, resourceId, recentExperiences);
	        this.recentExperiences = recentExperiences;
	        // Set up as an observer for list item changes to
	        // refresh the view.
	        for (int i = 0; i < recentExperiences.size(); i++) {
	        	//recentExperiences.get(i).setAdapter(this);
	        }
	    }
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) getActivity()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.experience, null);
        }

        Experience experience = recentExperiences.get(position);
        if (experience != null) {

            
            TextView username = (TextView) view.findViewById(R.id.ex_username);
            if(username!=null){
            	username.setText(experience.getAuthor().getUsername());
            }
            TextView location = (TextView) view.findViewById(R.id.ex_location);
            //location add
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
            ImageButton upvote = (ImageButton) view.findViewById(R.id.ex_upvote);
            upvote.setTag(new Integer(position));
            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	//upvote httppost
                	Log.i("experience upvote",v.getTag().toString());
                }
                });
            ImageButton downvote = (ImageButton) view.findViewById(R.id.ex_downvote);
            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	//downvote httppost
                	
                }
                });
            
            //Upvote downvote u user a göre renklerini ayarla. Onclick eventlerini yaz.
            ImageButton delete = (ImageButton) view.findViewById(R.id.btn_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	//delete httppost
                	
                }
                });
            delete.setVisibility(View.INVISIBLE);
            CheckBox verify = (CheckBox) view.findViewById(R.id.ex_verify);
            verify.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                          
                  if (((CheckBox) v).isChecked()) {
                            //verify http post
                		Log.i("experience verify","okk");
                  }
                  else {
                	  //unverify http post
              		Log.i("experience unverify","step back");

                  }
                }
              });
            
            if(experience.getAuthor().getUsername().equals(session.getUserDetails().get("name"))){
            	delete.setVisibility(View.VISIBLE);
            	verify.setVisibility(View.INVISIBLE);
            	upvote.setVisibility(View.INVISIBLE);
            	downvote.setVisibility(View.INVISIBLE);
            }
        }
            
        return view;
    }

}
	
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		 
		private final ProgressDialog dialog = new ProgressDialog(getActivity());
		   
		
		    @Override
		
		    protected void onPreExecute() {       
		
		        super.onPreExecute();		
		        dialog.setMessage("Refreshing experiences...");
		        dialog.show();           
		
		    }

		@Override
		protected Double doInBackground(String... params) {
				try {
					getData();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			return null;
		}
 
		protected void onPostExecute(Double result){
			JSONObject myObject;
			try {
				
			 myObject = new JSONObject(responseText);
			
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
			     exp.setDownvotedByUser(jsonObj.getBoolean("downvotedByUser"));
			     exp.setUpvotedByUser(jsonObj.getBoolean("upvotedByUser"));
			     //USER çekme tarafý ileride ayrý method yap kolay olsun.
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
			     //TAG çekme tarafý
			     JSONArray Tags = new JSONArray(jsonObj.getString("tags"));
			     for(int j=0;j<Tags.length();j++){
			    	 JSONObject tagObj = Tags.getJSONObject(j);
					 Tag t = new Tag();
			    	 t.setId(tagObj.getInt("id"));
			    	 t.setName(tagObj.getString("name"));
			    	 exp.addTag(t);
			     }
			     recentExperiences.add(exp);
			    
			 }
			 
			 } catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 
			dialog.dismiss();
			adapter.notifyDataSetChanged();
			 
		}
		
 
		public void getData() throws JSONException {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			//HttpGet httpGet = new HttpGet("http://titan.cmpe.boun.edu.tr:8086/UrbSource/experience/recent");
			HttpPost httpPost = new HttpPost("http://10.0.3.2/UrbSource/experience/mobilerecent"); //for genymotion
			
			try {
			
				// Add your data
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));
				
				StringEntity se = new StringEntity(jsonobj.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httpPost.setHeader("Content-Type", "application/json");
				httpPost.setHeader("Accept", "application/json");
			
				httpPost.setEntity(se);
					// Execute HTTP Get Request
				HttpResponse response = httpclient.execute(httpPost);
					
				 HttpEntity entity = response.getEntity();
				 
				 responseText = getASCIIContentFromEntity(entity);
				 
						
				 
				 
				 
				 
 
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
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
