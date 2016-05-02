package com.automotive.hhi.mileagetracker.model.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.automotive.hhi.mileagetracker.model.database.DataContract;

/**
 * Created by Josiah Hadley on 3/30/2016.
 */
public class FillupFactory {

    public static Fillup fromCursor(Cursor cursor){
        Fillup fillup = new Fillup();


        fillup.setCarId(cursor
                .getLong(cursor
                        .getColumnIndexOrThrow(DataContract.FillupTable.CAR)));
        fillup.setStationId(cursor
                .getLong(cursor
                        .getColumnIndexOrThrow(DataContract.FillupTable.STATION)));
        fillup.setId(cursor
                .getLong(cursor
                        .getColumnIndexOrThrow(DataContract.FillupTable._ID)));
        fillup.setDate(cursor
                .getLong(cursor
                        .getColumnIndexOrThrow(DataContract.FillupTable.DATE)));
        fillup.setFillupMileage(cursor
                .getDouble(cursor
                        .getColumnIndexOrThrow(DataContract.FillupTable.MILEAGE)));
        fillup.setFillupMpg(cursor
                .getDouble(cursor
                        .getColumnIndexOrThrow(DataContract.FillupTable.MPG)));
        fillup.setFuelCost(cursor
                .getDouble(cursor
                        .getColumnIndexOrThrow(DataContract.FillupTable.COST)));
        fillup.setGallons(cursor
                .getDouble(cursor
                        .getColumnIndexOrThrow(DataContract.FillupTable.GALLONS)));
        fillup.setOctane(cursor
                .getInt(cursor
                        .getColumnIndexOrThrow(DataContract.FillupTable.OCTANE)));
        return fillup;
    }

    public static ContentValues toContentValues(Fillup fillup){
        ContentValues vals = new ContentValues();
        vals.put(DataContract.FillupTable.CAR, fillup.getCarId());
        vals.put(DataContract.FillupTable.STATION, fillup.getStationId());
        vals.put(DataContract.FillupTable.COST, fillup.getFuelCost());
        vals.put(DataContract.FillupTable.DATE, fillup.getDate());
        vals.put(DataContract.FillupTable.GALLONS, fillup.getGallons());
        vals.put(DataContract.FillupTable.MILEAGE, fillup.getFillupMileage());
        vals.put(DataContract.FillupTable.MPG, fillup.getFillupMpg());
        vals.put(DataContract.FillupTable.OCTANE, fillup.getOctane());

        return vals;
    }
}
