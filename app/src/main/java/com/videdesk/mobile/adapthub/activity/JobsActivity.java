package com.videdesk.mobile.adapthub.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.adapter.JobAdapter;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.sqlite.JobDA;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;
import com.videdesk.mobile.adapthub.model.Job;
import com.videdesk.mobile.adapthub.model.Person;

import java.util.List;

public class JobsActivity extends AppCompatActivity {

    private ProgressBar progress;
    private static final String TAG = "Stories";

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private JobDA jobDA;
    private int countJobs;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private Videx videx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(JobsActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(JobsActivity.this);
        db = videx.getFirestore();
        jobDA = new JobDA(JobsActivity.this);

        countJobs = jobDA.count();

        recyclerView = findViewById(R.id.recycler_view);
        progress = findViewById(R.id.progress);
        recyclerView.setHasFixedSize(true);

        checkJobs();

        // Fab Home.
        com.github.clans.fab.FloatingActionMenu fabHome = findViewById(R.id.fab_home);
        fabHome.setMenuButtonColorNormal(videx.getColor("500"));

        // Open New.
        com.github.clans.fab.FloatingActionButton fabNew = findViewById(R.id.fab_new);
        fabNew.setColorNormal(videx.getColor("500"));
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JobsActivity.this, JobNewActivity.class));
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        PersonDA personDA = new PersonDA(this);
        if(personDA.count() > 0) {
            Person person = personDA.find(user.getUid());
            if (person.getRole().equals(Value.KEY_USER_ADMIN)) {
                fabHome.setVisibility(View.VISIBLE);
            }
        }
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
                startActivity(new Intent(JobsActivity.this, AboutActivity.class));
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

                if(countJobs > 0) {
                    jobAdapter.getFilter().filter(newText);
                }

                return true;
            }
        });
    }

    private void checkJobs(){
        if(countJobs > 0){
            loadJobs();
            getJobs();
        }else{
            getJobs();
        }
    }

    private void loadJobs(){
        List<Job> jobList = jobDA.all();
        jobAdapter = new JobAdapter(JobsActivity.this, jobList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(jobAdapter);
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
                            loadJobs();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            progress.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
