package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Thread;

import java.util.ArrayList;
import java.util.List;

public class ThreadDA {

    private SQLiteDatabase db;

    public ThreadDA(){}

    public ThreadDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting thread Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_THREADS;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a thread
     */
    public long add(Thread thread) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_UID, thread.getUid());
        values.put(Value.COLUMN_NODE, thread.getNode());
        values.put(Value.COLUMN_CAPTION, thread.getCaption());
        values.put(Value.COLUMN_CREATED, thread.getCreated());

        // return inserted row id
        return db.insert(Value.TABLE_THREADS, null, values);
    }

    /*
     * check if thread exists
     */
    public boolean exist(String thread_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_THREADS + " WHERE " + Value.COLUMN_NODE + " = '" + thread_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }

    /*
     * get single thread
     */
    public Thread get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_THREADS + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Thread thread = new Thread();
        thread.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        thread.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        thread.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        thread.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));

        c.close();

        return thread;
    }

    /*
     * get single thread
     */
    public Thread find(String thread_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_THREADS + " WHERE " + Value.COLUMN_NODE + " = '" + thread_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Thread thread = new Thread();
        thread.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        thread.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        thread.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        thread.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));

        c.close();

        return thread;
    }

    /*
     * get last thread
     */
    public Thread last() {
        String sql = "SELECT  * FROM " + Value.TABLE_THREADS + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Thread thread = new Thread();
        thread.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        thread.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        thread.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        thread.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));

        c.close();

        return thread;
    }

    /*
     * getting all threads
     * */
    public List<Thread> all() {
        List<Thread> threads = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_THREADS;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Thread thread = new Thread();
                thread.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                thread.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                thread.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                thread.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));

                // adding to thread list
                threads.add(thread);
            } while (c.moveToNext());
        }

        c.close();

        return threads;
    }

    /*
     * getting all matching threads
     * */
    public List<Thread> fetch(String whereKey, String whereOperator, String whereValue, String orderKey, String orderPath, int numRows)
    {
        List<Thread> threads = new ArrayList<>();
        // WHERE CLAUSE
        String where = "";
        if(whereKey != null && whereOperator != null && whereValue != null){
            where = " WHERE " + whereKey + " " + whereOperator + " '" + whereValue + "'";
        }
        // ORDERBY CLAUSE
        String orderBy = "";
        String by = " ASC";
        if(orderPath != null){
            by = " " + orderPath.toUpperCase();
        }
        if(orderKey != null){
            orderBy = " ORDER BY " + orderKey +  by;
        }
        // LIMIT CLAUSE
        String limit = "";
        if(numRows > 0){
            limit = " LIMIT " + numRows;
        }
        String sql = "SELECT  * FROM " + Value.TABLE_THREADS + orderBy + limit;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Thread thread = new Thread();
                thread.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                thread.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                thread.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                thread.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));

                // adding to thread list
                threads.add(thread);
            } while (c.moveToNext());
        }

        c.close();

        return threads;
    }

    /*
     * Updating an thread
     */
    public int update(Thread thread) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_CAPTION, thread.getCaption());

        // updating row
        return db.update(Value.TABLE_THREADS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(thread.getNode()) });
    }

    /*
     * Deleting an thread
     */
    public void delete(long thread_node) {
        db.delete(Value.TABLE_THREADS, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(thread_node) });
    }

    /*
     * Mark thread as read
     */
    public int read(String thread_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_THREADS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(thread_node) });
    }

    /*
     * Refresh all local data
     */
    public void refresh(Thread thread){
        if(exist(thread.getNode())){
            update(thread);
        }else{
            add(thread);
        }
    }
}
