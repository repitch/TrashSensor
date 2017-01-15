package com.repitch.trashsensor.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.repitch.trashsensor.db.DBHelper;
import com.repitch.trashsensor.db.dao.ChannelDao;
import com.repitch.trashsensor.thingspeak.data.Channel;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by repitch on 15.01.17.
 */
public class ChannelRepository {

    private static final String TAG = ChannelRepository.class.getName();
    private ChannelDao channelDao;

    public ChannelRepository() {
        try {
            channelDao = DBHelper.getInstance().getChannelDao();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public void persistChannel(@NonNull Channel channel) {
        try {
            channelDao.createOrUpdate(channel);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public List<Channel> getChannels() {
        try {
            return channelDao.queryForAll();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

}
