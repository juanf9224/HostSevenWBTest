package com.hostseven.hostsevenwbtest;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    TextView userId, userNum, userName;
    String day;
    ListView list;
    ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> map = new HashMap<String, String>();
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        Bundle bundle = getIntent().getExtras();

        userId =(TextView)findViewById(R.id.userId);
        userNum =(TextView)findViewById(R.id.userNum);
        userName =(TextView)findViewById(R.id.userName);


        /*ArrayList<HashMap<String, String>> mylistDef = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> mapDef = new HashMap<String, String>();

        mapDef.put("Grupo", "Grupo");
        mapDef.put("Ejercicio", "Ejercicio");
        mapDef.put("Series", "Series");
        mapDef.put("Repeticiones", "Repeticiones");
        mylistDef.add(map);


        list = (ListView)findViewById(R.id.listView2);
        SimpleAdapter mAdapter1 = new SimpleAdapter(this, mylistDef, R.layout.row, new String[] {"Grupo", "Ejercicio", "Series", "Repeticiones"}, new int[] {R.id.routines_group, R.id.routines_exercises, R.id.routines_series, R.id.routines_repeat});
        list.setAdapter(mAdapter1);*/

        userId.setText(bundle.getString("userId"));
        userNum.setText(bundle.getString("userNum"));
        userName.setText(bundle.getString("userName"));
        day = bundle.getString("selectedDay");

        String url = "http://hostseven.lq3.net:8091/VeoCRM/webservice/call_webservice.asp?VEOCIACRC=6764O1240&USERNAME=uuuu&PASSWORD=pppp&WC=DW2.2ARED.CALL&CALL=GYMPOWER&WS=GETRUTINA&PAR01="+userId.getText().toString()+"&PAR02="+day;


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



    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid. "+ e.toString();
            }
        }

        @Override
        protected void onPreExecute(){


            progress = ProgressDialog.show(Main3Activity.this, "Cargando datos",
                    "por favor espere", true);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Log.d("***************", "***********************************************************************"+ result);
                JSONObject obj = new JSONObject(result);
                JSONArray jArr = obj.getJSONArray("responseObject");
                JSONObject jsonData;

                for (int i = 0; i < jArr.length(); i++) {
                    jsonData = obj.getJSONArray("responseObject").getJSONObject(i);
                    map = new HashMap<String, String>();
                    map.put("group", jsonData.getString("Grupo"));
                    map.put("exercise", jsonData.getString("DescripcionMaquina"));
                    map.put("series", jsonData.getString("Series"));
                    map.put("repeat", jsonData.getString("Repeticiones"));
                    mylist.add(map);

                }

                list = (ListView) findViewById(R.id.listView);
                SimpleAdapter mAdapter = new SimpleAdapter(Main3Activity.this, mylist, R.layout.row, new String[] {"group", "exercise", "series", "repeat"}, new int[] {R.id.routines_group, R.id.routines_exercises, R.id.routines_series, R.id.routines_repeat});
                list.setAdapter(mAdapter);

            } catch (JSONException jse) {
                jse.printStackTrace();
            }

            progress.dismiss();
        }

    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 2000;

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
        getMenuInflater().inflate(R.menu.menu_main3, menu);
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


    public void backButton(View v) {
        super.onBackPressed(); // or super.finish();
    }
}
