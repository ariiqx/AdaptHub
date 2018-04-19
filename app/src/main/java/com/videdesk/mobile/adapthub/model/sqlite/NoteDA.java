package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteDA {

    private SQLiteDatabase db;

    public NoteDA(){}

    public NoteDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting note Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_NOTES;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a note
     */
    public long add(Note note) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_UID, note.getUid());
        values.put(Value.COLUMN_NODE, note.getNode());
        values.put(Value.COLUMN_CODE, note.getCode());
        values.put(Value.COLUMN_TITLE, note.getTitle());
        values.put(Value.COLUMN_CAPTION, note.getCaption());
        values.put(Value.COLUMN_CREATED, note.getCreated());
        values.put(Value.COLUMN_STATUS, note.getStatus());
        values.put(Value.COLUMN_READ, note.getRead());

        // return inserted row id
        return db.insert(Value.TABLE_NOTES, null, values);
    }

    /*
     * check if note exists
     */
    public boolean exist(String note_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_NOTES + " WHERE " + Value.COLUMN_NODE + " = '" + note_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }

    /*
     * get single note
     */
    public Note find(String note_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_NOTES + " WHERE " + Value.COLUMN_NODE + " = '" + note_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Note note = new Note();
        note.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        note.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        note.setCode(c.getString(c.getColumnIndex(Value.COLUMN_CODE)));
        note.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        note.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        note.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        note.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        note.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return note;
    }

    /*
     * get single note
     */
    public Note get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_NOTES + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Note note = new Note();
        note.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        note.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        note.setCode(c.getString(c.getColumnIndex(Value.COLUMN_CODE)));
        note.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        note.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        note.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        note.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        note.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return note;
    }

    /*
     * get last note
     */
    public Note last() {
        String sql = "SELECT  * FROM " + Value.TABLE_NOTES + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Note note = new Note();
        note.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        note.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        note.setCode(c.getString(c.getColumnIndex(Value.COLUMN_CODE)));
        note.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        note.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        note.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        note.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        note.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return note;
    }

    /*
     * getting all notes
     * */
    public List<Note> all() {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_NOTES;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Note note = new Note();
                note.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                note.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                note.setCode(c.getString(c.getColumnIndex(Value.COLUMN_CODE)));
                note.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
                note.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                note.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                note.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
                note.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

                // adding to note list
                notes.add(note);
            } while (c.moveToNext());
        }

        c.close();

        return notes;
    }

    /*
     * Updating an note
     */
    public int update(Note note) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_TITLE, note.getTitle());
        values.put(Value.COLUMN_CAPTION, note.getCaption());

        // updating row
        return db.update(Value.TABLE_NOTES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(note.getNode()) });
    }

    /*
     * Deleting an note
     */
    public void delete(String note_node) {
        db.delete(Value.TABLE_NOTES, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(note_node) });
    }

    /*
     * Mark note as read
     */
    public int mark(String note_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_NOTES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(note_node) });
    }

    /*
     * Refresh all local data
     */
    public void refresh(Note note){
        if(note.getStatus().equals(Value.KEY_STATUS_ACTIVE)) {
            if (exist(note.getNode())) {
                update(note);
            } else {
                add(note);
            }
        }else{
            delete(note.getNode());
        }
    }
}
