package com.project.hobme.Data.Network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.hobme.Utilities.AppExecutors;
import com.project.hobme.Data.Database.ActivityEntry;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AppNetworkDataSource {

    private final MutableLiveData<ActivityEntry[]> mDownloadedActivities;
    //private final MutableLiveData<UserEntry[]> mDownloadedUsers;
    private FirebaseDatabase database;
    private DatabaseReference ref;

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppNetworkDataSource sInstance;
    private final Context mContext;

    private final AppExecutors mExecutors;

    private AppNetworkDataSource(Context context, AppExecutors executors)
    {
        Log.d("Check", "Net DS - AppNetworkDataSource");
        mContext = context;
        mExecutors = executors;
        mDownloadedActivities = new MutableLiveData<ActivityEntry[]>();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("message");
    }

    /**
     * Get the singleton for this class
     */
    public static AppNetworkDataSource getInstance(Context context, AppExecutors executors) {
        Log.d("Check", "Net DS - getInstance");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppNetworkDataSource(context.getApplicationContext(), executors);
                Log.d("Check", "Made new network data source");
            }
        }
        return sInstance;
    }

    /**
     * Starts an intent service to fetch the weather.
     */
    public void startFetchActivitiesService() {
        Log.d("Check", "Net DS - startFetchActivitiesService");
        Intent intentToFetch = new Intent(mContext, AppSyncIntentService.class);
        mContext.startService(intentToFetch);

    }

//    /**
//     * Schedules a repeating job service which fetches the weather.
//     */
//    public void scheduleRecurringFetchWeatherSync() {
//        Driver driver = new GooglePlayDriver(mContext);
//        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
//
//        // Create the Job to periodically sync Sunshine
//        Job syncSunshineJob = dispatcher.newJobBuilder()
//                /* The Service that will be used to sync Sunshine's data */
//                .setService(SunshineFirebaseJobService.class)
//                /* Set the UNIQUE tag used to identify this Job */
//                .setTag(SUNSHINE_SYNC_TAG)
//                /*
//                 * Network constraints on which this Job should run. We choose to run on any
//                 * network, but you can also choose to run only on un-metered networks or when the
//                 * device is charging. It might be a good idea to include a preference for this,
//                 * as some users may not want to download any data on their mobile plan. ($$$)
//                 */
//                .setConstraints(Constraint.ON_ANY_NETWORK)
//                /*
//                 * setLifetime sets how long this job should persist. The options are to keep the
//                 * Job "forever" or to have it die the next time the device boots up.
//                 */
//                .setLifetime(Lifetime.FOREVER)
//                /*
//                 * We want Sunshine's weather data to stay up to date, so we tell this Job to recur.
//                 */
//                .setRecurring(true)
//                /*
//                 * We want the weather data to be synced every 3 to 4 hours. The first argument for
//                 * Trigger's static executionWindow method is the start of the time frame when the
//                 * sync should be performed. The second argument is the latest point in time at
//                 * which the data should be synced. Please note that this end time is not
//                 * guaranteed, but is more of a guideline for FirebaseJobDispatcher to go off of.
//                 */
//                .setTrigger(Trigger.executionWindow(
//                        SYNC_INTERVAL_SECONDS,
//                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
//                /*
//                 * If a Job with the tag with provided already exists, this new job will replace
//                 * the old one.
//                 */
//                .setReplaceCurrent(true)
//                /* Once the Job is ready, call the builder's build method to return the Job */
//                .build();
//
//        // Schedule the Job with the dispatcher
//        dispatcher.schedule(syncSunshineJob);
//        Log.d(LOG_TAG, "Job scheduled");
//    }

    /**
     * Gets the newest weather
     */
    void fetchActivities() {
        Log.d("Check", "Net DS - fetchActivities");
//        Log.d(LOG_TAG, "Fetch weather started");
//        mExecutors.networkIO().execute(() -> {
//            try {
//
//                // The getUrl method will return the URL that we need to get the forecast JSON for the
//                // weather. It will decide whether to create a URL based off of the latitude and
//                // longitude or off of a simple location as a String.
//
//                URL weatherRequestUrl = NetworkUtils.getUrl();
//
//                // Use the URL to retrieve the JSON
//                String jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(weatherRequestUrl);
//
                List<ActivityEntry> act = new ArrayList<ActivityEntry>();
                // Read from the database
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Check", "Net DS - onDataChange");
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                            act.add(dataSnapshot.getValue(ActivityEntry.class));
                        }

                        //Log.d("CheckFirebase", "Value is: " + value);
                        //ActivitiesResponse response = new AppResponseParsers().parse(value);
                        mDownloadedActivities.postValue(act.toArray(new ActivityEntry[act.size()]));
                        Log.d("CheckNew", "Posted");
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.d("Check", "Net DS - onCancelled");
                        // Failed to read value
                        Log.w("CheckFirebase", "Failed to read value.", error.toException());
                    }
                });
//                // Parse the JSON into a list of weather forecasts

//                Log.d(LOG_TAG, "JSON Parsing finished");
//
//
//                // As long as there are weather forecasts, update the LiveData storing the most recent
//                // weather forecasts. This will trigger observers of that LiveData, such as the
//                // SunshineRepository.
//                if (response != null && response.getWeatherForecast().length != 0) {
//                    Log.d(LOG_TAG, "JSON not null and has " + response.getWeatherForecast().length
//                            + " values");
//                    Log.d(LOG_TAG, String.format("First value is %1.0f and %1.0f",
//                            response.getWeatherForecast()[0].getMin(),
//                            response.getWeatherForecast()[0].getMax()));
//
//                    // TODO Finish this method when instructed.
//                    // Will eventually do something with the downloaded data

//                }
//            } catch (Exception e) {
//                // Server probably invalid
//                e.printStackTrace();
//            }
//        });
    }

    public LiveData<ActivityEntry[]> getCurrentActivities()
    {
        Log.d("Check", "Net DS - getCurrentActivities");
        List<ActivityEntry> act = new ArrayList<ActivityEntry>();
        // Read from the database
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("Check", "Net DS - onDataChange");
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    act.add(dataSnapshot.getValue(ActivityEntry.class));
                }

                //Log.d("CheckFirebase", "Value is: " + value);
                //ActivitiesResponse response = new AppResponseParsers().parse(value);
                mDownloadedActivities.postValue(act.toArray(new ActivityEntry[act.size()]));
                Log.d("CheckNew", "Posted");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d("Check", "Net DS - onCancelled");
                // Failed to read value
                Log.w("CheckFirebase", "Failed to read value.", error.toException());
            }
        });
        return mDownloadedActivities;
    }
}
