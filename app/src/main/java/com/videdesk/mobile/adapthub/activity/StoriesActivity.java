package com.videdesk.mobile.adapthub.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.adapter.StoryAdapter;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;
import com.videdesk.mobile.adapthub.model.sqlite.StoryDA;
import com.videdesk.mobile.adapthub.model.sqlite.ThemeDA;
import com.videdesk.mobile.adapthub.model.Person;
import com.videdesk.mobile.adapthub.model.Story;
import com.videdesk.mobile.adapthub.model.Theme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoriesActivity extends AppCompatActivity {

    private ProgressDialog pDialog;
    private ProgressBar progress;
    private static final String TAG = "Stories";

    private String node;
    private RecyclerView recyclerView;

    private StoryAdapter storyAdapter;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private StoryDA storyDA;

    private int countStories;
    private Videx videx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(StoriesActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(StoriesActivity.this);
        pDialog = new ProgressDialog(StoriesActivity.this);

        db = videx.getFirestore();
        storyDA = new StoryDA(this);
        countStories = storyDA.count();

        node = videx.getPref(Value.COLUMN_THEME_NODE);

        ThemeDA themeDA = new ThemeDA(StoriesActivity.this);
        Theme theme = themeDA.find(node);

        ActionBar bar = getSupportActionBar();
        bar.setTitle(theme.getTitle());
        bar.setSubtitle(theme.getCaption());
        bar.setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(Videx.getResId(theme.getColor(), R.color.class))));


        recyclerView = findViewById(R.id.recycler_view);
        progress = findViewById(R.id.progress);
        recyclerView.setHasFixedSize(true);

        checkStories();

        // Fab Home.
        com.github.clans.fab.FloatingActionMenu fabHome = findViewById(R.id.fab_home);
        fabHome.setMenuButtonColorNormal(videx.getColor("500"));

        // Open New.
        com.github.clans.fab.FloatingActionButton fabNew = findViewById(R.id.fab_new);
        fabNew.setColorNormal(videx.getColor("500"));
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StoriesActivity.this, StoryNewActivity.class));
            }
        });

        /*
        // Open Manage.
        com.github.clans.fab.FloatingActionButton fabManage = findViewById(R.id.fab_manage);
        fabManage.setColorNormal(videx.getColor("500"));
        fabManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(StoriesActivity.this, MessagesActivity.class));
            }
        });
        */

        PersonDA personDA = new PersonDA(this);
        if(personDA.count() > 0) {
            Person person = personDA.find(user.getUid());
            if (person.getRole().equals(Value.KEY_USER_ADMIN)) {
                fabHome.setVisibility(View.VISIBLE);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_about:
                startActivity(new Intent(StoriesActivity.this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(countStories > 0) {
                    storyAdapter.getFilter().filter(newText);
                }

                return true;
            }
        });
    }

    private void checkStories(){
        if(countStories > 0){
            loadStories();
            getStories();
        }else{
            getStories();
        }
    }

    private void loadStories(){
        List<Story> storyList = storyDA.byTheme(node);
        storyAdapter = new StoryAdapter(StoriesActivity.this, storyList, user.getUid());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(storyAdapter);
    }

    private void getStories(){
        progress.setVisibility(View.VISIBLE);

        db.collection(Value.TABLE_STORIES)
                .whereEqualTo(Value.COLUMN_STATUS, Value.KEY_STATUS_ACTIVE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Story story = document.toObject(Story.class);
                                storyDA.refresh(story);

                            }
                            progress.setVisibility(View.GONE);
                            loadStories();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            progress.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private void updateStory(Story story){

        pDialog.setTitle("Please wait...");
        pDialog.setMessage("We are updating your story.");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        Map<String,Object> updates = new HashMap<>();
        updates.put("video", "none");
        updates.put("document", "none");

        db.collection(Value.TABLE_STORIES).document(story.getNode())
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        Toast.makeText(StoriesActivity.this, "Operation failed. Try again later.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
