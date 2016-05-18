package com.automotive.hhi.mileagetracker.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.adapters.LocBasedStationAdapter;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;
import com.automotive.hhi.mileagetracker.model.data.Station;
import com.automotive.hhi.mileagetracker.model.managers.GasStationFinderService;
import com.automotive.hhi.mileagetracker.view.interfaces.NearbyStationView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josiah Hadley on 5/11/2016.
 */
public class NearbyStationPresenter implements Presenter<NearbyStationView>
        , ViewHolderOnClickListener<Station> {

    private final String LOG_TAG = NearbyStationPresenter.class.getSimpleName();

    private NearbyStationView mNearbyStationView;
    private Context mContext;
    private LocBasedStationAdapter mNearbyAdapter;
    private LocationManager mLocationManager;
    private String mFuelType;
    private LatLng mLatLng;
    private List<Station> mStations;


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

    public NearbyStationPresenter(Context context){
        mContext = context;
        mStations = new ArrayList<>();
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        mFuelType = "reg";

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(KeyContract.NEW_LOCATION);
        intentFilter.addAction(KeyContract.STATION_LIST);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onClick(Station station) {
        Intent returnStationIntent = new Intent();
        returnStationIntent.putExtra(KeyContract.STATION, station);
        mNearbyStationView.returnStation(returnStationIntent);
    }

    @Override
    public void attachView(NearbyStationView view) {
        mNearbyStationView = view;
        mNearbyAdapter = new LocBasedStationAdapter(mStations, this);
        checkConnectivity();
        loadNearbyStations();
    }

    @Override
    public void detachView() {
        mContext.unregisterReceiver(mBroadcastReceiver);
        mNearbyStationView = null;
        mContext = null;
    }

    public String getFuelType(){
        return mFuelType;
    }

    public Intent findStationsFromAddress(String address){
        Intent addressSearchIntent = new Intent(mContext, GasStationFinderService.class);
        addressSearchIntent.putExtra(KeyContract.SEARCH_ADDRESS, address);
        addressSearchIntent.putExtra(KeyContract.DISTANCE, 10);
        addressSearchIntent.putExtra(KeyContract.FUELTYPE, getFuelType());
        return addressSearchIntent;
    }

    private void getNearbyStations(){
        Intent serviceIntent = new Intent(mContext, GasStationFinderService.class);
        serviceIntent.putExtra(KeyContract.LATLNG, mLatLng);
        serviceIntent.putExtra(KeyContract.FUELTYPE, getFuelType());
        mNearbyStationView.launchService(serviceIntent);
    }

    public void loadNearbyStations(){
        mNearbyStationView.showNearby(mNearbyAdapter);
    }

    public boolean isOnline()
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void updateNearbyStations(){
        mNearbyStationView.showRecyclerView();
        mNearbyAdapter.updateStations(mStations);
    }

    public boolean checkConnectivity(){
        Log.i(LOG_TAG, "in check Connectivity");
        if(!isOnline()
                || ContextCompat.checkSelfPermission(mContext
                    , android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                ||!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return false;
        }
        return true;
    }

}
