package com.example.arminmehran.weatherapplication;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * WeatherActivity.java
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

public class WeatherActivity extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    Connectivity connectivity = null;
    WeatherFragment weatherFragment = new WeatherFragment();
    MenuFragment menuFragment = new MenuFragment();
    AboutFragment aboutFragment = new AboutFragment();
    City cityModel;
    ArrayList<String> wheatherInfo = new ArrayList<String>();
    ;

    Boolean menuIsShowing = false;

    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSION_ACCESS_NETWORK_STATE = 2;
    private GoogleApiClient googleApiClient;
    LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Log.w("TAG","Entered onCreate");



        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();



        manageWeatherFragment("add");
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
            case PERMISSION_ACCESS_NETWORK_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                            == PackageManager.PERMISSION_GRANTED) {
//                        TODO whatever you want to do check connetivity
                        Connectivity cn  = new Connectivity();
                        if (cn.isConnected(this)){
                         } else {
                         }
                    }
                } else {
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


//        TODO : create menu

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!menuFragment.isVisible()){
            manageMenuFragment("add");
            menuIsShowing = true;
        }
        else if (weatherFragment.isVisible()){

            MenuFragment mf = (MenuFragment) getFragmentManager().findFragmentByTag(MenuFragment.MenuFragmentName);
            mf.onExitMenuStatusSave();

            CheckRuntimePermissionAndGetGeoLocation();

            updateWeather();

            manageMenuFragment("remove");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    displayFragmentData();

                }
            }, 700);



            Log.w("myApp","back from menu");
//            getFragmentManager().beginTransaction().replace(R.id.container,weatherFragment).commit();
            menuIsShowing = false;
        }
        return super.onOptionsItemSelected(item);

//   TODO      Manage menu options, openningand closing and updates and saves
    }

    @Override
    public void onBackPressed() {
        if (aboutFragment.isVisible()) {
            manageAboutFragment("remove");
        }
        else if (menuFragment.isVisible()){

        }
        else{
            Log.w("myAPP","exit?");
            menuIsShowing = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("TAG"," onResume called");

        updateWeather();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("TAG"," onPause called");


    }



    void manageWeatherFragment(String mode){

        switch (mode) {
            case "add": getFragmentManager().beginTransaction().add(R.id.container,weatherFragment,WeatherFragment.WeatherFragmentName).addToBackStack(WeatherFragment.WeatherFragmentName).commit();
                break;
            case "remove":getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(WeatherFragment.WeatherFragmentName)).commit();//                    .setCustomAnimations(R.animator.anim_in, R.animator.anim_out)
                break;
            case "replace": getFragmentManager().beginTransaction().replace(R.id.container,weatherFragment).commit();
                break;
            default: break;
        }

    }

    void manageMenuFragment(String mode){
        switch (mode) {
            case "add": getFragmentManager().beginTransaction().add(R.id.container,menuFragment,MenuFragment.MenuFragmentName).addToBackStack(MenuFragment.MenuFragmentName).commit();
                break;
            case "remove":getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(MenuFragment.MenuFragmentName)).commit();//                    .setCustomAnimations(R.animator.anim_in, R.animator.anim_out)
                break;
            case "replace": getFragmentManager().beginTransaction().replace(R.id.container,menuFragment).commit();
                break;
            default: break;
        }

