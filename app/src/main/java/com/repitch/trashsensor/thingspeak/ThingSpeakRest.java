package com.repitch.trashsensor.thingspeak;

import com.repitch.trashsensor.thingspeak.data.ChannelFeed;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by repitch on 15.01.17.
 */
public interface ThingSpeakRest {

    /**
     * Update a Channel Feed
     * https://www.mathworks.com/help/thingspeak/update-channel-feed.html
     *
     * @return
     */
    @GET("update")
    Call<Void> update(); // TODO

    /**
     * Get a Channel Feed
     * https://www.mathworks.com/help/thingspeak/get-a-channel-feed.html
     */
    @GET("channels/{channel_id}/feeds")
    Call<ChannelFeed> getChannelFeed(@Path("channel_id") int channelId);
}
