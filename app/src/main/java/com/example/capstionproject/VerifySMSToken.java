package com.example.capstionproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifySMSToken extends AppCompatActivity {
    private TextView txtPhone,txtResend;
    private TextView num1,num2,num3,num4,num5,num6;
    private Button btnVerify;
    private String phone;
    private Authen auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_s_m_s_token);

        auth = new Authen(this);
        txtPhone = findViewById(R.id.textMobile);
        txtResend = findViewById(R.id.textResendOTP);
        btnVerify = findViewById(R.id.btnVerify);

        num1 = findViewById(R.id.inputCode1);
        num2 = findViewById(R.id.inputCode2);
        num3 = findViewById(R.id.inputCode3);
        num4 = findViewById(R.id.inputCode4);
        num5 = findViewById(R.id.inputCode5);
        num6 = findViewById(R.id.inputCode6);

        phone = getIntent().getStringExtra("phone");
        txtPhone.setText(phone);

        String FName = getIntent().getStringExtra("FName");
        String LName = getIntent().getStringExtra("LName");

        txtResend.setOnClickListener(v -> {
            phone = phone.replaceFirst("0","+84");
            auth.sendVerificationCode(phone,auth.getmResendToken());
        });

        btnVerify.setOnClickListener(v -> {
            String code = num1.getText().toString();
            code += num2.getText().toString();
            code += num3.getText().toString();
            code += num4.getText().toString();
            code += num5.getText().toString();
            code += num6.getText().toString();

            auth.verifyPhoneNumberWithCode(auth.getmVerificationId(),code);
        });

    }
}