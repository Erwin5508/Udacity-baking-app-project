package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
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
        return new ListRemoteViewsFactory(this.getApplicationContext(), dataTitles, dataIngredients);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private String[] mDataTitles;
    private String[] mDataIngredients;

    ListRemoteViewsFactory(Context applicationContext, String[] dataTitles, String[] dataIngredients) {
        this.mDataTitles = dataTitles;
        this.mDataIngredients = dataIngredients;
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
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

    @Override
    public void onDestroy() {
        mDataTitles = null;
        mDataIngredients = null;
    }

    @Override
    public int getCount() {
        if(mDataTitles == null) return 0;
        return mDataTitles.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(mDataTitles == null) return null;
        String title = mDataTitles[position];
        String ingredients = mDataIngredients[position];

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget);

        views.setTextViewText(R.id.widget_list_recipes, title);

        Bundle extras = new Bundle();
        extras.putInt("index", position);
        extras.putString("ingredients", ingredients);
        Intent intent = new Intent();
        intent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_list_recipes, intent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
