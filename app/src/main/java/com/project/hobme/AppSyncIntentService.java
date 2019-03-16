package com.project.hobme;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class AppSyncIntentService extends IntentService {

    public AppSyncIntentService() {
        super("AppSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Check", "Intent service started");
        AppNetworkDataSource networkDataSource = InjectorUtils.provideNetworkDataSource(this.getApplicationContext());
        networkDataSource.fetchActivities();
    }
}
