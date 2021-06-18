package com.example.capstionproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private Button btnRegister,btnLogin;
    private TextView phone;
    Authen authen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnRegister=findViewById(R.id.btnLogin2);
        phone = findViewById(R.id.txtLoginAccount);
        authen = new Authen(this);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });
        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phone.getText().toString().replaceFirst("0","+84");
                authen.sendVerificationCode(phoneNumber,null);
                Intent intent=new Intent(Login.this,VerifySMSToken.class);
                startActivity(intent);
            }
        });
    }
}