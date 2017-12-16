package com.example.android.bakingapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ErwinF on 11/28/2017.
 */

public class RecipeDetailFragment extends Fragment
        implements RecipeDetailAdapter.RecipeDetailAdapterOnClickHandler {

    private RecipeDetailAdapter mAdapter;
    private TextView mVoilaText;
    public static int mI;
    private String LIST_STATE_KEY = "list_state_key";

    private Parcelable mListState;
    RecyclerView.LayoutManager mLayoutManager;

    public RecipeDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_card_list, container, false);
        RecyclerView gridView = (RecyclerView) rootView.findViewById(R.id.card_grid_view);

        mVoilaText = (TextView) rootView.findViewById(R.id.voila_text);
        mVoilaText.setVisibility(View.GONE);

        mLayoutManager = (new LinearLayoutManager(getContext()));
        if(savedInstanceState != null)
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        gridView.setLayoutManager(mLayoutManager);
        mAdapter = new RecipeDetailAdapter(getContext(), this);
        gridView.setAdapter(mAdapter);

        rootView.setBackgroundColor(Color.parseColor("#3F51B4"));
        return rootView;
    }

    public void setIndex(int index){
        mAdapter.setUp(index);
    }

    @Override
    public void onClick(int index, int i) {
        if (i == -1) return;
        mI = i;
        if (!JsonInfoUtils.mSmallScreen) {
            try {
                EventBus.getDefault().post(new IndexingEvent(i, index));
            } catch (Exception e) {
                RecipeDetailActivity det = (RecipeDetailActivity) getActivity();
                det.stepChange(i);
            }
            return;
        }
        Intent DetailStep = new Intent(getContext(), DetailStepActivity.class);
        Bundle extra = new Bundle();
        extra.putInt("index", index);
        extra.putInt("i", i);
        DetailStep.putExtras(extra);
        startActivity(DetailStep);
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        // Save list state
        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }
}
