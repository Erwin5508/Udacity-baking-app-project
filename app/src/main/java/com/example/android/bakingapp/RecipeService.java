package com.example.android.bakingapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ErwinF on 12/4/2017.
 */

public class RecipeService extends IntentService {


    public RecipeService() {
        super("RecipeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.e("RecipeService", "onHandleIntent called");
        int i = intent.getIntExtra("i", -1);
        BakingAppWidget.updateIngredients(i);
    }
}
