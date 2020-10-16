package com.example.forestfiredetection.forgotpassword;

import androidx.annotation.NonNull;
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
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.forestfiredetection.login.Login;
import com.example.forestfiredetection.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Forgot_Password extends AppCompatActivity {

    TextInputLayout username;
    Button nextButton;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password);

        username = findViewById(R.id.forget_password_username);
        nextButton = findViewById(R.id.forget_password_next_btn);
        progressBar = findViewById(R.id.forget_password_progress_bar);

        refresh();

    }

    private void refresh() {
        username.setError(null);
        username.setErrorEnabled(false);
        progressBar.setVisibility(View.GONE);
    }

    private Boolean validateUsername() {

        String val = Objects.requireNonNull(username.getEditText()).getText().toString();
        String noWhiteSpaces = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }


    public void verifyUsername(View view) {

        if (!isConnected(this)) {
            showCustomDialog();
            return;
        }

        if (!validateUsername()) {
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        isUser();


    }

    private void isUser() {

        final String userEnteredUsername = Objects.requireNonNull(username.getEditText()).getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if ((snapshot.exists())) {

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                    String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                    String phoneNoFromDB = snapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                    String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);

                    Intent intent = new Intent(getApplicationContext(), MakeSelection.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("phoneNo", phoneNoFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("password", passwordFromDB);

                    startActivity(intent);
                    finish();

                } else {

                    username.setError("No such User exists");
                    username.requestFocus();
                    progressBar.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void callBackScreenFromForgetPassword(View view) {

        startActivity(new Intent(Forgot_Password.this, Login.class));
        finish();

    }


    // Check internet connection

    private boolean isConnected(Forgot_Password forgot_password) {


        ConnectivityManager connectivityManager = (ConnectivityManager) Forgot_Password.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {

            return true;
        } else {
            return false;

        }

    }

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Forgot_Password.this);

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
                        startActivity(new Intent(getApplicationContext(), Forgot_Password.class));
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void chooseMobileDataOrWifi() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Forgot_Password.this);

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
                        startActivity(new Intent(getApplicationContext(), Forgot_Password.class));
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }


}