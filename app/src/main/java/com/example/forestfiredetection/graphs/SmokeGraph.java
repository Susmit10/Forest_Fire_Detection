package com.example.forestfiredetection.graphs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.forestfiredetection.Dashboard;
import com.example.forestfiredetection.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SmokeGraph extends AppCompatActivity {

    BarChart smokeGraph;
    TextView smoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smoke_graph);
        smokeGraph = findViewById(R.id.smokeGraph);

        smoke = findViewById(R.id.smokeValue);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Sensor Data").child("smoke");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String smokeValue = Objects.requireNonNull(snapshot.getValue()).toString();
                smoke.setText(smokeValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayList<BarEntry> values = new ArrayList<>();

        values.add(new BarEntry(-80, 10));
        values.add(new BarEntry(-70, 30));
        values.add(new BarEntry(-60, 48));
        values.add(new BarEntry(-50, 24));
        values.add(new BarEntry(-40, 107));
        values.add(new BarEntry(-30, 280));
        values.add(new BarEntry(-20, 355));
        values.add(new BarEntry(-10, 478));

//        values.add(new BarEntry(203, 420));
//        values.add(new BarEntry(214, 327));
//        values.add(new BarEntry(225, 450));
//        values.add(new BarEntry(236, 239));
//        values.add(new BarEntry(247, 520));
//        values.add(new BarEntry(258, 553));
//        values.add(new BarEntry(269, 298));
//        values.add(new BarEntry(270, 478));

        BarDataSet barDataSet = new BarDataSet(values, "Values");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        smokeGraph.setFitBars(true);
        smokeGraph.setData(barData);
        smokeGraph.getDescription().setText("Smoke graph");
        smokeGraph.animateY(2000);

    }

    public void onHome(View view) {

        startActivity(new Intent(getApplicationContext(), Dashboard.class));
        finish();

    }

    public void onBack(View view) {

        super.onBackPressed();

    }
}