package com.example.arminmehran.weatherapplication;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * UpdateIntentService.java
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

public class UpdateIntentService extends IntentService {

    JSONObject currentJson, hourlyJson, dailyJson;
    City cityModel = new City(null);
    private static final String CurrentjsonFilePath = "/CW_service.json";

    ArrayList<String> weatherFilePathArray = new ArrayList<String>();
    ArrayList<String> weatherUrlArray = new ArrayList<String>();
    ArrayList<String> weatherInfoArray = new ArrayList<String>();
    ArrayList<String> wheatherInfo = new ArrayList<String>();

    String s1;
    Boolean cityNameChanged, geoLocationRequested;


    public UpdateIntentService() throws IOException {
        super("UpdateIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Context context = getApplicationContext();
        Log.i("TAG"," Intetnt Service called");


        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.WeatherData), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Log.w("sysApp","Entered Intent Service ");

        cityNameChanged = preferences.getBoolean(context.getString(R.string.cityNameChanged),false);
        geoLocationRequested = preferences.getBoolean(context.getString(R.string.geoLocationRequested),true);
        if (cityNameChanged) Log.w("myApp", "City name is changed ");
        if(geoLocationRequested) Log.w("myApp", "GPS Requested");

        if (cityNameChanged) {

            String L1 = context.getString(R.string.BaseAddressCurrentWeather)
                    + preferences.getString(context.getString(R.string.CityNameKey),context.getString(R.string.CityNameDefault))
                    + context.getString(R.string.API_KEY);

            String filePath1 = context.getFilesDir().getPath().toString() + context.getString(R.string.CurrentjsonFilePath);

            editor.putString(context.getString(R.string.CurrentjsonFilePathKey),filePath1);

            editor.putString(context.getString(R.string.CurrentjsonUrlKey),L1);
            editor.commit();
        }

        else if (geoLocationRequested) {

            String L1 = context.getString(R.string.BaseAddressCurrentWeatherGeo)
                    + preferences.getString(context.getString(R.string.CityCoordinateKey),context.getString(R.string.CityCoordinateDefault))
                    + context.getString(R.string.API_KEY);
            String filePath1 = context.getFilesDir().getPath().toString() + context.getString(R.string.CurrentjsonFilePath);

            editor.putString(context.getString(R.string.CurrentjsonFilePathKey),filePath1);

            editor.putString(context.getString(R.string.CurrentjsonUrlKey),L1);
            editor.commit();
        }

        if (true) {
            weatherFilePathArray.add(preferences.getString(getString(R.string.CurrentjsonFilePathKey),
                    String.valueOf(R.string.DefaultCurrentFilePath)));

            weatherUrlArray.add(preferences.getString(getString(R.string.CurrentjsonUrlKey),
                    context.getString(R.string.DefaultCurrentUrl)));  // "http://api.openweathermap.org/data/2.5/weather?q=toronto&appid=bed56cfa9f670032d2c444f195f4fae7"));
            s1 = JsonFileDownloader(context, weatherUrlArray.get(0));
            weatherInfoArray.add(s1);
            editor.putBoolean(getString(R.string.cityNameChanged), false);
            editor.putString(context.getString(R.string.Currentjson), weatherInfoArray.get(0));
            editor.commit();
        }



        weatherInfoArray.add(preferences.getString(getString(R.string.Currentjson),getString(R.string.CurrentDefaultjsonInfo)));

        try {
            String currentJsonFile  = weatherInfoArray.get(0);
;
            Log.w("myApp","read CurrentJson from IntentService = "+ s1);

            JSONObject currentJson;
            currentJson = currentJsonFile != null ? new JSONObject(currentJsonFile) : new JSONObject( getString(R.string.CurrentDefaultjsonInfo));


            JSONObject sys = currentJson.optJSONObject("sys"); //added
            String code = currentJson.optString("cod");
            JSONObject cityObject = currentJson.optJSONObject("city");
            JSONArray list = currentJson.optJSONArray("list");
            JSONObject main = currentJson.optJSONObject("main"); //added
            JSONObject wind = currentJson.optJSONObject("wind"); //added
            JSONObject coord = currentJson.optJSONObject("coord"); //added
            JSONArray weather = currentJson.optJSONArray("weather");
            JSONObject listObj = weather.optJSONObject(0);

            cityModel = new City(currentJson);
            cityModel.name = currentJson.optString("name", "").trim();

            cityModel.country = (sys != null && sys.length() > 0) ? sys.optString("country", "").trim() : "";

            cityModel.longitude = coord != null ? coord.optDouble("lon") : 0;
            cityModel.latitude = coord != null ? coord.optDouble("lat") : 0;
            cityModel.icon = weather.optJSONObject(0).optString("icon");
            cityModel.dt = Long.parseLong(currentJson.optString("dt")) ;



            cityModel.description = listObj != null ? listObj.getString("description") : "N/A";

            Double d = (double) Math.round((Double.parseDouble(main.getString("temp").toString())));
            cityModel.CurrentTemp = Integer.toString(d.intValue());

            d = (double) Math.round((Double.parseDouble(main.getString("pressure").toString())));
            cityModel.pressure = Integer.toString(d.intValue());
            cityModel.humidity = main.getString("humidity");

            if (wind != null && wind.length() > 0) {
                d = (double) Math.round((Double.parseDouble(wind.optString("speed").toString())));
                cityModel.wind = Integer.toString(d.intValue());
                cityModel.windDeg = wind.optString("deg");
            }
            wheatherInfo.add(context.getString(R.string.CityNameLable)+cityModel.name);
            wheatherInfo.add(context.getString(R.string.CountryNameLable)+cityModel.country);
            wheatherInfo.add(context.getString(R.string.LatitudeLable)+String.valueOf(cityModel.latitude));
            wheatherInfo.add(context.getString(R.string.LongitudeLable)+String.valueOf(cityModel.longitude));
            wheatherInfo.add(cityModel.icon);
            wheatherInfo.add(String.valueOf(cityModel.dt));
            wheatherInfo.add(context.getString(R.string.DescriptionLable)+cityModel.description);
            wheatherInfo.add(context.getString(R.string.TempLable)+cityModel.CurrentTemp);
            wheatherInfo.add(context.getString(R.string.PressureLable)+cityModel.pressure);
            wheatherInfo.add(context.getString(R.string.HumidityLable)+cityModel.humidity);
            wheatherInfo.add(context.getString(R.string.WindSpeedLable)+cityModel.wind);
            wheatherInfo.add(context.getString(R.string.WindLable)+cityModel.windDeg);

            editor.putString("cityName",context.getString(R.string.CityNameLable)+ cityModel.name);
            editor.putString("CountryName", context.getString(R.string.CountryNameLable)+ cityModel.country);
            editor.putString("lat", context.getString(R.string.LatitudeLable)+String.valueOf(cityModel.latitude));
            editor.putString("lon", context.getString(R.string.LongitudeLable)+String.valueOf(cityModel.longitude));
            editor.putString("date", String.valueOf(cityModel.dt));
            editor.putString("description", cityModel.description);
            editor.putString("temp", context.getString(R.string.TempLable)+cityModel.CurrentTemp);
            editor.putString("pressure", context.getString(R.string.PressureLable)+cityModel.pressure);
            editor.putString("humidity", context.getString(R.string.HumidityLable)+cityModel.humidity);
            editor.putString("windspeed", context.getString(R.string.WindSpeedLable)+cityModel.wind);
            editor.putString("winddeg", context.getString(R.string.WindLable)+cityModel.windDeg);
            editor.putString("icon", cityModel.icon);
            editor.commit();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    String JsonFileDownloader(Context context,String Path)  {
        String result = null;
        try {
            result =   Ion.with(context)
                    .load(Path)
                    .asString()
                    .get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.w("myApp", "result = "+result);

        return result;
    }

}
