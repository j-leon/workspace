package com.example.shorextickets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

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
	EditText etEmail;
	EditText etOrder;   
	
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.logscreen);
	        
	        Button searchButton = (Button) findViewById(R.id.buttonSearch);
	        searchButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                etEmail = (EditText) findViewById(R.id.edusuario);
	                etOrder = (EditText) findViewById(R.id.orderCode);

	                String email = etEmail.getText().toString();
	                String order = etOrder.getText().toString();

	                // make sure the fields are not empty
	                if (email.length()>0 && order.length()>0)
	                {
	                	HttpClient httpclient = new DefaultHttpClient();
	                	HttpPost httppost = new HttpPost("http://www.shoreexcursioneer.com/tickets/index.php");
	                	try {
	                		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	                		nameValuePairs.add(new BasicNameValuePair("email", email));
	                		nameValuePairs.add(new BasicNameValuePair("order", order));
	                		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	                		httpclient.execute(httppost);
	                		
	                		/*Intent myIntent = new Intent(getApplicationContext(), showDownloads.class);
	    	    			startActivityForResult(myIntent, 0);*/
	          	   	} catch (ClientProtocolException e) {
	          	   	  Toast.makeText(getBaseContext(), "Caught ClientProtocolException", Toast.LENGTH_SHORT).show();
	          	   	} catch (IOException e) {
	          	   	Toast.makeText(getBaseContext(), "Caught IOException", Toast.LENGTH_SHORT).show();
	          	   	}

	          	   }
	          	   else
	          	   {
	                  	// display message if text fields are empty
	          	   	Toast.makeText(getBaseContext(),"All fields  are required",Toast.LENGTH_SHORT).show();
	          	   }
	            	
	            	
	            
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