package com.project.hobme;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {ActivityEntry.class, UserEntry.class}, version = 1)
@TypeConverters(DataConverter.class)
public abstract class AppDB extends RoomDatabase {

    public abstract ActivityDao activityDao();
    public abstract UserDao userDao();

    private static final String DATABASE_NAME = "applicationDB";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static volatile AppDB sInstance;

    public static AppDB getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, AppDB.DATABASE_NAME).build();
                }
            }
        }
        return sInstance;
    }
}
