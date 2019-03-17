package com.project.hobme.Data.Network;

import com.project.hobme.Data.Database.ActivityEntry;

import androidx.annotation.NonNull;

public class ActivitiesResponse {

    @NonNull
    private final ActivityEntry[] mWeatherForecast;

    public ActivitiesResponse(@NonNull final ActivityEntry[] weatherForecast) {
        mWeatherForecast = weatherForecast;
    }

    public ActivityEntry[] getWeatherForecast() {
        return mWeatherForecast;
    }
}
