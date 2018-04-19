package com.videdesk.mobile.adapthub.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.videdesk.mobile.adapthub.adapter.PhotoAdapter;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Album;
import com.videdesk.mobile.adapthub.model.sqlite.AlbumDA;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;
import com.videdesk.mobile.adapthub.model.sqlite.PhotoDA;
import com.videdesk.mobile.adapthub.model.Person;
import com.videdesk.mobile.adapthub.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotosActivity extends AppCompatActivity {

    private ProgressBar progress;
    private static final String TAG = "Photos";
    private String node;

    private PhotoAdapter photoAdapter;

    private RecyclerView recyclerView;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private PhotoDA photoDA;
    private Videx videx;

    private int countPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(PhotosActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(PhotosActivity.this);
        db = videx.getFirestore();
        photoDA = new PhotoDA(PhotosActivity.this);
        countPhotos = photoDA.count();
        node = videx.getPref(Value.COLUMN_ALBUM_NODE);

        recyclerView = findViewById(R.id.recycler_view);
        progress = findViewById(R.id.progress);
        recyclerView.setHasFixedSize(true);

        // Fab Home.
        com.github.clans.fab.FloatingActionMenu fabHome = findViewById(R.id.fab_home);
        fabHome.setMenuButtonColorNormal(videx.getColor("500"));

        // Open New.
        com.github.clans.fab.FloatingActionButton fabNew = findViewById(R.id.fab_album);
        fabNew.setColorNormal(videx.getColor("500"));
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhotosActivity.this, PhotosNewActivity.class));
            }
        });

        // Open Manage.
        com.github.clans.fab.FloatingActionButton fabEdit = findViewById(R.id.fab_edit);
        fabEdit.setColorNormal(videx.getColor("500"));
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhotosActivity.this, PhotosEditActivity.class));
            }
        });

        // Open Manage.
        com.github.clans.fab.FloatingActionButton fabManage = findViewById(R.id.fab_photos);
        fabManage.setColorNormal(videx.getColor("500"));
        fabManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhotosActivity.this, PhotosAddActivity.class));
            }
        });

        checkPhotos();

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
                startActivity(new Intent(PhotosActivity.this, AboutActivity.class));
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

                if(countPhotos > 0) {
                    photoAdapter.getFilter().filter(newText);
                }

                return true;
            }
        });
    }

    private void checkPhotos(){
        if(countPhotos > 0){
            loadPhotos();
            getPhotos();
        }else{
            getPhotos();
        }
    }

    private void loadPhotos(){
        final ArrayList<Photo> photoList = new ArrayList<>();
        photoAdapter = new PhotoAdapter(PhotosActivity.this, photoList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(photoAdapter);
        photoList.clear();

        recyclerView.addOnItemTouchListener(new PhotoAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new PhotoAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("images", photoList);
                bundle.putInt("position", position);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                SlidesFragment newFragment = SlidesFragment.newInstance();
                newFragment.setArguments(bundle);
                newFragment.show(ft, "slideshow");
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        AlbumDA albumDA = new AlbumDA(PhotosActivity.this);
        Album album = albumDA.find(node);
        Photo foto = new Photo(album.getNode(), album.getNode(), album.getCreated(), album.getCaption(), album.getImage());
        photoList.add(foto);

        List<Photo> photos = photoDA.byAlbum(node);
        photoList.addAll(photos);
        photoAdapter.notifyDataSetChanged();
    }

    private void getPhotos(){
        progress.setVisibility(View.VISIBLE);
        db.collection(Value.TABLE_PHOTOS)
                .whereEqualTo(Value.COLUMN_STATUS, Value.KEY_STATUS_ACTIVE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Photo photo = document.toObject(Photo.class);
                                photoDA.refresh(photo);
                            }
                            progress.setVisibility(View.GONE);
                            loadPhotos();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            progress.setVisibility(View.GONE);
                        }
                    }
                });
    }

}
