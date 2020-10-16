package com.example.forestfiredetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forestfiredetection.database.SessionManager;
import com.example.forestfiredetection.login.Login;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;
import java.util.Objects;

public class UserProfile extends AppCompatActivity {

    CircularImageView circularImageView;

    TextInputLayout fullName, username, phoneNo, email, password;
    TextView fullNameLabel, usernameLabel;

    String user_name, user_username, user_phoneNo, user_email, user_password, whatToDo;

    CountryCodePicker codePicker;

    String nameFromDB, usernameFromDB, phoneNoFromDB, emailFromDB, passwordFromDB;

    DatabaseReference reference;

    int flag = 0;

    SessionManager sessionManager;

    final String googleMode = "fromGoogle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.MyTheme);

        setContentView(R.layout.activity_user_profile);

        sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USER_SESSION);

        reference = FirebaseDatabase.getInstance().getReference("users");

        // hooks
        fullName = findViewById(R.id.full_name_profile);
        username = findViewById(R.id.user_name);
        phoneNo = findViewById(R.id.phone_No);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass_word);
        circularImageView = findViewById(R.id.profile_image);
        codePicker = findViewById(R.id.country_Code_Picker);

        fullNameLabel = findViewById(R.id.full_name);
        usernameLabel = findViewById(R.id.username);

        // checking the mode
        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();

        if (sessionManager.checkLogin()) {
            whatToDo = userData.get(SessionManager.KEY_MODE);
//            Toast.makeText(this, whatToDo, Toast.LENGTH_SHORT).show();

//
//        Intent intentCode = getIntent();
//        whatToDo = intentCode.getStringExtra("whatToDo");


            assert whatToDo != null;
            if (whatToDo.equals(googleMode)) {

                GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
                if (signInAccount != null) {

                    user_name = signInAccount.getDisplayName();
                    user_username = signInAccount.getGivenName();
                    user_email = signInAccount.getEmail();
//                user_phoneNo = "NA";
//                user_password = "NA";

                    check();
                    updateView();
//                fullNameLabel.setText(signInAccount.getDisplayName());
//                usernameLabel.setText(signInAccount.getGivenName());
//                Objects.requireNonNull(fullName.getEditText()).setText(signInAccount.getDisplayName());
//                Objects.requireNonNull(username.getEditText()).setText(signInAccount.getGivenName());
//                Objects.requireNonNull(email.getEditText()).setText(signInAccount.getEmail());
//                Objects.requireNonNull(password.getEditText()).setText(signInAccount.getId());
//                Objects.requireNonNull(phoneNo.getEditText()).setText(signInAccount.getIdToken());
                }
            } else {
                getAllData();
            }
        }
        else {
            Toast.makeText(this, "Please login before going to your profile", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

    }

    private void check() {

        Query checkUser = reference.orderByChild("username").equalTo(user_username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if ((snapshot.exists())) {

                    passwordFromDB = snapshot.child(user_username).child("password").getValue(String.class);
                    phoneNoFromDB = snapshot.child(user_username).child("phoneNo").getValue(String.class);
                    //
//                    nameFromDB = snapshot.child(user_username).child("name").getValue(String.class);
//                    usernameFromDB = snapshot.child(user_username).child("username").getValue(String.class);

//                    emailFromDB = snapshot.child(user_username).child("email").getValue(String.class);

                    assert phoneNoFromDB != null;
                    if (phoneNoFromDB.equals("NA") && passwordFromDB.equals("NA")) {
                        Toast.makeText(UserProfile.this, "Please update your account with a phone no. and password", Toast.LENGTH_LONG).show();
                        user_phoneNo = "NA";
                        user_password = "NA";
                        updateView();

                    } else if (passwordFromDB.equals("NA")) {
                        Toast.makeText(UserProfile.this, "Please update your account with a password", Toast.LENGTH_LONG).show();
                        user_phoneNo = phoneNoFromDB;
                        user_password = "NA";
                        updateView();

                    } else if (phoneNoFromDB.equals("NA")) {
                        Toast.makeText(UserProfile.this, "Please update your account with a phone number", Toast.LENGTH_LONG).show();
                        user_password = passwordFromDB;
                        user_phoneNo = "NA";
                        updateView();

                    } else {
                        user_password = passwordFromDB;
                        user_phoneNo = phoneNoFromDB;
                        updateView();
                    }

                } else {

                    Toast.makeText(UserProfile.this, "Invalid Account! ERROR!", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getAllData() {

        Intent intent = getIntent();

        SessionManager sessionManager = new SessionManager(this, SessionManager.SESSION_USER_SESSION);
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();

        user_name = userData.get(SessionManager.KEY_FULL_NAME);
        user_username = userData.get(SessionManager.KEY_USERNAME);
        user_email = userData.get(SessionManager.KEY_EMAIL);
        user_phoneNo = userData.get(SessionManager.KEY_PHONE_NUMBER);
        user_password = userData.get(SessionManager.KEY_PASSWORD);

//        user_name = intent.getStringExtra("name");
//        user_username = intent.getStringExtra("username");
//        user_phoneNo = intent.getStringExtra("phoneNo");
//        user_email = intent.getStringExtra("email");
//        user_password = intent.getStringExtra("password");

        updateView();

    }

    public void updateView() {
        fullNameLabel.setText(user_name);
        usernameLabel.setText(user_username);
        Objects.requireNonNull(fullName.getEditText()).setText(user_name);
        Objects.requireNonNull(username.getEditText()).setText(user_username);
        Objects.requireNonNull(phoneNo.getEditText()).setText(user_phoneNo);
        Objects.requireNonNull(email.getEditText()).setText(user_email);
        Objects.requireNonNull(password.getEditText()).setText(user_password);

        sessionManager.createLoginSession(user_name, user_username, user_email, user_phoneNo, user_password);
    }


    public void update(View view) {

        refreshFields();

        if (whatToDo.equals("fromGoogle")) {

            onGoogleSignIn();
//            Toast.makeText(this, "Cannot modify when signed in through Google Account", Toast.LENGTH_LONG).show();
            return;
        }

        flag = 0;

        if (isNameChanged() || isUsernameChanged() || isPhoneNoChanged() || isEmailChanged() || isPasswordChanged()) {
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();

        } else {
            if (flag == 0) {
                refreshFields();
                Toast.makeText(this, "No change in data", Toast.LENGTH_SHORT).show();
            }


        }

        updateView();

    }

    private void onGoogleSignIn() {

        if (isNameChanged_Google() | isUsernameChanged_Google() | isPhoneNoChanged_Google() | isEmailChanged_Google() | isPasswordChanged_Google()) {
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();

        } else {
            if (flag == 0) {
                refreshFields();
                Toast.makeText(this, "No change in data", Toast.LENGTH_SHORT).show();
            }


        }

        updateView();
//
//        String val1 = Objects.requireNonNull(password.getEditText()).getText().toString();
//        String val2 = Objects.requireNonNull(phoneNo.getEditText()).getText().toString();

//        if(!val1.isEmpty()) {
//
//            if (setPassword()) {
//                reference.child(user_username).child("password").setValue(Objects.requireNonNull(password.getEditText()).getText().toString());
//                Toast.makeText(this, "Password successfully set", Toast.LENGTH_SHORT).show();
//            }
//        }
//        else
//        {
//            reference.child(user_username).child("password").removeValue();
//            Toast.makeText(this, "Password removed", Toast.LENGTH_SHORT).show();
//        }
//        if(!val2.isEmpty()) {
//
//            if (setPhoneNo()) {
//                reference.child(user_username).child("phoneNo").setValue(Objects.requireNonNull(password.getEditText()).getText().toString());
//                Toast.makeText(this, "Phone Number successfully set", Toast.LENGTH_SHORT).show();
//            }
//        }
//        else
//        {
//            reference.child(user_username).child("phoneNo").removeValue();
//            Toast.makeText(this, "Phone number removed", Toast.LENGTH_SHORT).show();
//        }

    }


    private boolean isNameChanged_Google() {

        if (!user_name.equals(Objects.requireNonNull(fullName.getEditText()).getText().toString())) {

            Toast.makeText(this, "Name cannot be changed", Toast.LENGTH_SHORT).show();
            flag = 1;

        }
        return false;

    }

    private boolean isUsernameChanged_Google() {

        if (!user_username.equals(Objects.requireNonNull(username.getEditText()).getText().toString())) {

            Toast.makeText(this, "Username cannot be changed", Toast.LENGTH_SHORT).show();
            flag = 1;

        }
        return false;

    }

    private boolean isPhoneNoChanged_Google() {

        if (!user_phoneNo.equals(Objects.requireNonNull(phoneNo.getEditText()).getText().toString())) {
            if (setPhoneNo()) {
                reference.child(user_username).child("phoneNo").setValue(Objects.requireNonNull(phoneNo.getEditText()).getText().toString());
                user_phoneNo = phoneNo.getEditText().getText().toString();

            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                flag = 1;
                return false;
            }
            return true;
        } else {
            return false;
        }

    }

    private boolean isEmailChanged_Google() {

        if (!user_email.equals(Objects.requireNonNull(email.getEditText()).getText().toString())) {

            Toast.makeText(this, "Email Id cannot be changed", Toast.LENGTH_SHORT).show();
            flag = 1;

        }
        return false;
    }

    private boolean isPasswordChanged_Google() {

        if (!user_password.equals(Objects.requireNonNull(password.getEditText()).getText().toString())) {
            if (setPassword()) {
                reference.child(user_username).child("password").setValue(Objects.requireNonNull(password.getEditText()).getText().toString());
                user_password = password.getEditText().getText().toString();
            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                flag = 1;
                return false;
            }
            return true;
        } else {
            return false;
        }

    }


    private Boolean setPhoneNo() {

        String val = Objects.requireNonNull(phoneNo.getEditText()).getText().toString();

        if (val.isEmpty()) {
            phoneNo.setError("Field cannot be empty");
            return false;
        } else {
            phoneNo.setError(null);
            phoneNo.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean setPassword() {

        String val = Objects.requireNonNull(password.getEditText()).getText().toString();

        String passCode = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passCode)) {
            password.setError("Password too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }


    // refresh all the errors in all the fields
    private void refreshFields() {

        fullName.setError(null);
        phoneNo.setError(null);
        email.setError(null);
        password.setError(null);
        fullName.setErrorEnabled(false);
        phoneNo.setErrorEnabled(false);
        email.setErrorEnabled(false);
        password.setErrorEnabled(false);

    }


    // if changed then updates the value and gives required feedback

    private boolean isPasswordChanged() {

        if (!user_password.equals(Objects.requireNonNull(password.getEditText()).getText().toString())) {
            if (validatePassword()) {
                reference.child(user_username).child("password").setValue(Objects.requireNonNull(password.getEditText()).getText().toString());
                user_password = password.getEditText().getText().toString();
            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                flag = 1;
                return false;
            }
            return true;
        } else {
            return false;
        }

    }

    private boolean isEmailChanged() {

        if (!user_email.equals(Objects.requireNonNull(email.getEditText()).getText().toString())) {
            if (validateEmail()) {
                reference.child(user_username).child("email").setValue(Objects.requireNonNull(email.getEditText()).getText().toString());
                user_email = email.getEditText().getText().toString();

            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        } else {
            return false;
        }

    }

    private boolean isPhoneNoChanged() {

        if (!user_phoneNo.equals(Objects.requireNonNull(phoneNo.getEditText()).getText().toString())) {
            if (validatePhoneNo()) {
                reference.child(user_username).child("phoneNo").setValue(Objects.requireNonNull(phoneNo.getEditText()).getText().toString());
                user_phoneNo = phoneNo.getEditText().getText().toString();

            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                flag = 1;
                return false;
            }
            return true;
        } else {
            return false;
        }

    }

    private boolean isUsernameChanged() {

        if (!user_username.equals(Objects.requireNonNull(username.getEditText()).getText().toString())) {

            Toast.makeText(this, "Username cannot be changed", Toast.LENGTH_SHORT).show();
            flag = 1;

        }
        return false;

    }

    private boolean isNameChanged() {

        if (!user_name.equals(Objects.requireNonNull(fullName.getEditText()).getText().toString())) {
            if (validateName()) {
                reference.child(user_username).child("name").setValue(fullName.getEditText().getText().toString());
                user_name = fullName.getEditText().getText().toString();

            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show();
                flag = 1;
                return false;
            }
            return true;
        } else {
            return false;
        }

    }


    // check the validity of the changed inputs in various fields

    private Boolean validateName() {

        String val = Objects.requireNonNull(fullName.getEditText()).getText().toString();

        if (val.isEmpty()) {
            fullName.setError("Field cannot be empty");
            return false;
        } else {
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateUsername() {

        String val = Objects.requireNonNull(username.getEditText()).getText().toString();
        String noWhiteSpaces = "\\A\\w{4,20}\\z";

        if (val.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 15) {
            username.setError("Username too long");
            return false;
        } else if (!val.matches(noWhiteSpaces)) {
            username.setError("White spaces are not allowed");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePhoneNo() {

        String val = Objects.requireNonNull(phoneNo.getEditText()).getText().toString();

        if (val.isEmpty()) {
            phoneNo.setError("Field cannot be empty");
            return false;
        } else {
            phoneNo.setError(null);
            return true;
        }

    }

    private Boolean validateEmail() {

        String val = Objects.requireNonNull(email.getEditText()).getText().toString();
        String validEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(validEmail)) {
            email.setError("Enter a valid Email Address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validatePassword() {

        String val = Objects.requireNonNull(password.getEditText()).getText().toString();
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
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passCode)) {
            password.setError("Password too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }


    // on pressing logout button

    public void logout(View view) {

        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USER_SESSION);
        sessionManager.logoutUserFromSession();

        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();

    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), Dashboard.class));

        super.onBackPressed();
    }
}