package com.automotive.hhi.mileagetracker.view.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Car;
import com.automotive.hhi.mileagetracker.model.managers.CarListWidgetService;
import com.automotive.hhi.mileagetracker.presenter.WidgetPresenter;
import com.automotive.hhi.mileagetracker.view.CarDetailActivity;
import com.google.android.gms.common.api.PendingResult;

/**
 * Created by Josiah Hadley on 4/28/2016.
 */
public class CarListWidget extends AppWidgetProvider {

    private final String LOG_TAG = CarListWidget.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent){
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        if(intent.getAction().equals(KeyContract.WIDGET_CAR_SELECTED)){
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            Intent launchCarDetailIntent = new Intent(context, CarDetailActivity.class);
            launchCarDetailIntent.putExtra(KeyContract.CAR, intent.getParcelableExtra(KeyContract.CAR));
            launchCarDetailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(launchCarDetailIntent);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for(int i = 0; i < appWidgetIds.length; ++i){
            Intent intent = new Intent(context, CarListWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.car_widget_list);
            remoteViews.setRemoteAdapter(appWidgetIds[i], R.id.car_widget_list_view, intent);

            //remoteViews.setEmptyView(R.id.car_widget_list_view, R.id.car_widget_empty_view);

            Intent launchIntent = new Intent(context, CarListWidget.class);
            launchIntent.setAction(KeyContract.WIDGET_CAR_SELECTED);
            launchIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent launchPendingIntent = PendingIntent.getBroadcast(context, 0
                    , launchIntent
                    , PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.car_widget_list_view, launchPendingIntent);



            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
