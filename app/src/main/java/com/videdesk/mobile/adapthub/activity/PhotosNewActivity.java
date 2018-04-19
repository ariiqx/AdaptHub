package com.videdesk.mobile.adapthub.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Album;

public class PhotosNewActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private Uri imgPath, imgUploadUrl;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private StorageReference storage;

    private TextView txtTitle;
    private ImageView imgImage;
    private String imgDownloadUrl, node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_new);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(PhotosNewActivity.this, HomeActivity.class));
            finish();
            return;
        }
        Videx videx = new Videx(PhotosNewActivity.this);
        db = videx.getFirestore();
        storage = videx.getStorage();
        imgDownloadUrl = "none";
        imgUploadUrl = null;
        txtTitle = findViewById(R.id.album_title);
        imgImage = findViewById(R.id.album_image);

        imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(txtTitle.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter album title!", Toast.LENGTH_SHORT).show();
                    txtTitle.setError("Enter album title!");
                }else{
                    onSelectImageClick(view);
                }
            }
        });

        Button btnPublish = findViewById(R.id.btn_publish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(txtTitle.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter album title!", Toast.LENGTH_SHORT).show();
                    txtTitle.setError("Enter album title!");
                }else{
                    uploadImage();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                imgUploadUrl = result.getUri();
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
            Toast.makeText(PhotosNewActivity.this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
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
                .setMaxCropResultSize(612,408)
                .start(this);
    }

    private void uploadImage(){

        node = Videx.getNode();

        if(imgUploadUrl != null){
            pDialog = new ProgressDialog(PhotosNewActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage("Uploading album cover image.");
            pDialog.setCancelable(true);
            pDialog.show();

            UploadTask uploadTask = storage.child(Value.KEY_DIR_IMAGES).child(node).putFile(imgUploadUrl);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    pDialog.dismiss();
                    Toast.makeText(PhotosNewActivity.this, "Upload failed! Please try again later.", Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    imgDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                    pDialog.dismiss();
                    Toast.makeText(PhotosNewActivity.this, "Upload successful! Setting things ready for album galleries.", Toast.LENGTH_LONG).show();
                    saveAlbum();
                }
            });
        }
    }
    private void saveAlbum(){

        pDialog = new ProgressDialog(PhotosNewActivity.this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("Uploading album cover image.");
        pDialog.setCancelable(true);
        pDialog.show();

        Album album  = new Album(node, user.getUid(), Videx.getDatedTimed(), txtTitle.getText().toString(), imgDownloadUrl, Value.KEY_STATUS_ACTIVE, Value.KEY_READ_NO);

        db.collection(Value.TABLE_ALBUMS).document(node)
                .set(album)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.dismiss();
                        Videx videx = new Videx(PhotosNewActivity.this);
                        videx.setPref(Value.COLUMN_ALBUM_NODE, node);
                        startActivity(new Intent(PhotosNewActivity.this, PhotosAddActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(PhotosNewActivity.this, "Operation failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
