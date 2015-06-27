package com.example.simpleui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simpleui.task.GeocodingTask;
import com.example.simpleui.task.LoadImageTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class OrderDetailActivity extends AppCompatActivity {

    private static final String STATIC_MAP_URL_FORMAT_STRING =
            "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=600x600&markers=%f,%f";

    private WebView mapWebView;
    private ImageView mapImageView;
    private TextView storeAddressTextView;
    private String storeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        mapWebView = (WebView) findViewById(R.id.static_map_web_view);
        mapImageView = (ImageView) findViewById(R.id.static_map_image_view);
        storeAddressTextView = (TextView) findViewById(R.id.store_address);

        storeAddress = getIntent().getStringExtra("address");
        storeAddressTextView.setText(storeAddress);
    }

    //TODO (homework3)
    public void loadWebView(View view) {
        mapWebView.setVisibility(View.VISIBLE);
        mapImageView.setVisibility(View.GONE);
        GeocodingTask geocodingTask = new GeocodingTask(new GeocodingTask.callback() {
            @Override
            public void done(double lat, double lng) {
                String staticMapUrl = String.format(
                        STATIC_MAP_URL_FORMAT_STRING,lat, lng, lat, lng);
                mapWebView.loadUrl(staticMapUrl);
            }
        });

        geocodingTask.execute(storeAddress);
    }

    //TODO (homework3)
    public void loadImageView(View view) {
        mapWebView.setVisibility(View.GONE);
        mapImageView.setVisibility(View.VISIBLE);
        GeocodingTask geocodingTask = new GeocodingTask(new GeocodingTask.callback() {
            @Override
            public void done(double lat, double lng) {
                LoadImageTask loadImageTask = new LoadImageTask(OrderDetailActivity.this, mapImageView);
                String staticMapUrl = String.format(
                        STATIC_MAP_URL_FORMAT_STRING,lat, lng, lat, lng);
                loadImageTask.execute(staticMapUrl);
            }
        });

        geocodingTask.execute(storeAddress);
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

}
//https://maps.googleapis.com/maps/api/staticmap?center=25.041171,121.565227&zoom=15&size=600x600