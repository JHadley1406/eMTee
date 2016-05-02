package com.automotive.hhi.mileagetracker.view.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Fillup;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class FillupViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.item_fillup_gallons)
    TextView mGallons;
    @Bind(R.id.item_fillup_cost)
    TextView mFuelCost;
    @Bind(R.id.item_fillup_total)
    TextView mTotalCost;
    @Bind(R.id.item_fillup_octane)
    TextView mOctane;
    @Bind(R.id.item_fillup_mpg)
    TextView mFillupMpg;
    @Bind(R.id.item_fillup_date)
    TextView mDate;

    Fillup mHolderFillup;

    public FillupViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setViewHolder(Fillup fillup, final ViewHolderOnClickListener<Fillup> onClickListener){
        mHolderFillup = fillup;
        mGallons.setText(String.format("%.2f", fillup.getGallons()));
        mFuelCost.setText(String.format("%.2f", fillup.getFuelCost()));
        mTotalCost.setText(String.format("%.2f", fillup.getTotalCost()));
        mOctane.setText(String.format("%d", fillup.getOctane()));
        mFillupMpg.setText(String.format("%.1f", fillup.getFillupMpg()));
        mDate.setText(fillup.getReadableDate());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(mHolderFillup);
            }
        });
    }
}
