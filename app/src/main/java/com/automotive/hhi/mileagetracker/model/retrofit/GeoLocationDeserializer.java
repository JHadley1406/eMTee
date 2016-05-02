package com.automotive.hhi.mileagetracker.model.retrofit;

import android.util.Log;

import com.automotive.hhi.mileagetracker.model.data.GeoLocation;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Array;
import java.lang.reflect.Type;

/**
 * Created by Josiah Hadley on 4/25/2016.
 */
public class GeoLocationDeserializer implements JsonDeserializer<GeoLocation> {

    private final String LOG_TAG = GeoLocationDeserializer.class.getSimpleName();

    @Override
    public GeoLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        try{
            JsonElement geoLocation = json.getAsJsonObject().get("geoLocation");
        } catch(Exception e){
            Log.e(LOG_TAG, e.toString());
        } finally{
            return new GeoLocation(null, null, null, null, null, null, null, null);
        }

    }
}
