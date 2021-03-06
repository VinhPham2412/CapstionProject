package com.example.capstionproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class Login extends AppCompatActivity {
    private Button btnLogin;
    private TextView phone;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone = findViewById(R.id.txtLoginAccount);
        final ProgressBar progressBar=findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID
                Intent intent = new Intent(Login.this, VerifySMSToken.class);

                intent.putExtra("phone", phoneNumber);
                intent.putExtra("verificationId",verificationId);
                startActivity(intent);
            }

            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
            }
        };
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (phone.getText().toString().trim().isEmpty()){
                    Toast.makeText(Login.this,"Enter phone number",Toast.LENGTH_SHORT).show();
                    return;
                }
                phoneNumber = phone.getText().toString().replaceFirst("0","+84");
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);

                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(phoneNumber)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(Login.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    }
}