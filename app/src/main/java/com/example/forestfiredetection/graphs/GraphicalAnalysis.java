package com.example.forestfiredetection.graphs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.forestfiredetection.R;

public class GraphicalAnalysis extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphical_analysis);
    }

    public void onRain(View view) {
        startActivity(new Intent(getApplicationContext(), RainGraph.class));
    }

    public void onFlame(View view) {
        startActivity(new Intent(getApplicationContext(), FireGraph.class));
    }

    public void onSmoke(View view) {
        startActivity(new Intent(getApplicationContext(), SmokeGraph.class));
    }

    public void onTemp(View view) {
        startActivity(new Intent(getApplicationContext(), TempGraph.class));
    }

    public void onBack(View view) {
        super.onBackPressed();
    }

    public void onHumid(View view) {
        startActivity(new Intent(getApplicationContext(), HumidityGraph.class));
    }

    public void onPir(View view) {
        startActivity(new Intent(getApplicationContext(), PirGraph.class));
    }
}