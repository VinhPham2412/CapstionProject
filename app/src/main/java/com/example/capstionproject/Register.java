package com.example.capstionproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class Register extends AppCompatActivity {
    private TextView txtFName;
    private TextView txtLName;
    private TextView txtPhone;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String lName,fName,phone;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button btnRegister = findViewById(R.id.btnRegister);
        txtFName = findViewById(R.id.txtfName);
        txtLName = findViewById(R.id.txtlName);
        txtPhone = findViewById(R.id.txtPhone);
        progressBar = findViewById(R.id.progressBar2);
        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID
                Intent intent = new Intent(Register.this, VerifySMSToken.class);

                intent.putExtra("phone", phone);
                intent.putExtra("FName", fName);
                intent.putExtra("LName", lName);
                intent.putExtra("verificationId",verificationId);
                startActivity(intent);
            }

            @Override
            public void onVerificationCompleted(@NonNull @NotNull PhoneAuthCredential phoneAuthCredential) {
                btnRegister.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onVerificationFailed(@NonNull @NotNull FirebaseException e) {
                btnRegister.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        };

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRegister.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                fName = txtFName.getText().toString();
                lName = txtLName.getText().toString();
                phone = txtPhone.getText().toString();
                if (checkInput(fName) && checkInput(lName) && checkInput(phone)) {
                    //send code
                    phone = txtPhone.getText().toString().replaceFirst("0","+84");
                    //send OTP
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(phone)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(Register.this)                 // Activity (for callback binding)
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }else
                     Toast.makeText(getApplicationContext(),"All field is required."
                           ,Toast.LENGTH_SHORT).show();
            }
        });

    }
    private boolean checkInput(String string){
        if(string.isEmpty()||string==null)
            return false;
        return true;
    }

}