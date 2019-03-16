package com.project.hobme;

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
