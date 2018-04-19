package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Like;

import java.util.ArrayList;
import java.util.List;

public class LikeDA {

    private SQLiteDatabase db;

    public LikeDA(){}

    public LikeDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting like Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_LIKES;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a like
     */
    public long add(Like like) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_UID, like.getUid());
        values.put(Value.COLUMN_NODE, like.getNode());
        values.put(Value.COLUMN_STORY_NODE, like.getStory_node());

        // return inserted row id
        return db.insert(Value.TABLE_LIKES, null, values);
    }

    /*
     * check if like exists
     */
    public boolean exist(String like_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_LIKES + " WHERE " + Value.COLUMN_NODE + " = '" + like_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }
    /*
     * get single like
     */
    public Like find(String like_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_LIKES + " WHERE " + Value.COLUMN_NODE + " = '" + like_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Like like = new Like();
        like.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        like.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        like.setStory_node(c.getString(c.getColumnIndex(Value.COLUMN_STORY_NODE)));

        c.close();

        return like;
    }
    /*
     * get single like
     */
    public Like get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_LIKES + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Like like = new Like();
        like.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        like.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        like.setStory_node(c.getString(c.getColumnIndex(Value.COLUMN_STORY_NODE)));

        c.close();

        return like;
    }

    /*
     * get last like
     */
    public Like last() {
        String sql = "SELECT  * FROM " + Value.TABLE_LIKES + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Like like = new Like();
        like.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        like.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        like.setStory_node(c.getString(c.getColumnIndex(Value.COLUMN_STORY_NODE)));

        c.close();

        return like;
    }

    /*
     * getting all likes
     * */
    public List<Like> all() {
        List<Like> likes = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_LIKES;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Like like = new Like();
                like.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                like.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                like.setStory_node(c.getString(c.getColumnIndex(Value.COLUMN_STORY_NODE)));

                // adding to like list
                likes.add(like);
            } while (c.moveToNext());
        }

        c.close();

        return likes;
    }

    /*
     * Updating an like
     */
    public int update(Like like) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_STORY_NODE, like.getStory_node());


        // updating row
        return db.update(Value.TABLE_LIKES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(like.getNode()) });
    }

    /*
     * Deleting an like
     */
    public void delete(long like_node) {
        db.delete(Value.TABLE_LIKES, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(like_node) });
    }

    /*
     * Mark like as read
     */
    public int mark(String like_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_LIKES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(like_node) });
    }

    /*
     * Refresh all local data
     */
    public void refresh(Like like){
        if(exist(like.getNode())){
            update(like);
        }else{
            add(like);
        }
    }
}
