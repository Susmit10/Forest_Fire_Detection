package com.example.forestfiredetection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.forestfiredetection.login.Login;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

import java.util.Objects;

public class SignUp extends AppCompatActivity {

    Animation rightanim;
    TextView text;

    ImageView image;
    TextView text2;
    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    Button button1, button2;

    CountryCodePicker codePicker;

    //
//    FirebaseDatabase rootNode;
//    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        rightanim = AnimationUtils.loadAnimation(this, R.anim.right_animation);
        rightanim.setDuration(700);

        text = findViewById(R.id.signup);
        text.setAnimation(rightanim);

        image = findViewById(R.id.img);
        text2 = findViewById(R.id.txt);

        regName = findViewById(R.id.fullname);
        regUsername = findViewById(R.id.username1);
        regEmail = findViewById(R.id.email);
        regPhoneNo = findViewById(R.id.phone);
        regPassword = findViewById(R.id.password1);
        button1 = findViewById(R.id.signupbtn);
        button2 = findViewById(R.id.loginbtn);

        codePicker = findViewById(R.id._country_code_picker);

        refresh();

//        rootNode = FirebaseDatabase.getInstance();
//        reference = rootNode.getReference("users");


    }

    private void refresh() {

        regName.setError(null);
        regUsername.setError(null);
        regEmail.setError(null);
        regPhoneNo.setError(null);
        regPassword.setError(null);
        regName.setErrorEnabled(false);
        regUsername.setErrorEnabled(false);
        regEmail.setErrorEnabled(false);
        regPhoneNo.setErrorEnabled(false);
        regPassword.setErrorEnabled(false);

    }

    private Boolean validateName() {

        String val = Objects.requireNonNull(regName.getEditText()).getText().toString();

        if (val.isEmpty()) {
            regName.setError("Field cannot be empty");
            return false;
        } else {
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateUsername() {

        String val = Objects.requireNonNull(regUsername.getEditText()).getText().toString();
        String noWhiteSpaces = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            regUsername.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpaces)) {
            regUsername.setError("White spaces are not allowed");
            return false;
        } else {
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateEmail() {

        String val = Objects.requireNonNull(regEmail.getEditText()).getText().toString();
        String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            regEmail.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(validEmail)) {
            regEmail.setError("Enter a valid Email Address");
            return false;
        } else {
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePhoneNo() {

        String val = Objects.requireNonNull(regPhoneNo.getEditText()).getText().toString();

        if (val.isEmpty()) {
            regPhoneNo.setError("Field cannot be empty");
            return false;
        } else {
            regPhoneNo.setError(null);
            return true;
        }

    }

    private Boolean validatePassword() {

        String val = Objects.requireNonNull(regPassword.getEditText()).getText().toString();
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
            regPassword.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passCode)) {
            regPassword.setError("Password too weak");
            return false;
        } else {
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }

    }


    public void btnClick(View view) {


        if (!isConnected(this)) {
            showCustomDialog();
            return;
        }


        if (!validateName() | !validateUsername() | !validateEmail() | !validatePhoneNo() | !validatePassword()) {
            return;
        }


//        Get complete phone number
        String code = codePicker.getSelectedCountryCodeWithPlus();
        String _getUserEnteredPhoneNumber = Objects.requireNonNull(regPhoneNo.getEditText()).getText().toString().trim();
//        Remove first zero if entered!
        if (_getUserEnteredPhoneNumber.charAt(0) == '0') {
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);
        }
        //Complete phone number
        final String phoneNo = code+_getUserEnteredPhoneNumber;

        String name = Objects.requireNonNull(regName.getEditText()).getText().toString();
        String username = Objects.requireNonNull(regUsername.getEditText()).getText().toString();
        String email = Objects.requireNonNull(regEmail.getEditText()).getText().toString();
        String password = Objects.requireNonNull(regPassword.getEditText()).getText().toString();


        Intent intent = new Intent(SignUp.this, Verify_PhoneNo.class);
        intent.putExtra("name", name);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        intent.putExtra("phoneNo", phoneNo);
        intent.putExtra("password", password);
        intent.putExtra("whatToDo", "signUp");
        startActivity(intent);
//        finish();


//        UserHelperClass helperClass = new UserHelperClass(name, username, email, phoneNo, password);
//
//        reference.child(username).setValue(helperClass);
//
//        Toast.makeText(this, "Account successfully created", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(SignUp.this, Login.class);
//        startActivity(intent);
//        finish();

    }

    public void test(View view) {

        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        finish();

    }


    // Check internet connection

    private boolean isConnected(SignUp signUp) {


        ConnectivityManager connectivityManager = (ConnectivityManager) SignUp.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {

            return true;
        } else {
            return false;

        }

    }

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);

        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                            startActivity(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS));
                            chooseMobileDataOrWifi();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void chooseMobileDataOrWifi() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);

        builder.setMessage("Please choose any one to proceed further")
                .setCancelable(false)
                .setPositiveButton("Mobile Data", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            startActivity(new Intent(Settings.ACTION_DATA_USAGE_SETTINGS));
                        }
                    }
                })
                .setNegativeButton("WiFi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(), SignUp.class));
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

}