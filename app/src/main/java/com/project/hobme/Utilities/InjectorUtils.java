package com.project.hobme.Utilities;

import android.content.Context;
import android.util.Log;

import com.project.hobme.Data.AppRepository;
import com.project.hobme.Data.Database.AppDB;
import com.project.hobme.Data.Network.AppNetworkDataSource;
import com.project.hobme.UI.DetailActivityViewModelFactory;

public class InjectorUtils {

        public static AppRepository provideRepository(Context context) {
            Log.d("Check", "Inj - provideRepository");
        AppDB database = AppDB.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        AppNetworkDataSource networkDataSource =
                AppNetworkDataSource.getInstance(context.getApplicationContext(), executors);
        return AppRepository.getInstance(database.activityDao(), database.userDao(), networkDataSource, executors);
    }

    public static AppNetworkDataSource provideNetworkDataSource(Context context) {
        Log.d("Check", "Inj - provideNetworkDataSource");
        AppExecutors executors = AppExecutors.getInstance();
        return AppNetworkDataSource.getInstance(context.getApplicationContext(), executors);
    }

    public static DetailActivityViewModelFactory provideDetailViewModelFactory(Context context, int id) {
            Log.d("Check", "Inj - provideDetailViewModelFactory");
        AppRepository repository = provideRepository(context.getApplicationContext());
        return new DetailActivityViewModelFactory(repository, id);
    }


}
