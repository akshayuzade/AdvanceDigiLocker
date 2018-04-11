package com.example.hakathon.advaceddigi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity {

    Button btnNext;
    String loginusername,loginpassword;
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_act);
        getSupportActionBar().hide();
        SharedPreferences prefs = getSharedPreferences("digilocker", MODE_PRIVATE);
            loginusername = prefs.getString("loginusername","xx");
           loginpassword = prefs.getString("loginpassword","xx");

        SharedPreferences prefs2 = getSharedPreferences("digilockeruserprofile", MODE_PRIVATE);
        final String mobilestatus = prefs2.getString("mobilenostatus","false");
        final String emailstatus = prefs2.getString("emailstatus","false");
        final String adharstats= prefs2.getString("adharnostatus","false");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if(loginusername.equals("xx") && loginpassword.equals("xx")) {
                    startActivity(new Intent(FirstActivity.this, UserLogin.class));

                }else {
                    if (mobilestatus.equals("false") || emailstatus.equals("false") || adharstats.equals("false")) {
                        startActivity(new Intent(FirstActivity.this, VerificationActivity.class));
                    }
                else {
                        startActivity(new Intent(FirstActivity.this, DashBoard.class));
                    }
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }







}
