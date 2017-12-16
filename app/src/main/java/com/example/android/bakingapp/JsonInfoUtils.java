package com.example.android.bakingapp;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ErwinF on 11/22/2017.
 */

public class JsonInfoUtils {

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

    public static String setUpTheJsonData(String Data, Context context) throws JSONException{



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

    public static void saveIndex(int index) {
        mIndex = index;
    }

    public static void smallScreen(boolean yesno) {
        mSmallScreen = yesno;
    }
}
