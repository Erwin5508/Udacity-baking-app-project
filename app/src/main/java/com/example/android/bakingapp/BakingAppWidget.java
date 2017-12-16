package com.example.android.bakingapp;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    static String[] mDataTitles;
    static String[] mDataIngredients;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String widgetText = "Wanabake?";

        makeData();

        // Construct the RemoteViews object
        RemoteViews views;


        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);
        // TODO -------------------- What to do for different screen sizes --------------------
        if (width < 10 || JsonInfoUtils.RECIPE_NAMES == null) { //   1){//
            // raw view
            views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            views.setTextViewText(R.id.appwidget_text, widgetText);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        } else {
            // recipes list + ingredients text
            views = getRecipeListView(context);

            // views.setRemoteAdapter(R.id.widget_ingredients, );
        }

        if (JsonInfoUtils.RECIPE_NAMES != null) {


        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static RemoteViews getRecipeListView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

        Intent intent = new Intent(context, ListWidgetService.class);
        ArrayList<String> dataList = new ArrayList<>(mDataTitles.length);
        ArrayList<String> dataIngredients = new ArrayList<>(mDataTitles.length);

        for (int i = 0; i<mDataTitles.length; i++) {
            dataList.add(mDataTitles[i]);
            dataIngredients.add(mDataIngredients[i]);
        }

        intent.putStringArrayListExtra("titles", dataList);
        intent.putStringArrayListExtra("ingredients", dataIngredients);
        views.setRemoteAdapter(R.id.widget_list_recipes, intent);

        Log.d("titles", "getRecipeListView: " + dataList.toString());
        Log.d("ingredients", "getRecipeListView: " + dataIngredients.toString());

        return views;
    }

    public static PendingIntent buildRecipyShowIngredientsPendingIntent(Context context) {
        Intent intent = new Intent();
        //intent.setAction("")
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void makeData() {
        if (JsonInfoUtils.RECIPE_NAMES == null) return;
        mDataTitles = JsonInfoUtils.RECIPE_NAMES;
        String[] data = new String[JsonInfoUtils.RECIPE_NAMES.length];
        for (int i = 0; i < JsonInfoUtils.RECIPE_NAMES.length; i++){
            StringBuilder string = new StringBuilder();
            for (int j = 0; j < JsonInfoUtils.INGREDIENTS_lengths[i]; j++) {
                string.append("\n" + JsonInfoUtils.INGREDIENTS[i][j]);
            }
            data[i] = string.toString();
        }
        mDataIngredients = data;
    }
}
