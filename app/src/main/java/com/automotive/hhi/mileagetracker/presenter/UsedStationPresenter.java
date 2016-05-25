package com.automotive.hhi.mileagetracker.presenter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.adapters.StationAdapter;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;
import com.automotive.hhi.mileagetracker.model.data.Station;
import com.automotive.hhi.mileagetracker.model.database.DataContract;
import com.automotive.hhi.mileagetracker.view.interfaces.UsedStationView;

/**
 * Created by Josiah Hadley on 5/14/2016.
 */
public class UsedStationPresenter implements Presenter<UsedStationView>
        , LoaderManager.LoaderCallbacks<Cursor>
        , ViewHolderOnClickListener<Station> {

    private Context mContext;
    private LoaderManager mLoaderManager;
    private StationAdapter mStationAdapter;
    private UsedStationView mUsedStationView;


    public UsedStationPresenter(Context context, LoaderManager loaderManager){
        mContext = context;
        mLoaderManager = loaderManager;
        mStationAdapter = new StationAdapter(mContext, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext
                , DataContract.StationTable.CONTENT_URI
                , null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.getCount() > 0) {
            mUsedStationView.showRecyclerView();
            mStationAdapter.changeCursor(data);
            mUsedStationView.showUsed(mStationAdapter);
        } else{
            mUsedStationView.showNoStations();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLoaderManager.restartLoader(KeyContract.USED_STATION_LOADER_ID, null, this);
    }

    @Override
    public void attachView(UsedStationView view) {
        mUsedStationView = view;
        mLoaderManager.initLoader(KeyContract.USED_STATION_LOADER_ID, null, this);
    }

    @Override
    public void detachView() {
        mUsedStationView = null;
        mContext = null;
    }

    @Override
    public void onClick(Station station) {
        Intent returnStationIntent = new Intent();
        returnStationIntent.putExtra(KeyContract.STATION, station);
        mUsedStationView.returnStation(returnStationIntent);
    }
}
