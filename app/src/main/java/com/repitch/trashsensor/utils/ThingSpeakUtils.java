package com.repitch.trashsensor.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.repitch.trashsensor.R;

/**
 * Created by repitch on 15.01.17.
 */
public class ThingSpeakUtils {

    private ThingSpeakUtils() {
        // empty
    }

    public static String getTopic(@NonNull Context context, int channelId) {
        final String apiKey = context.getString(R.string.api_key);
        return context.getString(R.string.topic, channelId, apiKey);
    }
}
