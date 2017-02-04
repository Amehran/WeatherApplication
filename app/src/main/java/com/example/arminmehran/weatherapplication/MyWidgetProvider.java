package com.example.arminmehran.weatherapplication;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Timer;

/**
 * when having a widget, why readfile() generates ERROR and stops  the app ???!?
 * how can update the app and the widget at the same time ?!?
 */


public class MyWidgetProvider extends AppWidgetProvider {
//    private WeatherTask weatherTask;
    Timer myTimer = new Timer();
    Boolean Update;


    private static final String jsonFilePath = "/storage/sdcard0/weather.json";
    public static String CityName = null;
    private static final String ACTION_CLICK = "ACTION_CLICK";
    private String weather[];
    private Button menuButtonFromWidget;
    RelativeLayout relativeLayout;

    //
    public static RemoteViews getWidgetRemoteViews(Context context) {
        Log.i("TAG"," getWidgetRemoteViews called");

        // CREATING INTENT NAMED button1Intent TO GET THE MENU FROM ACTIVITY
        // IT BRINGS THE MAIN ACTIVITY WHICH MENU BUTTON IS ACCESSIBLE
        Intent button1Intent = new Intent(context, WeatherActivity.class);
        //button1Intent.putExtra("Update",true);
        PendingIntent button1PendingIntent = PendingIntent.getActivity(context, 0, button1Intent, PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews appWidgetViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        // AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        appWidgetViews.setOnClickPendingIntent(R.id.layout, button1PendingIntent);
        return appWidgetViews;
    }
//



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Log.i("TAG"," onUpdate called");

        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            RemoteViews appWidgetViews = getWidgetRemoteViews(context);

            appWidgetManager.updateAppWidget(appWidgetId, appWidgetViews);

        }

    }

    private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
        iConnectivityManagerField.setAccessible(true);
        final Object iConnectivityManager = iConnectivityManagerField.get(conman);
        final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
    }

    private class ReadFile extends AsyncTask<Void, Void, JSONObject> {

        JSONObject jsonObj;

        @Override
        protected JSONObject doInBackground(Void... params) {

            // TODO Auto-generated method stub
            File yourFile = new File("/storage/sdcard0/weather.json");
            FileInputStream stream = null;

            try {
                stream = new FileInputStream(yourFile);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String jString = null;

            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                    /* Instead of using default, pass in a decoder. */
                jString = Charset.defaultCharset().decode(bb).toString();
                jsonObj = new JSONObject(jString);


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            ;
            return jsonObj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObj) {

            try {

                JSONObject coord = jsonObj.getJSONObject("coord");
                weather[0] = coord.getString("lon");
                weather[1] = coord.getString("lat");
                JSONObject main = jsonObj.getJSONObject("main");
                weather[2] = main.getString("temp");
                weather[3] = main.getString("humidity");

                JSONObject wind = jsonObj.getJSONObject("wind");
                weather[4] = main.getString("speed");
                weather[5] = main.getString("deg");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        super.onReceive(context, intent);

        String action = intent.getAction();
        Bundle extras = intent.getExtras();

        if (action != null) {
            final AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName name = new ComponentName(context, MyWidgetProvider.class);
            int[] appWidgetId = AppWidgetManager.getInstance(context).getAppWidgetIds(name);
            final int N = appWidgetId.length;
            if (N < 1) {
                return;
            } else {
                int id = appWidgetId[N - 1];
                updateWidget(context, appWidgetManager, id);
            }
        } else {
            super.onReceive(context, intent);
        }
    }

    static RemoteViews updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        //views.setTextViewText(R.id.city_field, title);
        appWidgetManager.updateAppWidget(appWidgetId, views);

        return views;
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

    }

    public class receiverScreen extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {


                RemoteViews appWidgetViews = getWidgetRemoteViews(context);


            }
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            }
            if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {

            }
        }

    }
}

 