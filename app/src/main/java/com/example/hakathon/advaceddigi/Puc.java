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

public class Puc extends AppCompatActivity {
    String username;
    EditText etpucpucno, etpucvehicleno;
    TextView tvpucname, tvpucadress, tvpucdoi, tvpucdoe;
    Button btnpucverify, btnpucsubmit;
    String strpucno, strvehicleno;
    final static String PUC_VERIFY_URL = "http://192.168.43.44/digilockerr/Vehicle/PUCverify.php";
    final static String PUC_SUBMIT_URL = "http://192.168.43.44/digilockerr/verified/PUCsubmit.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puc);

        SharedPreferences prefs = getSharedPreferences("digilocker", MODE_PRIVATE);
        username = prefs.getString("loginusername","");

        tvpucname = (TextView) findViewById(R.id.tvpucname);
        tvpucadress = (TextView) findViewById(R.id.tvpucadress);
        tvpucdoi = (TextView) findViewById(R.id.tvpucdoi);
        tvpucdoe = (TextView) findViewById(R.id.tvpucdoe);
        etpucpucno = (EditText) findViewById(R.id.etpucpucno);
        etpucvehicleno = (EditText) findViewById(R.id.etpucvehicleno);
        btnpucverify = (Button) findViewById(R.id.btnpucverify);
        btnpucsubmit = (Button) findViewById(R.id.btnpucsubmit);


        btnpucverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strpucno = etpucpucno.getText().toString();
                strvehicleno=etpucvehicleno.getText().toString();
                pucverify();
            }
        });
        btnpucsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tvpucname.equals("..............")) {
                    Toast.makeText(Puc.this, "Verify Driving Licence", Toast.LENGTH_SHORT).show();
                }else{
                    pucsucceess();
                }
            }
        });
    }

    public void pucverify() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PUC_VERIFY_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray result = jsonobject.getJSONArray("result");
                            JSONObject data = result.getJSONObject(0);
                            tvpucname.setText(data.getString("name"));
                            tvpucadress.setText(data.getString("address"));
                            tvpucdoi.setText(data.getString("dateofissuee"));
                            tvpucdoe.setText(data.getString("date_of_expiry"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Puc.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pucno", strpucno);
                params.put("vehicleno", strvehicleno);
                return params;
            }
        };
        MySingleton.getInstance(Puc.this).addtorequestQueue(stringRequest);
    }


    public void pucsucceess() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, PUC_SUBMIT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Puc.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Puc.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("pucno", strpucno);
                params.put("vehicleno", strvehicleno);
                params.put("username", username);
                return params;
            }
        };
        MySingleton.getInstance(Puc.this).addtorequestQueue(stringRequest);
    }
}
