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
import com.videdesk.mobile.adapthub.adapter.SchollAdapter;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;
import com.videdesk.mobile.adapthub.model.sqlite.SchollDA;
import com.videdesk.mobile.adapthub.model.Person;
import com.videdesk.mobile.adapthub.model.Scholl;

import java.util.List;

public class SchollsActivity extends AppCompatActivity {

    private ProgressBar progress;
    private static final String TAG = "Scholls";
    private int countScholls;

    private RecyclerView recyclerView;
    private SchollAdapter schollAdapter;
    private SchollDA schollDA;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private Videx videx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholls);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(SchollsActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(SchollsActivity.this);
        db = videx.getFirestore();
        schollDA = new SchollDA(SchollsActivity.this);
        countScholls = schollDA.count();

        recyclerView = findViewById(R.id.recycler_view);
        progress = findViewById(R.id.progress);
        recyclerView.setHasFixedSize(true);

        checkScholls();

        // Fab Home.
        com.github.clans.fab.FloatingActionMenu fabHome = findViewById(R.id.fab_home);
        fabHome.setMenuButtonColorNormal(videx.getColor("500"));

        // Open New.
        com.github.clans.fab.FloatingActionButton fabNew = findViewById(R.id.fab_new);
        fabNew.setColorNormal(videx.getColor("500"));
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SchollsActivity.this, SchollNewActivity.class));
            }
        });

        /*
        // Open Manage.
        com.github.clans.fab.FloatingActionButton fabManage = findViewById(R.id.fab_manage);
        fabManage.setColorNormal(videx.getColor("500"));
        fabManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(EntitiesActivity.this, MessagesActivity.class));
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
                startActivity(new Intent(SchollsActivity.this, AboutActivity.class));
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

                if(countScholls > 0) {
                    schollAdapter.getFilter().filter(newText);
                }

                return true;
            }
        });
    }

    private void checkScholls(){
        if(countScholls > 0){
            loadScholls();
            getScholls();
        }else{
            getScholls();
        }
    }

    private void loadScholls(){
        List<Scholl> schollList = schollDA.all();
        schollAdapter = new SchollAdapter(SchollsActivity.this, schollList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(schollAdapter);
     }

    private void getScholls(){
        progress.setVisibility(View.VISIBLE);
        db.collection(Value.TABLE_SCHOLLS)
                .whereEqualTo(Value.COLUMN_STATUS, Value.KEY_STATUS_ACTIVE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Scholl scholl = document.toObject(Scholl.class);
                                schollDA.refresh(scholl);
                            }
                            progress.setVisibility(View.GONE);
                            loadScholls();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            progress.setVisibility(View.GONE);
                        }
                        schollAdapter.notifyDataSetChanged();
                    }
                });
    }
}
