package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Theme;

import java.util.ArrayList;
import java.util.List;

public class ThemeDA {

    private SQLiteDatabase db;
    private Database database;

    public ThemeDA(){}

    public ThemeDA(Context context){
        database = new Database(context);
        this.db = database.open();
    }

    // Getting theme Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_THEMES;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a theme
     */
    public long add(Theme theme) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_NODE, theme.getNode());
        values.put(Value.COLUMN_TITLE, theme.getTitle());
        values.put(Value.COLUMN_CAPTION, theme.getCaption());
        values.put(Value.COLUMN_IMAGE, theme.getImage());
        values.put(Value.COLUMN_COLOR, theme.getColor());

        // return inserted row id
        return db.insert(Value.TABLE_THEMES, null, values);
    }

    /*
     * check if theme exists
     */
    public boolean exist(String theme_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_THEMES + " WHERE " + Value.COLUMN_NODE + " = '" + theme_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }

    /*
     * get single theme by field
     */
    public Theme get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_THEMES + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Theme theme = new Theme();
        theme.setNode((c.getString(c.getColumnIndex(Value.COLUMN_NODE))));
        theme.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        theme.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        theme.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        theme.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        theme.setColor(c.getString(c.getColumnIndex(Value.COLUMN_COLOR)));

        c.close();

        return theme;
    }

    /*
     * get single theme
     */
    public Theme find(String theme_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_THEMES + " WHERE " + Value.COLUMN_NODE + " = '" + theme_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Theme theme = new Theme();
        theme.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        theme.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        theme.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        theme.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        theme.setColor(c.getString(c.getColumnIndex(Value.COLUMN_COLOR)));

        c.close();

        return theme;
    }

    /*
     * get some themes
     */
    public List<Theme> fetch(int rows, String field, String order) {
        List<Theme> themes = new ArrayList<>();
        String limit = "";
        if(rows > 0){
            limit = " LIMIT " + rows;
        }
        String orderBy = "";
        if(field.length() > 0){
            orderBy = " ORDER BY " + field + " " + order.toUpperCase();
        }
        String sql = "SELECT  * FROM " + Value.TABLE_THEMES + orderBy + limit;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Theme theme = new Theme();
                theme.setNode((c.getString(c.getColumnIndex(Value.COLUMN_NODE))));
                theme.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                theme.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
                theme.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                theme.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
                theme.setColor(c.getString(c.getColumnIndex(Value.COLUMN_COLOR)));

                // adding to theme list
                themes.add(theme);
            } while (c.moveToNext());
        }

        c.close();

        return themes;
    }

    /*
     * get last theme
     */
    public Theme last() {
        String sql = "SELECT  * FROM " + Value.TABLE_THEMES + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Theme theme = new Theme();
        theme.setNode((c.getString(c.getColumnIndex(Value.COLUMN_NODE))));
        theme.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        theme.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        theme.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        theme.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        theme.setColor(c.getString(c.getColumnIndex(Value.COLUMN_COLOR)));

        c.close();

        return theme;
    }

    /*
     * getting all themes
     * */
    public List<Theme> all() {
        List<Theme> themes = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_THEMES;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Theme theme = new Theme();
                theme.setNode((c.getString(c.getColumnIndex(Value.COLUMN_NODE))));
                theme.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                theme.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
                theme.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                theme.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
                theme.setColor(c.getString(c.getColumnIndex(Value.COLUMN_COLOR)));

                // adding to theme list
                themes.add(theme);
            } while (c.moveToNext());
        }

        c.close();

        return themes;
    }

    /*
     * Updating an theme
     */
    public int update(Theme theme) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_NODE, theme.getNode());
        values.put(Value.COLUMN_TITLE, theme.getTitle());
        values.put(Value.COLUMN_CAPTION, theme.getCaption());
        values.put(Value.COLUMN_IMAGE, theme.getImage());
        values.put(Value.COLUMN_COLOR, theme.getColor());

        // updating row
        return db.update(Value.TABLE_THEMES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(theme.getNode()) });
    }

    /*
     * Deleting an theme
     */
    public void delete(long theme_node) {
        db.delete(Value.TABLE_THEMES, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(theme_node) });
    }

    /*
     * Mark theme as read
     */
    public int read(String theme_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_THEMES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(theme_node) });
    }

    /*
     * Refresh all local data
     */
    public void refresh(Theme theme){
        if(exist(theme.getNode())){
            update(theme);
        }else{
            add(theme);
        }
    }

    public void reset(){
        database.reset(Value.SQL_THEME, Value.TABLE_THEMES);
    }
}
