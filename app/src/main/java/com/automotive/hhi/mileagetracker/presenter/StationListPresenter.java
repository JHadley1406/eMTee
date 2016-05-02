package com.automotive.hhi.mileagetracker.presenter;

import android.content.Context;
import android.database.Cursor;
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
        , ViewHolderOnClickListener<Station> {

    private final String LOG_TAG = StationListPresenter.class.getSimpleName();

    private StationListView mStationListView;
    private Context mContext;

    @Override
    public void attachView(StationListView view) {
        mStationListView = view;
        mContext = mStationListView.getContext();
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
}
