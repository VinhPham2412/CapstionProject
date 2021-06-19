package com.example.capstionproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

public class Register extends AppCompatActivity {
    private EditText txtFName;
    private EditText txtLName;
    private EditText txtPhone;
    private Button btnRegister;
    private Authen authen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Context context = getApplicationContext();

        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtFName = (EditText) findViewById(R.id.txtFirstName);
        txtLName = (EditText)findViewById(R.id.txtLastName);
        txtPhone = (EditText)findViewById(R.id.txtPhoneNumber);
        authen = new Authen(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            String FName = txtFName.getText().toString();
            String LName = txtLName.getText().toString();
            String phone = txtPhone.getText().toString();
            @Override
            public void onClick(View v) {
                if(phone==null||phone.isEmpty()){
                    Toast toast = Toast.makeText(context,"All field is required."
                            ,Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    //get info,send sms and transfer phone to verify
                    Intent intent = new Intent(context,VerifySMSToken.class);
                    phone = phone.replaceFirst("0","+84");
                    intent.putExtra("phone",phone);
                    intent.putExtra("FName",FName);
                    intent.putExtra("LName",LName);

                    authen.sendVerificationCode(phone,null);
                    Log.d(TAG, "onCodeSent:" + phone);
                    startActivity(intent);
                }
            }
        });

    }

}