package com.automotive.hhi.mileagetracker.view.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.view.AddFillupActivity;

/**
 * Created by Josiah Hadley on 4/26/2016.
 */
public class DatePickerFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        int year = getArguments().getInt(KeyContract.YEAR);
        int month = getArguments().getInt(KeyContract.MONTH);
        int day = getArguments().getInt(KeyContract.DAY);
        return new DatePickerDialog(getActivity()
                , R.style.AppTheme_Dialog, (AddFillupActivity)getActivity(), year, month, day);
    }

}
