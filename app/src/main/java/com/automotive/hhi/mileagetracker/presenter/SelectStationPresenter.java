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
import com.automotive.hhi.mileagetracker.view.AddFillupActivity;
import com.automotive.hhi.mileagetracker.view.CarDetailActivity;
import com.automotive.hhi.mileagetracker.view.interfaces.SelectStationView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class SelectStationPresenter implements Presenter<SelectStationView> {

    private final String LOG_TAG = SelectStationPresenter.class.getSimpleName();

    private SelectStationView mSelectStationView;
    private Context mContext;
    private LocationManager mLocationManager;

    public SelectStationPresenter(Context context){
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void attachView(SelectStationView view) {
        mSelectStationView = view;
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            mSelectStationView.launchGPSAlert();
        }
    }

    @Override
    public void detachView() {
        mSelectStationView = null;
        mContext = null;
    }

    public Intent returnToAddFillupIntent(){
        Intent backIntent = new Intent(mContext, AddFillupActivity.class);
        return backIntent;
    }

}
