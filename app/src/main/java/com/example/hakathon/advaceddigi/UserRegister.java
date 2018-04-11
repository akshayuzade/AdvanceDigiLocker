package com.example.hakathon.advaceddigi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class UserRegister extends AppCompatActivity {
    EditText registeretname,registeretemail,registeretmobileno,registeretpassword,registeretconfirmpassword;
    String strregisteretname,strregisteretemail,strregisteretmobileno,strregisteretpassword,strregisteretconfirmpassword;
    String REGISTER_URL="http://192.168.43.44/digilockerr/register.php";
        ImageView imgprofilepic;
    private Bitmap bitmap;
    private Uri imageUri;

    /*
     //firebase decleration
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;
    private FirebaseUser user;
    String uid;

     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        /*
          mAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
         */
        registeretname=(EditText)findViewById(R.id.registeretname);
        registeretemail=(EditText)findViewById(R.id.registeretemail);
        registeretmobileno=(EditText)findViewById(R.id.registeretmobileno);
        registeretpassword=(EditText)findViewById(R.id.registeretpassword);
        registeretconfirmpassword=(EditText)findViewById(R.id.registeretconfirmpassword);
        imgprofilepic=(ImageView)findViewById(R.id.imgprofilepic);
        imgprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

    }

    public void userregistraion(View view) {
        strregisteretname=registeretname.getText().toString().trim();
        strregisteretemail=registeretemail.getText().toString().trim();
        strregisteretmobileno=registeretmobileno.getText().toString().trim();
        strregisteretpassword=registeretpassword.getText().toString().trim();
        strregisteretconfirmpassword=registeretconfirmpassword.getText().toString().trim();

        if(strregisteretpassword.equals(strregisteretconfirmpassword)){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(UserRegister.this, response, Toast.LENGTH_SHORT).show();
                   clearall();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserRegister.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
          //      strregisteretname,strregisteretemail,strregisteretmobileno,strregisteretpassword,strregisteretconfirmpassword;

                params.put("strregisteretname", strregisteretname);
                params.put("strregisteretemail", strregisteretemail);
                params.put("strregisteretmobileno", strregisteretmobileno);
                params.put("strregisteretpassword", strregisteretpassword);
                params.put("profilepic",ImageToString(bitmap));
                return params;
            }
        };
        MySingleton.getInstance(UserRegister.this).addtorequestQueue(stringRequest);
    }else{
            Toast.makeText(this, "Password Not Match", Toast.LENGTH_SHORT).show();
        }
    }


    private String ImageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }


    void clearall(){
        registeretname.setText("");
        registeretemail.setText("");
        registeretmobileno.setText("");
        registeretpassword.setText("");
        registeretconfirmpassword.setText("");

    }
    private void selectImage(){

        Intent i = new Intent();
        i.setType("image/*");

        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(i,123);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
             try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imgprofilepic.setImageBitmap(bitmap);
                imgprofilepic.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    /*

public void firebaseuserregistration(){
    if(strpassword.equals(strconpassword)) {
        mAuth.createUserWithEmailAndPassword(strusername, strpassword)
                .addOnCompleteListener(RegisterUser.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                               Toast.makeText(RegisterUser.this, "Registration Successfull...!",
                               Toast.LENGTH_SHORT).show();
                                         } else {
                            Toast.makeText(RegisterUser.this, "Registration Failed...!", Toast.LENGTH_SHORT).show();
                        }                      // ...
                    }
                });
    }else {
        Toast.makeText(RegisterUser.this, "Password Not Match", Toast.LENGTH_SHORT).show();
    }
}
     */
}
