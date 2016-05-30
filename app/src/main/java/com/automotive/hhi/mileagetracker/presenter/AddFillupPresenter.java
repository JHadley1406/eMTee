package com.automotive.hhi.mileagetracker.presenter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.model.data.CarFactory;
import com.automotive.hhi.mileagetracker.model.data.Fillup;
import com.automotive.hhi.mileagetracker.model.data.FillupFactory;
import com.automotive.hhi.mileagetracker.model.data.Station;
import com.automotive.hhi.mileagetracker.model.data.StationFactory;
import com.automotive.hhi.mileagetracker.model.database.DataContract;
import com.automotive.hhi.mileagetracker.view.interfaces.AddFillupView;
import com.automotive.hhi.mileagetracker.view.fragments.DatePickerFragment;
import com.automotive.hhi.mileagetracker.view.SelectStationActivity;

import java.util.Calendar;

/**
 * Created by Josiah Hadley on 4/1/2016.
 */
public class AddFillupPresenter implements Presenter<AddFillupView> {

    private final String LOG_TAG = AddFillupPresenter.class.getSimpleName();

    private AddFillupView mAddFillupView;
    private Context mContext;
    private Station mStation;
    private Car mCar;
    private Fillup mFillup;
    private Fillup mPrevFillup;
    private Fillup mNextFillup;
    private boolean hasPreviousFillup;
    private boolean hasNextFillup;
    private boolean mIsEdit;

    public AddFillupPresenter(Fillup fillup
            , Car car
            , Station station
            , boolean isEdit
            , Context context){
        mContext = context;
        mIsEdit = isEdit;
        mStation = station;
        mCar = car;
        mFillup = fillup;
        if(isEdit){
            if(fillup.getStationId() > 0){
                Cursor editStation = mContext.getContentResolver()
                        .query(DataContract.StationTable.CONTENT_URI, null
                        , DataContract.StationTable._ID + " = "
                        + mFillup.getStationId()
                        , null, null);
                if(editStation.moveToFirst()) {
                    mStation = StationFactory.fromCursor(editStation);
                }
            }
        }


    }

    @Override
    public void attachView(AddFillupView view) {
        mAddFillupView = view;
        if(mIsEdit){
            mAddFillupView.setFields();
        }
    }

    @Override
    public void detachView() {
        mAddFillupView = null;
        mContext = null;
    }

    public Car getCar(){ return mCar;}

    public void setCar(Car car){
        mCar = car;
    }

    public Fillup getFillup() { return mFillup; }

    public void setFillup(Fillup fillup){
        mFillup = fillup;
    }

    public Station getStation(){ return mStation; }

    public void setStation(Station station){
        mStation = station;
    }

    public boolean getIsEdit(){ return mIsEdit; }

    public Intent getReturnIntent(){
        Intent returnIntent = new Intent();
        Log.i(LOG_TAG, "Adding car to intent");
        returnIntent.putExtra(KeyContract.CAR, mCar);
        return returnIntent;
    }

    public void launchAddStation(){
        Intent addStationIntent = new Intent(mAddFillupView.getContext(), SelectStationActivity.class);

        mAddFillupView.launchActivity(addStationIntent, KeyContract.GET_STATION_CODE);
    }

    public void checkStation(){
        if(mStation.getId()==0 && mStation.getAddress() != null){
            Cursor fillupCheckCursor = mContext
                    .getContentResolver()
                    .query(DataContract.StationTable.CONTENT_URI
                            , null
                            , DataContract.StationTable.NAME
                            + " = '" + mStation.getName()
                            + "' AND " + DataContract.StationTable.ADDRESS
                            + " = '" + mStation.getAddress() + "'", null, null);

            if(fillupCheckCursor == null || !fillupCheckCursor.moveToFirst()){
                // If the station does not exist in the db, we add it, then add
                // the returned ID to the mStation object
                mStation.setId(ContentUris.parseId(mContext.getContentResolver()
                        .insert(DataContract.StationTable.CONTENT_URI
                                , StationFactory.toContentValues(mStation))));

            } else {
                // If the station does exist in the db, we just use the copy in the DB
                mStation = StationFactory.fromCursor(fillupCheckCursor);
            }
            if(fillupCheckCursor != null){
                fillupCheckCursor.close();
            }

        }
    }


