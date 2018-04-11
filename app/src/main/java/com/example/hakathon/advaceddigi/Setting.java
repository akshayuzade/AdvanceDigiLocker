package com.example.hakathon.advaceddigi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void ChangePassword(View view) {
        Intent i=new Intent(Setting.this,ChangePassword.class);
        startActivity(i);
    }
}
