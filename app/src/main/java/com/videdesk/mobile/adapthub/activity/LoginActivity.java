package com.videdesk.mobile.adapthub.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Videx;

public class LoginActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    private EditText txtEmail, txtPass;
    private String password, email;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
            return;
        }

        Videx videx = new Videx(LoginActivity.this);
        TextView lblConn = findViewById(R.id.lbl_conn);
        if(videx.isConn()){
            lblConn.setVisibility(View.GONE);
        }

        txtEmail = findViewById(R.id.email);
        txtPass = findViewById(R.id.password);
        Button btnRegister = findViewById(R.id.btn_signup);
        Button btnLogin = findViewById(R.id.btn_login);
        Button btnReset = findViewById(R.id.btn_reset_password);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = txtEmail.getText().toString();
                password = txtPass.getText().toString();
                if(checkInputs()){
                    loginUser();
                }else {
                    Toast.makeText(getApplicationContext(), "Login failed! Please correct the errors and try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private boolean checkInputs(){

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            txtEmail.setError("Enter email address!");
            return false;
        }

        if (!email.contains("@")) {
            Toast.makeText(getApplicationContext(), "Email address is missing the '@' sign!", Toast.LENGTH_SHORT).show();
            txtEmail.setFocusable(true);
            txtEmail.requestFocus();
            txtEmail.setError("Email address is missing the '@' sign!");
            return false;
        }

        if (!email.contains(".")) {
            Toast.makeText(getApplicationContext(), "Email address is missing the 'dot (.)' sign!", Toast.LENGTH_SHORT).show();
            txtEmail.setFocusable(true);
            txtEmail.requestFocus();
            txtEmail.setError("Email address is missing the 'dot (.)' sign!");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            txtPass.setFocusable(true);
            txtPass.requestFocus();
            txtEmail.setError("Enter password!");
            return false;
        }

        if (password.length() < 6 ) {
            Toast.makeText(getApplicationContext(), "Password must be more than 6 characters!", Toast.LENGTH_SHORT).show();
            txtPass.setFocusable(true);
            txtPass.requestFocus();
            txtEmail.setError("Password must be more than 6 characters!");
            return false;
        }
        return true;
    }

    private void loginUser()
    {

        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("Signing you into your account on this device.");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        pDialog.dismiss();
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                txtPass.setError(getString(R.string.txt_min_pass));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.txt_auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

    }
}
