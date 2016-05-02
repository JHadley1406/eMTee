package com.automotive.hhi.mileagetracker.presenter;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.model.data.CarFactory;
import com.automotive.hhi.mileagetracker.model.database.DataContract;
import com.automotive.hhi.mileagetracker.view.widget.CarListWidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josiah Hadley on 4/28/2016.
 */
public class WidgetPresenter implements RemoteViewsService.RemoteViewsFactory{

    private final String LOG_TAG = WidgetPresenter.class.getSimpleName();

    private Context mContext;
    private CarListWidget mCarListWidget;
    private Cursor mCursor;
    private List<Car> mCarList;

    public WidgetPresenter(Context context, Intent intent){
        mContext = context;
        mCarListWidget = new CarListWidget();
        mCarList = new ArrayList<>();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        long identityToken = Binder.clearCallingIdentity();
        try{
            mCursor = mContext.getContentResolver().query(DataContract.CarTable.CONTENT_URI
                    , null
                    , null
                    , null
                    , null);
            if(mCursor != null && mCursor.moveToFirst()){
                while(!mCursor.isAfterLast()){
                    Car car = CarFactory.fromCursor(mCursor);
                    mCarList.add(car);
                    mCursor.moveToNext();
                }
            }
        } finally{
            if(mCursor != null){
                mCursor.close();
            }
            Binder.restoreCallingIdentity(identityToken);
        }
    }

    @Override
    public void onDestroy() {
        mContext = null;
    }

    @Override
    public int getCount() {
        return mCarList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews mView = new RemoteViews(mContext.getPackageName(), R.layout.car_widget_item);
        mView.setTextViewText(R.id.car_widget_item_year
                , String.format("%d", mCarList.get(position).getYear()));
        mView.setTextViewText(R.id.car_widget_item_make
                , mCarList.get(position).getMake());
        mView.setTextViewText(R.id.car_widget_item_model
                , mCarList.get(position).getModel());

        final Intent selectIntent = new Intent();
        selectIntent.setAction(KeyContract.CALL_CAR_DETAIL);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(KeyContract.CAR, mCarList.get(position));
        selectIntent.putExtras(bundle);
        mView.setOnClickFillInIntent(R.id.car_widget_container, selectIntent);

        return mView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public Car getCarAtPosition(int position){
        return mCarList.get(position);
    }
}
