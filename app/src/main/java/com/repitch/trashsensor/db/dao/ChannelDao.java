package com.repitch.trashsensor.db.dao;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.repitch.trashsensor.thingspeak.data.Channel;

import java.sql.SQLException;

/**
 * Created by repitch on 15.01.17.
 */
public class ChannelDao extends BaseDaoImpl<Channel, Integer> {

    public ChannelDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Channel.class);
    }

}
