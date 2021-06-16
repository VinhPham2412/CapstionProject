package com.example.capstionproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {
    private Button btnRegister;
    private TextView txtUsername;
    private TextView txtPhone;
    private TextView txtPassword;
    private TextView txtConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister = findViewById(R.id.btnRegister);
        txtUsername = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirm = findViewById(R.id.txtPassword2);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtPassword.getText().toString().equals(txtConfirm.getText().toString())){
                    String username = txtUsername.getText().toString();
                    String phone = txtPhone.getText().toString();
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber(phoneNumber)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(this)                 // Activity (for callback binding)
                                    .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }else{
                    Context context = getApplicationContext();
                    Toast toast = Toast.makeText(context,"Confirm must match password",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}