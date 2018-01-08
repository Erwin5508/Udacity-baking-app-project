package com.example.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ErwinF on 11/22/2017.
 */

public class JsonInfoUtils {

    private static final String TAG = "JsonInfoUtils";
    public static String[] RECIPE_NAMES;

    public static String[][] INGREDIENTS;
    public static String[][] INGREDIENTS_Quantity;
    public static String[][] INGREDIENTS_Measure;
    public static int[] INGREDIENTS_lengths;

    public static String[][] STEPS_SHORT_DESCR;
    public static String[][] STEPS_DESCRIPTION;
    public static String[][] STEPS_VIDEO_URL;
    public static String[][] STEPS_THUMBNAIL_URL;
    public static int[] STEPS_lengths;

    public static String[] SERVINGS;
    public static String[] IMAGE_URL;

    public static int mIndex;
    public static boolean mSmallScreen = true;
    public static boolean mediumLargeScreen = false;
    private static Context mContext;

    public static String setUpTheJsonData(String Data, Context context) throws JSONException{

        mContext = context;

        final String pathNames = "name";
        final String pathServings = "servings";
        final String pathImage = "image";

        JSONArray results = new JSONArray(Data);

        // retrieving functions
        RECIPE_NAMES = lookThroughSimplePaths(results, pathNames);
        SERVINGS = lookThroughSimplePaths(results, pathServings);
        IMAGE_URL = lookThroughSimplePaths(results, pathImage);
        setINGREDIENTS(results);
        setSTEPS(results);
        makeWidgetData(context);

        return "setUpTheJsonData() executed in it's entirety";
    }

    private static String[] lookThroughSimplePaths(JSONArray results, String path) {
        String[] Data = new String[results.length()];
        try {
            for (int i = 0; i<results.length(); i++){
                JSONObject data = results.getJSONObject(i);
                Data[i] = data.getString(path);
            }
            return Data;
        } catch (Exception e) {
            return null;
        }
    }

    private static void setINGREDIENTS (JSONArray results) throws JSONException {

        final String arrayPath = "ingredients";
        final String pathQuantity = "quantity";
        final String pathMeasure = "measure";
        final String pathIngredient = "ingredient";

        INGREDIENTS_lengths = new int[results.length()];
        int max = 0;

        for (int i = 0; i<results.length(); i++) {
            JSONObject subResult = results.getJSONObject(i);
            JSONArray ingredients = subResult.getJSONArray(arrayPath);
            INGREDIENTS_lengths[i] = ingredients.length();
            if (ingredients.length() > max) {
                max = ingredients.length();
            }
        }

        INGREDIENTS = new String[results.length()][max];
        INGREDIENTS_Quantity = new String[results.length()][max];
        INGREDIENTS_Measure = new String[results.length()][max];

        for (int i = 0; i<results.length(); i++) {
            JSONObject subResult = results.getJSONObject(i);
            JSONArray ingredients = subResult.getJSONArray(arrayPath);

            for (int j = 0; j<ingredients.length(); j++) {
                JSONObject data = ingredients.getJSONObject(j);

                INGREDIENTS[i][j] = data.getString(pathIngredient);
                INGREDIENTS_Measure[i][j] = data.getString(pathMeasure);
                INGREDIENTS_Quantity[i][j] = data.getString(pathQuantity);
            }
        }
    }

    private static void setSTEPS (JSONArray results) throws JSONException {
        final String arrayPath = "steps";
        final String pathShortDescr = "shortDescription";
        final String pathDescription = "description";
        final String pathVideoUrl = "videoURL";
        final String pathThumbnailURL = "thumbnailURL";

        STEPS_lengths = new int[results.length()];
        int max = 0;

        for (int i = 0; i<results.length(); i++) {
            JSONObject subResult = results.getJSONObject(i);
            JSONArray steps = subResult.getJSONArray(arrayPath);
            STEPS_lengths[i] = steps.length();
            if (steps.length() > max) {
                max = steps.length();
            }
        }

        STEPS_SHORT_DESCR = new String[results.length()][max];
        STEPS_DESCRIPTION = new String[results.length()][max];
        STEPS_VIDEO_URL = new String[results.length()][max];
        STEPS_THUMBNAIL_URL = new String[results.length()][max];

        for (int i = 0; i<results.length(); i++) {
            JSONObject subResult = results.getJSONObject(i);
            JSONArray steps = subResult.getJSONArray(arrayPath);

            for (int j = 0; j<steps.length(); j++) {
                JSONObject data = steps.getJSONObject(j);

                STEPS_SHORT_DESCR[i][j] = data.getString(pathShortDescr);
                STEPS_DESCRIPTION[i][j] = data.getString(pathDescription);
                STEPS_VIDEO_URL[i][j] = data.getString(pathVideoUrl);
                STEPS_THUMBNAIL_URL[i][j] = data.getString(pathThumbnailURL);
            }
        }
    }

    public static void makeWidgetData(Context context) {
        if (JsonInfoUtils.RECIPE_NAMES == null) return;

        // call the db
        ContentResolver contentResolver = context.getContentResolver();

        // delete everything
        try {
            Uri uri = IngredientsContract.IngredientsEntry.CONTENT_URI;
            contentResolver.delete(uri, null, null);

        } catch (Exception e) {
            Log.e(TAG, "Content resolver broke\n-----\n-----\n" + e.toString());
        }

        // make the data
        String[] dataTitles = JsonInfoUtils.RECIPE_NAMES;
        String[] dataIngredients = new String[JsonInfoUtils.RECIPE_NAMES.length];
        for (int i = 0; i < JsonInfoUtils.RECIPE_NAMES.length; i++){
            StringBuilder string = new StringBuilder("Here is the ingredients you'll need:\n");
            for (int j = 0; j < JsonInfoUtils.INGREDIENTS_lengths[i]; j++) {
                string.append("\n* " + JsonInfoUtils.INGREDIENTS[i][j]);
            }
            dataIngredients[i] = string.toString();
        }

        // send the data to the database
        for (int i = 0; i < dataTitles.length; i++) {
            ContentValues cv = new ContentValues();
            cv.put(IngredientsContract.IngredientsEntry.COLUMN_TITLES, dataTitles[i]);
            cv.put(IngredientsContract.IngredientsEntry.COLUMN_INGREDIENTS, dataIngredients[i]);
            try {
                contentResolver.insert
                        (IngredientsContract.IngredientsEntry.CONTENT_URI, cv);
            }  catch (Exception e){
                Log.e(TAG + "2", "Content resolver broke\n-----\n-----\n" + e.toString());
            }
        }
        // get the widget to load the new data
        Intent intent = new Intent(context, BakingAppWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        context.sendBroadcast(intent);
    }

    public static void saveIndex(int index) {
        mIndex = index;
    }

    public static void smallScreen(int i) {
        switch (i) {
            case 0:
                mSmallScreen = false;
                break;
            case 1:
                mSmallScreen = true;
                break;
            case 2:
                mediumLargeScreen = true;
                break;
            default:
                Log.e("Screen Size Issue", "\n|\nsmallScreen(0>i>2)\n|\n|\n|\n|");
        }
    }

    public static boolean getLandscape(Context context) {
        return mediumLargeScreen && context.getResources()
                .getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }
}
