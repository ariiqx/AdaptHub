package com.videdesk.mobile.adapthub.activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Videx;

public class ForumsActivity extends AppCompatActivity {

    private int countForums;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private Videx videx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(ForumsActivity.this, HomeActivity.class));
            finish();
            return;
        }
        videx = new Videx(ForumsActivity.this);
        db = videx.getFirestore();

        countForums = 0;


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
                startActivity(new Intent(ForumsActivity.this, AboutActivity.class));
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

                if(countForums > 0) {
                    //forumAdapter.getFilter().filter(newText);
                }

                return true;
            }
        });
    }

}
