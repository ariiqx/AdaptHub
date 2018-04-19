package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.File;

import java.util.ArrayList;
import java.util.List;

public class FileDA {

    private SQLiteDatabase db;

    public FileDA(){}

    public FileDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting file Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_FILES;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a file
     */
    public long add(File file) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_UID, file.getUid());
        values.put(Value.COLUMN_NODE, file.getNode());
        values.put(Value.COLUMN_TITLE, file.getTitle());
        values.put(Value.COLUMN_CONTENT, file.getContent());
        values.put(Value.COLUMN_CREATED, file.getCreated());
        values.put(Value.COLUMN_STATUS, file.getStatus());
        values.put(Value.COLUMN_READ, file.getRead());

        // return inserted row id
        return db.insert(Value.TABLE_FILES, null, values);
    }

    /*
     * check if file exists
     */
    public boolean exist(String file_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_FILES + " WHERE " + Value.COLUMN_NODE + " = '" + file_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }

    /*
     * get single file
     */
    public File find(String file_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_FILES + " WHERE " + Value.COLUMN_NODE + " = '" + file_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        File file = new File();
        file.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        file.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        file.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        file.setContent(c.getString(c.getColumnIndex(Value.COLUMN_CONTENT)));
        file.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        file.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        file.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return file;
    }

    /*
     * get single file
     */
    public File get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_FILES + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        File file = new File();
        file.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        file.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        file.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        file.setContent(c.getString(c.getColumnIndex(Value.COLUMN_CONTENT)));
        file.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        file.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        file.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return file;
    }

    /*
     * get last file
     */
    public File last() {
        String sql = "SELECT  * FROM " + Value.TABLE_FILES + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        File file = new File();
        file.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        file.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        file.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        file.setContent(c.getString(c.getColumnIndex(Value.COLUMN_CONTENT)));
        file.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        file.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        file.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return file;
    }

    /*
     * getting all files
     * */
    public List<File> all() {
        List<File> files = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_FILES;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                File file = new File();
                file.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                file.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                file.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
                file.setContent(c.getString(c.getColumnIndex(Value.COLUMN_CONTENT)));
                file.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                file.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
                file.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

                // adding to file list
                files.add(file);
            } while (c.moveToNext());
        }

        c.close();

        return files;
    }

    /*
     * Updating an file
     */
    public int update(File file) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_TITLE, file.getTitle());
        values.put(Value.COLUMN_CONTENT, file.getContent());

        // updating row
        return db.update(Value.TABLE_FILES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(file.getNode()) });
    }

    /*
     * Deleting an file
     */
    public void delete(String file_node) {
        db.delete(Value.TABLE_FILES, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(file_node) });
    }

    /*
     * Mark file as read
     */
    public int mark(String file_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_FILES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(file_node) });
    }

    /*
     * Refresh all local data
     */
    public void refresh(File file){
        if(file.getStatus().equals(Value.KEY_STATUS_ACTIVE)) {
            if (exist(file.getNode())) {
                update(file);
            } else {
                add(file);
            }
        }else{
            delete(file.getNode());
        }
    }
}
