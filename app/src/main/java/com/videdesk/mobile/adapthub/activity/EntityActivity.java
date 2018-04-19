package com.videdesk.mobile.adapthub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Videx;

public class EntityActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseFirestore db;
    private Videx videx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entity);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(EntityActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(EntityActivity.this);
        db = videx.getFirestore();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
