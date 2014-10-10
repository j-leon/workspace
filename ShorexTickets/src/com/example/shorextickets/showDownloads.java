package com.example.shorextickets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.support.v7.app.ActionBarActivity;
import android.net.Uri;
import android.os.AsyncTask;
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
    private String downId, downName, inEmail, inOrder;
    private int pos,firstOpen;
    private List l1,l2;
    private Menu refreshMenu;
    private String JsonResponse;
    static DownloadTextURLTask avisotask;
    private int[] delFromView = new int[1];
    TextView txtViewParsedValue;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //layout setting
        setContentView(R.layout.show_downloads);
        ticketslist = (ListView)findViewById(R.id.listViewDownload);

        //getting account data  
        Intent intent = getIntent();
        inEmail = intent.getStringExtra("email");
        inOrder = intent.getStringExtra("orderTicket");
        //initializing lists, to avoid null when resetting
        l1 = excursions;
        l2 = status;
        
        //running the download task, and forming listview onPostExecute
        avisotask = new DownloadTextURLTask();
        avisotask.execute(new String[]{inEmail,inOrder});
              
        //code for clickable lv
        ticketslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {
                //get the data from the listview position
                downId = (String) l2.get(position);
                downName= (String) l1.get(position);
                
                delFromView[0] = position;
                if (downId.equals("Pending")){
                    Toast.makeText(getApplicationContext(), "This ticket is not available.", Toast.LENGTH_LONG).show();
                }
                else{
                    //sets the title of the file to download
                    int c = downId.indexOf(":");
                    int b = downId.indexOf("-");
                    String order = downId.substring(c+1, b);
                    String ticket = downId.substring(b+1);
                    String title = order + "_" + ticket + "_" + downName + ".jpg" ;

                    //Verify if file exists
                    if (fileExists(position, title)){
                        //if it does delete it from the list
                        showTickets(delFromView);
                    }
                    else{
                        //otherwise proceed to download. and delete the item from the list.
                        Toast.makeText(getApplicationContext(), "Downloading Started", Toast.LENGTH_LONG).show();
                        String url = "http://www.shoreexcursioneer.com/?tt" + order + "=" + ticket;
                        showTickets(delFromView);
                        downloadItem(url ,title);
                    }
                }
            }
        });
        
        //return to the main screen and destroy this activity
        Button backMain = (Button) findViewById(R.id.buttonMainBack);
        backMain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                thisActivity.finish();
                Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    //meant to hide already downloaded tickets.
    public void reshowTickets(List l1, List l2){
        int[] delTickets = new int[l1.size()];
        int arrayPos = 0;
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
                    delTickets[arrayPos] = i;
                    arrayPos++;
                }
            }
        }
        showTickets(delTickets);
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
            //get the keys from the json
            while(itr.hasNext()) {
                String tempName =  itr.next().toString();
                excursions.add(tempName);
            }
            //get the values of the json
            int c = excObject.length();
            for (int i=0; i<c; i++){
                String temp = excObject.getString(excursions.get(i));
                status.add(temp);
            }
        }
        else {

        }
        l1 = excursions;
        l2 = status;
    }


    public void showTickets(int[] pos){
        //remove already downloaded tickets
        for(int i = 0; i <= pos.length;i++){ 
            if(pos[i] < 0){
            }
            else{
                l1.remove(pos[i]);
                l2.remove(pos[i]);
            }
        }
        //create the map that will be passed to the simpleAdapter for the listview
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
            if (!stat.equals("Pending")){
                stat = "Ready";
            }
            map.put("excursion",exc);
            map.put("status",stat);
            mylist.add(map);
        }
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
        String dir = "SEE_Tickets";
        request.setDestinationInExternalPublicDir(dir, title );
        // get download service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);          
    }

    //Obtains the Json
    private class DownloadTextURLTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            String response= "";
            String email = params[0];
            String orderTickets = params[1];
            try{
                url=new URL("http://www.shoreexcursioneer.com/tickets/index.php");
                /*String param="email=" + URLEncoder.encode(etEmail.getText().toString(),"UTF-8")+
                "&order="+URLEncoder.encode(etOrder.getText().toString(),"UTF-8");*/
                String param="email=" + URLEncoder.encode(email,"UTF-8")+"&order="+URLEncoder.encode(orderTickets,"UTF-8");
                conn=(HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setFixedLengthStreamingMode(param.getBytes().length);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                PrintWriter out = new PrintWriter(conn.getOutputStream());
                out.print(param);
                out.close();
                Scanner inStream = new Scanner(conn.getInputStream());
                while(inStream.hasNextLine()){
                    response+=(inStream.nextLine());
                }    
                inStream.close();
            }
            catch(MalformedURLException ex){
                Toast.makeText(showDownloads.this, ex.toString(), Toast.LENGTH_SHORT ).show();
            }
            catch(IOException ex){
                Toast.makeText(showDownloads.this, ex.toString(), Toast.LENGTH_SHORT).show();
            }

            JsonResponse=response;
            return response; 
        }
        
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //parses and displays the json.
            try{ 
                if(l1.isEmpty() && l2.isEmpty()){
                    parseJSON(JsonResponse);
                }
                else{
                    l2.clear();
                    l1.clear();
                    parseJSON(JsonResponse);
                }
                
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            delFromView[0] = -1;
            showTickets(delFromView);
            reshowTickets(l1,l2);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.refreshMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            avisotask = new DownloadTextURLTask();
            avisotask.execute(new String[]{inEmail,inOrder});
        }
        return super.onOptionsItemSelected(item);
    }
}