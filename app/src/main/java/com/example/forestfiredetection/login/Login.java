package com.example.forestfiredetection.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forestfiredetection.R;
import com.example.forestfiredetection.SignUp;
import com.example.forestfiredetection.UserProfile;
import com.example.forestfiredetection.database.SessionManager;
import com.example.forestfiredetection.forgotpassword.Forgot_Password;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class Login extends AppCompatActivity {

    Animation rightanim;
    TextView signIn;

    ImageView image;
    TextView text;
    TextInputLayout username, password;
    Button button1, button2;
    CheckBox rememberMe;

    private long backpressedtime;
    Toast backToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setTheme(R.style.AppTheme);

        setContentView(R.layout.login);

//        SessionManager sessionManager = new SessionManager(getApplicationContext());
//        if(sessionManager.checkLogin())
//        {
//            Toast.makeText(this, "You are already logged in", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), UserProfile.class));
//            finish();
//        }

        rightanim = AnimationUtils.loadAnimation(this, R.anim.right_animation);
        rightanim.setDuration(1800);

        signIn = findViewById(R.id.slogan_name);
        signIn.setAnimation(rightanim);


        image = findViewById(R.id.logo_image);
        text = findViewById(R.id.logo_id);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        button1 = findViewById(R.id.signin);
        button2 = findViewById(R.id.new_user);
        rememberMe = findViewById(R.id.rememberMe);

        username.setError(null);
        password.setError(null);
        username.setErrorEnabled(false);
        password.setErrorEnabled(false);

        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_REMEMBER_ME);
        if (sessionManager.checkRememberMe()) {
            HashMap<String, String> rememberMeDetails = sessionManager.getRememberMeDetailFromSession();
            Objects.requireNonNull(username.getEditText()).setText(rememberMeDetails.get(SessionManager.KEY_SESSION_USERNAME));
            Objects.requireNonNull(password.getEditText()).setText(rememberMeDetails.get(SessionManager.KEY_SESSION_PASSWORD));
            rememberMe.setChecked(true);
        }

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

    private Boolean validatePassword() {

        String val = Objects.requireNonNull(password.getEditText()).getText().toString();

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }

    public void onSignIn(View view) {

        if (!isConnected(this)) {
            showCustomDialog();
            return;
        }

        if (!validateUsername() | !validatePassword()) {
            return;
        } else {

            SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_REMEMBER_ME);

            if (rememberMe.isChecked()) {
                final String userEnteredUsername = Objects.requireNonNull(username.getEditText()).getText().toString().trim();
                final String userEnteredPassword = Objects.requireNonNull(password.getEditText()).getText().toString().trim();
                sessionManager.createRememberMeSession(userEnteredUsername, userEnteredPassword);
            }
            else
            {
                sessionManager.forgetRememberMe();
            }

            isUser();
        }

    }


    // Check internet connection

    private boolean isConnected(Login login) {


        ConnectivityManager connectivityManager = (ConnectivityManager) Login.this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if ((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected())) {

            return true;
        } else {
            return false;

        }

    }

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

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
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }


    private void isUser() {

        final String userEnteredUsername = Objects.requireNonNull(username.getEditText()).getText().toString().trim();
        final String userEnteredPassword = Objects.requireNonNull(password.getEditText()).getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query checkUser = reference.orderByChild("username").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if ((snapshot.exists())) {

                    username.setError(null);
                    username.setErrorEnabled(false);

                    String passwordFromDB = snapshot.child(userEnteredUsername).child("password").getValue(String.class);

                    if ((passwordFromDB).equals(userEnteredPassword)) {

                        password.setError(null);
                        password.setErrorEnabled(false);

                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        String phoneNoFromDB = snapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                        String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);
                        String mode = "fromLogin";

                        SessionManager sessionManager = new SessionManager(Login.this, SessionManager.SESSION_USER_SESSION);
                        sessionManager.createLoginSession(nameFromDB, usernameFromDB, emailFromDB, phoneNoFromDB, passwordFromDB, mode);


                        Intent intent = new Intent(getApplicationContext(), UserProfile.class);
//                        intent.putExtra("name", nameFromDB);
//                        intent.putExtra("username", usernameFromDB);
//                        intent.putExtra("phoneNo", phoneNoFromDB);
//                        intent.putExtra("email", emailFromDB);
//                        intent.putExtra("password", passwordFromDB);
//                        intent.putExtra("whatToDo", "fromLogin");

                        startActivity(intent);
                        finish();
                    } else {

                        password.setError("Wrong Password");
                        password.requestFocus();
                    }
                } else {

                    username.setError("No such User exists");
                    username.requestFocus();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void onSignUp(View view) {

        Intent intent = new Intent(Login.this, SignUp.class);

        Pair[] pairs = new Pair[6];
        pairs[0] = new Pair<View, String>(image, "logo_image");
        pairs[1] = new Pair<View, String>(text, "slogan_text");
        pairs[2] = new Pair<View, String>(username, "username");
        pairs[3] = new Pair<View, String>(password, "password");
        pairs[4] = new Pair<View, String>(button1, "mainbtn");
        pairs[5] = new Pair<View, String>(button2, "otherbtn");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
        startActivity(intent, options.toBundle());

    }


    public void callForgotPassword(View view) {

        Intent intent = new Intent(getApplicationContext(), Forgot_Password.class);
        startActivity(intent);

    }

    public void onSignInWithGoogle(View view) {

        Intent intent = new Intent(getApplicationContext(), SignInWithGoogle.class);
        startActivity(intent);

    }


    @Override
    public void onBackPressed() {


        if (backpressedtime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            finish();
            return;
        } else {
            backToast = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backpressedtime = System.currentTimeMillis();
    }

}
