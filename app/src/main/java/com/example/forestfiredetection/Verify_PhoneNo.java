package com.example.forestfiredetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.forestfiredetection.forgotpassword.SetNewPassword;
import com.example.forestfiredetection.helperclass.UserHelperClass;
import com.example.forestfiredetection.login.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Verify_PhoneNo extends AppCompatActivity {

    String verificationCodeBySystem;

    Button verify_button;
    ProgressBar progressBar;

    String _name, _username, _email, _phoneNo, _password;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    PinView pinView;

    String whatToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify__phone_no);

        verify_button = findViewById(R.id.verify_btn);
        progressBar = findViewById(R.id.progress_bar);

        pinView = findViewById(R.id.pin_view);

        progressBar.setVisibility(View.GONE);

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("users");

         _name = getIntent().getStringExtra("name");
         _username = getIntent().getStringExtra("username");
         _email = getIntent().getStringExtra("email");
        _phoneNo = getIntent().getStringExtra("phoneNo");
         _password = getIntent().getStringExtra("password");
         whatToDo = getIntent().getStringExtra("whatToDo");

        sendVerificationCodeToUser(_phoneNo);


    }

    private void sendVerificationCodeToUser(String phoneNo) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationCodeBySystem = s;

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


            String code = phoneAuthCredential.getSmsCode();
            if ((code != null)) {
                pinView.setText(code);
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(Verify_PhoneNo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void verifyCode(String codeByUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);

    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(Verify_PhoneNo.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            if(whatToDo.equals("signUp"))
                            {
                                Toast.makeText(Verify_PhoneNo.this, "Your Account has been created successfully!", Toast.LENGTH_LONG).show();

                                UserHelperClass helperClass = new UserHelperClass(_name, _username, _email, _phoneNo, _password);

                                reference.child(_username).setValue(helperClass);

                                //Perform Your required action here to either let the user sign In or do something required
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                            else if(whatToDo.equals("updatePassword"))
                            {
                                updateUserPassword();
                            }



                        } else {
                            Toast.makeText(Verify_PhoneNo.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void updateUserPassword() {

        Intent intent = new Intent(getApplicationContext(), SetNewPassword.class);
        intent.putExtra("username", _username);
        startActivity(intent);
        finish();

    }

    public void onClickVerify(View view) {

        String code = pinView.getText().toString();

        if (code.isEmpty() || code.length() < 6) {
            pinView.setError("Wrong OTP...");
            pinView.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        verifyCode(code);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(this, "pressed", Toast.LENGTH_SHORT).show();

    }
}