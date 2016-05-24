package com.automotive.hhi.mileagetracker.view.williamchart;

import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;

import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.Fillup;
import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.Tooltip;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.BounceEase;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Josiah Hadley on 4/27/2016.
 */
public class FuelChart {

    private final String LOG_TAG = FuelChart.class.getSimpleName();

    private LineChartView mFuelChart;
    private Context mContext;
    private Runnable mChartAction;

    private Tooltip mTip;

    private ArrayList<Fillup> mFillups;

    private int mAvgMpg;

    private String[] mLabels = {"1", "2", "3", "4", "5", "6"};
    private float[] mValues = {1f, .8f, 1.2f, 1f, .9f, 1f};


    public FuelChart(LineChartView fuelChart, Context context, ArrayList<Fillup> fillups){
        mFuelChart = fuelChart;
        mContext = context;
        mFillups = fillups;
    }

    public void show(int avgMpg){
        if(mFillups.size()>1) {
            mAvgMpg = avgMpg;
        } else{
            mAvgMpg = 1;
        }

        buildTip();
        buildFuelChart();

    }

    private void buildTip(){
        mTip = new Tooltip(mContext, R.layout.fuel_chart_tooltip, R.id.tool_tip_value);
        mTip.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        mTip.setDimensions((int) Tools.fromDpToPx(100), (int) Tools.fromDpToPx(25));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

            mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f)).setDuration(200);

            mTip.setPivotX(Tools.fromDpToPx(65) / 2);
            mTip.setPivotY(Tools.fromDpToPx(25));
        }
    }

    private void buildFuelChart(){
        mFuelChart.reset();
        mFuelChart.setTooltips(mTip);
        mFuelChart.setBorderSpacing(Tools.fromDpToPx(15))
                .setAxisBorderValues(0, mAvgMpg*2)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setXLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(mContext.getResources().getColor(R.color.black))
                .setXAxis(false)
                .setYAxis(false)
                .setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        buildDataSet();
    }

    private void buildDataSet(){
        fillupsToVals();
        LineSet dataset = new LineSet(mLabels, mValues);
        dataset.setColor(mContext.getResources().getColor(R.color.colorPrimaryDark))
                .setThickness(4)
                .setFill(mContext.getResources().getColor(R.color.colorPrimaryDark))
                .setDotsColor(mContext.getResources().getColor(R.color.colorAccent));
        mFuelChart.addData(dataset);
        Animation animation = new Animation()
                .setEasing(new BounceEase())
                .setEndAction(mChartAction);
        mFuelChart.show(animation);

    }

    public void update(ArrayList<Fillup> fillups){
        mFillups = fillups;
        mFuelChart.dismissAllTooltips();
        buildFuelChart();
        mFuelChart.updateValues(0, mValues);
        mFuelChart.notifyDataUpdate();
    }

    private void fillupsToVals() {
        if (mFillups.size() > 1){
            mValues = new float[mFillups.size()];
            mLabels = new String[mFillups.size()];
            Iterator<Fillup> fillupIterator = mFillups.iterator();
            for (int i = 0; i < mFillups.size(); i++) {
                Fillup fillup = fillupIterator.next();
                mValues[i] = (float) fillup.getFillupMpg();
                mLabels[i] = fillup.getReadableDate();
            }
        }
    }

}
