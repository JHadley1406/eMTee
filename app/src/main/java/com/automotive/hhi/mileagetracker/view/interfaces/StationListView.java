package com.automotive.hhi.mileagetracker.view.interfaces;

import com.automotive.hhi.mileagetracker.adapters.StationAdapter;

/**
 * Created by Josiah Hadley on 3/31/2016.
 */
public interface StationListView extends MvpView {

    void showStations(StationAdapter stations);

}
