package com.example.hakathon.advaceddigi;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DrivingLicense extends AppCompatActivity {
    String username;
    EditText etdllicenseno;
    DatePicker dpdldob;
    TextView tvdlname, tvdladdress, tvdldoi, tvdldoe;
    Button btndlverify, btndlsubmit;
    String strdlno, strdob;
   // TextView dlpic;
    final static String DL_VERIFY_URL = "http://192.168.43.44/digilockerr/Vehicle/dlverify.php";
    final static String DL_SUBMIT_URL = "http://192.168.43.44/digilockerr/verified/dlsubmit.php";
    String IMAGE_URL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_license);

        SharedPreferences prefs = getSharedPreferences("digilocker", MODE_PRIVATE);
        username = prefs.getString("loginusername","");
        etdllicenseno = (EditText) findViewById(R.id.etdllicenseno);
        dpdldob = (DatePicker) findViewById(R.id.dpdldob);
        //dlpic = (ImageView) findViewById(R.id.imageView);

        tvdlname = (TextView) findViewById(R.id.tvdlname);
        tvdladdress = (TextView) findViewById(R.id.tvdladdress);
        tvdldoi = (TextView) findViewById(R.id.tvdldoi);
        tvdldoe = (TextView) findViewById(R.id.tvdldoe);

        btndlverify = (Button) findViewById(R.id.btndlverify);
        btndlsubmit = (Button) findViewById(R.id.btndlsubmit);
        getdate();

        btndlverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strdlno = etdllicenseno.getText().toString();
                dlverify();
            }
        });
        btndlsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvdlname.equals("..............")) {
                    Toast.makeText(DrivingLicense.this, "Verify Driving Licence", Toast.LENGTH_SHORT).show();
                }else{
                    dlsucceess();
                }
            }
        });

    }

    void getdate(){
        int day = dpdldob.getDayOfMonth();
        int month = dpdldob.getMonth() + 1;
        int year = dpdldob.getYear();
        strdob= day+"-"+month+"-"+year ;
    }

    public void dlverify() {
        Toast.makeText(DrivingLicense.this, strdlno+"---"+strdob, Toast.LENGTH_SHORT).show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DL_VERIFY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray result = jsonobject.getJSONArray("result");
                            JSONObject data = result.getJSONObject(0);
                            tvdlname.setText(data.getString("name"));
                            tvdladdress.setText(data.getString("address"));
                            tvdldoi.setText(data.getString("dateofissuee"));
                            tvdldoe.setText(data.getString("date_of_expiry"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DrivingLicense.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("dlno", strdlno);
                params.put("dateofbirth", strdob);
                return params;
            }
        };
        MySingleton.getInstance(DrivingLicense.this).addtorequestQueue(stringRequest);
    }


    public void dlsucceess() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, DL_SUBMIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(DrivingLicense.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DrivingLicense.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("dlno", strdlno);
                        params.put("dateofbirth", strdob);
                        params.put("username", username);
                        return params;
                    }
                };
        MySingleton.getInstance(DrivingLicense.this).addtorequestQueue(stringRequest);
    }





}
