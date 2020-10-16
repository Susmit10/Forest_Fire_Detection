package com.example.forestfiredetection.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.forestfiredetection.R;

public class ForgetPasswordOnEmailSelection extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_on_email_selection);



    }

    public void callBackScreenFromForgetPasswordOnEmailSelection(View view) {

        Intent intent = new Intent(getApplicationContext(), Forgot_Password.class);
        startActivity(intent);
        finish();
    }

    public void verifyEmail(View view) {



    }
}