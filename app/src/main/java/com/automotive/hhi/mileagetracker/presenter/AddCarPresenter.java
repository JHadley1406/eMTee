package com.automotive.hhi.mileagetracker.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.model.data.CarFactory;
import com.automotive.hhi.mileagetracker.model.database.DataContract;
import com.automotive.hhi.mileagetracker.view.interfaces.AddCarView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Josiah Hadley on 4/1/2016.
 */
public class AddCarPresenter implements Presenter<AddCarView> {

    private final String LOG_TAG = AddCarPresenter.class.getSimpleName();

    private AddCarView mAddCarView;
    private Context mContext;
    private boolean mIsEdit;
    private Car mCar;

    public AddCarPresenter(Car car, boolean edit, Context context){
        mCar = car;
        mIsEdit = edit;
        mContext = context;
    }

    @Override
    public void attachView(AddCarView view) {

        mAddCarView = view;
        if(mIsEdit) {
            mAddCarView.setFields();
        }
    }

    @Override
    public void detachView() {
        mAddCarView = null;
        mContext = null;
    }

    public Car getCar(){
        return mCar;
    }

    public boolean getIsEdit(){ return mIsEdit; }

    public void selectImage(){
        Intent selectImageIntent = new Intent();
        selectImageIntent.setType("image/*");
        selectImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        Log.i(LOG_TAG, "passing image intent");
        mAddCarView.selectImage(selectImageIntent);
    }

    public void saveImage(Uri imageUri){
        Picasso.with(mContext).load(imageUri).into(target);
    }

    public void insertCar(){
        mAddCarView.buildCar();
        if(mIsEdit){
            mContext.getContentResolver().update(DataContract.CarTable.CONTENT_URI
                    , CarFactory.toContentValues(mCar)
                    , DataContract.CarTable._ID + " = " + mCar.getId()
                    , null);
        } else {
            mContext.getContentResolver().insert(DataContract.CarTable.CONTENT_URI
                    , CarFactory.toContentValues(mCar));
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
        insertCar();
        return true;
    }


    private Target target = new Target(){

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            String fileName = "carimage"+ Calendar.getInstance().getTimeInMillis()+".jpg";
            FileOutputStream fileStream;

            mCar.setImage("file:"+mContext.getFilesDir()+"/"+fileName);
            try{
                fileStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileStream);
                fileStream.flush();
                fileStream.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "IOException: " + e.toString());
            }
            mAddCarView.setFields();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
}
