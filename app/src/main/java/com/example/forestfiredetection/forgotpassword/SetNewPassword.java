package com.example.forestfiredetection.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.forestfiredetection.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SetNewPassword extends AppCompatActivity {

    TextInputLayout newPassword, confirmPassword;
    Button button;
    ProgressBar progressBar;

    DatabaseReference reference;

    String _username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);
        button = findViewById(R.id.set_new_password_btn);
        progressBar = findViewById(R.id._progress_Bar);

        reference = FirebaseDatabase.getInstance().getReference("users");

        progressBar.setVisibility(View.GONE);
        newPassword.setError(null);
        confirmPassword.setError(null);
        newPassword.setErrorEnabled(false);
        confirmPassword.setErrorEnabled(false);

        _username = getIntent().getStringExtra("username");



    }


    private Boolean validatePassword() {

        String val = Objects.requireNonNull(newPassword.getEditText()).getText().toString();
//        String passwordVal =
//                "(?=.*[0-9])" +         //at least 1 digit
//                //"(?=.*[a-z])" +         //at least 1 lower case letter
//                //"(?=.*[A-Z])" +         //at least 1 upper case letter
//                //"(?=.*[a-zA-Z])" +      //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                "\\A\\w{4,20}\\z" +           //no white spaces
//                ".{4,}" +               //at least 4 characters
//                "$";

        String passCode = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        if (val.isEmpty()) {
            newPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passCode)) {
            newPassword.setError("Password too weak");
            return false;
        } else {
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateConfirmPassword() {

        String val = Objects.requireNonNull(confirmPassword.getEditText()).getText().toString();
//        String passwordVal =
//                "(?=.*[0-9])" +         //at least 1 digit
//                //"(?=.*[a-z])" +         //at least 1 lower case letter
//                //"(?=.*[A-Z])" +         //at least 1 upper case letter
//                //"(?=.*[a-zA-Z])" +      //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                "\\A\\w{4,20}\\z" +           //no white spaces
//                ".{4,}" +               //at least 4 characters
//                "$";

//        String passCode = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        if (val.isEmpty()) {
            confirmPassword.setError("Field cannot be empty");
            return false;
        }
        else
        {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }

    }


    public void goToHomeFromSetNewPassword(View view) {

        Intent intent = new Intent(getApplicationContext(), Forgot_Password.class);
        startActivity(intent);
        finish();

    }

    public void setNewPasswordBtn(View view) {

        progressBar.setVisibility(View.VISIBLE);

        if(!validatePassword() || !validateConfirmPassword())
        {
            progressBar.setVisibility(View.GONE);
            return;
        }

        String NewPassword = Objects.requireNonNull(newPassword.getEditText()).getText().toString();
        String ConfirmPassword = Objects.requireNonNull(confirmPassword.getEditText()).getText().toString();

        if(NewPassword.equals(ConfirmPassword))
        {
            Toast.makeText(this, "Password Updated", Toast.LENGTH_SHORT).show();
            reference.child(_username).child("password").setValue(Objects.requireNonNull(newPassword.getEditText()).getText().toString());
            Intent intent = new Intent(getApplicationContext(), ForgetPasswordSuccessMessage.class);
            startActivity(intent);
            finish();

        }
        else
        {
            confirmPassword.setError("Incorrect Password");
            confirmPassword.requestFocus();
            progressBar.setVisibility(View.GONE);
        }

    }
}