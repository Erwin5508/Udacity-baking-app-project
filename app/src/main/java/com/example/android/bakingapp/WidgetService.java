package com.example.android.bakingapp;

/**
 * Created by ErwinF on 12/21/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViewsService;


public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        final String sTitles = "titles";
        final String sIngredients = "ingredients";
        Bundle bundle = intent.getExtras();
        if (bundle == null) return new ListRemoteViewsFactory(getApplicationContext(), intent);
        String[] titles = bundle.getStringArray(sTitles);
        String[] ingredients = bundle.getStringArray(sIngredients);
        Log.e("WidgetService", "titles: " + titles.toString());
        return new ListRemoteViewsFactory(getApplicationContext(), intent);
    }
}
