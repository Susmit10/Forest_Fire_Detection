package com.example.forestfiredetection.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.forestfiredetection.R;
import com.example.forestfiredetection.Verify_PhoneNo;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class ForgetPasswordOnPhoneSelection extends AppCompatActivity {

    String  _name, _username, _email, _phoneNo, _password;

    ProgressBar progressBar;

    TextInputLayout phone_No;
    CountryCodePicker codePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_on_phone_selection);

        phone_No = findViewById(R.id.forget_password_phone_number);
        codePicker = findViewById(R.id.country_code_picker);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        _name = getIntent().getStringExtra("name");
        _username = getIntent().getStringExtra("username");
        _email = getIntent().getStringExtra("email");
        _phoneNo = getIntent().getStringExtra("phoneNo");
        _password = getIntent().getStringExtra("password");

        phone_No.setError(null);
        phone_No.setErrorEnabled(false);



    }


    public void callBackScreenFromForgetPasswordOnPhoneSelection(View view) {

        startActivity(new Intent(ForgetPasswordOnPhoneSelection.this, Forgot_Password.class));
        finish();

    }


    private Boolean validatePhoneNo() {

        String val = Objects.requireNonNull(phone_No.getEditText()).getText().toString();

        if (val.isEmpty()) {
            phone_No.setError("Field cannot be empty");
            return false;
        } else {
            phone_No.setError(null);
            return true;
        }

    }


    public void verifyPhoneNumber(View view) {

        if(!validatePhoneNo())
        {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //        Get complete phone number
        String code = codePicker.getSelectedCountryCodeWithPlus();
        String _getUserEnteredPhoneNumber = Objects.requireNonNull(phone_No.getEditText()).getText().toString().trim();
//        Remove first zero if entered!
        if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
        }
        //Complete phone number
        final String phoneNo = code+_getUserEnteredPhoneNumber;

        if(_phoneNo.equals(phoneNo))
        {
            Intent intent = new Intent(getApplicationContext(), Verify_PhoneNo.class);
            intent.putExtra("phoneNo", phoneNo);
            intent.putExtra("username", _username);
            intent.putExtra("whatToDo", "updatePassword");
            startActivity(intent);
            finish();
        }
        else
        {
            phone_No.setError("Incorrect phone number");
            phone_No.requestFocus();
            progressBar.setVisibility(View.GONE);
        }



    }
}