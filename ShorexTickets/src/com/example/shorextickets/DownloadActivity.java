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
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DownloadActivity extends Activity{
    private EditText etEmail;
    private EditText etOrder;
    private TextView eWarning;
    private TextView wWarning;
    private String JsonResponse, stEmail, stOrder;
    private String strParsedValue = null;
    private TextView txtViewParsedValue;
    static DownloadTextURLTask avisotask;
    private Activity thisActivity =this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //set layout and layout vars
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logscreen);
        final TextView eWarning = (TextView) findViewById(R.id.emptyWarning);
        final TextView wWarning = (TextView) findViewById(R.id.wrongWarning);
        etEmail= (EditText) findViewById(R.id.email);
        etOrder= (EditText) findViewById(R.id.orderCode);
        
        //temp, to set the search data
        etEmail.setText("nahummartinez.tts@gmail.com");
        etOrder.setText("217691");
        //setting the email account registered in the device to the search field
        AccountManager accountManager =AccountManager.get(this);
        Account account = getAccount(accountManager);
        if (!(account == null)){
            etEmail.setText(account.name);  
        }

        //button to execute the connection and start the next activity if response satisfactory
        Button searchButton = (Button) findViewById(R.id.buttonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eWarning.setVisibility(View.GONE);
                wWarning.setVisibility(View.GONE);
                stEmail = etEmail.getText().toString();
                stOrder = etOrder.getText().toString();
                if (stEmail.equals("") || stOrder.equals("")){
                    eWarning.setVisibility(View.VISIBLE);
                }else{
                    avisotask = new DownloadTextURLTask();
                    avisotask.execute(new String[]{});
                } 
            }
        });
    };

    private class DownloadTextURLTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            String response= "";
            try{
                url=new URL("http://www.shoreexcursioneer.com/tickets/index.php");
                String param="email=" + URLEncoder.encode(etEmail.getText().toString(),"UTF-8")+"&order="+URLEncoder.encode(etOrder.getText().toString(),"UTF-8");
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
                Toast.makeText(DownloadActivity.this, ex.toString(), Toast.LENGTH_SHORT ).show();
            }
            catch(IOException ex){
                Toast.makeText(DownloadActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
            }
            JsonResponse=response;
            return response; 
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //if there is not an order, displays an error mesagge.
            String error ="{\"Response\":\"Email Address or Order Number not valid\"}";
            if (JsonResponse.equals(error)){
                final TextView wWarning = (TextView) findViewById(R.id.wrongWarning);
                wWarning.setVisibility(View.VISIBLE);
                        //Toast.makeText(DownloadActivity.this, "No results for this E-mail and order combination.", Toast.LENGTH_LONG).show(); 
            }
            //otherwise, starts the showDownloads activity, and passes the search params.
            else{
                Intent myIntent = new Intent(getApplicationContext(), showDownloads.class);
                myIntent.putExtra("email", stEmail);
                myIntent.putExtra("orderTicket", stOrder);                  
                startActivityForResult(myIntent, 0);
            }
        }
    }

    private static Account getAccount(AccountManager accountManager){
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0){
            account = accounts[0];
        }
        else{
            account= null;
        }
        return account;
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