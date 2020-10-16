package com.example.forestfiredetection.users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.forestfiredetection.R;

public class SelectMode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mode);
    }

    public void onAuto(View view) {
        Toast.makeText(this, "Autonomous Mode Selected", Toast.LENGTH_SHORT).show();
    }

    public void onManual(View view) {
        Toast.makeText(this, "Manual Mode Selected", Toast.LENGTH_SHORT).show();
    }
}