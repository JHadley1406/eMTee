package com.automotive.hhi.mileagetracker.model.retrofit;

import com.automotive.hhi.mileagetracker.model.data.Station;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Josiah Hadley on 4/23/2016.
 */
public interface GooglePlacesService {

   @GET("maps/api/place/nearbysearch/json")
    Call<List<Station>> listStations(@QueryMap Map<String, String> options);
}
