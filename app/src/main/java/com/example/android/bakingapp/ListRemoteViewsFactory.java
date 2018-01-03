package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import static com.example.android.bakingapp.BakingAppWidget.mCursor;

public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = "ListRemoteViewsFactory";
    private Context mContext;
    private static String[] mDataTitles;
    private static String[] mDataIngredients;

    ListRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
        makeData(mContext);
    }

    @Override
    public void onDataSetChanged() {
        makeData(mContext);
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

    public static void makeData(Context context) {
        try {
            if (mCursor != null) mCursor.close();
            mCursor = context.getContentResolver().query(
                    IngredientsContract.IngredientsEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    IngredientsContract.IngredientsEntry._ID);
            if (mCursor == null) {
                makeFakeData();
                mDataIngredients[0] = "Most likely no internet:\nNo data was loaded\n" +
                        "Try restarting the App";
                return;
            }
            int i = 0;
            int size = mCursor.getCount();
            mDataTitles = new String[size];
            mDataIngredients = new String[size];
            while (mCursor.moveToNext()) {
                mDataTitles[i] = mCursor.getString(mCursor.getColumnIndex
                        (IngredientsContract.IngredientsEntry.COLUMN_TITLES));
                mDataIngredients[i] = mCursor.getString(mCursor.getColumnIndex
                        (IngredientsContract.IngredientsEntry.COLUMN_INGREDIENTS));
                i++;
            }
        } catch (Exception e) {
            makeFakeData();
            mDataIngredients[0] = e.toString();
        } finally {
            mCursor.close();
        }
    }

    private static void makeFakeData() {
        int n = 4;
        mDataTitles = new String[n];
        mDataIngredients = new String[n];
        for (int i = 0; i < n; i++) {
            mDataTitles[i] = "title: " + i;
            mDataIngredients[i] = "ingredients: " + i;
        }
    }
}
