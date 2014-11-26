package com.group7.urbsourceandroid;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 
 * 
 * @author Gokce Yesiltas
 *
 */

public class Register extends Activity {

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
	}
	/**
	 * Is called when the register button is clicked on the register page. 
	 */
	public void register(View view){
		String username = ((EditText)findViewById(R.id.reg_username)).getText().toString();
		String firstname = ((EditText)findViewById(R.id.reg_firstname)).getText().toString();
		String lastname = ((EditText)findViewById(R.id.reg_lastname)).getText().toString();
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

	}
	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}

}