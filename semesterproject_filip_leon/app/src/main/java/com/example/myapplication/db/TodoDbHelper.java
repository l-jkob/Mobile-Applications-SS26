package com.example.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todos.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TODOS = "todos";
    // MANDATORY for CursorAdapter (Lecture 06)
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DONE = "done";
    public static final String COLUMN_FAVORITE = "favorite";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_DUE_TIME = "due_time";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_TODOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_DONE + " INTEGER, " +
                    COLUMN_FAVORITE + " INTEGER, " +
                    COLUMN_DUE_DATE + " TEXT, " +
                    COLUMN_DUE_TIME + " TEXT" +
                    ");";

    public TodoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);
        onCreate(db);
    }
}