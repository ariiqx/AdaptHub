package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Scholl;

import java.util.ArrayList;
import java.util.List;

public class SchollDA {

    private SQLiteDatabase db;

    public SchollDA(){}

    public SchollDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting scholl Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_SCHOLLS;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a scholl
     */
    public long add(Scholl scholl) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_UID, scholl.getUid());
        values.put(Value.COLUMN_NODE, scholl.getNode());
        values.put(Value.COLUMN_CAPTION, scholl.getCaption());
        values.put(Value.COLUMN_IMAGE, scholl.getImage());
        values.put(Value.COLUMN_CREATED, scholl.getCreated());
        values.put(Value.COLUMN_STATUS, scholl.getStatus());
        values.put(Value.COLUMN_READ, scholl.getRead());

        // return inserted row id
        return db.insert(Value.TABLE_SCHOLLS, null, values);
    }

    /*
     * check if scholl exists
     */
    public boolean exist(String scholl_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_SCHOLLS + " WHERE " + Value.COLUMN_NODE + " = '" + scholl_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }

    /*
     * get single scholl
     */
    public Scholl find(String scholl_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_SCHOLLS + " WHERE " + Value.COLUMN_NODE + " = '" + scholl_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Scholl scholl = new Scholl();
        scholl.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        scholl.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        scholl.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        scholl.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        scholl.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        scholl.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        scholl.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return scholl;
    }

    /*
     * get single scholl
     */
    public Scholl get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_SCHOLLS + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Scholl scholl = new Scholl();
        scholl.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        scholl.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        scholl.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        scholl.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        scholl.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        scholl.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        scholl.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return scholl;
    }

    /*
     * get last scholl
     */
    public Scholl last() {
        String sql = "SELECT  * FROM " + Value.TABLE_SCHOLLS + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Scholl scholl = new Scholl();
        scholl.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        scholl.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        scholl.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        scholl.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        scholl.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        scholl.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        scholl.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return scholl;
    }

    /*
     * getting all scholls
     * */
    public List<Scholl> all() {
        List<Scholl> scholls = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_SCHOLLS;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Scholl scholl = new Scholl();
                scholl.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                scholl.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                scholl.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                scholl.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
                scholl.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                scholl.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
                scholl.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

                // adding to scholl list
                scholls.add(scholl);
            } while (c.moveToNext());
        }

        c.close();

        return scholls;
    }

    /*
     * Updating an scholl
     */
    public int update(Scholl scholl) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_CAPTION, scholl.getCaption());
        values.put(Value.COLUMN_IMAGE, scholl.getImage());
        values.put(Value.COLUMN_STATUS, scholl.getStatus());

        // updating row
        return db.update(Value.TABLE_SCHOLLS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(scholl.getNode()) });
    }

    /*
     * Deleting an scholl
     */
    public void delete(String scholl_node) {
        db.delete(Value.TABLE_SCHOLLS, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(scholl_node) });
    }

    /*
     * Mark scholl as read
     */
    public int mark(String scholl_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_SCHOLLS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(scholl_node) });
    }
    /*
     * Refresh all local data
     */
    public void refresh(Scholl scholl){
        if(scholl.getStatus().equals(Value.KEY_STATUS_ACTIVE)) {
            if (exist(scholl.getNode())) {
                update(scholl);
            } else {
                add(scholl);
            }
        }else{
            delete(scholl.getNode());
        }
    }
}
