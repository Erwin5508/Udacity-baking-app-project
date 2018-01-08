package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailStepActivity extends AppCompatActivity {

    private int mIndex;
    private int mI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_step);

        if (savedInstanceState == null) {

            if (JsonInfoUtils.mSmallScreen || !JsonInfoUtils.getLandscape(getApplicationContext())) {
                Bundle extra = getIntent().getExtras();
                mIndex = extra.getInt("index");
                mI = extra.getInt("i");
            }

            DetailStepFragment DetailFragment = (DetailStepFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.detail_step_fragment);
            DetailFragment.setIndex(mIndex, mI);
        }
    }
}
