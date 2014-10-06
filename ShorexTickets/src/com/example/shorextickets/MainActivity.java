package com.example.shorextickets;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import android.view.*;
import android.app.Activity;
import android.content.Intent;




public class MainActivity extends ActionBarActivity {
	ListView ticketslist; 
	Activity thisActivity = this;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ticketslist = (ListView)findViewById(R.id.listView1);
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.d("MyApp", "No SDCARD");
        } else {
        File directory = new File(Environment.getExternalStorageDirectory()+ File.separator + "SEE_Tickets");
        directory.mkdirs();
        }
        showCurrentTickets();
        
        Button myButton = (Button) findViewById(R.id.downButton);
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(getApplicationContext(), DownloadActivity.class);
    			startActivityForResult(myIntent, 0);
            }
        });

    }

    
    public void showCurrentTickets(){
    	List<String> names = new ArrayList<String>();
    	List<String> dirs = new ArrayList<String>();
    	List<String> orderTickets = new ArrayList<String>();
    	String dir,name;
    	String path = Environment.getExternalStorageDirectory().getAbsolutePath() + getFilesDir().getAbsolutePath();
    	File dirF = new File(path);
    	Log.v("MA.SCT", path);    	
    	File[] files = dirF.listFiles();
    	int sFiles = files.length;
    	Log.v("MA.SCT", "Size: " + sFiles);
    	if (sFiles > 0){
    		for (File inFile : files){
    			
    			dir = inFile.getAbsolutePath();
    			name = inFile.getName();
    			String[] data = cleanNames(name);
    			names.add(data[2]);
    			orderTickets.add(data[0] + " - " + data[1]);
    			Log.v("MADA", "orderTickets: " + orderTickets);
    			dirs.add(dir);
    		}
    		Log.v("MA.SCT", names + " , " + dirs);
    		int c = names.size();
    		ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    		HashMap<String, String> map = new HashMap<String, String>();
    		for(int i = 0; i < c ; i++ ){
    			map = new HashMap<String, String>();
    			name = (String) names.get(i) ;
    			dir = (String) orderTickets.get(i);
    			map.put("names",name);
    			map.put("dirs",dir);
    			mylist.add(map);
    		}
    		Log.v("MA.SCT", "Mapa: " + map);
    		Log.v("MA.SCT", "Lista: " + mylist);
    		SimpleAdapter mTickets = new SimpleAdapter(this, mylist, R.layout.lv_item, new String[] {"names", "dirs"}, new int[] {R.id.excursion, R.id.status}){

    			public boolean isEnabled(int pos){
    				return true;
    			}
    		};
    		ticketslist.setAdapter(mTickets);
    	}
    }
    	
    public String[] cleanNames(String name){
    	String[] data = new String[2];
    	int o =name.indexOf("_");
    	int t = name.indexOf("_", o);
    	int n = name.indexOf(".");
    	data[0] = name.substring(0, o);
    	data[1] = name.substring(o+1, t);
    	data[2] = name.substring(t+1, n);
    	Log.v("MADA", "Data: " + data);
    	return data;
    }
    	
    	
    	
    	


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
