package com.example.weather;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.weather.entity.WeatherData;
import com.example.weather.task.LoadDataTask;
import com.example.weather.task.LoadImageTask;

import java.text.DecimalFormat;

/**
 * Main application activity.
 */
public class WeatherAppActivity extends Activity {

    /**
     * Loads weather data from remote service.
     */
    private LoadDataTask mLoadDataTask;

    /**
     * Loads weather condition icon.
     */
    private LoadImageTask mLoadImageTask;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        loadWeatherData();
    }

    public void refreshClicked(View view) {
        loadWeatherData();
    }
    
    private void loadWeatherData() {
        if (mLoadDataTask != null) {
            mLoadDataTask.cancel(true);
        }
        mLoadDataTask = new LoadDataTask() {
            @Override
            public void onSuccess(WeatherData result) {
                displayResult(result);
            }

            @Override
            public void onException(Exception e) {
                showErrorNotification(e);
            }
        };
        mLoadDataTask.execute();
    }

    private void showErrorNotification(Exception e) {
        Toast.makeText(this, "data loading exception: " + e, Toast.LENGTH_LONG).show();
    }

    private void displayResult(WeatherData result) {
        TextView temperatureTextView = (TextView) findViewById(R.id.temperature);

        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        temperatureTextView.setText(decimalFormat.format(result.getTemperature()) + "\u2103");
        displayWeatherIcon(result.getWeatherIcon());
    }

    private void displayWeatherIcon(String weatherIcon) {
        final ImageView weatherIconImage = (ImageView) findViewById(R.id.weather_icon);
        if (mLoadImageTask != null) {
            mLoadImageTask.cancel(true);
        }
        mLoadImageTask = new LoadImageTask(weatherIcon) {

            @Override
            protected void onImageLoaded(Bitmap bitmap) {
                weatherIconImage.setImageBitmap(bitmap);
            }
        };
        mLoadImageTask.execute();
    }
}
