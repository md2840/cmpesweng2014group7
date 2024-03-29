package com.group7.urbsourceandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
*@author Dilara Kekulluoglu
* The last fragment of the home page 
* Shows settings ; user profile , search, and logout.
*/
public class SettingsFragment extends Fragment {

	private Button UserProfile, Logout, SearchExperience;

	private SessionManager session;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.settings, container, false);

		session = new SessionManager(getActivity().getApplicationContext());
		session.checkLogin();

		UserProfile = (Button) view.findViewById(R.id.userprofile);
		UserProfile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity().getApplicationContext(), UserProfile.class);
				startActivity(i);
			}
		});

		SearchExperience = (Button) view.findViewById(R.id.searchpage);
		SearchExperience.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity().getApplicationContext(), SearchExperience.class);
				startActivity(i);
			}
		});

		Logout = (Button) view.findViewById(R.id.logout);
		Logout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				session.logoutUser();

			}
		});
		return view;
	}


}
