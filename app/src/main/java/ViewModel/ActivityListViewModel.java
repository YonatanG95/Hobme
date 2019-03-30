package ViewModel;

import android.app.Application;

import java.util.List;

import Model.Activity;
import Model.ActivityType;
import Model.AppRepository;
import Utils.AppExecutors;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ActivityListViewModel extends ViewModel {

    private AppRepository repository;
    private AppExecutors mExecutor;

    public ActivityListViewModel(AppRepository repository, AppExecutors mExecutor){
        this.repository = repository;
        this.mExecutor = mExecutor;
    }

    public void insertActivity(Activity activity){
        mExecutor.diskIO().execute(() -> repository.insertActivity(activity));
    }

    public LiveData<List<Activity>> getActivitiesByType(int activityTypeId){
        return repository.getActivitiesByType(activityTypeId);
    }
}
