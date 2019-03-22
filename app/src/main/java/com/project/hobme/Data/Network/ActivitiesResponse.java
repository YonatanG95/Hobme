package com.project.hobme.Data.Network;

import android.util.Log;

import com.project.hobme.Data.Database.ActivityEntry;

import androidx.annotation.NonNull;

public class ActivitiesResponse {

    @NonNull
    private final ActivityEntry[] mActivities;

    public ActivitiesResponse(@NonNull final ActivityEntry[] activities) {
        Log.d("Check","AcRes - ActivitiesResponse");
        mActivities = activities;
    }

    public ActivityEntry[] getActivities() {
        Log.d("Check","AcRes - getActivities");
        return mActivities;
    }
}
