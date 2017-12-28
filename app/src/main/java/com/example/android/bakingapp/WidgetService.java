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
        return new ListRemoteViewsFactory(getApplicationContext(), intent);
    }
}
