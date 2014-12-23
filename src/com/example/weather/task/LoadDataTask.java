package com.example.weather.task;

import android.os.AsyncTask;
import android.util.Log;
import com.example.weather.entity.WeatherData;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Loads weather data from external service.
 */
public abstract class LoadDataTask extends AsyncTask<Void, Void, WeatherData> {

    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?q=Sumy&units=metric";

    /**
     * Contains exception if it was on data loading.
     */
    private Exception exception;

    @Override
    protected WeatherData doInBackground(Void... params) {
        WeatherData data = new WeatherData();
        //make http call
        HttpClient httpClient = new DefaultHttpClient();
        try {
            HttpResponse response = httpClient.execute(new HttpGet(URL));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                Log.d("Response", builder.toString());
                JSONObject resp = new JSONObject(builder.toString());
                data = WeatherData.from(resp);
            } else {
                exception = new HttpResponseException(statusLine.getStatusCode(), "data can't be delivered!");
            }
        } catch (IOException e) {
            exception = e;
        } catch (JSONException e) {
            exception = e;
        }
        
        return data;
    }

    @Override
    protected void onPostExecute(WeatherData result) {
        super.onPostExecute(result);
        if (exception != null) {
            onException(exception);            
        } else {
            onSuccess(result);
        }
    }
    
    public abstract void onSuccess(WeatherData result);
    
    public abstract void onException(Exception e);
}
