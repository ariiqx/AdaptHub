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
import com.videdesk.mobile.adapthub.adapter.NoteAdapter;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;
import com.videdesk.mobile.adapthub.model.Note;
import com.videdesk.mobile.adapthub.model.Person;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private ProgressBar progress;
    private static final String TAG = "Notes";
    private RecyclerView recyclerView;

    private String code;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private Videx videx;
    private com.github.clans.fab.FloatingActionMenu fabHome;
    private int countNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        videx = new Videx(NotesActivity.this);
        db = videx.getFirestore();
        countNotes = 0;

        videx = new Videx(NotesActivity.this);
        code = videx.getPref(Value.COLUMN_NOTE_NODE);

        recyclerView = findViewById(R.id.recycler_view);
        progress = findViewById(R.id.progress);
        recyclerView.setHasFixedSize(true);

        // Fab Home.
        fabHome = findViewById(R.id.fab_home);
        fabHome.setMenuButtonColorNormal(videx.getColor("500"));

        // Open New.
        com.github.clans.fab.FloatingActionButton fabNew = findViewById(R.id.fab_new);
        fabNew.setColorNormal(videx.getColor("500"));
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(NotesActivity.this, ProfileActivity.class));
            }
        });

        // Open Manage.
        com.github.clans.fab.FloatingActionButton fabManage = findViewById(R.id.fab_manage);
        fabManage.setColorNormal(videx.getColor("500"));
        fabManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(NotesActivity.this, MainActivity.class));
            }
        });

        switch (code) {
            case Value.KEY_NOTE_PRIVACY:
                fabNew.setLabelText("New Policy");
                fabManage.setLabelText("Manage Policies");
                getSupportActionBar().setTitle(getResources().getString(R.string.txt_privacy));
                break;
            case Value.KEY_NOTE_FAQ:
                fabNew.setLabelText("New FAQ");
                fabManage.setLabelText("Manage FAQs");
                getSupportActionBar().setTitle(getResources().getString(R.string.txt_faqs));
                break;
        }

        loadNotes();

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
        getMenuInflater().inflate(R.menu.menu_notes, menu);
        // search
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        // Menu title
        MenuItem fag = menu.findItem(R.id.menu_title);
        switch (code){
            case Value.KEY_NOTE_FAQ:
                fag.setTitle(getResources().getString(R.string.txt_privacy));
                return true;
            case Value.KEY_NOTE_PRIVACY:
                fag.setTitle(getResources().getString(R.string.txt_faqs));
                return true;
            default:
                return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_about:
                startActivity(new Intent(NotesActivity.this, AboutActivity.class));
                return true;
            case R.id.menu_title:
                switch (code){
                    case Value.KEY_NOTE_FAQ:
                        videx.setPref(Value.COLUMN_NOTE_NODE, Value.KEY_NOTE_PRIVACY);
                        reloadPage();
                        return true;
                    case Value.KEY_NOTE_PRIVACY:
                        videx.setPref(Value.COLUMN_NOTE_NODE, Value.KEY_NOTE_FAQ);
                        reloadPage();
                        return true;
                    default:
                        return true;
                }
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

                if(countNotes > 0) {
                    noteAdapter.getFilter().filter(newText);
                }

                return true;
            }
        });
    }

    private void reloadPage(){
        finish();
        startActivity(getIntent());
    }

    private void loadNotes(){
        progress.setVisibility(View.VISIBLE);
        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(NotesActivity.this, noteList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(noteAdapter);
        noteList.clear();

        db.collection(Value.TABLE_NOTES)
                .whereEqualTo("status", Value.KEY_STATUS_ACTIVE)
                .whereEqualTo("code", code)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Note note = document.toObject(Note.class);
                                noteList.add(note);
                                countNotes++;
                                noteAdapter.notifyDataSetChanged();
                            }
                            progress.setVisibility(View.GONE);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            progress.setVisibility(View.GONE);
                        }
                        noteAdapter.notifyDataSetChanged();
                    }
                });

    }

}
