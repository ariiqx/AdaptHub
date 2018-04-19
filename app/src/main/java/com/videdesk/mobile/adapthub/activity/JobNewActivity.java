package com.videdesk.mobile.adapthub.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class JobNewActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseFirestore db;
    private EditText txtTitle, txtDetails, txtWage, txtLocation, txtDeadline;
    private Spinner txtCurrency;
    private ProgressDialog pDialog;
    private Calendar myCalendar;
    private Videx videx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_new);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(JobNewActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(JobNewActivity.this);
        db = videx.getFirestore();

        myCalendar = Calendar.getInstance();
        txtTitle = findViewById(R.id.job_title);
        txtDetails = findViewById(R.id.job_details);
        txtWage = findViewById(R.id.job_wage);
        txtCurrency = findViewById(R.id.job_currency);
        txtLocation = findViewById(R.id.job_location);
        txtDeadline = findViewById(R.id.job_deadline);

        loadCurrencies();

        ImageView btnPicker = findViewById(R.id.job_date_picker);
        btnPicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(JobNewActivity.this, datePika, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button btnCreate = findViewById(R.id.btn_create_job);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs()){
                    saveJob();
                }
            }
        });
    }

    private void loadCurrencies(){
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("GHS");
        spinnerArray.add("USD");
        spinnerArray.add("GBP");
        spinnerArray.add("EUR");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtCurrency.setAdapter(adapter);
    }

    DatePickerDialog.OnDateSetListener datePika = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            fillExpired();
        }

    };
    private void fillExpired() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtDeadline.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean checkInputs(){

        if (TextUtils.isEmpty(txtTitle.getText())) {
            txtTitle.setFocusable(true);
            txtTitle.requestFocus();
            txtTitle.setError("Enter job title!");
            return false;
        }

        if (TextUtils.isEmpty(txtDetails.getText())) {
            txtDetails.setFocusable(true);
            txtDetails.requestFocus();
            txtDetails.setError("Enter job description!");
            return false;
        }

        if (TextUtils.isEmpty(txtWage.getText())) {
            txtWage.setFocusable(true);
            txtWage.requestFocus();
            txtWage.setError("Enter hourly pay amount!");
            return false;
        }

        if (TextUtils.isEmpty(txtLocation.getText())) {
            txtLocation.setFocusable(true);
            txtLocation.requestFocus();
            txtLocation.setError("Enter name of town, city or location!");
            return false;
        }

        if (TextUtils.isEmpty(txtDeadline.getText())) {
            txtDeadline.setFocusable(true);
            txtDeadline.requestFocus();
            txtDeadline.setError("Enter date on which job expires!");
            return false;
        }

        return true;
    }

    private void saveJob(){
        pDialog = new ProgressDialog(JobNewActivity.this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("We are submitting your story.");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        final String node = Videx.getNode();

        Job job = new Job(user.getUid(), node, txtTitle.getText().toString(), txtDetails.getText().toString(),
                txtLocation.getText().toString(), "none", txtDeadline.getText().toString(), txtWage.getText().toString(),
                txtCurrency.getSelectedItem().toString(), Videx.getDatedTimed(), Value.KEY_STATUS_PENDING, Value.KEY_READ_NO);
        db.collection(Value.TABLE_JOBS).document(node)
                .set(job)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        videx.setPref(Value.COLUMN_JOB_NODE, node);
                        pDialog.dismiss();
                        startActivity(new Intent(JobNewActivity.this, JobEditActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(JobNewActivity.this, "Operation failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
