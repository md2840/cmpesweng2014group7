package com.group7.urbsourceandroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class UserProfile extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("started", "started");
		setContentView(R.layout.userprofile);
		
		//Username of user is updated in the page
		String usernameS = "gokce";
		Log.i("user", usernameS);
		TextView username = (TextView) findViewById(R.id.pro_username);
		username.setText(usernameS);
		
		//Point of user is updated in the page
		int pointI = 13;
		String pointS = Integer.toString(pointI);
		TextView point = (TextView) findViewById(R.id.pro_point);
		point.setText(pointS);
		
	}
}
