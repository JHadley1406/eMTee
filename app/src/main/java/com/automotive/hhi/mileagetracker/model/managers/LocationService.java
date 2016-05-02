package com.automotive.hhi.mileagetracker.model.managers;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.model.database.DataContract;
import com.automotive.hhi.mileagetracker.model.retrofit.GooglePlacesService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Josiah Hadley on 4/24/2016.
 */
public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener
        , LocationListener {

    private final String LOG_TAG = LocationService.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LatLng mLatLng;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(KeyContract.LOCATION_OK)){
                stopLocationUpdates();
                stopSelf();
            }
        }
    };


    @Override
    public void onCreate(){
        super.onCreate();
        startLocationUpdated();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(KeyContract.LOCATION_OK);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_NOT_STICKY;
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch(SecurityException e){
            Log.i(LOG_TAG, "Security Exception Caught: " + e.toString());
            //TODO: report back to app that we don't have permission
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "Locations Connection Suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Locations Connection Failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location.getAccuracy() != 0){
            mLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            Intent outgoingIntent = new Intent();
            outgoingIntent.setAction(KeyContract.NEW_LOCATION);
            outgoingIntent.putExtra(KeyContract.LATLNG, mLatLng);
            sendBroadcast(outgoingIntent);
        }
    }

    @Override
    public void onDestroy(){
        stopLocationUpdates();
        mGoogleApiClient = null;
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startLocationUpdated(){
        if(mGoogleApiClient == null) {
            buildGoogleApiClient();
        }
        if(mLocationRequest == null){
            buildLocationRequest();
        }
        mGoogleApiClient.connect();
    }

    private void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void buildLocationRequest(){
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(8000)
                .setFastestInterval(2000)
                .setSmallestDisplacement(10);
    }

    private void stopLocationUpdates(){
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
}
