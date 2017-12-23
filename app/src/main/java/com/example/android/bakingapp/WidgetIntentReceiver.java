package com.example.android.bakingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by ErwinF on 12/15/2017.
 */

public class WidgetIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.example.android.bakingapp.action.update_ingredients_textview")) {
            Bundle bundle = intent.getExtras();
            String ingredients = bundle.getString("ingredients");
            startActionUpdateIngredients(context, ingredients);
        }
        Log.w("WidgetIntentReceiver", "WidgetIntentReceiver called..");

    }

    private void startActionUpdateIngredients(Context context, String ingredients) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        remoteViews.setTextViewText(R.id.widget_ingredients, ingredients);
    }
}
