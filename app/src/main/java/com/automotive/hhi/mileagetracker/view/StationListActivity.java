package com.automotive.hhi.mileagetracker.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.adapters.StationAdapter;
import com.automotive.hhi.mileagetracker.presenter.StationListPresenter;
import com.automotive.hhi.mileagetracker.view.interfaces.StationListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StationListActivity extends AppCompatActivity implements StationListView {

    @Bind(R.id.station_list_ad_view)
    public AdView mAdView;
    @Bind(R.id.station_list_rv)
    public RecyclerView mStationRecyclerView;
    @Bind(R.id.station_list_toolbar)
    public Toolbar mToolbar;
    private StationListPresenter mStationListPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_list);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mStationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        preparePresenter();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public void showStations(StationAdapter stations) {
        mStationRecyclerView.setAdapter(stations);
        //stations.notifyDataSetChanged();
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_station_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 1:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void preparePresenter(){
        mStationListPresenter = new StationListPresenter(getApplicationContext(), getLoaderManager());
        mStationListPresenter.attachView(this);
        mStationListPresenter.loadStations();

    }
}
