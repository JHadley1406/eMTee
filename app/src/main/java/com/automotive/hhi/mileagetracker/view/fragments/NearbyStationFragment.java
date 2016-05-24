package com.automotive.hhi.mileagetracker.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.adapters.LocBasedStationAdapter;
import com.automotive.hhi.mileagetracker.model.callbacks.StationFragmentListener;
import com.automotive.hhi.mileagetracker.model.managers.LocationService;
import com.automotive.hhi.mileagetracker.presenter.NearbyStationPresenter;
import com.automotive.hhi.mileagetracker.view.interfaces.NearbyStationView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Josiah Hadley on 5/10/2016.
 */
public class NearbyStationFragment extends Fragment implements NearbyStationView {

    private final String LOG_TAG = NearbyStationFragment.class.getSimpleName();

    @Bind(R.id.nearby_station_no_connectivity_container)
    public RelativeLayout mConnectivityContainer;
    @Bind(R.id.select_station_address_input)
    public EditText mAddressSearch;
    @Bind(R.id.select_station_address_find_button)
    public Button mAddressSearchButton;
    @Bind(R.id.select_station_nearby_label)
    public TextView mNearbyLabel;
    @Bind(R.id.select_station_nearby_reset_button)
    public ImageButton mRefresh;
    @Bind(R.id.select_station_nearby_progress_bar)
    public ProgressBar mProgressBar;
    @Bind(R.id.select_station_nearby_rv)
    public RecyclerView mNearbyStationRV;

    private StationFragmentListener mStationFragmentListener;
    private NearbyStationPresenter mNearbyStationPresenter;
    private Context mContext;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        mContext = context;
        try{
            mStationFragmentListener = (StationFragmentListener) getActivity();
        } catch(ClassCastException e){
            Log.e(LOG_TAG, getActivity().toString() + " must implement StationFragmentListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_nearby_stations, container, false);
        ButterKnife.bind(this, view);
        mNearbyStationRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        mNearbyStationPresenter = new NearbyStationPresenter(getContext());
        mNearbyStationPresenter.attachView(this);
        checkPermission();
    }

    @Override
    public void onDestroyView(){
        mNearbyStationPresenter.detachView();
        super.onDestroy();
    }

    @OnClick(R.id.select_station_address_find_button)
    public void addressSearch(){
        if(!mNearbyStationPresenter.isOnline()){
            Toast.makeText(getContext(), R.string.no_internet_error, Toast.LENGTH_LONG).show();
        } else if(mAddressSearch.getText().length() > 0){
            showProgressBar();
            hideKeyboard();
            ((LocBasedStationAdapter)mNearbyStationRV.getAdapter()).clearStations();
            getContext()
                    .startService(mNearbyStationPresenter
                            .findStationsFromAddress(mAddressSearch.getText().toString()));
            mNearbyLabel.setText(R.string.select_station_search_station_text);
        } else{
            mAddressSearch.setError(getContext().getResources().getString(R.string.edit_text_error));
        }
    }

    @OnClick(R.id.select_station_nearby_reset_button)
    public void refreshSearch(){
        mNearbyLabel.setText(R.string.select_station_nearby_rv);
        ((LocBasedStationAdapter)mNearbyStationRV.getAdapter()).clearStations();
        checkPermission();
    }

    @Override
    public void showNearby(LocBasedStationAdapter stations) {
        mNearbyStationRV.setAdapter(stations);
    }


    @Override
    public void returnStation(Intent returnStationIntent){
        mStationFragmentListener.stationSelected(returnStationIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            showProgressBar();
            launchService(new Intent(getContext(), LocationService.class));

        }
    }

    @Override
    public void launchService(Intent intent){
        hideKeyboard();
        getContext().startService(intent);
    }

    @Override
    public void showRecyclerView() {
        mAddressSearch.setText("");
        mNearbyStationRV.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mConnectivityContainer.setVisibility(View.GONE);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext()
                , android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            showConnectivityIssues();
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}
                    , KeyContract.PERMISSION_REQUEST_CODE);
        } else{
            if(mNearbyStationPresenter.checkConnectivity()) {
                showProgressBar();
                launchService(new Intent(getContext(), LocationService.class));
            } else{
                showConnectivityIssues();
            }
        }
    }

    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
        mNearbyStationRV.setVisibility(View.INVISIBLE);
        mConnectivityContainer.setVisibility(View.INVISIBLE);
    }

    private void showConnectivityIssues(){
        mConnectivityContainer.setVisibility(View.VISIBLE);
        mAddressSearchButton.setClickable(false);
        mNearbyStationRV.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        hideKeyboard();
    }

    private void hideKeyboard(){
        View view = getActivity().getCurrentFocus();
        if(view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
