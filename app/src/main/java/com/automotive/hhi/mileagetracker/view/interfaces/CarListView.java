package com.automotive.hhi.mileagetracker.view.interfaces;

import android.content.Intent;
import com.automotive.hhi.mileagetracker.adapters.CarAdapter;

/**
 * Created by Josiah Hadley on 3/24/2016.
 */
public interface CarListView extends MvpView {

    void showCars(CarAdapter cars);

    void addCar();

    void launchCarDetail(Intent carIntent);
}
