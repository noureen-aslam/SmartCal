package com.example.smartcal;

import android.app.Application;
import com.example.smartcal.db.AppDatabase;

/**
 * App - Application class for SmartCal
 */
public class App extends Application {
    private static App instance;
    private static AppDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        db = AppDatabase.getInstance(getApplicationContext());
    }

    public static App getInstance() {
        return instance;
    }

    public static AppDatabase getDatabase() {
        return db;
    }
}
