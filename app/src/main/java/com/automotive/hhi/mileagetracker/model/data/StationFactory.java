package com.automotive.hhi.mileagetracker.model.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.automotive.hhi.mileagetracker.model.database.DataContract;

/**
 * Created by Josiah Hadley on 3/30/2016.
 */
public class StationFactory {

    public static Station fromCursor(Cursor cursor){
        Station station = new Station();

        station.setId(cursor
                .getLong(cursor
                        .getColumnIndexOrThrow(DataContract.StationTable._ID)));
        station.setAddress(cursor
                .getString(cursor
                        .getColumnIndexOrThrow(DataContract.StationTable.ADDRESS)));
        station.setName(cursor
                .getString(cursor
                        .getColumnIndexOrThrow(DataContract.StationTable.NAME)));
        station.setLat(cursor
                .getDouble(cursor
                        .getColumnIndexOrThrow(DataContract.StationTable.LAT)));
        station.setLon(cursor
                .getDouble(cursor
                        .getColumnIndexOrThrow(DataContract.StationTable.LON)));

        return station;
    }

    public static ContentValues toContentValues(Station station){
        ContentValues vals = new ContentValues();
        vals.put(DataContract.StationTable.ADDRESS, station.getAddress());
        vals.put(DataContract.StationTable.NAME, station.getName());
        vals.put(DataContract.StationTable.LAT, station.getLat());
        vals.put(DataContract.StationTable.LON, station.getLon());
        return vals;
    }
}
