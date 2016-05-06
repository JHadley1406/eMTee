package com.automotive.hhi.mileagetracker.presenter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.automotive.hhi.mileagetracker.adapters.StationAdapter;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;
import com.automotive.hhi.mileagetracker.model.data.Station;
import com.automotive.hhi.mileagetracker.model.database.DataContract;
import com.automotive.hhi.mileagetracker.view.interfaces.StationListView;

/**
 * Created by Josiah Hadley on 3/31/2016.
 */
public class StationListPresenter implements Presenter<StationListView>
        , ViewHolderOnClickListener<Station>, LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = StationListPresenter.class.getSimpleName();
    private final int LOADER_ID = 32154321;

    private StationListView mStationListView;
    private Context mContext;
    private StationAdapter mStationListAdapter;
    private LoaderManager mLoaderManager;

    public StationListPresenter(Context context
            , LoaderManager loaderManager){
        mContext = context;
        mLoaderManager = loaderManager;
        mStationListAdapter = new StationAdapter(mContext, null, this);
    }

    @Override
    public void attachView(StationListView view) {
        mStationListView = view;
        mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public void detachView() {
        mStationListView = null;
        mContext = null;
    }

    public void loadStations(){
        Cursor stationCursor = mContext.getContentResolver()
                .query(DataContract.StationTable.CONTENT_URI
                        , null, null, null, null);
        if(stationCursor != null && stationCursor.moveToFirst()){
            mStationListView.showStations(new StationAdapter(mContext, stationCursor, this));
        }

    }

    @Override
    public void onClick(Station station) {
        Log.i(LOG_TAG, "Station Clicked On");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext
                , DataContract.StationTable.CONTENT_URI
                , null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mStationListAdapter.changeCursor(data);
        mStationListView.showStations(mStationListAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        restartLoader();
    }

    public void restartLoader(){ mLoaderManager.restartLoader(LOADER_ID, null, this); }
}
