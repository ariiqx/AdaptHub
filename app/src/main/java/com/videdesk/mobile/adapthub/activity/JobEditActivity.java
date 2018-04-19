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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.sqlite.JobDA;
import com.videdesk.mobile.adapthub.model.Job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobEditActivity extends AppCompatActivity {

    private ProgressBar progress;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private EditText txtTitle, txtDetails, txtWage, txtLocation, txtDeadline;
    private Spinner txtCurrency;
    private ProgressDialog pDialog;
    private Calendar myCalendar;
    private Videx videx;
    private String node;

    private JobDA jobDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_edit);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(JobEditActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(JobEditActivity.this);
        db = videx.getFirestore();
        node = videx.getPref(Value.COLUMN_JOB_NODE);

        myCalendar = Calendar.getInstance();
        txtTitle = findViewById(R.id.job_title);
        txtDetails = findViewById(R.id.job_details);
        txtWage = findViewById(R.id.job_wage);
        txtCurrency = findViewById(R.id.job_currency);
        txtLocation = findViewById(R.id.job_location);
        txtDeadline = findViewById(R.id.job_deadline);

        progress = findViewById(R.id.progress);

        checkJob();
        loadCurrencies();

        ImageView btnPicker = findViewById(R.id.job_date_picker);
        btnPicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(JobEditActivity.this, datePika, myCalendar
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

    private void checkJob(){
        if(jobDA.exist(node)){
            loadJob();
        }else{
            getJobs();
        }
    }

    private void loadJob(){

    }

    private void getJobs(){
        progress.setVisibility(View.VISIBLE);
        db.collection(Value.TABLE_JOBS)
                .whereEqualTo(Value.COLUMN_STATUS, Value.KEY_STATUS_ACTIVE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Job job = document.toObject(Job.class);
                                jobDA.refresh(job);
                            }
                            progress.setVisibility(View.GONE);
                            loadJob();
                        } else {
                            progress.setVisibility(View.GONE);
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
        pDialog = new ProgressDialog(JobEditActivity.this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("We are updating your career details.");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        Map<String,Object> updates = new HashMap<>();
        updates.put("title", txtTitle.getText().toString());
        updates.put("details", txtDetails.getText().toString());
        updates.put("location", txtLocation.getText().toString());
        updates.put("deadline", txtDeadline.getText().toString());
        updates.put("wage", txtWage.getText().toString());
        updates.put("currency", txtCurrency.getSelectedItem().toString());

        db.collection(Value.TABLE_JOBS).document(node)
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        videx.setPref(Value.COLUMN_JOB_NODE, node);
                        pDialog.dismiss();
                        finish();
                        startActivity(getIntent());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(JobEditActivity.this, "Operation failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
