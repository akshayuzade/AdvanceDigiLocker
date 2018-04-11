package com.example.hakathon.advaceddigi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class VerificationActivity extends AppCompatActivity {
    EditText vvetadharno, vvetadharotp, vvetmobileno, vvetmobileotp, vvetemail, vvetemailotp;
    Button vvtnsendotpadhar, vvbtnadharverify, vvtnsendotpmobile, vvbtnmobileverify, vvtnsendotpemail, vvbtnemailverify;
    String VERIFICATION_URL = "http://192.168.43.44/digilockerr/retriveuserinfo.php";
    String loginusername,adharno,adharotp,mobileno,mobileotp,email,emailotp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);


        SharedPreferences prefs = getSharedPreferences("digilocker", MODE_PRIVATE);
        loginusername = prefs.getString("loginusername", "");



        vvetadharno = (EditText) findViewById(R.id.vvetadharno);
        vvetadharotp = (EditText) findViewById(R.id.vvetadharotp);
        vvetmobileno = (EditText) findViewById(R.id.vvetmobileno);
        vvetmobileotp = (EditText) findViewById(R.id.vvetmobileotp);
        vvetemail = (EditText) findViewById(R.id.vvetemail);
        vvetemailotp = (EditText) findViewById(R.id.vvetemailotp);

        vvtnsendotpadhar = (Button) findViewById(R.id.vvtnsendotpadhar);
        vvbtnadharverify = (Button) findViewById(R.id.vvbtnadharverify);
        vvtnsendotpmobile = (Button) findViewById(R.id.vvtnsendotpmobile);
        vvbtnmobileverify = (Button) findViewById(R.id.vvbtnmobileverify);
        vvtnsendotpemail = (Button) findViewById(R.id.vvtnsendotpemail);
        vvbtnemailverify = (Button) findViewById(R.id.vvbtnemailverify);
        autofetch();

    }


    public void autofetch() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VERIFICATION_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray result = jsonobject.getJSONArray("result");
                            JSONObject data = result.getJSONObject(0);
                            vvetadharno.setText(data.getString("adharno"));
                            vvetmobileno.setText(data.getString("mobileno"));
                            vvetemail.setText(data.getString("email"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerificationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", loginusername);

                return params;
            }
        };
        MySingleton.getInstance(VerificationActivity.this).addtorequestQueue(stringRequest);
    }


    void successverification(){

        SharedPreferences.Editor editor = getSharedPreferences("digilockeruserprofile", MODE_PRIVATE).edit();
        editor.putString("mobilenostatus","false");
        editor.putString("adharnostatus", "false");
        editor.putString("emailstatus", "false");
        editor.apply();
    }

    public void skip(View view) {
        Intent i=new Intent(VerificationActivity.this,DashBoard.class);
        startActivity(i);
        finish();
    }


    public void sendadharotp(final View view) {
         adharno=vvetadharno.getText().toString().trim();
        vvbtnadharverify.setVisibility(view.VISIBLE);
        vvetadharotp.setVisibility(view.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.44/digilockerr/documentverification/adharotp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(VerificationActivity.this, response, Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerificationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", loginusername);
                params.put("adharno", adharno);
                params.put("doc_name", "adharcard");

                return params;
            }
        };
        MySingleton.getInstance(VerificationActivity.this).addtorequestQueue(stringRequest);

    }

    public void verifyadharotp(View view) {
        adharotp=vvetadharotp.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.44/digilockerr/documentverification/adharotpverify.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       if(response.equals("success")) {
                           Toast.makeText(VerificationActivity.this, "Adhar Card verification success" , Toast.LENGTH_SHORT).show();
                           SharedPreferences.Editor editor1 = getSharedPreferences("digilockeruserprofile", MODE_PRIVATE).edit();
                           editor1.putString("adharnostatus", "true");
                           editor1.apply();
                       }
                       else {
                           Toast.makeText(VerificationActivity.this, "Failed to verify", Toast.LENGTH_SHORT).show();
                       }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerificationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", loginusername);
                params.put("adharno", adharno);
                params.put("doc_name", "adharcard");
                params.put("adharotp",adharotp);

                return params;
            }
        };
        MySingleton.getInstance(VerificationActivity.this).addtorequestQueue(stringRequest);
    }


    public void sendmobileotp(final View view) {
        mobileno=vvetmobileno.getText().toString().trim();
        vvbtnmobileverify.setVisibility(view.VISIBLE);
        vvetmobileotp.setVisibility(view.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.44/digilockerr/documentverification/mobileotp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(VerificationActivity.this, response, Toast.LENGTH_SHORT).show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerificationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", loginusername);
                params.put("mobileno", mobileno);
                params.put("doc_name", "mobileno");

                return params;
            }
        };
        MySingleton.getInstance(VerificationActivity.this).addtorequestQueue(stringRequest);

    }


    public void verifymobileotp(View view) {
        mobileotp=vvetmobileotp.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.44/digilockerr/documentverification/mobileotpverify.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")) {
                            Toast.makeText(VerificationActivity.this, "Mobile verification success" , Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor2 = getSharedPreferences("digilockeruserprofile", MODE_PRIVATE).edit();
                            editor2.putString("mobilenostatus","true");
                            editor2.apply();
                        }
                        else {
                            Toast.makeText(VerificationActivity.this, "Failed to verify", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerificationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", loginusername);
                params.put("mobileno", mobileno);
                params.put("doc_name", "adharcard");
                params.put("mobileotp", mobileotp);

                return params;
            }
        };
        MySingleton.getInstance(VerificationActivity.this).addtorequestQueue(stringRequest);






    }


    public void sendemailotp(final View view) {
        email=vvetemail.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.44/digilockerr/documentverification/emailotp.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(VerificationActivity.this, response, Toast.LENGTH_SHORT).show();
                        vvbtnemailverify.setVisibility(view.VISIBLE);
                        vvetemailotp.setVisibility(view.VISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerificationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", loginusername);
                params.put("emailid", email);
                params.put("doc_name", "emailid");

                return params;
            }
        };
        MySingleton.getInstance(VerificationActivity.this).addtorequestQueue(stringRequest);

    }

    public void verifyemailotp(View view) {
        emailotp=vvetemailotp.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.43.44/digilockerr/documentverification/emailotpverify.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("success")) {
                            Toast.makeText(VerificationActivity.this, "Email verification success" , Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor3 = getSharedPreferences("digilockeruserprofile", MODE_PRIVATE).edit();
                            editor3.putString("emailstatus", "true");
                            editor3.apply();
                        }
                        else {
                            Toast.makeText(VerificationActivity.this, "Failed to verify", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VerificationActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", loginusername);
                params.put("email", email);
                params.put("doc_name", "emailid");
                params.put("emailotp", emailotp);

                return params;
            }
        };
        MySingleton.getInstance(VerificationActivity.this).addtorequestQueue(stringRequest);






    }
}

