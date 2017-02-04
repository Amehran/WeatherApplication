package com.example.arminmehran.weatherapplication;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;



/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    public static final String MenuFragmentName = MenuFragment.class.getSimpleName();
    LinearLayout linearLayout;
    private Button about, citySelectButton;
    private ToggleButton unitsButton, geoButton;
    private EditText cityNameText;
    private TextView cityNameInMenu;
    private Boolean cityNameChanged, geoLocationRequested, metricUnitsSelected, menuVisibilty,linearLayoutVisble;


    LocationManager locationManager;
    double longitudeGPS, latitudeGPS;
    String coodinates;

    public MenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        about = (Button) view.findViewById(R.id.about);
        geoButton = (ToggleButton) view.findViewById(R.id.geoButton);
        unitsButton = (ToggleButton) view.findViewById(R.id.unitsButton);
        linearLayout = (LinearLayout) view.findViewById(R.id.cityNameBox);
        citySelectButton = (Button) view.findViewById(R.id.citySelectButton);
        cityNameText = (EditText) view.findViewById(R.id.cityNameText);
        cityNameInMenu = (TextView) view.findViewById(R.id.text1);

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.WeatherData), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        onStartMenuStatusInit();

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((WeatherActivity) getActivity()).manageAboutFragment("add");
            }
        });

        geoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (geoButton.isChecked()) {
                    linearLayout.setVisibility(View.INVISIBLE);


                    geoLocationRequested = true;
                    cityNameChanged = false;
                    Toast.makeText(getActivity(), "will be implemented soon", Toast.LENGTH_LONG).show();
                    onExitMenuStatusSave();

                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                    cityNameText.setText(preferences.getString(getString(R.string.CityNameKey),getString(R.string.CityNameDefault)));
                    linearLayoutVisble =true;
                    geoLocationRequested = false;

                    Toast.makeText(getActivity(), "will be implemented soon",Toast.LENGTH_LONG).show();
                }
            }
        });

        unitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(unitsButton.isChecked()){
                    metricUnitsSelected = true;
                    Toast.makeText(getActivity(), "will be implemented soon",Toast.LENGTH_LONG).show();

                } else {
                    metricUnitsSelected = false;
                    Toast.makeText(getActivity(), "will be implemented soon",Toast.LENGTH_LONG).show();
                }

            }
        });
        citySelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.WeatherData), Context.MODE_PRIVATE);

                String currentCity  = preferences.getString(getString(R.string.CityNameKey),getString(R.string.CityNameDefault));
                String newCity = String.valueOf(cityNameText.getText()).trim();
                if (currentCity == newCity || newCity == "") {
                    String s = "2";
                    cityNameChanged = false;
                } else {

                    editor.putString(getString(R.string.CityNameKey),newCity);
                    editor.commit();

                    String s = preferences.getString(getString(R.string.CityNameKey),getString(R.string.CityNameDefault));

                    cityNameChanged = true;
                }
                editor.putString(getString(R.string.CityNameKey),newCity);
                editor.commit();
                cityNameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            hideKeyboard(v);
                        }
                    }
                });
                onExitMenuStatusSave();
            }
        });
        editor.putBoolean("menuVisibilty",true);
        editor.commit();
        onExitMenuStatusSave();

    }

    public void onStartMenuStatusInit(){

        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.WeatherData), Context.MODE_PRIVATE);

        cityNameChanged = preferences.getBoolean("cityNameChanged",false);
        geoLocationRequested = preferences.getBoolean("geoLocationRequested",true);
        metricUnitsSelected = preferences.getBoolean("metricUnitsSelected",true);
        linearLayoutVisble = preferences.getBoolean("cityNameTextVisible",false);

        geoButton.setChecked(preferences.getBoolean("geoLocationRequested",false));
        if (geoButton.isChecked()) {linearLayout.setVisibility(View.INVISIBLE);} else linearLayout.setVisibility(View.VISIBLE);
        unitsButton.setChecked(preferences.getBoolean("metricUnitsSelected",false));
        cityNameInMenu.setText(preferences.getString(getString(R.string.CityNameKey),getString(R.string.CityNameDefault)));
    }

    public void onExitMenuStatusSave(){
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.WeatherData), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Boolean geoToSave, UnitsToSave;
        geoToSave = geoButton.isChecked() ? true : false;

        if (geoButton.isChecked()) {
            editor.putBoolean("geoLocationRequested", true);
            editor.putBoolean("cityNameChanged",false);


        } else {editor.putBoolean("geoLocationRequested", false);
            editor.putBoolean("cityNameChanged",true);
        }
        editor.putBoolean("metricUnitsSelected", unitsButton.isChecked());
        editor.putString(getString(R.string.CityCoordinateKey),coodinates);
        editor.putBoolean("cityNameTextVisible",linearLayoutVisble);
        editor.commit();

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
