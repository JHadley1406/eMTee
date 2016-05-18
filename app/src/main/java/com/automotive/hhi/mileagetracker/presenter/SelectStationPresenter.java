package com.automotive.hhi.mileagetracker.presenter;


import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.automotive.hhi.mileagetracker.view.AddFillupActivity;
import com.automotive.hhi.mileagetracker.view.interfaces.SelectStationView;


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
