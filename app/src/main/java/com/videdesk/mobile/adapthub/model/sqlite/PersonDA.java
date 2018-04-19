package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonDA {

    private SQLiteDatabase db;

    public PersonDA(){}

    public PersonDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting person Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_PEOPLE;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a person
     */
    public long add(Person person) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_NODE, person.getNode());
        values.put(Value.COLUMN_ROLE, person.getRole());
        values.put(Value.COLUMN_NAME, person.getName());
        values.put(Value.COLUMN_EMAIL, person.getEmail());
        values.put(Value.COLUMN_PHONE, person.getPhone());
        values.put(Value.COLUMN_IMAGE, person.getImage());
        values.put(Value.COLUMN_NATION, person.getNation());
        values.put(Value.COLUMN_REGION, person.getRegion());
        values.put(Value.COLUMN_LOCATION, person.getLocation());
        values.put(Value.COLUMN_GENDER, person.getGender());
        values.put(Value.COLUMN_CREATED, person.getCreated());
        values.put(Value.COLUMN_STATUS, person.getStatus());

        // return inserted row id
        return db.insert(Value.TABLE_PEOPLE, null, values);
    }

    /*
     * check if person exists
     */
    public boolean exist(String person_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_PEOPLE + " WHERE " + Value.COLUMN_NODE + " = '" + person_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }

    /*
     * get single person
     */
    public Person find(String person_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_PEOPLE + " WHERE " + Value.COLUMN_NODE + " = '" + person_node + "'";
        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Person person = new Person();
        person.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        person.setRole(c.getString(c.getColumnIndex(Value.COLUMN_ROLE)));
        person.setName(c.getString(c.getColumnIndex(Value.COLUMN_NAME)));
        person.setEmail(c.getString(c.getColumnIndex(Value.COLUMN_EMAIL)));
        person.setPhone(c.getString(c.getColumnIndex(Value.COLUMN_PHONE)));
        person.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        person.setNation(c.getString(c.getColumnIndex(Value.COLUMN_NATION)));
        person.setRegion(c.getString(c.getColumnIndex(Value.COLUMN_REGION)));
        person.setLocation(c.getString(c.getColumnIndex(Value.COLUMN_LOCATION)));
        person.setGender(c.getString(c.getColumnIndex(Value.COLUMN_GENDER)));
        person.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        person.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));

        c.close();

        return person;
    }

    /*
     * get single person
     */
    public Person get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_PEOPLE + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Person person = new Person();
        person.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        person.setRole(c.getString(c.getColumnIndex(Value.COLUMN_ROLE)));
        person.setName(c.getString(c.getColumnIndex(Value.COLUMN_NAME)));
        person.setEmail(c.getString(c.getColumnIndex(Value.COLUMN_EMAIL)));
        person.setPhone(c.getString(c.getColumnIndex(Value.COLUMN_PHONE)));
        person.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        person.setNation(c.getString(c.getColumnIndex(Value.COLUMN_NATION)));
        person.setRegion(c.getString(c.getColumnIndex(Value.COLUMN_REGION)));
        person.setLocation(c.getString(c.getColumnIndex(Value.COLUMN_LOCATION)));
        person.setGender(c.getString(c.getColumnIndex(Value.COLUMN_GENDER)));
        person.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        person.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));

        c.close();

        return person;
    }

    /*
     * getting all people
     * */
    public List<Person> all() {
        List<Person> people = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_PEOPLE;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Person person = new Person();
                person.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                person.setRole(c.getString(c.getColumnIndex(Value.COLUMN_ROLE)));
                person.setName(c.getString(c.getColumnIndex(Value.COLUMN_NAME)));
                person.setEmail(c.getString(c.getColumnIndex(Value.COLUMN_EMAIL)));
                person.setPhone(c.getString(c.getColumnIndex(Value.COLUMN_PHONE)));
                person.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
                person.setNation(c.getString(c.getColumnIndex(Value.COLUMN_NATION)));
                person.setRegion(c.getString(c.getColumnIndex(Value.COLUMN_REGION)));
                person.setLocation(c.getString(c.getColumnIndex(Value.COLUMN_LOCATION)));
                person.setGender(c.getString(c.getColumnIndex(Value.COLUMN_GENDER)));
                person.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                person.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));

                // adding to person list
                people.add(person);
            } while (c.moveToNext());
        }

        c.close();

        return people;
    }

    /*
     * Updating an person
     */
    public int update(Person person) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_ROLE, person.getRole());
        values.put(Value.COLUMN_NAME, person.getName());
        values.put(Value.COLUMN_EMAIL, person.getEmail());
        values.put(Value.COLUMN_PHONE, person.getPhone());
        values.put(Value.COLUMN_IMAGE, person.getImage());
        values.put(Value.COLUMN_NATION, person.getNation());
        values.put(Value.COLUMN_REGION, person.getRegion());
        values.put(Value.COLUMN_LOCATION, person.getLocation());
        values.put(Value.COLUMN_GENDER, person.getGender());

        // updating row
        return db.update(Value.TABLE_PEOPLE, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(person.getNode()) });
    }

    public int photo(String photoUrl){

        return  1;
    }
    /*
     * Deleting an person
     */
    public void delete(String person_node) {
        db.delete(Value.TABLE_PEOPLE, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(person_node) });
    }

    /*
     * Mark person as read
     */
    public int mark(String person_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_PEOPLE, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(person_node) });
    }

    /*
     * Refresh all local data
     */
    public void refresh(Person person){
        if(person.getStatus().equals(Value.KEY_STATUS_ACTIVE) || person.getStatus().equals(Value.KEY_STATUS_PENDING)) {
            if (exist(person.getNode())) {
                update(person);
            } else {
                add(person);
            }
        }else{
            delete(person.getNode());
        }
    }
}
