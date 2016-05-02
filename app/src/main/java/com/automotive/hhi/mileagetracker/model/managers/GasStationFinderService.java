package com.automotive.hhi.mileagetracker.model.managers;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.automotive.hhi.mileagetracker.KeyContract;
import com.automotive.hhi.mileagetracker.R;
import com.automotive.hhi.mileagetracker.model.data.GeoLocation;
import com.automotive.hhi.mileagetracker.model.data.MyGasFeedData;
import com.automotive.hhi.mileagetracker.model.data.Station;
import com.automotive.hhi.mileagetracker.model.retrofit.GeoLocationDeserializer;
import com.automotive.hhi.mileagetracker.model.retrofit.MyGasFeedService;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Josiah Hadley on 4/25/2016.
 */
public class GasStationFinderService extends IntentService{

    private final String LOG_TAG = GasStationFinderService.class.getSimpleName();

    private MyGasFeedService mMyGasFeedService;
    private Retrofit mRetrofit;
    private ArrayList<Station> mStationList;

    public GasStationFinderService(){
        super("GasStationFinderService");
    }

    public GasStationFinderService(String name){
        super(name);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mStationList = new ArrayList<>();

        Gson mGson = new GsonBuilder()
                .registerTypeAdapter(GeoLocation.class, new GeoLocationDeserializer())
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(KeyContract.MYGASFEEDURL)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();
        mMyGasFeedService = mRetrofit.create(MyGasFeedService.class);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(LOG_TAG, "in onHandleIntent");


        int distance = intent.getIntExtra(KeyContract.DISTANCE, 1);
        String fuelType = intent.getStringExtra(KeyContract.FUELTYPE);

        if(intent.hasExtra(KeyContract.SEARCH_ADDRESS)){
            getStations(getGeoFromAddress(intent.getStringExtra(KeyContract.SEARCH_ADDRESS))
                    , distance
                    , fuelType);
        } else if(intent.hasExtra(KeyContract.LATLNG)){
            LatLng latLng = intent.getParcelableExtra(KeyContract.LATLNG);
            Log.i(LOG_TAG, "Automagic location: Lat: " + latLng.latitude + " lng: " + latLng.longitude);
            getStations(latLng, distance, fuelType);
        }
    }

    private LatLng getGeoFromAddress(String address){
        try {
            Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> locations = geocoder.getFromLocationName(address, 1);

            return new LatLng(locations.get(0).getLatitude(), locations.get(0).getLongitude());
        } catch(IOException e){
            Log.e(LOG_TAG, "IOException Caught : " + e.toString());
        }
        return null;
    }

    private void getStations(LatLng latLng, int distance, String fuelType){
        Log.i(LOG_TAG, "In getStations: lat: " + latLng.latitude + " lng: " + latLng.longitude + " distance: " + distance + " FuelType: " + fuelType);
        Call<MyGasFeedData> stations = mMyGasFeedService.listStations(latLng.latitude
                , latLng.longitude
                , distance
                , fuelType
                , getResources().getString(R.string.mygasfeed_key));


        stations.enqueue(new Callback<MyGasFeedData>() {

            @Override
            public void onResponse(Call<MyGasFeedData> call, Response<MyGasFeedData> response) {
                mStationList = (ArrayList) response.body().getStations();
                // Sanitize the ID, since we're accidentally picking up the API's IDs
                for (Station station : mStationList) {
                    station.setId(0);
                }
                internalSendBroadcast();
            }

            @Override
            public void onFailure(Call<MyGasFeedData> call, Throwable t) {
                Log.i(LOG_TAG, "API Call Failed");
                Log.e(LOG_TAG, "Caught throwable: " + t.toString());
            }
        });
    }

    private void internalSendBroadcast(){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(KeyContract.STATION_LIST);
        broadcastIntent.putParcelableArrayListExtra(KeyContract.STATION_LIST, mStationList);
        Log.i(LOG_TAG, "sending broadcast intent, closing service");
        sendBroadcast(broadcastIntent);
    }


}
