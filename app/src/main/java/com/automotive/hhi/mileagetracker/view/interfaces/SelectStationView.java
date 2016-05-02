package com.automotive.hhi.mileagetracker.view.interfaces;

import android.content.Intent;

import com.automotive.hhi.mileagetracker.adapters.LocBasedStationAdapter;
import com.automotive.hhi.mileagetracker.adapters.StationAdapter;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public interface SelectStationView extends MvpView {

    void showNearby(LocBasedStationAdapter stations);

    void showUsed(StationAdapter stations);

    void returnStation(Intent intent);

    void launchGPSAlert();

    void launchService(Intent intent);

}
