package com.group7.urbsourceandroid;
import java.io.IOException;
import java.io.InputStream;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


/**
 * Shows register page and registers the user by sending 
 * information which are gathered from the form to WEB API.
 * 
 * @author Gokce Yesiltas
 * @author Dilara Kekulluoglu
 *
 */

public class Register extends Activity {
	private Button register;
	private String responseString=null;
	private EditText usernameT; 
	private EditText emailT;

	/**
	 * Creates a view for register page. 
	 * Sets a click listener for the login link on the page.
	 * Sets a click listener for the register button.
	 */
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
				//Calls the method to make http request
				register(arg0);

			}
		});
	}

	/**
	 * Is called when the register button is clicked on the register page. 
	 */
	public void register(View view){
		usernameT = (EditText)findViewById(R.id.reg_username);
		String username = usernameT.getText().toString();
		String firstName = ((EditText)findViewById(R.id.reg_firstname)).getText().toString();
		String lastName = ((EditText)findViewById(R.id.reg_lastname)).getText().toString();
		emailT = (EditText) findViewById(R.id.reg_email);
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
		new MyAsyncTask().execute(username,firstName,lastName,email,password,password2);	
	}
	/**
	 * Checks whether the parameter is in the format of e-mail address or not.
	 * 
	 * @author Gokce Yesiltas
	 * @param target Text which is wanted to check
	 * @return true if the target is in the format of an e-mail address, else false.
	 */
	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

	/**
	 * @author dilara kekulluoglu
	 *     AsyncTask class which does http post, get in background.
	 *     When it finishes, signs up the new user. 
	 * 
	 * */
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

			try {
				JSONObject res = new JSONObject(responseString);
				if(!res.getBoolean("success")){
					Toast.makeText(getApplicationContext(), "Username or email already in use", Toast.LENGTH_LONG).show();
					usernameT.setText("");
					emailT.setText("");
				}else{
					Toast.makeText(getApplicationContext(), "Sign Up Successful", Toast.LENGTH_LONG).show();
					finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		public void postData(String username, String firstName,String lastName,String email,String password,String password2) throws JSONException {
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/signup/mobileconfirm");
			try {
				// Add your data
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",username);
				jsonobj.put("firstName",firstName);
				jsonobj.put("lastName",lastName);
				jsonobj.put("email",email);
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

