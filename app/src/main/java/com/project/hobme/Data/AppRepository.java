package com.project.hobme.Data;

import android.util.Log;

import com.project.hobme.Utilities.AppExecutors;
import com.project.hobme.Data.Network.AppNetworkDataSource;
import com.project.hobme.Data.Database.ActivityDao;
import com.project.hobme.Data.Database.ActivityEntry;
import com.project.hobme.Data.Database.UserDao;

import androidx.lifecycle.LiveData;

public class AppRepository {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppRepository sInstance;
    private final ActivityDao mActivityDao;
    private final UserDao mUserDao;
    private final AppNetworkDataSource mNetworkDataSource;
    private final AppExecutors mExecutors;
    private boolean mInitialized = false;

    private AppRepository(ActivityDao activityDao, UserDao userDao,
                               AppNetworkDataSource appNetworkDataSource,
                               AppExecutors executors) {
        mActivityDao = activityDao;
        mUserDao = userDao;
        mNetworkDataSource = appNetworkDataSource;
        mExecutors = executors;
        LiveData<ActivityEntry[]> networkData = mNetworkDataSource.getCurrentActivities();
        networkData.observeForever(newActivitiesFromNetwork -> {
            mExecutors.diskIO().execute(() -> {
                // Insert our new weather data into Sunshine's database
                mActivityDao.bulkInsert(newActivitiesFromNetwork);
                Log.d("Check", "New values inserted");
            });
        });
    }

    public synchronized static AppRepository getInstance(
            ActivityDao activityDao, UserDao userDao, AppNetworkDataSource appNetworkDataSource,
            AppExecutors executors) {
        Log.d("Check", "Getting the repository");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppRepository(activityDao, userDao, appNetworkDataSource,
                        executors);
                Log.d("Check", "Made new repository");
            }
        }
        return sInstance;
    }

    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     */
    private synchronized void initializeData() {

        // Only perform initialization once per app lifetime. If initialization has already been
        // performed, we have nothing to do in this method.
        if (mInitialized) return;
        mInitialized = true;

        startFetchActivitiesService();
    }

    /**
     * Database related operations
     **/

    /**
     * Deletes old weather data because we don't need to keep multiple days' data
     */
    private void deleteOldData() {
        // TODO Finish this method when instructed
    }

    /**
     * Checks if there are enough days of future weather for the app to display all the needed data.
     *
     * @return Whether a fetch is needed
     */
    private boolean isFetchNeeded() {
        // TODO Finish this method when instructed
        return true;
    }

    /**
     * Network related operation
     */

    private void startFetchActivitiesService() {
        mNetworkDataSource.startFetchActivitiesService();
    }

    private LiveData<ActivityEntry> getActivityById(int id){
        initializeData();
        return mActivityDao.getActivityByID(id);
    }

}
