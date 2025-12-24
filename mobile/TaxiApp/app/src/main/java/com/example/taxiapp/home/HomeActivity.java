package com.example.taxiapp.home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taxiapp.R;

import com.example.taxiapp.map.MapFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // connects to layout

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map_container, new MapFragment())
                    .commit();
        }
    }
}