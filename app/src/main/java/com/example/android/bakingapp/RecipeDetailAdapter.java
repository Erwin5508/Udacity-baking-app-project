package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecipeDetailAdapter extends RecyclerView.Adapter<RecipeDetailAdapter.CardListViewHolder> {

    private Context mContext;
    private String[] mInfos;

    private int mIndex;

    private final RecipeDetailAdapterOnClickHandler mClickHandler;

    public RecipeDetailAdapter(Context context, RecipeDetailAdapterOnClickHandler click) {
        mContext = context;
        mClickHandler = click;
    }

    public interface RecipeDetailAdapterOnClickHandler {
        void onClick(int index, int i);
    }

    @Override
    public RecipeDetailAdapter.CardListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.card_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);

        return new RecipeDetailAdapter.CardListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeDetailAdapter.CardListViewHolder holder, int position) {
        holder.textView.setText(mInfos[position]);
    }

    @Override
    public int getItemCount() {
        if (mInfos == null) return 0;
        return mInfos.length;
    }

    public void setUp(int index) {
        mIndex = index;
        if (JsonInfoUtils.INGREDIENTS != null) {
            organize(index);
            notifyDataSetChanged();
        }
    }

    private void organize(int index) {
        mInfos = new String[JsonInfoUtils.STEPS_lengths[index] + 1];
        StringBuilder ingredients = new StringBuilder("Ingredients:\n");
        for (int i = 0; i < JsonInfoUtils.INGREDIENTS_lengths[index]; i++) {
            ingredients.append("\n-> " + JsonInfoUtils.INGREDIENTS[index][i]);
        }
        mInfos[0] = ingredients.toString();
        for (int i = 0; i < JsonInfoUtils.STEPS_lengths[index]; i++) {
            mInfos[i + 1] = i + ". " + JsonInfoUtils.STEPS_SHORT_DESCR[index][i];
        }
    }

    public class CardListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;

        public CardListViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.card_view);
            textView.setTextSize(20);
            textView.setAllCaps(false);
            textView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mIndex, getAdapterPosition() -1);
        }
    }
}