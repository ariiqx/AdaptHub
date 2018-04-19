package com.videdesk.mobile.adapthub.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public Database(Context context) {
        super(context, Value.DATABASE_NAME, null, Value.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(Value.SQL_ALBUM);
        db.execSQL(Value.SQL_CHAT);
        db.execSQL(Value.SQL_ENTITY);
        db.execSQL(Value.SQL_EVENT);
        db.execSQL(Value.SQL_FILE);
        db.execSQL(Value.SQL_JOB);
        db.execSQL(Value.SQL_LIKE);
        db.execSQL(Value.SQL_NOTE);
        db.execSQL(Value.SQL_PERSON);
        db.execSQL(Value.SQL_PHOTO);
        db.execSQL(Value.SQL_SCHOLL);
        db.execSQL(Value.SQL_STORY);
        db.execSQL(Value.SQL_THEME);
        db.execSQL(Value.SQL_THREAD);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_ALBUMS);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_CHATS);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_ENTITIES);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_FILES);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_JOBS);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_LIKES);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_PEOPLE);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_PHOTOS);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_SCHOLLS);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_STORIES);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_THEMES);
        db.execSQL("DROP TABLE IF EXISTS " + Value.TABLE_THREADS);

        // create new tables
        onCreate(db);
    }

    public SQLiteDatabase open(){
        return this.getWritableDatabase();
    }

    public void close(){
        SQLiteDatabase db = open();
        if (db != null && db.isOpen())
            db.close();
    }

    /*
     * Refresh all local data
     */
    public void reset(String sql, String table_name){
        drop(table_name);
        make(sql);
    }

    private void drop(String table_name){
        SQLiteDatabase db = open();
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
    }

    private void make(String sql){
        SQLiteDatabase db = open();
        db.execSQL(sql);
    }
}