//        if (mode == "add") {
//            getFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.container, menuFragment, MenuFragment.MenuFragmentName)
//                    .commit();
//
//        } else if (mode == "remove"){
//            final MenuFragment mf = (MenuFragment) getFragmentManager()
//                    .findFragmentByTag(MenuFragment.MenuFragmentName);
//            mf.onExitMenuStatusSave();
//            runWeatherTask();
//            getFragmentManager().beginTransaction()
////                    .setCustomAnimations(R.animator.anim_in, R.animator.anim_out)
//                    .remove(getFragmentManager()
//                            .findFragmentByTag(MenuFragment.MenuFragmentName))
//                    .commit();
//        }
    }

    void manageAboutFragment(String mode){
        switch (mode) {
            case "add": getFragmentManager().beginTransaction().add(R.id.container,aboutFragment,AboutFragment.AboutFragmentName).addToBackStack(AboutFragment.AboutFragmentName).commit();
                break;
            case "remove":getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag(AboutFragment.AboutFragmentName)).commit();//                    .setCustomAnimations(R.animator.anim_in, R.animator.anim_out)
                break;
            case "replace": getFragmentManager().beginTransaction().replace(R.id.container,aboutFragment).commit();
                break;
            default: break;
        }


    }

    public void setWidgetDataUpdate(Context context) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName appWidgetComponentName = new ComponentName(context, MyWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(appWidgetComponentName);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.WeatherData), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Date dt = new Date();
        dt.getTime();

        if(appWidgetIds.length>0) {
            for (int index = 0; index < appWidgetIds.length; index++) {
                String timeStamp = new SimpleDateFormat("yyyy/MM/dd @ HH:mm").format(Calendar.getInstance().getTime());
                int appWidgetId = appWidgetIds[index];
                RemoteViews appWidgetViews = MyWidgetProvider.getWidgetRemoteViews(context);



                appWidgetViews.setCharSequence(R.id.city_field1, "setText", preferences.getString("cityName", getString(R.string.CityNameDefault)) + "," + preferences.getString("CountryName", getString(R.string.CountryNameDefault)));
                appWidgetViews.setCharSequence(R.id.lat_field, "setText", preferences.getString("lat", getString(R.string.LatitudeLable)));
                appWidgetViews.setCharSequence(R.id.lon_field, "setText", preferences.getString("lon", getString(R.string.LongitudeLable)));
                appWidgetViews.setCharSequence(R.id.date_field, "setText", "Date:" + preferences.getString("date", ""));
                appWidgetViews.setCharSequence(R.id.descript_field, "setText", "Description" + preferences.getString("description", ""));
                appWidgetViews.setCharSequence(R.id.current_temperature_field, "setText", preferences.getString("temp", ""));
                appWidgetViews.setCharSequence(R.id.pressure_field, "setText", preferences.getString("pressure", ""));
                appWidgetViews.setCharSequence(R.id.humidity_field, "setText", preferences.getString("humidity", ""));
                appWidgetViews.setCharSequence(R.id.windSpeed_field, "setText", preferences.getString("windspeed", ""));
                appWidgetViews.setCharSequence(R.id.winddeg_feild, "setText", preferences.getString("winddeg", ""));


                appWidgetManager.updateAppWidget(appWidgetId, appWidgetViews);
            }
        }
    }

    void  displayFragmentData(){
        final WeatherFragment wf = (WeatherFragment) getFragmentManager().findFragmentByTag(WeatherFragment.WeatherFragmentName);
        wf.displayList();
    }



    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    void runUpdateIntentService(String link) {
        Intent service = new Intent(this, UpdateIntentService.class);
        service.putExtra("link", link);
        this.startService(service);
    }




    ArrayList<String> parsWeatherDataInformation(){
        new Thread(new Runnable() {


            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences(getString(R.string.WeatherData), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                try{
                    wheatherInfo.clear();
//                    final String CurrentjsonFilePath = "/CW_service.json";
//                    final String HourlyJsonFilePath = "/HW_service.json";
//                    final String DailyJsonFilePath = "/DW_service.json";
//
//                    String filePath1 = getFilesDir().getPath().toString() + CurrentjsonFilePath;
//                    String filePath2 = getFilesDir().getPath().toString() + HourlyJsonFilePath;
//                    String filePath3 = getFilesDir().getPath().toString() + DailyJsonFilePath;

                    String currentJsonFile  = preferences.getString(getString(R.string.Currentjson),getString(R.string.CurrentDefaultjsonInfo)); //readFileFromLocalDisc("",filePath1);
                    String dailyJsonFile  = preferences.getString(getString(R.string.HourlyJson),getString(R.string.HourlyDefaultjsonInfo));//readFileFromLocalDisc("",filePath2);
                    String hourlyJsonFile  = preferences.getString(getString(R.string.DailyJson),getString(R.string.DailyDefaultjsonInfo));//readFileFromLocalDisc("",filePath3);

                    currentJsonFile = preferences.getString(getString(R.string.Currentjson),
                            getString(R.string.CurrentDefaultjsonInfo));
                    dailyJsonFile = preferences.getString(getString(R.string.DailyJson),
                            getString(R.string.DailyDefaultjsonInfo));
                    hourlyJsonFile = preferences.getString(getString(R.string.HourlyJson),
                            getString(R.string.HourlyDefaultjsonInfo));



                    Context context = getApplicationContext();

                    JSONObject currentJson = new JSONObject(currentJsonFile);
                    JSONObject dailyJson = new JSONObject(dailyJsonFile);
                    JSONObject hourlyJson = new JSONObject(hourlyJsonFile);

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
                    Log.i("rr",cityModel.name);


                    cityModel.country = (sys != null && sys.length() > 0) ? sys.optString("country", "").trim() : "";
                    //   cityModel.id = currentJson.optInt("id");
                    cityModel.longitude = coord != null ? coord.optDouble("lon") : 0;

                    cityModel.latitude = coord != null ? coord.optDouble("lat") : 0;
                    cityModel.icon = weather.optJSONObject(0).optString("icon");
                    cityModel.dt = Long.parseLong(currentJson.optString("dt")) ;
                    //Date a = new Date(seconds * 1000L);


                    cityModel.description = listObj != null ? listObj.getString("description") : "N/A";

                    Double d = (double) Math.round((Double.parseDouble(main.getString("temp").toString())));
                    cityModel.CurrentTemp = Integer.toString(d.intValue());
//            Toast.makeText(this, "curent temp for" + cityModel.name + ":" + cityModel.CurrentTemp, Toast.LENGTH_LONG).show();
                    d = (double) Math.round((Double.parseDouble(main.getString("pressure").toString())));
                    cityModel.pressure = Integer.toString(d.intValue());
                    cityModel.humidity = main.getString("humidity");
                    Log.w("myApp", "name:" + cityModel.name);
                    Log.w("myApp", "Geo-coordinate Lat:" + cityModel.latitude);
                    Log.w("myApp", "Geo-coordinate Lon:" + cityModel.longitude);
                    Log.w("myApp", "Temp:" + cityModel.CurrentTemp);
                    Log.w("myApp", "Location:" + cityModel.CurrentTemp);
                    if (wind != null && wind.length() > 0) {
                        d = (double) Math.round((Double.parseDouble(wind.optString("speed").toString())));
                        cityModel.wind = Integer.toString(d.intValue());
                        cityModel.windDeg = wind.optString("deg");
                    }
                    Log.w("myApp", "name:" + cityModel.name);
                    Log.w("myApp", "Geo-coordinate Lat:" + cityModel.latitude);
                    Log.w("myApp", "Geo-coordinate Lon:" + cityModel.longitude);
                    Log.w("myApp", "Temp:" + cityModel.CurrentTemp);
                    Log.w("myApp", "Location:" + cityModel.CurrentTemp);
                    wheatherInfo.add(getString(R.string.CityNameLable)+cityModel.name);
                    wheatherInfo.add(getString(R.string.CountryNameLable)+cityModel.country);
                    wheatherInfo.add(getString(R.string.LatitudeLable)+String.valueOf(cityModel.latitude));
                    wheatherInfo.add(getString(R.string.LongitudeLable)+String.valueOf(cityModel.longitude));
                    wheatherInfo.add(cityModel.icon);
                    wheatherInfo.add(String.valueOf(cityModel.dt));
                    wheatherInfo.add(getString(R.string.DescriptionLable)+cityModel.description);
                    wheatherInfo.add(getString(R.string.TempLable)+cityModel.CurrentTemp);
                    wheatherInfo.add(getString(R.string.PressureLable)+cityModel.pressure);
                    wheatherInfo.add(getString(R.string.HumidityLable)+cityModel.humidity);
                    wheatherInfo.add(getString(R.string.WindSpeedLable)+cityModel.wind);
                    wheatherInfo.add(getString(R.string.WindLable)+cityModel.windDeg);

                    List<String> textList = new ArrayList<String>();
                    Set<String> weatherSet = new HashSet<String>(wheatherInfo);

                    editor.putStringSet("key", weatherSet);
                    editor.commit();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        return wheatherInfo;


    }

    String readFileFromLocalDisc(String content, String Path){
        String result = null;
        try{
            StringBuilder text = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(Path));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();


            Log.w("myApp", "reading == "+ text.toString());
            result = text.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public  void updateWeather(){

        SharedPreferences preferences = getSharedPreferences(getString(R.string.WeatherData), Context.MODE_PRIVATE);
        runUpdateIntentService("");
        setWidgetDataUpdate(this);
        manageWeatherFragment("replace");

    }


    public void getLocation(){
         SharedPreferences preferences = getSharedPreferences(getString(R.string.WeatherData), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            if (preferences.getBoolean(getString(R.string.geoLocationRequested),true)) {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                if(lastLocation!= null) {
                    double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();


                    editor.putString(getString(R.string.CityCoordinateKey),
                            "lat=" + String.valueOf(lat) + "&" + "lon=" + String.valueOf(lon)).commit();
                }
            }
        }
    }



    public void CheckRuntimePermissionAndGetGeoLocation(){
        SharedPreferences preferences = getSharedPreferences(getString(R.string.WeatherData), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if (preferences.getBoolean(getString(R.string.geoLocationRequested),true)){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(WeatherActivity.class.getSimpleName(), "Connected to Google Play Services!");
        SharedPreferences preferences = getSharedPreferences(getString(R.string.WeatherData), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

//            if (preferences.getBoolean(getString(R.string.geoLocationRequested),true)) {
            if(true){
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                if(lastLocation!= null) {
                    double lat = lastLocation.getLatitude(), lon = lastLocation.getLongitude();
                    Log.w("myApp", "LAT -== " + String.valueOf(lat));
                    Log.w("myApp", "LON -== " + String.valueOf(lon));

                    String coordinates = "lat=" + String.valueOf(lat) + "&" + "lon=" + String.valueOf(lon);

//                editor.putString(getString(R.string.CityCoordinateKey), coordinates);
//                editor.commit();
                    Log.w("myApp", "COORDINATE -== " + preferences.getString(getString(R.string.CityCoordinateKey), "missed"));
                    editor.putString("newCoordinates", "COORDINATE -== " + "lat=" + String.valueOf(lat) + "&" + "lon=" + String.valueOf(lon));
                    editor.commit();
                }

            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
