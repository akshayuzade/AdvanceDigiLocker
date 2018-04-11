package com.example.hakathon.advaceddigi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class ChangePassword extends AppCompatActivity {
    EditText cpetoldpassword,cpetnewpassword,cpetconfirmnewpassword;
String CHANGE_PASS_URL="http://192.168.43.44/digilockerr/changepassword.php";
    String strcpoldpassword,strcpnewpassword,cpconfirmnewpassword,strusername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        SharedPreferences prefs = getSharedPreferences("digilocker", MODE_PRIVATE);
        strusername = prefs.getString("loginusername","");

        cpetoldpassword=(EditText)findViewById(R.id.cpetoldpassword);
        cpetnewpassword=(EditText)findViewById(R.id.cpetnewpassword);
        cpetconfirmnewpassword=(EditText)findViewById(R.id.cpetconfirmnewpassword);
    }

    public void changepassword(View view) {
        strcpoldpassword=cpetoldpassword.getText().toString();
        strcpnewpassword=cpetnewpassword.getText().toString();
        cpconfirmnewpassword=cpetconfirmnewpassword.getText().toString();

        if(strcpnewpassword.equals(cpconfirmnewpassword)) {
            chgpass();
        }{
         //   Toast.makeText(this, "Password Not Matched", Toast.LENGTH_SHORT).show();
        }
    }


    public void chgpass() {
        Toast.makeText(this, strusername+strcpoldpassword+strcpnewpassword, Toast.LENGTH_SHORT).show();
         StringRequest stringRequest = new StringRequest(Request.Method.POST, CHANGE_PASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(ChangePassword.this,"Password Change", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor = getSharedPreferences("digilocker", MODE_PRIVATE).edit();
                        editor.putString("loginusername", "xx");
                        editor.putString("loginpassword", "xx");
                        editor.apply();
                        Intent i=new Intent(ChangePassword.this,UserLogin.class);
                       startActivity(i);
                        clearall();



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangePassword.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
           //  strcpoldpassword,strcpnewpassword,cpconfirmnewpassword,strusername;
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("strcpoldpassword", strcpoldpassword);
                params.put("strcpnewpassword", strcpnewpassword);
                params.put("cpconfirmnewpassword", cpconfirmnewpassword);
                params.put("strusername", strusername);
                return params;
            }
        };
        MySingleton.getInstance(ChangePassword.this).addtorequestQueue(stringRequest);
    }


    void clearall(){
        cpetoldpassword.setText("");
        cpetnewpassword.setText("");
        cpetconfirmnewpassword.setText("");
    }
}
