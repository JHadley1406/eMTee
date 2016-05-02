package com.automotive.hhi.mileagetracker.model.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class Fillup implements Parcelable {

    private long id;
    private long carId;
    private long stationId;
    private double fillupMileage;
    private double fillupMpg;
    private double gallons;
    private double fuelCost;
    private int octane;
    private long date;

    public Fillup(){}

    public Fillup(Parcel in){
        setId(in.readLong());
        setCarId(in.readLong());
        setStationId(in.readLong());
        setFillupMileage(in.readDouble());
        setFillupMpg(in.readDouble());
        setGallons(in.readDouble());
        setFuelCost(in.readDouble());
        setOctane(in.readInt());
        setDate(in.readLong());
    }

    public double getTotalCost() {
        return getFuelCost()*getGallons();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCarId() {
        return carId;
    }

    public void setCarId(long car) {
        this.carId = car;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long station) {
        this.stationId = station;
    }

    public double getFillupMileage() {
        return fillupMileage;
    }

    public void setFillupMileage(double fillupMileage) {
        this.fillupMileage = fillupMileage;
    }

    public double getFillupMpg() {
        return fillupMpg;
    }

    public void setFillupMpg(double fillupMpg) {
        this.fillupMpg = fillupMpg;
    }

    public double getGallons() {
        return gallons;
    }

    public void setGallons(double gallons) {
        this.gallons = gallons;
    }

    public double getFuelCost() {
        return fuelCost;
    }

    public void setFuelCost(double fuelCost) {
        this.fuelCost = fuelCost;
    }

    public int getOctane() {
        return octane;
    }

    public void setOctane(int octane) {
        this.octane = octane;
    }

    public long getDate() {
        return date;
    }

    public String getReadableDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(new Date(getDate()));
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(getCarId());
        dest.writeLong(getStationId());
        dest.writeDouble(getFillupMileage());
        dest.writeDouble(getFillupMpg());
        dest.writeDouble(getGallons());
        dest.writeDouble(getFuelCost());
        dest.writeInt(getOctane());
        dest.writeLong(getDate());
    }

    public static final Parcelable.Creator<Fillup> CREATOR = new Parcelable.Creator<Fillup>(){
        public Fillup createFromParcel(Parcel in){ return new Fillup(in); }

        public Fillup[] newArray(int size){ return new Fillup[size]; }
    };
}
