package com.videdesk.mobile.adapthub.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.videdesk.mobile.adapthub.R;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.config.Videx;
import com.videdesk.mobile.adapthub.model.sqlite.EventDA;

public class EventActivity extends AppCompatActivity {

    private String node;
    private EventDA eventDA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Videx videx = new Videx(EventActivity.this);
        node = videx.getPref(Value.COLUMN_EVENT_NODE);
        eventDA = new EventDA(this);
    }
}
