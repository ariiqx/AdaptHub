package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatDA {
    
    private SQLiteDatabase db;

    public ChatDA(){}

    public ChatDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting chat Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_CHATS;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a chat
     */
    public long add(Chat chat) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_UID, chat.getUid());
        values.put(Value.COLUMN_NODE, chat.getNode());
        values.put(Value.COLUMN_THREAD_NODE, chat.getThread_node());
        values.put(Value.COLUMN_DETAILS, chat.getDetails());
        values.put(Value.COLUMN_CREATED, chat.getCreated());
        values.put(Value.COLUMN_READ, chat.getRead());

        // return inserted row id
        return db.insert(Value.TABLE_CHATS, null, values);
    }
    /*
     * check if chat exists
     */
    public boolean exist(String chat_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_CHATS + " WHERE " + Value.COLUMN_NODE + " = '" + chat_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }

    /*
     * get single chat
     */
    public Chat find(String chat_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_CHATS + " WHERE " + Value.COLUMN_NODE + " = '" + chat_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Chat chat = new Chat();
        chat.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        chat.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        chat.setThread_node(c.getString(c.getColumnIndex(Value.COLUMN_THREAD_NODE)));
        chat.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        chat.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        chat.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return chat;
    }

    /*
     * get single chat
     */
    public Chat get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_CHATS + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Chat chat = new Chat();
        chat.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        chat.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        chat.setThread_node(c.getString(c.getColumnIndex(Value.COLUMN_THREAD_NODE)));
        chat.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        chat.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        chat.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return chat;
    }

    /*
     * get last chat
     */
    public Chat last() {
        String sql = "SELECT  * FROM " + Value.TABLE_CHATS + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Chat chat = new Chat();
        chat.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        chat.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        chat.setThread_node(c.getString(c.getColumnIndex(Value.COLUMN_THREAD_NODE)));
        chat.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        chat.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        chat.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return chat;
    }

    /*
     * getting all chats
     * */
    public List<Chat> all() {
        List<Chat> chats = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_CHATS;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Chat chat = new Chat();
                chat.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                chat.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                chat.setThread_node(c.getString(c.getColumnIndex(Value.COLUMN_THREAD_NODE)));
                chat.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
                chat.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                chat.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

                // adding to chat list
                chats.add(chat);
            } while (c.moveToNext());
        }

        c.close();

        return chats;
    }

    /*
     * getting all chats
     * */
    public List<Chat> byThread(String thread_node) {
        List<Chat> chats = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_CHATS + " WHERE " + Value.COLUMN_THREAD_NODE + " = '" + thread_node + "'";

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Chat chat = new Chat();
                chat.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                chat.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                chat.setThread_node(c.getString(c.getColumnIndex(Value.COLUMN_THREAD_NODE)));
                chat.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
                chat.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                chat.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

                // adding to chat list
                chats.add(chat);
            } while (c.moveToNext());
        }

        c.close();

        return chats;
    }

    /*
     * Updating an chat
     */
    public int update(Chat chat) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_DETAILS, chat.getDetails());

        // updating row
        return db.update(Value.TABLE_CHATS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(chat.getNode()) });
    }

    /*
     * Deleting an chat
     */
    public void delete(String chat_node) {
        db.delete(Value.TABLE_CHATS, Value.COLUMN_NODE + " = ?",
                new String[] { chat_node});
    }

    /*
     * Mark chat as read
     */
    public int mark(String chat_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_CHATS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(chat_node) });
    }

    /*
     * Refresh all local data
     */
    public void refresh(Chat chat){

        if(exist(chat.getNode())){
            update(chat);
        }else{
            add(chat);
        }
    }
}
