package com.example.android.bakingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.bakingapp.IngredientsContract.*;


public class IngredientsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ingredients.db";

    private static final int DATABASE_VERSION = 0;

    public IngredientsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_INGREDIENTS_TABLE =
                "CREATE TABLE" + IngredientsEntry.TABLE_NAME + " (" +
                        IngredientsEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        IngredientsEntry.COLUMN_TITLES      + " TEXT NOT NULL, " +
                        IngredientsEntry.COLUMN_INGREDIENTS + " TEXT NOT NULL, " + "); ";

        db.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IngredientsEntry.TABLE_NAME);
        onCreate(db);
    }
}
