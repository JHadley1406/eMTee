package com.automotive.hhi.mileagetracker.model.data;

import com.automotive.hhi.mileagetracker.model.retrofit.MyGasFeedService;

import java.util.ArrayList;

/**
 * Created by Josiah Hadley on 4/25/2016.
 */
public class MyGasFeedData {

    public MyGasFeedData(GasFeedStatus status, GeoLocation geoLocation, ArrayList<Station> stations){
        this.status = status;
        this.geoLocation = geoLocation;
        this.stations = stations;
    }

    private GasFeedStatus status;

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }

    private GeoLocation geoLocation;
    private ArrayList<Station> stations;


}
