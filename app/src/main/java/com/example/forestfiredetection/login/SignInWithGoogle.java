package com.example.forestfiredetection.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.forestfiredetection.R;
import com.example.forestfiredetection.UserProfile;
import com.example.forestfiredetection.database.SessionManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignInWithGoogle extends AppCompatActivity {

    DatabaseReference reference;

    String full_name, user_name, email_id, mode;

    ProgressBar progressBar;

    String nameFromDB, usernameFromDB, phoneNoFromDB, emailFromDB, passwordFromDB;

    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user != null)
//        {
//            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
//            startActivity(intent);
//        }
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_google);

        progressBar = findViewById(R.id.ProgressBar);
        progressBar.setVisibility(View.INVISIBLE);

        mode = "fromGoogle";

        reference = FirebaseDatabase.getInstance().getReference("users");

        mAuth = FirebaseAuth.getInstance();

        createRequest();
    }

    private void createRequest() {

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
//            startActivity(intent);
//        }
//    }


    // on sign button clicked
    public void onGoogleSignIn(View view) {

        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USER_SESSION);
        if (sessionManager.checkLogin()) {
            Toast.makeText(this, "Already logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), UserProfile.class));
            finish();
        } else {
            signIn();
        }

    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            progressBar.setVisibility(View.VISIBLE);

                            GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(SignInWithGoogle.this);
                            if (signInAccount != null) {
                                full_name = signInAccount.getDisplayName();
                                user_name = signInAccount.getGivenName();
                                email_id = signInAccount.getEmail();
//                                assert user_name != null;

                                checkUsername();

//                                reference.child(user_name).child("name").setValue(full_name);
//                                reference.child(user_name).child("username").setValue(user_name);
//                                reference.child(user_name).child("email").setValue(email_id);

//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//
//                                        checkPhoneNo();
//                                        checkPassword();
//
//                                    }
//                                }, 2000);

//
//                                reference.child(user_name).child("password").setValue("");
//                                reference.child(user_name).child("phoneNo").setValue("");
                            }

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
//                            intent.putExtra("whatToDo", "fromGoogle");

                            startActivity(intent);
                            finish();

                                }
                            }, 2000);


                        } else {
                            Toast.makeText(SignInWithGoogle.this, "Sorry authorization failed.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), SignInWithGoogle.class));

                            progressBar.setVisibility(View.INVISIBLE);

                        }

                    }
                });
    }

    private void checkUsername() {

//        Query checkUser = reference.orderByChild("username").equalTo(user_name);
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if ((snapshot.exists())) {
//
//                    Toast.makeText(SignInWithGoogle.this, "Account exists", Toast.LENGTH_SHORT).show();
//                    //                    reference.child(user_name).child("phoneNo").setValue("");
////
////                    passwordFromDB = snapshot.child(user_name).child("password").getValue(String.class);
////
////                    password.setError(null);
////                    password.setErrorEnabled(false);
////
////                    nameFromDB = snapshot.child(user_username).child("name").getValue(String.class);
////                    usernameFromDB = snapshot.child(user_username).child("username").getValue(String.class);
////                    phoneNoFromDB = snapshot.child(user_username).child("phoneNo").getValue(String.class);
////                    emailFromDB = snapshot.child(user_username).child("email").getValue(String.class);
////
////                    if(phoneNoFromDB.isEmpty() && passwordFromDB.isEmpty())
////                    {
////                        Toast.makeText(UserProfile.this, "Please update your account with a phone no. and password", Toast.LENGTH_LONG).show();
////                    }
////                    else if(passwordFromDB.isEmpty())
////                    {
////                        Toast.makeText(UserProfile.this, "Please update your account with a password", Toast.LENGTH_LONG).show();
////
////                    }
////                    else if(phoneNoFromDB.isEmpty())
////                    {
////                        Toast.makeText(UserProfile.this, "Please update your account with a phone number", Toast.LENGTH_LONG).show();
////
////                    }
////                    else {
////                        user_password = passwordFromDB;
////                        user_phoneNo = phoneNoFromDB;
////                    }
//
//                } else {
//
//                    reference.child(user_name).child("password").setValue("NA");
//                    reference.child(user_name).child("phoneNo").setValue("NA");
//
////                    Toast.makeText(SignInWithGoogle.this, "Update your account with password", Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//                reference.child(user_name).child("password").setValue("NA");
//                reference.child(user_name).child("phoneNo").setValue("NA");
//
//            }
//        });

        Query checkUser = reference.orderByChild("username").equalTo(user_name);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if ((snapshot.exists())) {

                    Toast.makeText(SignInWithGoogle.this, "Signing in through Google account", Toast.LENGTH_SHORT).show();
                    SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USER_SESSION);
                    sessionManager.createLoginSession(full_name, user_name, mode);

//                    String passwordFromDB = snapshot.child(user_name).child("password").getValue(String.class);
//                    reference.child(user_name).child("password").setValue(passwordFromDB);
//                    String phoneNoFromDB = snapshot.child(user_name).child("phoneNo").getValue(String.class);
//                    reference.child(user_name).child("phoneNo").setValue(passwordFromDB);


//                        String nameFromDB = snapshot.child(userEnteredUsername).child("name").getValue(String.class);
//                        String usernameFromDB = snapshot.child(userEnteredUsername).child("username").getValue(String.class);
//                    String emailFromDB = snapshot.child(userEnteredUsername).child("email").getValue(String.class);

//                    Intent intent = new Intent(getApplicationContext(), UserProfile.class);
//                    intent.putExtra("name", nameFromDB);
//                    intent.putExtra("username", usernameFromDB);
//                    intent.putExtra("phoneNo", phoneNoFromDB);
//                    intent.putExtra("email", emailFromDB);
//                    intent.putExtra("password", passwordFromDB);
//                    intent.putExtra("whatToDo", "fromLogin");
//
//                    startActivity(intent);

                } else {
                    reference.child(user_name).child("name").setValue(full_name);
                    reference.child(user_name).child("username").setValue(user_name);
                    reference.child(user_name).child("email").setValue(email_id);
                    reference.child(user_name).child("password").setValue("NA");
                    reference.child(user_name).child("phoneNo").setValue("NA");

                    SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USER_SESSION);
                    sessionManager.createLoginSession(full_name, user_name, email_id, "NA", "NA", mode);

//
//                username.setError("No such User exists");
//                username.requestFocus();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

//    private void checkPassword() {
//
//        Query checkUser = reference.child(user_name).orderByChild("password").equalTo(null);
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if ((snapshot.exists())) {
//                    reference.child(user_name).child("password").setValue("NA");
//
//                    //                    reference.child(user_name).child("phoneNo").setValue("");
////
////                    passwordFromDB = snapshot.child(user_name).child("password").getValue(String.class);
////
////                    password.setError(null);
////                    password.setErrorEnabled(false);
////
////                    nameFromDB = snapshot.child(user_username).child("name").getValue(String.class);
////                    usernameFromDB = snapshot.child(user_username).child("username").getValue(String.class);
////                    phoneNoFromDB = snapshot.child(user_username).child("phoneNo").getValue(String.class);
////                    emailFromDB = snapshot.child(user_username).child("email").getValue(String.class);
////
////                    if(phoneNoFromDB.isEmpty() && passwordFromDB.isEmpty())
////                    {
////                        Toast.makeText(UserProfile.this, "Please update your account with a phone no. and password", Toast.LENGTH_LONG).show();
////                    }
////                    else if(passwordFromDB.isEmpty())
////                    {
////                        Toast.makeText(UserProfile.this, "Please update your account with a password", Toast.LENGTH_LONG).show();
////
////                    }
////                    else if(phoneNoFromDB.isEmpty())
////                    {
////                        Toast.makeText(UserProfile.this, "Please update your account with a phone number", Toast.LENGTH_LONG).show();
////
////                    }
////                    else {
////                        user_password = passwordFromDB;
////                        user_phoneNo = phoneNoFromDB;
////                    }
//
//                } else {
//
////                    Toast.makeText(SignInWithGoogle.this, "Update your account with password", Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//
//    }
//
//    private void checkPhoneNo() {
//
//        Query checkUser = reference.child(user_name).orderByChild("phoneNo").equalTo(null);
//        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if ((snapshot.exists())) {
//                    reference.child(user_name).child("phoneNo").setValue("NA");
//
//                    //                    reference.child(user_name).child("phoneNo").setValue("");
////
////                    passwordFromDB = snapshot.child(user_name).child("password").getValue(String.class);
////
////                    password.setError(null);
////                    password.setErrorEnabled(false);
////
////                    nameFromDB = snapshot.child(user_username).child("name").getValue(String.class);
////                    usernameFromDB = snapshot.child(user_username).child("username").getValue(String.class);
////                    phoneNoFromDB = snapshot.child(user_username).child("phoneNo").getValue(String.class);
////                    emailFromDB = snapshot.child(user_username).child("email").getValue(String.class);
////
////                    if(phoneNoFromDB.isEmpty() && passwordFromDB.isEmpty())
////                    {
////                        Toast.makeText(UserProfile.this, "Please update your account with a phone no. and password", Toast.LENGTH_LONG).show();
////                    }
////                    else if(passwordFromDB.isEmpty())
////                    {
////                        Toast.makeText(UserProfile.this, "Please update your account with a password", Toast.LENGTH_LONG).show();
////
////                    }
////                    else if(phoneNoFromDB.isEmpty())
////                    {
////                        Toast.makeText(UserProfile.this, "Please update your account with a phone number", Toast.LENGTH_LONG).show();
////
////                    }
////                    else {
////                        user_password = passwordFromDB;
////                        user_phoneNo = phoneNoFromDB;
////                    }
//
//                } else {
//
////                    Toast.makeText(SignInWithGoogle.this, "Update your account with phone number", Toast.LENGTH_LONG).show();
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }


    public void callBackScreenFromGoogleSignIn(View view) {

        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();

    }

    public void onFacebook(View view) {
    }

    public void onGooglePlus(View view) {
    }

    public void onTwitter(View view) {
    }
}


