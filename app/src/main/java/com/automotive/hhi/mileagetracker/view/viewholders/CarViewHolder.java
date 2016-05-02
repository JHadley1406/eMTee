package com.automotive.hhi.mileagetracker.view.viewholders;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class CarViewHolder extends RecyclerView.ViewHolder {

    private final String LOG_TAG = CarViewHolder.class.getSimpleName();
    @Bind(R.id.item_car_image)
    public ImageView mImage;
    @Bind(R.id.item_car_name)
    public TextView mName;
    @Bind(R.id.item_car_make)
    public TextView mMake;
    @Bind(R.id.item_car_model)
    public TextView mModel;
    @Bind(R.id.item_car_year)
    public TextView mYear;
    @Bind(R.id.item_car_mpg)
    public TextView mMpg;

    public Car mHolderCar;


    public CarViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setViewHolder(Context context, Car car, final ViewHolderOnClickListener<Car> selectedCarListener){
        mHolderCar = car;
        if(mHolderCar.getImage() != null) {
            Picasso.with(context)
                    .load(Uri.parse(mHolderCar.getImage()))
                    .into(mImage);
        }else{
            mImage.getLayoutParams().height = 1;
        }
        mName.setText(car.getName());
        mMake.setText(car.getMake());
        mModel.setText(car.getModel());
        mYear.setText(String.format("%d", car.getYear()));
        mMpg.setText(String.format("%.1f", car.getAvgMpg()));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCarListener.onClick(mHolderCar);
            }
        });
    }
}
