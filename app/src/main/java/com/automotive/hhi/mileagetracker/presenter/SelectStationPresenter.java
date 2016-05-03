package com.automotive.hhi.mileagetracker.presenter;


import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.adapters.LocBasedStationAdapter;
import com.automotive.hhi.mileagetracker.adapters.StationAdapter;
import com.automotive.hhi.mileagetracker.model.callbacks.LatLonCallback;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;
import com.automotive.hhi.mileagetracker.model.data.Station;
import com.automotive.hhi.mileagetracker.model.database.DataContract;
import com.automotive.hhi.mileagetracker.model.managers.GasStationFinderService;
import com.automotive.hhi.mileagetracker.view.CarDetailActivity;
import com.automotive.hhi.mileagetracker.view.interfaces.SelectStationView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class SelectStationPresenter implements Presenter<SelectStationView>
        , ViewHolderOnClickListener<Station>
        , LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = SelectStationPresenter.class.getSimpleName();


    private SelectStationView mSelectStationView;
    private LocBasedStationAdapter mNearbyAdapter;
    private Context mContext;
    private List<Station> mStations;
    private StationAdapter mStationAdapter;
    private LoaderManager mLoaderManager;
    private LocationManager mLocationManager;
    private LatLng mLatLng;
    private String mFuelType;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(KeyContract.NEW_LOCATION)) {
                mLatLng = intent.getParcelableExtra(KeyContract.LATLNG);
                Intent returnIntent = new Intent();
                returnIntent.setAction(KeyContract.LOCATION_OK);
                mContext.sendBroadcast(returnIntent);
                getNearbyStations();
            } else if(intent.getAction().equals(KeyContract.STATION_LIST)){
                mStations = intent.getParcelableArrayListExtra(KeyContract.STATION_LIST);
                updateNearbyStations();
            }
        }
    };





    public SelectStationPresenter(Context context, LoaderManager loaderManager){
        mContext = context;
        mLoaderManager = loaderManager;
        mStations = new ArrayList<>();
        mStationAdapter = new StationAdapter(mContext, null, this);
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mFuelType = "reg";
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(KeyContract.NEW_LOCATION);
        intentFilter.addAction(KeyContract.STATION_LIST);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void attachView(SelectStationView view) {
        mSelectStationView = view;
        mNearbyAdapter = new LocBasedStationAdapter(mStations, this);
        loadNearbyStations();
        mLoaderManager.initLoader(KeyContract.USED_STATION_LOADER_ID, null, this);
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            mSelectStationView.launchGPSAlert();
        }
    }

    @Override
    public void detachView() {
        mSelectStationView = null;
        mContext = null;
    }

    public String getFuelType(){
        return mFuelType;
    }

    public Intent returnToCarDetailIntent(){
        Intent backIntent = new Intent(mContext, CarDetailActivity.class);
        return backIntent;
    }

    public Intent findStationsFromAddress(String address){
        Intent addressSearchIntent = new Intent(mContext, GasStationFinderService.class);
        addressSearchIntent.putExtra(KeyContract.SEARCH_ADDRESS, address);
        addressSearchIntent.putExtra(KeyContract.DISTANCE, 10);
        addressSearchIntent.putExtra(KeyContract.FUELTYPE, getFuelType());
        return addressSearchIntent;
    }

    public boolean isOnline()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void loadNearbyStations(){
        mSelectStationView.showNearby(mNearbyAdapter);
    }

    private void updateNearbyStations(){
        mNearbyAdapter.updateStations(mStations);
    }

    //TODO: Remove after testing is done
    /*private void insertTestData(){
        Station station = new Station();
        station.setLat(9999);
        station.setLon(9999);
        station.setName("Test Station");
        station.setAddress("123 Test St. San Jose CA, 95113");
        mContext.getContentResolver()
                .insert(DataContract.StationTable.CONTENT_URI
                        , StationFactory.toContentValues(station));
    }*/

    @Override
    public void onClick(Station station) {
        Intent returnStationIntent = new Intent();
        returnStationIntent.putExtra(KeyContract.STATION, station);
        mSelectStationView.returnStation(returnStationIntent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext
                , DataContract.StationTable.CONTENT_URI
                , null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        /*if(data == null || !data.moveToFirst()){
            insertTestData();
        }*/
        mStationAdapter.changeCursor(data);
        mSelectStationView.showUsed(mStationAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLoaderManager.restartLoader(KeyContract.USED_STATION_LOADER_ID, null, this);
    }

    private void getNearbyStations(){
        Intent serviceIntent = new Intent(mContext, GasStationFinderService.class);
        serviceIntent.putExtra(KeyContract.LATLNG, mLatLng);
        serviceIntent.putExtra(KeyContract.FUELTYPE, getFuelType());
        mSelectStationView.launchService(serviceIntent);
    }
}
