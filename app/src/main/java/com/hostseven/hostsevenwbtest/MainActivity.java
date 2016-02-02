package com.hostseven.hostsevenwbtest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText userNumber;
    ProgressDialog progress;
    String id;
    String num;
    String name;
    int isNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        @Override
        protected void onPreExecute(){


            progress = ProgressDialog.show(MainActivity.this, "Cargando datos",
                    "por favor espere", true);

        }

        @Override
        protected void onPostExecute(String result) {
            try {

                Log.d("***************", "***********************************************************************" + result);
                JSONObject obj = new JSONObject(result);

                if(obj.getString("success") == "true"){
                    Log.d("***************", "***********************************************************************"+ result+obj);
                    JSONObject response = obj.getJSONArray("responseObject").getJSONObject(0);

                    id = response.getString("ID");
                    num = response.getString("NumeroSocio");
                    name = response.getString("Nombre");
                    Log.d("Data from json", "********************************************datos de json: " + id + " " + num + " " + name + "****************************************************");


                    Intent i = new Intent(MainActivity.this, Main2Activity.class);
                    i.putExtra("userId", id);
                    i.putExtra("userNumber", num);
                    i.putExtra("userName", name);
                    startActivityForResult(i, isNumber);
                }else if(isNumber < 1){
                    progress.dismiss();
                    Toast.makeText(MainActivity.this, "No puedes dejar el campo vacio", Toast.LENGTH_SHORT).show();
                    super.cancel(true);
                    return;
                }else if(obj.getString("success") == "false"){
                    progress.dismiss();
                    Toast.makeText(MainActivity.this, "El usuario no existe", Toast.LENGTH_SHORT).show();
                    super.cancel(true);
                }
            }catch(JSONException jse){
                jse.printStackTrace();
            }
            progress.dismiss();
        }

    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.exitApp){
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    public void getUser(View view){
        userNumber =(EditText)findViewById(R.id.tvUserNum);

        String url = "http://hostseven.lq3.net:8091/VeoCRM/webservice/call_webservice.asp?VEOCIACRC=6764O1240&USERNAME=uuuu&PASSWORD=pppp&WC=DW2.2ARED.CALL&CALL=GYMPOWER&WS=GETMEMBER&PAR01=" + userNumber.getText().toString();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        try {
            if (networkInfo != null && networkInfo.isConnected()) {

                new DownloadWebPageTask().execute(url);

            } else {
                Toast.makeText(MainActivity.this, "Verifique su conexion a internet e intentelo de nuevo.", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
