package com.automotive.hhi.mileagetracker.model.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class Car implements Parcelable {

    private long id;
    private String name;
    private String make;
    private String model;
    private String image;
    private int year;
    private double avgMpg;

    public Car(){}

    public Car(Parcel in){
        setId(in.readLong());
        setName(in.readString());
        setMake(in.readString());
        setModel(in.readString());
        setImage(in.readString());
        setYear(in.readInt());
        setAvgMpg(in.readDouble());
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getAvgMpg() {
        return avgMpg;
    }

    public void setAvgMpg(double avgMpg) {
        this.avgMpg = avgMpg;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getName());
        dest.writeString(getMake());
        dest.writeString(getModel());
        dest.writeString(getImage());
        dest.writeInt(getYear());
        dest.writeDouble(getAvgMpg());
    }

    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>(){
        public Car createFromParcel(Parcel in){return new Car(in);}

        public Car[] newArray(int size){return new Car[size];}
    };
}
