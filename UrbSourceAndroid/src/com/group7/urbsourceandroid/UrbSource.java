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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
 
public class UrbSource extends Activity {
	
	private Button login;
	
	//Session for users
	SessionManager session;
	
	//EditTexts
	EditText usernameT,passwordT;
	
	String responseText;
	
	String username , password;
	
 	private ProgressDialog dialog;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setting default screen to login.xml
        
        setContentView(R.layout.activity_main);
        
        
        session = new SessionManager(this.getApplicationContext());
        
        if(session.isLoggedIn()){
        	Intent i = new Intent(getApplicationContext(), HomePage.class);
            startActivity(i);
            //This may result in not being able to exit. Check for it.
            finish();
        }
        
        dialog = new ProgressDialog(this);
        
        
        Button registerScreen = (Button) findViewById(R.id.link_to_register);
 
        // Listening to register new account link
        registerScreen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });
        
        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View v) {
                
                Login();
            }
        });
        
        usernameT = (EditText) findViewById(R.id.username);
        passwordT = (EditText) findViewById(R.id.password);
        
    }
    public void Login(){
    	//Check from the api if there exists such user
    	username = usernameT.getText().toString();
    	password = passwordT.getText().toString();
    	
    	new MyAsyncTask().execute(username,password);	
    	
    	
    	
    }
    
    private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		 
   	
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				checkLogin(params[0],params[1]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
 
		protected void onPostExecute(Double result){
			
			 try {
				 Log.i("gelen response",responseText);
				JSONObject myObject = new JSONObject(responseText);
				if(myObject.getBoolean("success")){
					session.createLoginSession(username,password);
					Intent i = new Intent(getApplicationContext(), HomePage.class);
			        startActivity(i);
			        finish();
				}else{
						 String error = myObject.getString("error");
						 dialog.setMessage(error);
					     dialog.show(); 
					     usernameT.setText("");
					     passwordT.setText("");
					     username = null;
					     password = null;
					
				}
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
			 
		}
		
 
		public void checkLogin(String username,String password) throws JSONException {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://10.0.3.2/UrbSource/mobilelogin/confirm");
			try {
				// Add your data
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",username);
				jsonobj.put("password",password);
				
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
}