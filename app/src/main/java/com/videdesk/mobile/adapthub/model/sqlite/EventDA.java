package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventDA {

    private SQLiteDatabase db;

    public EventDA(){}

    public EventDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting event Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_EVENTS;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a event
     */
    public long add(Event event) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_UID, event.getUid());
        values.put(Value.COLUMN_NODE, event.getNode());
        values.put(Value.COLUMN_TITLE, event.getTitle());
        values.put(Value.COLUMN_CAPTION, event.getCaption());
        values.put(Value.COLUMN_DATETIME, event.getDatetime());
        values.put(Value.COLUMN_VENUE, event.getVenue());
        values.put(Value.COLUMN_CREATED, event.getCreated());
        values.put(Value.COLUMN_STATUS, event.getStatus());
        values.put(Value.COLUMN_READ, event.getRead());

        // return inserted row id
        return db.insert(Value.TABLE_EVENTS, null, values);
    }

    /*
     * check if event exists
     */
    public boolean exist(String event_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_EVENTS + " WHERE " + Value.COLUMN_NODE + " = '" + event_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }

    /*
     * get single event
     */
    public Event find(String event_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_EVENTS + " WHERE " + Value.COLUMN_NODE + " = '" + event_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Event event = new Event();
        event.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        event.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        event.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        event.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        event.setDatetime(c.getString(c.getColumnIndex(Value.COLUMN_DATETIME)));
        event.setVenue(c.getString(c.getColumnIndex(Value.COLUMN_VENUE)));
        event.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        event.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        event.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return event;
    }

    /*
     * get single event
     */
    public Event get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_EVENTS + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Event event = new Event();
        event.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        event.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        event.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        event.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        event.setDatetime(c.getString(c.getColumnIndex(Value.COLUMN_DATETIME)));
        event.setVenue(c.getString(c.getColumnIndex(Value.COLUMN_VENUE)));
        event.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        event.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        event.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return event;
    }

    /*
     * get last event
     */
    public Event last() {
        String sql = "SELECT  * FROM " + Value.TABLE_EVENTS + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Event event = new Event();
        event.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        event.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        event.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        event.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        event.setDatetime(c.getString(c.getColumnIndex(Value.COLUMN_DATETIME)));
        event.setVenue(c.getString(c.getColumnIndex(Value.COLUMN_VENUE)));
        event.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        event.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        event.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return event;
    }

    /*
     * getting all events
     * */
    public List<Event> all() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_EVENTS;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Event event = new Event();
                event.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                event.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                event.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
                event.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                event.setDatetime(c.getString(c.getColumnIndex(Value.COLUMN_DATETIME)));
                event.setVenue(c.getString(c.getColumnIndex(Value.COLUMN_VENUE)));
                event.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                event.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
                event.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

                // adding to event list
                events.add(event);
            } while (c.moveToNext());
        }

        c.close();

        return events;
    }

    /*
     * Updating an event
     */
    public int update(Event event) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_TITLE, event.getTitle());
        values.put(Value.COLUMN_CAPTION, event.getCaption());
        values.put(Value.COLUMN_DATETIME, event.getDatetime());
        values.put(Value.COLUMN_VENUE, event.getVenue());

        // updating row
        return db.update(Value.TABLE_EVENTS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(event.getNode()) });
    }

    /*
     * Deleting an event
     */
    public void delete(String event_node) {
        db.delete(Value.TABLE_EVENTS, Value.COLUMN_NODE + " = ?",
                new String[] { event_node });
    }

    /*
     * Mark event as read
     */
    public int mark(String event_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_EVENTS, values, Value.COLUMN_NODE + " = ?",
                new String[] { event_node });
    }

    /*
     * Refresh all local data
     */
    public void refresh(Event event){
        if(event.getStatus().equals(Value.KEY_STATUS_ACTIVE)) {
            if (exist(event.getNode())) {
                update(event);
            } else {
                add(event);
            }
        }else{
            delete(event.getNode());
        }
    }
}
