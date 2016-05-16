package com.automotive.hhi.mileagetracker.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.presenter.AddCarPresenter;
import com.automotive.hhi.mileagetracker.view.interfaces.AddCarView;
import com.squareup.picasso.Picasso;

import java.security.Key;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class AddCarActivity extends AppCompatActivity implements AddCarView {

    private final String LOG_TAG = AddCarActivity.class.getSimpleName();

    @Bind(R.id.add_car_image)
    public ImageView mImage;
    @Bind(R.id.add_car_name)
    public EditText mName;
    @Bind(R.id.add_car_make)
    public EditText mMake;
    @Bind(R.id.add_car_model)
    public EditText mModel;
    @Bind(R.id.add_car_year)
    public EditText mYear;
    @Bind(R.id.add_car_submit)
    public Button mAddCar;
    @Bind(R.id.add_car_input_container)
    public LinearLayout mInputContainer;
    private AddCarPresenter mAddCarPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Car car = new Car();
        if(getIntent().hasExtra(KeyContract.CAR)){
            car = getIntent().getParcelableExtra(KeyContract.CAR);
        }
        mAddCarPresenter = new AddCarPresenter(car
                , getIntent().getBooleanExtra(KeyContract.IS_EDIT, false)
                , getContext());
        setContentView(R.layout.activity_add_car);
        ButterKnife.bind(this);
        mAddCarPresenter.attachView(this);
        if(mAddCarPresenter.getIsEdit()){
            mAddCar.setText(getResources().getString(R.string.add_car_edit_button));
        }
    }


    @OnClick(R.id.add_car_submit)
    public void onButtonPressed() {
        if(mAddCarPresenter.validateInput(mInputContainer)){
            Intent returnIntent = new Intent();
            returnIntent.putExtra(KeyContract.CAR, mAddCarPresenter.getCar());
            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }

    @OnClick(R.id.add_car_image)
    public void onImageClick(){
        buildCar();
        mAddCarPresenter.selectImage();
    }

    @OnTextChanged(R.id.add_car_year)
    public void onYearTextChanged(CharSequence s, int start, int before, int count){

        if(!s.toString().matches("^(\\d{1,4})?$")){
            String userInput = "" + s.toString().replaceAll("[^\\d]", "");
            mYear.setText(userInput);
            mYear.setSelection(mYear.getText().length());
        }
    }


    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onDestroy() {
        mAddCarPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void setFields(){
        if(mAddCarPresenter.getCar().getImage() != null) {
            Picasso.with(getContext())
                    .load(Uri.parse(mAddCarPresenter.getCar().getImage()))
                    .into(mImage);
        }
        mName.setText(mAddCarPresenter.getCar().getName());
        mMake.setText(mAddCarPresenter.getCar().getMake());
        mModel.setText(mAddCarPresenter.getCar().getModel());
        if(mAddCarPresenter.getCar().getYear() != 0) {
            mYear.setText(String.format("%d", mAddCarPresenter.getCar().getYear()));
        }
    }

    @Override
    public void buildCar(){
        if(!mName.getText().toString().equals("")) {
            mAddCarPresenter.getCar().setName(mName.getText().toString());
        }
        if(!mMake.getText().toString().equals("")) {
            mAddCarPresenter.getCar().setMake(mMake.getText().toString());
        }
        if(!mModel.getText().toString().equals("")) {
            mAddCarPresenter.getCar().setModel(mModel.getText().toString());
        }
        if(!mYear.getText().toString().equals("")) {
            mAddCarPresenter.getCar().setYear(Integer.valueOf(mYear.getText().toString()));
        }
        if(mAddCarPresenter.getCar().getId() == 0){
            mAddCarPresenter.getCar().setAvgMpg(0.0);
        }
    }

    @Override
    public void selectImage(Intent selectImageIntent){
        startActivityForResult(Intent
                .createChooser(selectImageIntent, "Select Image"), KeyContract.SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == KeyContract.SELECT_IMAGE
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.getData() != null){
            mAddCarPresenter.saveImage(data.getData());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelable(KeyContract.CAR, mAddCarPresenter.getCar());
        savedInstanceState.putString(KeyContract.CAR_NAME, mName.getText().toString());
        savedInstanceState.putString(KeyContract.CAR_MAKE, mMake.getText().toString());
        savedInstanceState.putString(KeyContract.CAR_MODEL, mModel.getText().toString());
        savedInstanceState.putString(KeyContract.CAR_YEAR, mYear.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        mName.setText(savedInstanceState.getString(KeyContract.CAR_NAME));
        mMake.setText(savedInstanceState.getString(KeyContract.CAR_MAKE));
        mModel.setText(savedInstanceState.getString(KeyContract.CAR_MODEL));
        mYear.setText(savedInstanceState.getString(KeyContract.CAR_YEAR));
    }

}
