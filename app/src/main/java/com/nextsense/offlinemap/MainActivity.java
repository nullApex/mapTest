package com.nextsense.offlinemap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.nextsense.offlinemap.MapUtil.NsMapView;


public class MainActivity extends AppCompatActivity {

    private NsMapView testMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    private void setupViews() {
        testMapView = findViewById(R.id.testMapView);
        testMapView.initMap(18.6f, 42.007305f, 21.417469f);
    }
}
