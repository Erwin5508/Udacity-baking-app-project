package com.example.android.bakingapp;

/**
 * Created by ErwinF on 12/21/2017.
 */

import android.content.Intent;
import android.widget.RemoteViewsService;


public class WidgetService extends RemoteViewsService {
    private final String TAG = "WidgetService";
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
//        final String sTitles = "titles";
//        final String sIngredients = "ingredients";
//        Bundle bundle = intent.getExtras();
//        if (bundle == null) return new ListRemoteViewsFactory(getApplicationContext(), intent);
//        String[] titles = bundle.getStringArray(sTitles);
//        String[] ingredients = bundle.getStringArray(sIngredients);
        return new ListRemoteViewsFactory(getApplicationContext(), intent);
    }
}
