package com.hostseven.hostsevenwbtest;

import android.accounts.NetworkErrorException;
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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    TextView userId, userNum, userName;
    URL url = null;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        progress = ProgressDialog.show(this, "Cargando datos",
                "por favor espere", true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        progress.dismiss();
        userId = (TextView)findViewById(R.id.userId);
        userNum = (TextView)findViewById(R.id.userNum);
        userName = (TextView)findViewById(R.id.userName);

        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("userNumber");
        String url = "http://hostseven.lq3.net:8091/VeoCRM/webservice/call_webservice.asp?VEOCIACRC=6764O1240&USERNAME=uuuu&PASSWORD=pppp&WC=DW2.2ARED.CALL&CALL=GYMPOWER&WS=GETMEMBER&PAR01=" + data;

        ConnectivityManager connMgr = (ConnectivityManager)
        getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        try {
            if (networkInfo != null && networkInfo.isConnected()) {

                new DownloadWebPageTask().execute(url);

            } else {
                userName.setText("Verifique su conexion a internet e intentelo de nuevo.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String>{

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


            progress = ProgressDialog.show(Main2Activity.this, "Cargando datos",
                    "por favor espere", true);

        }

        @Override
        protected void onPostExecute(String result) {
            try {

                Log.d("***************", "***********************************************************************"+ result);
                JSONObject obj = new JSONObject(result);
                Log.d("***************", "***********************************************************************"+ result+obj);
                JSONObject response = obj.getJSONArray("responseObject").getJSONObject(0);
                String id = response.getString("ID");
                String num = response.getString("NumeroSocio");
                String name = response.getString("Nombre");
                Log.d("Data from json", "********************************************datos de json: " + id + " " + num + " " + name + "****************************************************");
                if(id.equals("") || num.equals("") || name.equals("")){
                    progress = ProgressDialog.show(Main2Activity.this, "Usuario no existe", "Retornando a la pantalla de busqueda", true);
                    System.exit(0);
                }
                userId.setText(id);
                userNum.setText(num);
                userName.setText(name);
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
        getMenuInflater().inflate(R.menu.menu_main2, menu);
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

    public void getDayOne(View view){
        ArrayList<String> data = new ArrayList<>();
        data.add(userId.getText().toString());
        data.add(userNum.getText().toString());
        data.add(userName.getText().toString());
        data.add("1");

        Bundle extra = new Bundle();
        extra.putSerializable("data", data);

        Intent intent = new Intent(getBaseContext(), Main3Activity.class);
        intent.putExtra("extra", extra);
       // i.putExtra("workoutDay", 1);
        startActivity(intent);

    }

    public void getDayTwo(View view){
        ArrayList<String> data = new ArrayList<>();
        data.add(userId.getText().toString());
        data.add(userNum.getText().toString());
        data.add(userName.getText().toString());
        data.add("2");

        Bundle extra = new Bundle();
        extra.putSerializable("data", data);

        Intent intent = new Intent(getBaseContext(), Main3Activity.class);
        intent.putExtra("extra", extra);
        startActivity(intent);

    }

    public void getDayThree(View view){
        ArrayList<String> data = new ArrayList<>();
        data.add(userId.getText().toString());
        data.add(userNum.getText().toString());
        data.add(userName.getText().toString());
        data.add("3");

        Bundle extra = new Bundle();
        extra.putSerializable("data", data);

        Intent intent = new Intent(getBaseContext(), Main3Activity.class);
        intent.putExtra("extra", extra);
        startActivity(intent);

    }
}
