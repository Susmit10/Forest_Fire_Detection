package com.example.forestfiredetection.users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.forestfiredetection.Dashboard;
import com.example.forestfiredetection.R;
import com.example.forestfiredetection.graphs.FireGraph;
import com.example.forestfiredetection.graphs.HumidityGraph;
import com.example.forestfiredetection.graphs.PirGraph;
import com.example.forestfiredetection.graphs.RainGraph;
import com.example.forestfiredetection.graphs.SmokeGraph;
import com.example.forestfiredetection.graphs.TempGraph;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SensorData extends AppCompatActivity {

//    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView flame, rain, smoke, temp, pir, humid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_data);

        flame = findViewById(R.id.flameValue);
        rain = findViewById(R.id.rainValue);
        smoke = findViewById(R.id.smokeValue);
        temp = findViewById(R.id.tempValue);
        pir = findViewById(R.id.pirValue);
        humid = findViewById(R.id.humidityValue);

        databaseReference = FirebaseDatabase.getInstance().getReference("Sensor Data");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String flameValue = Objects.requireNonNull(snapshot.child("Fire Sensor").getValue()).toString();
                String rainValue = Objects.requireNonNull(snapshot.child("rain").getValue()).toString();
                String smokeValue = Objects.requireNonNull(snapshot.child("smoke").getValue()).toString();
                String tempValue = Objects.requireNonNull(snapshot.child("Temp Sensor").getValue()).toString();
                String pirValue = Objects.requireNonNull(snapshot.child("pir").getValue()).toString();
                String humidityValue = Objects.requireNonNull(snapshot.child("Humidity Sensor").getValue()).toString();

                flame.setText(flameValue);
                rain.setText(rainValue);
                smoke.setText(smokeValue);
                temp.setText(tempValue);
                pir.setText(pirValue);
                humid.setText(humidityValue);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void onBack(View view) {
        super.onBackPressed();
    }

    public void onHome(View view) {
        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();
    }



    public void onFlame(View view) {
        startActivity(new Intent(getApplicationContext(), FireGraph.class));
    }

    public void onRain(View view) {
        startActivity(new Intent(getApplicationContext(), RainGraph.class));
    }

    public void onSmoke(View view) {
        startActivity(new Intent(getApplicationContext(), SmokeGraph.class));
    }

    public void onTemp(View view) {
        startActivity(new Intent(getApplicationContext(), TempGraph.class));
    }

    public void onPir(View view) {
        startActivity(new Intent(getApplicationContext(), PirGraph.class));
    }

    public void onHumid(View view) {
        startActivity(new Intent(getApplicationContext(), HumidityGraph.class));
    }
}