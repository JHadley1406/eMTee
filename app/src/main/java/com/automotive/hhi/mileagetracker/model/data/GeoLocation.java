package com.automotive.hhi.mileagetracker.model.data;

/**
 * Created by Josiah Hadley on 4/25/2016.
 */
public class GeoLocation {

    private String country_short;
    private String lat;
    private String lng;
    private String country_long;
    private String region_short;
    private String region_long;
    private String city_long;
    private String address;

    public GeoLocation(String country_short
            , String lat
            , String lng
            , String country_long
            , String region_short
            , String region_long
            , String city_long
            , String address){
        this.country_short = country_short;
        this.lat = lat;
        this.lng = lng;
        this.country_long = country_long;
        this.region_short = region_short;
        this.region_long = region_long;
        this.city_long = city_long;
        this.address = address;
    }
}
