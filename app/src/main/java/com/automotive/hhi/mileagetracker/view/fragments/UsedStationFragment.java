package com.automotive.hhi.mileagetracker.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.adapters.StationAdapter;
import com.automotive.hhi.mileagetracker.model.callbacks.StationFragmentListener;
import com.automotive.hhi.mileagetracker.presenter.UsedStationPresenter;
import com.automotive.hhi.mileagetracker.view.interfaces.UsedStationView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Josiah Hadley on 5/10/2016.
 */
public class UsedStationFragment extends Fragment implements UsedStationView {

    private final String LOG_TAG = UsedStationFragment.class.getSimpleName();

    @Bind(R.id.select_station_used_rv)
    public RecyclerView mUsedStationRV;
    @Bind(R.id.select_station_no_station_container)
    public RelativeLayout mNoStationContainer;
    private StationFragmentListener mStationFragmentListener;
    private UsedStationPresenter mUsedStationPresenter;
    private Context mContext;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_used_stations, container, false);
        ButterKnife.bind(this, view);
        mUsedStationRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        try{
            mStationFragmentListener = (StationFragmentListener) getActivity();
        } catch(ClassCastException e){
            Log.e(LOG_TAG, getActivity().toString() + " must implement StationFragmentListener");
        }
        mUsedStationPresenter = new UsedStationPresenter(mContext, getActivity().getLoaderManager());
        mUsedStationPresenter.attachView(this);
    }

    @Override
    public void showUsed(StationAdapter stations) {
        mUsedStationRV.setAdapter(stations);
        stations.notifyDataSetChanged();
    }

    @Override
    public void returnStation(Intent returnStationIntent){
       mStationFragmentListener.stationSelected(returnStationIntent);
    }

    @Override
    public void showRecyclerView() {
        mUsedStationRV.setVisibility(View.VISIBLE);
        mNoStationContainer.setVisibility(View.GONE);
    }

    @Override
    public void showNoStations() {
        mNoStationContainer.setVisibility(View.VISIBLE);
        mUsedStationRV.setVisibility(View.GONE);
    }

}
