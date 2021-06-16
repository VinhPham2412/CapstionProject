package com.example.capstionproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class Register extends AppCompatActivity {
    private boolean check=true;
    private TextView txtUsername;
    private TextView txtPhone;
    private TextView txtPassword;
    private TextView txtConfirm;
    private TextView txtCode;
    FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    Context context = getApplicationContext();
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser user = task.getResult().getUser();
                        Intent intent = new Intent(Register.this,MainActivity.class);
                        intent.putExtra("user",user);
                        startActivity(intent);
                    } else {
                        // Sign in failed, display a message and update the UI
                        Toast toast = Toast.makeText(context,"Something wrong, please login",Toast.LENGTH_SHORT);
                        toast.show();
                        Intent intent = new Intent(Register.this,Login.class);
                        startActivity(intent);
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast toast2 = Toast.makeText(context,"Invalid code",Toast.LENGTH_SHORT);
                            toast2.show();
                        }
                    }
                });
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        Button btnRegister = findViewById(R.id.btnRegister);
        txtUsername = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirm = findViewById(R.id.txtPassword2);
        txtCode = findViewById(R.id.txtCode);
        Button btnResend = findViewById(R.id.btnSendCode);
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Context context = getApplicationContext();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast toast = Toast.makeText(context,"Try again later.",Toast.LENGTH_SHORT);
                    toast.show();
                }
                // Show a message and update the UI
                Toast toast = Toast.makeText(context,"Something wrong, please check phone number and try again.",Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

            }
        };
        btnRegister.setOnClickListener(new View.OnClickListener() {
            String username;
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                if(txtPassword.getText().toString().equals(txtConfirm.getText().toString())){
                    //get info
                    username= txtUsername.getText().toString();
                    if(txtCode.getText()!=null && mVerificationId!=null){
                        verifyPhoneNumberWithCode(mVerificationId,txtCode.getText().toString());
                    }else{
                        Toast toast = Toast.makeText(context,"Invalid code.",Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }else{
                    Toast toast = Toast.makeText(context,"Confirm must match password",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
        btnResend.setOnClickListener(v -> {
            String phone = txtPhone.getText().toString();
            phone = phone.replaceFirst("0","+84");
            if(mResendToken == null){
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(phone)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(Register.this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }else{
                resendVerificationCode(phone,mResendToken);
            }
        });
    }
    private void checkInput(EditText editText){
        if(editText!=null)
            check=true;
        else
            check=false;

    }

}