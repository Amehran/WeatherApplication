package com.example.arminmehran.weatherapplication;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * AboutFragment.java
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
public class AboutFragment extends Fragment {
    public static final String AboutFragmentName = AboutFragment.class.getSimpleName();

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

}
