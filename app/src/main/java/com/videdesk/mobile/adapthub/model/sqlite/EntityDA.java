package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityDA {

    private SQLiteDatabase db;

    public EntityDA(){}

    public EntityDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting entity Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_ENTITIES;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a entity
     */
    public long add(Entity entity) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_UID, entity.getUid());
        values.put(Value.COLUMN_NODE, entity.getNode());
        values.put(Value.COLUMN_TITLE, entity.getTitle());
        values.put(Value.COLUMN_CAPTION, entity.getCaption());
        values.put(Value.COLUMN_ABOUT, entity.getAbout());
        values.put(Value.COLUMN_DETAILS, entity.getDetails());
        values.put(Value.COLUMN_PHONE, entity.getPhone());
        values.put(Value.COLUMN_EMAIL, entity.getEmail());
        values.put(Value.COLUMN_URL, entity.getUrl());
        values.put(Value.COLUMN_POSTAL, entity.getPostal());
        values.put(Value.COLUMN_LOCATION, entity.getLocation());
        values.put(Value.COLUMN_NATION, entity.getNation());
        values.put(Value.COLUMN_IMAGE, entity.getImage());
        values.put(Value.COLUMN_CREATED, entity.getCreated());
        values.put(Value.COLUMN_STATUS, entity.getStatus());
        values.put(Value.COLUMN_READ, entity.getRead());

        // return inserted row id
        return db.insert(Value.TABLE_ENTITIES, null, values);
    }

    /*
     * check if entity exists
     */
    public boolean exist(String entity_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_ENTITIES + " WHERE " + Value.COLUMN_NODE + " = '" + entity_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }

    /*
     * get single entity
     */
    public Entity find(String entity_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_ENTITIES + " WHERE " + Value.COLUMN_NODE + " = '" + entity_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Entity entity = new Entity();
        entity.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        entity.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        entity.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        entity.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        entity.setAbout(c.getString(c.getColumnIndex(Value.COLUMN_ABOUT)));
        entity.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        entity.setPhone(c.getString(c.getColumnIndex(Value.COLUMN_PHONE)));
        entity.setEmail(c.getString(c.getColumnIndex(Value.COLUMN_EMAIL)));
        entity.setUrl(c.getString(c.getColumnIndex(Value.COLUMN_URL)));
        entity.setPostal(c.getString(c.getColumnIndex(Value.COLUMN_POSTAL)));
        entity.setLocation(c.getString(c.getColumnIndex(Value.COLUMN_LOCATION)));
        entity.setNation(c.getString(c.getColumnIndex(Value.COLUMN_NATION)));
        entity.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        entity.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        entity.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        entity.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return entity;
    }

    /*
     * get single entity
     */
    public Entity get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_ENTITIES + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Entity entity = new Entity();
        entity.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        entity.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        entity.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        entity.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        entity.setAbout(c.getString(c.getColumnIndex(Value.COLUMN_ABOUT)));
        entity.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        entity.setPhone(c.getString(c.getColumnIndex(Value.COLUMN_PHONE)));
        entity.setEmail(c.getString(c.getColumnIndex(Value.COLUMN_EMAIL)));
        entity.setUrl(c.getString(c.getColumnIndex(Value.COLUMN_URL)));
        entity.setPostal(c.getString(c.getColumnIndex(Value.COLUMN_POSTAL)));
        entity.setLocation(c.getString(c.getColumnIndex(Value.COLUMN_LOCATION)));
        entity.setNation(c.getString(c.getColumnIndex(Value.COLUMN_NATION)));
        entity.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        entity.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        entity.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        entity.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return entity;
    }

    /*
     * get last entity
     */
    public Entity last() {
        String sql = "SELECT  * FROM " + Value.TABLE_ENTITIES + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Entity entity = new Entity();
        entity.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        entity.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        entity.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        entity.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
        entity.setAbout(c.getString(c.getColumnIndex(Value.COLUMN_ABOUT)));
        entity.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        entity.setPhone(c.getString(c.getColumnIndex(Value.COLUMN_PHONE)));
        entity.setEmail(c.getString(c.getColumnIndex(Value.COLUMN_EMAIL)));
        entity.setUrl(c.getString(c.getColumnIndex(Value.COLUMN_URL)));
        entity.setPostal(c.getString(c.getColumnIndex(Value.COLUMN_POSTAL)));
        entity.setLocation(c.getString(c.getColumnIndex(Value.COLUMN_LOCATION)));
        entity.setNation(c.getString(c.getColumnIndex(Value.COLUMN_NATION)));
        entity.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        entity.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        entity.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        entity.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return entity;
    }

    /*
     * getting all entities
     * */
    public List<Entity> all() {
        List<Entity> entities = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_ENTITIES;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Entity entity = new Entity();
                entity.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                entity.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                entity.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
                entity.setCaption(c.getString(c.getColumnIndex(Value.COLUMN_CAPTION)));
                entity.setAbout(c.getString(c.getColumnIndex(Value.COLUMN_ABOUT)));
                entity.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
                entity.setPhone(c.getString(c.getColumnIndex(Value.COLUMN_PHONE)));
                entity.setEmail(c.getString(c.getColumnIndex(Value.COLUMN_EMAIL)));
                entity.setUrl(c.getString(c.getColumnIndex(Value.COLUMN_URL)));
                entity.setPostal(c.getString(c.getColumnIndex(Value.COLUMN_POSTAL)));
                entity.setLocation(c.getString(c.getColumnIndex(Value.COLUMN_LOCATION)));
                entity.setNation(c.getString(c.getColumnIndex(Value.COLUMN_NATION)));
                entity.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
                entity.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                entity.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
                entity.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

                // adding to entity list
                entities.add(entity);
            } while (c.moveToNext());
        }

        c.close();

        return entities;
    }

    /*
     * Updating an entity
     */
    public int update(Entity entity) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_TITLE, entity.getTitle());
        values.put(Value.COLUMN_CAPTION, entity.getCaption());
        values.put(Value.COLUMN_ABOUT, entity.getAbout());
        values.put(Value.COLUMN_DETAILS, entity.getDetails());
        values.put(Value.COLUMN_PHONE, entity.getPhone());
        values.put(Value.COLUMN_EMAIL, entity.getEmail());
        values.put(Value.COLUMN_URL, entity.getUrl());
        values.put(Value.COLUMN_POSTAL, entity.getPostal());
        values.put(Value.COLUMN_LOCATION, entity.getLocation());
        values.put(Value.COLUMN_NATION, entity.getNation());
        values.put(Value.COLUMN_IMAGE, entity.getImage());

        // updating row
        return db.update(Value.TABLE_ENTITIES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(entity.getNode()) });
    }

    /*
     * Deleting an entity
     */
    public void delete(String entity_node) {
        db.delete(Value.TABLE_ENTITIES, Value.COLUMN_NODE + " = ?",
                new String[] { entity_node });
    }

    /*
     * Mark entity as read
     */
    public int mark(String entity_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_ENTITIES, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(entity_node) });
    }

    public void refresh(Entity entity){
        if(entity.getStatus().equals(Value.KEY_STATUS_ACTIVE)) {
            if (exist(entity.getNode())) {
                update(entity);
            } else {
                add(entity);
            }
        }else{
            delete(entity.getNode());
        }
    }
}
