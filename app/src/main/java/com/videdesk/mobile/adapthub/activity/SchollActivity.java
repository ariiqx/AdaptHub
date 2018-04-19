package com.videdesk.mobile.adapthub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Scholl;

public class SchollActivity extends AppCompatActivity {

    private ProgressBar progress;
    private String node;

    private TextView txtTitle, txtCaption, txtDeadline;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private Videx videx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholl);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(SchollActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(SchollActivity.this);
        db = videx.getFirestore();

        node = videx.getPref(Value.COLUMN_SCHOLL_NODE);
        progress = findViewById(R.id.progress);

        txtDeadline = findViewById(R.id.scholl_deadline);
        txtTitle = findViewById(R.id.scholl_title);
        txtCaption = findViewById(R.id.scholl_caption);
        getScholl();
    }

    private void getScholl(){
        progress.setVisibility(View.VISIBLE);
        DocumentReference profile = db.collection(Value.TABLE_SCHOLLS).document(node);
        profile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    Scholl scholl = documentSnapshot.toObject(Scholl.class);
                    txtDeadline.setText(scholl.getDeadline());
                    txtTitle.setText(scholl.getTitle());
                    txtCaption.setText(scholl.getCaption());

                }
                progress.setVisibility(View.GONE);

            }
        });
    }
}
