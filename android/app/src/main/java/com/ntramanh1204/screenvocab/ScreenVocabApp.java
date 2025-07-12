package com.ntramanh1204.screenvocab;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;
import com.ntramanh1204.screenvocab.data.local.database.ScreenVocabDatabase;
import com.ntramanh1204.screenvocab.core.di.AppContainer;

public class ScreenVocabApp extends Application {
    private static ScreenVocabApp instance;
    private ScreenVocabDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(
                getApplicationContext(),
                ScreenVocabDatabase.class,
                "screenvocab_db"
        ).build();
        AppContainer.getInstance();
    }

    public static ScreenVocabApp getInstance() {
        return instance;
    }

    public ScreenVocabDatabase getDatabase() {
        return database;
    }
    public static Context getContext() {
        return instance.getApplicationContext();
    }
}