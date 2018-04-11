package com.example.hakathon.advaceddigi;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class SmartCard extends AppCompatActivity {
    String username;
    EditText etrcregno,etrcvehicleno;
    TextView tvrcname, tvrcaddress, tvrcdoi, tvrcdoe;
    Button btnrcverify, btnrcsubmit;
    String strrcregno, strrcvehicleno;
    ImageView dlpic;
    final static String RC_VERIFY_URL = "http://192.168.43.44/digilockerr/Vehicle/RCverify.php";
    final static String RC_SUBMIT_URL = "http://192.168.43.44/digilockerr/verified/RCsubmit.php";
    String IMAGE_URL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_card);


        SharedPreferences prefs = getSharedPreferences("digilocker", MODE_PRIVATE);
        username = prefs.getString("loginusername","");

        etrcregno=(EditText)findViewById(R.id.etrcregno);
        etrcvehicleno=(EditText)findViewById(R.id.etrcvehicleno);
        tvrcname=(TextView)findViewById(R.id.tvrcname);
        tvrcaddress=(TextView)findViewById(R.id.tvrcaddress);
        tvrcdoe=(TextView)findViewById(R.id.tvrcdoe);
        tvrcdoi=(TextView)findViewById(R.id.tvrcdoi);
        btnrcverify=(Button)findViewById(R.id.btnrcverify);
        btnrcsubmit=(Button)findViewById(R.id.btnrcsubmit);


        btnrcverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strrcregno = etrcregno.getText().toString();
                strrcvehicleno=etrcvehicleno.getText().toString();
                RCverify();
            }
        });

        btnrcsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvrcname.equals("..............")) {
                    Toast.makeText(SmartCard.this, "Verify Smartcard", Toast.LENGTH_SHORT).show();
                }else{
                    rcsuccess();
                }
            }
        });
    }

    public void RCverify() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RC_VERIFY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray result = jsonobject.getJSONArray("result");
                            JSONObject data = result.getJSONObject(0);
                            tvrcname.setText(data.getString("name"));
                            tvrcaddress.setText(data.getString("address"));
                            tvrcdoe.setText(data.getString("date_of_expiry"));
                            tvrcdoi.setText(data.getString("dateofissuee"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SmartCard.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cardno", strrcregno);
                params.put("vehicleno", strrcvehicleno);
                return params;
            }
        };
        MySingleton.getInstance(SmartCard.this).addtorequestQueue(stringRequest);
    }


    public void rcsuccess() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RC_SUBMIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SmartCard.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SmartCard.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cardno", strrcregno);
                params.put("vehicleno", strrcvehicleno);
                params.put("username", username);
                return params;
            }
        };
        MySingleton.getInstance(SmartCard.this).addtorequestQueue(stringRequest);
    }
}
