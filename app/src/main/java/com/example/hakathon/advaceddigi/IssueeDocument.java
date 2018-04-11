package com.example.hakathon.advaceddigi;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IssueeDocument extends AppCompatActivity {
    String username;
    IssueeDocumentAdapter adapter;
    String DOCUMENTS_URL="http://192.168.43.44/digilockerr/fetchissueedocument.php";
    ListView issueedoclistview;
    Context context;
    ArrayList<String> documentname = new ArrayList<String>();
    ArrayList<String> documenturl = new ArrayList<String>();
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issuee_document);
            pd=new ProgressDialog(this);
        SharedPreferences prefs = getSharedPreferences("digilocker", MODE_PRIVATE);
        username = prefs.getString("loginusername","");

        context=this;
        issueedoclistview=(ListView)findViewById(R.id.issueedoclistview);
        adapter=new IssueeDocumentAdapter(this,documenturl,documentname);
        fetchissueedocument();

    issueedoclistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String url=documenturl.get(position);
            pd.setMessage("File Downloading.....");
            pd.show();
            downloadfile(url);
            pd.setMessage("Download Complete");
            pd.dismiss();
        }
    });
    }


    public void fetchissueedocument() {
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
                                String url = data.getString("pdflink");
                                documentname.add(name);
                                documenturl.add(url);
                            }
                            issueedoclistview.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(IssueeDocument.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);

                return params;
            }
        };
        MySingleton.getInstance(IssueeDocument.this).addtorequestQueue(stringRequest);
    }

    void downloadfile(String fileurl){
        DownloadManager downloadmaneger=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri= Uri.parse(fileurl);
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        long refrences=downloadmaneger.enqueue(request);
    }

}
