package com.automotive.hhi.mileagetracker.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class Station implements Parcelable{

    private long id;
    @SerializedName("station")
    private String name;
    private String address;
    private String distance;
    private double lat;
    @SerializedName("lng")
    private double lon;

    public Station(){}

    public Station(Parcel in){
        setId(in.readLong());
        setName(in.readString());
        setAddress(in.readString());
        setDistance(in.readString());
        setLat(in.readDouble());
        setLon(in.readDouble());
    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getName());
        dest.writeString(getAddress());
        dest.writeString(getDistance());
        dest.writeDouble(getLat());
        dest.writeDouble(getLon());
    }

    public static final Parcelable.Creator<Station> CREATOR = new Parcelable.Creator<Station>(){
        public Station createFromParcel(Parcel in){ return new Station(in);}

        public Station[] newArray(int size){ return new Station[size]; }
    };
}
