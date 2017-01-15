package com.repitch.trashsensor;

import android.app.Application;

import com.repitch.trashsensor.db.DBHelper;

/**
 * Created by repitch on 15.01.17.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        DBHelper.releaseHelper();
        super.onTerminate();
    }
}
