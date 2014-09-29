package com.example.shorextickets;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.view.*;
import android.app.Activity;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CopyOfDownloadActivity extends Activity{
	EditText etEmail;
	EditText etOrder;   
	private String JsonResponse;
	String strParsedValue = null;
	TextView txtViewParsedValue;
	//static DownloadTextURLTask avisotask;
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.logscreen);
	        
	        
	        
	        
	        Button searchButton = (Button) findViewById(R.id.buttonSearch);
	        searchButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	      	//  avisotask = new DownloadTextURLTask();
	      	//  avisotask.execute(new Object[]{});
	      	
	      	  
	      	  Log.v("FC", "jSONResponse: " + JsonResponse );
   	      	  Intent myIntent = new Intent(getApplicationContext(), showDownloads.class);
   	      	  myIntent.putExtra("response", "JsonResponse");
   	      	  startActivityForResult(myIntent, 0);
	          }});
	       
	            	
	    			
	        };
	        
	      /*  private class DownloadTextURLTask extends AsyncTask<String,String,String>{
	        	
	        	protected String doInBackground(Object... arg0) {
	        		
            	URL url;
            	HttpURLConnection conn;
           	 	String response= "";

            	try{
            	
            	url=new URL("http://www.shoreexcursioneer.com/tickets/index.php");

            	
            	String param="email=" + URLEncoder.encode("nahummartinez.tts@gmail.com","UTF-8")+
            	"&order="+URLEncoder.encode("217691","UTF-8");

            	conn=(HttpURLConnection)url.openConnection();
                conn.setDoOutput(true);
            	conn.setRequestMethod("POST");
            	conn.setFixedLengthStreamingMode(param.getBytes().length);
            	conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            	Log.v("FC", "HTTP Header: " + param );
            	PrintWriter out = new PrintWriter(conn.getOutputStream());
            	out.print(param);
            	out.close();

               

            	Scanner inStream = new Scanner(conn.getInputStream());
            
            	while(inStream.hasNextLine()){
	            	response+=(inStream.nextLine());
	            }
            	
            	inStream.close();
	            Log.v("FC", "HTTP Message: " + conn.getResponseMessage() );
	            	
	            
            	}

            	catch(MalformedURLException ex){
            		Toast.makeText(CopyOfDownloadActivity.this, ex.toString(), 1 ).show();
            	}
            	catch(IOException ex){
            		Toast.makeText(CopyOfDownloadActivity.this, ex.toString(), 1 ).show();
            	}
            	Log.v("FC", "Response" + response);
            	JsonResponse=response;
				return response; 
	        	
	        		
	        		
	        	}
	        	protected void onPostExecute(Integer result) {
	        		 super.onPostExecute(result);
	        	
	            }
				@Override
				
					
					return null;
				}

	        }*/
	       
	    		
	       public void parseJSON() throws JSONException {
	        	JSONObject jsonObject = new JSONObject(JsonResponse);
	     
	            JSONObject object = jsonObject.getJSONObject("FirstObject");
	            String attr1 = object.getString("attr1");
	            String attr2 = object.getString("attr2");
	     
	            strParsedValue = "Attribute 1 value => " + attr1;
	            strParsedValue += "\n Attribute 2 value => " + attr2;
	     
	            JSONObject subObject = object.getJSONObject("sub");
	            JSONArray subArray = subObject.getJSONArray("sub1");
	     
	            strParsedValue += "\n Array Length => " + subArray.length();
	     
	            for (int i = 0; i < subArray.length(); i++) {
	                strParsedValue += "\n"
	                        + subArray.getJSONObject(i).getString("sub1_attr")
	                                .toString();
	            }
	     
	            txtViewParsedValue.setText(strParsedValue);
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