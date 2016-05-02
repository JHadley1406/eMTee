package com.automotive.hhi.mileagetracker.model.retrofit;

import android.util.Log;

import com.automotive.hhi.mileagetracker.model.data.Station;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Josiah Hadley on 4/25/2016.
 */
public class StationsDeserializer implements JsonDeserializer<Station> {

    private final String LOG_TAG = StationsDeserializer.class.getSimpleName();

    @Override
    public Station deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Log.i(LOG_TAG, "Json: " + json);
        return context.deserialize(json, Station.class);
    }
}
