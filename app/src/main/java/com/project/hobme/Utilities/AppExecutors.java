package com.project.hobme.Utilities;

import android.os.Handler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

public class AppExecutors
{
    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppExecutors sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        Log.d("Check", "AppEx - AppExecutors");
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static AppExecutors getInstance() {
        Log.d("Check", "AppEx - getInstance");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO()
    {
        Log.d("Check", "AppEx - diskIO");
        return diskIO;
    }

    public Executor mainThread()
    {
        Log.d("Check", "AppEx - mainThread()");
        return mainThread;
    }

    public Executor networkIO()
    {
        Log.d("Check", "AppEx - networkIO");
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command)
        {
            Log.d("Check", "MainThreadExecutor - execute");
            mainThreadHandler.post(command);
        }
    }
}
