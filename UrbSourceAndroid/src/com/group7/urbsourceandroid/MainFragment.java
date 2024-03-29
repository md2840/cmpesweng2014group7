package com.group7.urbsourceandroid;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.group7.urbsourceandroid.models.Experience;
import com.group7.urbsourceandroid.models.Tag;
import com.group7.urbsourceandroid.models.User;


/**
* @author Dilara Kekulluoglu
* MainFragment is the page that show when we login, it has 2 other sibling fragments
* which we can go by swiping to right
*/

public class MainFragment extends Fragment{

    private ListView listView;
    private List<Experience> recentExperiences;
    private ActionListAdapter adapter;
    private SessionManager session;
    final String GET_EXPERIENCES="ge";
    final String UPVOTE_EXP="ue";
    final String DOWNVOTE_EXP="de";
    final String DELETE_EXP="dele";
    final String SPAM_EXP = "se";
    private String responseText;
    private AlertDialog.Builder alert ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mainfragment, container, false);

        //session management to see whether the user is logged in. Kicks out if not
        session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();

        responseText="";
        alert =  new AlertDialog.Builder(getActivity());
       
        //listview to show the experiences on the screen as list
        //we assign adapter to our experience list to watch the changes
        listView = (ListView) view.findViewById(R.id.exp_list);
        recentExperiences = new ArrayList<Experience>();
        adapter = new ActionListAdapter(getActivity(),
                R.id.exp_list, recentExperiences);
        listView.setAdapter(adapter);
        addExperiences();

        return view;
    }

    /**
    *@author Dilara Kekulluoglu
    *gets the experiences from the server and adds it to list to show on page
    */
    public void addExperiences(){

        new MyAsyncTask().execute(GET_EXPERIENCES);
    }
    
    /**
    *@author Dilara Kekulluoglu
    *customized adapter to go with our experience items.
    *shows the elements of experience and assigns onclick listeners for it
    */

    private class ActionListAdapter extends ArrayAdapter<Experience> {
        private List<Experience> recentExperiences;

        public ActionListAdapter(Context context, int resourceId,
                                 List<Experience> recentExperiences) {
            super(context, resourceId, recentExperiences);
            this.recentExperiences = recentExperiences;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup par1ent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater =
                        (LayoutInflater) getActivity()
                                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.experience, null);
            }

            final Experience experience = recentExperiences.get(position);
            if (experience != null) {


                TextView username = (TextView) view.findViewById(R.id.ex_username);
                if(username!=null){
                    username.setText(experience.getAuthor().getUsername());
                    username.setTag(new Integer(position));

                    username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity().getApplicationContext(), UserProfile.class);
                            i.putExtra("username",recentExperiences.get(Integer.parseInt(v.getTag().toString())).getAuthor().getUsername());
                            startActivity(i);
                        }

                    });
                }
                ImageButton location = (ImageButton) view.findViewById(R.id.ex_location);
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent myIntent = new Intent(getContext(), ShowOnMap.class);
                        myIntent.putExtra("LatLing",experience.getLocation());
                        startActivityForResult(myIntent, 0);
                    }
                });

                TextView mood = (TextView) view.findViewById(R.id.ex_mood);


                mood.setText(experience.getMood()+" Experience");
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

                TextView comment = (TextView) view.findViewById(R.id.ex_comment);
                if(comment!=null){
                    if(experience.getNumberOfComments()==0){
                        comment.setText("No comments");
                    }else if(experience.getNumberOfComments()==1){
                        comment.setText("1 comment");
                    }else{
                        comment.setText(experience.getNumberOfComments()+" comments");
                    }
                    comment.setTag(new Integer(position));

                    comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getActivity().getApplicationContext(), ShowExperience.class);
                            i.putExtra("id",recentExperiences.get(Integer.parseInt(v.getTag().toString())).getId());
                            startActivity(i);
                        }

                    });
                }

                ImageButton upvote = (ImageButton) view.findViewById(R.id.ex_upvote);
                upvote.setTag(new Integer(position));
                upvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(v.getTag().toString());
                        ImageButton upButton = (ImageButton) v;
                        if(recentExperiences.get(pos).isUpvotedByUser()){
                            upButton.setImageResource(R.drawable.arrow_up_inactive);
                        }else{
                            upButton.setImageResource(R.drawable.arrow_up);

                        }
                        new MyAsyncTask().execute(UPVOTE_EXP,v.getTag().toString());

                    }
                });
                upvote.setVisibility(View.VISIBLE);

                if(recentExperiences.get(position).isUpvotedByUser()){
                    upvote.setImageResource(R.drawable.arrow_up);
                }else{
                    upvote.setImageResource(R.drawable.arrow_up_inactive);
                }

                ImageButton downvote = (ImageButton) view.findViewById(R.id.ex_downvote);
                downvote.setTag(new Integer(position));
                downvote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(v.getTag().toString());
                        ImageButton downButton = (ImageButton) v;
                        if(recentExperiences.get(pos).isDownvotedByUser()){
                            downButton.setImageResource(R.drawable.arrow_down_inactive);
                        }else{
                            downButton.setImageResource(R.drawable.arrow_down);
                        }
                        new MyAsyncTask().execute(DOWNVOTE_EXP,v.getTag().toString());
                    }
                });
                downvote.setVisibility(View.VISIBLE);

                if(recentExperiences.get(position).isDownvotedByUser()){
                    downvote.setImageResource(R.drawable.arrow_down);
                }else{
                    downvote.setImageResource(R.drawable.arrow_down_inactive);
                }

                //WEB api spam i��in a����l��p deploy edince a��al��m.

                ImageButton spam = (ImageButton) view.findViewById(R.id.ex_spam);
                spam.setTag(new Integer(position));
                spam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = Integer.parseInt(v.getTag().toString());
                        ImageButton spamButton = (ImageButton) v;
                        if(recentExperiences.get(pos).isUserMarkedSpam()){
                            spamButton.setImageResource(R.drawable.spam_inactive);
                        }else{
                            spamButton.setImageResource(R.drawable.spam);

                        }
                        new MyAsyncTask().execute(SPAM_EXP,v.getTag().toString());

                    }
                });
                spam.setVisibility(View.VISIBLE);

                if(recentExperiences.get(position).isUserMarkedSpam()){
                    spam.setImageResource(R.drawable.spam);
                }else{
                    spam.setImageResource(R.drawable.spam_inactive);
                }

                ImageButton delete = (ImageButton) view.findViewById(R.id.btn_delete);
                delete.setTag(new Integer(position));
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Delete entry")
                                .setMessage("Are you sure you want to delete this experience?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        new MyAsyncTask().execute(DELETE_EXP,v.getTag().toString());
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });

                delete.setVisibility(View.INVISIBLE);
                //            CheckBox verify = (CheckBox) view.findViewById(R.id.ex_verify);
                //            verify.setOnClickListener(new View.OnClickListener() {
                //
                //                @Override
                //                public void onClick(View v) {
                //
                //                  if (((CheckBox) v).isChecked()) {
                //                            //verify http post
                //                		Log.i("experience verify","okk");
                //                  }
                //                  else {
                //                	  //unverify http post
                //              		Log.i("experience unverify","step back");
                //
                //                  }
                //                }
                //              });
                Log.i("name experience user",experience.getAuthor().getUsername());
                if(experience.getAuthor().getUsername().equals(session.getUserDetails().get("name"))){
                    delete.setVisibility(View.VISIBLE);
                    spam.setVisibility(View.INVISIBLE);
                    upvote.setVisibility(View.INVISIBLE);
                    downvote.setVisibility(View.INVISIBLE);
                }
            }


            return view;
        }

    }
    /**
    * @author Dilara Kekulluoglu
    * customized Async task for getting experiences; adding upvotes,downvotes and spam; view map
    */
    private class MyAsyncTask extends AsyncTask<String, Integer, Double>{

        private final ProgressDialog dialog = new ProgressDialog(getActivity());

        private boolean get = false;
        private boolean upvote = false;
        private boolean downvote = false;
        private boolean delete = false;
        private boolean spam = false;
        private boolean undo = false;
        private int position;
        @Override

        protected void onPreExecute() {
            super.onPreExecute();

            if(get){
                dialog.setMessage("Refreshing experiences...");
                dialog.show();
            }
        }

        @Override
        protected Double doInBackground(String... params) {

            if(params[0]==GET_EXPERIENCES){
                get=true;
                getData();
            }else if(params[0]==UPVOTE_EXP){
                upvote=true;
                upvoteExp(params[1]);
            }else if(params[0]==DOWNVOTE_EXP){
                downvote = true;
                downvoteExp(params[1]);
            }else if(params[0]==DELETE_EXP){
                delete = true;
                deleteExp(params[1]);
            }else if(params[0]==SPAM_EXP){
                spam=true;
                spamExp(params[1]);
            }


            return null;
        }
        //after the http post parses the results according to the method

        protected void onPostExecute(Double result){
            if(get){
                dialog.dismiss();
                adapter.notifyDataSetChanged();
            }else if(upvote){
                try {
                    JSONObject myObject = new JSONObject(responseText);

                    if(myObject.getBoolean("success")){
                        alert.setTitle("Success");

                        if(undo){

                            alert.setMessage("You unliked the experience");
                            undo =false;
                            alert.show();
                        }else{
                            alert.setMessage("You liked the experience");
                            alert.show();
                        }

                    }else{
                        alert.setTitle("error");
                        alert.setMessage(myObject.getString("error"));
                        alert.show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }else if(downvote){
                try {
                    JSONObject myObject = new JSONObject(responseText);

                    if(myObject.getBoolean("success")){
                        alert.setTitle("success");

                        if(undo){
                            alert.setMessage("You took back the dislike");
                            undo = false;
                            alert.show();
                        }else{
                            alert.setMessage("You disliked the experience");
                            alert.show();
                        }


                    }else{
                        alert.setTitle("error");
                        alert.setMessage(myObject.getString("error"));
                        alert.show();
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else if(delete){
                try {
                    JSONObject myObject = new JSONObject(responseText);

                    if(myObject.getBoolean("success")){
                        alert.setTitle("success");
                        alert.setMessage("You deleted your experience");
                        alert.show();
                    }else{
                        alert.setTitle("error");
                        alert.setMessage(myObject.getString("error"));
                        alert.show();
                    }
                    recentExperiences.remove(position);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else if(spam){
                try {
                    JSONObject myObject = new JSONObject(responseText);

                    if(myObject.getBoolean("success")){
                        alert.setTitle("success");

                        if(undo){
                            alert.setMessage("You took back the spam alert");
                            undo = false;
                            alert.show();
                        }else{
                            alert.setMessage("You marked the experience as spam");
                            alert.show();
                        }

                    }else{
                        alert.setTitle("error");
                        alert.setMessage(myObject.getString("error"));
                        alert.show();
                    }
                }catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        public void upvoteExp(String pos){

            int exp_pos = Integer.parseInt(pos);
            position = exp_pos;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobileupvote");


            try {

                JSONObject jsonobj = new JSONObject();
                jsonobj.put("username",session.getUserDetails().get("name"));

                jsonobj.put("IsLoggedIn", session.isLoggedIn());
                jsonobj.put("id",recentExperiences.get(exp_pos).getId());
                jsonobj.put("undo",recentExperiences.get(exp_pos).isUpvotedByUser());

                undo = recentExperiences.get(exp_pos).isUpvotedByUser();

                if(recentExperiences.get(exp_pos).isUpvotedByUser()){
                    recentExperiences.get(exp_pos).setUpvotedByUser(false);
                }else{
                    recentExperiences.get(exp_pos).setUpvotedByUser(true);
                }
                StringEntity se = new StringEntity(jsonobj.toString());
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept", "application/json");

                httpPost.setEntity(se);
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpPost);

                HttpEntity entity = response.getEntity();

                String text = getASCIIContentFromEntity(entity);
                responseText=text;

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        public void downvoteExp(String pos){
            int exp_pos = Integer.parseInt(pos);
            position = exp_pos;

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobiledownvote");


            try {

                JSONObject jsonobj = new JSONObject();
                jsonobj.put("username",session.getUserDetails().get("name"));

                jsonobj.put("IsLoggedIn", session.isLoggedIn());
                jsonobj.put("id",recentExperiences.get(exp_pos).getId());
                jsonobj.put("undo",recentExperiences.get(exp_pos).isDownvotedByUser());
                undo = recentExperiences.get(exp_pos).isDownvotedByUser();

                if(recentExperiences.get(exp_pos).isDownvotedByUser()){
                    recentExperiences.get(exp_pos).setDownvotedByUser(false);
                }else{
                    recentExperiences.get(exp_pos).setDownvotedByUser(true);
                }
                StringEntity se = new StringEntity(jsonobj.toString());
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept", "application/json");

                httpPost.setEntity(se);
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpPost);

                HttpEntity entity = response.getEntity();

                String text = getASCIIContentFromEntity(entity);
                responseText=text;

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        /*
         * This method deletes experience
         */
        public void deleteExp(String pos){

            int exp_pos = Integer.parseInt(pos);
            position = exp_pos;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobiledelete");


            try {

                JSONObject jsonobj = new JSONObject();
                jsonobj.put("username",session.getUserDetails().get("name"));

                jsonobj.put("IsLoggedIn", session.isLoggedIn());
                JSONObject innerJson = new JSONObject();

                innerJson.put("id",recentExperiences.get(exp_pos).getId());

                jsonobj.put("params", innerJson);


                StringEntity se = new StringEntity(jsonobj.toString());
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept", "application/json");

                httpPost.setEntity(se);
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpPost);

                HttpEntity entity = response.getEntity();

                String text = getASCIIContentFromEntity(entity);
                responseText=text;

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void spamExp(String pos){

            int exp_pos = Integer.parseInt(pos);
            position = exp_pos;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost;
            if(recentExperiences.get(exp_pos).isUserMarkedSpam()){
                httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobileunmarkSpam");

            }else{
                httpPost = new HttpPost("http://titan.cmpe.boun.edu.tr:8086/UrbSource/mobile/mobilemarkSpam");

            }
            undo = recentExperiences.get(exp_pos).isUserMarkedSpam();
            if(undo){
                recentExperiences.get(exp_pos).setUserMarkedSpam(false);
            }else{
                recentExperiences.get(exp_pos).setUserMarkedSpam(true);
            }
            try {

                JSONObject jsonobj = new JSONObject();
                jsonobj.put("username",session.getUserDetails().get("name"));

                jsonobj.put("IsLoggedIn", session.isLoggedIn());
                jsonobj.put("id",recentExperiences.get(exp_pos).getId());



                StringEntity se = new StringEntity(jsonobj.toString());
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept", "application/json");

                httpPost.setEntity(se);
                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httpPost);

                HttpEntity entity = response.getEntity();

                String text = getASCIIContentFromEntity(entity);
                responseText=text;

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        /*
         * This method gets needed data from the server
         */

        public void getData(){
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://titan.cmpe.boun.edu.tr:8086/UrbSource/experience/recent");

            //HttpPost httpPost = new HttpPost("http://10.0.3.2/UrbSource/experience/mobilerecent"); //for genymotion
            //incase it works when deployed

            try {

                //				JSONObject jsonobj = new JSONObject();
                //				jsonobj.put("username",session.getUserDetails().get("name"));
                //
                //				StringEntity se = new StringEntity(jsonobj.toString());
                //				se.setContentType("application/json;charset=UTF-8");
                //				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                //				httpPost.setHeader("Content-Type", "application/json");
                //				httpPost.setHeader("Accept", "application/json");
                //
                //				httpPost.setEntity(se);
                //					// Execute HTTP Post Request
                //				HttpResponse response = httpclient.execute(httpPost);
                // Execute HTTP Get Request
                HttpResponse response = httpclient.execute(httpGet);

                HttpEntity entity = response.getEntity();

                String text = getASCIIContentFromEntity(entity);
                JSONObject myObject = new JSONObject(text);
                JSONArray jsona = new JSONArray(myObject.getString("experiences"));

                for(int i=0; i<jsona.length();i++){  // teker teker experience e gir.
                    JSONObject jsonObj = jsona.getJSONObject(i);
                    Experience exp = new Experience();
                    User u = new User();
                    exp.setId(jsonObj.getInt("id"));
                    exp.setText(jsonObj.getString("text"));
                    exp.setMood(jsonObj.getString("mood"));
                    exp.setLocation(jsonObj.getString("location"));
                    //exp.setCreationTime(Timestamp.valueOf(jsonObj.getString("creationTime")));
                    //exp.setExpirationDate(Date.valueOf(jsonObj.getString("expirationDate")));
                    //exp.setModificationTime(Timestamp.valueOf(jsonObj.getString("modificationTime")));
                    exp.setSpam(jsonObj.getInt("spam"));
                    exp.setUserMarkedSpam(jsonObj.getBoolean("userMarkedSpam"));
                    exp.setUpvotedByUser(jsonObj.getBoolean("upvotedByUser"));
                    exp.setDownvotedByUser(jsonObj.getBoolean("downvotedByUser"));
                    exp.setNumberOfComments(jsonObj.getInt("numberOfComments"));

                    //USER ��ekme taraf�� ileride ayr�� method yap kolay olsun.
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
                    //TAG ��ekme taraf��
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




            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
