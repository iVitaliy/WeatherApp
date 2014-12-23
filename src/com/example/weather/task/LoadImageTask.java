package com.example.weather.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.net.URL;

/**
 * Loads image icon from given url.
 */
public abstract class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {

    private static final String BASE_URL = "http://openweathermap.org/img/w/";
    
    private String mIcon;

    public LoadImageTask(String icon) {
        mIcon = icon;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL url = new URL(BASE_URL + mIcon + ".png");

            HttpGet httpRequest = new HttpGet(url.toURI());
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httpRequest);

            HttpEntity entity = response.getEntity();
            BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
            InputStream input = bufferedEntity.getContent();

            return BitmapFactory.decodeStream(input);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            onImageLoaded(bitmap);
        }
    }

    protected abstract void onImageLoaded(Bitmap bitmap);
}
