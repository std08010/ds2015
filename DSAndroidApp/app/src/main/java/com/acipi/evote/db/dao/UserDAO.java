package com.acipi.evote.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.acipi.evote.utils.PropertiesUtils;

import java.util.HashMap;

/**
 * Created by Altin Cipi on 1/30/2015.
 */
public class UserDAO extends SQLiteOpenHelper
{
    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_SESSION_TOKEN = "session_token";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_COUNTRY_URL = "country_url";
    public static final String KEY_AVATAR_URL = "avatar_url";

    public UserDAO(Context context)
    {
        super(context, PropertiesUtils.getDBProps(context).getProperty("db.name"), null, Integer.valueOf(PropertiesUtils.getDBProps(context).getProperty("db.version")));
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER
                                   + "("
                                   + KEY_ID + " INTEGER PRIMARY KEY, "
                                   + KEY_USERNAME + " TEXT UNIQUE, "
                                   + KEY_PASSWORD + " TEXT, "
                                   + KEY_EMAIL + " TEXT, "
                                   + KEY_SESSION_TOKEN + " TEXT, "
                                   + KEY_COUNTRY + " TEXT, "
                                   + KEY_COUNTRY_URL + " TEXT, "
                                   + KEY_AVATAR_URL + " TEXT "
                                   + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     */
    public void addUser(String username, String password, String email, String sessionToken, String country, String countryURL, String avatarURL)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, username);
        values.put(KEY_PASSWORD, password);
        values.put(KEY_EMAIL, email);
        values.put(KEY_SESSION_TOKEN, sessionToken);
        values.put(KEY_COUNTRY, country);
        values.put(KEY_COUNTRY_URL, countryURL);
        values.put(KEY_AVATAR_URL, avatarURL);

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
    }

    public void updateValue(String column, String value)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(column, value);

        db.update(TABLE_USER, values, null, null);
        db.close();
    }

    public void updateValue(String column, Long value)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(column, value);

        db.update(TABLE_USER, values, null, null);
        db.close();
    }

    /**
     * Getting user data from database
     */
    public HashMap<String, Object> getUserDetails()
    {
        HashMap<String, Object> user = new HashMap<String, Object>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
        {
            user.put(KEY_USERNAME, cursor.getString(1));
            user.put(KEY_PASSWORD, cursor.getString(2));
            user.put(KEY_EMAIL, cursor.getString(3));
            user.put(KEY_SESSION_TOKEN, cursor.getString(4));
            user.put(KEY_COUNTRY, cursor.getString(5));
            user.put(KEY_COUNTRY_URL, cursor.getString(6));
            user.put(KEY_AVATAR_URL, cursor.getString(7));
        }
        cursor.close();
        db.close();

        // return user
        return user;
    }

    /**
     * Getting user login status
     * return true if rows are there in table
     */
    public int getRowCount()
    {
        String countQuery = "SELECT * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        // return row count
        return rowCount;
    }

    /**
     * Delete All Rows
     */
    public void resetTables()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USER, null, null);
        db.close();
    }
}
