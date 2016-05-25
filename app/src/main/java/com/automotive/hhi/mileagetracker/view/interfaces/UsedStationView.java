package com.automotive.hhi.mileagetracker.view.interfaces;

import android.content.Intent;

import com.automotive.hhi.mileagetracker.adapters.StationAdapter;

/**
 * Created by Josiah Hadley on 5/14/2016.
 */
public interface UsedStationView {

    void showUsed(StationAdapter stations);

    void returnStation(Intent intent);

    void showRecyclerView();

    void showNoStations();
}
