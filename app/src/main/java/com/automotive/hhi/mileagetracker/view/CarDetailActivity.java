package com.automotive.hhi.mileagetracker.view;

import android.app.AlertDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.adapters.FillupAdapter;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.presenter.CarDetailPresenter;
import com.automotive.hhi.mileagetracker.view.interfaces.CarDetailView;
import com.db.chart.view.LineChartView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.security.Key;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarDetailActivity extends AppCompatActivity implements CarDetailView {

    private final String LOG_TAG = CarDetailActivity.class.getSimpleName();

    @Bind(R.id.car_detail_ad_view)
    public AdView mAdView;
    @Bind(R.id.car_detail_chart)
    public LineChartView mFuelChart;
    @Bind(R.id.car_detail_graph_title)
    public TextView mGraphTitle;
    @Bind(R.id.car_detail_name)
    public TextView mCarName;
    @Bind(R.id.car_detail_make)
    public TextView mCarMake;
    @Bind(R.id.car_detail_model)
    public TextView mCarModel;
    @Bind(R.id.car_detail_year)
    public TextView mCarYear;
    @Bind(R.id.car_detail_avg_mpg)
    public TextView mAverageMpg;
    @Bind(R.id.car_detail_image)
    public ImageView mCarImage;
    @Bind(R.id.car_detail_fillups_rv)
    public RecyclerView mFillupRecyclerView;
    @Bind(R.id.car_detail_no_fillups_container)
    public RelativeLayout mNoFillupsContainer;
    @Bind(R.id.car_detail_delete_car)
    public Button mDeleteCar;
    @Bind(R.id.car_detail_edit_car)
    public Button mEditCar;
    @Bind(R.id.car_detail_add_fillup)
    public Button mAddFillup;
    @Bind(R.id.car_detail_toolbar)
    public Toolbar mToolbar;
    private CarDetailPresenter mCarDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        ButterKnife.bind(this);
        mToolbar.setTitle(getContext().getResources().getString(R.string.title_activity_car_detail));
        setSupportActionBar(mToolbar);
        MobileAds.initialize(getContext(), getContext().getResources().getString(R.string.admob_app_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mFillupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart(){
        super.onStart();
        if(mCarDetailPresenter == null){
            preparePresenter();
        }
        prepareView();
    }

    @OnClick(R.id.car_detail_add_fillup)
    public void addFillup(){
        mCarDetailPresenter.launchAddFillup(getContext());
    }

    @OnClick(R.id.car_detail_edit_car)
    public void editCar(){
        mCarDetailPresenter.launchEditCar(getContext());
    }

    @OnClick(R.id.car_detail_delete_car)
    public void deleteCar(){
        launchCarDeleteAlert();
    }

    @Override
    public void launchActivity(Intent intent, int code){
        startActivityForResult(intent, code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case KeyContract.EDIT_CAR_CODE:{
                if(resultCode == RESULT_OK){
                    mCarDetailPresenter.updateCar((Car)data.getParcelableExtra(KeyContract.CAR));
                }
                break;
            }
            case KeyContract.CREATE_CAR_CODE:{
                mCarDetailPresenter.checkForCars();
                break;
            }
            case KeyContract.CREATE_FILLUP_CODE:{
                if(resultCode == RESULT_OK){
                    mCarDetailPresenter.updateCar((Car) data.getParcelableExtra(KeyContract.CAR));
                    mCarDetailPresenter.initChart(mFuelChart);
                }
                break;
            }
            case KeyContract.EDIT_FILLUP_CODE:{
                if(resultCode == RESULT_OK){
                    mCarDetailPresenter.updateCar((Car) data.getParcelableExtra(KeyContract.CAR));
                    mCarDetailPresenter.notifyChartDataChanged();
                }
                break;
            }
            default:{
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_car_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.car_detail_menu_manage_cars:{
                launchCarList();
                return true;
            }
            case R.id.car_detail_menu_about:{
                startActivity(new Intent(getContext(), AbouteMTeeActivity.class));
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void launchCarList(){
        startActivity(new Intent(getContext(), CarListActivity.class));
        finish();
    }

    @Override
    public void showFillups(FillupAdapter fillups) {
        mFillupRecyclerView.setVisibility(View.VISIBLE);
        mNoFillupsContainer.setVisibility(View.GONE);
        mFillupRecyclerView.setAdapter(fillups);
        if(fillups.getItemCount() >= 3){
            mGraphTitle.setVisibility(View.GONE);
        }
        fillups.notifyDataSetChanged();
    }

    @Override
    public void showNoFillups(){
        mFillupRecyclerView.setVisibility(View.GONE);
        mNoFillupsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCar(Car car) {
        mAverageMpg.setText(String.format("%.1f", car.getAvgMpg()));
        mCarName.setText(car.getName());
        mCarMake.setText(car.getMake());
        mCarModel.setText(car.getModel());
        mCarYear.setText(String.format("%d", car.getYear()));
        if(car.getImage() != null) {
            Picasso.with(getContext()).load(car.getImage()).fit().into(mCarImage);
        }
    }



    @Override
    public Context getContext() {
        return getApplicationContext();
    }



    private void preparePresenter(){
        mCarDetailPresenter = new CarDetailPresenter(getApplicationContext()
                , getLoaderManager());
        mCarDetailPresenter.attachView(this);
    }

    private void prepareView(){
        if(getIntent().hasExtra(KeyContract.CAR)){
            Log.i(LOG_TAG, "Found a car");
            mCarDetailPresenter.updateCar((Car)getIntent().getParcelableExtra(KeyContract.CAR));
        } else if(mCarDetailPresenter.mCurrentCar == null
                || mCarDetailPresenter.mCurrentCar.getId() == -1) {
            Log.i(LOG_TAG, "No car found, checking db");
            mCarDetailPresenter.checkForCars();
        } else{

        }
    }

    @Override
    public void close(){
        finish();
    }

    @Override
    public void onDestroy(){
        mCarDetailPresenter.detachView();
        mCarDetailPresenter = null;
        super.onDestroy();
    }

    @Override
    public LineChartView getChart(){
        return mFuelChart;
    }

    private void launchCarDeleteAlert(){
        AlertDialog.Builder deleteAlertDialog = new AlertDialog.Builder(this);
        deleteAlertDialog.setMessage(R.string.car_detail_delete_car_dialog)
                .setCancelable(true)
                .setPositiveButton(R.string.car_detail_delete_dialog_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(getIntent().hasExtra(KeyContract.CAR)){
                            getIntent().removeExtra(KeyContract.CAR);
                        }
                        mCarDetailPresenter.deleteCar();
                    }
                })
                .setNegativeButton(R.string.car_detail_delete_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();

    }
}
