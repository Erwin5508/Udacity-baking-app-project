package com.example.android.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private static int mIndex;
    private static int mI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);


        try {
            Bundle extra = getIntent().getExtras();
            mIndex = extra.getInt("index");
        } catch (Exception e) {
            mIndex = JsonInfoUtils.mIndex;
        }

        RecipeDetailFragment recipeDetailFragment = (RecipeDetailFragment)getSupportFragmentManager()
                .findFragmentById(R.id.recipe_list_fragment);
        recipeDetailFragment.setIndex(mIndex);

        JsonInfoUtils.saveIndex(mIndex);

        boolean big = !JsonInfoUtils.mSmallScreen
                || JsonInfoUtils.getLandscape(getApplicationContext());

        if (big && savedInstanceState == null) {
            mI = recipeDetailFragment.mI;
            DetailStepFragment DetailFragment = (DetailStepFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.detail_step_fragment);
            DetailFragment.setIndex(mIndex, mI);
        }
    }

    // TODO replace this ------------
    public void stepChange(int I) {
        DetailStepFragment DetailFragment = (DetailStepFragment)getSupportFragmentManager()
                .findFragmentById(R.id.detail_step_fragment);
        DetailFragment.setIndex(mIndex, I);
    }


}
