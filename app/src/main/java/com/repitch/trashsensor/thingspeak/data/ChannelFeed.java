package com.repitch.trashsensor.thingspeak.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ChannelFeed {

    @SerializedName("channel")
    @Expose
    private Channel channel;
    @SerializedName("feeds")
    @Expose
    private List<Feed> feeds = null;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

}
