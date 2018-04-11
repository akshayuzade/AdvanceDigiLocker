package com.example.hakathon.advaceddigi;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Insurance extends AppCompatActivity {
    String username;
    TextView tvinsname,tvinsaddress,tvinsdoi,tvinsdoe,tvinsinsurancetype;
    Button btninsverify,btninssubmit;
    EditText etinspolicyno,etinsvehiclenocode;
    String strpolicyno,strinsvehiclenocode;
    final static String INSU_VERIFY_URL = "http://192.168.43.44/digilockerr/Vehicle/INSURANCEverify.php";
    final static String INSU_SUBMIT_URL = "http://192.168.43.44/digilockerr/verified/INSURANCEsubmit.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);

        SharedPreferences prefs = getSharedPreferences("digilocker", MODE_PRIVATE);
        username = prefs.getString("loginusername","");
        tvinsname =(TextView)findViewById(R.id.tvinsname);
        tvinsaddress =(TextView)findViewById(R.id.tvinsaddress);
        tvinsdoi =(TextView)findViewById(R.id.tvinsdoi);
        tvinsdoe =(TextView)findViewById(R.id.tvinsdoe);


        btninsverify=(Button)findViewById(R.id.btninsverify);
        btninssubmit=(Button)findViewById(R.id.btninssubmit);

        etinsvehiclenocode=(EditText)findViewById(R.id.etinsinsuredcode);
        etinspolicyno=(EditText)findViewById(R.id.etinspolicyno);


        btninsverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strpolicyno = etinspolicyno.getText().toString();
                strinsvehiclenocode=etinsvehiclenocode.getText().toString();
                insuranceverify();
            }
        });

        btninssubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvinsname.equals("..............")) {
                    Toast.makeText(Insurance.this, "Verify Insurance", Toast.LENGTH_SHORT).show();
                }else{
                    insurancesuccess();
                }
            }
        });
    }

    public void insuranceverify() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSU_VERIFY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray result = jsonobject.getJSONArray("result");
                            JSONObject data = result.getJSONObject(0);
                            tvinsname.setText(data.getString("name"));
                            tvinsaddress.setText(data.getString("address"));
                            tvinsdoi.setText(data.getString("dateofissuee"));
                            tvinsdoe.setText(data.getString("date_of_expiry"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Insurance.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("policyno", strpolicyno);
                params.put("vehicleno", strinsvehiclenocode);
                return params;
            }
        };
        MySingleton.getInstance(Insurance.this).addtorequestQueue(stringRequest);
    }


    public void insurancesuccess() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSU_SUBMIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Insurance.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Insurance.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("policyno", strpolicyno);
                params.put("vehicleno", strinsvehiclenocode);
                params.put("username", username);
                return params;
            }
        };
        MySingleton.getInstance(Insurance.this).addtorequestQueue(stringRequest);
    }
}
