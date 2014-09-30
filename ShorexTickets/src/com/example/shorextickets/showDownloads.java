package com.example.shorextickets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.view.*;
import android.app.Activity;
import android.content.Intent;
import android.app.ProgressDialog;





public class showDownloads extends Activity{
	private String JsonResponse;
	String strParsedValue = null;
	List<String> excursions = new ArrayList<String>();
	List<String> status = new ArrayList<String>();
	private ListView ticketslist; 
	Activity thisActivity = this;
	
	
	TextView txtViewParsedValue;
	@Override
	    public void onCreate(Bundle savedInstanceState) {
			ticketslist = (ListView)findViewById(R.id.listViewDownload);
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.show_downloads);
	        Log.v("SD", "Show Downloads "  );
	        Intent intent = getIntent();
	        String JsonResponse = intent.getStringExtra("response");
	        Log.v("SD", "json response " + JsonResponse);
	        ((TextView)findViewById(R.id.textDownload)).setText(JsonResponse);
	        try{ 
	        	parseJSON(JsonResponse);
	        	}
	        catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
	        
	        showTickets();
	        
	        
	        Button backMain = (Button) findViewById(R.id.buttonMainBack);
	        backMain.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
	    			startActivityForResult(myIntent, 0);
	            }
	        });
	    }
	
	
	
		public void parseJSON(String JsonResponse) throws JSONException {
			Log.v("SD", "JsonResponse: " + JsonResponse );
			JSONObject jsonObject = new JSONObject(JsonResponse);
   
          //JSONObject object = jsonObject.getJSONObject("Response");
          String resp = jsonObject.getString("Response");
          Log.v("SD", "JsonResponse: " + resp );
   
          if (resp.equals("OK")){
        	  JSONObject excObject = jsonObject.getJSONObject("Excursions");
        	  Iterator itr = excObject.keys();
        	  while(itr.hasNext()) {
        		  String tempName =  itr.next().toString();
        	
        	      excursions.add(tempName);
        	  }
        	  Log.v("SD", "JsonResponse: " + excursions );
        	  int c = excObject.length();
        	  Log.v("SD", "Contador: " + c);
        	  for (int i=0; i<c; i++){
        		  Log.v("SD", "Contador for: " + c);
        		  Log.v("SD", "For: " + excursions.get(i));
        		  String temp = excObject.getString(excursions.get(i));
        		  Log.v("SD", "temp " + temp);
        		  status.add(temp);
        	  }
        	  Log.v("SD", "JsonResponse: " + status );
          }
          else {
        	  Log.v("SD", "Else");
          }
          
		}
		
		
		 public void showTickets(){
					List l1 = excursions;
					List l2 = status;
					Log.v("SDST", "Lista 1: " + l1);
					Log.v("SDST", "Lista 2: " + l2);
					
					ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
				    HashMap<String, String> map = new HashMap<String, String>();
				    int MAX = l1.size();
				    if(l1.size() < l2.size()){
				    	MAX = l2.size();
				    }
				    for(int i = 0; i < MAX ; i++ ){
				    	map = new HashMap<String, String>();
				    	String exc = i >= l1.size() ? "" : (String)l1.get(i);
				    	String stat = i >= l2.size() ? "" : (String)l2.get(i);
				    	map.put("exc",exc);
				    	map.put("stat",stat);
				    	mylist.add(map);
				    }
				    Log.v("SDST", "Mapa: " + map);
				    Log.v("SDST", "Mapa: " + mylist);

				    //SimpleAdapter mTickets = new SimpleAdapter(this, mylist, R.layout.lv_item, new String[] {"exc", "stat"}, new int[] {R.id.excursion, R.id.status}){
				    SimpleAdapter mTickets = new SimpleAdapter(this, mylist, R.layout.lv_item, new String[] {"exc", "stat"}, new int[] {R.id.excursion, R.id.status}){
				    	
				    	
				    };
				    ticketslist.setAdapter(mTickets);
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