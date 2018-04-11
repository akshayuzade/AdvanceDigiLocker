package com.example.hakathon.advaceddigi;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
//refresh code
    private static DashBoard mInstance;
    //end
        String username;
    DocumentAdapter adapter;
    ListView listView;
    Context context;
    String DOCUMENTS_URL="http://192.168.43.44/digilockerr/fetchimg.php";
    String USER_PROFILE_URL="http://192.168.43.44/digilockerr/retriveuserinfo.php";
    ArrayList<String> documentname = new ArrayList<String>();
    ArrayList<String> documenturl = new ArrayList<String>();

    TextView myname,myemail;
    ImageView myimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //refresh code
        mInstance = this;
//end

        context=this;
        listView=(ListView)findViewById(R.id.imagesloadlistview);
        adapter=new DocumentAdapter(this,documenturl,documentname);
        SharedPreferences prefs = getSharedPreferences("digilocker", MODE_PRIVATE);
        username = prefs.getString("loginusername","");

        usermybahin();
        fetchimg();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        myname   = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nvmyname);
        myemail   = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nvmyemail);
        myimage    = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.nvmyimage);
    }

    public static synchronized DashBoard getInstance() {
        return mInstance;
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            SharedPreferences.Editor editor = getSharedPreferences("digilocker", MODE_PRIVATE).edit();
            editor.putString("loginusername", "xx");
            editor.putString("loginpassword", "xx");
            editor.apply();
            startActivity(new Intent(DashBoard.this, UserLogin.class));
            finish();
            return true;
        }else if (id == R.id.action_refresh) {

            startActivity(new Intent(DashBoard.this, DashBoard.class));
            finish();
            return true;
        }else if (id == R.id.action_newdoc) {

            startActivity(new Intent(DashBoard.this, AddDocument.class));
             return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
          int id = item.getItemId();
        if (id == R.id.add_new_doc) {
            startActivity(new Intent(DashBoard.this,AddDocument.class));
        } else if (id == R.id.add_new_doc_qr) {
            startActivity(new Intent(DashBoard.this, QRDocumentActivity.class));
        } else if (id == R.id.issuee_doc) {
            startActivity(new Intent(DashBoard.this,IssueeDocument.class));
        }else if (id == R.id.vehicledoc) {

        }else if (id == R.id.driving_license) {
            startActivity(new Intent(DashBoard.this,DrivingLicense.class));
        } else if (id == R.id.puc) {
            startActivity(new Intent(DashBoard.this,Puc.class));
        } else if (id == R.id.insurance) {
            startActivity(new Intent(DashBoard.this,Insurance.class));
        } else if (id == R.id.smart_card) {
            startActivity(new Intent(DashBoard.this,SmartCard.class));
        } else if (id == R.id.qr_code) {
            startActivity(new Intent(DashBoard.this,QrCode.class));
        }else if (id == R.id.setting) {
            startActivity(new Intent(DashBoard.this,Setting.class));
        }else if (id == R.id.feedback) {
            startActivity(new Intent(DashBoard.this,FeedBack.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public void fetchimg() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOCUMENTS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray result = jsonobject.getJSONArray("result");
                            for(int i=0;i<result.length();i++) {
                                JSONObject data = result.getJSONObject(i);
                                String name = data.getString("document_name");
                                String url = data.getString("document_url");
                                documentname.add(name);
                                documenturl.add(url);
                            }
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashBoard.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);

                return params;
            }
        };
        MySingleton.getInstance(DashBoard.this).addtorequestQueue(stringRequest);
    }





    void usermybahin(){


        StringRequest stringRequest = new StringRequest(Request.Method.POST, USER_PROFILE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray result = jsonobject.getJSONArray("result");
                            JSONObject data = result.getJSONObject(0);
                            String name = data.getString("name");
                            String email = data.getString("email");
                            String mobileno = data.getString("mobileno");
                            String adharno = data.getString("adharno");
                            String firebase_id = data.getString("firebase_id");
                            String profilepic = data.getString("profilepic");

                            SharedPreferences.Editor editor = getSharedPreferences("digilockeruserprofile", MODE_PRIVATE).edit();
                            editor.putString("name", name);
                            editor.putString("email", email);
                            editor.putString("mobileno",mobileno);
                            editor.putString("adharno", adharno);
                            editor.putString("firebase_id", firebase_id);
                            editor.putString("profilepic",profilepic);
                            editor.apply();


                            myname.setText(name);
                            myemail.setText(email);
                            ImageRequest imageRequest = new ImageRequest(profilepic, new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    myimage.setImageBitmap(response);
                                }
                            }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {


                                    error.printStackTrace();

                                }
                            });
                            MySingleton.getInstance(context).addtorequestQueue(imageRequest);





                    } catch(
                    JSONException e)

                    {
                        e.printStackTrace();
                    }
                }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DashBoard.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
               params.put("username", username);

                return params;
            }
        };
        MySingleton.getInstance(DashBoard.this).addtorequestQueue(stringRequest);
    }




}


