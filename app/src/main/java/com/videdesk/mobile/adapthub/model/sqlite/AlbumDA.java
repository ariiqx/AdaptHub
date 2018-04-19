package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumDA {

    private SQLiteDatabase db;
    private Database database;

    public AlbumDA(){}

    public AlbumDA(Context context){
        database = new Database(context);
        this.db = database.open();
    }

    // Getting album Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_ALBUMS;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a album
     */
    public long add(Album album) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_UID, album.getUid());
        values.put(Value.COLUMN_NODE, album.getNode());
        values.put(Value.COLUMN_CAPTION, album.getCaption());
        values.put(Value.COLUMN_IMAGE, album.getImage());
        values.put(Value.COLUMN_CREATED, album.getCreated());
        values.put(Value.COLUMN_STATUS, album.getStatus());
        values.put(Value.COLUMN_READ, album.getRead());

        // return inserted row id
        return db.insert(Value.TABLE_ALBUMS, null, values);
    }

    /*
     * get single album
     */
    public Album find(String album_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_ALBUMS + " WHERE " + Value.COLUMN_NODE + " = '" + album_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Album album = new Album();
        album.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        album.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        album.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        album.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        album.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        album.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        album.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return album;
    }

    /*
     * get single album
     */
    public Album get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_ALBUMS + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Album album = new Album();
        album.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        album.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        album.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        album.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        album.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        album.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        album.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return album;
    }

    /*
     * check if album exists
     */
    public boolean exist(String album_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_ALBUMS + " WHERE " + Value.COLUMN_NODE + " = '" + album_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }
    /*
     * get last album
     */
    public Album last() {
        String sql = "SELECT  * FROM " + Value.TABLE_ALBUMS + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Album album = new Album();
        album.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        album.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        album.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        album.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        album.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        album.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        album.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return album;
    }

    /*
     * getting all albums
     * */
    public List<Album> all() {
        List<Album> albums = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_ALBUMS;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Album album = new Album();
                album.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                album.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                album.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                album.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
                album.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                album.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
                album.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

                // adding to album list
                albums.add(album);
            } while (c.moveToNext());
        }

        c.close();

        return albums;
    }

    /*
     * Updating an album
     */
    public int update(Album album) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_CAPTION, album.getCaption());
        values.put(Value.COLUMN_IMAGE, album.getImage());
        values.put(Value.COLUMN_STATUS, album.getStatus());

        // updating row
        return db.update(Value.TABLE_ALBUMS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(album.getNode()) });
    }

    /*
     * Deleting an album
     */
    public void delete(String album_node) {
        db.delete(Value.TABLE_ALBUMS, Value.COLUMN_NODE + " = ?",
                new String[] { album_node });
    }

    /*
     * Mark album as read
     */
    public int mark(String album_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_ALBUMS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(album_node) });
    }

    /*
     * Refresh all local data
     */
    public void refresh(Album album){
        if(album.getStatus().equals(Value.KEY_STATUS_ACTIVE)) {
            if (exist(album.getNode())) {
                update(album);
            } else {
                add(album);
            }
        }else{
            delete(album.getNode());
        }
    }

    public void reset(){
        database.reset(Value.SQL_ALBUM, Value.TABLE_ALBUMS);
    }
}
