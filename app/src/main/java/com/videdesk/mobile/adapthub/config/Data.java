package com.videdesk.mobile.adapthub.config;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.model.Thread;

public class Data {

    private SQLiteDatabase db;

    public Data(){}

    public Data(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Count records
    public int count(String table, String whereKey, String whereOperator, String whereValue) {
        // WHERE CLAUSE
        String where = "";
        if(whereKey != null && whereOperator != null && whereValue != null){
            where = " WHERE " + whereKey + " " + whereOperator + " '" + whereValue + "'";
        }
        String sql = "SELECT  * FROM " + table + where;

        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Get single record
     */
    public Thread get(String table, String key, String value, String order) {

        String sql = "SELECT  * FROM " + table + " WHERE " + key + " = '" + value + "'";

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
}
