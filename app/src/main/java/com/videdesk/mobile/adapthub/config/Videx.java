package com.videdesk.mobile.adapthub.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Joejo on 2017-12-14.
 */

public class Videx {
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;

    private Context mContext;

    public Videx(){}

    public Videx(Context context){
        this.mContext = context;
    }


    public boolean isConn(){
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public FirebaseFirestore  getFirestore(){
        if (firestore == null) {
            firestore = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            firestore.setFirestoreSettings(settings);
        }
        return firestore;
    }


    public StorageReference getStorage()
    {
        if (storage == null) {
            storage = FirebaseStorage.getInstance();
        }
        return storage.getReference();
    }


    @NonNull
    public static String toProper(String words){
        String[] strArray = words.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return builder.toString();
    }

    public void setPref(String key, String value){
        cutPref(key);
        SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.putString(key, value);
        prefEditor.commit();
    }

    private void cutPref(String key){
        SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor prefEditor = prefs.edit();
        prefEditor.remove(key);
        prefEditor.commit();
    }

    public String getPref(String key){
        SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(mContext);
        return prefs.getString(key, null);
    }

    public static String getNode(){
        long date  = System.currentTimeMillis();
        SimpleDateFormat calNode = new SimpleDateFormat("yyMMddHHmmss", Locale.US);
        Random rnd = new Random();
        int n = 1000000 + rnd.nextInt(9000000);
        return calNode.format(date) + ""  + n;
    }

    public static boolean isOutdated(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date strDate = sdf.parse(date);
            return new Date().after(strDate);
        }catch (ParseException ex){
            Log.w("Expired Date Error", ex.getMessage());
            return false;
        }
    }

    public static String getDated(){
        long date  = System.currentTimeMillis();
        SimpleDateFormat calDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return calDate.format(date);
    }

    public static String getDateAfterDays(int days){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, days);
        Date d = c.getTime();
        SimpleDateFormat calDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return calDate.format(d);
    }

    public static String getTimed(){
        long date  = System.currentTimeMillis();
        SimpleDateFormat calDate = new SimpleDateFormat("HH:mm:ss", Locale.US);
        return calDate.format(date);
    }

    public static String getDatedTimed(){
        long date  = System.currentTimeMillis();
        SimpleDateFormat calDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return calDate.format(date);
    }

    @NonNull
    public static String getCode(){
        // Generate random id, for example 2839520-V8M32QK
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder((1000000 + rnd.nextInt(9000000)) + "-");
        for (int i = 0; i < 7; i++)
            sb.append(chars[rnd.nextInt(chars.length)]);

        return sb.toString();
    }

    public int getColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = mContext.getResources().getIdentifier("mdcolor_" + typeColor, "array", mContext.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = mContext.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "square()"; }
    }

    public void reduceImage(String filePath){
        /*
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            //save scaled down image to cache dir
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            File imageFile = new File(filePath);

            // write the bytes in file
            FileOutputStream fo = new FileOutputStream(imageFile);
            fo.write(bytes.toByteArray());
        }catch (Exception ex){
            Log.e("ReduceImageError", ex.getMessage());
        }
        */
    }

    private static void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch(FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }
}
