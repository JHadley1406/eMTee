package com.automotive.hhi.mileagetracker.model.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class DataContract {

    public static final String CONTENT_AUTHORITY = "com.automotive.hhi.mileagetracker";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CARS = "cars";
    public static final String PATH_FILLUPS = "fillups";
    public static final String PATH_STATIONS = "stations";

    public static final String CAR_TABLE = "car_table";
    public static final String FILLUP_TABLE = "fillup_table";
    public static final String STATION_TABLE = "station_table";


    public static final class CarTable implements BaseColumns{
        public static final String NAME = "name";
        public static final String MAKE = "make";
        public static final String MODEL = "model";
        public static final String IMAGE = "image";
        public static final String YEAR = "year";
        public static final String AVGMPG = "avgmpg";

        public static final String CREATE_TABLE = "CREATE TABLE " + CAR_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT NOT NULL, "
                + MAKE + " TEXT NULL, "
                + MODEL + " TEXT NULL, "
                + IMAGE + " TEXT NULL, "
                + YEAR + " INTEGER NULL, "
                + AVGMPG + " REAL NULL);";

        public static Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CARS;

        public static Uri buildCarWithId(){
            return CONTENT_URI.buildUpon().appendPath(_ID).build();
        }

    }

    public static final class FillupTable implements BaseColumns{
        public static final String CAR = "car";
        public static final String MILEAGE = "mileage";
        public static final String MPG = "mpg";
        public static final String STATION = "station";
        public static final String GALLONS = "gallons";
        public static final String OCTANE = "octane";
        public static final String COST = "cost";
        public static final String DATE = "date";

        public static final String CREATE_TABLE = "CREATE TABLE " + FILLUP_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CAR + " INTEGER NOT NULL, "
                + MILEAGE + " REAL NOT NULL, "
                + MPG + " REAL NOT NULL, "
                + STATION + " INTEGER NULL, "
                + GALLONS + " REAL NOT NULL, "
                + OCTANE + " INTEGER NOT NULL, "
                + COST + " REAL NOT NULL, "
                + DATE + " INTEGER NOT NULL, "
                + " FOREIGN KEY (" + CAR + ") REFERENCES "
                + CAR_TABLE + " (" + CarTable._ID + "), "
                + " FOREIGN KEY (" + STATION + ") REFERENCES "
                + STATION_TABLE + " (" + StationTable._ID + "));";

        public static Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILLUPS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILLUPS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILLUPS;

        public static Uri buildFillupWithId(){
            return CONTENT_URI.buildUpon().appendPath(_ID).build();
        }

        public static Uri buildFillupWithCar(){
            return CONTENT_URI.buildUpon().appendPath(CAR).build();
        }

        public static Uri buildFillupWithStation(){
            return CONTENT_URI.buildUpon().appendPath(STATION).build();
        }
    }

    public static final class StationTable implements BaseColumns{
        public static final String NAME = "name";
        public static final String ADDRESS = "address";
        public static final String LAT = "lat";
        public static final String LON = "lon";

        public static final String CREATE_TABLE = "CREATE TABLE " + STATION_TABLE + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT NOT NULL, "
                + ADDRESS + " TEXT NOT NULL, "
                + LAT + " REAL NOT NULL, "
                + LON + " REAL NOT NULL);";


        public static Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STATIONS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STATIONS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STATIONS;

        public static Uri buildStationWithId(){
            return CONTENT_URI.buildUpon().appendPath(_ID).build();
        }

    }



}
