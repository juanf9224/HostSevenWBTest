package com.hostseven.hostsevenwbtest;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText userNumber;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userNumber =(EditText)findViewById(R.id.tvUserNum);
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
        int result = 0;
        if((!userNumber.getText().toString().equals("")) && (!userNumber.getText().toString().equals(null))) {
            Intent i = new Intent(this, Main2Activity.class);
            i.putExtra("userNumber", userNumber.getText().toString());
            result = 1;
            startActivityForResult(i, result);

        }
        if(result < 1){
            Toast.makeText(this, "No puedes dejar el campo vacio", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
