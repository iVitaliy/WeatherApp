package com.example.weather.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Contains weather info.
 */
public class WeatherData {
    
    private static final String MAIN = "main";
    
    private static final String TEMPERATURE = "temp";
    
    private static final String WEATHER = "weather";

    private static final String ICON = "icon";
    
    private double mTemperature;
    
    private String mWeatherIcon;
    
    public WeatherData(double temperature, String weatherIcon) {
        mTemperature = temperature;
        mWeatherIcon = weatherIcon;
    }

    public WeatherData() {

    }

    public static WeatherData from(JSONObject data) throws JSONException {
        double temperature = data.getJSONObject(MAIN).getDouble(TEMPERATURE);
        String weatherIcon = data.getJSONArray(WEATHER).getJSONObject(0).getString(ICON);
        return new WeatherData(temperature, weatherIcon);
    }

    public double getTemperature() {
        return mTemperature;
    }
    
    public String getWeatherIcon() {
        return mWeatherIcon;
    }
}
