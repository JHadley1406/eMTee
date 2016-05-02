package com.automotive.hhi.mileagetracker.model.retrofit;

import com.automotive.hhi.mileagetracker.model.data.MyGasFeedData;
import com.automotive.hhi.mileagetracker.model.data.Station;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Josiah Hadley on 4/25/2016.
 */
public interface MyGasFeedService {

    @GET("/stations/radius/{lat}/{lon}/{distance}/{fueltype}/distance/{apikey}.json")
    Call<MyGasFeedData> listStations(@Path("lat") double latitude
            , @Path("lon") double longitude
            , @Path("distance") int distance
            , @Path("fueltype") String fuelType
            , @Path("apikey") String apikey);
}
