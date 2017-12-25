package com.example.android.bakingapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.bakingapp.IngredientsContract.IngredientsEntry.TABLE_NAME;

/**
 * Created by ErwinF on 12/25/2017.
 */

public class IngredientsContentProvider extends ContentProvider {

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static final int ALL_INGREDIENTS = 100;
    public static final int INGREDIENTS_WITH_ID = 101;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(IngredientsContract.AUTHORITY,
                IngredientsContract.PATH_TO_INGREDIENTS, ALL_INGREDIENTS);
        uriMatcher.addURI(IngredientsContract.AUTHORITY,
                IngredientsContract.PATH_TO_INGREDIENTS + "/#", INGREDIENTS_WITH_ID);

        return uriMatcher;
    }

    private IngredientsDbHelper mIngredientsDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mIngredientsDbHelper = new IngredientsDbHelper(context);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mIngredientsDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            case ALL_INGREDIENTS:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case INGREDIENTS_WITH_ID:
                String id = uri.getPathSegments().get(1);

                String mSelection = "_id=?";
                String[] mSelectionArgs = new String[]{id};

                retCursor = db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mIngredientsDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;
        switch (match) {
            case ALL_INGREDIENTS:
                long id = db.insert(TABLE_NAME, null, values);
                if (id < 0) {
                    returnUri = ContentUris.withAppendedId
                            (IngredientsContract.IngredientsEntry.CONTENT_URI, id);
                } else {
                    throw new UnsupportedOperationException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unkown Uri:" + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mIngredientsDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int tasksDeleted;
        switch (match) {

            case INGREDIENTS_WITH_ID:
                tasksDeleted = db.delete(TABLE_NAME,
                        IngredientsContract.IngredientsEntry._ID + "=?",
                        new String[]{selection});
                break;
            case ALL_INGREDIENTS:
                tasksDeleted = db.delete(TABLE_NAME, null, null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (tasksDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
