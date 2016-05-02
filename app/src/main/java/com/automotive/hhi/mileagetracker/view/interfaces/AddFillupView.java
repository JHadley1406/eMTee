package com.automotive.hhi.mileagetracker.view.interfaces;

import android.content.Intent;

/**
 * Created by Josiah Hadley on 4/1/2016.
 */
public interface AddFillupView extends MvpView{

    void setFields();

    void buildFillup();

    void launchActivity(Intent intent, int code);
}
