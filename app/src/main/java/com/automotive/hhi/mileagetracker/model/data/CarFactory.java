package com.automotive.hhi.mileagetracker.model.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.automotive.hhi.mileagetracker.model.database.DataContract;

/**
 * Created by Josiah Hadley on 3/30/2016.
 */
public class CarFactory  {

    public static Car fromCursor(Cursor cursor){
        Car car = new Car();
        car.setAvgMpg(cursor
                .getDouble(cursor
                        .getColumnIndexOrThrow(DataContract.CarTable.AVGMPG)));
        car.setId(cursor
                .getLong(cursor
                        .getColumnIndexOrThrow(DataContract.CarTable._ID)));
        car.setMake(cursor
                .getString(cursor
                        .getColumnIndexOrThrow(DataContract.CarTable.MAKE)));
        car.setModel(cursor
                .getString(cursor
                        .getColumnIndexOrThrow(DataContract.CarTable.MODEL)));
        car.setName(cursor
                .getString(cursor
                        .getColumnIndexOrThrow(DataContract.CarTable.NAME)));
        car.setImage(cursor
                .getString(cursor
                        .getColumnIndexOrThrow(DataContract.CarTable.IMAGE)));
        car.setYear(cursor
                .getInt(cursor
                        .getColumnIndexOrThrow(DataContract.CarTable.YEAR)));
        return car;
    }

    public static ContentValues toContentValues(Car car){
        ContentValues vals = new ContentValues();
        vals.put(DataContract.CarTable.AVGMPG, car.getAvgMpg());
        vals.put(DataContract.CarTable.MAKE, car.getMake());
        vals.put(DataContract.CarTable.MODEL, car.getModel());
        vals.put(DataContract.CarTable.NAME, car.getName());
        vals.put(DataContract.CarTable.IMAGE, car.getImage());
        vals.put(DataContract.CarTable.YEAR, car.getYear());

        return vals;
    }
}
