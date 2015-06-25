package com.example.simpleui;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class OrderDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        String address = getIntent().getStringExtra("address");
        asyncTask.execute(address);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order_detail, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    AsyncTask<String, Void, Void> asyncTask = new AsyncTask<String, Void, Void>() {
        @Override
        protected Void doInBackground(String... params) {
            String address = params[0];

            String out = null;
            try {
                out = Utils.fetch("https://maps.googleapis.com/maps/api/geocode/json?address=" +
                        URLEncoder.encode(address, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("debug", "fetch: " + out);
            return null;
        }
    };

}
