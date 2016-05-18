package com.automotive.hhi.mileagetracker.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.model.data.Fillup;
import com.automotive.hhi.mileagetracker.model.data.Station;
import com.automotive.hhi.mileagetracker.presenter.AddFillupPresenter;
import com.automotive.hhi.mileagetracker.view.interfaces.AddFillupView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;


public class AddFillupActivity extends AppCompatActivity implements AddFillupView
        , DatePickerDialog.OnDateSetListener {

    private final String LOG_TAG = AddFillupActivity.class.getSimpleName();

    @Bind(R.id.add_fillup_date_container)
    public LinearLayout mDateInputContainer;
    @Bind(R.id.add_fillup_date)
    public TextView mDate;
    @Bind(R.id.add_fillup_station)
    public TextView mStation;
    @Bind(R.id.add_fillup_station_address)
    public TextView mStationAddress;
    @Bind(R.id.add_fillup_fuel_amount)
    public EditText mFuelAmount;
    @Bind(R.id.add_fillup_price)
    public EditText mFuelPrice;
    @Bind(R.id.add_fillup_octane)
    public Spinner mOctane;
    @Bind(R.id.add_fillup_current_mileage)
    public EditText mMileage;
    @Bind(R.id.add_fillup_submit)
    public Button mAddFillup;
    @Bind(R.id.add_fillup_layout)
    public LinearLayout mInputContainer;
    @Bind(R.id.add_fillup_station_container)
    public LinearLayout mStationContainer;
    private AddFillupPresenter mAddFillupPresenter;
    private ArrayAdapter<CharSequence> mOctaneAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Car car;
        if(getIntent().hasExtra(KeyContract.CAR)){
            car = getIntent().getParcelableExtra(KeyContract.CAR);
        } else{
            car = new Car();
        }
        Station station;
        if(getIntent().hasExtra(KeyContract.STATION)){
            station = getIntent().getParcelableExtra(KeyContract.STATION);
        } else{
            station = new Station();
        }
        Fillup fillup;
        if(getIntent().hasExtra(KeyContract.FILLUP)){
            fillup = getIntent().getParcelableExtra(KeyContract.FILLUP);
        } else{
            fillup = new Fillup();
        }

        mAddFillupPresenter =
                new AddFillupPresenter(fillup
                        , car
                        , station
                        , getIntent().getBooleanExtra(KeyContract.IS_EDIT, false)
                        , getContext());
        setContentView(R.layout.activity_add_fillup);
        ButterKnife.bind(this);
        mOctaneAdapter = ArrayAdapter.createFromResource(getContext()
                , R.array.octane_array
                , R.layout.octane_spinner_item);
        mOctane.setAdapter(mOctaneAdapter);
        mAddFillupPresenter.attachView(this);
        if(!mAddFillupPresenter.getIsEdit()){
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            mDate.setText(sdf.format(new Date()));
        }
        setStationText();

    }

    @OnClick(R.id.add_fillup_submit)
    public void onSubmitButtonPressed() {
        mAddFillupPresenter.checkStation();
        if(mAddFillupPresenter.validateInput(mInputContainer)){
            setResult(RESULT_OK, mAddFillupPresenter.getReturnIntent());
            finish();
        }
    }

    @OnClick(R.id.add_fillup_station_container)
    public void addFillup(){
        mAddFillupPresenter.launchAddStation();
    }

    @OnClick(R.id.add_fillup_date_container)
    public void showDatePickerDialog(){
        mAddFillupPresenter
                .buildDatePickerFragment()
                .show(getSupportFragmentManager(), "date_picker");
    }

    @OnTextChanged(R.id.add_fillup_price)
    public void onPriceTextChanged(CharSequence s, int start, int before, int count){
        DecimalFormat decimal = new DecimalFormat("0.00");
        if(!s.toString().matches("^\\$(\\d+)(\\.\\d{2})?$")){
            String userInput = ""+s.toString().replaceAll("[^\\d]", "");
            if(userInput.length() > 0){
                Float in = Float.parseFloat(userInput);
                float percent = in/100;
                mFuelPrice.setText("$"+decimal.format(percent));
                mFuelPrice.setSelection(mFuelPrice.getText().length());
            }
        }
    }

    @OnTextChanged(R.id.add_fillup_current_mileage)
    public void onMileageTextChanged(CharSequence s, int start, int before, int count){
        if(!s.toString().matches("^\\d+\\.\\d+?$")
                && !s.toString().matches("^\\d+\\.?$")
                && !s.toString().matches("^\\d+\\.\\d+?&")
                && !s.toString().equals("")){
            mMileage.setError(getString(R.string.number_field_error));
            mAddFillup.setClickable(false);
        } else{
            mAddFillup.setClickable(true);
        }
    }

    @OnTextChanged(R.id.add_fillup_fuel_amount)
    public void onGallonsTextChanged(CharSequence s, int start, int before, int count){
        if(!s.toString().matches("^\\d+\\.\\d+?$")
                && !s.toString().matches("^\\d+\\.?$")
                && !s.toString().matches("^\\d+\\.\\d+?&")
                && !s.toString().equals("")){
            mFuelAmount.setError(getString(R.string.number_field_error));
            mAddFillup.setClickable(false);
        } else{
            mAddFillup.setClickable(true);
        }
    }

    @OnItemSelected(R.id.add_fillup_octane)
    public void onOctaneItemSelected(AdapterView<?> parent, View view, int pos, long id){
        switch (pos){
            case 0:{
                mAddFillupPresenter.getFillup().setOctane("reg");
                break;
            }
            case 1:{
                mAddFillupPresenter.getFillup().setOctane("mid");
                break;
            }
            case 2:{
                mAddFillupPresenter.getFillup().setOctane("pre");
                break;
            }
            case 3:{
                mAddFillupPresenter.getFillup().setOctane("diesel");
                break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putParcelable(KeyContract.CAR, mAddFillupPresenter.getCar());
        savedInstanceState.putParcelable(KeyContract.STATION, mAddFillupPresenter.getStation());
        savedInstanceState.putParcelable(KeyContract.FILLUP, mAddFillupPresenter.getFillup());
        savedInstanceState.putString(KeyContract.OCTANE, mOctane.getSelectedItem().toString());
        savedInstanceState.putString(KeyContract.FUEL_PRICE, mFuelPrice.getText().toString());
        savedInstanceState.putString(KeyContract.FUEL_AMOUNT, mFuelAmount.getText().toString());
        savedInstanceState.putString(KeyContract.MILES, mMileage.getText().toString());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        mAddFillupPresenter
                .setCar((Car) savedInstanceState.getParcelable(KeyContract.CAR));
        mAddFillupPresenter
                .setStation((Station) savedInstanceState.getParcelable(KeyContract.STATION));
        mAddFillupPresenter
                .setFillup((Fillup) savedInstanceState.getParcelable(KeyContract.FILLUP));
        mFuelAmount.setText(savedInstanceState.getString(KeyContract.FUEL_AMOUNT));
        mFuelPrice.setText(savedInstanceState.getString(KeyContract.FUEL_PRICE));
        mOctane.setSelection(mOctaneAdapter.getPosition(mAddFillupPresenter.getFillup().getReadableOctane()));
        mMileage.setText(savedInstanceState.getString(KeyContract.MILES));
    }

    @Override
    public void launchActivity(Intent getStationIntent, int code){
        startActivityForResult(getStationIntent, code);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onDestroy(){
        mAddFillupPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void setFields() {
        mDate.setText(mAddFillupPresenter.getFillup().getReadableDate());
        setStationText();
        if(mAddFillupPresenter.getFillup().getGallons() != 0){
            mFuelAmount.setText(String.format("%.2f", mAddFillupPresenter.getFillup().getGallons()));
        }
        if(mAddFillupPresenter.getFillup().getFuelCost() != 0) {
            mFuelPrice.setText(String.format("%.2f", mAddFillupPresenter.getFillup().getFuelCost()));
        }
        if(!mAddFillupPresenter.getFillup().getReadableOctane().equals("No Octane Selected")) {
            mOctane.setSelection(mOctaneAdapter.getPosition(mAddFillupPresenter.getFillup().getReadableOctane()));
        }
        if(mAddFillupPresenter.getFillup().getFillupMileage() != 9) {
            mMileage.setText(String.format("%.1f", mAddFillupPresenter.getFillup().getFillupMileage()));
        }
    }

    @Override
    public void buildFillup(){
        if(!mFuelAmount.getText().toString().equals("")) {
            mAddFillupPresenter.getFillup().setGallons(Double.parseDouble(mFuelAmount.getText().toString()));
        }
        if(!mFuelPrice.getText().toString().equals("")) {
            mAddFillupPresenter.getFillup().setFuelCost(Double.parseDouble(mFuelPrice.getText().toString().substring(1)));
        }
        if(!mMileage.getText().toString().equals("")) {
            mAddFillupPresenter.getFillup().setFillupMileage(Double.parseDouble(mMileage.getText().toString()));
        }
    }

    private void setStationText(){
        if(mAddFillupPresenter.getStation().getName() != null) {
            mStation.setText(mAddFillupPresenter.getStation().getName());
            mStationAddress.setText(mAddFillupPresenter.getStation().getAddress());
        } else{
            mStation.setText(R.string.add_fillup_no_station_text);
            mStationAddress.setText(R.string.add_fillup_no_station_click_here);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case KeyContract.GET_STATION_CODE: {
                if (resultCode == RESULT_OK) {
                    mAddFillupPresenter.setStation((Station) data.getParcelableExtra(KeyContract.STATION));
                    setStationText();
                }
                break;
            }
        }

    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mAddFillupPresenter.onDateSet(year, monthOfYear, dayOfMonth);
    }
}
