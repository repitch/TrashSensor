package com.repitch.trashsensor.thingspeak;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.repitch.trashsensor.thingspeak.data.ChannelFeed;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by repitch on 15.01.17.
 */
public class RetrofitManager {

    public static final String BASE_URL_THING_SPEAK = "https://api.thingspeak.com/";

    private static RetrofitManager sRFManager;
    private Retrofit retrofit;
    private ThingSpeakRest thingSpeakRest;

    public RetrofitManager() {
        Gson gson = new GsonBuilder().create();
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_THING_SPEAK)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        thingSpeakRest = retrofit.create(ThingSpeakRest.class);
    }

    public static RetrofitManager getInstance() {
        if (sRFManager == null) {
            sRFManager = new RetrofitManager();
        }
        return sRFManager;
    }

    public void getChannelFeed(int channelId, @NonNull Callback<ChannelFeed> callback) {
        thingSpeakRest.getChannelFeed(channelId).enqueue(callback);
    }

}
