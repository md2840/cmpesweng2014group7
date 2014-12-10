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
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


public class MainFragment extends Fragment{

	private ListView listView;
	private List<Experience> recentExperiences;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View view = inflater.inflate(R.layout.mainfragment, container, false);
        
        listView = (ListView) view.findViewById(R.id.exp_list);
        recentExperiences = new ArrayList<Experience>();
        addExperiences();
        listView.setAdapter(new ActionListAdapter(getActivity(), 
                R.id.exp_list, recentExperiences));

        
        
        
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
            	tags.setText(experience.getText());
            }
            ImageButton upvote = (ImageButton) view.findViewById(R.id.ex_upvote);
            upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	//upvote httppost
                	Log.i("experience upvote","geldi");
                }
                });
            ImageButton downvote = (ImageButton) view.findViewById(R.id.ex_downvote);
            downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	//downvote httppost
                	Log.i("experience downvote","geldi");
                }
                });
            ImageButton delete = (ImageButton) view.findViewById(R.id.btn_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	//delete httppost
                	Log.i("experience delete","geldi");
                }
                });
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
            }
            
        return view;
    }

}
	
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		 
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				getData();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
 
		protected void onPostExecute(Double result){
			
		}
		
 
		public void getData() throws JSONException {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/signup/confirm");
			HttpGet httpGet = new HttpGet("http://titan.cmpe.boun.edu.tr:8086/UrbSource/experience/recent");
			try {
			
				
					// Execute HTTP Get Request
					HttpResponse response = httpclient.execute(httpGet);
					
				 Log.i("geldi mi",response.getStatusLine().toString());
				 HttpEntity entity = response.getEntity();
				 
				 String text = getASCIIContentFromEntity(entity);
				 JSONObject myObject = new JSONObject(text);
				 JSONArray jsona = new JSONArray(myObject.getString("experiences"));
				 Log.i("json array size",Integer.toString(jsona.length()));
				// ObjectMapper mapper = new ObjectMapper();
				 
				 for(int i=0; i<jsona.length();i++){
					 Log.i("kacýncý",Integer.toString(i));
					 //JSONObject jsonObj = jsona.getJSONObject(i);
					 String json = jsona.getString(i);
					 
				     Log.i("json array eleman",json);
				     Experience exp = null;
				    
//					 Experience e = new ObjectMapper().readValue(jsona.get(i).toString(), Experience.class);
//					 if(e!=null){
//						 recentExperiences.add(e);
//						 Log.i("experiences added",e.getAuthor().getUsername());
//					 }
//					Object mapper ýn çalýþmasý lazým ama nedense çalýþýp experience assigne edemiyor
				     //en son çare olarak elle bütün fieldlarý girmek.
					 
				 }
				 
				 
 
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
