package com.automotive.hhi.mileagetracker.view.interfaces;

import android.content.Intent;

/**
 * Created by Josiah Hadley on 4/1/2016.
 */
public interface AddCarView extends MvpView {

    void setFields();

    void buildCar();

    void selectImage(Intent intent);

    void popToast(String message);
}
