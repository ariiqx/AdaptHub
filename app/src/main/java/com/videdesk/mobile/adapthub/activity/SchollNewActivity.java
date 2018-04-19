package com.videdesk.mobile.adapthub.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Scholl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SchollNewActivity extends AppCompatActivity {

    private EditText txtTitle, txtCaption, txtDeadline;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private Videx videx;

    private Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholl_new);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(SchollNewActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(SchollNewActivity.this);
        db = videx.getFirestore();

        txtTitle = findViewById(R.id.scholl_title);
        txtCaption = findViewById(R.id.scholl_caption);
        txtDeadline = findViewById(R.id.scholl_deadline);

        ImageView btnPicker = findViewById(R.id.date_picker);

        myCalendar = Calendar.getInstance();

        btnPicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SchollNewActivity.this, datePika, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btnPublish = findViewById(R.id.btn_publish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs()){
                    saveStory();
                }
            }
        });

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

        String myFormat = "yyyy-MM-dd"; //Date Format to display in input text
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtDeadline.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean checkInputs(){

        if (TextUtils.isEmpty(txtTitle.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter scholarship title!", Toast.LENGTH_SHORT).show();
            txtTitle.setError("Enter scholarship title!");
            return false;
        }
        if (TextUtils.isEmpty(txtCaption.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter scholarship description!", Toast.LENGTH_SHORT).show();
            txtCaption.setError("Enter scholarship description!");
            return false;
        }
        if (TextUtils.isEmpty(txtDeadline.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter scholarship deadline!", Toast.LENGTH_SHORT).show();
            txtDeadline.setError("Enter scholarship deadline!");
            return false;
        }

        return true;
    }

    private void saveStory(){
        final ProgressDialog pDialog = new ProgressDialog(SchollNewActivity.this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("We are submitting your entry.");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        final String node = Videx.getNode();
        String title = txtTitle.getText().toString();
        String caption = txtCaption.getText().toString();
        String image = "none";
        String deadline = txtDeadline.getText().toString();

        Scholl scholl = new Scholl(user.getUid(), node, title, caption,  image, deadline, Videx.getDatedTimed(),
                Value.KEY_STATUS_PENDING, Value.KEY_STATUS_ACTIVE);
        db.collection(Value.TABLE_SCHOLLS).document(node)
                .set(scholl)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        videx.setPref(Value.COLUMN_SCHOLL_NODE, node);
                        pDialog.dismiss();
                        startActivity(new Intent(SchollNewActivity.this, SchollEditActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(SchollNewActivity.this, "Operation failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
