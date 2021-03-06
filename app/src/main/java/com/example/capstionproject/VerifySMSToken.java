package com.example.capstionproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class VerifySMSToken extends AppCompatActivity {
    private TextView txtPhone,txtResend;
    private TextView num1,num2,num3,num4,num5,num6;
    private Button btnVerify;
    private ProgressBar progressBar;
    private String phone;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            String fname = getIntent().getStringExtra("FName");
                            String lname = getIntent().getStringExtra("LName");
                            FirebaseUser user = task.getResult().getUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fname+" "+lname)
                                    .build();
                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                            }
                                        }
                                    });
                            Intent intent = new Intent(VerifySMSToken.this,MainActivity.class);
                            // Update UI
                            startActivity(intent);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                progressBar.setVisibility(View.GONE);
                                btnVerify.setVisibility(View.VISIBLE);
                                // The verification code entered was invalid
                                Toast.makeText(VerifySMSToken.this,"Invalid verification code",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void resendVerificationCode(String phoneNumber,
                                       PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_s_m_s_token);
        txtPhone = findViewById(R.id.textMobile);
        txtResend = findViewById(R.id.textResendOTP);
        btnVerify = findViewById(R.id.btnVerify);
        progressBar = findViewById(R.id.progressBar3);
        mAuth = FirebaseAuth.getInstance();

        phone = getIntent().getStringExtra("phone");
        txtPhone.setText(phone);

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {


                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(VerifySMSToken.this,"Invalid request",Toast.LENGTH_SHORT).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(VerifySMSToken.this,"The SMS quota for the project has been exceeded",Toast.LENGTH_SHORT).show();
                }
                return;
                // Show a message and update the UI
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };

        num1 = findViewById(R.id.inputCode1);
        num2 = findViewById(R.id.inputCode2);
        num3 = findViewById(R.id.inputCode3);
        num4 = findViewById(R.id.inputCode4);
        num5 = findViewById(R.id.inputCode5);
        num6 = findViewById(R.id.inputCode6);
        num1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(num1.getText().toString().length()==1)     //size as per your requirement
                {
                    num2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        num2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(num2.getText().toString().length()==1)     //size as per your requirement
                {
                    num3.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        num3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(num3.getText().toString().length()==1)     //size as per your requirement
                {
                    num4.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        num4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(num4.getText().toString().length()==1)     //size as per your requirement
                {
                    num5.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
        num5.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(num5.getText().toString().length()==1)     //size as per your requirement
                {
                    num6.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        String FName = getIntent().getStringExtra("FName");
        String LName = getIntent().getStringExtra("LName");

        txtResend.setOnClickListener(v -> {
            resendVerificationCode(phone,mResendToken);
            Log.w(TAG, "On resend press 11111111111111111111"+mResendToken);
        });

        btnVerify.setOnClickListener(v -> {
            if(num1.getText().toString().trim().isEmpty()
            ||num2.getText().toString().trim().isEmpty()
                    ||num3.getText().toString().trim().isEmpty()
                    ||num4.getText().toString().trim().isEmpty()
                    ||num5.getText().toString().trim().isEmpty()
                    ||num6.getText().toString().trim().isEmpty()) {
                Toast.makeText(VerifySMSToken.this, "Please enter valid code", Toast.LENGTH_SHORT).show();
                return;
            }
            String code = num1.getText().toString();
            code += num2.getText().toString();
            code += num3.getText().toString();
            code += num4.getText().toString();
            code += num5.getText().toString();
            code += num6.getText().toString();
            progressBar.setVisibility(View.VISIBLE);
            btnVerify.setVisibility(View.GONE);
            mVerificationId = getIntent().getStringExtra("verificationId");
            verifyPhoneNumberWithCode(mVerificationId,code);
        });

    }
}