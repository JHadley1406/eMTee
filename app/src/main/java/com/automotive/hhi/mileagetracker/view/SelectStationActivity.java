package com.automotive.hhi.mileagetracker.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import android.widget.Toast;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.adapters.LocBasedStationAdapter;
import com.automotive.hhi.mileagetracker.adapters.StationAdapter;
import com.automotive.hhi.mileagetracker.adapters.StationViewPagerAdapter;
import com.automotive.hhi.mileagetracker.model.callbacks.StationFragmentListener;
import com.automotive.hhi.mileagetracker.model.managers.LocationService;
import com.automotive.hhi.mileagetracker.presenter.SelectStationPresenter;
import com.automotive.hhi.mileagetracker.view.fragments.NearbyStationFragment;
import com.automotive.hhi.mileagetracker.view.fragments.UsedStationFragment;
import com.automotive.hhi.mileagetracker.view.interfaces.SelectStationView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectStationActivity extends AppCompatActivity implements SelectStationView, StationFragmentListener {

    private final String LOG_TAG = SelectStationActivity.class.getSimpleName();

    @Bind(R.id.select_station_toolbar)
    public Toolbar mToolbar;
    @Bind(R.id.select_station_tabs)
    public TabLayout mTabLayout;
    @Bind(R.id.select_station_view_pager)
    public ViewPager mViewPager;
    private SelectStationPresenter mSelectStationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_station);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setViewPager();
        preparePresenter();

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
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:{
                setResult(RESULT_CANCELED);
                finish();
                return true;
            }
            case R.id.station_select_menu_about: {
                startActivity(new Intent(getContext(), AbouteMTeeActivity.class));
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setViewPager(){
        StationViewPagerAdapter adapter = new StationViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NearbyStationFragment(), "Nearby Stations");
        adapter.addFragment(new UsedStationFragment(), "Visited Stations");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void preparePresenter(){
        mSelectStationPresenter = new SelectStationPresenter(getApplicationContext());
        mSelectStationPresenter.attachView(this);
    }

    @Override
    public void stationSelected(Intent stationIntent) {
        setResult(RESULT_OK, stationIntent);
        finish();
    }
}
