package com.example.arminmehran.weatherapplication;


import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    public static final String WeatherFragmentName = WeatherFragment.class.getSimpleName();
    ListView weatherList;
    ImageView imageView;
    Button day0,day1,day2,day3,day4;

    City cityModel;
    ArrayList<String> weatherInfo = new ArrayList<String>();

    ArrayList<String> ll = new ArrayList<String>();
    Set<String> result =null;
    ArrayAdapter<String> adapter=null;


    public WeatherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        day0 = (Button) view.findViewById(R.id.day0);
        day1 = (Button) view.findViewById(R.id.day1);
        day2 = (Button) view.findViewById(R.id.day2);
        day3 = (Button) view.findViewById(R.id.day3);
        day4 = (Button) view.findViewById(R.id.day4);
        imageView = (ImageView) view.findViewById(R.id.WeatherImage);
        weatherList = (ListView) view.findViewById(R.id.weatherList);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.w("TAG", "onViewCreated" );



        displayList();
    }

    public void onClick(View v) {

        switch (v.getId()){
            case R.id.day0 :
                Toast.makeText(getActivity(), "will be implemented soon",Toast.LENGTH_LONG).show();
                break;
            case R.id.day1 :
                Toast.makeText(getActivity(), "will be implemented soon",Toast.LENGTH_LONG).show();
                break;
            case R.id.day2 :
                Toast.makeText(getActivity(), "will be implemented soon",Toast.LENGTH_LONG).show();
                break;
            case R.id.day3 :
                Toast.makeText(getActivity(), "will be implemented soon",Toast.LENGTH_LONG).show();
                break;
            case R.id.day4 :Toast.makeText(getActivity(), "will be implemented soon",Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getActivity(), "will be implemented soon",Toast.LENGTH_LONG).show();
                break;

        }
    }
    void displayList(){
    Log.w("sysApp","Entered the Display weather method");
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.WeatherData), Context.MODE_PRIVATE);

        setIcon(preferences.getString("icon", "10n"));


        adapter = new ArrayAdapter<String>(getActivity(), R.layout.da_item, weatherInfo);
        adapter.clear();
        weatherInfo.clear();
        weatherInfo.add(preferences.getString("cityName",getString(R.string.CityNameDefault)) +","+ preferences.getString("CountryName", getString(R.string.CountryNameDefault)));
        weatherInfo.add(preferences.getString("lat", getString(R.string.LatitudeLable)));
        weatherInfo.add(preferences.getString("lon", getString(R.string.LongitudeLable)));
        weatherInfo.add(preferences.getString("date", ""));
        weatherInfo.add(preferences.getString("description", ""));
        weatherInfo.add(preferences.getString("temp", ""));
        weatherInfo.add(preferences.getString("pressure", ""));
        weatherInfo.add(preferences.getString("humidity", ""));
        weatherInfo.add(preferences.getString("windspeed", ""));
        weatherInfo.add(preferences.getString("winddeg", ""));



        weatherList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        int ids[] = AppWidgetManager.getInstance(getActivity()).getAppWidgetIds(new ComponentName(getActivity(),MyWidgetProvider.class));

        Toast.makeText(getActivity(), "Number of widgets: "+ids.length, Toast.LENGTH_LONG).show();

        adapter.notifyDataSetChanged();


    }
    private void setIcon(String cityIcon) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && cityIcon != null) {
            switch (cityIcon) {
                case "01d":
                        imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d01d, null));
                    break;

                case "02d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d02d, null));
                    break;

                case "03d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d03d, null));
                    break;

                case "04d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d04d, null));
                    break;

                case "09d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d09d, null));
                    break;

                case "10d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d10d, null));
                    break;

                case "11d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d11d, null));
                    break;

                case "13d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d13d, null));
                    break;

                case "50d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d50d, null));
                    break;

                case "01n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n01n, null));
                    break;

                case "02n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n02n, null));
                    break;

                case "03n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n03n, null));
                    break;

                case "04n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n04n, null));
                    break;

                case "09n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n09n, null));
                    break;

                case "10n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n10n, null));
                    break;

                case "11n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n11n, null));
                    break;

                case "13n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n13n, null));
                    break;

                case "50n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n50n, null));
                    break;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            switch (cityIcon) {
                case "01d":

                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d01d));

                    break;

                case "02d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d02d));
                    break;

                case "03d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d03d));
                    break;

                case "04d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d04d));
                    break;

                case "09d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d09d));
                    break;

                case "10d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d10d));
                    break;

                case "11d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d11d));
                    break;

                case "13d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d13d));
                    break;

                case "50d":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.d50d));
                    break;

                case "01n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n01n));
                    break;

                case "02n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n02n));
                    break;

                case "03n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n03n));
                    break;

                case "04n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n04n));
                    break;

                case "09n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n09n));
                    break;

                case "10n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n10n));
                    break;

                case "11n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n11n));
                    break;

                case "13n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n13n));
                    break;

                case "50n":
                    imageView.setBackground(getActivity().getResources().getDrawable(R.drawable.n50n));
                    break;
            }
        }
    }



}
