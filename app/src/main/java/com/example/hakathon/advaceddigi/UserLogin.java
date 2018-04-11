package com.example.hakathon.advaceddigi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class UserLogin extends AppCompatActivity {
    EditText etusername,etpassword;
    String strusername,strpassword;
    private static final String LOGIN_URL = "http://192.168.43.44/digilockerr/login.php";
      ProgressDialog pd;

      /*
          private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference mStorageRef;

    //autologin function
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            checkIfEmailVerified();
        }
    }

       */
    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);



        etusername=(EditText)findViewById(R.id.loginetemail);
        etpassword=(EditText)findViewById(R.id.loginetpassword);
        pd=new ProgressDialog(this);
        pd.setMessage("Loading Plz wait...");

    }
    public void loginhere(View view) {
        strusername=etusername.getText().toString().trim();
        strpassword=etpassword.getText().toString().trim();
        login();
        pd.show();
    }
    public void login() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //        Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                        if(response.trim().equals("success")){
                            Toast.makeText(UserLogin.this, "Login Success", Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = getSharedPreferences("digilocker", MODE_PRIVATE).edit();
                            editor.putString("loginusername", strusername);
                            editor.putString("loginpassword", strpassword);
                            editor.apply();

                            Intent i=new Intent(UserLogin.this,DashBoard.class);
                            startActivity(i);
                            finish();
                            pd.dismiss();
                        }
                        else if(response.trim().equals("failure")){
                            Toast.makeText(UserLogin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserLogin.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("strusername", strusername);
                params.put("strpassword", strpassword);
                return params;
            }

        };
        MySingleton.getInstance(UserLogin.this).addtorequestQueue(stringRequest);

    }

    public void registerhere(View view) {
        Intent i=new Intent(UserLogin.this,UserRegister.class);
        startActivity(i);
    }



}

/*

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            Toast.makeText(this, "Login Success..", Toast.LENGTH_SHORT).show();
        //    Intent i=new Intent(Login.this,Main2Activity.class);
         //   startActivity(i);
            // user is verified, so you can finish this activity or send user to activity which you want.
            finish();
            Toast.makeText(Login.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent i=new Intent(Login.this,Login.class);
            startActivity(i);
            // user is verified, so you can finish this activity or send user to activity which you want.
            finish();
            Toast.makeText(Login.this, "Verify Email..Link send on register email Id", Toast.LENGTH_SHORT).show();
            sendVerificationEmail();
            FirebaseAuth.getInstance().signOut();
            //restart this activity
        }
    }


    public void Forgotpassword(String email) {
        if (!TextUtils.isEmpty(email)) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    });
        }else{
            Toast.makeText(this, "Enter Email Correclty", Toast.LENGTH_SHORT).show();
        }
    }

    public void dialog(){
        // TODO Auto-generated method stub

        final AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
        final EditText input = new EditText(Login.this);
        //    input.setTransformationMethod(PasswordTransformationMethod.getInstance());
        alert.setView(input);
        alert.setTitle("Forgot Password");
        //  alert.setIcon(R.drawable.ic_launcher);
        alert.setMessage("Please Type Your Email  below to Authenticate");

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                forgotemail=input.getText().toString();
                Forgotpassword(forgotemail);
                Toast.makeText(Login.this,"Password reset link sent on your email", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });

        alert.show();
    }

    public void openforgot(View view) {
        dialog();
    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            //        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            //      finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }
//********


    //**********
  /*
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkIfEmailVerified();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

*/
