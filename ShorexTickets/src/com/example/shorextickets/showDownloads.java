package com.example.shorextickets;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.view.*;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.app.ProgressDialog;


public class showDownloads extends Activity{
	private String strParsedValue = null;
    private List<String> excursions = new ArrayList<String>();
    private List<String> status = new ArrayList<String>();
    private ListView ticketslist; 
    private Activity thisActivity = this;
    private String downId, downName;
    int pos,firstOpen;
    private List l1,l2;

    TextView txtViewParsedValue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_downloads);
        ticketslist = (ListView)findViewById(R.id.listViewDownload);

        Intent intent = getIntent();
        String JsonResponse = intent.getStringExtra("response");
        try{ 
            parseJSON(JsonResponse);
        }
        catch (JSONException e) {
                    // TODO Auto-generated catch block
            e.printStackTrace();
        }
         l1 = excursions;
         l2 = status;
         showTickets(pos);
         for(int i=0; i < l1.size() ; i++ ){
        	 downId = (String) l2.get(i);
             downName= (String) l1.get(i);
             if (!downId.equals("Pending")){
	             int c = downId.indexOf(":");
	             int b = downId.indexOf("-");
	             String order = downId.substring(c+1, b);
	             String ticket = downId.substring(b+1);
	             String title = order + "_" + ticket + "_" + downName + ".jpg" ;
	             if (fileExists(i, title)){
	                 showTickets(i);
	             }
             }
         }
        
        
            //code for clickable lv
        ticketslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {
            // When clicked, show a toast with the TextView text
            	 downId = (String) l2.get(position);
                 downName= (String) l1.get(position);
                
                if (downId.equals("Pending")){
                    Toast.makeText(getApplicationContext(), "This ticket is not available.", Toast.LENGTH_LONG).show();
                }
                else{

                    int c = downId.indexOf(":");
                    int b = downId.indexOf("-");
                    String order = downId.substring(c+1, b);
                    String ticket = downId.substring(b+1);
                    String title = order + "_" + ticket + "_" + downName + ".jpg" ;
                    
                    //Verify if file exists
                    if (fileExists(position, title)){
                                showTickets(position);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Downloading Started", Toast.LENGTH_LONG).show();
                        String url = "http://www.shoreexcursioneer.com/?tt" + order + "=" + ticket;
                        showTickets(position);
                        downloadItem(url ,title);
                    }
                }
            }
        });



Button backMain = (Button) findViewById(R.id.buttonMainBack);
backMain.setOnClickListener(new View.OnClickListener() {
    public void onClick(View v) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
    }
});
}

public boolean fileExists(int position, String title){
	String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "SEE_Tickets";
    File dirF = new File(path);
    
    File[] files = dirF.listFiles();
    int sFiles = files.length;
    if (sFiles > 0){
        for (File inFile : files){
            String name = inFile.getName();
            if (name.equals(title)){
                return true;
            }
        }
    }
    return false;
}

public void parseJSON(String JsonResponse) throws JSONException {

    JSONObject jsonObject = new JSONObject(JsonResponse);
    String resp = jsonObject.getString("Response");

    if (resp.equals("OK")){
      JSONObject excObject = jsonObject.getJSONObject("Excursions");
      Iterator itr = excObject.keys();
      while(itr.hasNext()) {
          String tempName =  itr.next().toString();
          excursions.add(tempName);
      }

      int c = excObject.length();
      for (int i=0; i<c; i++){
          String temp = excObject.getString(excursions.get(i));
          status.add(temp);
      }

  }
  else {

  }
}


public void showTickets(int pos){
    
    if(firstOpen == 0){
    	firstOpen++;
    }
    else{
    	l1.remove(pos);
    	l2.remove(pos);
    }
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map = new HashMap<String, String>();
    Log.v("SDST", "List: " + l1);
    int MAX = l1.size();
    if(l1.size() < l2.size()){
        MAX = l2.size();
    }
    for(int i = 0; i < MAX ; i++ ){

        map = new HashMap<String, String>();
        String exc = i >= l1.size() ? "" : (String)l1.get(i);
        String stat = i >= l2.size() ? "" : (String)l2.get(i);
        if (!stat.equals("Pending")){
            stat = "Ready";
        }
        map.put("excursion",exc);
        map.put("status",stat);
        mylist.add(map);
    }


                    //SimpleAdapter mTickets = new SimpleAdapter(this, mylist, R.layout.lv_item, new String[] {"exc", "stat"}, new int[] {R.id.excursion, R.id.status}){
    SimpleAdapter mTickets = new SimpleAdapter(this, mylist, R.layout.lv_item, new String[] {"excursion", "status"}, new int[] {R.id.excursion, R.id.status}){

        public boolean isEnabled(int pos){
            return true;
        }
    };
    ticketslist.setAdapter(mTickets);
}

public void downloadItem(String url, String title){

    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
    request.setTitle(title);
                // in order for this if to run, you must use the android 3.2 to compile your app
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }*/
                File dirF = getFilesDir();
                String dir = "SEE_Tickets";
                
                
                request.setDestinationInExternalPublicDir(dir, title );

                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);          
            }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
            }
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