package com.videdesk.mobile.adapthub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.adapter.AlbumAdapter;
import com.videdesk.mobile.adapthub.adapter.EventAdapter;
import com.videdesk.mobile.adapthub.adapter.StoryAdapter;
import com.videdesk.mobile.adapthub.adapter.ThemeAdapter;
import com.videdesk.mobile.adapthub.config.CircleTransform;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.Album;
import com.videdesk.mobile.adapthub.model.Event;
import com.videdesk.mobile.adapthub.model.Person;
import com.videdesk.mobile.adapthub.model.Story;
import com.videdesk.mobile.adapthub.model.Theme;
import com.videdesk.mobile.adapthub.model.sqlite.AlbumDA;
import com.videdesk.mobile.adapthub.model.sqlite.EventDA;
import com.videdesk.mobile.adapthub.model.sqlite.PersonDA;
import com.videdesk.mobile.adapthub.model.sqlite.StoryDA;
import com.videdesk.mobile.adapthub.model.sqlite.ThemeDA;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ProgressBar progress;
    private RecyclerView recyclerView;

    private ThemeAdapter themeAdapter;
    private AlbumAdapter albumAdapter;
    private StoryAdapter storyAdapter;
    private EventAdapter eventAdapter;

    private int countThemes, countGalleries, countStories, countEvents, pg;
    private com.github.clans.fab.FloatingActionMenu fabMenu;
    private com.github.clans.fab.FloatingActionButton fabNew, fabManage;

    private FirebaseUser user;
    private FirebaseFirestore db;

    private ThemeDA themeDA;
    private PersonDA personDA;
    private AlbumDA albumDA;
    private StoryDA storyDA;
    private EventDA eventDA;

    private Videx videx;

    private ActionBar actionBar;
    private View headerView;
    private TextView txtName, txtEmail;
    private ImageView imgPhoto;

    private String news_node;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = this.getSupportActionBar();
        actionBar.setTitle("Themes");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {

            startActivity(new Intent(HomeActivity.this, StartActivity.class));
            finish();
            return;

        }


        news_node = "900";

        videx = new Videx(HomeActivity.this);
        db = videx.getFirestore();

        personDA = new PersonDA(HomeActivity.this);
        themeDA = new ThemeDA(HomeActivity.this);
        storyDA = new StoryDA(HomeActivity.this);
        albumDA = new AlbumDA(HomeActivity.this);
        eventDA = new EventDA(HomeActivity.this);

        countThemes = themeDA.count();
        countGalleries = albumDA.count();
        countStories = storyDA.count();
        countEvents = eventDA.count();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        progress = findViewById(R.id.progress);

        pg = 0;

        checkThemes();

        /* THEME MENU
        // Fab Home.
        com.github.clans.fab.FloatingActionMenu fabHome = findViewById(R.id.fab_home);
        fabHome.setMenuButtonColorNormal(videx.getColor("500"));

        // Open Account.
        com.github.clans.fab.FloatingActionButton fabProfile = findViewById(R.id.fab_file);
        fabProfile.setColorNormal(videx.getColor("500"));
        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        // Open Manage.
        com.github.clans.fab.FloatingActionButton fabMsg = findViewById(R.id.fab_forum);
        fabMsg.setColorNormal(videx.getColor("500"));
        fabMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MessagesActivity.class));
            }
        });
        */

        // NON-THEME MENU.
        fabMenu = findViewById(R.id.fab_menu);
        fabMenu.setMenuButtonColorNormal(videx.getColor("500"));

        // Open New.
        fabNew = findViewById(R.id.fab_new);
        fabNew.setColorNormal(videx.getColor("500"));
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               create();
            }
        });

        /*
        // Open Manage.
        fabManage = findViewById(R.id.fab_manage);
        fabManage.setColorNormal(videx.getColor("500"));
        fabManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manage();
            }
        });
        */

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerView =  navigationView.getHeaderView(0);

        txtName = headerView.findViewById(R.id.my_name);
        txtEmail = headerView.findViewById(R.id.my_email);
        imgPhoto = headerView.findViewById(R.id.my_photo);

        checkPerson();

        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));
                return true;
            case R.id.menu_privacy:
                videx.setPref(Value.COLUMN_NOTE_NODE, Value.KEY_NOTE_PRIVACY);
                startActivity(new Intent(HomeActivity.this, NotesActivity.class));
                return true;
            case R.id.menu_faqs:
                videx.setPref(Value.COLUMN_NOTE_NODE, Value.KEY_NOTE_FAQ);
                startActivity(new Intent(HomeActivity.this, NotesActivity.class));
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

                switch (pg){
                    case 0:
                        if(countThemes > 0) {
                            themeAdapter.getFilter().filter(newText);
                        }
                        break;
                    case 1:
                        if(countStories > 0) {
                            storyAdapter.getFilter().filter(newText);
                        }
                        break;
                    case 2:
                        if(countGalleries > 0) {
                            albumAdapter.getFilter().filter(newText);
                        }
                        break;
                    case 3:
                        if(countStories > 0) {
                            storyAdapter.getFilter().filter(newText);
                        }
                        break;
                    case 4:
                        if(countEvents> 0) {
                            eventAdapter.getFilter().filter(newText);
                        }
                        break;
                }


                return true;
            }
        });
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle nav_admin view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fabMenu.setVisibility(View.GONE);
            actionBar.setTitle("Home");
            pg = 0;
            loadThemes();
        } else if (id == R.id.nav_updates) {
            fabMenu.setVisibility(View.VISIBLE);
            actionBar.setTitle("Latest Updates");
            fabNew.setLabelText("New Story");
            pg = 1;
            loadUpdates();
        } else if (id == R.id.nav_gallery) {
            fabMenu.setVisibility(View.VISIBLE);
            fabNew.setLabelText("New Gallery");
            actionBar.setTitle("Gallery");
            pg = 2;
            checkAlbums();
        } else if (id == R.id.nav_articles) {
            fabMenu.setVisibility(View.VISIBLE);
            fabNew.setLabelText("New News");
            actionBar.setTitle("News");
            pg = 3;
            loadNews();
        } else if (id == R.id.nav_events) {
            fabMenu.setVisibility(View.VISIBLE);
            fabNew.setLabelText("New Event");
            actionBar.setTitle("Events");
            pg = 4;
            loadEvents();
        } else if (id == R.id.nav_careers) {
            startActivity(new Intent(HomeActivity.this, JobsActivity.class));
        } else if (id == R.id.nav_scholarships) {
            startActivity(new Intent(HomeActivity.this, SchollsActivity.class));
        } else if (id == R.id.nav_institutions) {
            startActivity(new Intent(HomeActivity.this, EntitiesActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkPerson(){
        if(personDA.exist(user.getUid())) {
            loadPerson();
        }else{
            getPerson();
        }
    }

    private void getPerson(){
        progress.setVisibility(View.VISIBLE);
        DocumentReference profile = db.collection(Value.TABLE_PEOPLE).document(user.getUid());
        profile.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Person person = documentSnapshot.toObject(Person.class);
                personDA.add(person);
                progress.setVisibility(View.GONE);
                loadPerson();

            }
        });
    }

    private void loadPerson(){
        Person person = personDA.find(user.getUid());

        txtName.setText(person.getName());
        txtEmail.setText(person.getEmail());

        Picasso.get()
                .load(person.getImage())
                .resize(360, 360)
                .centerCrop()
                .transform(new CircleTransform())
                .placeholder(R.drawable.img_profile)
                .error(R.drawable.img_profile)
                .into(imgPhoto);

    }
    /* *********************************
            THEMES
     ********************************* */
    private void checkThemes(){
        if(countThemes > 0){
            loadThemes();
        }else{
            makeThemes();
        }
    }
    private void loadThemes(){
        List<Theme> themeList = themeDA.fetch(8, Value.COLUMN_NODE, "ASC");
        themeAdapter = new ThemeAdapter(HomeActivity.this, themeList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(themeAdapter);
    }

    private void makeThemes(){
        String[] theme_nodes = Value.themeNodes;
        String[] theme_titles = Value.themeTitle;
        String[] theme_captions = Value.themeCaption;
        String[] theme_images = Value.themeImages;
        String[] theme_colors = Value.themeColors;
        for(int s = 0; s < theme_nodes.length; s++){
            String node = theme_nodes[s];
            String title = theme_titles[s];
            String caption = theme_captions[s];
            String image = theme_images[s];
            String color = theme_colors[s];

            Theme theme  = new Theme(node, title, caption, image, color);
            themeDA.refresh(theme);
        }
        loadThemes();
    }

    private void loadUpdates(){
        recyclerView.setHasFixedSize(true);
        StoryDA storyDA = new StoryDA(this);
        List<Story> storyList = storyDA.all();
        storyAdapter = new StoryAdapter(HomeActivity.this, storyList, user.getUid());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(storyAdapter);

    }

    /* *********************************
            ALBUMS
     ********************************* */
    private void checkAlbums(){
        if(albumDA.count() > 0){
            loadAlbums();
            getAlbums();
        }else{
            getAlbums();
        }
    }
    private void loadAlbums(){
        recyclerView.setHasFixedSize(true);
        List<Album> albumList = albumDA.all();
        albumAdapter = new AlbumAdapter(HomeActivity.this, albumList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(albumAdapter);
    }

    private void getAlbums(){
        progress.setVisibility(View.VISIBLE);

        db.collection(Value.TABLE_ALBUMS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Album mfoni = document.toObject(Album.class);
                                albumDA.refresh(mfoni);

                            }
                            progress.setVisibility(View.GONE);
                            loadAlbums();
                        } else {
                            progress.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void loadNews(){
        recyclerView.setHasFixedSize(true);
        String news_node = "9000";
        List<Story> storyList = storyDA.byTheme(news_node);
        storyAdapter = new StoryAdapter(HomeActivity.this, storyList, user.getUid());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(storyAdapter);

    }
    private void loadEvents(){
        recyclerView.setHasFixedSize(true);
        List<Event> eventList = eventDA.all();
        eventAdapter = new EventAdapter(this, eventList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(eventAdapter);
    }

    private void manage(){
        switch (pg){
            case 0:
                // Do nothing - its themes
                break;
            case 1:
                // Do nothing - its themes
                break;
            case 2:
                // Galley
                startActivity(new Intent(HomeActivity.this, StoryManActivity.class));
                break;
            case 3:
                // News
                videx.setPref(Value.COLUMN_THEME_NODE,  "9000");
                startActivity(new Intent(HomeActivity.this, StoryManActivity.class));
                break;
            case 4:
                // Events
                break;

        }
    }

    private void create(){
        switch (pg){
            case 0:
                // Do nothing - its themes
                break;
            case 1:
                // Updates
                videx.setPref(Value.COLUMN_THEME_NODE,  "1000");
                startActivity(new Intent(HomeActivity.this, StoryNewActivity.class));
                break;
            case 2:
                // Galley
                startActivity(new Intent(HomeActivity.this, PhotosNewActivity.class));
                break;
            case 3:
                // News
                videx.setPref(Value.COLUMN_THEME_NODE,  "9000");
                startActivity(new Intent(HomeActivity.this, StoryNewActivity.class));
                break;
            case 4:
                // Events
                break;

        }
    }
}
