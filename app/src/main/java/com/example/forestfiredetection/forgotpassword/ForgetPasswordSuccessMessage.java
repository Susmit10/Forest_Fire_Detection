package com.example.forestfiredetection.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.forestfiredetection.login.Login;
import com.example.forestfiredetection.R;

public class ForgetPasswordSuccessMessage extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_success_message);

        button =findViewById(R.id.success_message_btn);

    }

    public void backToLogin(View view) {

        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();

    }
}