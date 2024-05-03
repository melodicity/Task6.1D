package com.example.task61d;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

// Helper class to manage the DB
public class AccountDBHelper extends SQLiteOpenHelper implements BaseColumns {
    public static final String DATABASE_NAME = "accounts.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_NAME = "accounts";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_INTERESTS = "interests"; // all interests will be stored as CSV

    public AccountDBHelper(Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    // Create a database for storing data locally
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY, " +
                COLUMN_USERNAME + " TEXT NOT NULL," +
                COLUMN_EMAIL + " TEXT NOT NULL," +
                COLUMN_PASSWORD + " TEXT NOT NULL," +
                COLUMN_PHONE + " TEXT NOT NULL," +
                COLUMN_INTERESTS + " TEXT);");
    }

    // On version change, delete the old database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
