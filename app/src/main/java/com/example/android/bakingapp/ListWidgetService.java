package com.example.android.bakingapp;

import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Created by ErwinF on 12/3/2017.
 */

public class ListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.w("called", "ListWidgetService: " + intent.toString());
        //return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
        String[] dataTitles = intent.getStringArrayExtra("titles");
        String[] dataIngredients = intent.getStringArrayExtra("ingredients");
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

