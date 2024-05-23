package com.adobe.aem.guides.wknd.core.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.adobe.aem.guides.wknd.core.service.WeatherData;


@Component(service = WeatherService.class)
public class WeatherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherService.class);
    private static final String API_KEY = "ca4163fb1a789bc84b7a5551671317dc"; // Replace with your API key
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
 
    public WeatherData getWeatherData(String city) {
        String description = "Sunny";
        double temperature = 25.0;
        int humidity = 50;
        try {
            URL url = new URL(String.format(API_URL, city, API_KEY));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            String response = IOUtils.toString(inputStream, "UTF-8");

            JSONObject jsonObject = new JSONObject(response);

            description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            temperature = jsonObject.getJSONObject("main").getDouble("temp") - 273.15; // Convert Kelvin to Celsius
            humidity = jsonObject.getJSONObject("main").getInt("humidity");

            LOGGER.info("Weather Data Received: Description={}, Temperature={}, Humidity={}", description, temperature, humidity);
        } catch (IOException | JSONException e) {
            LOGGER.error("Error fetching weather data: {}", e.getMessage());
        }

        return new WeatherData(city, description, temperature, humidity);
    }
}