package com.automotive.hhi.mileagetracker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Fillup;
import com.automotive.hhi.mileagetracker.model.data.FillupFactory;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;
import com.automotive.hhi.mileagetracker.view.viewholders.FillupViewHolder;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class FillupAdapter extends CursorRecyclerViewAdapter<FillupViewHolder> {

    private Context mContext;
    private ViewHolderOnClickListener<Fillup> mFillupOnClickListener;

    public FillupAdapter(Context context
            , Cursor cursor
            , ViewHolderOnClickListener<Fillup> fillupOnClickListener){
        super(context, cursor);
        mContext = context;
        mFillupOnClickListener = fillupOnClickListener;
    }

    @Override
    public void onBindViewHolder(FillupViewHolder viewHolder, Cursor cursor) {
        viewHolder.setViewHolder(FillupFactory.fromCursor(cursor), mFillupOnClickListener);

    }

    @Override
    public FillupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fillup_item, parent, false);

        return new FillupViewHolder(itemView);
    }
}
