package com.videdesk.mobile.adapthub.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Story;
import com.videdesk.mobile.adapthub.model.Theme;
import com.videdesk.mobile.adapthub.model.sqlite.StoryDA;
import com.videdesk.mobile.adapthub.model.sqlite.ThemeDA;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoryEditActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ProgressBar progress;
    private String node;
    private Uri imgPath, imgUploadUri;
    private String imgDownloadUri;

    private Spinner cboTheme;
    private TextView txtTitle, txtCaption, txtDetails, txtURL, txtVideo, txtDocx;
    private ImageView imgImage;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private StorageReference storage;
    private ThemeDA themeDA;
    private StoryDA storyDA;


    private Videx videx;
    private Story story;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_edit);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(StoryEditActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(StoryEditActivity.this);
        db = videx.getFirestore();
        storage = videx.getStorage();
        themeDA = new ThemeDA(StoryEditActivity.this);
        storyDA = new StoryDA(StoryEditActivity.this);

        node = videx.getPref(Value.COLUMN_STORY_NODE);
        story = storyDA.find(node);

        progress = findViewById(R.id.progress);
        pDialog = new ProgressDialog(StoryEditActivity.this);

        imgUploadUri = null;
        imgDownloadUri = "none";

        cboTheme = findViewById(R.id.story_theme);
        txtTitle = findViewById(R.id.story_title);
        txtCaption = findViewById(R.id.story_caption);
        txtDetails = findViewById(R.id.story_details);
        txtURL = findViewById(R.id.story_url);
        txtVideo = findViewById(R.id.story_video);
        txtDocx = findViewById(R.id.story_docx);

        imgImage = findViewById(R.id.story_image);
        imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });

        loadStory();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Button btnPublish = findViewById(R.id.btn_publish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs()){
                    updateImage();
                }
            }
        });

    }

    private void loadStory(){
        progress.setVisibility(View.VISIBLE);
        txtTitle.setText(story.getTitle());
        txtCaption.setText(story.getCaption());
        txtDetails.setText(story.getDetails());
        txtURL.setText(story.getUrl());
        txtVideo.setText(story.getVideo());
        txtDocx.setText(story.getDocument());

        Theme them = themeDA.find(story.getTheme_node());
        List<String> spinnerArray =  new ArrayList<>();
        spinnerArray.add(them.getTitle());
        List<Theme> themes = themeDA.all();
        for(Theme theme: themes) {
            spinnerArray.add(theme.getTitle());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(StoryEditActivity.this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboTheme.setAdapter(adapter);

        Picasso.get()
                .load(story.getImage())
                .resize(612, 408)
                .centerCrop()
                .placeholder(R.drawable.img_item)
                .error(R.drawable.img_item)
                .into(imgImage);

        imgImage.setVisibility(View.VISIBLE);
        LinearLayout linBtn = findViewById(R.id.lin_btn);
        linBtn.setVisibility(View.VISIBLE);

        progress.setVisibility(View.GONE);
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

        pDialog.setTitle("Please wait...");
        pDialog.setMessage("We are updating your story.");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        String theme_title = cboTheme.getSelectedItem().toString();
        Theme theem = themeDA.get(Value.COLUMN_TITLE, theme_title);

        Map<String,Object> updates = new HashMap<>();
        updates.put(Value.COLUMN_TITLE, txtTitle.getText().toString());
        updates.put(Value.COLUMN_CAPTION, txtCaption.getText().toString());
        updates.put(Value.COLUMN_DETAILS, txtDetails.getText().toString());
        updates.put(Value.COLUMN_URL, txtURL.getText().toString());
        updates.put(Value.COLUMN_STATUS, Value.KEY_STATUS_ACTIVE);
        updates.put(Value.COLUMN_THEME_NODE, theem.getNode());
        updates.put(Value.COLUMN_VIDEO, txtVideo.getText().toString());
        updates.put(Value.COLUMN_DOCUMENT, txtDocx.getText().toString());

        db.collection(Value.TABLE_STORIES).document(node)
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        videx.setPref(Value.COLUMN_STORY_NODE, node);
                        pDialog.dismiss();
                        Toast.makeText(StoryEditActivity.this, "Update successful. You may add another story.", Toast.LENGTH_SHORT).show();
                        startActivity(getIntent());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(StoryEditActivity.this, "Operation failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    /**
     * Start pick image activity with chooser.
     */
    public void onSelectImageClick(View view) {
        CropImage.startPickImageActivity(this);
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                imgPath = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgImage.setImageURI(result.getUri());
                imgUploadUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Image upload failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (imgPath != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(imgPath);
        } else {
            Toast.makeText(StoryEditActivity.this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setShowCropOverlay(true)
                .setMinCropResultSize(612,408)
                .setMaxCropResultSize(612, 408)
                .start(StoryEditActivity.this);
    }


    private void updateImage(){

        if(imgUploadUri != null) {
            pDialog = new ProgressDialog(StoryEditActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage("Uploading story image.");
            pDialog.show();

            UploadTask uploadTask = storage.child(Value.KEY_DIR_IMAGES).child(node).putFile(imgUploadUri);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    pDialog.dismiss();
                    Toast.makeText(StoryEditActivity.this, "Upload failed! Try again later.",Toast.LENGTH_SHORT).show();
                    saveStory();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    imgDownloadUri = taskSnapshot.getDownloadUrl().toString();
                    DocumentReference mfoni = db.collection(Value.TABLE_STORIES).document(node);
                    mfoni.update(Value.COLUMN_IMAGE, imgDownloadUri).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pDialog.dismiss();
                            Toast.makeText(StoryEditActivity.this, "Update successful! Finishing things up...", Toast.LENGTH_SHORT).show();
                            saveStory();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pDialog.dismiss();
                            Toast.makeText(StoryEditActivity.this, "Update failed! Try again later.", Toast.LENGTH_SHORT).show();
                            saveStory();
                        }
                    });
                }
            });
        }else{
            Toast.makeText(StoryEditActivity.this, "No Upload! No image selected for this story...", Toast.LENGTH_SHORT).show();
            saveStory();
        }
    }

    private void uploadPhoto(){
        pDialog = new ProgressDialog(StoryEditActivity.this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("Uploading story image.");
        pDialog.show();

        // Get the data from an ImageView as bytes
        imgImage.setDrawingCacheEnabled(true);
        imgImage.buildDrawingCache();
        Bitmap bitmap = imgImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storage.child(Value.KEY_DIR_IMAGES).child(node).putBytes(data);
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                pDialog.setMessage("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
                pDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                pDialog.dismiss();
                Toast.makeText(StoryEditActivity.this, "Upload failed! Try again later.",Toast.LENGTH_SHORT).show();
                saveStory();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                imgDownloadUri = taskSnapshot.getDownloadUrl().toString();
                DocumentReference mfoni = db.collection(Value.TABLE_STORIES).document(node);
                mfoni.update(Value.COLUMN_IMAGE, imgDownloadUri).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.dismiss();
                        Toast.makeText(StoryEditActivity.this, "Update successful! Finishing things up...", Toast.LENGTH_SHORT).show();
                        saveStory();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(StoryEditActivity.this, "Update failed! Try again later.", Toast.LENGTH_SHORT).show();
                        saveStory();
                    }
                });
            }
        });
    }

}
