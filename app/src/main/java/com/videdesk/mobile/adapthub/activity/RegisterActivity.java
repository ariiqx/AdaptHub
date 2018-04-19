package com.videdesk.mobile.adapthub.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;
import com.videdesk.mobile.adapthub.model.Person;

public class RegisterActivity extends AppCompatActivity {

    private ProgressDialog pDialog;

    private EditText txtName, txtEmail, txtPhone, txtPass;

    private String name, email, phone, pass, image, nation, region, location, gender;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user != null){
            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
            finish();
            return;
        }

        Videx videx = new Videx(RegisterActivity.this);
        db = videx.getFirestore();

        pDialog = new ProgressDialog(RegisterActivity.this);


        txtName = findViewById(R.id.user_name);
        txtEmail = findViewById(R.id.user_email);
        txtPhone = findViewById(R.id.user_phone);
        txtPass = findViewById(R.id.user_password);

        TextView lblConn = findViewById(R.id.lbl_conn);
        if(videx.isConn()){
            lblConn.setVisibility(View.GONE);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        Button btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs()){
                    name = Videx.toProper(txtName.getText().toString().trim());
                    email = txtEmail.getText().toString().trim().toLowerCase();
                    pass = txtPass.getText().toString().trim();
                    image = nation = region = location = gender = "none";
                    register();
                }

            }
        });

    }

    private boolean checkInputs(){

        if (TextUtils.isEmpty(txtName.getText().toString())) {
            txtName.setFocusable(true);
            txtName.requestFocus();
            txtName.setError("Enter your full name!");
            return false;
        }

        if (txtName.getText().toString().length() < 6) {
            txtName.setFocusable(true);
            txtName.requestFocus();
            txtName.setError("Please enter your valid full name!");
            return false;
        }

        if (TextUtils.isEmpty(txtEmail.getText().toString())) {
            txtEmail.setFocusable(true);
            txtEmail.requestFocus();
            txtEmail.setError("Enter email address!");
            return false;
        }

        if (!txtEmail.getText().toString().contains("@")) {
            txtEmail.setFocusable(true);
            txtEmail.requestFocus();
            txtEmail.setError("Email address is missing the '@' sign!");
            return false;
        }

        if (!txtEmail.getText().toString().contains(".")) {
            txtEmail.setFocusable(true);
            txtEmail.requestFocus();
            txtEmail.setError("Email address is missing the 'dot (.)' sign!");
            return false;
        }

        if (!txtPhone.getText().toString().startsWith("+")) {
            txtPhone.setFocusable(true);
            txtPhone.requestFocus();
            txtPhone.setError("Phone number must begin with a '+' sign!");
            return false;
        }

        if (TextUtils.isEmpty(txtPhone.getText().toString())) {
            txtPhone.setFocusable(true);
            txtPhone.requestFocus();
            txtPhone.setError("Please enter your phone number!");
            return false;
        }

        if(!isValidPhoneNumber(txtPhone.getText().toString())){
            txtPhone.setFocusable(true);
            txtPhone.requestFocus();
            txtPhone.setError("Phone number is not valid!");
            return false;
        }

        if(!validateUsing_libphonenumber(txtPhone.getText().toString())){
            txtPhone.setFocusable(true);
            txtPhone.requestFocus();
            txtPhone.setError("Phone number is not valid!");
            return false;
        }

        if (TextUtils.isEmpty(txtPass.getText().toString())) {
            txtPass.setFocusable(true);
            txtPass.requestFocus();
            txtPass.setError("Enter password!");
            return false;
        }

        if (txtPass.getText().toString().length() < 6) {
            txtPass.setFocusable(true);
            txtPass.requestFocus();
            txtPass.setError("Password too short, enter minimum 6 characters!");
            return false;
        }

        return true;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        return !TextUtils.isEmpty(phoneNumber) && Patterns.PHONE.matcher(phoneNumber).matches();
    }

    private boolean validateUsing_libphonenumber(String vPhone) {
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phoneNumber = null;
        try {
            phoneNumber = phoneNumberUtil.parse(vPhone, "");
        } catch (NumberParseException e) {
            System.err.println(e);
        }

        boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
        if (isValid) {
            phone = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
            return true;
        } else {
            Toast.makeText(this, "Phone number is not valid " + phoneNumber, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void register(){
        pDialog = new ProgressDialog(RegisterActivity.this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("Creating a new account using your credentials.");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = auth.getCurrentUser();
                            savePerson();
                        } else {
                            pDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void savePerson(){
        final Person person = new Person(user.getUid(), Value.KEY_USER_READ, name, email, phone, "none", nation, region, location, gender, Videx.getDatedTimed(), Value.KEY_STATUS_PENDING);
        db.collection(Value.TABLE_PEOPLE).document(user.getUid())
                .set(person)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        PersonDA personDA = new PersonDA(RegisterActivity.this);
                        personDA.add(person);
                        pDialog.dismiss();
                        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Authentication failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
