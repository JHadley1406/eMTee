package com.automotive.hhi.mileagetracker.view.interfaces;

import android.content.Intent;

import com.automotive.hhi.mileagetracker.adapters.LocBasedStationAdapter;

/**
 * Created by Josiah Hadley on 5/11/2016.
 */
public interface NearbyStationView {

    void showNearby(LocBasedStationAdapter stations);

    void returnStation(Intent intent);

    void launchService(Intent intent);

    void showRecyclerView();

}
