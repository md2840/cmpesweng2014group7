package com.group7.urbsourceandroid;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;


public class HomePage extends FragmentActivity implements TabListener {

	 private ViewPager viewPager;
	 private TabsPagerAdapter mAdapter;
	 private ActionBar actionBar;
	   
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.homepage);
	    
	    viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 
        viewPager.setAdapter(mAdapter);
        
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);  
        
        actionBar.addTab(actionBar.newTab().setText("UrbSource").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Create New Experience").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Settings").setTabListener(this));
         /**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
		 
		    @Override
		    public void onPageSelected(int position) {
		        // on changing the page
		        // make respected tab selected
		        actionBar.setSelectedNavigationItem(position);
		    }
		 
		    @Override
		    public void onPageScrolled(int arg0, float arg1, int arg2) {
		    }
		 
		    @Override
		    public void onPageScrollStateChanged(int arg0) {
		    }
		});

	}
	
	
	
		@Override
		public void onResume() {
		    super.onResume();
		   
		}
		
		@Override
		protected void onResumeFragments() {
		    super.onResumeFragments();
				   
		}

		@Override
		public void onPause() {
		    super.onPause();
		   
		}

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
		    super.onActivityResult(requestCode, resultCode, data);
		 }

		@Override
		public void onDestroy() {
		    super.onDestroy();
		  
		}

		@Override
		protected void onSaveInstanceState(Bundle outState) {
		    super.onSaveInstanceState(outState);
		  
		}
		@Override
		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
			
		}
		@Override
		public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
			// on tab selected
	        // show respected fragment view
	        viewPager.setCurrentItem(tab.getPosition());			
		}
		
		@Override
		public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
					
		}
		
	

}
