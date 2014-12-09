package com.group7.urbsourceandroid;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.group7.urbsourceandroid.models.User;

/**
 * 
 * 
 * @author Gokce Yesiltas
 *
 */

public class Register extends Activity {
	
	private Button register;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set View to register.xml
		setContentView(R.layout.register);


		TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
		
		// Listening to Login Screen link
		loginScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				// Closing registration screen
				// Switching to Login Screen/closing register screen
				finish();
			}
		});
		
		register = (Button) findViewById(R.id.btnRegister);
		register.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				//register(arg0);
				deneme();
			}
		});
	}
	/**
	 * Is called when the register button is clicked on the register page. 
	 */
	public void register(View view){
		String username = ((EditText)findViewById(R.id.reg_username)).getText().toString();
		String firstName = ((EditText)findViewById(R.id.reg_firstname)).getText().toString();
		String lastName = ((EditText)findViewById(R.id.reg_lastname)).getText().toString();
		EditText emailT = (EditText) findViewById(R.id.reg_email);
		String email = emailT.getText().toString();
		EditText passwordT = (EditText)findViewById(R.id.reg_password);
		String password = passwordT.getText().toString();
		String password2 = ((EditText)findViewById(R.id.reg_password2)).getText().toString();

		boolean isValidEmail = isValidEmail(email);
		if(isValidEmail)
			Log.i("email validity", "valid");
		else{
			Log.i("email validity", "not valid");
			emailT.setError("Email is not valid");
		}
		if(!password.equals(password2))
			passwordT.setError("Passwords aren't matched.");
		
		///////send'em all to API
//		 HttpClient httpclient = new DefaultHttpClient();
//		 HttpPost httppost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/signup/confirm");
//		 try {
//		        // Add your data
//		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//		        nameValuePairs.add(new BasicNameValuePair("username", username));
//		        nameValuePairs.add(new BasicNameValuePair("firstName", firstName));
//		        nameValuePairs.add(new BasicNameValuePair("lastName", lastName));
//		        nameValuePairs.add(new BasicNameValuePair("email", email));
//		        nameValuePairs.add(new BasicNameValuePair("password", password));
//		        
//		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//		        // Execute HTTP Post Request
//		        HttpResponse response = httpclient.execute(httppost);
//		        Log.i("geldi mi",response.toString());
//
//		    } catch (ClientProtocolException e) {
//		        // TODO Auto-generated catch block
//		    } catch (IOException e) {
//		        // TODO Auto-generated catch block
//		    }

		new MyAsyncTask().execute(username,firstName,lastName,email,password,password2);	
	}
	public void deneme(){
		
		String username="dilara91";
		String firstName = "Dilara";
		String lastName = "Kek";
		String email = "dilara.kekulluoglu@boun.edu.tr";
		String password = "123456";
		String password2 = "123456";
		new MyAsyncTask().execute(username,firstName,lastName,email,password,password2);	
	}
	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
	
	private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		 
		@Override
		protected Double doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				postData(params[0],params[1],params[2],params[3],params[4],params[5]);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
 
		protected void onPostExecute(Double result){
			Toast.makeText(getApplicationContext(), "command sent", Toast.LENGTH_LONG).show();
		}
		
 
		public void postData(String username, String firstName,String lastName,String email,String password,String password2) throws JSONException {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/signup/confirm");
 
			try {
				// Add your data
//				JSONObject jsonobj = new JSONObject();
//				jsonobj.put("username",username);
//				jsonobj.put("firstName",firstName);
//				jsonobj.put("lastName",lastName);
//				jsonobj.put("email",email);
//				jsonobj.put("password",password);
//				StringEntity se = new StringEntity(jsonobj.toString());    
//				se.setContentType("application/json;charset=UTF-8");
//				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
//				httppost.setHeader("Content-Type", "application/json");
//				 httppost.setHeader("Accept", "application/json");
//			
//				httppost.setEntity(se);
//				
//				// Execute HTTP Post Request
//				HttpResponse response = httpclient.execute(httppost);
				
				User u= new User();
				u.setUsername(username);
				u.setFirstName(firstName);
				u.setLastName(lastName);
				u.setEmail(email);
				u.setPassword(password);
				u.setPassword2(password2);
				Log.i("parametreler",username);
				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(u);
				 Log.i("json",json);
				 
				 //httppost.setHeader("Content-Type", "application/json");
				  StringEntity se = new StringEntity(json);    
				 se.setContentType("application/json;charset=UTF-8");
				 se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));

				 httppost.setEntity(se);
					
					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);
					
				 Log.i("geldi mi",response.getStatusLine().toString());

 
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
 
	}

}