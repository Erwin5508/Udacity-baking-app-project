package com.example.android.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{

    String issueReporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            screenTest();
            setContentView(R.layout.activity_main);
            final LoaderManager.LoaderCallbacks<String> callback = MainActivity.this;
            getSupportLoaderManager().initLoader(0, null, callback);
        } catch (Exception e) {
            toastMessage(e.toString());
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            String Data = null;

            @Override
            protected void onStartLoading() {
                if (Data != null) {
                    deliverResult(Data);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                try {
                    URL url = NetworkUtis.getRecipeUrl();
                    if (url == null) {
                        return "Malformed Url";
                    }
                    Data = NetworkUtis.getResponseFromHttpUrl(url);
                    Data = Data.replace("\n", "");
                    issueReporter = JsonInfoUtils.setUpTheJsonData(Data, MainActivity.this);

                } catch (Exception e) {
                    return "nada\n" + e.toString();
                }
                return Data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        // Tests the loader to see if the data was properly serialized
        CardListFragment cardListFragment = (CardListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.master_list_fragment);
        cardListFragment.resetAdapter();
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
    }

    public void toastMessage(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.BOTTOM, 0, 0);

        toast.show();
    }

    private void screenTest() {
        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {

            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                JsonInfoUtils.smallScreen(true);
                break;

            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                JsonInfoUtils.smallScreen(false);
                break;
            default:
                toastMessage("This Screen size wasn't accounted for");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
