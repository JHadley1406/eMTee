package com.automotive.hhi.mileagetracker.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.adapters.LocBasedStationAdapter;
import com.automotive.hhi.mileagetracker.adapters.StationAdapter;
import com.automotive.hhi.mileagetracker.model.managers.LocationService;
import com.automotive.hhi.mileagetracker.presenter.SelectStationPresenter;
import com.automotive.hhi.mileagetracker.view.interfaces.SelectStationView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectStationActivity extends AppCompatActivity implements SelectStationView {

    private final String LOG_TAG = SelectStationActivity.class.getSimpleName();

    private final int PERMISSION_REQUEST_CODE = 100;

    @Bind(R.id.select_station_address_input)
    public EditText mAddressSearch;
    @Bind(R.id.select_station_address_find_button)
    public Button mAddressSearchButton;
    @Bind(R.id.select_station_nearby_label)
    public TextView mNearbyLabel;
    @Bind(R.id.select_station_nearby_rv)
    public RecyclerView mNearbyStationRV;
    @Bind(R.id.select_station_used_rv)
    public RecyclerView mUsedStationRV;
    @Bind(R.id.select_station_toolbar)
    public Toolbar mToolbar;
    private SelectStationPresenter mSelectStationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mUsedStationRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mNearbyStationRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        preparePresenter();
        checkPermission();
    }

    @OnClick(R.id.select_station_address_find_button)
    public void addressSearch(){
        getContext()
                .startService(mSelectStationPresenter
                        .findStationsFromAddress(mAddressSearch.getText().toString()));
        mNearbyLabel.setText(R.string.select_station_search_station_text);
    }

    @Override
    public void showNearby(LocBasedStationAdapter stations) {
        mNearbyStationRV.setAdapter(stations);
    }

    @Override
    public void showUsed(StationAdapter stations) {
        mUsedStationRV.setAdapter(stations);
        stations.notifyDataSetChanged();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_station_select, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:{
                NavUtils.navigateUpTo(this, mSelectStationPresenter.returnToCarDetailIntent());
                return true;
            }
            case 1:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext()
                , android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this
                    , new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}
                    , PERMISSION_REQUEST_CODE);
        } else{
            Log.i(LOG_TAG, "Launching location service from checkPermission");
            launchService(new Intent(getContext(), LocationService.class));
        }
    }

    private void preparePresenter(){
        mSelectStationPresenter = new SelectStationPresenter(getApplicationContext(), getLoaderManager());
        mSelectStationPresenter.attachView(this);
    }

    @Override
    public void returnStation(Intent returnStationIntent){
        setResult(RESULT_OK, returnStationIntent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.i(LOG_TAG, "Launching location service from checkPermission");
            launchService(new Intent(getContext(), LocationService.class));

        }
    }

    @Override
    public void launchGPSAlert(){
        AlertDialog.Builder gpsAlertDialog = new AlertDialog.Builder(this);
        gpsAlertDialog.setMessage(R.string.gps_alert_message)
                .setCancelable(false)
                .setPositiveButton(R.string.gps_alert_settings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.gps_alert_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();
    }


    @Override
    public void launchService(Intent intent){
        getContext().startService(intent);
    }
}
