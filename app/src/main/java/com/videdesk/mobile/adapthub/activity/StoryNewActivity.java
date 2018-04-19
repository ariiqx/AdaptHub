package com.videdesk.mobile.adapthub.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.videdesk.mobile.adapthub.model.sqlite.ThemeDA;
import com.videdesk.mobile.adapthub.model.Story;
import com.videdesk.mobile.adapthub.model.Theme;

import java.util.ArrayList;
import java.util.List;

public class StoryNewActivity extends AppCompatActivity {

    private Spinner cboTheme;
    private EditText txtTitle, txtCaption, txtDetails, txtURL, txtVideo, txtDocx;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private ThemeDA themeDA;

    private Videx videx;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_new);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(StoryNewActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(StoryNewActivity.this);
        db = videx.getFirestore();
        themeDA = new ThemeDA(StoryNewActivity.this);

        cboTheme = findViewById(R.id.story_theme);
        txtTitle = findViewById(R.id.story_title);
        txtCaption = findViewById(R.id.story_caption);
        txtDetails = findViewById(R.id.story_details);
        txtURL = findViewById(R.id.story_url);
        txtVideo = findViewById(R.id.story_video);
        txtDocx = findViewById(R.id.story_docx);

        loadThemes();

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

    private void loadThemes(){
        String node = videx.getPref(Value.COLUMN_THEME_NODE);
        Theme them = themeDA.find(node);
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add(them.getTitle());
        List<Theme> themes = themeDA.all();
        for(Theme theme: themes) {
            spinnerArray.add(theme.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(StoryNewActivity.this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboTheme.setAdapter(adapter);
    }

    private boolean checkInputs(){

        if (TextUtils.isEmpty(txtTitle.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter story title!", Toast.LENGTH_SHORT).show();
            txtTitle.setError("Enter story title!");
            return false;
        }
        if (TextUtils.isEmpty(txtCaption.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Enter story summary!", Toast.LENGTH_SHORT).show();
            txtCaption.setError("Enter summary title!");
            return false;
        }

        return true;
    }

    private void saveStory(){
        pDialog = new ProgressDialog(StoryNewActivity.this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("We are submitting your career post.");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        final String node = Videx.getNode();
        String title = txtTitle.getText().toString();
        String caption = txtCaption.getText().toString();
        String details = txtDetails.getText().toString();
        if (TextUtils.isEmpty(txtDetails.getText().toString())) {
            details = "none";
        }
        String image = "none";
        String video = txtVideo.getText().toString();
        String docx = txtDocx.getText().toString();
        String album_node = "none";
        String url =  txtURL.getText().toString();
        if (TextUtils.isEmpty(txtURL.getText().toString())) {
            url = "none";
        }

        String theme_title = cboTheme.getSelectedItem().toString();
        Theme theme = themeDA.get(Value.COLUMN_TITLE, theme_title);

        Story story = new Story(user.getUid(), node, theme.getNode(), title, caption, details, image, video, docx, album_node, url,
                Videx.getDatedTimed(), Value.KEY_STATUS_ACTIVE, Value.KEY_READ_NO);

        db.collection(Value.TABLE_STORIES).document(node)
                .set(story)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        videx.setPref(Value.COLUMN_STORY_NODE, node);
                        pDialog.dismiss();
                        startActivity(new Intent(StoryNewActivity.this, StoryEditActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(StoryNewActivity.this, "Operation failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
