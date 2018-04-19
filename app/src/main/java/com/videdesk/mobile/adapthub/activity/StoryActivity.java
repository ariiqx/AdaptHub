package com.videdesk.mobile.adapthub.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Like;
import com.videdesk.mobile.adapthub.model.Person;
import com.videdesk.mobile.adapthub.model.Story;
import com.videdesk.mobile.adapthub.model.Theme;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;
import com.videdesk.mobile.adapthub.model.sqlite.StoryDA;
import com.videdesk.mobile.adapthub.model.sqlite.ThemeDA;

public class StoryActivity extends AppCompatActivity {

    private com.google.android.youtube.player.YouTubeThumbnailView youTubeThumbnailView;
    private ProgressBar progress;
    private String node;
    private final static String TAG = "StoryActivity";

    private TextView txtDated, txtTitle, txtCaption, txtDetails, txtUrl;
    private ImageView imgImage;

    private StoryDA storyDA;
    private Story story;
    private ActionBar bar;

    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(StoryActivity.this, HomeActivity.class));
            finish();
            return;
        }

        final Videx videx = new Videx(StoryActivity.this);
        db = videx.getFirestore();
        node = videx.getPref(Value.COLUMN_STORY_NODE);
        storyDA = new StoryDA(this);

        progress = findViewById(R.id.progress);
        bar = getSupportActionBar();

        txtDated = findViewById(R.id.story_dated);
        txtTitle = findViewById(R.id.story_title);
        txtCaption = findViewById(R.id.story_caption);
        txtDetails = findViewById(R.id.story_details);
        txtUrl = findViewById(R.id.story_url);
        imgImage = findViewById(R.id.story_image);
        youTubeThumbnailView = findViewById(R.id.story_video_thumbnail);


        FloatingActionMenu fabRoom = findViewById(R.id.fab);
        fabRoom.setMenuButtonColorNormal(videx.getColor("500"));

        com.github.clans.fab.FloatingActionButton fabLike = findViewById(R.id.fab_love);
        fabLike.setColorNormal(videx.getColor("500"));
        fabLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeStory();
            }
        });

        com.github.clans.fab.FloatingActionButton fabShare = findViewById(R.id.fab_share);
        fabShare.setColorNormal(videx.getColor("500"));
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareStory();
            }
        });

        // Fab Home.
        com.github.clans.fab.FloatingActionMenu fabHome = findViewById(R.id.fab_home);
        fabHome.setMenuButtonColorNormal(videx.getColor("500"));

        // Open New.
        com.github.clans.fab.FloatingActionButton fabEdit = findViewById(R.id.fab_edit);
        fabEdit.setColorNormal(videx.getColor("500"));
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoryActivity.this, StoryEditActivity.class));
            }
        });

        // Open Manage.
        com.github.clans.fab.FloatingActionButton fabDelete = findViewById(R.id.fab_delete);
        fabDelete.setColorNormal(videx.getColor("500"));
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sureAlert();
            }
        });

        loadStory();

        PersonDA personDA = new PersonDA(this);
        Person person = personDA.find(user.getUid());
        if(person.getRole().equals(Value.KEY_USER_ADMIN)){
            fabHome.setVisibility(View.VISIBLE);
        }

        ThemeDA themeDA = new ThemeDA(StoryActivity.this);
        String theme_node = videx.getPref(Value.COLUMN_THEME_NODE);
        Theme theme = themeDA.find(theme_node);

        bar.setTitle(theme.getTitle());
        bar.setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(Videx.getResId(theme.getColor(), R.color.class))));

        txtUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videx.setPref(Value.COLUMN_BROWSE_URL, txtUrl.getText().toString());
                startActivity(new Intent(StoryActivity.this, BrowseActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        updateStory();

        youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playVideo(story.getVideo());
            }
        });

    }

    private void loadStory(){
        if(storyDA.exist(node)) {
            story = storyDA.find(node);
            progress.setVisibility(View.VISIBLE);
            txtDated.setText(story.getCreated());
            txtTitle.setText(story.getTitle());

            bar.setSubtitle(story.getTitle());

            txtCaption.setText(story.getCaption());

            txtDetails.setText(story.getDetails());
            if (story.getDetails().equals("none")) {
                txtDetails.setVisibility(View.GONE);
            }

            txtUrl.setText(story.getUrl());
            if (story.getUrl().equals("none")) {
                txtUrl.setVisibility(View.GONE);
            }

            Picasso.get()
                    .load(story.getImage())
                    .resize(612, 408)
                    .centerCrop()
                    .placeholder(R.drawable.img_item_err)
                    .error(R.drawable.img_item_err)
                    .into(imgImage);

            progress.setVisibility(View.GONE);

            if(!story.getVideo().equals("none")){
                LinearLayout linearLayout = findViewById(R.id.story_video);
                linearLayout.setVisibility(View.VISIBLE);

                youTubeThumbnailView.initialize(Value.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        youTubeThumbnailLoader.setVideo(story.getVideo());
                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                            @Override
                            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                                youTubeThumbnailLoader.release();
                            }

                            @Override
                            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                            }
                        });
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
            }

        }else{
            Toast.makeText(StoryActivity.this, "Sorry! This story has been archived so we could not process it.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(StoryActivity.this, HomeActivity.class));
            finish();
        }
    }

    private void updateStory(){
        progress.setVisibility(View.VISIBLE);
        DocumentReference asem = db.collection(Value.TABLE_STORIES).document(node);
        asem.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Story ade = documentSnapshot.toObject(Story.class);
                storyDA.refresh(ade);
                loadStory();
                progress.setVisibility(View.GONE);

            }
        });
    }

    private void cleanStory(){
        db.collection(Value.TABLE_STORIES)
                .whereEqualTo(Value.COLUMN_NODE, node)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Log.d(TAG, "New story: " + dc.getDocument().getData());
                                    Story added = dc.getDocument().toObject(Story.class);
                                    storyDA.add(added);
                                    loadStory();
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified story: " + dc.getDocument().getData());
                                    Story modified = dc.getDocument().toObject(Story.class);
                                    storyDA.update(modified);
                                    loadStory();
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed story: " + dc.getDocument().getData());
                                    Story removed = dc.getDocument().toObject(Story.class);
                                    storyDA.delete(removed.getNode());
                                    startActivity(new Intent(StoryActivity.this, StoriesActivity.class));
                                    break;
                            }
                        }

                    }
                });
    }

    private void likeStory(){
        final ProgressDialog pDialog = new ProgressDialog(StoryActivity.this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("We are updating your Likes Vault.");
        pDialog.setCancelable(true);
        pDialog.show();

        String mode = Videx.getNode();
        Like like = new Like(user.getUid(), mode, node);

        db.collection(Value.TABLE_LIKES).document(mode)
                .set(like)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.dismiss();
                        Toast.makeText(StoryActivity.this, "Operation successful. Your Likes Vault has been updated.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(StoryActivity.this, "Operation failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void shareStory(){

        String body = "Adaptation Hub\n===========================" +
                "\n\n" + story.getTitle() +
                "\n\n" + story.getCaption() +
                "\n\nTo view more details on this story, get the Adaptation Hub mobile app on Google play store: " +
                "\nhttps://play.google.com/store/apps/details?id=com.videdesk.mobile.adapthub" +
                "\n\nDeveloped by: Vide Desk http://www.videdesk.com/";

        String title = "Share Story";
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, body);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, title));
    }

    private void sureAlert(){
        String title = "Delete Story";
        String message = "Are you sure you want to delete this story completely? You will not be able to undo this operation.";
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(StoryActivity.this, android.R.style.Theme_Material_Dialog_NoActionBar);
        } else {
            builder = new AlertDialog.Builder(StoryActivity.this);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteStory();
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

    private void deleteStory(){
        final ProgressDialog pDialog = new ProgressDialog(StoryActivity.this);
        pDialog.setTitle("Please wait...");
        pDialog.setMessage("We are deleting the story from the central database.");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
        Videx videx = new Videx(StoryActivity.this);
        FirebaseFirestore db = videx.getFirestore();
        DocumentReference histRef = db.collection(Value.TABLE_STORIES).document(node);
        histRef.update(Value.COLUMN_STATUS, Value.KEY_STATUS_DELETED)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.dismiss();
                        Toast.makeText(StoryActivity.this, "The story has been deleted.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(StoryActivity.this, StoriesActivity.class));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(StoryActivity.this, "Error performing the operation. Please try again later!", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void playVideo(String video_id){
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(StoryActivity.this, Value.YOUTUBE_API_KEY, video_id);
        startActivity(intent);
    }

}
