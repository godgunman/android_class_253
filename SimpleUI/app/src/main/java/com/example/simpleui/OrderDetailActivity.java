package com.example.simpleui;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class OrderDetailActivity extends ActionBarActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        webView = (WebView) findViewById(R.id.static_map);

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

    AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {
        @Override
        protected String doInBackground(String... params) {
            String address = params[0];

            String out = null;
            try {
                out = Utils.fetch("https://maps.googleapis.com/maps/api/geocode/json?address=" +
                        URLEncoder.encode(address, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("debug", "fetch: " + out);
            return out;
        }

        @Override
        protected void onPostExecute(String jsonString) {
            try {
                JSONObject object = new JSONObject(jsonString);
                JSONObject location = object.getJSONArray("results")
                        .getJSONObject(0)
                        .getJSONObject("geometry")
                        .getJSONObject("location");

                double lat = location.getDouble("lat");
                double lng = location.getDouble("lng");

                String staticMapUrl = String.format(
                        "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=600x600",
                        lat, lng);

                webView.loadUrl(staticMapUrl);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

}
//https://maps.googleapis.com/maps/api/staticmap?center=25.041171,121.565227&zoom=15&size=600x600