    public boolean validateInput(LinearLayout container){
        for(int i=0; i < container.getChildCount(); i++){
            View v = container.getChildAt(i);
            if(v instanceof TextInputLayout){
                View et = ((TextInputLayout) v).getChildAt(0);
                if(TextUtils.isEmpty(((EditText) et).getText().toString())
                        || ((EditText)et).getText().toString() == ""){
                    ((EditText) et).setHintTextColor(Color.RED);
                    ((EditText) et).setError(mContext.getResources()
                            .getString(R.string.edit_text_error));
                    return false;
                }
            }
        }
        mAddFillupView.buildFillup();
        if(!mIsEdit){
            mFillup.setCarId(mCar.getId());
            mFillup.setDate(System.currentTimeMillis());
        }
        getPreviousFillup();
        getNextFillup();
        if(hasPreviousFillup
                && mFillup.getFillupMileage() <= mPrevFillup.getFillupMileage()
                && mFillup.getId() != mPrevFillup.getId()){
            mAddFillupView.popToast(mContext.getString(R.string.add_fillup_odo_too_low_warning)
                    + mPrevFillup.getFillupMileage());
            return false;
        }
        if(hasPreviousFillup
                && mFillup.getFillupMileage() - mPrevFillup.getFillupMileage() > 10000
                && mFillup.getId() != mPrevFillup.getId()){
            mAddFillupView.popToast(mContext.getString(R.string.add_fillup_odo_too_different));
            return false;
        }
        if (hasNextFillup
                && mFillup.getFillupMileage() >= mNextFillup.getFillupMileage()
                && mFillup.getId() != mNextFillup.getId()){
            mAddFillupView.popToast(mContext.getString(R.string.add_fillup_odo_too_high_warning)
                    + mNextFillup.getFillupMileage());
            return false;
        }
        if(mStation.getId() != 0 && mStation.getId() != mFillup.getStationId()){
            mFillup.setStationId(mStation.getId());
        }
        calculateFillupMpg();
        insertFillup();
        calculateAvgMpg();
        return true;
    }

    public void insertFillup(){
        if(mIsEdit){
            mContext.getContentResolver().update(DataContract.FillupTable.CONTENT_URI
                    , FillupFactory.toContentValues(mFillup)
                    , DataContract.FillupTable._ID + " = " + mFillup.getId()
                    , null);
        } else {
            mContext.getContentResolver().insert(DataContract.FillupTable.CONTENT_URI
                    , FillupFactory.toContentValues(mFillup));
        }
    }

    private void calculateFillupMpg(){
        //pull the fillup previous to this one, find the mileage difference and then divide the difference by the amount of fuel purchased at this fillup
        if(hasPreviousFillup){
            mFillup.setFillupMpg((mFillup.getFillupMileage() - mPrevFillup.getFillupMileage())
                    / mFillup.getGallons());

        } else{
            mFillup.setFillupMpg(0.00);
        }
    }

    private void calculateAvgMpg(){
        int fillupCount;
        double mpgTotal = 0;
        Cursor allFillups = mContext.getContentResolver()
                .query(DataContract.FillupTable.CONTENT_URI
                        , null
                        , DataContract.FillupTable.CAR + " = " + mCar.getId()

                        , null, "date ASC");
        if(allFillups != null && allFillups.moveToFirst()) {
            fillupCount = allFillups.getCount()-1;

            while (allFillups.moveToNext()) {
                mpgTotal += allFillups.getDouble(allFillups.getColumnIndexOrThrow(DataContract.FillupTable.MPG));
            }
            if(fillupCount > 0) {
                mCar.setAvgMpg(mpgTotal / fillupCount);
            }
            allFillups.close();
        }
        mContext.getContentResolver().update(DataContract.CarTable.CONTENT_URI, CarFactory.toContentValues(mCar), DataContract.CarTable._ID + " = " + mCar.getId(), null);

    }

    public DatePickerFragment buildDatePickerFragment(){
        Calendar cal = Calendar.getInstance();
        if(mIsEdit) {
            cal.setTimeInMillis(mFillup.getDate());
        }
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        Bundle data = new Bundle();
        data.putInt(KeyContract.YEAR, cal.get(Calendar.YEAR));
        data.putInt(KeyContract.MONTH, cal.get(Calendar.MONTH));
        data.putInt(KeyContract.DAY, cal.get(Calendar.DAY_OF_MONTH));
        datePickerFragment.setArguments(data);

        return datePickerFragment;
    }

    public void onDateSet(int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mFillup.setDate(cal.getTimeInMillis());
        mAddFillupView.setFields();
    }

    private void getPreviousFillup(){
        Log.i(LOG_TAG, "in get Previous Fillup");
        Cursor previousFillupCursor = mContext
                .getContentResolver()
                .query(DataContract.FillupTable.CONTENT_URI
                        , null
                        , DataContract.FillupTable.CAR + " = " + mCar.getId()
                        + " AND " + DataContract.FillupTable.DATE + " < " + mFillup.getDate()
                        , null
                        , " date DESC LIMIT 1");
        if(previousFillupCursor != null && previousFillupCursor.moveToFirst()){
            mPrevFillup = FillupFactory.fromCursor(previousFillupCursor);
            previousFillupCursor.close();
            Log.i(LOG_TAG, "Previous Fillup date: " + mPrevFillup.getDate());
            hasPreviousFillup = true;
        } else {
            hasPreviousFillup = false;
        }
    }

    private void getNextFillup(){
        Log.i(LOG_TAG, "in get Next Fillup");
        Cursor nextFillupCursor = mContext
                .getContentResolver()
                .query(DataContract.FillupTable.CONTENT_URI
                        , null
                        , DataContract.FillupTable.CAR + " = " + mCar.getId()
                        + " AND " + DataContract.FillupTable.DATE + " > " + mFillup.getDate()
                        , null
                        , " date ASC LIMIT 1");
        if(nextFillupCursor != null && nextFillupCursor.moveToFirst()){
            mNextFillup = FillupFactory.fromCursor(nextFillupCursor);
            nextFillupCursor.close();
            Log.i(LOG_TAG, "Next Fillup date: " + mNextFillup.getDate());
            hasNextFillup = true;
        } else {
            hasNextFillup = false;
        }
    }
}
