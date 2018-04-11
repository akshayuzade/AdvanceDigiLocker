package com.example.hakathon.advaceddigi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FeedBack extends AppCompatActivity {
String username;
    TextView tvfeedbackusername;
    EditText etfeedbacktext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        SharedPreferences prefs = getSharedPreferences("digilockeruserprofile", MODE_PRIVATE);
        username = prefs.getString("name","");

        etfeedbacktext=(EditText)findViewById(R.id.etfeedbacktext);
        tvfeedbackusername=(TextView)findViewById(R.id.tvfeedbackusername);
        tvfeedbackusername.setText(username);
    }

    @SuppressLint("LongLogTag")
    public void sendfeedback(View view) {

        Log.i("Send email", "");
        String[] TO = {"thedreambuilder6@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback From "+username);
        emailIntent.putExtra(Intent.EXTRA_TEXT, etfeedbacktext.getText().toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FeedBack.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    }

