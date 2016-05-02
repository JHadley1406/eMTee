package com.automotive.hhi.mileagetracker.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.model.data.CarFactory;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;
import com.automotive.hhi.mileagetracker.view.viewholders.CarViewHolder;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class CarAdapter extends CursorRecyclerViewAdapter<CarViewHolder> {

    private Context mContext;
    private ViewHolderOnClickListener<Car> mCarOnClickListener;

    public CarAdapter(Context context
            , Cursor cursor
            , ViewHolderOnClickListener<Car> carOnClickListener){
        super(context, cursor);
        mContext = context;
        mCarOnClickListener = carOnClickListener;
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.car_item, parent, false);

        return new CarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarViewHolder viewHolder, Cursor cursor) {
        viewHolder.setViewHolder(mContext, CarFactory.fromCursor(cursor), mCarOnClickListener);
    }

}
