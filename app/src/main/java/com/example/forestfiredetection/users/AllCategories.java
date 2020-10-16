package com.example.forestfiredetection.users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.forestfiredetection.R;
import com.example.forestfiredetection.graphs.GraphicalAnalysis;

public class AllCategories extends AppCompatActivity {

    ImageView backIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);

        backIcon = findViewById(R.id.back_pressed);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllCategories.super.onBackPressed();
            }
        });

    }

    public void onRoverControlling(View view) {
    }

    public void onSelectMode(View view) {
        startActivity(new Intent(getApplicationContext(), SelectMode.class));
    }

    public void onGraphicalAnalysis(View view) {

        startActivity(new Intent(getApplicationContext(), GraphicalAnalysis.class));

    }

    public void onSensorValues(View view) {
        startActivity(new Intent(getApplicationContext(), SensorData.class));
    }

}