package com.group7.urbsourceandroid;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class CreateExperienceFragment extends Fragment implements LocationListener,GoogleMap.OnMapClickListener {

	private EditText Text,Tags;
	private Button save;
	private Spinner MoodS;


    private LocationManager locationManager;
    private GoogleMap mMap;

	//Session for users
	SessionManager session;
	private String responseString;
    private LatLng LATLNG=new LatLng(41.017238, 28.953128);
    public static FragmentManager fragmentManager;
    GPSTracker gps;
    double latitude;
    double longitude;
    ImageButton locButton;
    Marker marker;
    LatLng currentLatLing;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.createexp, container, false);



        session = new SessionManager(getActivity().getApplicationContext());

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
        fragmentManager = getFragmentManager();
        setUpMapIfNeeded();
        locButton = (ImageButton) view.findViewById(R.id.locationBut);

        // Setting click event listener for the find button

        locButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // create class object
                gps = new GPSTracker(getActivity());
                // check if GPS enabled
                if(gps.canGetLocation()){
                    if(marker!=null){
                        marker.remove();
                    }



                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        marker = mMap.addMarker(new MarkerOptions()
                                     .position(new LatLng(latitude, longitude))
                                     .title(Text.getText().toString())
                                     .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerlogo))
                        );



                        currentLatLing=new LatLng(latitude,longitude);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLing));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

                }else{

                    gps.showSettingsAlert();
                }
            }
        });



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
    public  void setUpMapIfNeeded() {

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) fragmentManager
                    .findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null)
                setUpMap();
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the
     * camera.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap}
     * is not null.
     */
    String provider;

    private  void setUpMap() {
        // For showing a move to my loction button
        locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        provider=locationManager.GPS_PROVIDER;

        mMap.setOnMapClickListener(this);


        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showGPSDisabledAlertToUser();

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LATLNG));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
	private void createExp(){
		String exptext = Text.getText().toString();
		String tags = Tags.getText().toString();
		String mood = MoodS.getSelectedItem().toString();
        String location="";
        if(currentLatLing!=null) {
             location= currentLatLing.toString();
        }


        //Here we need to pass data as parameters to MyAsyncTask together with location
        if(exptext.equals("")){
            Toast.makeText(getActivity().getBaseContext(), "Place Enter Your Experience Text!",
                    Toast.LENGTH_SHORT).show();
        }else if(tags.equals("")){
            Toast.makeText(getActivity().getBaseContext(), "Place Enter Your Experience Tags!",
                    Toast.LENGTH_SHORT).show();
        }else if(location.equals("")){
            new MyAsyncTask1().execute(exptext,tags,mood);
        }
        else{
		    new MyAsyncTask().execute(exptext,tags,mood,location);
        }
	}

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(marker!=null){
            marker.remove();
        }
        latitude=latLng.latitude;
        longitude=latLng.longitude;
        marker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(Text.getText().toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerlogo))
        );
        currentLatLing=latLng;

    }

    private class MyAsyncTask extends AsyncTask<String, Integer, Double>{
		 
		
		    @Override
		
		    protected void onPreExecute() {       
		
		        super.onPreExecute();		
		           
		
		    }

		@Override
		protected Double doInBackground(String... params) {
				try {
					sendData(params[0],params[1],params[2],params[3]);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			return null;
		}
 
		protected void onPostExecute(Double result){
			
			Log.i("result",responseString);

            Text.setText("");
            Tags.setText("");
            Toast.makeText(getActivity().getBaseContext(), "You Have Created an Experience",
                    Toast.LENGTH_SHORT).show();

			
		}
		

		public void sendData(String Text,String Tags, String Mood,String Location) throws JSONException {
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobilecreate");
			
			try {
			
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("username",session.getUserDetails().get("name"));
				jsonobj.put("IsLoggedIn", session.isLoggedIn());
				jsonobj.put("text", Text);
				jsonobj.put("mood", Mood);
                jsonobj.put("location", Location);
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

    private class MyAsyncTask1 extends AsyncTask<String, Integer, Double>{


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

            Text.setText("");
            Tags.setText("");
            Toast.makeText(getActivity().getBaseContext(), "You Have Created an Experience",
                    Toast.LENGTH_SHORT).show();

        }


        public void sendData(String Text,String Tags, String Mood) throws JSONException {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobilecreate");

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
    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("Please enable your GPS.")
                .setCancelable(false)
                .setPositiveButton("Enable",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

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
	



