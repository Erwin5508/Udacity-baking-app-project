package com.example.android.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by ErwinF on 12/4/2017.
 */

public class RecipeService extends IntentService {

    String[] mDataTitles;
    String[] mDataIngredients;

    public static final String ACTION_CHANGE_INGREDIENTS_TEXT =
            "com.example.android.bakingapp.action.update_ingredients_textview";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public RecipeService() {
        super("RecipeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("RecipeService", "onHandleIntent called");
        if (intent != null) {
            handleActionUpdateWidgetData();
        }
    }

    public void handleActionUpdateWidgetData() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_recipes);

        for (int appWidgetId : appWidgetIds) {
            BakingAppWidget.updateAppWidget(this, appWidgetManager, appWidgetIds[appWidgetId]);
        }
    }

    public static void startActionUpdateIngredients(Context context, int position, String[] ingredients) {
        Intent intent = new Intent(context, RecipeService.class);
        intent.setAction(ACTION_CHANGE_INGREDIENTS_TEXT);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putStringArray("ingredients", ingredients);
        intent.putExtras(bundle);
        context.startService(intent);
    }
}
