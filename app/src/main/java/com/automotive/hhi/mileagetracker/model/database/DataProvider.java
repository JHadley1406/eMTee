package com.automotive.hhi.mileagetracker.model.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class DataProvider extends ContentProvider {

    private static DbHelper mOpenHelper;
    private SQLiteDatabase mDb;
    private static final int CARS = 100;
    private static final int CAR_ID = 101;
    private static final int STATIONS = 200;
    private static final int STATION_ID = 201;
    private static final int FILLUPS = 300;
    private static final int FILLUP_ID = 301;
    private static HashMap<String, String> CARS_PROJECTION_MAP;
    private static HashMap<String, String> STATIONS_PROJECTION_MAP;
    private static HashMap<String, String> FILLUPS_PROJECTION_MAP;

    private UriMatcher mUriMatcher = buildUriMatcher();

    private static final SQLiteQueryBuilder mCarQuery = new SQLiteQueryBuilder();
    private static final SQLiteQueryBuilder mStationQuery = new SQLiteQueryBuilder();
    private static final SQLiteQueryBuilder mFillupQuery = new SQLiteQueryBuilder();

    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DataContract.BASE_CONTENT_URI.toString();

        matcher.addURI(authority, DataContract.PATH_CARS, CARS);
        matcher.addURI(authority, DataContract.PATH_CARS + "/#", CAR_ID);
        matcher.addURI(authority, DataContract.PATH_STATIONS, STATIONS);
        matcher.addURI(authority, DataContract.PATH_STATIONS + "/#", STATION_ID);
        matcher.addURI(authority, DataContract.PATH_FILLUPS, FILLUPS);
        matcher.addURI(authority, DataContract.PATH_FILLUPS + "/#", FILLUP_ID);
        return matcher;
    }

    private int matchUri(Uri uri){
        String request = uri.toString();

        if(request.contentEquals(DataContract.CarTable.CONTENT_URI.toString())){
            return CARS;
        } else if(request.contentEquals(DataContract.CarTable.CONTENT_ITEM_TYPE.toString())){
            return CAR_ID;
        } else if(request.contentEquals(DataContract.StationTable.CONTENT_URI.toString())){
            return STATIONS;
        } else if(request.contentEquals(DataContract.StationTable.CONTENT_ITEM_TYPE.toString())){
            return STATION_ID;
        } else if(request.contentEquals(DataContract.FillupTable.CONTENT_URI.toString())){
            return FILLUPS;
        } else if(request.contentEquals(DataContract.FillupTable.CONTENT_ITEM_TYPE.toString())){
            return FILLUP_ID;
        }

        return -1;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DbHelper(getContext());
        mDb = mOpenHelper.getWritableDatabase();
        return (mDb==null)?false:true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri
            , String[] projection
            , String selection
            , String[] selectionArgs
            , String sortOrder) {
        Cursor retCursor;
        mCarQuery.setTables(DataContract.CAR_TABLE);
        mStationQuery.setTables(DataContract.STATION_TABLE);
        mFillupQuery.setTables(DataContract.FILLUP_TABLE);
        switch (matchUri(uri)){
            case CARS:
                mCarQuery.setProjectionMap(CARS_PROJECTION_MAP);
                retCursor = mCarQuery.query(mDb
                        , projection
                        , selection
                        , selectionArgs
                        , null, null
                        , sortOrder);
                break;
            case CAR_ID:
                mCarQuery.appendWhere(DataContract.CarTable._ID
                        + "=" + uri.getPathSegments().get(1));
                retCursor = mCarQuery.query(mDb
                        , projection
                        , selection
                        , selectionArgs
                        , null, null
                        , sortOrder);
                break;
            case STATIONS:
                mStationQuery.setProjectionMap(STATIONS_PROJECTION_MAP);
                retCursor = mStationQuery.query(mDb
                        , projection
                        , selection
                        , selectionArgs
                        , null, null
                        , sortOrder);
                break;
            case STATION_ID:
                mStationQuery.appendWhere(DataContract.StationTable._ID
                        + "=" + uri.getPathSegments().get(1));
                retCursor = mStationQuery.query(mDb
                        , projection
                        , selection
                        , selectionArgs
                        , null, null
                        , sortOrder);
                break;
            case FILLUPS:
                mFillupQuery.setProjectionMap(FILLUPS_PROJECTION_MAP);
                retCursor = mFillupQuery.query(mDb
                        , projection
                        , selection
                        , selectionArgs
                        , null, null
                        , sortOrder);
                break;
            case FILLUP_ID:
                mFillupQuery.appendWhere(DataContract.FillupTable._ID
                        + "=" + uri.getPathSegments().get(1));
                retCursor = mFillupQuery.query(mDb
                        , projection
                        , selection
                        , selectionArgs
                        , null, null
                        , sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int type = mUriMatcher.match(uri);

        switch (type){
            case CARS:
                return DataContract.CarTable.CONTENT_TYPE;
            case CAR_ID:
                return DataContract.CarTable.CONTENT_ITEM_TYPE;
            case STATIONS:
                return DataContract.StationTable.CONTENT_TYPE;
            case STATION_ID:
                return DataContract.StationTable.CONTENT_ITEM_TYPE;
            case FILLUPS:
                return DataContract.FillupTable.CONTENT_TYPE;
            case FILLUP_ID:
                return DataContract.FillupTable.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId;
        switch(matchUri(uri)){
            case CARS:
                rowId = mDb.insert(DataContract.CAR_TABLE, "", values);
                if(rowId > 0){
                    Uri newUri =
                            ContentUris.withAppendedId(DataContract.CarTable.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(newUri, null);
                    return newUri;
                }
                break;
            case STATIONS:
                rowId = mDb.insert(DataContract.STATION_TABLE, "", values);
                if(rowId > 0){
                    Uri newUri =
                            ContentUris.withAppendedId(DataContract.StationTable.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(newUri, null);
                    return newUri;
                }
                break;
            case FILLUPS:
                rowId = mDb.insert(DataContract.FILLUP_TABLE, "", values);
                if(rowId > 0){
                    Uri newUri =
                            ContentUris.withAppendedId(DataContract.FillupTable.CONTENT_URI, rowId);
                    getContext().getContentResolver().notifyChange(newUri, null);
                    return newUri;
                }
                break;
        }
        throw new SQLException("Failed to add a record into: " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        String id;
        switch (matchUri(uri)){
            case CARS:
                count = mDb.delete(DataContract.CAR_TABLE, selection, selectionArgs);
                break;
            case CAR_ID:
                count = mDb.delete(DataContract.CAR_TABLE
                        , DataContract.CarTable._ID
                            + "=" + uri.getPathSegments().get(1)
                            + (!TextUtils.isEmpty(selection)? " AND (" + selection + ")":"")
                        , selectionArgs);
                break;
            case STATIONS:
                count = mDb.delete(DataContract.STATION_TABLE, selection, selectionArgs);
                break;
            case STATION_ID:
                count = mDb.delete(DataContract.STATION_TABLE
                        , DataContract.StationTable._ID
                        + "=" + uri.getPathSegments().get(1)
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "")
                        , selectionArgs);
                break;
            case FILLUPS:
                count = mDb.delete(DataContract.FILLUP_TABLE, selection, selectionArgs);
                break;
            case FILLUP_ID:
                count = mDb.delete(DataContract.FILLUP_TABLE
                        , DataContract.FillupTable._ID
                        + "=" + uri.getPathSegments().get(1)
                        + (!TextUtils.isEmpty(selection)? " AND (" + selection + ")":"")
                        , selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        switch (matchUri(uri)){
            case CARS:
                count = mDb.update(DataContract.CAR_TABLE, values, selection, selectionArgs);
                break;
            case CAR_ID:
                count = mDb.update(DataContract.CAR_TABLE
                        , values
                        , DataContract.CarTable._ID
                            + "=" + uri.getPathSegments().get(1)
                            + (!TextUtils.isEmpty(selection)?" AND (" + selection + ")" : "")
                        , selectionArgs);
                break;
            case STATIONS:
                count = mDb.update(DataContract.STATION_TABLE, values, selection, selectionArgs);
                break;
            case STATION_ID:
                count = mDb.update(DataContract.STATION_TABLE
                        , values
                        , DataContract.StationTable._ID
                        + "=" + uri.getPathSegments().get(1)
                        + (!TextUtils.isEmpty(selection)?" AND (" + selection + ")" : "")
                        , selectionArgs);
                break;
            case FILLUPS:
                count = mDb.update(DataContract.FILLUP_TABLE, values, selection, selectionArgs);
                break;
            case FILLUP_ID:
                count = mDb.update(DataContract.FILLUP_TABLE
                        , values
                        , DataContract.FillupTable._ID
                        + "=" + uri.getPathSegments().get(1)
                        + (!TextUtils.isEmpty(selection)?" AND (" + selection + ")" : "")
                        , selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Uri: " + uri);
        }
        return count;
    }


}
