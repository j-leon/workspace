package com.example.shorextickets;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.view.*;
import android.app.Activity;
import android.content.Intent;

public class DownloadActivity extends Activity{
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.logscreen);
	        
	        Button searchButton = (Button) findViewById(R.id.buttonSearch);
	        searchButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Intent myIntent = new Intent(getApplicationContext(), showDownloads.class);
	    			startActivityForResult(myIntent, 0);
	            }
	        });
	    }
	    @Override
	    protected void onStart() {
	        super.onStart();
	        // The activity is about to become visible.
	    }
	    @Override
	    protected void onResume() {
	        super.onResume();
	        // The activity has become visible (it is now "resumed").
	    }
	    @Override
	    protected void onPause() {
	        super.onPause();
	        // Another activity is taking focus (this activity is about to be "paused").
	    }
	    @Override
	    protected void onStop() {
	        super.onStop();
	        // The activity is no longer visible (it is now "stopped")
	    }
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        // The activity is about to be destroyed.
	    }
	}