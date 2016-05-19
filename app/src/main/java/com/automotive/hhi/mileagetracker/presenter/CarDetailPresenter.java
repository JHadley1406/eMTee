package com.automotive.hhi.mileagetracker.presenter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.adapters.FillupAdapter;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.model.data.CarFactory;
import com.automotive.hhi.mileagetracker.model.data.Fillup;
import com.automotive.hhi.mileagetracker.model.data.FillupFactory;
import com.automotive.hhi.mileagetracker.model.data.Station;
import com.automotive.hhi.mileagetracker.model.data.StationFactory;
import com.automotive.hhi.mileagetracker.model.database.DataContract;
import com.automotive.hhi.mileagetracker.view.AddCarActivity;
import com.automotive.hhi.mileagetracker.view.AddFillupActivity;
import com.automotive.hhi.mileagetracker.view.CarListActivity;
import com.automotive.hhi.mileagetracker.view.interfaces.CarDetailView;
import com.automotive.hhi.mileagetracker.view.williamchart.FuelChart;
import com.db.chart.view.LineChartView;

import java.util.ArrayList;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class CarDetailPresenter implements Presenter<CarDetailView>
        , ViewHolderOnClickListener<Fillup>
        , LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = CarDetailPresenter.class.getSimpleName();
    private final int DETAIL_FILLUPS_LOADER_ID = 543219876;

    private CarDetailView mCarDetailView;
    private Context mContext;
    private FillupAdapter mFillupAdapter;
    private LoaderManager mLoaderManager;
    private FuelChart mFuelChart;

    public Car mCurrentCar;

    public CarDetailPresenter(Context context, LoaderManager loaderManager){
        mContext = context;
        mLoaderManager = loaderManager;
        mFillupAdapter = new FillupAdapter(mContext, null, this);
        mCurrentCar = new Car();
    }


    @Override
    public void attachView(CarDetailView view) {
        mCarDetailView = view;
        mLoaderManager.initLoader(DETAIL_FILLUPS_LOADER_ID, null, this);
    }

    @Override
    public void detachView() {
        mCarDetailView = null;
    }

    @Override
    public void onClick(Fillup fillup){
        Intent editFillupIntent = new Intent(mCarDetailView.getContext(), AddFillupActivity.class);
        editFillupIntent.putExtra(KeyContract.CAR, mCurrentCar);
        editFillupIntent.putExtra(KeyContract.FILLUP, fillup);
        editFillupIntent.putExtra(KeyContract.IS_EDIT, true);
        Cursor stationCursor = mContext.getContentResolver().query(
                DataContract.StationTable.CONTENT_URI
                , null
                , DataContract.StationTable._ID + " = " + fillup.getStationId()
                , null, null);
        if(stationCursor.moveToFirst()) {
            editFillupIntent.putExtra(KeyContract.STATION, StationFactory.fromCursor(stationCursor));
        } else{
            editFillupIntent.putExtra(KeyContract.STATION, new Station());
        }
        mCarDetailView.launchActivity(editFillupIntent, KeyContract.EDIT_FILLUP_CODE);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String sortOrder = "date DESC";
        return new CursorLoader(mContext
                , DataContract.FillupTable.CONTENT_URI
                , null
                , DataContract.FillupTable.CAR + " = " + mCurrentCar.getId()
                , null, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mFillupAdapter.changeCursor(data);
        mCarDetailView.showFillups(mFillupAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mLoaderManager.restartLoader(DETAIL_FILLUPS_LOADER_ID, null, this);
    }

    private ArrayList<Fillup> getFillups(){
        ArrayList<Fillup> fillupList = new ArrayList<>();
        Cursor fillupCursor = mContext.getContentResolver()
                .query(DataContract.FillupTable.CONTENT_URI
                        , null, DataContract.FillupTable.CAR + " = " + mCurrentCar.getId()
                        , null, "date ASC");
        if(fillupCursor != null && fillupCursor.moveToFirst()){
            // We'll skip the first fillup since it doesn't have any MPG data in it
            while(fillupCursor.moveToNext()){
                fillupList.add(FillupFactory.fromCursor(fillupCursor));
            }
            fillupCursor.close();
        }
        return fillupList;
    }

    public void initChart(LineChartView fuelChartView){
        mFuelChart = new FuelChart(fuelChartView, mContext, getFillups());
        mFuelChart.show((int) Math.round(mCurrentCar.getAvgMpg()));
    }

    public void notifyChartDataChanged(){
        mFuelChart.update(getFillups());
    }

    public void launchEditCar(){

        Intent editCarIntent = new Intent(mCarDetailView.getContext(), AddCarActivity.class);
        editCarIntent.putExtra(KeyContract.CAR, mCurrentCar);
        editCarIntent.putExtra(KeyContract.IS_EDIT, true);
        mCarDetailView.launchActivity(editCarIntent, KeyContract.EDIT_CAR_CODE);
    }

    public void loadCar(){
        mCarDetailView.showCar(mCurrentCar);
    }

    public void updateCar(Car car){
        mCurrentCar = car;
        loadCar();
    }

    public void deleteCar(){
        mContext.getContentResolver()
                .delete(DataContract.CarTable.CONTENT_URI
                        , DataContract.CarTable._ID + " = " + mCurrentCar.getId()
                        , null);
        mContext.getContentResolver()
                .delete(DataContract.FillupTable.CONTENT_URI
                        , DataContract.FillupTable.CAR + " = " + mCurrentCar.getId()
                        , null);
        mCurrentCar = null;
        checkForCars();
    }

    public void launchAddFillup(){
        Intent addFillupIntent = new Intent(mCarDetailView.getContext(), AddFillupActivity.class);
        addFillupIntent.putExtra(KeyContract.CAR, mCurrentCar);
        addFillupIntent.putExtra(KeyContract.FILLUP, new Fillup());
        addFillupIntent.putExtra(KeyContract.STATION, new Station());
        addFillupIntent.putExtra(KeyContract.IS_EDIT, false);
        mCarDetailView.launchActivity(addFillupIntent, KeyContract.CREATE_FILLUP_CODE);
    }

    public Intent returnToCarListIntent(){
        Intent backIntent = new Intent(mContext, CarListActivity.class);
        return backIntent;
    }

    public void checkForCars(){
        Cursor carList = mContext.getContentResolver().query(DataContract.CarTable.CONTENT_URI, null, null, null, null);
        if(carList != null && carList.moveToFirst()){
            if(carList.getCount() == 1){
                updateCar(CarFactory.fromCursor(carList));

            } else {
                mCarDetailView.launchCarList();
            }
        } else{
            mCarDetailView.launchActivity(new Intent(mContext, AddCarActivity.class), KeyContract.CREATE_CAR_CODE);
        }
    }
}
