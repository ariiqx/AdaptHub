package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoDA {

    private SQLiteDatabase db;

    public PhotoDA(){}

    public PhotoDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting photo Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_PHOTOS;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a photo
     */
    public long add(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_NODE, photo.getNode());
        values.put(Value.COLUMN_ALBUM_NODE, photo.getAlbum_node());
        values.put(Value.COLUMN_CAPTION, photo.getCaption());
        values.put(Value.COLUMN_IMAGE, photo.getImage());
        values.put(Value.COLUMN_CREATED, photo.getCreated());

        // return inserted row id
        return db.insert(Value.TABLE_PHOTOS, null, values);
    }

    /*
     * check if photo exists
     */
    public boolean exist(String photo_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_PHOTOS + " WHERE " + Value.COLUMN_NODE + " = '" + photo_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }

    /*
     * get single photo
     */
    public Photo find(String photo_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_PHOTOS + " WHERE " + Value.COLUMN_NODE + " = '" + photo_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Photo photo = new Photo();
        photo.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        photo.setAlbum_node(c.getString(c.getColumnIndex(Value.COLUMN_ALBUM_NODE)));
        photo.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        photo.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        photo.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));

        c.close();

        return photo;
    }
    /*
     * get single photo
     */
    public Photo get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_PHOTOS + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Photo photo = new Photo();
        photo.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        photo.setAlbum_node(c.getString(c.getColumnIndex(Value.COLUMN_ALBUM_NODE)));
        photo.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        photo.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        photo.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));

        c.close();

        return photo;
    }

    /*
     * get last photo
     */
    public Photo last() {
        String sql = "SELECT  * FROM " + Value.TABLE_PHOTOS + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Photo photo = new Photo();
        photo.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        photo.setAlbum_node(c.getString(c.getColumnIndex(Value.COLUMN_ALBUM_NODE)));
        photo.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        photo.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        photo.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));

        c.close();

        return photo;
    }

    /*
     * getting all photos by album
     * */
    public List<Photo> byAlbum(String album_node) {
        List<Photo> photos = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_PHOTOS + " WHERE " + Value.COLUMN_ALBUM_NODE + " = '" + album_node + "'";

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Photo photo = new Photo();
                photo.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                photo.setAlbum_node(c.getString(c.getColumnIndex(Value.COLUMN_ALBUM_NODE)));
                photo.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                photo.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
                photo.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));

                // adding to photo list
                photos.add(photo);
            } while (c.moveToNext());
        }

        c.close();

        return photos;
    }

    /*
     * count all photos by album
     * */
    public int countAlbum(String album_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_PHOTOS + " WHERE " + Value.COLUMN_ALBUM_NODE + " = '" + album_node + "'";
        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    /*
     * getting all photos
     * */
    public List<Photo> all() {
        List<Photo> photos = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_PHOTOS;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Photo photo = new Photo();
                photo.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                photo.setAlbum_node(c.getString(c.getColumnIndex(Value.COLUMN_ALBUM_NODE)));
                photo.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                photo.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
                photo.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));

                // adding to photo list
                photos.add(photo);
            } while (c.moveToNext());
        }

        c.close();

        return photos;
    }

    /*
     * Updating an photo
     */
    public int update(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_CAPTION, photo.getCaption());
        values.put(Value.COLUMN_IMAGE, photo.getImage());

        // updating row
        return db.update(Value.TABLE_PHOTOS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(photo.getNode()) });
    }

    /*
     * Deleting an photo
     */
    public void delete(long photo_node) {
        db.delete(Value.TABLE_PHOTOS, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(photo_node) });
    }

    /*
     * Mark photo as read
     */
    public int mark(String photo_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_PHOTOS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(photo_node) });
    }

    /*
     * Refresh all local data
     */
    public void refresh(Photo photo){
        if(exist(photo.getNode())){
            update(photo);
        }else{
            add(photo);
        }
    }
}
