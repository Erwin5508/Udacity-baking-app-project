package com.example.android.bakingapp;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ErwinF on 12/25/2017.
 */

public class IngredientsContract {

    public static final String AUTHORITY = "com.example.android.bakingapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_TO_INGREDIENTS = "all_ingredients";

    public static final class IngredientsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TO_INGREDIENTS).build();

        public static final String TABLE_NAME = "ingredients_table";

        public static final String COLUMN_TITLES = "titles";
        public static final String COLUMN_INGREDIENTS = "ingredients";
    }
}


