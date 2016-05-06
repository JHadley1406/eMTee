package com.automotive.hhi.mileagetracker.presenter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.adapters.CarAdapter;
import com.automotive.hhi.mileagetracker.model.callbacks.ViewHolderOnClickListener;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.model.database.DataContract;
import com.automotive.hhi.mileagetracker.view.CarDetailActivity;
import com.automotive.hhi.mileagetracker.view.interfaces.CarListView;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public class CarListPresenter implements Presenter<CarListView>, ViewHolderOnClickListener<Car>, LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = CarListPresenter.class.getSimpleName();
    private final int LOADER_ID = 12345123;


    private CarListView mCarListView;
    private Context mContext;
    private CarAdapter mCarListAdapter;
    private LoaderManager mLoaderManager;

    public CarListPresenter(Context context
            , LoaderManager loaderManager){
        mContext = context;
        mLoaderManager = loaderManager;
        mCarListAdapter = new CarAdapter(mContext, null, this);
    }

    @Override
    public void attachView(CarListView view) {
        mCarListView = view;

        mLoaderManager.initLoader(LOADER_ID, null, this);

    }

    @Override
    public void detachView() {
        mCarListView = null;
    }

    @Override
    public void onClick(Car car) {
        Intent carDetailIntent = new Intent(mContext, CarDetailActivity.class);
        carDetailIntent.putExtra(KeyContract.CAR, car);
        mCarListView.launchCarDetail(carDetailIntent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext
                , DataContract.CarTable.CONTENT_URI
                , null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCarListAdapter.changeCursor(data);
        mCarListView.showCars(mCarListAdapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        restartLoader();
    }

    public void restartLoader(){
        mLoaderManager.restartLoader(LOADER_ID, null, this);
    }

    private void addCar(){
        mCarListView.addCar();
    }
}
