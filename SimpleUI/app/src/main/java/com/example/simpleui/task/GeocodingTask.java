package com.example.simpleui.task;

import android.os.AsyncTask;
import android.util.Log;

import com.example.simpleui.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by ggm on 6/27/15.
 */
public class GeocodingTask extends AsyncTask<String, Integer, String> {

    private GeocodingTask.callback callback;

    public GeocodingTask(GeocodingTask.callback callback) {
        this.callback = callback;
    }

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

            callback.done(lat, lng);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public interface callback {
        void done(double lat, double lng);
    }
};
