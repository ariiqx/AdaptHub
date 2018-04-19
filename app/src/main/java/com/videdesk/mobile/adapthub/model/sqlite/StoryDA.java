package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Story;

import java.util.ArrayList;
import java.util.List;

public class StoryDA {

    private SQLiteDatabase db;

    public StoryDA(){}

    public StoryDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting story Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_STORIES;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a story
     */
    public long add(Story story) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_UID, story.getUid());
        values.put(Value.COLUMN_THEME_NODE, story.getTheme_node());
        values.put(Value.COLUMN_NODE, story.getNode());
        values.put(Value.COLUMN_TITLE, story.getTitle());
        values.put(Value.COLUMN_CAPTION, story.getCaption());
        values.put(Value.COLUMN_DETAILS, story.getDetails());
        values.put(Value.COLUMN_IMAGE, story.getImage());
        values.put(Value.COLUMN_VIDEO, story.getVideo());
        values.put(Value.COLUMN_DOCUMENT, story.getDocument());
        values.put(Value.COLUMN_ALBUM_NODE, story.getAlbum_node());
        values.put(Value.COLUMN_URL, story.getUrl());
        values.put(Value.COLUMN_CREATED, story.getCreated());
        values.put(Value.COLUMN_STATUS, story.getStatus());
        values.put(Value.COLUMN_READ, story.getRead());

        // return inserted row id
        return db.insert(Value.TABLE_STORIES, null, values);
    }

    /*
     * check if story exists
     */
    public boolean exist(String story_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_STORIES + " WHERE " + Value.COLUMN_NODE + " = '" + story_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }

    /*
     * get single story
     */
    public Story get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_STORIES + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Story story = new Story();
        story.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        story.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        story.setTheme_node(c.getString(c.getColumnIndex(Value.COLUMN_THEME_NODE)));
        story.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        story.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        story.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        story.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        story.setVideo(c.getString(c.getColumnIndex(Value.COLUMN_VIDEO)));
        story.setDocument(c.getString(c.getColumnIndex(Value.COLUMN_DOCUMENT)));
        story.setAlbum_node(c.getString(c.getColumnIndex(Value.COLUMN_ALBUM_NODE)));
        story.setUrl(c.getString(c.getColumnIndex(Value.COLUMN_URL)));
        story.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        story.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        story.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return story;
    }

    /*
     * get single story
     */
    public Story find(String story_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_STORIES + " WHERE " + Value.COLUMN_NODE + " = '" + story_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Story story = new Story();
        story.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        story.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        story.setTheme_node(c.getString(c.getColumnIndex(Value.COLUMN_THEME_NODE)));
        story.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        story.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        story.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        story.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        story.setVideo(c.getString(c.getColumnIndex(Value.COLUMN_VIDEO)));
        story.setDocument(c.getString(c.getColumnIndex(Value.COLUMN_DOCUMENT)));
        story.setAlbum_node(c.getString(c.getColumnIndex(Value.COLUMN_ALBUM_NODE)));
        story.setUrl(c.getString(c.getColumnIndex(Value.COLUMN_URL)));
        story.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        story.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        story.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return story;
    }

    /*
     * get last story
     */
    public Story last() {
        String sql = "SELECT  * FROM " + Value.TABLE_STORIES + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Story story = new Story();
        story.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        story.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        story.setTheme_node(c.getString(c.getColumnIndex(Value.COLUMN_THEME_NODE)));
        story.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        story.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        story.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        story.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        story.setVideo(c.getString(c.getColumnIndex(Value.COLUMN_VIDEO)));
        story.setDocument(c.getString(c.getColumnIndex(Value.COLUMN_DOCUMENT)));
        story.setAlbum_node(c.getString(c.getColumnIndex(Value.COLUMN_ALBUM_NODE)));
        story.setUrl(c.getString(c.getColumnIndex(Value.COLUMN_URL)));
        story.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        story.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        story.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return story;
    }

    /*
     * getting all stories by theme
     * */
    public List<Story> byTheme(String theme_node) {
        List<Story> stories = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_STORIES + " WHERE " + Value.COLUMN_THEME_NODE + " = '" + theme_node + "'";

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Story story = new Story();
                story.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                story.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                story.setTheme_node(c.getString(c.getColumnIndex(Value.COLUMN_THEME_NODE)));
                story.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
                story.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                story.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
                story.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
                story.setVideo(c.getString(c.getColumnIndex(Value.COLUMN_VIDEO)));
                story.setDocument(c.getString(c.getColumnIndex(Value.COLUMN_DOCUMENT)));
                story.setAlbum_node(c.getString(c.getColumnIndex(Value.COLUMN_ALBUM_NODE)));
                story.setUrl(c.getString(c.getColumnIndex(Value.COLUMN_URL)));
                story.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                story.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
                story.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

                // adding to story list
                stories.add(story);
            } while (c.moveToNext());
        }

        c.close();

        return stories;
    }

    /*
     * getting all stories
     * */
    public List<Story> all() {
        List<Story> stories = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_STORIES;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Story story = new Story();
                story.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                story.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                story.setTheme_node(c.getString(c.getColumnIndex(Value.COLUMN_THEME_NODE)));
                story.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
                story.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                story.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
                story.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
                story.setVideo(c.getString(c.getColumnIndex(Value.COLUMN_VIDEO)));
                story.setDocument(c.getString(c.getColumnIndex(Value.COLUMN_DOCUMENT)));
                story.setAlbum_node(c.getString(c.getColumnIndex(Value.COLUMN_ALBUM_NODE)));
                story.setUrl(c.getString(c.getColumnIndex(Value.COLUMN_URL)));
                story.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                story.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
                story.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

                // adding to story list
                stories.add(story);
            } while (c.moveToNext());
        }

        c.close();

        return stories;
    }

    /*
     * Updating an story
     */
    public int update(Story story) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_THEME_NODE, story.getTheme_node());
        values.put(Value.COLUMN_TITLE, story.getTitle());
        values.put(Value.COLUMN_CAPTION, story.getCaption());
        values.put(Value.COLUMN_DETAILS, story.getDetails());
        values.put(Value.COLUMN_IMAGE, story.getImage());
        values.put(Value.COLUMN_VIDEO, story.getVideo());
        values.put(Value.COLUMN_DOCUMENT, story.getDocument());
        values.put(Value.COLUMN_URL, story.getUrl());

        // updating row
        return db.update(Value.TABLE_STORIES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(story.getNode()) });
    }

    /*
     * Deleting an story
     */
    public void delete(String story_node) {
        db.delete(Value.TABLE_STORIES, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(story_node) });
    }

    /*
     * Mark story as read
     */
    public int mark(String story_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_STORIES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(story_node) });
    }

    /*
     * Refresh all local data
     */
    public void refresh(Story story){
        if(story.getStatus().equals(Value.KEY_STATUS_ACTIVE)) {
            if (exist(story.getNode())) {
                update(story);
            } else {
                add(story);
            }
        }else{
            delete(story.getNode());
        }
    }
}
