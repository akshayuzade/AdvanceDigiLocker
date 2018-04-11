package com.example.hakathon.advaceddigi;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
public class AddDocument extends AppCompatActivity {
    Button btnupload,btnchoose;
    DatePicker dpdocexpiry;
    Spinner spinnerdoclist;
    private Uri imageUri;
    private TextRecognizer detector; //ocr
    ImageView imgImage;
    TextView scanResults;//ocr
    private static final int PHOTO_REQUEST = 10;
    private Bitmap bitmap;
    String expirydate,uploaddate;
    String userid,documentname;
    private String Upload_Url = "http://192.168.43.44/digilockerr/uploaddocument.php";

    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);

        pd=new ProgressDialog(this);

        detector = new TextRecognizer.Builder(getApplicationContext()).build();//ocr
        scanResults = (TextView) findViewById(R.id.results);//ocr
        SharedPreferences prefs = getSharedPreferences("digilocker", MODE_PRIVATE);
        userid = prefs.getString("loginusername","");

        dpdocexpiry=(DatePicker)findViewById(R.id.dpdocexpiry);
        spinnerdoclist=(Spinner)findViewById(R.id.spinnerdoclist);

        imgImage = (ImageView) findViewById(R.id.myimage);
        btnchoose = (Button) findViewById(R.id.mychoimg);
        btnupload = (Button) findViewById(R.id.myupimg);
        getspinnername();
        getuploaddate();
        btnchoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getspinnername();
                getexprarydate();
            //    Toast.makeText(AddDocument.this,documentname+expirydate+uploaddate, Toast.LENGTH_SHORT).show();

                if(documentname.equals("Select Document")){
                    Toast.makeText(AddDocument.this,"Select Document Name", Toast.LENGTH_SHORT).show();
                }else if(expirydate.equals(uploaddate)){
                    Toast.makeText(AddDocument.this, "Expirary date not same as upload date", Toast.LENGTH_SHORT).show();
                }else {
                    ImageUpload();
                }
            }
        });
    }

    private void selectImage(){

        Intent i = new Intent();
        i.setType("image/*");

        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(i,PHOTO_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PHOTO_REQUEST && resultCode == RESULT_OK && data != null) {


            imageUri = data.getData();
            launchMediaScanIntent();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgImage.setImageBitmap(bitmap);
                imgImage.setVisibility(View.VISIBLE);
                ocrfunction1();

            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }


    private void ImageUpload()
    {
        pd.setTitle("Uploading...");
        pd.setMessage("Please Wait.......");
        pd.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Upload_Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    Toast.makeText(AddDocument.this,Response, Toast.LENGTH_SHORT).show();
                    imgImage.setImageResource(R.drawable.successupload);
                        pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(AddDocument.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",documentname);
                params.put("image",ImageToString(bitmap));
                params.put("expirary_date",expirydate);
                params.put("upload_date",uploaddate);
                params.put("userid",userid);
                return params;
            }
        };
        MySingleton.getInstance(AddDocument.this).addtorequestQueue(stringRequest);
    }

    private String ImageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

    void getspinnername(){
        spinnerdoclist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                documentname=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    void getexprarydate(){
        int day = dpdocexpiry.getDayOfMonth();
        int month = dpdocexpiry.getMonth() + 1;
        int year = dpdocexpiry.getYear();
       expirydate= day+"-"+month+"-"+year ;
    }

    void getuploaddate(){
        final Calendar c = Calendar.getInstance();

        int Year = c.get(Calendar.YEAR);
        int Month = c.get(Calendar.MONTH)+1;
        int Day = c.get(Calendar.DAY_OF_MONTH);
        uploaddate= Day+"-"+Month+"-"+Year ;
    }


    //ocr

    void ocrfunction1(){
        if (detector.isOperational() && bitmap != null) {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlocks = detector.detect(frame);
            String blocks = "";
            String lines = "";
            String words = "";
            for (int index = 0; index < textBlocks.size(); index++) {
                //extract scanned text blocks here
                TextBlock tBlock = textBlocks.valueAt(index);
                blocks = blocks + tBlock.getValue() + "\n" + "\n";
                for (Text line : tBlock.getComponents()) {
                    //extract scanned text lines here
                    lines = lines + line.getValue() + "\n";
                    for (Text element : line.getComponents()) {
                        //extract scanned text words here
                        words = words + element.getValue() + ", ";
                    }
                }
            }
            if (textBlocks.size() == 0) {
                Toast.makeText(this, "Scan Failed: Found nothing to scan", Toast.LENGTH_SHORT).show();

            } else {
                scanResults.setText(scanResults.getText() + blocks + "\n");
                String scanvalue=scanResults.getText().toString().trim();
                Toast.makeText(this,scanvalue, Toast.LENGTH_SHORT).show();
            }
        } else {
            scanResults.setText("Could not set up the detector!");
        }
    }
    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    //ocr function end
}
