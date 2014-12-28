package com.group7.urbsourceandroid;



import java.io.IOException;
import java.io.InputStream;
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

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateExperienceFragment extends Fragment {

	private EditText Text,Tags;
	private Button save;
	private Spinner MoodS;
	
	//Session for users
	SessionManager session;
	
	private String responseString;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View view = inflater.inflate(R.layout.createexp, container, false);
        
        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin(); 
       
        Text = (EditText) view.findViewById(R.id.editText1);
        Tags = (EditText) view.findViewById(R.id.editText2);
        MoodS = (Spinner) view.findViewById(R.id.spinner1);
        
        List<String> moodList = new ArrayList<String>();
    	moodList.add("Good");
    	moodList.add("Bad");
    	
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
    		android.R.layout.simple_spinner_item, moodList);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	MoodS.setAdapter(dataAdapter);
        
        save = (Button) view.findViewById(R.id.button1);
        save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                	//create new experience
                	createExp();
                }
                });
        return view;
	}
	
	private void createExp(){
		String exptext = Text.getText().toString();
		String tags = Tags.getText().toString();
		String mood = MoodS.getSelectedItem().toString();
		new MyAsyncTask().execute(exptext,tags,mood);
	}
	
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		 
		
		    @Override
		
		    protected void onPreExecute() {       
		
		        super.onPreExecute();		
		           
		
		    }

		@Override
		protected Double doInBackground(String... params) {
				try {
					sendData(params[0],params[1],params[2]);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			return null;
		}
 
		protected void onPostExecute(Double result){
			
			Log.i("result",responseString);
			
		}
		
 
		public void sendData(String Text,String Tags, String Mood) throws JSONException {
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://10.0.3.2/UrbSource/experience/mobilecreate");
			
			try {
			
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));
				jsonobj.put("IsLoggedIn", session.isLoggedIn());
				jsonobj.put("text", Text);
				jsonobj.put("mood", Mood);
				JSONArray jsona = new JSONArray();
				
				if(Tags.contains(",")){
					String[] splitTags = Tags.split(",");
					for(int i=0;i<splitTags.length;i++){
						jsona.put(splitTags[i]);
					}
				}else{
					jsona.put(Tags);
				}
				
				jsonobj.put("tags", jsona);
				
				
				
				
				
				StringEntity se = new StringEntity(jsonobj.toString());    
				se.setContentType("application/json;charset=UTF-8");
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
				httppost.setHeader("Content-Type", "application/json");
				httppost.setHeader("Accept", "application/json");
			
				httppost.setEntity(se);
				// Execute HTTP Post Request
				HttpResponse response = httpclient.execute(httppost);
				
				HttpEntity entity = response.getEntity();
				 
				String text = getASCIIContentFromEntity(entity);
				responseString = text;
				
				 
				 
 
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
