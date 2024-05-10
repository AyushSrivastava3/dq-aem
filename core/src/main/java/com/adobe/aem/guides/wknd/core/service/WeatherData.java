package com.adobe.aem.guides.wknd.core.service;


public class WeatherData {
    private String city;
    private String description;
    private double temperature;
    private int humidity;

    // Constructor
    public WeatherData(String city, String description, double temperature, int humidity) {
        this.city = city;
        this.description = description;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    // Getters and setters
    public String getCity() {
        return city;
    }


    public String getDescription() {
        return description;
    }


    public double getTemperature() {
        return temperature;
    }


    public int getHumidity() {
        return humidity;
    }

   
}