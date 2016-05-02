package com.automotive.hhi.mileagetracker.view.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Station;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class StationViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.item_station_name)
    TextView mName;
    @Bind(R.id.item_station_address)
    TextView mAddress;
    @Bind(R.id.item_station_distance)
    TextView mDistance;


    Station mHolderStation;


    public StationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setViewHolder(Station station, final ViewHolderOnClickListener<Station> selectedStationListener){
        mHolderStation = station;
        mName.setText(station.getName());
        mAddress.setText(station.getAddress());
        if(mHolderStation.getId() == 0){
            mDistance.setText(mHolderStation.getDistance());
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedStationListener.onClick(mHolderStation);
            }
        });

    }
}
