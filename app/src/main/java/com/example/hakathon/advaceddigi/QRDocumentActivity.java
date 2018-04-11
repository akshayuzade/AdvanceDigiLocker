package com.example.hakathon.advaceddigi;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.google.zxing.integration.android.IntentIntegrator;
        import com.google.zxing.integration.android.IntentResult;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.HashMap;
        import java.util.Map;

public class QRDocumentActivity extends AppCompatActivity {
   String strqrcodeid;
    String strdocumentname;
    String strusername;

    TextView tvname,tvdob;
    Spinner spinnerdoclist;
    Button btnqrscan,btnsubmitdoc;
    String DOC_FETCH_URL="http://192.168.43.44/digilockerr/otherdocsubmit/common.php";
    String FINAL_DOC_SUBMIT="http://192.168.43.44/digilockerr/otherdocsubmit/inserissuee.php";

    private IntentIntegrator qrScan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrdocument);


        SharedPreferences prefs = getSharedPreferences("digilocker", MODE_PRIVATE);
        strusername = prefs.getString("loginusername","");


        tvname=(TextView)findViewById(R.id.tvname);
        tvdob=(TextView)findViewById(R.id.tvdob);
        spinnerdoclist=(Spinner)findViewById(R.id.spinnerdocumentlist);
        btnqrscan=(Button)findViewById(R.id.btnqrscan);
        btnsubmitdoc=(Button)findViewById(R.id.submitdoc);
        qrScan = new IntentIntegrator(QRDocumentActivity.this);

        spinnerdoclist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                strdocumentname=parentView.getItemAtPosition(position).toString();
             //   Toast.makeText(QRDocumentActivity.this, strdocumentname, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                strdocumentname="Select_Documents";
            }

        });



        btnqrscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               qrScan.initiateScan();
            }
        });

        btnsubmitdoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalsubmitdoc(strqrcodeid,strdocumentname);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                  String  qrcodeid=result.getContents();
                  strqrcodeid=qrcodeid;
                    fetchdocumentdetails(qrcodeid,strdocumentname);
                 //  Toast.makeText(this, strqrcodeid, Toast.LENGTH_SHORT).show();

                    JSONObject obj = new JSONObject(result.getContents());

                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    public void fetchdocumentdetails(final String strqrcodeid, final String strdocumentname) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, DOC_FETCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(QRDocumentActivity.this, response, Toast.LENGTH_SHORT).show();
                    try {
                            JSONObject jsonobject = new JSONObject(response);
                            JSONArray result = jsonobject.getJSONArray("result");
                            for(int i=0;i<result.length();i++) {
                                JSONObject data = result.getJSONObject(i);

                                String n=data.getString("name");
                                Toast.makeText(QRDocumentActivity.this, n, Toast.LENGTH_SHORT).show();
                                tvname.setText(data.getString("name"));
                              tvdob.setText(data.getString("dob"));
                            }

                            } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(QRDocumentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("strqrcodeid", strqrcodeid);
                params.put("strdocumentname", strdocumentname);
                return params;
            }
        };
        MySingleton.getInstance(QRDocumentActivity.this).addtorequestQueue(stringRequest);
    }



    public void finalsubmitdoc(final String strqrcodeid, final String strdocumentname) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, FINAL_DOC_SUBMIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(QRDocumentActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(QRDocumentActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("strqrcodeid", strqrcodeid);
                params.put("strdocumentname", strdocumentname);
                params.put("strusername", strusername);

                return params;
            }
        };
        MySingleton.getInstance(QRDocumentActivity.this).addtorequestQueue(stringRequest);
    }

}
