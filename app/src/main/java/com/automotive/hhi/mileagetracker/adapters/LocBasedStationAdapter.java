package com.automotive.hhi.mileagetracker.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Station;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;
import com.automotive.hhi.mileagetracker.view.viewholders.StationViewHolder;

import java.util.List;

/**
 * Created by Josiah Hadley on 4/5/2016.
 */
public class LocBasedStationAdapter extends RecyclerView.Adapter<StationViewHolder> {

    private final String LOG_TAG = LocBasedStationAdapter.class.getSimpleName();

    private List<Station> mStations;
    private ViewHolderOnClickListener<Station> mOnClickListener;

    public LocBasedStationAdapter(List<Station> stations, ViewHolderOnClickListener<Station> onClickListener){

        mStations = stations;
        mOnClickListener = onClickListener;
    }


    @Override
    public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.station_item, parent, false);

        return new StationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StationViewHolder holder, int position) {
        holder.setViewHolder(mStations.get(position), mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }

    public void clearStations(){
        mStations.clear();
        notifyDataSetChanged();
    }

    public void updateStations(List<Station> stations){
        mStations = stations;
        notifyDataSetChanged();
    }
}
