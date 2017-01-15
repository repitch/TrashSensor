package com.repitch.trashsensor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.repitch.trashsensor.db.dao.ChannelDao;
import com.repitch.trashsensor.thingspeak.data.Channel;

import java.sql.SQLException;

/**
 * @author i.s.pyavkin
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "trashsensor.db";
    private static final int DB_VERSION = 1;

    private static DBHelper instance;

    private ChannelDao channelDao;

    public DBHelper(@NonNull Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DBHelper getInstance() {
        if (instance == null) {
            throw new IllegalStateException("You need to init(Context) DBHelper in App class");
        }
        return instance;
    }

    public static synchronized void releaseHelper() {
        OpenHelperManager.releaseHelper();
        instance = null;
    }

    public static synchronized void init(@NonNull Context context) {
        instance = OpenHelperManager.getHelper(context, DBHelper.class);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Channel.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Channel.class, true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
        channelDao = null;
    }

    public void clearTableChannel() throws SQLException {
        TableUtils.clearTable(connectionSource, Channel.class);
    }

    public ChannelDao getChannelDao() throws SQLException {
        if (channelDao == null) {
            channelDao = new ChannelDao(getConnectionSource());
        }
        return channelDao;
    }

}
