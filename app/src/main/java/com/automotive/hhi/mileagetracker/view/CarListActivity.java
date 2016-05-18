package com.automotive.hhi.mileagetracker.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.adapters.CarAdapter;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.presenter.CarListPresenter;
import com.automotive.hhi.mileagetracker.view.interfaces.CarListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CarListActivity extends AppCompatActivity implements CarListView {

    private final String LOG_TAG = CarListActivity.class.getSimpleName();

    @Bind(R.id.car_list_ad_view)
    public AdView mAdView;
    @Bind(R.id.car_list_fab)
    public FloatingActionButton mFab;
    @Bind(R.id.car_list_rv)
    public RecyclerView mCarRecyclerView;
    @Bind(R.id.car_list_toolbar)
    public Toolbar mToolbar;
    private CarListPresenter mCarListPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        preparePresenter();

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mCarRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCar();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        mCarListPresenter.restartLoader();
    }
    @Override
    protected void onDestroy(){
        mCarListPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showCars(CarAdapter cars) {
        mCarRecyclerView.setAdapter(cars);
        cars.notifyDataSetChanged();
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_car_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.car_list_menu_about:
                startActivity(new Intent(getContext(), AbouteMTeeActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void preparePresenter() {
        mCarListPresenter = new CarListPresenter(getApplicationContext(), getLoaderManager());
        mCarListPresenter.attachView(this);
    }

    @Override
    public void addCar(){
        Intent addCarIntent = new Intent(getContext(), AddCarActivity.class);
        addCarIntent.putExtra(KeyContract.CAR, new Car());
        addCarIntent.putExtra(KeyContract.IS_EDIT, false);
        startActivityForResult(addCarIntent, KeyContract.CREATE_CAR_CODE);
    }

    @Override
    public void launchCarDetail(Intent carDetailIntent){
        startActivity(carDetailIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode){
            case KeyContract.CREATE_CAR_CODE:{
                if(resultCode == RESULT_OK){
                    mCarListPresenter.onLoaderReset(null);
                }
            }
        }
    }

}
