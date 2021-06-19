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
    private TextView txtFName;
    private TextView txtLName;
    private TextView txtPhone;
    private Authen authen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Context context = getApplicationContext();

        Button btnRegister = findViewById(R.id.btnRegister);
        txtFName = findViewById(R.id.txtfName);
        txtLName = findViewById(R.id.txtlName);
        txtPhone = findViewById(R.id.txtPhone);
        authen = new Authen(this);

//        btnRegister.setOnClickListener(new View.OnClickListener() {
//            String FName = txtFName.getText().toString();
//            String LName = txtLName.getText().toString();
//            String phone = txtPhone.getText().toString().replaceFirst("0","+84");
//            @Override
//            public void onClick(View v) {
//                if(phone!=null||!phone.isEmpty()||FName!=null|| !FName.isEmpty()
//                        ||LName!=null|| !LName.isEmpty()){
//
//                    //get info,send sms and transfer phone to verify
//                    Intent intent = new Intent(context,VerifySMSToken.class);
//                    intent.putExtra("phone",phone);
//                    intent.putExtra("FName",FName);
//                    intent.putExtra("LName",LName);
//
////
//
//                    startActivity(intent);
//                }else{
//                    Toast toast = Toast.makeText(context,"All field is required."
//                            ,Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fName = txtFName.getText().toString();
                String lName = txtLName.getText().toString();
                String phone = txtPhone.getText().toString().replaceFirst("0","+84");
                if (checkInput(fName) && checkInput(lName) && checkInput(phone)) {
                    Intent intent = new Intent(Register.this, VerifySMSToken.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("FName", fName);
                    intent.putExtra("LName", lName);
                    authen.sendVerificationCode(phone, null);
                    startActivity(intent);
                }else
                     Toast.makeText(context,"All field is required."
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