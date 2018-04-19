package com.videdesk.mobile.adapthub.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.videdesk.mobile.adapthub.BuildConfig;
import com.videdesk.mobile.adapthub.R;

import java.util.Calendar;

public class AboutActivity extends AppCompatActivity {

    private static final String copy = "Copyright ";
    private static final String version = "Version ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Year
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        String copi = "\u00a9";

        TextView appDev = (TextView) findViewById(R.id.lbl_app_ryt);
        appDev.setText(copy + copi + year);

        //Version
        String versionName = BuildConfig.VERSION_NAME;

        TextView appVer = (TextView) findViewById(R.id.lbl_app_ver);
        String ver = version + versionName;
        appVer.setText(ver);
    }
}
