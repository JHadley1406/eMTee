package com.automotive.hhi.mileagetracker.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CarDetailActivity extends AppCompatActivity implements CarDetailView {

    private final String LOG_TAG = CarDetailActivity.class.getSimpleName();

    @Bind(R.id.car_detail_ad_view)
    public AdView mAdView;
    @Bind(R.id.car_detail_chart)
    public LineChartView mFuelChart;
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
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mFillupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        preparePresenter();

    }

    @OnClick(R.id.car_detail_add_fillup)
    public void onClick(){
        mCarDetailPresenter.launchAddFillup();

    }

    @OnClick(R.id.car_detail_edit_car)
    public void editCar(){
        mCarDetailPresenter.launchEditCar();
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
            case KeyContract.CREATE_FILLUP_CODE:{
                if(resultCode == RESULT_OK){
                    mCarDetailPresenter.updateCar((Car)data.getParcelableExtra(KeyContract.CAR));
                    mCarDetailPresenter.onLoaderReset(null);
                    mCarDetailPresenter.initChart(mFuelChart);
                }
                break;
            }
            case KeyContract.EDIT_FILLUP_CODE:{
                if(resultCode == RESULT_OK){
                    mCarDetailPresenter.updateCar((Car)data.getParcelableExtra(KeyContract.CAR));
                    mCarDetailPresenter.onLoaderReset(null);
                    mCarDetailPresenter.notifyChartDataChanged();
                }
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
            case android.R.id.home:{
                NavUtils.navigateUpTo(this, mCarDetailPresenter.returnToCarListIntent());
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showFillups(FillupAdapter fillups) {
        mFillupRecyclerView.setAdapter(fillups);
        fillups.notifyDataSetChanged();
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
                , getLoaderManager()
                , (Car)getIntent().getParcelableExtra(KeyContract.CAR));
        mCarDetailPresenter.attachView(this);
        mCarDetailPresenter.loadCar();
        mCarDetailPresenter.initChart(mFuelChart);
    }

    @Override
    public void close(){
        finish();
    }

    @Override
    public void onDestroy(){
        mCarDetailPresenter.detachView();
        super.onDestroy();
    }

    private void launchCarDeleteAlert(){
        AlertDialog.Builder deleteAlertDialog = new AlertDialog.Builder(this);
        deleteAlertDialog.setMessage(R.string.car_detail_delete_car_dialog)
                .setCancelable(true)
                .setPositiveButton(R.string.car_detail_delete_dialog_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
