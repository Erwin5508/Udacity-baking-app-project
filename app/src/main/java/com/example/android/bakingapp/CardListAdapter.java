package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ErwinF on 11/23/2017.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardListViewHolder> {

    private Context mContext;
    private final CardListAdapterOnClickHandler mClickHandler;

    public CardListAdapter(Context context, CardListAdapterOnClickHandler click) {
        this.mContext = context;
        mClickHandler = click;
    }

    public interface CardListAdapterOnClickHandler {
        void onClick(int position);
    }

    @Override
    public CardListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.card_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new CardListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardListViewHolder holder, int position) {
        holder.textView.setText(JsonInfoUtils.RECIPE_NAMES[position]);
    }

    @Override
    public int getItemCount() {
        if (JsonInfoUtils.RECIPE_NAMES == null) return 0;
        return JsonInfoUtils.RECIPE_NAMES.length;
    }

    public class CardListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;

        public CardListViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.card_view) ;
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(mContext, textView.getText(),Toast.LENGTH_SHORT).show();
            mClickHandler.onClick(getAdapterPosition());
        }
    }
}