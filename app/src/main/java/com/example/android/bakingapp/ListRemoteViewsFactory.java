package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private String[] mDataTitles;
    private String[] mDataIngredients;

    ListRemoteViewsFactory(Context applicationContext, Intent intent) {
        //this.mDataTitles = dataTitles;
        //this.mDataIngredients = dataIngredients;
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        Log.e("ListRemoteViewsFactory", "ListRemoteViewsFactory: created smthin");
        makeFakeData();
    }

    @Override
    public void onDataSetChanged() {
        makeFakeData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.e("ListRemoteViewsFactory", "ListRemoteViewsFactory getCount() = " + mDataTitles.length);
        //if(mDataTitles == null) return 0;
        return mDataTitles.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.e("ListRemoteViewsFactory", "ListRemoteViewsFactory getViewAt()");
        if(mDataTitles == null) return null;
        String title = mDataTitles[position];
        String ingredients = mDataIngredients[position];

        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                android.R.layout.simple_list_item_1);
        view.setTextViewText(android.R.id.text1, title);

        return view;
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
        return true;
    }

    public void makeData() {
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

    private void makeFakeData() {
        int n = 6;
        mDataTitles = new String[n];
        mDataIngredients = new String[n];
        for (int i = 0; i < n; i++) {
            mDataTitles[i] = "title: " + i;
            mDataIngredients[i] = "ingredients: " + i;
        }
    }
}
