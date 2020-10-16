package com.example.forestfiredetection.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.forestfiredetection.R;

public class MakeSelection extends AppCompatActivity {

    String  _name, _username, _email, _phoneNo, _password;

    TextView phoneNumber, emailId;

    String phoneHint, emailHint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_selection);

        phoneNumber = findViewById(R.id.mobile_des);
        emailId = findViewById(R.id.mail_des);

        _name = getIntent().getStringExtra("name");
        _username = getIntent().getStringExtra("username");
        _email = getIntent().getStringExtra("email");
        _phoneNo = getIntent().getStringExtra("phoneNo");
        _password = getIntent().getStringExtra("password");


        phoneHint = "*******" + _phoneNo.substring(10);
        emailHint = "*******" + _email.substring(10);

        phoneNumber.setText(phoneHint);
        emailId.setText(emailHint);

    }

    public void callBackScreenFromMakeSelection(View view) {

        startActivity(new Intent(MakeSelection.this, Forgot_Password.class));
        finish();

    }

    public void callOnPhoneSelection(View view) {

        Intent intent = new Intent(getApplicationContext(), ForgetPasswordOnPhoneSelection.class);
        intent.putExtra("name", _name);
        intent.putExtra("username", _username);
        intent.putExtra("phoneNo", _phoneNo);
        intent.putExtra("email", _email);
        intent.putExtra("password", _password);
        startActivity(intent);
        finish();

    }


    public void callOnEmailSelection(View view) {

        Intent intent = new Intent(getApplicationContext(), ForgetPasswordOnEmailSelection.class);
        intent.putExtra("name", _name);
        intent.putExtra("username", _username);
        intent.putExtra("phoneNo", _phoneNo);
        intent.putExtra("email", _email);
        intent.putExtra("password", _password);
        startActivity(intent);
        finish();

    }
}