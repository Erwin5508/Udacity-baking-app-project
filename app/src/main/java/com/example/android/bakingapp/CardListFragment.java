package com.example.android.bakingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by ErwinF on 11/23/2017.
 */

public class CardListFragment extends Fragment implements
CardListAdapter.CardListAdapterOnClickHandler{

    private CardListAdapter mAdapter;
    private ProgressBar mProgressBar;

    public CardListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_card_list, container, false);
        RecyclerView gridView = (RecyclerView) rootView.findViewById(R.id.card_grid_view);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);

        if (!JsonInfoUtils.mSmallScreen) {
            gridView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        } else {
            gridView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        }

        mAdapter = new CardListAdapter(getContext(), this);
        gridView.setAdapter(mAdapter);

        rootView.setBackgroundColor(Color.parseColor("#3F51B4"));
        return rootView;
    }

    public void resetAdapter() {
        mAdapter.notifyDataSetChanged();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(int position) {
        Intent recipeDetails = new Intent(getContext(), RecipeDetailActivity.class);
        Bundle extra = new Bundle();
        extra.putInt("index", position);
        recipeDetails.putExtras(extra);
        startActivity(recipeDetails);
    }
}
