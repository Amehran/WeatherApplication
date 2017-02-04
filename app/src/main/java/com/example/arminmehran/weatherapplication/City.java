
/**
 * City.java
 *
 * Created by Armin Mehran on 2015-03-28.
 * Copyright (c) 2015 cs2680. All rights reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of Armin Mehran
 * and Frank Pagliuso.The intellectual and technical concepts contained herein are proprietary
 * to Armin Mehran and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material is strictly forbidden
 * unless prior written permission is obtained from Armin Mehran and Frank Pagliuso."
 *
 */

package com.example.arminmehran.weatherapplication;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class City {
    public final JSONObject json;
    public final boolean isEmpty;
    public boolean noNetwork = false;

    public int id;
    public String country;
    public String name;

    public double longitude;
    public double latitude;
    public Double[] tempArray = new Double[8];
    public String[] maxTempArrayForecast = new String[5];
    public String[] minTempArrayForecast = new String[5];

    public String[] pressureArrayForecast = new String[5];
    public String[] humidityArrayForecast = new String[5];
    public String[] windArrayForecast = new String[5];

    public String[] rainArrayForecast = new String[5];
    public String[] snowArrayForecast = new String[5];
    public String[] iconArrayForecast = new String[5];
    public String[] descriptionArrayForecast = new String[5];



    public Double[] maxTempArrayDailyForecast = new Double[5];
    public Double[] minTempArrayDailyForecast = new Double[5];
    public String[] mainForecast = new String[5];
    public String[] dateForecast = new String[5];
    public String[] weekDayForecast = new String[5];
    public String[] iconForecast = new String[5];

    public Double[] snowArray = new Double[8];
    public Double[] rainArray = new Double[8];


    List<String> weatherInfo = new ArrayList<String>();

    //   public final List<Time> times;

    public String CurrentTemp;
    public String maxTemp;
    public String minTemp;
    public String pressure;
    public String humidity;
    public String wind;
    public String windDeg;
    public String snow;
    public String rain;
    public String description;
    public String icon;
    public long dt;

    public City(JSONObject json) {
        this.json = json != null ? json : new JSONObject();

        id = this.json.optInt("id");
        name = this.json.optString("name", "").trim();

        isEmpty = this.json.length() == 0 || id < 1;
    }
}
