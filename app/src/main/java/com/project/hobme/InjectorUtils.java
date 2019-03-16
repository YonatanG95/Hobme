package com.project.hobme;

import android.content.Context;

public class InjectorUtils {

        public static AppRepository provideRepository(Context context) {
        AppDB database = AppDB.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        AppNetworkDataSource networkDataSource =
                AppNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return AppRepository.getInstance(database.activityDao(), database.userDao(), networkDataSource, executors);
    }

    public static AppNetworkDataSource provideNetworkDataSource(Context context) {
        AppExecutors executors = AppExecutors.getInstance();
        return AppNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static DetailActivityViewModelFactory provideDetailViewModelFactory(Context context, int id) {
        AppRepository repository = provideRepository(context.getApplicationContext());
        return new DetailActivityViewModelFactory(repository, id);
    }


}
