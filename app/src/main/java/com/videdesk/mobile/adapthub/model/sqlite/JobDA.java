package com.videdesk.mobile.adapthub.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.videdesk.mobile.adapthub.config.Database;
import com.videdesk.mobile.adapthub.config.Value;
import com.videdesk.mobile.adapthub.model.Job;

import java.util.ArrayList;
import java.util.List;

public class JobDA {

    private SQLiteDatabase db;

    public JobDA(){}

    public JobDA(Context context){
        Database database = new Database(context);
        this.db = database.open();
    }

    // Getting job Count
    public int count() {
        String sql = "SELECT  * FROM " + Value.TABLE_JOBS;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    /*
     * Creating a job
     */
    public long add(Job job) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_UID, job.getUid());
        values.put(Value.COLUMN_NODE, job.getNode());
        values.put(Value.COLUMN_TITLE, job.getTitle());
        values.put(Value.COLUMN_DETAILS, job.getDetails());
        values.put(Value.COLUMN_LOCATION, job.getLocation());
        values.put(Value.COLUMN_IMAGE, job.getImage());
        values.put(Value.COLUMN_DEADLINE, job.getDeadline());
        values.put(Value.COLUMN_WAGE, job.getWage());
        values.put(Value.COLUMN_CURRENCY, job.getCurrency());
        values.put(Value.COLUMN_CREATED, job.getCreated());
        values.put(Value.COLUMN_STATUS, job.getStatus());
        values.put(Value.COLUMN_READ, job.getRead());

        // return inserted row id
        return db.insert(Value.TABLE_JOBS, null, values);
    }

    /*
     * check if job exists
     */
    public boolean exist(String job_node){
        boolean it_exist = false;
        String sql = "SELECT  * FROM " + Value.TABLE_JOBS + " WHERE " + Value.COLUMN_NODE + " = '" + job_node + "'";

        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();
        if(count > 0){
            it_exist = true;
        }
        return it_exist;
    }
    /*
     * get single job
     */
    public Job find(String job_node) {
        String sql = "SELECT  * FROM " + Value.TABLE_JOBS + " WHERE " + Value.COLUMN_NODE + " = '" + job_node + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Job job = new Job();
        job.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        job.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        job.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        job.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        job.setLocation(c.getString(c.getColumnIndex(Value.COLUMN_LOCATION)));
        job.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        job.setDeadline(c.getString(c.getColumnIndex(Value.COLUMN_DEADLINE)));
        job.setWage(c.getString(c.getColumnIndex(Value.COLUMN_WAGE)));
        job.setCurrency(c.getString(c.getColumnIndex(Value.COLUMN_CURRENCY)));
        job.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        job.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        job.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return job;
    }

    /*
     * get single job
     */
    public Job get(String field, String value) {
        String sql = "SELECT  * FROM " + Value.TABLE_JOBS + " WHERE " + field + " = '" + value + "'";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Job job = new Job();
        job.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        job.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        job.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        job.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        job.setLocation(c.getString(c.getColumnIndex(Value.COLUMN_LOCATION)));
        job.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        job.setDeadline(c.getString(c.getColumnIndex(Value.COLUMN_DEADLINE)));
        job.setWage(c.getString(c.getColumnIndex(Value.COLUMN_WAGE)));
        job.setCurrency(c.getString(c.getColumnIndex(Value.COLUMN_CURRENCY)));
        job.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        job.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        job.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return job;
    }

    /*
     * get last job
     */
    public Job last() {
        String sql = "SELECT  * FROM " + Value.TABLE_JOBS + " ORDER BY " + Value.COLUMN_NODE + " DESC LIMIT 1";

        Cursor c = db.rawQuery(sql, null);

        if (c != null)
            c.moveToFirst();

        Job job = new Job();
        job.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
        job.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
        job.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
        job.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
        job.setLocation(c.getString(c.getColumnIndex(Value.COLUMN_LOCATION)));
        job.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
        job.setDeadline(c.getString(c.getColumnIndex(Value.COLUMN_DEADLINE)));
        job.setWage(c.getString(c.getColumnIndex(Value.COLUMN_WAGE)));
        job.setCurrency(c.getString(c.getColumnIndex(Value.COLUMN_CURRENCY)));
        job.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
        job.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
        job.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

        c.close();

        return job;
    }

    /*
     * getting all jobs
     * */
    public List<Job> all() {
        List<Job> jobs = new ArrayList<>();
        String sql = "SELECT  * FROM " + Value.TABLE_JOBS;

        Cursor c = db.rawQuery(sql, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Job job = new Job();
                job.setUid((c.getString(c.getColumnIndex(Value.COLUMN_UID))));
                job.setNode(c.getString(c.getColumnIndex(Value.COLUMN_NODE)));
                job.setTitle(c.getString(c.getColumnIndex(Value.COLUMN_TITLE)));
                job.setDetails(c.getString(c.getColumnIndex(Value.COLUMN_DETAILS)));
                job.setLocation(c.getString(c.getColumnIndex(Value.COLUMN_LOCATION)));
                job.setImage(c.getString(c.getColumnIndex(Value.COLUMN_IMAGE)));
                job.setDeadline(c.getString(c.getColumnIndex(Value.COLUMN_DEADLINE)));
                job.setWage(c.getString(c.getColumnIndex(Value.COLUMN_WAGE)));
                job.setCurrency(c.getString(c.getColumnIndex(Value.COLUMN_CURRENCY)));
                job.setCreated(c.getString(c.getColumnIndex(Value.COLUMN_CREATED)));
                job.setStatus(c.getString(c.getColumnIndex(Value.COLUMN_STATUS)));
                job.setRead(c.getString(c.getColumnIndex(Value.COLUMN_READ)));

                // adding to job list
                jobs.add(job);
            } while (c.moveToNext());
        }

        c.close();

        return jobs;
    }

    /*
     * Updating an job
     */
    public int update(Job job) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_TITLE, job.getTitle());
        values.put(Value.COLUMN_DETAILS, job.getDetails());
        values.put(Value.COLUMN_LOCATION, job.getLocation());
        values.put(Value.COLUMN_IMAGE, job.getImage());
        values.put(Value.COLUMN_DEADLINE, job.getDeadline());
        values.put(Value.COLUMN_WAGE, job.getWage());
        values.put(Value.COLUMN_CURRENCY, job.getCurrency());

        // updating row
        return db.update(Value.TABLE_JOBS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(job.getNode()) });
    }

    /*
     * Deleting an job
     */
    public void delete(String job_node) {
        db.delete(Value.TABLE_JOBS, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(job_node) });
    }

    /*
     * Mark job as read
     */
    public int mark(String job_node) {
        ContentValues values = new ContentValues();
        values.put(Value.COLUMN_READ, Value.KEY_READ_YES);

        // updating row
        return db.update(Value.TABLE_JOBS, values, Value.COLUMN_NODE + " = ?",
                new String[] { String.valueOf(job_node) });
    }

    /*
     * Refresh all local data
     */
    public void refresh(Job job){
        if(job.getStatus().equals(Value.KEY_STATUS_ACTIVE)) {
            if (exist(job.getNode())) {
                update(job);
            } else {
                add(job);
            }
        }else {
            delete(job.getNode());
        }
    }
}
