package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "ListRemoteViewsFactory";
    private Context mContext;
    private String[] mDataTitles;
    private String[] mDataIngredients;

    ListRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
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
        if(mDataTitles == null) return 0;
        return mDataTitles.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(mDataTitles == null) return null;
        String title = mDataTitles[position];

        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                android.R.layout.simple_list_item_1);
        view.setTextViewText(android.R.id.text1, title);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra("i", position);
        view.setOnClickFillInIntent(android.R.id.text1, fillInIntent);

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
        int n = 11;
        mDataTitles = new String[n];
        mDataIngredients = new String[n];
        for (int i = 0; i < n; i++) {
            mDataTitles[i] = "title: " + i;
            mDataIngredients[i] = "ingredients: " + i;
        }
    }
}
