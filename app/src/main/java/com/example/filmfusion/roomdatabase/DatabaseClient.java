package com.example.filmfusion.roomdatabase;


import android.content.Context;

import androidx.room.Room;

import com.example.filmfusion.roomdatabase.db.AppDatabase;

public class DatabaseClient {

    private Context mCtx;
    private static DatabaseClient mInstance;

    private AppDatabase appDatabase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        appDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "MyToDos").build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
