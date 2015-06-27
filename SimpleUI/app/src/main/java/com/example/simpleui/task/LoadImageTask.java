package com.example.simpleui.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by ggm on 6/27/15.
 */
public class LoadImageTask extends AsyncTask<String, Integer, byte[]> {

    private ProgressDialog progressDialog;
    private ImageView imageView;


    public LoadImageTask(Context context, ImageView imageView) {
        this.progressDialog = new ProgressDialog(context);
        this.imageView = imageView;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPreExecute() {
        progressDialog.setTitle("ImageLoader");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(0);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }

    @Override
    protected byte[] doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len;

            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
                //fake
                onProgressUpdate(progressDialog.getProgress() + 10);
            }

            return baos.toByteArray();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);

        progressDialog.setProgress(100);
        progressDialog.dismiss();
    }

}
