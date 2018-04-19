package com.videdesk.mobile.adapthub.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.CircleTransform;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Person;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;

public class ProfileActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ProgressBar progressBar;
    private Button btnAdmin;

    private TextView txtName, txtEmail, txtPhone, txtGender, txtLocate, txtRegion, txtNation, txtCreated;
    private ImageView imgPhoto;

    private Uri imgPath, imgUploadUri;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private StorageReference storage;
    private PersonDA personDA;

    private String imgDownloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            finish();
            return;
        }

        Videx videx = new Videx(ProfileActivity.this);
        db = videx.getFirestore();
        storage = videx.getStorage();

        txtName = findViewById(R.id.prof_name);
        txtEmail = findViewById(R.id.prof_email);
        txtPhone = findViewById(R.id.prof_phone);
        txtGender = findViewById(R.id.prof_gender);
        txtLocate = findViewById(R.id.prof_location);
        txtRegion = findViewById(R.id.prof_region);
        txtNation = findViewById(R.id.prof_nation);
        txtCreated = findViewById(R.id.prof_created);
        imgPhoto = findViewById(R.id.prof_photo);

        progressBar = findViewById(R.id.prof_progress);

        personDA = new PersonDA(ProfileActivity.this);
        imgDownloadUrl = "none";
        imgUploadUri = null;

        Button btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, EditActivity.class));
            }
        });

        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        Button btnDelete = findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });


        btnAdmin = findViewById(R.id.btn_admin);
        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, AdminActivity.class));
            }
        });

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectImageClick(v);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getPerson();
    }

    private void getPerson(){
        if(personDA.exist(user.getUid())) {
            loadPerson();
            refreshPerson();
        }else{
            refreshPerson();
        }
    }

    private void loadPerson(){
        Person person = personDA.find(user.getUid());
        txtName.setText(person.getName());
        txtEmail.setText(person.getEmail());
        txtPhone.setText(person.getPhone());
        txtGender.setText(person.getGender());
        txtLocate.setText(person.getLocation());
        txtRegion.setText(person.getRegion());
        txtNation.setText(person.getNation());
        txtCreated.setText(person.getCreated());

        Picasso.get()
                .load(person.getImage())
                .resize(360, 360)
                .centerCrop()
                .transform(new CircleTransform())
                .placeholder(R.drawable.img_profile)
                .error(R.drawable.img_profile)
                .into(imgPhoto);

        if(person.getRole().equals(Value.KEY_USER_ADMIN))
            btnAdmin.setVisibility(View.VISIBLE);
    }

    private void refreshPerson(){
        progressBar.setVisibility(View.VISIBLE);
        DocumentReference profile = db.collection(Value.TABLE_PEOPLE).document(user.getUid());
        profile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Person person = documentSnapshot.toObject(Person.class);
                personDA.update(person);
                loadPerson();
                progressBar.setVisibility(View.GONE);

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
                imgPhoto.setImageURI(result.getUri());
                imgUploadUri = result.getUri();
                uploadPhoto();
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
            Toast.makeText(ProfileActivity.this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
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
                .setMinCropResultSize(360,360)
                .setMaxCropResultSize(360,360)
                .start(this);
    }

    private void uploadPhoto(){
        if(imgUploadUri != null) {
            pDialog = new ProgressDialog(ProfileActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage("Uploading profile photo.");
            pDialog.show();

            UploadTask uploadTask = storage.child(Value.KEY_DIR_PHOTOS).child(user.getUid()).putFile(imgUploadUri);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    pDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Upload failed! Try again later.", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    imgDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                    pDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "Upload successful! Updating profile details...", Toast.LENGTH_SHORT).show();
                    updateProfile();
                }
            });
        }
    }

    private void updateProfile(){
        if(!imgDownloadUrl.equals("none")) {
            DocumentReference profile = db.collection(Value.TABLE_PEOPLE).document(user.getUid());
            profile.update(Value.COLUMN_IMAGE, imgDownloadUrl).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressBar.setVisibility(View.GONE);
                    refreshPerson();
                    Toast.makeText(ProfileActivity.this, "Upload failed! Try again later.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, "Upload failed! Try again later.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void authAlert(String title, String message){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ProfileActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(ProfileActivity.this);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void logout(){
        String title = "Logout Account";
        String message = "Are you sure you want to logout of your user account on this device? You can always login again on any device.";
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ProfileActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(ProfileActivity.this);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout(false);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void delete(){
        String title = "Delete Account";
        String message = "Are you sure you want to delete your user account completely? You can always register again on any device.";
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(ProfileActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(ProfileActivity.this);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        logout(true);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void logout(final boolean delete) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        if(delete){
                            user.delete();
                        }
                        Toast.makeText(ProfileActivity.this, "Operation successful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                        finish();
                    }
                });
    }
}
