package com.automotive.hhi.mileagetracker.model.managers;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.automotive.hhi.mileagetracker.presenter.WidgetPresenter;

/**
 * Created by Josiah Hadley on 4/28/2016.
 */
public class CarListWidgetService extends RemoteViewsService {
    @Override
    public WidgetPresenter onGetViewFactory(Intent intent) {

        return new WidgetPresenter(getApplicationContext(), intent);
    }
}
