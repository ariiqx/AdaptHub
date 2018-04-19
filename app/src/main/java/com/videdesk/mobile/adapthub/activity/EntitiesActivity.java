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
import com.videdesk.mobile.adapthub.adapter.EntityAdapter;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.sqlite.EntityDA;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;
import com.videdesk.mobile.adapthub.model.Entity;
import com.videdesk.mobile.adapthub.model.Person;

import java.util.List;

public class EntitiesActivity extends AppCompatActivity {

    private ProgressBar progress;
    private static final String TAG = "Entities";

    private RecyclerView recyclerView;
    private EntityAdapter entityAdapter;
    private List<Entity> entityList;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private EntityDA entityDA;

    private Videx videx;
    private com.github.clans.fab.FloatingActionMenu fabHome;
    private int countEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entities);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(EntitiesActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(EntitiesActivity.this);
        db = videx.getFirestore();
        entityDA = new EntityDA(EntitiesActivity.this);
        countEntities = entityDA.count();

        recyclerView = findViewById(R.id.recycler_view);
        progress = findViewById(R.id.progress);
        recyclerView.setHasFixedSize(true);

        checkEntities();

        // Fab Home.
        com.github.clans.fab.FloatingActionMenu fabHome = findViewById(R.id.fab_home);
        fabHome.setMenuButtonColorNormal(videx.getColor("500"));

        // Open New.
        com.github.clans.fab.FloatingActionButton fabNew = findViewById(R.id.fab_new);
        fabNew.setColorNormal(videx.getColor("500"));
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntitiesActivity.this, EntitiesNewActivity.class));
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
                startActivity(new Intent(EntitiesActivity.this, AboutActivity.class));
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

                if(countEntities > 0) {
                    entityAdapter.getFilter().filter(newText);
                }

                return true;
            }
        });
    }

    private void checkEntities(){
        if(countEntities > 0){
            loadEntities();
            getEntities();
        }else{
            getEntities();
        }
    }

    private void loadEntities(){

        entityList = entityDA.all();
        entityAdapter = new EntityAdapter(EntitiesActivity.this, entityList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(entityAdapter);
    }

    private void getEntities(){
        progress.setVisibility(View.VISIBLE);
        db.collection(Value.TABLE_ENTITIES)
                .whereEqualTo(Value.COLUMN_STATUS, Value.KEY_STATUS_ACTIVE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Entity entity = document.toObject(Entity.class);
                                entityDA.refresh(entity);
                            }
                            progress.setVisibility(View.GONE);
                            loadEntities();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            progress.setVisibility(View.GONE);
                        }
                    }
                });
    }

}
